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
import vn.com.tma.emsbackend.model.dto.CredentialDTO;
import vn.com.tma.emsbackend.model.exception.CredentialNameExistsException;
import vn.com.tma.emsbackend.model.exception.CredentialNotFoundException;
import vn.com.tma.emsbackend.service.credential.CredentialService;

import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@WebMvcTest(CredentialController.class)
@AutoConfigureMockMvc(addFilters = false)
class CredentialControllerTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CredentialService credentialService;

    private CredentialDTO credentialDTO;

    private final JsonMapper jsonMapper = new JsonMapper();

    private final Long TEST_CREDENTIAL_ID = 1L;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mvc);
        credentialDTO = new CredentialDTO();
        credentialDTO.setId(TEST_CREDENTIAL_ID);
        credentialDTO.setName("name");
        credentialDTO.setUsername("username");
        credentialDTO.setPassword("password");
    }

    @Test
    void shouldReturn200AndValidCredentialsWhenGetAllCredentials() throws Exception {
        // Given
        when(credentialService.getAll()).thenReturn(List.of(credentialDTO));

        // When - Then
        given()
                .get("/api/v1/credentials")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(is(jsonMapper.writeValueAsString(List.of(credentialDTO))));
    }

    @Test
    void shouldReturn200AndAValidCredentialWhenGetCredentialById() throws Exception {
        // Given
        when(credentialService.get(anyLong())).thenReturn(credentialDTO);

        // When - Then
        given()
                .get("/api/v1/credentials/" + TEST_CREDENTIAL_ID)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(is(jsonMapper.writeValueAsString(credentialDTO)));
    }

    @Test
    void shouldReturn404WhenGetCredentialByIdWithNonExistentId() {
        // Given
        when(credentialService.get(anyLong())).thenThrow(new CredentialNotFoundException(TEST_CREDENTIAL_ID));

        // When - Then
        given()
                .get("/api/v1/credentials/" + TEST_CREDENTIAL_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldReturn201WhenAddCredentialWithValidData() throws Exception {
        // Given
        when(credentialService.add(any(CredentialDTO.class))).thenReturn(credentialDTO);

        // When - Then
        given()
                .contentType(ContentType.JSON)
                .body(credentialDTO)
                .post("/api/v1/credentials")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON)
                .body(is(jsonMapper.writeValueAsString(credentialDTO)));
    }

    @Test
    void shouldReturn400WhenAddCredentialWithExistedName() {
        // Given
        when(credentialService.add(any(CredentialDTO.class))).thenThrow(new CredentialNameExistsException(credentialDTO.getName()));

        // When - Then
        given()
                .contentType(ContentType.JSON)
                .body(credentialDTO)
                .post("/api/v1/credentials")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldReturn200WhenUpdateCredentialWithValidData() throws Exception {
        // Given
        when(credentialService.update(anyLong(), any(CredentialDTO.class))).thenReturn(credentialDTO);

        // When - Then
        given()
                .contentType(ContentType.JSON)
                .body(credentialDTO)
                .put("/api/v1/credentials/" + TEST_CREDENTIAL_ID)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(is(jsonMapper.writeValueAsString(credentialDTO)));
    }

    @Test
    void shouldReturn404WhenUpdateCredentialWithNonExistentId() {
        // Given
        when(credentialService.update(anyLong(), any(CredentialDTO.class))).thenThrow(new CredentialNotFoundException(TEST_CREDENTIAL_ID));

        // When - Then
        given()
                .contentType(ContentType.JSON)
                .body(credentialDTO)
                .put("/api/v1/credentials/" + TEST_CREDENTIAL_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldReturn400WhenUpdateCredentialWithExistedName() {
        // Given
        when(credentialService.update(anyLong(), any(CredentialDTO.class))).thenThrow(new CredentialNameExistsException(credentialDTO.getName()));

        // When - Then
        given()
                .contentType(ContentType.JSON)
                .body(credentialDTO)
                .put("/api/v1/credentials/" + TEST_CREDENTIAL_ID)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldReturn204WhenDeleteCredentialWithValidId() {
        // Given
        doNothing().when(credentialService).delete(anyLong());

        // When - Then
        given()
                .delete("/api/v1/credentials/" + TEST_CREDENTIAL_ID)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void shouldReturn404WhenDeleteCredentialWithNonExistentId() {
        // Given
        doThrow(new CredentialNotFoundException(TEST_CREDENTIAL_ID)).when(credentialService).delete(anyLong());

        // When - Then
        given()
                .delete("/api/v1/credentials/" + TEST_CREDENTIAL_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}