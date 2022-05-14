package vn.com.tma.emsbackend.unit.controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import vn.com.tma.emsbackend.controller.CredentialController;
import vn.com.tma.emsbackend.dto.CredentialDto;
import vn.com.tma.emsbackend.service.credential.CredentialService;

import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@WebMvcTest(CredentialController.class)
@AutoConfigureMockMvc(addFilters = false)
class CredentialControllerTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CredentialService credentialService;

    private CredentialDto credentialDto;

    private final JsonMapper jsonMapper = new JsonMapper();

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mvc);
        credentialDto = new CredentialDto();
        credentialDto.setId(1L);
        credentialDto.setName("name");
        credentialDto.setUsername("username");
        credentialDto.setPassword("password");
    }

    @Test
    void shouldReturn200AndValidCredentialsWhenGetAllCredentials() throws Exception {
        // Given
        when(credentialService.getAll()).thenReturn(List.of(credentialDto));

        // When - Then
        given()
                .auth().none()
                .get("/api/v1/credentials")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(is(jsonMapper.writeValueAsString(List.of(credentialDto))));
    }

    @Test
    void shouldReturn200AndAValidCredentialWhenGetCredentialById() throws Exception {
        // Given
        when(credentialService.get(anyLong())).thenReturn(credentialDto);

        // When
        given()
                .auth().none()
                .get("/api/v1/credentials/" + 1L)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(is(jsonMapper.writeValueAsString(credentialDto)));
    }
//
//    @Test
//    void shouldReturn201AndAddedCredentialWhenAddCredential() {
//        // Given
//        when(credentialService.add(any(CredentialRequestDto.class))).thenReturn(credentialDto);
//
//        // When
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
//        ResponseEntity<CredentialDto> responseEntity = credentialController.addCredential(credentialRequestDto);
//
//        // Then
//        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.CREATED.value());
//
//        CredentialDto credentialDtoResult = responseEntity.getBody();
//        assertThat(credentialDtoResult).isNotNull();
//        assertThat(credentialDtoResult.getId()).isEqualTo(1L);
//        assertThat(credentialDtoResult.getUsername()).isEqualTo(credentialRequestDto.getUsername());
//        assertThat(credentialDtoResult.getName()).isEqualTo(credentialRequestDto.getName());
//    }
//
//    @Test
//    void shouldReturn200AndUpdatedCredentialWhenUpdateCredential() {
//        // Given
//        when(credentialService.update(anyLong(), any(CredentialRequestDto.class))).thenReturn(credentialDto);
//
//        // When
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
//        ResponseEntity<CredentialDto> responseEntity = credentialController.updateCredential(1L, credentialRequestDto);
//
//        // Then
//        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
//
//        CredentialDto credentialDtoResult = responseEntity.getBody();
//        assertThat(credentialDtoResult).isNotNull();
//        assertThat(credentialDtoResult.getId()).isEqualTo(1L);
//        assertThat(credentialDtoResult.getUsername()).isEqualTo(credentialRequestDto.getUsername());
//        assertThat(credentialDtoResult.getName()).isEqualTo(credentialRequestDto.getName());
//    }
//
//    @Test
//    void shouldReturn204AndNullWhenDeleteCredential() {
//        // Given
//
//        // When
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
//        ResponseEntity<CredentialDto> responseEntity = credentialController.deleteCredential(1L);
//
//        // Then
//        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.NO_CONTENT.value());
//        assertThat(responseEntity.getBody()).isNull();
//    }
}