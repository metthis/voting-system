package metthis.voting_system.api;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = FillRepositories.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CandidateControllerTests {

    @Autowired
    private TestRestTemplate restTemplate;

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
}
