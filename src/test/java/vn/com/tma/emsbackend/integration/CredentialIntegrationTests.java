package vn.com.tma.emsbackend.integration;

;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import vn.com.tma.emsbackend.model.dto.CredentialDTO;
import vn.com.tma.emsbackend.model.dto.ErrorDTO;
import vn.com.tma.emsbackend.model.entity.Credential;
import vn.com.tma.emsbackend.model.mapper.CredentialMapper;
import vn.com.tma.emsbackend.model.mapper.CredentialMapperImpl;
import vn.com.tma.emsbackend.repository.CredentialRepository;
import vn.com.tma.emsbackend.util.auth.LoginUtil;
import vn.com.tma.emsbackend.util.entity.DTO.LoginDTO;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CredentialIntegrationTests {
    @LocalServerPort
    private Integer port;

    private String baseURL;

    @Autowired
    private CredentialRepository credentialRepository;

    private CredentialMapper credentialMapper = new CredentialMapperImpl();
    private Credential genericCredential;
    private CredentialDTO genericCredentialDTO;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private String accessToken;
    private LoginUtil loginUtil= new LoginUtil();


    @BeforeEach
    void setUp() {
        baseURL = "http://localhost:" + port;

        genericCredential = new Credential();
        genericCredential.setName("admin");
        genericCredential.setPassword("admin");
        genericCredential.setUsername("admin");
        genericCredential.setId(1L);


        LoginDTO loginDTO = loginUtil.loginAsAdmin(testRestTemplate);
        accessToken = loginDTO.getAccess_token();

        genericCredentialDTO = credentialMapper.entityToDTO(genericCredential);
    }

    @Test
    void shouldReturn200AndValidCredentialsWhenGetAllCredentials() {
        // Given
        credentialRepository.save(genericCredential);

        String url = baseURL + "/api/v1/credentials";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        ResponseEntity<List<CredentialDTO>> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<>() {
        });
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().size()).isOne();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertCredentialsIsEqual(responseEntity.getBody().get(0), genericCredentialDTO);
    }

    @Test
    void shouldReturn201AndCredentialWhenAddNewCredential() {
        // Given
        String url = baseURL + "/api/v1/credentials";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);

        genericCredentialDTO.setDevices(null);
        HttpEntity<CredentialDTO> credentialDTOHttpEntity = new HttpEntity<>(genericCredentialDTO, headers);

        ResponseEntity<CredentialDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, credentialDTOHttpEntity, CredentialDTO.class);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);


        assertCredentialsIsEqual(responseEntity.getBody(), genericCredentialDTO);

    }

    @Test
    void shouldReturn201AndCredentialWhenUpdateCredential() {
        // Given
        credentialRepository.save(genericCredential);

        String url = baseURL + "/api/v1/credentials/" + genericCredential.getId();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);

        genericCredentialDTO.setDevices(null);
        genericCredentialDTO.setUsername("admin1");

        HttpEntity<CredentialDTO> credentialDTOHttpEntity = new HttpEntity<>(genericCredentialDTO, headers);

        ResponseEntity<CredentialDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, credentialDTOHttpEntity, CredentialDTO.class);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertCredentialsIsEqual(responseEntity.getBody(), genericCredentialDTO);

    }


    @Test
    void shouldReturn4AndCredentialWhenDeleteCredential() {
        // Given
        credentialRepository.save(genericCredential);

        String url = baseURL + "/api/v1/credentials/" + genericCredential.getId();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<CredentialDTO> credentialDTOHttpEntity = new HttpEntity<>(headers);

        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(url, HttpMethod.DELETE, credentialDTOHttpEntity, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    void assertCredentialsIsEqual(CredentialDTO thisCredential, CredentialDTO thatCredential){
        assertThat(thisCredential.getId()).isEqualTo(thatCredential.getId());
        assertThat(thisCredential.getUsername()).isEqualTo(thatCredential.getUsername());
        assertThat(thisCredential.getName()).isEqualTo(thatCredential.getName());
        assertThat(thisCredential.getPassword()).isEqualTo(thatCredential.getPassword());
    }

//    @Test
//    void shouldReturn200AndAValidCredentialWhenGetCredentialByIdWithExistedId() {
//        // Given
//        credentialRepository.save(credential);
//
//        // When
//        ResponseEntity<CredentialDTO> responseEntity = testRestTemplate
//                .exchange("/api/v1/credentials/1", HttpMethod.GET, null, CredentialDTO.class);
//
//        // Then
//        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
//
//        CredentialDTO CredentialDTOResult = responseEntity.getBody();
//        assertThat(CredentialDTOResult).isNotNull();
//        assertThat(CredentialDTOResult.getId()).isEqualTo(credential.getId());
//        assertThat(CredentialDTOResult.getUsername()).isEqualTo(credential.getUsername());
//        assertThat(CredentialDTOResult.getName()).isEqualTo(credential.getName());
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

//    @Test
//    void shouldReturn201AndAddedCredentialWhenAddCredentialWithValidData() {
//        // Given
//        CredentialDTO credentialDTO = new CredentialDTO();
//        credentialDTO.setName("name1");
//        credentialDTO.setUsername("username1");
//        credentialDTO.setPassword("password1");
//
//
//        HttpEntity<CredentialDTO> httpEntity = new HttpEntity<>(credentialDTO, null);
//
//        // When
//        ResponseEntity<CredentialDTO> responseEntity = testRestTemplate
//                .exchange("/api/v1/credentials", HttpMethod.POST, httpEntity, CredentialDTO.class);
//
//        // Then
//        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.CREATED.value());
//
//        CredentialDTO CredentialDTOResult = responseEntity.getBody();
//        assertThat(CredentialDTOResult).isNotNull();
//        assertThat(CredentialDTOResult.getUsername()).isEqualTo(credentialDTO.getUsername());
//        assertThat(CredentialDTOResult.getName()).isEqualTo(credentialDTO.getName());
//
//        List<Credential> credentialList = credentialRepository.findAll();
//        assertThat(credentialList.size()).isEqualTo(1);
//
//        Credential credentialResult = credentialList.get(0);
//        assertThat(credentialResult.getId()).isEqualTo(CredentialDTOResult.getId());
//        assertThat(credentialResult.getUsername()).isEqualTo(credentialDTO.getUsername());
//        assertThat(credentialResult.getName()).isEqualTo(credentialDTO.getName());
//        assertThat(credentialDTO.getPassword()).isEqualTo(credentialResult.getPassword());
//    }
//
//    @Test
//    void shouldReturn409AndErrorWhenAddCredentialWithDuplicateName() throws JSONException {
//        // Given
//        credentialRepository.save(credential);
//
//        CredentialDTO CredentialDTO = new CredentialDTO();
//        CredentialDTO.setName("name1");
//        CredentialDTO.setUsername("username2");
//        CredentialDTO.setPassword("password2");
//
//        HttpEntity<CredentialDTO> httpEntity = new HttpEntity<>(CredentialDTO, null);
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
//        CredentialDTO CredentialDTO = new CredentialDTO();
//        CredentialDTO.setName("nameUpdated");
//        CredentialDTO.setUsername("usernameUpdated");
//        CredentialDTO.setPassword("passwordUpdated");
//
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//
//        HttpEntity<CredentialDTO> httpEntity = new HttpEntity<>(CredentialDTO, null);
//
//        // When
//        ResponseEntity<CredentialDTO> responseEntity = testRestTemplate
//                .exchange("/api/v1/credentials/1", HttpMethod.PUT, httpEntity, CredentialDTO.class);
//
//        // Then
//        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
//
//        CredentialDTO CredentialDTOResult = responseEntity.getBody();
//        assertThat(CredentialDTOResult).isNotNull();
//        assertThat(CredentialDTOResult.getId()).isEqualTo(credential.getId());
//        assertThat(CredentialDTOResult.getUsername()).isEqualTo(CredentialDTO.getUsername());
//        assertThat(CredentialDTOResult.getName()).isEqualTo(CredentialDTO.getName());
//
//        List<Credential> credentialList = credentialRepository.findAll();
//        assertThat(credentialList.size()).isEqualTo(1);
//
//        Credential credentialResult = credentialList.get(0);
//        assertThat(credentialResult.getId()).isEqualTo(credential.getId());
//        assertThat(credentialResult.getUsername()).isEqualTo(CredentialDTO.getUsername());
//        assertThat(credentialResult.getName()).isEqualTo(CredentialDTO.getName());
//        assertThat(passwordEncoder.matches(CredentialDTO.getPassword(), credentialResult.getPassword())).isTrue();
//    }
//
//    @Test
//    void shouldReturn404AndErrorWhenUpdateCredentialWithNotExistedId() throws JSONException {
//        // Given
//        CredentialDTO CredentialDTO = new CredentialDTO();
//        CredentialDTO.setName("name");
//        CredentialDTO.setUsername("username");
//        CredentialDTO.setPassword("password");
//
//        HttpEntity<CredentialDTO> httpEntity = new HttpEntity<>(CredentialDTO, null);
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
//        CredentialDTO CredentialDTO = new CredentialDTO();
//        CredentialDTO.setName("name2");
//        CredentialDTO.setUsername("username2");
//        CredentialDTO.setPassword("password2");
//
//        HttpEntity<CredentialDTO> httpEntity = new HttpEntity<>(CredentialDTO, null);
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
//        ResponseEntity<CredentialDTO> responseEntity = testRestTemplate
//                .exchange("/api/v1/credentials/1", HttpMethod.DELETE, null, CredentialDTO.class);
//
//        // Then
//        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.NO_CONTENT.value());
//
//        CredentialDTO CredentialDTOResult = responseEntity.getBody();
//        assertThat(CredentialDTOResult).isNull();
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
