package vn.com.tma.emsbackend.integration;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;

import static org.assertj.core.groups.Tuple.tuple;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import vn.com.tma.emsbackend.common.Mapper;
import vn.com.tma.emsbackend.dto.ManagedDeviceDto;
import vn.com.tma.emsbackend.dto.ManagedDeviceRequestDto;
import vn.com.tma.emsbackend.entity.Credential;
import vn.com.tma.emsbackend.entity.ManagedDevice;
import vn.com.tma.emsbackend.repository.CredentialRepository;
import vn.com.tma.emsbackend.repository.ManagedDeviceRepository;
import vn.com.tma.emsbackend.util.database.ResetDatabase;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ResetDatabase
public class ManagedDeviceIntegrationTests {

        @Autowired
        private ManagedDeviceRepository managedDeviceRepository;

        @Autowired
        private CredentialRepository credentialRepository;

        @Autowired
        private TestRestTemplate testRestTemplate;

        @Autowired
        private Mapper mapper;

        private List<ManagedDevice> managedDevices = new ArrayList<>();

        Credential credential = new Credential();

        @BeforeEach
        public void setUp() {
                credential.setId(1L);
                credential.setName("admin");
                credential.setPassword("admin");
                credential.setUsername("admin");
                credentialRepository.save(credential);

                ManagedDevice managedDevice1 = new ManagedDevice();
                managedDevice1.setIpAddress("10.10.88.91");
                managedDevice1.setLabel("10.10.88.91");
                managedDevice1.setSSHPort(22);
                managedDevice1.setCredential(credential);

                ManagedDevice managedDevice2 = new ManagedDevice();
                managedDevice2.setIpAddress("10.10.88.92");
                managedDevice2.setLabel("10.10.88.92");
                managedDevice2.setSSHPort(22);
                managedDevice2.setCredential(credential);

                managedDevices.add(managedDevice1);
                managedDevices.add(managedDevice2);

        }

        @Test
        public void shouldReturn200AndValidManagedDeices() {
                // Given
                managedDeviceRepository.saveAll(managedDevices);
                ManagedDevice managedDevice1 = managedDevices.get(0);
                ManagedDevice managedDevice2 = managedDevices.get(1);

                // When
                ResponseEntity<List<ManagedDeviceDto>> responseEntity = this.testRestTemplate.exchange(
                                "/api/v1/devices",
                                HttpMethod.GET, null, new ParameterizedTypeReference<List<ManagedDeviceDto>>() {
                                });

                // Then
                List<ManagedDeviceDto> managedDeviceDtos = responseEntity.getBody();
                Assertions.assertThat(responseEntity.getStatusCode()).as("Status code equal OK")
                                .isEqualTo(HttpStatus.OK);
                Assertions.assertThat(managedDeviceDtos.size()).as("Has size as 2").isEqualTo(2);
                Assertions.assertThat(managedDeviceDtos)
                                .extracting(ManagedDeviceDto::getIpAddress, ManagedDeviceDto::getLabel,
                                                ManagedDeviceDto::getPort,
                                                ManagedDeviceDto::getCredentialName)
                                .contains(
                                                tuple(managedDevice1.getIpAddress(), managedDevice1.getLabel(),
                                                                managedDevice1.getSSHPort(),
                                                                credential.getName()),
                                                tuple(managedDevice2.getIpAddress(), managedDevice2.getLabel(),
                                                                managedDevice2.getSSHPort(),
                                                                credential.getName()));
        }

        @Test
        public void shouldReturn200AndValidManagedDevice() {
                // Given
                ManagedDevice managedDevice = managedDeviceRepository.save(managedDevices.get(0));

                // When
                ResponseEntity<ManagedDeviceDto> responseEntity = this.testRestTemplate.exchange(
                                "/api/v1/devices/" + managedDevice.getId(),
                                HttpMethod.GET, null, new ParameterizedTypeReference<ManagedDeviceDto>() {
                                });
                // Then
                ManagedDeviceDto managedDeviceDto = responseEntity.getBody();
                Assertions.assertThat(responseEntity.getStatusCode()).as("Status code equal OK")
                                .isEqualTo(HttpStatus.OK);
                Assertions.assertThat(managedDeviceDto).usingRecursiveComparison()
                                .isEqualTo(mapper.map(managedDevice, managedDeviceDto.getClass()));
        }

        @Test
        public void shouldReturn404WhenGetNotExistManagedDevice() {
                // When
                ResponseEntity<ManagedDeviceDto> responseEntity = this.testRestTemplate.exchange(
                                "/api/v1/devices/0",
                                HttpMethod.GET, null, new ParameterizedTypeReference<ManagedDeviceDto>() {
                                });
                // Then
                Assertions.assertThat(responseEntity.getStatusCode()).as("Status code equal NOTFOUND")
                                .isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        public void shouldReturn404WhenDeleteNotExistManagedDevice() {
                // When
                ResponseEntity<ManagedDeviceDto> responseEntity = this.testRestTemplate.exchange(
                                "/api/v1/devices/0",
                                HttpMethod.DELETE, null, new ParameterizedTypeReference<ManagedDeviceDto>() {
                                });
                // Then
                Assertions.assertThat(responseEntity.getStatusCode()).as("Status code equal NOTFOUND")
                                .isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        public void shouldReturn204WhenDeleteExistManagedDevice() {
                // Given
                ManagedDevice managedDevice = managedDeviceRepository.save(managedDevices.get(0));

                // When
                ResponseEntity<ManagedDeviceDto> responseEntity = this.testRestTemplate.exchange(
                                "/api/v1/devices/" + managedDevice.getId(),
                                HttpMethod.DELETE, null, new ParameterizedTypeReference<ManagedDeviceDto>() {
                                });

                // Then
                Assertions.assertThat(responseEntity.getStatusCode()).as("Status code equal NOCONTENT")
                                .isEqualTo(HttpStatus.NO_CONTENT);
                Assertions.assertThat(managedDeviceRepository.findById(managedDevice.getId())).isEmpty();
        }

        @Test
        public void shouldReturn201WhenCreateManagedDevice() {

                // When
                HttpEntity<ManagedDeviceRequestDto> httpEntity = new HttpEntity<ManagedDeviceRequestDto>(
                                mapper.map(managedDevices.get(0), ManagedDeviceRequestDto.class));
                ResponseEntity<ManagedDeviceDto> responseEntity = this.testRestTemplate.exchange(
                                "/api/v1/devices",
                                HttpMethod.POST, httpEntity, new ParameterizedTypeReference<ManagedDeviceDto>() {
                                });

                // Then
                ManagedDeviceDto managedDeviceDto = responseEntity.getBody();
                List<ManagedDevice> managedDevices = managedDeviceRepository.findAll();
                Assertions.assertThat(managedDevices).hasSize(1);
                ManagedDevice managedDevice = managedDevices.get(0);

                Credential credential = new Credential();
                credential.setId(managedDeviceDto.getCredentialId());
                managedDevice.setCredential(credential);

                Assertions.assertThat(responseEntity.getStatusCode()).as("Status code equal CREATE")
                                .isEqualTo(HttpStatus.CREATED);
                
                Assertions.assertThat(mapper.map(managedDevice, ManagedDeviceDto.class))
                                .usingRecursiveComparison()
                                .isEqualTo(managedDeviceDto);
        }

}
