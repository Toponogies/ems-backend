package vn.com.tma.emsbackend.integration;

import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.com.tma.emsbackend.dto.CredentialDto;
import vn.com.tma.emsbackend.dto.CredentialRequestDto;
import vn.com.tma.emsbackend.entity.Credential;
import vn.com.tma.emsbackend.repository.CredentialRepository;
import vn.com.tma.emsbackend.util.database.ResetDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ResetDatabase
class CredentialIntegrationTests {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CredentialRepository credentialRepository;

    private Credential credential;

    @BeforeEach
    void setUp() {
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

        credentialRepository.saveAll(credentialList);

        // When
        ResponseEntity<List<CredentialDto>> responseEntity = testRestTemplate
                .exchange("/api/v1/credentials", HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });

        // Then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        List<CredentialDto> credentialDtoListBody = responseEntity.getBody();
        Assertions.assertThat(credentialDtoListBody).as("should have 2 members in the List").hasSize(2);
        Assertions.assertThat(credentialDtoListBody).as("should match data in items")
                .extracting(CredentialDto::getId, CredentialDto::getName, CredentialDto::getUsername)
                .containsExactlyInAnyOrder(
                        tuple(1L, "name1", "username1"),
                        tuple(2L, "name2", "username2")
                );
    }

    @Test
    void shouldReturn200AndAValidCredentialWhenGetCredentialByIdWithExistedId() {
        // Given
        credentialRepository.save(credential);

        // When
        ResponseEntity<CredentialDto> responseEntity = testRestTemplate
                .exchange("/api/v1/credentials/1", HttpMethod.GET, null, CredentialDto.class);

        // Then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        CredentialDto credentialDtoResult = responseEntity.getBody();
        assertThat(credentialDtoResult).isNotNull();
        assertThat(credentialDtoResult.getId()).isEqualTo(credential.getId());
        assertThat(credentialDtoResult.getUsername()).isEqualTo(credential.getUsername());
        assertThat(credentialDtoResult.getName()).isEqualTo(credential.getName());
    }

    @Test
    void shouldReturn404AndAnErrorWhenGetCredentialByIdWithNotExistedId() throws JSONException {
        // Given

        // When
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange("/api/v1/credentials/1", HttpMethod.GET, null, String.class);

        // Then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.NOT_FOUND.value());
        JSONObject jsonObject = new JSONObject(responseEntity.getBody());
        assertThat(jsonObject.has("timestamp")).isTrue();
        assertThat(jsonObject.has("message")).isTrue();
        assertThat(jsonObject.has("details")).isTrue();
    }

    @Test
    void shouldReturn201AndAddedCredentialWhenAddCredentialWithValidData() {
        // Given
        CredentialRequestDto credentialRequestDto = new CredentialRequestDto();
        credentialRequestDto.setName("name1");
        credentialRequestDto.setUsername("username1");
        credentialRequestDto.setPassword("password1");

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        HttpEntity<CredentialRequestDto> httpEntity = new HttpEntity<>(credentialRequestDto, null);

        // When
        ResponseEntity<CredentialDto> responseEntity = testRestTemplate
                .exchange("/api/v1/credentials", HttpMethod.POST, httpEntity, CredentialDto.class);

        // Then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.CREATED.value());

        CredentialDto credentialDtoResult = responseEntity.getBody();
        assertThat(credentialDtoResult).isNotNull();
        assertThat(credentialDtoResult.getUsername()).isEqualTo(credentialRequestDto.getUsername());
        assertThat(credentialDtoResult.getName()).isEqualTo(credentialRequestDto.getName());

        List<Credential> credentialList = credentialRepository.findAll();
        assertThat(credentialList.size()).isEqualTo(1);

        Credential credentialResult = credentialList.get(0);
        assertThat(credentialResult.getId()).isEqualTo(credentialDtoResult.getId());
        assertThat(credentialResult.getUsername()).isEqualTo(credentialRequestDto.getUsername());
        assertThat(credentialResult.getName()).isEqualTo(credentialRequestDto.getName());
        assertThat(passwordEncoder.matches(credentialRequestDto.getPassword(), credentialResult.getPassword())).isTrue();
    }

    @Test
    void shouldReturn409AndErrorWhenAddCredentialWithDuplicateName() throws JSONException {
        // Given
        credentialRepository.save(credential);

        CredentialRequestDto credentialRequestDto = new CredentialRequestDto();
        credentialRequestDto.setName("name1");
        credentialRequestDto.setUsername("username2");
        credentialRequestDto.setPassword("password2");

        HttpEntity<CredentialRequestDto> httpEntity = new HttpEntity<>(credentialRequestDto, null);

        // When
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange("/api/v1/credentials", HttpMethod.POST, httpEntity, String.class);

        // Then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.CONFLICT.value());

        JSONObject jsonObject = new JSONObject(responseEntity.getBody());
        assertThat(jsonObject.has("timestamp")).isTrue();
        assertThat(jsonObject.has("message")).isTrue();
        assertThat(jsonObject.has("details")).isTrue();

        List<Credential> credentialList = credentialRepository.findAll();
        assertThat(credentialList.size()).isEqualTo(1);

        Credential credentialResult = credentialList.get(0);
        assertThat(credentialResult.getUsername()).isEqualTo(credential.getUsername());
        assertThat(credentialResult.getName()).isEqualTo(credential.getName());
        assertThat(credentialResult.getPassword()).isEqualTo(credential.getPassword());
    }

    @Test
    void shouldReturn200AndUpdatedCredentialWhenUpdateCredentialWithValidData() {
        // Given
        credentialRepository.save(credential);

        CredentialRequestDto credentialRequestDto = new CredentialRequestDto();
        credentialRequestDto.setName("nameUpdated");
        credentialRequestDto.setUsername("usernameUpdated");
        credentialRequestDto.setPassword("passwordUpdated");

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        HttpEntity<CredentialRequestDto> httpEntity = new HttpEntity<>(credentialRequestDto, null);

        // When
        ResponseEntity<CredentialDto> responseEntity = testRestTemplate
                .exchange("/api/v1/credentials/1", HttpMethod.PUT, httpEntity, CredentialDto.class);

        // Then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        CredentialDto credentialDtoResult = responseEntity.getBody();
        assertThat(credentialDtoResult).isNotNull();
        assertThat(credentialDtoResult.getId()).isEqualTo(credential.getId());
        assertThat(credentialDtoResult.getUsername()).isEqualTo(credentialRequestDto.getUsername());
        assertThat(credentialDtoResult.getName()).isEqualTo(credentialRequestDto.getName());

        List<Credential> credentialList = credentialRepository.findAll();
        assertThat(credentialList.size()).isEqualTo(1);

        Credential credentialResult = credentialList.get(0);
        assertThat(credentialResult.getId()).isEqualTo(credential.getId());
        assertThat(credentialResult.getUsername()).isEqualTo(credentialRequestDto.getUsername());
        assertThat(credentialResult.getName()).isEqualTo(credentialRequestDto.getName());
        assertThat(passwordEncoder.matches(credentialRequestDto.getPassword(), credentialResult.getPassword())).isTrue();
    }

    @Test
    void shouldReturn404AndErrorWhenUpdateCredentialWithNotExistedId() throws JSONException {
        // Given
        CredentialRequestDto credentialRequestDto = new CredentialRequestDto();
        credentialRequestDto.setName("name");
        credentialRequestDto.setUsername("username");
        credentialRequestDto.setPassword("password");

        HttpEntity<CredentialRequestDto> httpEntity = new HttpEntity<>(credentialRequestDto, null);

        // When
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange("/api/v1/credentials/1", HttpMethod.PUT, httpEntity, String.class);

        // Then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.NOT_FOUND.value());

        JSONObject jsonObject = new JSONObject(responseEntity.getBody());
        assertThat(jsonObject.has("timestamp")).isTrue();
        assertThat(jsonObject.has("message")).isTrue();
        assertThat(jsonObject.has("details")).isTrue();

        List<Credential> credentialList = credentialRepository.findAll();
        assertThat(credentialList.size()).isEqualTo(0);
    }

    @Test
    void shouldReturn409AndErrorWhenUpdateCredentialWithDuplicateName() throws JSONException {
        // Given
        Credential credential1 = new Credential();
        credential1.setId(2L);
        credential1.setName("name2");
        credential1.setUsername("username2");
        credential1.setPassword("password2");

        List<Credential> credentialList = new ArrayList<>();
        credentialList.add(credential);
        credentialList.add(credential1);

        credentialRepository.saveAll(credentialList);

        CredentialRequestDto credentialRequestDto = new CredentialRequestDto();
        credentialRequestDto.setName("name2");
        credentialRequestDto.setUsername("username2");
        credentialRequestDto.setPassword("password2");

        HttpEntity<CredentialRequestDto> httpEntity = new HttpEntity<>(credentialRequestDto, null);

        // When
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange("/api/v1/credentials/1", HttpMethod.PUT, httpEntity, String.class);

        // Then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.CONFLICT.value());

        JSONObject jsonObject = new JSONObject(responseEntity.getBody());
        assertThat(jsonObject.has("timestamp")).isTrue();
        assertThat(jsonObject.has("message")).isTrue();
        assertThat(jsonObject.has("details")).isTrue();

        Optional<Credential> optionalCredential = credentialRepository.findById(1L);
        assertThat(optionalCredential).isNotEmpty();

        Credential credentialResult = optionalCredential.get();
        assertThat(credentialResult.getUsername()).isEqualTo(credential.getUsername());
        assertThat(credentialResult.getName()).isEqualTo(credential.getName());
        assertThat(credentialResult.getPassword()).isEqualTo(credential.getPassword());
    }

    @Test
    void shouldReturn204WhenDeleteCredentialWithExistedId() {
        // Given
        credentialRepository.save(credential);

        // When
        ResponseEntity<CredentialDto> responseEntity = testRestTemplate
                .exchange("/api/v1/credentials/1", HttpMethod.DELETE, null, CredentialDto.class);

        // Then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.NO_CONTENT.value());

        CredentialDto credentialDtoResult = responseEntity.getBody();
        assertThat(credentialDtoResult).isNull();

        List<Credential> credentialList = credentialRepository.findAll();
        assertThat(credentialList.size()).isEqualTo(0);
    }

    @Test
    void shouldReturn404AndErrorWhenDeleteCredentialWithNotExistedId() throws JSONException {
        // Given
        credentialRepository.save(credential);

        // When
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange("/api/v1/credentials/2", HttpMethod.DELETE, null, String.class);

        // Then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.NOT_FOUND.value());

        JSONObject jsonObject = new JSONObject(responseEntity.getBody());
        assertThat(jsonObject.has("timestamp")).isTrue();
        assertThat(jsonObject.has("message")).isTrue();
        assertThat(jsonObject.has("details")).isTrue();

        List<Credential> credentialList = credentialRepository.findAll();
        assertThat(credentialList.size()).isEqualTo(1);

        Credential credentialResult = credentialList.get(0);
        assertThat(credentialResult.getId()).isEqualTo(credential.getId());
        assertThat(credentialResult.getUsername()).isEqualTo(credential.getUsername());
        assertThat(credentialResult.getName()).isEqualTo(credential.getName());
        assertThat(credentialResult.getPassword()).isEqualTo(credential.getPassword());
    }
}
