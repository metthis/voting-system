package metthis.voting_system.api;

import metthis.voting_system.persons.Voter;
import metthis.voting_system.persons.VoterRepository;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.net.URI;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
// TestInstance.Lifecycle.PER_CLASS is selected to allow the use of @AfterAll on a non-static repository
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VoterControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VoterRepository voterRepository;

    private Voter[] voters;

    private TestUtils testUtils;

    @BeforeAll
    void initTestUtils() {
        testUtils = new TestUtils();
    }

    @BeforeAll
    void initVoters() {
        voters = Arrays.array(
                new Voter("Chris Wool", "140","1985-03-15", true),
                new Voter("Mary Given", "abc789","1991-09-09", false),
                new Voter("Elizabeth Wong", "anID","2001-11-30", true));

        voters[1].setLastVotedRound(1);
        voters[2].setLastVotedRound(3);
    }

    @BeforeEach
    void setUpRepository() {
        voterRepository.deleteAll();

        for (Voter voter : voters) {
            voterRepository.save(voter);
        }
    }

    @AfterAll
    void clearRepository() {
        voterRepository.deleteAll();
    }

    @Test
    void getResponds200AndAVoterWhenItExists() throws Exception {
        String expectedJson = testUtils.fileToString("voter1.json");

        mockMvc.perform(get("/voters/{id}", "140"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson, true));
    }

    @Test
    void getResponds404WhenVoterDoesNotExist() throws Exception {
        mockMvc.perform(get("/voters/{id}", "9999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("Could not find voter 9999"));
    }

    @Test
    void getResponds200AndWithAllVotersWhenTheRootIsCalled() throws  Exception {
        String expectedJson = testUtils.fileToString("voterList2.json");

        mockMvc.perform(get("/voters"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson, true));
    }

    @RepeatedTest(2)
    void putResponds201AndWithANewVoterWhenSupplyingANonexistentVoter(RepetitionInfo repetitionInfo)
            throws Exception {
        int currentRepetition = repetitionInfo.getCurrentRepetition();
        String jsonWithId = testUtils.fileToString("voterNewWithId.json");
        String jsonWithoutId = testUtils.fileToString("voterNewWithoutId.json");

        MvcResult putResponse = mockMvc.perform(put("/voters/{id}", "newId99")
                                                        .header("Content-Type", "application/json")
                                                        .content(currentRepetition == 1 ? jsonWithId : jsonWithoutId))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonWithId, true))
                .andReturn();

        URI location = URI.create(putResponse.getResponse().getHeader("Location"));

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonWithId, true));
    }

    @RepeatedTest(2)
    void putResponds204AndUpdatedTheVoterWhenSupplyingAnAlteredExistingVoter(RepetitionInfo repetitionInfo)
            throws Exception {
        int currentRepetition = repetitionInfo.getCurrentRepetition();
        String jsonWithId = testUtils.fileToString("voterUpdatedPutWithId.json");
        String jsonWithoutId = testUtils.fileToString("voterUpdatedPutWithoutId.json");

        mockMvc.perform(put("/voters/{id}", "140")
                                .header("Content-Type", "application/json")
                                .content(currentRepetition == 1 ? jsonWithId : jsonWithoutId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/voters/{id}", "140"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonWithId, true));
    }


    @Test
    void putResponds400WhenSupplyingInvalidVoterData() throws Exception {
        String invalidVoter = testUtils.fileToString("voterInvalid.json");

        mockMvc.perform(put("/voters/{id}", "140")
                                .header("Content-Type", "application/json")
                                .content(invalidVoter))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(ints = {2})
    void patchResponds200AndWithUpdatedVoterWhenAnExistingVoterIsUpdatedWithLastVotedRoundOrWhenANullUpdateIsIgnored(
            Integer newLastVotedRound
    ) throws Exception {
        Integer expectedLastVoterRound = newLastVotedRound == null ? 0 : newLastVotedRound;

        String updateJson = testUtils.fileToString("voterUpdate.json");
        updateJson = testUtils.modifyJsonField(updateJson, "lastVotedRound", newLastVotedRound);

        String expectedJson = testUtils.fileToString("voterUpdatedPatchPartial.json");
        expectedJson = testUtils.modifyJsonField(expectedJson, "lastVotedRound", expectedLastVoterRound);

        mockMvc.perform(patch("/voters/{id}", "140")
                                .header("Content-Type", "application/json")
                                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson, true));

        mockMvc.perform(get("/voters/{id}", "140"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson, true));
    }

    @Test
    void patchIgnoresAllUpdateFieldsOtherThanLastVotedRound() throws Exception {
        String updateJson = testUtils.fileToString("voterUpdateMostlyIgnored.json");
        String expectedJson = testUtils.fileToString("voterUpdatedPatchFull.json");

        mockMvc.perform(patch("/voters/{id}", "140")
                                .header("Content-Type", "application/json")
                                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson, true));

        mockMvc.perform(get("/voters/{id}", "140"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson, true));
    }

    @Test
    void patchIgnoresFieldsWhichDoNotCorrespondToVoterFields() throws Exception {
        String updateJson = testUtils.fileToString("genericUpdateInvalid.json");
        String expectedJson = testUtils.fileToString("voter1.json");

        mockMvc.perform(patch("/voters/{id}", "140")
                                .header("Content-Type", "application/json")
                                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson, true));

        mockMvc.perform(get("/voters/{id}", "140"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson, true));
    }

    @Test
    void patchResponds404WhenAttemptingToUpdateANonexistentVoter() throws Exception {
        String updateJson = testUtils.fileToString("voterUpdate.json");

        mockMvc.perform(patch("/voters/{id}", "9999")
                                .header("Content-Type", "application/json")
                                .content(updateJson))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("Could not find voter 9999"));
    }

    @Test
    void deleteResponds204WhenRootIsCalledAndCallingGetOnRootReturnsAnEmptyArray() throws Exception {
        String expectedJson = testUtils.fileToString("emptyArray.json");

        mockMvc.perform(delete("/voters"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/voters"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson, true));
    }
}
