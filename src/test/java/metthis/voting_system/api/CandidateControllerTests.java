package metthis.voting_system.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

        boolean isCitizen = documentContext.read("$.isCitizen");
        assertThat(isCitizen).isEqualTo(true);

        String registrationDate = documentContext.read("$.registrationDate");
        assertThat(registrationDate).isEqualTo("2023-11-15");

        String withdrawalDate = documentContext.read("$.withdrawalDate");
        assertThat(withdrawalDate).isEqualTo(null);

        boolean lostThisElection = documentContext.read("$.lostThisElection");
        assertThat(lostThisElection).isEqualTo(false);
    }

    @Test
    void getResponds404WhenCandidateDoesNotExist() {
        ResponseEntity<String> response = restTemplate
                .getForEntity("/candidates/999", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Could not find candidate 999");
    }
}
