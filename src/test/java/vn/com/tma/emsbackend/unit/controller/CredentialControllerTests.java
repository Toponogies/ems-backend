package vn.com.tma.emsbackend.unit.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import vn.com.tma.emsbackend.controller.CredentialController;
import vn.com.tma.emsbackend.dto.CredentialDto;
import vn.com.tma.emsbackend.dto.CredentialRequestDto;
import vn.com.tma.emsbackend.service.credential.CredentialService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static vn.com.tma.emsbackend.util.entity.CredentialCreator.createCredentialDtoBy;

@ExtendWith(MockitoExtension.class)
class CredentialControllerTests {
    @Mock
    private CredentialService credentialService;

    @InjectMocks
    private CredentialController credentialController;

    private CredentialRequestDto credentialRequestDto;

    private CredentialDto credentialDto;

    @BeforeEach
    void setUp() {
        credentialRequestDto = new CredentialRequestDto();
        credentialRequestDto.setName("name");
        credentialRequestDto.setUsername("username");
        credentialRequestDto.setPassword("password");

        credentialDto = createCredentialDtoBy(credentialRequestDto);
    }

    @Test
    void shouldReturn200AndValidCredentialsWhenGetAllCredentials() {
        // Given
        when(credentialService.getAll()).thenReturn(List.of(credentialDto));

        // When
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        ResponseEntity<List<CredentialDto>> responseEntity = credentialController.getAllCredentials();

        // Then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        List<CredentialDto> credentialDtoList = responseEntity.getBody();
        assertThat(credentialDtoList).isNotNull();
        assertThat(credentialDtoList.size()).isEqualTo(1);

        CredentialDto credentialDtoResult = credentialDtoList.get(0);
        assertThat(credentialDtoResult.getId()).isEqualTo(credentialDto.getId());
        assertThat(credentialDtoResult.getUsername()).isEqualTo(credentialDto.getUsername());
        assertThat(credentialDtoResult.getName()).isEqualTo(credentialDto.getName());
    }

    @Test
    void shouldReturn200AndAValidCredentialWhenGetCredentialById() {
        // Given
        when(credentialService.get(anyLong())).thenReturn(credentialDto);

        // When
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        ResponseEntity<CredentialDto> responseEntity = credentialController.getCredentialById(1L);

        // Then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        CredentialDto credentialDtoResult = responseEntity.getBody();
        assertThat(credentialDtoResult).isNotNull();
        assertThat(credentialDtoResult.getId()).isEqualTo(credentialDto.getId());
        assertThat(credentialDtoResult.getUsername()).isEqualTo(credentialDto.getUsername());
        assertThat(credentialDtoResult.getName()).isEqualTo(credentialDto.getName());
    }

    @Test
    void shouldReturn201AndAddedCredentialWhenAddCredential() {
        // Given
        when(credentialService.add(any(CredentialRequestDto.class))).thenReturn(credentialDto);

        // When
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        ResponseEntity<CredentialDto> responseEntity = credentialController.addCredential(credentialRequestDto);

        // Then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.CREATED.value());

        CredentialDto credentialDtoResult = responseEntity.getBody();
        assertThat(credentialDtoResult).isNotNull();
        assertThat(credentialDtoResult.getId()).isEqualTo(1L);
        assertThat(credentialDtoResult.getUsername()).isEqualTo(credentialRequestDto.getUsername());
        assertThat(credentialDtoResult.getName()).isEqualTo(credentialRequestDto.getName());
    }

    @Test
    void shouldReturn200AndUpdatedCredentialWhenUpdateCredential() {
        // Given
        when(credentialService.update(anyLong(), any(CredentialRequestDto.class))).thenReturn(credentialDto);

        // When
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        ResponseEntity<CredentialDto> responseEntity = credentialController.updateCredential(1L, credentialRequestDto);

        // Then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        CredentialDto credentialDtoResult = responseEntity.getBody();
        assertThat(credentialDtoResult).isNotNull();
        assertThat(credentialDtoResult.getId()).isEqualTo(1L);
        assertThat(credentialDtoResult.getUsername()).isEqualTo(credentialRequestDto.getUsername());
        assertThat(credentialDtoResult.getName()).isEqualTo(credentialRequestDto.getName());
    }

    @Test
    void shouldReturn204AndNullWhenDeleteCredential() {
        // Given

        // When
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        ResponseEntity<CredentialDto> responseEntity = credentialController.deleteCredential(1L);

        // Then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(responseEntity.getBody()).isNull();
    }
}