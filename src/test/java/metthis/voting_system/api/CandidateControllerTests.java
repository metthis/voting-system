package metthis.voting_system.api;

import metthis.voting_system.persons.Candidate;
import metthis.voting_system.persons.CandidateRepository;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
public class CandidateControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CandidateRepository candidateRepository;

    private Candidate[] candidates;

    private TestUtils testUtils;

    @BeforeAll
    void initTestUtils() {
        testUtils = new TestUtils();
    }

    @BeforeAll
    void initCandidates() {
        candidates = Arrays.array(
                new Candidate("Chris Wool", "140",
                              "1985-03-15", true, "2023-11-15"),
                new Candidate("Mary Given", "abc789",
                              "1991-09-09", false, "2023-11-30"),
                new Candidate("Elizabeth Wong", "anID",
                              "2001-11-30", true, "2023-12-05"));

        candidates[1].setWithdrawalDate("2023-12-02");
        candidates[2].setLostThisElection(true);
    }

    @BeforeEach
    void setUpRepository() {
        candidateRepository.deleteAll();

        for (Candidate candidate : candidates) {
            candidateRepository.save(candidate);
        }
    }

    @AfterAll
    void clearRepository() {
        candidateRepository.deleteAll();
    }

    @Test
    void getResponds200AndACandidateWhenItExists() throws Exception {
        String expectedJson = testUtils.fileToString("candidate1.json");

        mockMvc.perform(get("/candidates/{id}", "140"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson, true));
    }

    @Test
    void getResponds404WhenCandidateDoesNotExist() throws Exception {
        mockMvc.perform(get("/candidates/{id}", "9999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("Could not find candidate 9999"));
    }

    @Test
    void getResponds200AndWithAllCandidatesWhenTheRootIsCalled() throws  Exception {
        String expectedJson = testUtils.fileToString("candidateList2.json");

        mockMvc.perform(get("/candidates"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson, true));
    }

    @RepeatedTest(2)
    void putResponds201AndWithANewCandidateWhenSupplyingANonexistentCandidate(RepetitionInfo repetitionInfo)
            throws Exception {
        int currentRepetition = repetitionInfo.getCurrentRepetition();
        String jsonWithId = testUtils.fileToString("candidateNewWithId.json");
        String jsonWithoutId = testUtils.fileToString("candidateNewWithoutId.json");

        MvcResult putResponse = mockMvc.perform(put("/candidates/{id}", "newId99")
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
    void putResponds204AndUpdatedTheCandidateWhenSupplyingAnAlteredExistingCandidate(RepetitionInfo repetitionInfo)
            throws Exception {
        int currentRepetition = repetitionInfo.getCurrentRepetition();
        String jsonWithId = testUtils.fileToString("candidateUpdatedPutWithId.json");
        String jsonWithoutId = testUtils.fileToString("candidateUpdatedPutWithoutId.json");

        mockMvc.perform(put("/candidates/{id}", "140")
                                                        .header("Content-Type", "application/json")
                                                        .content(currentRepetition == 1 ? jsonWithId : jsonWithoutId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/candidates/{id}", "140"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonWithId, true));
    }


    @Test
    void putResponds400WhenSupplyingInvalidCandidateData() throws Exception {
        String invalidCandidate = testUtils.fileToString("candidateInvalid.json");

        mockMvc.perform(put("/candidates/{id}", "140")
                                .header("Content-Type", "application/json")
                                .content(invalidCandidate))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @CsvSource(nullValues = "NULL", textBlock = """
            2023-12-30,         NULL
            NULL,               true
            2023-12-30,         true
            """)
    void patchResponds200AndWithUpdatedCandidateWhenAnExistingCandidateIsSuccessfullyUpdatedWithWithdrawalDateOrLostThisElectionOrBoth(
            String newWithdrawalDate,
            Boolean newLostThisElection
    ) throws Exception {
        String expectedWithdrawalDate = newWithdrawalDate;
        Boolean expectedLostThisElection = newLostThisElection == null ? false : newLostThisElection;

        String updateJson = testUtils.fileToString("candidateUpdate.json");
        updateJson = testUtils.modifyJsonField(updateJson, "withdrawalDate", newWithdrawalDate);
        updateJson = testUtils.modifyJsonField(updateJson, "lostThisElection", newLostThisElection);

        String expectedJson = testUtils.fileToString("candidateUpdatedPatchPartial.json");
        expectedJson = testUtils.modifyJsonField(expectedJson, "withdrawalDate", expectedWithdrawalDate);
        expectedJson = testUtils.modifyJsonField(expectedJson, "lostThisElection", expectedLostThisElection);

        mockMvc.perform(patch("/candidates/{id}", "140")
                                .header("Content-Type", "application/json")
                                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson, true));

        mockMvc.perform(get("/candidates/{id}", "140"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson, true));
    }

    @Test
    void patchIgnoresAllUpdateFieldsOtherThanWithdrawalDateAndLostThisElection() throws Exception {
        String updateJson = testUtils.fileToString("candidateUpdateMostlyIgnored.json");
        String expectedJson = testUtils.fileToString("candidateUpdatedPatchFull.json");

        mockMvc.perform(patch("/candidates/{id}", "140")
                                .header("Content-Type", "application/json")
                                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson, true));

        mockMvc.perform(get("/candidates/{id}", "140"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson, true));
    }

    @Test
    void patchIgnoresFieldsWhichDoNotCorrespondToCandidateFields() throws Exception {
        String updateJson = testUtils.fileToString("candidateUpdateInvalid.json");
        String expectedJson = testUtils.fileToString("candidate1.json");

        mockMvc.perform(patch("/candidates/{id}", "140")
                                .header("Content-Type", "application/json")
                                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson, true));

        mockMvc.perform(get("/candidates/{id}", "140"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson, true));
    }

    @Test
    void patchResponds404WhenAttemptingToUpdateANonexistentCandidate() throws Exception {
        String updateJson = testUtils.fileToString("candidateUpdateInvalid.json");

        mockMvc.perform(patch("/candidates/{id}", "9999")
                                .header("Content-Type", "application/json")
                                .content(updateJson))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("Could not find candidate 9999"));
    }
}
