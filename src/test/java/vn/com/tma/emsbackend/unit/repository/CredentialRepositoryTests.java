package vn.com.tma.emsbackend.unit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import vn.com.tma.emsbackend.entity.Credential;
import vn.com.tma.emsbackend.repository.CredentialRepository;
import vn.com.tma.emsbackend.util.database.ResetDatabase;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DataJpaTest
@ResetDatabase
class CredentialRepositoryTests {
    @Autowired
    private CredentialRepository credentialRepository;

    private Credential credential;

    @BeforeEach
    void setUp() {
        credential = new Credential();
        credential.setId(1L);
        credential.setName("name");
        credential.setUsername("username");
        credential.setPassword("password");
    }

    @Test
    void shouldFindValidCredentialsWhenFindAllWithNotEmptyRepository() {
        // Given
        credentialRepository.save(credential);

        // When
        List<Credential> credentialListResult = credentialRepository.findAll();

        // Then
        assertThat(credentialListResult).hasSize(1);

        Credential credentialResult = credentialListResult.get(0);
        assertThat(credentialResult.getId()).isEqualTo(credential.getId());
        assertThat(credentialResult.getUsername()).isEqualTo(credential.getUsername());
        assertThat(credentialResult.getName()).isEqualTo(credential.getName());
        assertThat(credentialResult.getPassword()).isEqualTo(credential.getPassword());
    }

    @Test
    void shouldFindNoCredentialWhenFindAllWithEmptyRepository() {
        // Given

        // When
        List<Credential> credentialListResult = credentialRepository.findAll();

        // Then
        assertThat(credentialListResult).hasSize(0);
    }

    @Test
    void shouldFindAValidCredentialWhenFindByIdWithExistedId() {
        // Given
        credentialRepository.save(credential);

        // When
        Optional<Credential> credentialOptionalResult = credentialRepository.findById(1L);

        // Then
        assertThat(credentialOptionalResult).isPresent();

        Credential credentialResult = credentialOptionalResult.get();
        assertThat(credentialResult.getId()).isEqualTo(credential.getId());
        assertThat(credentialResult.getName()).isEqualTo(credential.getName());
        assertThat(credentialResult.getUsername()).isEqualTo(credential.getUsername());
        assertThat(credentialResult.getPassword()).isEqualTo(credential.getPassword());
    }

    @Test
    void shouldFindNoCredentialWhenFindByIdWithNotExistedId() {
        // Given

        // When
        Optional<Credential> credentialOptionalResult = credentialRepository.findById(1L);

        // Then
        assertThat(credentialOptionalResult).isEmpty();
    }

    @Test
    void shouldReturnTrueWhenCheckExistByIdWithExistedId() {
        // Given
        credentialRepository.save(credential);

        // When
        boolean isCredentialExisted = credentialRepository.existsById(1L);

        // Then
        assertThat(isCredentialExisted).isTrue();
    }

    @Test
    void shouldReturnFalseWhenCheckExistByIdWithNotExistedId() {
        // Given

        // When
        boolean isCredentialExisted = credentialRepository.existsById(1L);

        // Then
        assertThat(isCredentialExisted).isFalse();
    }

    @Test
    void shouldUpdateACredentialWhenSaveWithExistedId() {
        // Given
        credentialRepository.save(credential);

        Credential credentialUpdate = new Credential();
        credentialUpdate.setId(1L);
        credentialUpdate.setName("name1");
        credentialUpdate.setUsername("username1");
        credentialUpdate.setPassword("password1");

        // When
        credentialRepository.save(credentialUpdate);

        // Then
        Optional<Credential> credentialOptionalResult = credentialRepository.findById(1L);
        assertThat(credentialOptionalResult).isPresent();

        Credential credentialResult = credentialOptionalResult.get();
        assertThat(credentialResult.getId()).isEqualTo(credentialUpdate.getId());
        assertThat(credentialResult.getName()).isEqualTo(credentialUpdate.getName());
        assertThat(credentialResult.getUsername()).isEqualTo(credentialUpdate.getUsername());
        assertThat(credentialResult.getPassword()).isEqualTo(credentialUpdate.getPassword());
    }

    @Test
    void shouldCreateACredentialWhenSaveWithNotExistedId() {
        // Given
        Credential credential2 = new Credential();
        credential2.setId(2L);
        credential2.setName("name2");
        credential2.setUsername("username2");
        credential2.setPassword("password2");

        credentialRepository.save(credential);

        // When
        credentialRepository.save(credential2);

        // Then
        List<Credential> credentialListResult = credentialRepository.findAll();
        assertThat(credentialListResult).hasSize(2);

        Credential credentialResult1 = credentialListResult.get(0);
        assertThat(credentialResult1.getId()).isEqualTo(credential.getId());
        assertThat(credentialResult1.getName()).isEqualTo(credential.getName());
        assertThat(credentialResult1.getUsername()).isEqualTo(credential.getUsername());
        assertThat(credentialResult1.getPassword()).isEqualTo(credential.getPassword());

        Credential credentialResult2 = credentialListResult.get(1);
        assertThat(credentialResult2.getId()).isEqualTo(credential2.getId());
        assertThat(credentialResult2.getName()).isEqualTo(credential2.getName());
        assertThat(credentialResult2.getUsername()).isEqualTo(credential2.getUsername());
        assertThat(credentialResult2.getPassword()).isEqualTo(credential2.getPassword());
    }

    @Test
    void shouldThrowExceptionWhenSaveWithDuplicateName() {
        // Given
        credentialRepository.save(credential);

        Credential credentialDuplicate = new Credential();
        credentialDuplicate.setId(2L);
        credentialDuplicate.setName("name");
        credentialDuplicate.setUsername("username2");
        credentialDuplicate.setPassword("password2");

        // When
        Throwable throwable = catchThrowable(() -> {
            credentialRepository.save(credentialDuplicate);

            // The exception was hold until this method invokes?
            credentialRepository.findAll();
        });

        // Then
        assertThat(throwable).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldDeleteACredentialWhenDeleteByIdWithExistedId() {
        // Given
        credentialRepository.save(credential);

        // When
        credentialRepository.deleteById(1L);

        // Then
        Optional<Credential> credentialOptionalResult = credentialRepository.findById(1L);
        assertThat(credentialOptionalResult).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenDeleteByIdWithNotExistedId() {
        // Given

        // When
        Throwable throwable = catchThrowable(() -> credentialRepository.deleteById(1L));

        // Then
        assertThat(throwable).isInstanceOf(EmptyResultDataAccessException.class);
    }
}