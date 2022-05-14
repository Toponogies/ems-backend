package vn.com.tma.emsbackend.integration;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import vn.com.tma.emsbackend.dto.CredentialDto;
import vn.com.tma.emsbackend.entity.Credential;
import vn.com.tma.emsbackend.repository.CredentialRepository;
import vn.com.tma.emsbackend.util.database.ResetDatabase;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser
@ResetDatabase
class CredentialIntegrationTests {
    @LocalServerPort
    private Integer port;

    private String baseURL;

    @Autowired
    private CredentialRepository credentialRepository;

    private Credential credential;

    @BeforeEach
    void setUp() {
        baseURL = "http://localhost:" + port;

        credential = new Credential();
        credential.setId(1L);
        credential.setName("name1");
        credential.setUsername("username1");
        credential.setPassword("password1");
    }

    @Test
    void shouldReturn200AndValidCredentialsWhenGetAllCredentials() {
        // Given
        Credential credential1 = new Credential();
        credential1.setId(2L);
        credential1.setName("name2");
        credential1.setUsername("username2");
        credential1.setPassword("password2");

        List<Credential> credentialList = new ArrayList<>();
        credentialList.add(credential);
        credentialList.add(credential1);

        credentialList = credentialRepository.saveAll(credentialList);

        // When
        Response response = given()
                .get(baseURL + "/api/v1/credentials");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());

        List<CredentialDto> credentialsResponse = response.getBody().as(new TypeRef<>() {});
        assertThat(credentialsResponse).hasSize(2);
        CredentialDto credentialResult1 = credentialsResponse.get(0);
        Credential credentialBase1 = credentialList.get(0);
        assertThat(credentialResult1.getId()).isEqualTo(credentialBase1.getId());
        assertThat(credentialResult1.getName()).isEqualTo(credentialBase1.getName());
        assertThat(credentialResult1.getUsername()).isEqualTo(credentialBase1.getUsername());
        assertThat(credentialResult1.getPassword()).isEqualTo(credentialBase1.getPassword());

        CredentialDto credentialResult2 = credentialsResponse.get(1);
        Credential credentialBase2 = credentialList.get(1);
        assertThat(credentialResult2.getId()).isEqualTo(credentialBase2.getId());
        assertThat(credentialResult2.getName()).isEqualTo(credentialBase2.getName());
        assertThat(credentialResult2.getUsername()).isEqualTo(credentialBase2.getUsername());
        assertThat(credentialResult2.getPassword()).isEqualTo(credentialBase2.getPassword());
    }

