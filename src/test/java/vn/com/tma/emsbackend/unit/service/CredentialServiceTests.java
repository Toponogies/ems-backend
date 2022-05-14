package vn.com.tma.emsbackend.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import vn.com.tma.emsbackend.common.Mapper;
import vn.com.tma.emsbackend.dto.CredentialDto;
import vn.com.tma.emsbackend.entity.Credential;
import vn.com.tma.emsbackend.exception.ResourceConstraintViolationException;
import vn.com.tma.emsbackend.exception.ResourceNotFoundException;
import vn.com.tma.emsbackend.repository.CredentialRepository;
import vn.com.tma.emsbackend.service.credential.CredentialService;
import vn.com.tma.emsbackend.service.credential.CredentialServiceImpl;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static vn.com.tma.emsbackend.util.entity.CredentialCreator.createCredentialBy;

@ExtendWith(MockitoExtension.class)
class CredentialServiceTests {
    @Mock
    private CredentialRepository credentialRepository;

    @Mock
    private Mapper mapper;

    private CredentialService credentialService;

    private Credential credential;

    private CredentialDto credentialDto;

    @BeforeEach
    void setUp() {
        credentialService = new CredentialServiceImpl(credentialRepository, mapper);

        credentialDto = new CredentialDto();
        credentialDto.setId(1L);
        credentialDto.setName("name");
        credentialDto.setUsername("username");
        credentialDto.setPassword("password");

        credential = createCredentialBy(credentialDto);
    }

    @Test
    void shouldGetAllCredentialsWhenGetAll() {
        // Given
        when(credentialRepository.findAll()).thenReturn(List.of(credential));
        when(mapper.mapList(anyList(), any())).thenReturn(List.of(credentialDto));

        // When
        List<CredentialDto> credentialDtoListResult = credentialService.getAll();

        // Then
        verify(credentialRepository).findAll();

        assertThat(credentialDtoListResult).hasSize(1);
        CredentialDto credentialDtoResult = credentialDtoListResult.get(0);
        assertThat(credentialDtoResult.getId()).isEqualTo(credentialDto.getId());
        assertThat(credentialDtoResult.getName()).isEqualTo(credentialDto.getName());
        assertThat(credentialDtoResult.getUsername()).isEqualTo(credentialDto.getUsername());
    }

    @Test
    void shouldGetACredentialWhenGetWithExistedId() {
        // Given
        when(credentialRepository.findById(anyLong())).thenReturn(Optional.of(credential));
        when(mapper.map(any(Credential.class), any())).thenReturn(credentialDto);

        // When
        CredentialDto credentialDtoResult = credentialService.get(1L);

        // Then
        assertThat(credentialDtoResult.getId()).isEqualTo(credential.getId());
        assertThat(credentialDtoResult.getName()).isEqualTo(credential.getName());
        assertThat(credentialDtoResult.getUsername()).isEqualTo(credential.getUsername());
        assertThat(credentialDtoResult.getPassword()).isEqualTo(credential.getPassword());
    }

    @Test
    void shouldThrowExceptionWhenGetWithNotExistedId() {
        // Given
        when(credentialRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When
        Throwable throwable = catchThrowable(() -> credentialService.get(1L));

        // Then
        assertThat(throwable).isInstanceOf(ResourceNotFoundException.class);
    }


    @Test
    void shouldAddACredentialWhenAddWithValidData() {
        when(mapper.map(any(CredentialDto.class), any())).thenReturn(credential);
        when(mapper.map(any(Credential.class), any())).thenReturn(credentialDto);

        // Given
        when(credentialRepository.save(any(Credential.class))).thenReturn(new Credential());

        // When
        CredentialDto credentialDtoResult = credentialService.add(credentialDto);

        // Then
        assertThat(credentialDtoResult.getId()).isEqualTo(credentialDto.getId());
        assertThat(credentialDtoResult.getName()).isEqualTo(credentialDto.getName());
        assertThat(credentialDtoResult.getUsername()).isEqualTo(credentialDto.getUsername());
        assertThat(credentialDtoResult.getPassword()).isEqualTo(credential.getPassword());
    }

    @Test
    void shouldThrowExceptionWhenAddWithConstraintViolatedData() {
        // Given
        when(credentialRepository.save(any(Credential.class))).thenThrow(DataIntegrityViolationException.class);
        when(mapper.map(any(CredentialDto.class), any())).thenReturn(credential);

        // When
        Throwable throwable = catchThrowable(() -> credentialService.add(credentialDto));

        // Then
        assertThat(throwable).isInstanceOf(ResourceConstraintViolationException.class);
    }

    @Test
    void shouldUpdateACredentialWhenUpdateWithExistedIdAndValidData() {
        // Given
        when(credentialRepository.existsById(anyLong())).thenReturn(true);
        when(credentialRepository.save(any(Credential.class))).thenReturn(new Credential());
        when(mapper.map(any(CredentialDto.class), any())).thenReturn(credential);
        when(mapper.map(any(Credential.class), any())).thenReturn(credentialDto);

        // When
        CredentialDto credentialDtoResult = credentialService.update(1L, credentialDto);

        // Then
        assertThat(credentialDtoResult.getId()).isEqualTo(credentialDto.getId());
        assertThat(credentialDtoResult.getName()).isEqualTo(credentialDto.getName());
        assertThat(credentialDtoResult.getUsername()).isEqualTo(credentialDto.getUsername());
        assertThat(credentialDtoResult.getPassword()).isEqualTo(credentialDto.getPassword());
    }

    @Test
    void shouldThrowExceptionWhenUpdateWithNotExistedId() {
        // Given
        when(credentialRepository.existsById(anyLong())).thenReturn(false);

        // When
        Throwable throwable = catchThrowable(() -> credentialService.update(1L, credentialDto));

        // Then
        assertThat(throwable).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldThrowExceptionWhenUpdateWithConstraintViolatedData() {
        // Given
        when(credentialRepository.existsById(anyLong())).thenReturn(true);
        when(credentialRepository.save(any(Credential.class))).thenThrow(DataIntegrityViolationException.class);
        when(mapper.map(any(CredentialDto.class), any())).thenReturn(credential);

        // When
        Throwable throwable = catchThrowable(() -> credentialService.update(1L, credentialDto));

        // Then
        assertThat(throwable).isInstanceOf(ResourceConstraintViolationException.class);
    }

    @Test
    void shouldDeleteACredentialWhenDeleteWithExistedId() {
        // Given
        doNothing().when(credentialRepository).deleteById(anyLong());

        // When
        credentialService.delete(1L);

        // Then
        verify(credentialRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeleteWithNotExistedId() {
        // Given
        doThrow(EmptyResultDataAccessException.class).doNothing().when(credentialRepository).deleteById(anyLong());

        // When
        Throwable throwable = catchThrowable(() -> credentialService.delete(1L));

        // Then
        assertThat(throwable).isInstanceOf(ResourceNotFoundException.class);
    }
}