package vn.com.tma.emsbackend.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import vn.com.tma.emsbackend.model.dto.CredentialDTO;
import vn.com.tma.emsbackend.model.entity.Credential;
import vn.com.tma.emsbackend.model.exception.CredentialNameExistsException;
import vn.com.tma.emsbackend.model.exception.CredentialNotFoundException;
import vn.com.tma.emsbackend.model.mapper.CredentialMapper;
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
    private CredentialMapper credentialMapper;

    private CredentialService credentialService;

    private Credential credential;

    private CredentialDTO credentialDto;

    @BeforeEach
    void setUp() {
        credentialService = new CredentialServiceImpl(credentialRepository, credentialMapper);

        credentialDto = new CredentialDTO();
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
        when(credentialMapper.entitiesToDTOs(anyList())).thenReturn(List.of(credentialDto));

        // When
        List<CredentialDTO> credentialDTOListResult = credentialService.getAll();

        // Then
        verify(credentialRepository).findAll();

        assertThat(credentialDTOListResult).hasSize(1);
        CredentialDTO credentialDTOResult = credentialDTOListResult.get(0);
        assertThat(credentialDTOResult.getId()).isEqualTo(credentialDto.getId());
        assertThat(credentialDTOResult.getName()).isEqualTo(credentialDto.getName());
        assertThat(credentialDTOResult.getUsername()).isEqualTo(credentialDto.getUsername());
    }

    @Test
    void shouldGetACredentialWhenGetWithExistedId() {
        // Given
        when(credentialRepository.findById(anyLong())).thenReturn(Optional.of(credential));
        when(credentialMapper.entityToDTO(any(Credential.class))).thenReturn(credentialDto);

        // When
        CredentialDTO credentialDTOResult = credentialService.get(1L);

        // Then
        assertThat(credentialDTOResult.getId()).isEqualTo(credential.getId());
        assertThat(credentialDTOResult.getName()).isEqualTo(credential.getName());
        assertThat(credentialDTOResult.getUsername()).isEqualTo(credential.getUsername());
        assertThat(credentialDTOResult.getPassword()).isEqualTo(credential.getPassword());
    }

    @Test
    void shouldThrowExceptionWhenGetWithNotExistedId() {
        // Given
        when(credentialRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When
        Throwable throwable = catchThrowable(() -> credentialService.get(1L));

        // Then
        assertThat(throwable).isInstanceOf(CredentialNotFoundException.class);
    }


    @Test
    void shouldAddACredentialWhenAddWithValidData() {
        // Given
        when(credentialMapper.dtoToEntity(any(CredentialDTO.class))).thenReturn(credential);
        when(credentialMapper.entityToDTO(any(Credential.class))).thenReturn(credentialDto);
        when(credentialRepository.existsByName(anyString())).thenReturn(false);
        when(credentialRepository.save(any(Credential.class))).thenReturn(new Credential());

        // When
        CredentialDTO credentialDTOResult = credentialService.add(credentialDto);

        // Then
        assertThat(credentialDTOResult.getId()).isEqualTo(credentialDto.getId());
        assertThat(credentialDTOResult.getName()).isEqualTo(credentialDto.getName());
        assertThat(credentialDTOResult.getUsername()).isEqualTo(credentialDto.getUsername());
        assertThat(credentialDTOResult.getPassword()).isEqualTo(credential.getPassword());
    }

    @Test
    void shouldThrowExceptionWhenAddWithExistedName() {
        // Given
        when(credentialRepository.existsByName(anyString())).thenReturn(true);

        // When
        Throwable throwable = catchThrowable(() -> credentialService.add(credentialDto));

        // Then
        assertThat(throwable).isInstanceOf(CredentialNameExistsException.class);
    }

    @Test
    void shouldUpdateACredentialWhenUpdateWithExistedIdAndValidData() {
        // Given
        when(credentialMapper.dtoToEntity(any(CredentialDTO.class))).thenReturn(credential);
        when(credentialMapper.entityToDTO(any(Credential.class))).thenReturn(credentialDto);
        when(credentialRepository.existsById(anyLong())).thenReturn(true);
        when(credentialRepository.existsByName(anyString())).thenReturn(false);
        when(credentialRepository.save(any(Credential.class))).thenReturn(new Credential());

        // When
        CredentialDTO credentialDTOResult = credentialService.update(1L, credentialDto);

        // Then
        assertThat(credentialDTOResult.getId()).isEqualTo(credentialDto.getId());
        assertThat(credentialDTOResult.getName()).isEqualTo(credentialDto.getName());
        assertThat(credentialDTOResult.getUsername()).isEqualTo(credentialDto.getUsername());
        assertThat(credentialDTOResult.getPassword()).isEqualTo(credentialDto.getPassword());
    }

    @Test
    void shouldThrowExceptionWhenUpdateWithNotExistedId() {
        // Given
        when(credentialRepository.existsById(anyLong())).thenReturn(false);

        // When
        Throwable throwable = catchThrowable(() -> credentialService.update(1L, credentialDto));

        // Then
        assertThat(throwable).isInstanceOf(CredentialNotFoundException.class);
    }

    @Test
    void shouldThrowExceptionWhenUpdateWithExistedName() {
        // Given
        when(credentialRepository.existsById(anyLong())).thenReturn(true);
        when(credentialRepository.existsByName(anyString())).thenReturn(true);

        // When
        Throwable throwable = catchThrowable(() -> credentialService.update(1L, credentialDto));

        // Then
        assertThat(throwable).isInstanceOf(CredentialNameExistsException.class);
    }

    @Test
    void shouldDeleteACredentialWhenDeleteWithExistedId() {
        // Given
        when(credentialRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(credentialRepository).deleteById(anyLong());

        // When
        credentialService.delete(1L);

        // Then
        verify(credentialRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeleteWithNotExistedId() {
        // Given
        when(credentialRepository.existsById(anyLong())).thenReturn(false);

        // When
        Throwable throwable = catchThrowable(() -> credentialService.delete(1L));

        // Then
        assertThat(throwable).isInstanceOf(CredentialNotFoundException.class);
    }
}