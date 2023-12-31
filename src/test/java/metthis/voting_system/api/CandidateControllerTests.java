package metthis.voting_system.api;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import metthis.voting_system.persons.Candidate;
import metthis.voting_system.persons.CandidateRepository;
import org.assertj.core.util.Arrays;
import org.json.JSONException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/test.properties")
// TestInstance.Lifecycle.PER_CLASS is selected to allow the use of @AfterAll on a non-static repository
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CandidateControllerTests {

    @Autowired
    private TestRestTemplate restTemplate;

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
    void getResponds200AndACandidateWhenItExists() throws IOException, JSONException {
        ResponseEntity<String> response = restTemplate
                .getForEntity("/candidates/140", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        String expectedJson = testUtils.fileToString("candidate1.json");
        JSONAssert.assertEquals(expectedJson, response.getBody(), true);
    }

    @Test
    void getResponds404WhenCandidateDoesNotExist() {
        ResponseEntity<String> response = restTemplate
                .getForEntity("/candidates/9999", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Could not find candidate 9999");
    }

    @Test
    void getResponds200AndWithAllCandidatesWhenTheRootIsCalled() throws  IOException, JSONException {
        ResponseEntity<String> response = restTemplate
                .getForEntity("/candidates", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        String expectedJson = testUtils.fileToString("candidateList2.json");
        JSONAssert.assertEquals(expectedJson, response.getBody(), true);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"newId99"})
    void putResponds201AndWithANewCandidateWhenSupplyingANonexistentCandidate(String idInBody)
            throws IOException, JSONException {
        Candidate newCandidate = new Candidate("Tiina Glass",
                                               idInBody,
                                               "1990-01-01",
                                               true,
                                               "2023-12-01");
        newCandidate.setLostThisElection(true);
        newCandidate.setWithdrawalDate("2023-12-15");

        HttpEntity<Candidate> request = new HttpEntity<>(newCandidate);
        ResponseEntity<String> createResponse = restTemplate
                .exchange("/candidates/newId99", HttpMethod.PUT, request, String.class);

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI locationOfNewCandidate = createResponse.getHeaders().getLocation();
        ResponseEntity<String> getResponse = restTemplate
                .getForEntity(locationOfNewCandidate, String.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(createResponse.getBody()).isEqualTo(getResponse.getBody());

        String expectedJson = testUtils.fileToString("candidateNew.json");
        JSONAssert.assertEquals(expectedJson, createResponse.getBody(), true);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"140"})
    void putResponds204AndUpdatedTheCandidateWhenSupplyingAnAlteredExistingCandidate(String idInBody)
            throws IOException, JSONException {
        Candidate updatedCandidate = new Candidate("Tris Bool",
                                               idInBody,
                                               "1985-03-15",
                                               false,
                                               "2023-11-15");
        updatedCandidate.setLostThisElection(true);
        updatedCandidate.setWithdrawalDate("2023-12-15");

        HttpEntity<Candidate> request = new HttpEntity<>(updatedCandidate);
        ResponseEntity<String> updateResponse = restTemplate
                .exchange("/candidates/140", HttpMethod.PUT, request, String.class);

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> getResponse = restTemplate
                .getForEntity("/candidates/140", String.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        String expectedJson = testUtils.fileToString("candidateUpdatedPut.json");
        JSONAssert.assertEquals(expectedJson, getResponse.getBody(), true);
    }


    @Test
    void putResponds400WhenSupplyingInvalidCandidateData() throws IOException {
        String invalidCandidate = testUtils.fileToString("candidateInvalid.json");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> request = new HttpEntity<>(invalidCandidate, headers);
        ResponseEntity<String> updateResponse = restTemplate
                .exchange("/candidates/140", HttpMethod.PUT, request, String.class);

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
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
    ) throws IOException, JSONException {
        Candidate updateData = new Candidate();
        updateData.setWithdrawalDate(newWithdrawalDate);
        updateData.setLostThisElection(newLostThisElection);

        HttpEntity<Candidate> request = new HttpEntity<>(updateData);
        ResponseEntity<String> patchResponse = restTemplate
                .exchange("/candidates/140", HttpMethod.PATCH, request, String.class);

        assertThat(patchResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<String> getResponse = restTemplate
                .getForEntity("/candidates/140", String.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(patchResponse.getBody()).isEqualTo(getResponse.getBody());

        String expectedSubsetJson = testUtils.fileToString("candidateUpdatedPatchPartial.json");
        String actualJson = patchResponse.getBody();

        JSONAssert.assertEquals(expectedSubsetJson, actualJson, false);

        DocumentContext documentContext = JsonPath.parse(actualJson);

        String withdrawalDate = documentContext.read("$.withdrawalDate");
        assertThat(withdrawalDate).isEqualTo(newWithdrawalDate);

        Boolean lostThisElection = documentContext.read("$.lostThisElection");
        assertThat(lostThisElection).isEqualTo(newLostThisElection == null ? false : newLostThisElection);
    }

    @Test
    void patchIgnoresAllUpdateFieldsOtherThanWithdrawalDateAndLostThisElection() throws IOException, JSONException {
        Candidate updateData = new Candidate("newName", "newId", "1800-01-01",
                                             false, "2020-01-01");

        updateData.setWithdrawalDate("2023-12-30");
        updateData.setLostThisElection(true);

        HttpEntity<Candidate> request = new HttpEntity<>(updateData);
        ResponseEntity<String> patchResponse = restTemplate
                .exchange("/candidates/140", HttpMethod.PATCH, request, String.class);

        assertThat(patchResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<String> getResponse = restTemplate
                .getForEntity("/candidates/140", String.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(patchResponse.getBody()).isEqualTo(getResponse.getBody());

        String expectedJson = testUtils.fileToString("candidateUpdatedPatchFull.json");
        JSONAssert.assertEquals(expectedJson, patchResponse.getBody(), true);
    }

    @Test
    void patchIgnoresFieldsWhichDoNotCorrespondToCandidateFields() throws IOException, JSONException {
        String invalidUpdate = testUtils.fileToString("invalidUpdate.json");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> request = new HttpEntity<>(invalidUpdate, headers);
        ResponseEntity<String> patchResponse = restTemplate
                .exchange("/candidates/140", HttpMethod.PATCH, request, String.class);

        assertThat(patchResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<String> getResponse = restTemplate
                .getForEntity("/candidates/140", String.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(patchResponse.getBody()).isEqualTo(getResponse.getBody());

        String expectedJson = testUtils.fileToString("candidate1.json");
        JSONAssert.assertEquals(expectedJson, patchResponse.getBody(), true);
    }

    @Test
    void patchResponds404WhenAttemptingToUpdateANonexistentCandidate() {
        Candidate updateData = new Candidate();
        updateData.setWithdrawalDate("2023-12-30");

        HttpEntity<Candidate> request = new HttpEntity<>(updateData);
        ResponseEntity<String> updateResponse = restTemplate
                .exchange("/candidates/9999", HttpMethod.PATCH, request, String.class);

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(updateResponse.getBody()).isEqualTo("Could not find candidate 9999");
    }
}
