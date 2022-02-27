package vn.com.tma.emsbackend.service;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
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

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static vn.com.tma.emsbackend.util.entity.CredentialCreator.createCredentialBy;
import static vn.com.tma.emsbackend.util.entity.CredentialCreator.createCredentialDtoBy;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class CredentialServiceTest {
    @Mock
    private CredentialRepository credentialRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Mapper mapper;

    private CredentialService credentialService;

    private CredentialRequestDto credentialRequestDto;

    @Before
    public void setUp() {
        credentialService = new CredentialServiceImpl(credentialRepository, passwordEncoder, mapper);

        credentialRequestDto = new CredentialRequestDto();
        credentialRequestDto.setName("name");
        credentialRequestDto.setUsername("username");
        credentialRequestDto.setPassword("password");

        Credential credential = createCredentialBy(credentialRequestDto);
        CredentialDto credentialDto = createCredentialDtoBy(credential);

        when(mapper.map(any(CredentialRequestDto.class), any())).thenReturn(credential);
        when(mapper.map(any(Credential.class), any())).thenReturn(credentialDto);
        when(passwordEncoder.encode(anyString())).thenReturn(Constant.hashedPassword);
    }

    @Test
    public void shouldGetAllCredentialsWhenGetAll() {
        // Given
        when(credentialRepository.findAll()).thenReturn(new ArrayList<>());

        // When
        credentialService.getAll();

        // Then
        verify(credentialRepository).findAll();
    }

    @Test
    public void shouldGetACredentialWhenGetWithExistedId() {
        // Given
        when(credentialRepository.findById(anyLong())).thenReturn(Optional.of(new Credential()));

        // When
        credentialService.get(1L);

        // Then
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(credentialRepository).findById(longArgumentCaptor.capture());
        long id = longArgumentCaptor.getValue();
        assertThat(id).isEqualTo(1L);
    }

    @Test
    public void shouldThrowExceptionWhenGetWithNotExistedId() {
        // Given
        when(credentialRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When
        Throwable throwable = catchThrowable(() -> {
            credentialService.get(1L);
        });

        // Then
        AssertionsForClassTypes.assertThat(throwable)
                .as("should throw the correct exception")
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.valueOf(1L));
    }


    @Test
    public void shouldAddACredentialWhenAddWithValidData() {
        // Given
        when(credentialRepository.save(any(Credential.class))).thenReturn(new Credential());

        // When
        credentialService.add(credentialRequestDto);

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
    }

    @Test
    public void shouldThrowExceptionWhenAddWithConstraintViolatedData() {
        // Given
        when(credentialRepository.save(any(Credential.class))).thenThrow(DataIntegrityViolationException.class);

        // When
        Throwable throwable = catchThrowable(() -> {
            credentialService.add(credentialRequestDto);
        });

        // Then
        AssertionsForClassTypes.assertThat(throwable)
                .as("should throw the correct exception")
                .isInstanceOf(ResourceConstraintViolationException.class)
                .hasMessageContaining(Credential.class.getName());
    }

    @Test
    public void shouldUpdateACredentialWhenUpdateWithExistedIdAndValidData() {
        // Given
        when(credentialRepository.existsById(anyLong())).thenReturn(true);
        when(credentialRepository.save(any(Credential.class))).thenReturn(new Credential());

        // When
        credentialService.update(1L, credentialRequestDto);

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
    }

    @Test
    public void shouldThrowExceptionWhenUpdateWithNotExistedId() {
        // Given
        when(credentialRepository.existsById(anyLong())).thenReturn(false);
        when(credentialRepository.save(any(Credential.class))).thenReturn(new Credential());

        // When
        Throwable throwable = catchThrowable(() -> {
            credentialService.update(1L, credentialRequestDto);
        });

        // Then
        AssertionsForClassTypes.assertThat(throwable)
                .as("should throw the correct exception")
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.valueOf(1L));
    }

    @Test
    public void shouldThrowExceptionWhenUpdateWithConstraintViolatedData() {
        // Given
        when(credentialRepository.existsById(anyLong())).thenReturn(true);
        when(credentialRepository.save(any(Credential.class))).thenThrow(DataIntegrityViolationException.class);

        // When
        Throwable throwable = catchThrowable(() -> {
            credentialService.update(1L, credentialRequestDto);
        });

        // Then
        AssertionsForClassTypes.assertThat(throwable)
                .as("should throw the correct exception")
                .isInstanceOf(ResourceConstraintViolationException.class)
                .hasMessageContaining(Credential.class.getName());
    }

    @Test
    public void shouldDeleteACredentialWhenDeleteWithExistedId() {
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
    public void shouldThrowExceptionWhenDeleteWithNotExistedId() {
        // Given
        doThrow(EmptyResultDataAccessException.class).doNothing().when(credentialRepository).deleteById(anyLong());

        // When
        Throwable throwable = catchThrowable(() -> {
            credentialService.delete(1L);
        });

        // Then
        AssertionsForClassTypes.assertThat(throwable)
                .as("should throw the correct exception")
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.valueOf(1L));
    }
}