package vn.com.tma.emsbackend.unit.service;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.com.tma.emsbackend.common.Mapper;
import vn.com.tma.emsbackend.dto.CredentialDto;
import vn.com.tma.emsbackend.dto.CredentialRequestDto;
import vn.com.tma.emsbackend.entity.Credential;
import vn.com.tma.emsbackend.exception.ResourceConstraintViolationException;
import vn.com.tma.emsbackend.exception.ResourceNotFoundException;
import vn.com.tma.emsbackend.repository.CredentialRepository;
import vn.com.tma.emsbackend.service.credential.CredentialService;
import vn.com.tma.emsbackend.service.credential.CredentialServiceImpl;
import vn.com.tma.emsbackend.util.Constant;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static vn.com.tma.emsbackend.util.entity.CredentialCreator.createCredentialBy;
import static vn.com.tma.emsbackend.util.entity.CredentialCreator.createCredentialDtoBy;

@ExtendWith(MockitoExtension.class)
class CredentialServiceTests {
    @Mock
    private CredentialRepository credentialRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Mapper mapper;

    private CredentialService credentialService;

    private CredentialRequestDto credentialRequestDto;

    private Credential credential;

    private CredentialDto credentialDto;

    @BeforeEach
    void setUp() {
        credentialService = new CredentialServiceImpl(credentialRepository, passwordEncoder, mapper);

        credentialRequestDto = new CredentialRequestDto();
        credentialRequestDto.setName("name");
        credentialRequestDto.setUsername("username");
        credentialRequestDto.setPassword("password");

        credential = createCredentialBy(credentialRequestDto);
        credentialDto = createCredentialDtoBy(credential);
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

        assertThat(credentialDtoListResult).as("should have 1 member in the List").hasSize(1);
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
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(credentialRepository).findById(longArgumentCaptor.capture());
        long id = longArgumentCaptor.getValue();
        assertThat(id).isEqualTo(1L);

        assertThat(credentialDtoResult.getId()).isEqualTo(credential.getId());
        assertThat(credentialDtoResult.getName()).isEqualTo(credential.getName());
        assertThat(credentialDtoResult.getUsername()).isEqualTo(credential.getUsername());
    }

    @Test
    void shouldThrowExceptionWhenGetWithNotExistedId() {
        // Given
        when(credentialRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When
        Throwable throwable = catchThrowable(() -> credentialService.get(1L));

        // Then
        AssertionsForClassTypes.assertThat(throwable)
                .as("should throw the correct exception")
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.valueOf(1L));
    }


    @Test
    void shouldAddACredentialWhenAddWithValidData() {
        when(passwordEncoder.encode(anyString())).thenReturn(Constant.hashedPassword);
        when(mapper.map(any(CredentialRequestDto.class), any())).thenReturn(credential);
        when(mapper.map(any(Credential.class), any())).thenReturn(credentialDto);

        // Given
        when(credentialRepository.save(any(Credential.class))).thenReturn(new Credential());

        // When
        CredentialDto credentialDtoResult = credentialService.add(credentialRequestDto);

        // Then
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(passwordEncoder).encode(stringArgumentCaptor.capture());
        String password = stringArgumentCaptor.getValue();
        assertThat(password).isEqualTo(credentialRequestDto.getPassword());

        ArgumentCaptor<Credential> credentialArgumentCaptor = ArgumentCaptor.forClass(Credential.class);
        verify(credentialRepository).save(credentialArgumentCaptor.capture());
        Credential credentialResult = credentialArgumentCaptor.getValue();
        assertThat(credentialResult.getName()).isEqualTo(credentialRequestDto.getName());
        assertThat(credentialResult.getUsername()).isEqualTo(credentialRequestDto.getUsername());
        assertThat(credentialResult.getPassword()).isEqualTo(Constant.hashedPassword);

        assertThat(credentialDtoResult.getId()).isEqualTo(credentialDto.getId());
        assertThat(credentialDtoResult.getName()).isEqualTo(credentialDto.getName());
        assertThat(credentialDtoResult.getUsername()).isEqualTo(credentialDto.getUsername());
    }

