package vn.com.tma.emsbackend.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import vn.com.tma.emsbackend.common.enums.Enum;
import vn.com.tma.emsbackend.model.dto.ErrorDTO;
import vn.com.tma.emsbackend.model.dto.NetworkDeviceDTO;
import vn.com.tma.emsbackend.model.entity.Credential;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.repository.CredentialRepository;
import vn.com.tma.emsbackend.repository.NetworkDeviceRepository;
import vn.com.tma.emsbackend.service.ssh.utils.ResyncQueueManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class NetworkDeviceIntegrationTests {
    @Autowired
    ResyncQueueManager resyncQueueManager;
    @Autowired
    CredentialRepository credentialRepository;
    @Autowired
    NetworkDeviceRepository networkDeviceRepository;
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private String baseUrl;

    private Credential genericCredential;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;

        genericCredential = new Credential();
        genericCredential.setName("admin");
        genericCredential.setPassword("admin");
        genericCredential.setUsername("admin");
        genericCredential.setId(1L);

        credentialRepository.save(genericCredential);

        NetworkDevice networkDevice = new NetworkDevice();
        networkDevice.setIpAddress("10.220.4.10");
        networkDevice.setLabel("10.220.4.10");
        networkDevice.setCredential(genericCredential);
        networkDevice.setState(Enum.NetworkDeviceState.OUT_OF_SERVICE);
        networkDevice.setSshPort(22);
        networkDevice.setDeviceType(Enum.NetworkDeviceType.VCX);
        networkDevice.setResyncStatus(Enum.ResyncStatus.DONE);
        genericCredential.setId(1L);


        networkDeviceRepository.save(networkDevice);
    }

    @Test
    void shouldReturn200WhenGetANetworkDevice() {
        String url = baseUrl + "/api/v1/devices";
        ResponseEntity<List<NetworkDeviceDTO>> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().size()).isOne();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldReturn201AndAddNetworkDeviceWhenAddNewNetworkDevice() {
        String url = baseUrl + "/api/v1/devices";

        //Give
        NetworkDeviceDTO newNetworkDevice = new NetworkDeviceDTO();
        newNetworkDevice.setIpAddress("10.220.4.5");
        newNetworkDevice.setLabel("10.220.4.5");
        newNetworkDevice.setCredential(genericCredential.getName());
        newNetworkDevice.setSshPort(22);

        //When
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NetworkDeviceDTO> requestBody = new HttpEntity<>(newNetworkDevice, headers);

        //Then
        ResponseEntity<NetworkDeviceDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, requestBody, NetworkDeviceDTO.class);

        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(resyncQueueManager.getResyncStatus(responseEntity.getBody().getId())).isEqualTo(Enum.ResyncStatus.ONGOING);
        url = url + "/" + responseEntity.getBody().getId().toString();
        ResponseEntity<NetworkDeviceDTO> responseEntity1 = testRestTemplate.exchange(url, HttpMethod.GET, null, NetworkDeviceDTO.class);
        assertThat(responseEntity1.getBody()).isNotNull();
        assertThat(responseEntity1.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldReturn200AndAllNetworkDeviceWhenGetAllNetworkDevice() {
        String url = baseUrl + "/api/v1/devices";
        ResponseEntity<List<NetworkDeviceDTO>> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().size()).isOne();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldReturn200AndDeleteNetworkDeviceWhenDeleteNetworkDevice() {
        String url = baseUrl + "/api/v1/devices/";

        //Give
        NetworkDeviceDTO newNetworkDevice = new NetworkDeviceDTO();
        newNetworkDevice.setIpAddress("10.220.4.5");
        newNetworkDevice.setLabel("10.220.4.5");
        newNetworkDevice.setCredential(genericCredential.getName());
        newNetworkDevice.setSshPort(22);

        //When
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NetworkDeviceDTO> requestBody = new HttpEntity<>(newNetworkDevice, headers);
        ResponseEntity<NetworkDeviceDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, requestBody, NetworkDeviceDTO.class);

        newNetworkDevice = responseEntity.getBody();

        assert newNetworkDevice != null;
        url += newNetworkDevice.getId();
        assertThat(responseEntity.getBody()).isNotNull();


        ResponseEntity<Void> deleteResponseEntity = testRestTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);

        //then
        assertThat(deleteResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        ResponseEntity<Void> responseEntityGetDevice = testRestTemplate.exchange(url, HttpMethod.GET, null, Void.class);
    }

    @Test
    void shouldReturn404WhenDeleteNotExistNetworkDevice() {
        //When
        String url = baseUrl + "/api/devices/10";
        ResponseEntity<Void> deleteResponseEntity = testRestTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
        //Then
        assertThat(deleteResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldReturn200AndModifyDeviceAndAddQueueWhenUpdateNetworkDevice() {
        String url = baseUrl + "/api/v1/devices/";

        //Give
        NetworkDeviceDTO newNetworkDevice = new NetworkDeviceDTO();
        newNetworkDevice.setIpAddress("10.220.4.5");
        newNetworkDevice.setLabel("10.220.4.5");
        newNetworkDevice.setCredential(genericCredential.getName());
        newNetworkDevice.setSshPort(22);

        //When
        //Add new device
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NetworkDeviceDTO> requestBody = new HttpEntity<>(newNetworkDevice, headers);
        ResponseEntity<NetworkDeviceDTO> addResponseEntity = testRestTemplate.exchange(url, HttpMethod.POST, requestBody, NetworkDeviceDTO.class);

        assert addResponseEntity.getBody() != null;
        newNetworkDevice.setId(addResponseEntity.getBody().getId());

        assertThat(addResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(resyncQueueManager.getResyncStatus(addResponseEntity.getBody().getId())).isEqualTo(Enum.ResyncStatus.ONGOING);
        resyncQueueManager.popResynchronizingQueue(addResponseEntity.getBody().getId());


        //Update
        url += newNetworkDevice.getId();

        newNetworkDevice.setIpAddress("10.220.4.6");
        newNetworkDevice.setSshPort(12022);
        HttpHeaders updateHeader = new HttpHeaders();
        updateHeader.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NetworkDeviceDTO> updateRequestBody = new HttpEntity<>(newNetworkDevice, updateHeader);
        ResponseEntity<NetworkDeviceDTO> updateResponseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, updateRequestBody, NetworkDeviceDTO.class);

        assertThat(updateResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resyncQueueManager.getResyncStatus(addResponseEntity.getBody().getId())).isEqualTo(Enum.ResyncStatus.ONGOING);

        //Get
        ResponseEntity<NetworkDeviceDTO> getResponseEntity = testRestTemplate.exchange(url, HttpMethod.GET, null, NetworkDeviceDTO.class);
        assert getResponseEntity.getBody() != null;
        assertThat(getResponseEntity.getBody().getIpAddress()).isEqualTo(newNetworkDevice.getIpAddress());
    }

    @Test
    void shouldReturn404WhenGetNotExistDevice() {
        String url = baseUrl + "/api/v1/devices/3";
        ResponseEntity<ErrorDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, null, ErrorDTO.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldReturn400WhenAddDeviceWithWrongIpAddressFormat() {
        //Given
        String url = baseUrl + "/api/v1/devices";
        NetworkDeviceDTO newWrongIpNetworkDevice = new NetworkDeviceDTO();
        newWrongIpNetworkDevice.setIpAddress("10.220.4.20676423");
        newWrongIpNetworkDevice.setLabel("10.220.4.5");
        newWrongIpNetworkDevice.setCredential(genericCredential.getName());
        newWrongIpNetworkDevice.setSshPort(22);

        //When
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NetworkDeviceDTO> requestBody = new HttpEntity<>(newWrongIpNetworkDevice, headers);

        //Then
        ResponseEntity<ErrorDTO> addResponseEntity = testRestTemplate.exchange(url, HttpMethod.POST, requestBody, ErrorDTO.class);
        assertThat(addResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldReturn400WhenAddDeviceWithNotExistCredential() {
        //Given
        String url = baseUrl + "/api/v1/devices";
        NetworkDeviceDTO newWrongIpNetworkDevice = new NetworkDeviceDTO();
        newWrongIpNetworkDevice.setIpAddress("10.220.4.5");
        newWrongIpNetworkDevice.setLabel("10.220.4.5");
        newWrongIpNetworkDevice.setCredential("not_exist_credential");
        newWrongIpNetworkDevice.setSshPort(22);

        //When
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NetworkDeviceDTO> requestBody = new HttpEntity<>(newWrongIpNetworkDevice, headers);

        ResponseEntity<ErrorDTO> addResponseEntity = testRestTemplate.exchange(url, HttpMethod.POST, requestBody, ErrorDTO.class);

        //Then
        assertThat(addResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        NetworkDevice networkDevice = networkDeviceRepository.findByIpAddress("10.220.4.5");
        assertThat(networkDevice).isNull();
    }

    @Test
    void shouldReturn400WhenAddDeviceWithExistedLabel() {
        //Given
        String url = baseUrl + "/api/v1/devices";
        NetworkDeviceDTO newWrongIpNetworkDevice = new NetworkDeviceDTO();
        newWrongIpNetworkDevice.setIpAddress("10.220.4.5");
        newWrongIpNetworkDevice.setLabel("10.220.4.10");
        newWrongIpNetworkDevice.setCredential(genericCredential.getName());
        newWrongIpNetworkDevice.setSshPort(22);

        //When
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NetworkDeviceDTO> requestBody = new HttpEntity<>(newWrongIpNetworkDevice, headers);

        ResponseEntity<ErrorDTO> addResponseEntity = testRestTemplate.exchange(url, HttpMethod.POST, requestBody, ErrorDTO.class);
        //Then
        assertThat(addResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        NetworkDevice networkDevice = networkDeviceRepository.findByIpAddress("10.220.4.5");
        assertThat(networkDevice).isNull();
    }

    @Test
    void shouldReturn400WhenAddDeviceWithExistedIp() {
        String url = baseUrl + "/api/v1/devices";
        //Given
        NetworkDeviceDTO newWrongIpNetworkDevice = new NetworkDeviceDTO();
        newWrongIpNetworkDevice.setIpAddress("10.220.4.10");
        newWrongIpNetworkDevice.setLabel("new_label");
        newWrongIpNetworkDevice.setCredential(genericCredential.getName());
        newWrongIpNetworkDevice.setSshPort(22);

        //When
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NetworkDeviceDTO> requestBody = new HttpEntity<>(newWrongIpNetworkDevice, headers);
        ResponseEntity<ErrorDTO> addResponseEntity = testRestTemplate.exchange(url, HttpMethod.POST, requestBody, ErrorDTO.class);

        //Then
        assertThat(addResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        NetworkDevice networkDevice = networkDeviceRepository.findByIpAddress("10.220.4.10");
        assertThat(networkDevice).isNull();
    }

    @Test
    void shouldReturn200AndDevicesWithTypeWhenGetDeviceByType() {
        String url = baseUrl + "/api/v1/devices/type/VCX";
        //Give
        NetworkDevice networkDevice = new NetworkDevice();
        networkDevice.setIpAddress("10.220.4.5");
        networkDevice.setLabel("10.220.4.5");
        networkDevice.setCredential(genericCredential);
        networkDevice.setState(Enum.NetworkDeviceState.OUT_OF_SERVICE);
        networkDevice.setSshPort(22);
        networkDevice.setDeviceType(Enum.NetworkDeviceType.GT);
        networkDevice.setResyncStatus(Enum.ResyncStatus.DONE);
        networkDeviceRepository.save(networkDevice);
        //When
        ResponseEntity<List<NetworkDeviceDTO>> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).hasSize(1);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

    }
}