//    @Test
//    void shouldReturn200AndAValidCredentialWhenGetCredentialByIdWithExistedId() {
//        // Given
//        credentialRepository.save(credential);
//
//        // When
//        ResponseEntity<CredentialDto> responseEntity = testRestTemplate
//                .exchange("/api/v1/credentials/1", HttpMethod.GET, null, CredentialDto.class);
//
//        // Then
//        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
//
//        CredentialDto credentialDtoResult = responseEntity.getBody();
//        assertThat(credentialDtoResult).isNotNull();
//        assertThat(credentialDtoResult.getId()).isEqualTo(credential.getId());
//        assertThat(credentialDtoResult.getUsername()).isEqualTo(credential.getUsername());
//        assertThat(credentialDtoResult.getName()).isEqualTo(credential.getName());
//    }
//
//    @Test
//    void shouldReturn404AndAnErrorWhenGetCredentialByIdWithNotExistedId() throws JSONException {
//        // Given
//
//        // When
//        ResponseEntity<String> responseEntity = testRestTemplate
//                .exchange("/api/v1/credentials/1", HttpMethod.GET, null, String.class);
//
//        // Then
//        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.NOT_FOUND.value());
//        JSONObject jsonObject = new JSONObject(responseEntity.getBody());
//        assertThat(jsonObject.has("timestamp")).isTrue();
//        assertThat(jsonObject.has("message")).isTrue();
//        assertThat(jsonObject.has("details")).isTrue();
//    }
//
//    @Test
//    void shouldReturn201AndAddedCredentialWhenAddCredentialWithValidData() {
//        // Given
//        CredentialRequestDto credentialRequestDto = new CredentialRequestDto();
//        credentialRequestDto.setName("name1");
//        credentialRequestDto.setUsername("username1");
//        credentialRequestDto.setPassword("password1");
//
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//
//        HttpEntity<CredentialRequestDto> httpEntity = new HttpEntity<>(credentialRequestDto, null);
//
//        // When
//        ResponseEntity<CredentialDto> responseEntity = testRestTemplate
//                .exchange("/api/v1/credentials", HttpMethod.POST, httpEntity, CredentialDto.class);
//
//        // Then
//        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.CREATED.value());
//
//        CredentialDto credentialDtoResult = responseEntity.getBody();
//        assertThat(credentialDtoResult).isNotNull();
//        assertThat(credentialDtoResult.getUsername()).isEqualTo(credentialRequestDto.getUsername());
//        assertThat(credentialDtoResult.getName()).isEqualTo(credentialRequestDto.getName());
//
//        List<Credential> credentialList = credentialRepository.findAll();
//        assertThat(credentialList.size()).isEqualTo(1);
//
//        Credential credentialResult = credentialList.get(0);
//        assertThat(credentialResult.getId()).isEqualTo(credentialDtoResult.getId());
//        assertThat(credentialResult.getUsername()).isEqualTo(credentialRequestDto.getUsername());
//        assertThat(credentialResult.getName()).isEqualTo(credentialRequestDto.getName());
//        assertThat(passwordEncoder.matches(credentialRequestDto.getPassword(), credentialResult.getPassword())).isTrue();
//    }
//
//    @Test
//    void shouldReturn409AndErrorWhenAddCredentialWithDuplicateName() throws JSONException {
//        // Given
//        credentialRepository.save(credential);
//
//        CredentialRequestDto credentialRequestDto = new CredentialRequestDto();
//        credentialRequestDto.setName("name1");
//        credentialRequestDto.setUsername("username2");
//        credentialRequestDto.setPassword("password2");
//
//        HttpEntity<CredentialRequestDto> httpEntity = new HttpEntity<>(credentialRequestDto, null);
//
//        // When
//        ResponseEntity<String> responseEntity = testRestTemplate
//                .exchange("/api/v1/credentials", HttpMethod.POST, httpEntity, String.class);
//
//        // Then
//        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.CONFLICT.value());
//
//        JSONObject jsonObject = new JSONObject(responseEntity.getBody());
//        assertThat(jsonObject.has("timestamp")).isTrue();
//        assertThat(jsonObject.has("message")).isTrue();
//        assertThat(jsonObject.has("details")).isTrue();
//
//        List<Credential> credentialList = credentialRepository.findAll();
//        assertThat(credentialList.size()).isEqualTo(1);
//
//        Credential credentialResult = credentialList.get(0);
//        assertThat(credentialResult.getUsername()).isEqualTo(credential.getUsername());
//        assertThat(credentialResult.getName()).isEqualTo(credential.getName());
//        assertThat(credentialResult.getPassword()).isEqualTo(credential.getPassword());
//    }
//
//    @Test
//    void shouldReturn200AndUpdatedCredentialWhenUpdateCredentialWithValidData() {
//        // Given
//        credentialRepository.save(credential);
//
//        CredentialRequestDto credentialRequestDto = new CredentialRequestDto();
//        credentialRequestDto.setName("nameUpdated");
//        credentialRequestDto.setUsername("usernameUpdated");
//        credentialRequestDto.setPassword("passwordUpdated");
//
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//
//        HttpEntity<CredentialRequestDto> httpEntity = new HttpEntity<>(credentialRequestDto, null);
//
//        // When
//        ResponseEntity<CredentialDto> responseEntity = testRestTemplate
//                .exchange("/api/v1/credentials/1", HttpMethod.PUT, httpEntity, CredentialDto.class);
//
//        // Then
//        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
//
//        CredentialDto credentialDtoResult = responseEntity.getBody();
//        assertThat(credentialDtoResult).isNotNull();
//        assertThat(credentialDtoResult.getId()).isEqualTo(credential.getId());
//        assertThat(credentialDtoResult.getUsername()).isEqualTo(credentialRequestDto.getUsername());
//        assertThat(credentialDtoResult.getName()).isEqualTo(credentialRequestDto.getName());
//
//        List<Credential> credentialList = credentialRepository.findAll();
//        assertThat(credentialList.size()).isEqualTo(1);
//
//        Credential credentialResult = credentialList.get(0);
//        assertThat(credentialResult.getId()).isEqualTo(credential.getId());
//        assertThat(credentialResult.getUsername()).isEqualTo(credentialRequestDto.getUsername());
//        assertThat(credentialResult.getName()).isEqualTo(credentialRequestDto.getName());
//        assertThat(passwordEncoder.matches(credentialRequestDto.getPassword(), credentialResult.getPassword())).isTrue();
//    }
//
//    @Test
//    void shouldReturn404AndErrorWhenUpdateCredentialWithNotExistedId() throws JSONException {
//        // Given
//        CredentialRequestDto credentialRequestDto = new CredentialRequestDto();
//        credentialRequestDto.setName("name");
//        credentialRequestDto.setUsername("username");
//        credentialRequestDto.setPassword("password");
//
//        HttpEntity<CredentialRequestDto> httpEntity = new HttpEntity<>(credentialRequestDto, null);
//
//        // When
//        ResponseEntity<String> responseEntity = testRestTemplate
//                .exchange("/api/v1/credentials/1", HttpMethod.PUT, httpEntity, String.class);
//
//        // Then
//        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.NOT_FOUND.value());
//
//        JSONObject jsonObject = new JSONObject(responseEntity.getBody());
//        assertThat(jsonObject.has("timestamp")).isTrue();
//        assertThat(jsonObject.has("message")).isTrue();
//        assertThat(jsonObject.has("details")).isTrue();
//
//        List<Credential> credentialList = credentialRepository.findAll();
//        assertThat(credentialList.size()).isEqualTo(0);
//    }
//
//    @Test
//    void shouldReturn409AndErrorWhenUpdateCredentialWithDuplicateName() throws JSONException {
//        // Given
//        Credential credential1 = new Credential();
//        credential1.setId(2L);
//        credential1.setName("name2");
//        credential1.setUsername("username2");
//        credential1.setPassword("password2");
//
//        List<Credential> credentialList = new ArrayList<>();
//        credentialList.add(credential);
//        credentialList.add(credential1);
//
//        credentialRepository.saveAll(credentialList);
//
//        CredentialRequestDto credentialRequestDto = new CredentialRequestDto();
//        credentialRequestDto.setName("name2");
//        credentialRequestDto.setUsername("username2");
//        credentialRequestDto.setPassword("password2");
//
//        HttpEntity<CredentialRequestDto> httpEntity = new HttpEntity<>(credentialRequestDto, null);
//
//        // When
//        ResponseEntity<String> responseEntity = testRestTemplate
//                .exchange("/api/v1/credentials/1", HttpMethod.PUT, httpEntity, String.class);
//
//        // Then
//        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.CONFLICT.value());
//
//        JSONObject jsonObject = new JSONObject(responseEntity.getBody());
//        assertThat(jsonObject.has("timestamp")).isTrue();
//        assertThat(jsonObject.has("message")).isTrue();
//        assertThat(jsonObject.has("details")).isTrue();
//
//        Optional<Credential> optionalCredential = credentialRepository.findById(1L);
//        assertThat(optionalCredential).isNotEmpty();
//
//        Credential credentialResult = optionalCredential.get();
//        assertThat(credentialResult.getUsername()).isEqualTo(credential.getUsername());
//        assertThat(credentialResult.getName()).isEqualTo(credential.getName());
//        assertThat(credentialResult.getPassword()).isEqualTo(credential.getPassword());
//    }
//
//    @Test
//    void shouldReturn204WhenDeleteCredentialWithExistedId() {
//        // Given
//        credentialRepository.save(credential);
//
//        // When
//        ResponseEntity<CredentialDto> responseEntity = testRestTemplate
//                .exchange("/api/v1/credentials/1", HttpMethod.DELETE, null, CredentialDto.class);
//
//        // Then
//        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.NO_CONTENT.value());
//
//        CredentialDto credentialDtoResult = responseEntity.getBody();
//        assertThat(credentialDtoResult).isNull();
//
//        List<Credential> credentialList = credentialRepository.findAll();
//        assertThat(credentialList.size()).isEqualTo(0);
//    }
//
//    @Test
//    void shouldReturn404AndErrorWhenDeleteCredentialWithNotExistedId() throws JSONException {
//        // Given
//        credentialRepository.save(credential);
//
//        // When
//        ResponseEntity<String> responseEntity = testRestTemplate
//                .exchange("/api/v1/credentials/2", HttpMethod.DELETE, null, String.class);
//
//        // Then
//        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.NOT_FOUND.value());
//
//        JSONObject jsonObject = new JSONObject(responseEntity.getBody());
//        assertThat(jsonObject.has("timestamp")).isTrue();
//        assertThat(jsonObject.has("message")).isTrue();
//        assertThat(jsonObject.has("details")).isTrue();
//
//        List<Credential> credentialList = credentialRepository.findAll();
//        assertThat(credentialList.size()).isEqualTo(1);
//
//        Credential credentialResult = credentialList.get(0);
//        assertThat(credentialResult.getId()).isEqualTo(credential.getId());
//        assertThat(credentialResult.getUsername()).isEqualTo(credential.getUsername());
//        assertThat(credentialResult.getName()).isEqualTo(credential.getName());
//        assertThat(credentialResult.getPassword()).isEqualTo(credential.getPassword());
//    }
}
