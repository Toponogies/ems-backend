package vn.com.tma.emsbackend.repository;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;
import vn.com.tma.emsbackend.entity.Credential;
import vn.com.tma.emsbackend.util.database.ResetDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@ResetDatabase
public class CredentialRepositoryTest {
    @Autowired
    private CredentialRepository credentialRepository;

    @Test
    public void shouldFindValidCredentialsWhenFindAllWithNotEmptyRepository() {
        // Given
        Credential credential1 = new Credential();
        credential1.setId(1L);
        credential1.setName("name1");
        credential1.setUsername("username1");
        credential1.setPassword("password1");

        Credential credential2 = new Credential();
        credential2.setId(2L);
        credential2.setName("name2");
        credential2.setUsername("username2");
        credential2.setPassword("password2");

        List<Credential> credentialList = new ArrayList<>();
        credentialList.add(credential1);
        credentialList.add(credential2);

        credentialRepository.saveAll(credentialList);

        // When
        List<Credential> credentialListResult = credentialRepository.findAll();

        // Then
        Assertions.assertThat(credentialListResult).as("should have 2 members in the List").hasSize(2);
        Assertions.assertThat(credentialListResult).as("should match data in items")
                .extracting(Credential::getId, Credential::getName, Credential::getUsername, Credential::getPassword)
                .containsExactlyInAnyOrder(
                        tuple(1L, "name1", "username1", "password1"),
                        tuple(2L, "name2", "username2", "password2")
                );
    }

    @Test
    public void shouldFindNoCredentialWhenFindAllWithEmptyRepository() {
        // Given

        // When
        List<Credential> credentialListResult = credentialRepository.findAll();

        // Then
        Assertions.assertThat(credentialListResult).as("should have 0 member in the List").hasSize(0);
    }

    @Test
    public void shouldFindAValidCredentialWhenFindByIdWithExistedId() {
        // Given
        Credential credential = new Credential();
        credential.setId(1L);
        credential.setName("name");
        credential.setUsername("username");
        credential.setPassword("password");

        credentialRepository.save(credential);

        // When
        Optional<Credential> credentialOptionalResult = credentialRepository.findById(1L);

        // Then
        Assertions.assertThat(credentialOptionalResult).as("should have a Credential in the Option").isPresent();

        Credential credentialResult = credentialOptionalResult.get();
        Assertions.assertThat(credentialResult.getId()).isEqualTo(credential.getId());
        Assertions.assertThat(credentialResult.getName()).isEqualTo(credential.getName());
        Assertions.assertThat(credentialResult.getUsername()).isEqualTo(credential.getUsername());
        Assertions.assertThat(credentialResult.getPassword()).isEqualTo(credential.getPassword());
    }

    @Test
    public void shouldFindNoCredentialWhenFindByIdWithNotExistedId() {
        // Given

        // When
        Optional<Credential> credentialOptionalResult = credentialRepository.findById(1L);

        // Then
        Assertions.assertThat(credentialOptionalResult).as("should not have a Credential in the Option").isEmpty();
    }

    @Test
    public void shouldReturnTrueWhenCheckExistByIdWithExistedId() {
        // Given
        Credential credential = new Credential();
        credential.setId(1L);
        credential.setName("name");
        credential.setUsername("username");
        credential.setPassword("password");

        credentialRepository.save(credential);

        // When
        boolean isCredentialExisted = credentialRepository.existsById(1L);

        // Then
        Assertions.assertThat(isCredentialExisted).as("should return true").isTrue();
    }

    @Test
    public void shouldReturnFalseWhenCheckExistByIdWithNotExistedId() {
        // Given

        // When
        boolean isCredentialExisted = credentialRepository.existsById(1L);

        // Then
        Assertions.assertThat(isCredentialExisted).as("should return false").isFalse();
    }

    @Test
    public void shouldUpdateACredentialWhenSaveWithExistedId() {
        // Given
        Credential credential = new Credential();
        credential.setId(1L);
        credential.setName("name");
        credential.setUsername("username");
        credential.setPassword("password");

        Credential credentialUpdate = new Credential();
        credentialUpdate.setId(1L);
        credentialUpdate.setName("name1");
        credentialUpdate.setUsername("username1");
        credentialUpdate.setPassword("password1");

        credentialRepository.save(credential);

        // When
        credentialRepository.save(credentialUpdate);

        // Then
        Optional<Credential> credentialOptionalResult = credentialRepository.findById(1L);

        Assertions.assertThat(credentialOptionalResult).as("should have a Credential in the Option").isPresent();

        Credential credentialResult = credentialOptionalResult.get();
        Assertions.assertThat(credentialResult.getId()).isEqualTo(credentialUpdate.getId());
        Assertions.assertThat(credentialResult.getName()).isEqualTo(credentialUpdate.getName());
        Assertions.assertThat(credentialResult.getUsername()).isEqualTo(credentialUpdate.getUsername());
        Assertions.assertThat(credentialResult.getPassword()).isEqualTo(credentialUpdate.getPassword());
    }

    @Test
    public void shouldCreateACredentialWhenSaveWithNotExistedId() {
        // Given
        Credential credential1 = new Credential();
        credential1.setId(1L);
        credential1.setName("name1");
        credential1.setUsername("username1");
        credential1.setPassword("password1");

        Credential credential2 = new Credential();
        credential2.setId(2L);
        credential2.setName("name2");
        credential2.setUsername("username2");
        credential2.setPassword("password2");

        credentialRepository.save(credential1);

        // When
        credentialRepository.save(credential2);

        // Then
        List<Credential> credentialListResult = credentialRepository.findAll();
        Assertions.assertThat(credentialListResult).as("should have 2 members in the List").hasSize(2);
        Assertions.assertThat(credentialListResult).as("should match data in items")
                .extracting(Credential::getId, Credential::getName, Credential::getUsername, Credential::getPassword)
                .containsExactlyInAnyOrder(
                        tuple(1L, "name1", "username1", "password1"),
                        tuple(2L, "name2", "username2", "password2")
                );
    }

    @Test
    public void shouldThrowExceptionWhenSaveWithDuplicateName() {
        // Given
        Credential credential = new Credential();
        credential.setId(1L);
        credential.setName("name");
        credential.setUsername("username1");
        credential.setPassword("password1");

        Credential credentialDuplicate = new Credential();
        credentialDuplicate.setId(2L);
        credentialDuplicate.setName("name");
        credentialDuplicate.setUsername("username2");
        credentialDuplicate.setPassword("password2");

        credentialRepository.save(credential);

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
    public void shouldDeleteACredentialWhenDeleteByIdWithExistedId() {
        // Given
        Credential credential = new Credential();
        credential.setId(1L);
        credential.setName("name");
        credential.setUsername("username1");
        credential.setPassword("password1");

        credentialRepository.save(credential);

        // When
        credentialRepository.deleteById(1L);

        // Then
        Optional<Credential> credentialOptionalResult = credentialRepository.findById(1L);

        Assertions.assertThat(credentialOptionalResult).as("should have no Credential in the Option").isEmpty();
    }

    @Test
    public void shouldThrowExceptionWhenDeleteByIdWithNotExistedId() {
        // Given

        // When
        Throwable throwable = catchThrowable(() -> {
            credentialRepository.deleteById(1L);
        });

        // Then
        assertThat(throwable)
                .as("should throw the correct exception")
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}