    @Test
    void shouldThrowExceptionWhenAddWithConstraintViolatedData() {
        // Given
        when(credentialRepository.save(any(Credential.class))).thenThrow(DataIntegrityViolationException.class);
        when(passwordEncoder.encode(anyString())).thenReturn(Constant.hashedPassword);
        when(mapper.map(any(CredentialRequestDto.class), any())).thenReturn(credential);

        // When
        Throwable throwable = catchThrowable(() -> credentialService.add(credentialRequestDto));

        // Then
        AssertionsForClassTypes.assertThat(throwable)
                .as("should throw the correct exception")
                .isInstanceOf(ResourceConstraintViolationException.class)
                .hasMessageContaining(Credential.class.getName());
    }

    @Test
    void shouldUpdateACredentialWhenUpdateWithExistedIdAndValidData() {
        // Given
        when(credentialRepository.existsById(anyLong())).thenReturn(true);
        when(credentialRepository.save(any(Credential.class))).thenReturn(new Credential());
        when(passwordEncoder.encode(anyString())).thenReturn(Constant.hashedPassword);
        when(mapper.map(any(CredentialRequestDto.class), any())).thenReturn(credential);
        when(mapper.map(any(Credential.class), any())).thenReturn(credentialDto);

        // When
        CredentialDto credentialDtoResult = credentialService.update(1L, credentialRequestDto);

        // Then
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(credentialRepository).existsById(longArgumentCaptor.capture());
        long id = longArgumentCaptor.getValue();
        assertThat(id).isEqualTo(1L);

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(passwordEncoder).encode(stringArgumentCaptor.capture());
        String password = stringArgumentCaptor.getValue();
        assertThat(password).isEqualTo(credentialRequestDto.getPassword());

        ArgumentCaptor<Credential> credentialArgumentCaptor = ArgumentCaptor.forClass(Credential.class);
        verify(credentialRepository).save(credentialArgumentCaptor.capture());
        Credential credentialResult = credentialArgumentCaptor.getValue();
        assertThat(credentialResult.getName()).isEqualTo(credentialRequestDto.getName());
        assertThat(credentialResult.getUsername()).isEqualTo(credentialRequestDto.getUsername());
        assertThat(credentialResult.getPassword()).isEqualTo(Constant.hashedPassword);

        assertThat(credentialDtoResult.getId()).isEqualTo(credentialDto.getId());
        assertThat(credentialDtoResult.getName()).isEqualTo(credentialDto.getName());
        assertThat(credentialDtoResult.getUsername()).isEqualTo(credentialDto.getUsername());
    }

    @Test
    void shouldThrowExceptionWhenUpdateWithNotExistedId() {
        // Given
        when(credentialRepository.existsById(anyLong())).thenReturn(false);

        // When
        Throwable throwable = catchThrowable(() -> credentialService.update(1L, credentialRequestDto));

        // Then
        AssertionsForClassTypes.assertThat(throwable)
                .as("should throw the correct exception")
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.valueOf(1L));
    }

    @Test
    void shouldThrowExceptionWhenUpdateWithConstraintViolatedData() {
        // Given
        when(credentialRepository.existsById(anyLong())).thenReturn(true);
        when(credentialRepository.save(any(Credential.class))).thenThrow(DataIntegrityViolationException.class);
        when(passwordEncoder.encode(anyString())).thenReturn(Constant.hashedPassword);
        when(mapper.map(any(CredentialRequestDto.class), any())).thenReturn(credential);

        // When
        Throwable throwable = catchThrowable(() -> credentialService.update(1L, credentialRequestDto));

        // Then
        AssertionsForClassTypes.assertThat(throwable)
                .as("should throw the correct exception")
                .isInstanceOf(ResourceConstraintViolationException.class)
                .hasMessageContaining(Credential.class.getName());
    }

    @Test
    void shouldDeleteACredentialWhenDeleteWithExistedId() {
        // Given
        doNothing().when(credentialRepository).deleteById(anyLong());

        // When
        credentialService.delete(1L);

        // Then
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(credentialRepository).deleteById(longArgumentCaptor.capture());
        long id = longArgumentCaptor.getValue();
        assertThat(id).isEqualTo(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeleteWithNotExistedId() {
        // Given
        doThrow(EmptyResultDataAccessException.class).doNothing().when(credentialRepository).deleteById(anyLong());

        // When
        Throwable throwable = catchThrowable(() -> credentialService.delete(1L));

        // Then
        AssertionsForClassTypes.assertThat(throwable)
                .as("should throw the correct exception")
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.valueOf(1L));
    }
}