package metthis.voting_system.api;

import metthis.voting_system.persons.Candidate;
import metthis.voting_system.persons.CandidateRepository;
import metthis.voting_system.voting.SingleCandidateSingleChoiceVote;
import metthis.voting_system.voting.Vote;
import metthis.voting_system.voting.VoteRepository;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.net.URI;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
// TestInstance.Lifecycle.PER_CLASS is selected to allow the use of @AfterAll on a non-static repository
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VoteControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private VoteRepository voteRepository;

    private Candidate[] candidates;

    private Vote[] votes;

    private TestUtils testUtils;

    @BeforeAll
    void initTestUtils() {
        testUtils = new TestUtils();
    }

    @BeforeAll
    void initCandidatesAndVotes() {
        candidates = Arrays.array(
                new Candidate("Chris Wool", "140",
                              "1985-03-15", true, "2023-11-15"),
                new Candidate("Mary Given", "100",
                              "1991-09-09", false, "2023-11-30"),
                new Candidate("Elizabeth Wong", "anID",
                              "2001-11-30", true, "2023-12-05"));

        for (Candidate candidate : candidates) {
            candidateRepository.save(candidate);
        }

        votes = Arrays.array(
                new SingleCandidateSingleChoiceVote(2, candidates[0]),
                new SingleCandidateSingleChoiceVote(1, null),
                new SingleCandidateSingleChoiceVote(3, candidates[0]),
                new SingleCandidateSingleChoiceVote(3, candidates[1]));
    }

    @BeforeEach
    void setUpRepository() {
        voteRepository.deleteAll();

        for (Vote vote : votes) {
            voteRepository.save(vote);
        }
    }

    @AfterAll
    void clearRepository() {
        voteRepository.deleteAll();
    }

    @Test
    void getResponds200AndAVoteWhenItExists() throws Exception {
        String expectedJson = testUtils.fileToString("vote1.json");

        String id = votes[0].getId().toString();

        mockMvc.perform(get("/votes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson, true));
    }

    @Test
    void getResponds404WhenVoteDoesNotExist() throws Exception {
        String nonexistentId = getNonexistentId();

        mockMvc.perform(get("/votes/{id}", nonexistentId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("Could not find vote " + nonexistentId));
    }

    private String getNonexistentId() {
        UUID uuid;
        do {
            uuid = UUID.randomUUID();
        } while(!uuidIsUnique(uuid));
        return uuid.toString();
    }

    private boolean uuidIsUnique(UUID uuid) {
        for (Vote vote : voteRepository.findAll()) {
            if (vote.getId().equals(uuid)) {
                return false;
            }
        }
        return true;
    }

    @RepeatedTest(10)
    void getResponds200AndWithAllVotesSortedByVotingRoundAndChoiceIdAscendingWhenTheRootIsCalled() throws Exception {
        String expectedJson = testUtils.fileToString("voteList2.json");

        mockMvc.perform(get("/votes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson, true));
    }

    @Test
    void postResponds201AndWithANewVoteWhenSupplyingAVoteWithASavedCandidateAsChoice()
            throws Exception {
        String json = testUtils.fileToString("voteNewSavedCandidate.json");

        MvcResult putResponse = mockMvc.perform(post("/votes")
                                .header("Content-Type", "application/json")
                                .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(json, true))
                .andReturn();

        URI location = URI.create(putResponse.getResponse().getHeader("Location"));

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(json, true));
    }

    @Test
    void postResponds404WhenSupplyingAVoteWithAnUnsavedCandidateAsChoice() throws Exception {
        String invalidVote = testUtils.fileToString("voteNewUnsavedCandidate.json");

        mockMvc.perform(post("/votes")
                                .header("Content-Type", "application/json")
                                .content(invalidVote))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Could not find candidate unsavedId"));
    }
    @Test
    void postResponds400WhenSupplyingInvalidVoteData() throws Exception {
        String invalidVote = testUtils.fileToString("voteInvalid.json");

        mockMvc.perform(post("/votes")
                                .header("Content-Type", "application/json")
                                .content(invalidVote))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteResponds204WhenRootIsCalledAndCallingGetOnRootReturnsAnEmptyArray() throws Exception {
        String expectedJson = testUtils.fileToString("emptyArray.json");

        mockMvc.perform(delete("/votes"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/votes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson, true));
    }
}
