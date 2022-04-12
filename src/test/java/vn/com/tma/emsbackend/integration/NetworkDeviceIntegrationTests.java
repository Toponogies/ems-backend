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
import vn.com.tma.emsbackend.dto.NetworkDeviceDto;
import vn.com.tma.emsbackend.dto.NetworkDeviceRequestDto;
import vn.com.tma.emsbackend.entity.Credential;
import vn.com.tma.emsbackend.entity.NetworkDevice;
import vn.com.tma.emsbackend.repository.CredentialRepository;
import vn.com.tma.emsbackend.repository.NetworkDeviceRepository;
import vn.com.tma.emsbackend.util.database.ResetDatabase;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ResetDatabase
class NetworkDeviceIntegrationTests {

        @Autowired
        private NetworkDeviceRepository networkDeviceRepository;

        @Autowired
        private CredentialRepository credentialRepository;

        @Autowired
        private TestRestTemplate testRestTemplate;

        @Autowired
        private Mapper mapper;

        private final List<NetworkDevice> networkDevices = new ArrayList<>();

        Credential credential = new Credential();

        @BeforeEach
        void setUp() {
                credential.setId(1L);
                credential.setName("admin");
                credential.setPassword("admin");
                credential.setUsername("admin");
                credentialRepository.save(credential);

                NetworkDevice networkDevice1 = new NetworkDevice();
                networkDevice1.setIpAddress("10.10.88.91");
                networkDevice1.setLabel("10.10.88.91");
                networkDevice1.setSshPort(22);
                networkDevice1.setCredential(credential);

                NetworkDevice networkDevice2 = new NetworkDevice();
                networkDevice2.setIpAddress("10.10.88.92");
                networkDevice2.setLabel("10.10.88.92");
                networkDevice2.setSshPort(22);
                networkDevice2.setCredential(credential);

                networkDevices.add(networkDevice1);
                networkDevices.add(networkDevice2);

        }

        @Test
        void shouldReturn200AndValidNetworkDeices() {
                // Given
                networkDeviceRepository.saveAll(networkDevices);
                NetworkDevice networkDevice1 = networkDevices.get(0);
                NetworkDevice networkDevice2 = networkDevices.get(1);

                // When
                ResponseEntity<List<NetworkDeviceDto>> responseEntity = this.testRestTemplate.exchange(
                                "/api/v1/devices",
                                HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });

                // Then
                List<NetworkDeviceDto> networkDeviceDtos = responseEntity.getBody();
                Assertions.assertThat(responseEntity.getStatusCode()).as("Status code equal OK")
                                .isEqualTo(HttpStatus.OK);
                Assertions.assertThat(networkDeviceDtos).as("Has size as 2").hasSize(2);
                Assertions.assertThat(networkDeviceDtos)
                                .extracting(NetworkDeviceDto::getIpAddress, NetworkDeviceDto::getLabel,
                                        NetworkDeviceDto::getPort,
                                        NetworkDeviceDto::getCredentialName)
                                .contains(
                                                tuple(networkDevice1.getIpAddress(), networkDevice1.getLabel(),
                                                                networkDevice1.getSshPort(),
                                                                credential.getName()),
                                                tuple(networkDevice2.getIpAddress(), networkDevice2.getLabel(),
                                                                networkDevice2.getSshPort(),
                                                                credential.getName()));
        }

        @Test
        void shouldReturn200AndValidNetworkDevice() {
                // Given
                NetworkDevice networkDevice = networkDeviceRepository.save(networkDevices.get(0));

                // When
                ResponseEntity<NetworkDeviceDto> responseEntity = this.testRestTemplate.exchange(
                                "/api/v1/devices/" + networkDevice.getId(),
                                HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
                // Then
                NetworkDeviceDto networkDeviceDto = responseEntity.getBody();
                Assertions.assertThat(responseEntity.getStatusCode()).as("Status code equal OK")
                                .isEqualTo(HttpStatus.OK);
                assert networkDeviceDto != null;
                Assertions.assertThat(networkDeviceDto).usingRecursiveComparison()
                                .isEqualTo(mapper.map(networkDevice, networkDeviceDto.getClass()));
        }

        @Test
        void shouldReturn404WhenGetNotExistnetworkDevice() {
                // When
                ResponseEntity<NetworkDeviceDto> responseEntity = this.testRestTemplate.exchange(
                                "/api/v1/devices/0",
                                HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
                // Then
                Assertions.assertThat(responseEntity.getStatusCode()).as("Status code equal NOTFOUND")
                                .isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        void shouldReturn404WhenDeleteNotExistNetworkDevice() {
                // When
                ResponseEntity<NetworkDeviceDto> responseEntity = this.testRestTemplate.exchange(
                                "/api/v1/devices/0",
                                HttpMethod.DELETE, null, new ParameterizedTypeReference<>() {
                        });
                // Then
                Assertions.assertThat(responseEntity.getStatusCode()).as("Status code equal NOTFOUND")
                                .isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        void shouldReturn204WhenDeleteExistNetworkDevice() {
                // Given
                NetworkDevice networkDevice = networkDeviceRepository.save(networkDevices.get(0));

                // When
                ResponseEntity<NetworkDeviceDto> responseEntity = this.testRestTemplate.exchange(
                                "/api/v1/devices/" + networkDevice.getId(),
                                HttpMethod.DELETE, null, new ParameterizedTypeReference<>() {
                        });

                // Then
                Assertions.assertThat(responseEntity.getStatusCode()).as("Status code equal NOCONTENT")
                                .isEqualTo(HttpStatus.NO_CONTENT);
                Assertions.assertThat(networkDeviceRepository.findById(networkDevice.getId())).isEmpty();
        }

        @Test
        void shouldReturn201WhenCreatenetworkDevice() {

                // When
                HttpEntity<NetworkDeviceRequestDto> httpEntity = new HttpEntity<>(
                        mapper.map(networkDevices.get(0), NetworkDeviceRequestDto.class));
                ResponseEntity<NetworkDeviceRequestDto> responseEntity = this.testRestTemplate.exchange(
                                "/api/v1/devices",
                                HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
                        });

                // Then
                NetworkDeviceRequestDto networkDeviceDto = responseEntity.getBody();
                List<NetworkDevice> networkDevices = networkDeviceRepository.findAll();
                Assertions.assertThat(networkDevices).hasSize(1);
                NetworkDevice networkDevice = networkDevices.get(0);

                Credential credential = new Credential();
                assert networkDeviceDto != null;
                credential.setId(networkDeviceDto.getCredentialId());
                networkDevice.setCredential(credential);

                Assertions.assertThat(responseEntity.getStatusCode()).as("Status code equal CREATE")
                                .isEqualTo(HttpStatus.CREATED);
                
                Assertions.assertThat(mapper.map(networkDevice, NetworkDeviceRequestDto.class))
                                .usingRecursiveComparison()
                                .isEqualTo(networkDeviceDto);
        }

}
