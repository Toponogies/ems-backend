package vn.com.tma.emsbackend.unit.repository;

import org.assertj.core.api.Assertions;
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
import static org.assertj.core.api.AssertionsForClassTypes.*;

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
        assertThat(credentialListResult).as("should have 1 member in the List").hasSize(1);

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
        Assertions.assertThat(credentialListResult).as("should have 0 member in the List").hasSize(0);
    }

    @Test
    void shouldFindAValidCredentialWhenFindByIdWithExistedId() {
        // Given
        credentialRepository.save(credential);

        // When
        Optional<Credential> credentialOptionalResult = credentialRepository.findById(1L);

        // Then
        assertThat(credentialOptionalResult).as("should have a Credential in the Option").isPresent();

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
        assertThat(credentialOptionalResult).as("should not have a Credential in the Option").isEmpty();
    }

    @Test
    void shouldReturnTrueWhenCheckExistByIdWithExistedId() {
        // Given
        credentialRepository.save(credential);

        // When
        boolean isCredentialExisted = credentialRepository.existsById(1L);

        // Then
        assertThat(isCredentialExisted).as("should return true").isTrue();
    }

    @Test
    void shouldReturnFalseWhenCheckExistByIdWithNotExistedId() {
        // Given

        // When
        boolean isCredentialExisted = credentialRepository.existsById(1L);

        // Then
        assertThat(isCredentialExisted).as("should return false").isFalse();
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
        assertThat(credentialOptionalResult).as("should have a Credential in the Option").isPresent();

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
        Assertions.assertThat(credentialListResult).as("should have 2 members in the List").hasSize(2);
        Assertions.assertThat(credentialListResult).as("should match data in items")
                .extracting(Credential::getId, Credential::getName, Credential::getUsername, Credential::getPassword)
                .containsExactlyInAnyOrder(
                        tuple(1L, "name", "username", "password"),
                        tuple(2L, "name2", "username2", "password2")
                );
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
        assertThat(throwable)
                .as("should throw the correct exception")
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("CREDENTIALS(NAME)");
    }

    @Test
    void shouldDeleteACredentialWhenDeleteByIdWithExistedId() {
        // Given
        credentialRepository.save(credential);

        // When
        credentialRepository.deleteById(1L);

        // Then
        Optional<Credential> credentialOptionalResult = credentialRepository.findById(1L);
        Assertions.assertThat(credentialOptionalResult).as("should have no Credential in the Option").isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenDeleteByIdWithNotExistedId() {
        // Given

        // When
        Throwable throwable = catchThrowable(() -> credentialRepository.deleteById(1L));

        // Then
        assertThat(throwable)
                .as("should throw the correct exception")
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}