package metthis.voting_system.api;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import metthis.voting_system.persons.Candidate;
import metthis.voting_system.persons.CandidateRepository;
import metthis.voting_system.persons.Voter;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.net.URI;
import java.util.List;

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
    void getResponds200AndACandidateWhenItExists() {
        ResponseEntity<String> response = restTemplate
                .getForEntity("/candidates/140", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        String id = documentContext.read("$.id");
        assertThat(id).isEqualTo("140");

        String name = documentContext.read("$.name");
        assertThat(name).isEqualTo("Chris Wool");

        String dateOfBirth = documentContext.read("$.dateOfBirth");
        assertThat(dateOfBirth).isEqualTo("1985-03-15");

        Boolean isCitizen = documentContext.read("$.isCitizen");
        assertThat(isCitizen).isEqualTo(true);

        String registrationDate = documentContext.read("$.registrationDate");
        assertThat(registrationDate).isEqualTo("2023-11-15");

        String withdrawalDate = documentContext.read("$.withdrawalDate");
        assertThat(withdrawalDate).isEqualTo(null);

        Boolean lostThisElection = documentContext.read("$.lostThisElection");
        assertThat(lostThisElection).isEqualTo(false);
    }

    @Test
    void getResponds404WhenCandidateDoesNotExist() {
        ResponseEntity<String> response = restTemplate
                .getForEntity("/candidates/999", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Could not find candidate 999");
    }

    @Test
    void getResponds200AndWithAllCandidatesWhenTheRootIsCalled() {
        ResponseEntity<String> response = restTemplate
                .getForEntity("/candidates", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());

        int candidateCount = documentContext.read("$.length()");
        assertThat(candidateCount).isEqualTo(3);

        List<String> ids = documentContext.read("$..id");
        assertThat(ids).containsExactlyInAnyOrder("140", "abc789", "anID");

        List<String> names = documentContext.read("$..name");
        assertThat(names).containsExactlyInAnyOrder("Chris Wool", "Mary Given", "Elizabeth Wong");

        List<String> datesOfBirth = documentContext.read("$..dateOfBirth");
        assertThat(datesOfBirth).containsExactlyInAnyOrder("1985-03-15", "1991-09-09", "2001-11-30");

        List<Boolean> isCitizens = documentContext.read("$..isCitizen");
        assertThat(isCitizens).containsExactlyInAnyOrder(true, false, true);

        List<String> registrationDates = documentContext.read("$..registrationDate");
        assertThat(registrationDates).containsExactlyInAnyOrder("2023-11-15", "2023-11-30", "2023-12-05");

        List<String> withdrawalDates = documentContext.read("$..withdrawalDate");
        assertThat(withdrawalDates).containsExactlyInAnyOrder(null, "2023-12-02", null);

        List<Boolean> lostThisElections = documentContext.read("$..lostThisElection");
        assertThat(lostThisElections).containsExactlyInAnyOrder(false, false, true);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"newId99"})
    void putResponds201AndWithANewCandidateWhenSupplyingANonexistentCandidate(String idInBody) {
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

        DocumentContext documentContext = JsonPath.parse(createResponse.getBody());

        String id = documentContext.read("$.id");
        assertThat(id).isEqualTo("newId99");

        String name = documentContext.read("$.name");
        assertThat(name).isEqualTo("Tiina Glass");

        String dateOfBirth = documentContext.read("$.dateOfBirth");
        assertThat(dateOfBirth).isEqualTo("1990-01-01");

        Boolean isCitizen = documentContext.read("$.isCitizen");
        assertThat(isCitizen).isEqualTo(true);

        String registrationDate = documentContext.read("$.registrationDate");
        assertThat(registrationDate).isEqualTo("2023-12-01");

        String withdrawalDate = documentContext.read("$.withdrawalDate");
        assertThat(withdrawalDate).isEqualTo("2023-12-15");

        Boolean lostThisElection = documentContext.read("$.lostThisElection");
        assertThat(lostThisElection).isEqualTo(true);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"140"})
    void putResponds204AndUpdatedTheCandidateWhenSupplyingAnAlteredExistingCandidate(String idInBody) {
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

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());

        String id = documentContext.read("$.id");
        assertThat(id).isEqualTo("140");

        String name = documentContext.read("$.name");
        assertThat(name).isEqualTo("Tris Bool");

        String dateOfBirth = documentContext.read("$.dateOfBirth");
        assertThat(dateOfBirth).isEqualTo("1985-03-15");

        Boolean isCitizen = documentContext.read("$.isCitizen");
        assertThat(isCitizen).isEqualTo(false);

        String registrationDate = documentContext.read("$.registrationDate");
        assertThat(registrationDate).isEqualTo("2023-11-15");

        String withdrawalDate = documentContext.read("$.withdrawalDate");
        assertThat(withdrawalDate).isEqualTo("2023-12-15");

        Boolean lostThisElection = documentContext.read("$.lostThisElection");
        assertThat(lostThisElection).isEqualTo(true);
    }


    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"140, newId99"})
    void putResponds400WhenSupplyingVoterInsteadOfCandidate(String idInBody) {
        Voter voter = new Voter("Tris Bool", idInBody, "1985-03-15", false);

        HttpEntity<Voter> request = new HttpEntity<>(voter);
        ResponseEntity<String> updateResponse = restTemplate
                .exchange("/candidates/140", HttpMethod.PUT, request, String.class);

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
