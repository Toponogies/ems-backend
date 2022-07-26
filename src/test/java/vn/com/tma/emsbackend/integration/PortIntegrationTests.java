package vn.com.tma.emsbackend.integration;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import vn.com.tma.emsbackend.common.enums.Enum;
import vn.com.tma.emsbackend.model.dto.PortDTO;
import vn.com.tma.emsbackend.model.entity.Credential;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.model.entity.Port;
import vn.com.tma.emsbackend.repository.CredentialRepository;
import vn.com.tma.emsbackend.repository.NetworkDeviceRepository;
import vn.com.tma.emsbackend.repository.PortRepository;
import vn.com.tma.emsbackend.util.auth.LoginUtil;
import vn.com.tma.emsbackend.util.entity.creator.PortCreator;
import vn.com.tma.emsbackend.util.entity.DTO.LoginDTO;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PortIntegrationTests {

    @Autowired
    private CredentialRepository credentialRepository;
    @Autowired
    private NetworkDeviceRepository networkDeviceRepository;
    @Autowired
    private PortRepository portRepository;
    @Autowired
    private TestRestTemplate testRestTemplate;
    private Port genericPort;
    private NetworkDevice genericNetworkDevice;
    private Credential genericCredential;
    @LocalServerPort
    private int port;
    String baseUrl;

    String accessToken;

    private final LoginUtil loginUtil = new LoginUtil();

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;

        genericCredential = new Credential();
        genericCredential.setName("admin");
        genericCredential.setPassword("admin");
        genericCredential.setUsername("admin");
        genericCredential.setId(1L);

        credentialRepository.save(genericCredential);


        genericNetworkDevice = new NetworkDevice();
        genericNetworkDevice.setId(1L);
        genericNetworkDevice.setDeviceType(Enum.NetworkDeviceType.LT);
        genericNetworkDevice.setFirmware("AMO-10000-LT_7.9.4_24860");
        genericNetworkDevice.setIpAddress("10.220.4.5");
        genericNetworkDevice.setLabel("10.220.4.5");
        genericNetworkDevice.setMacAddress("00:15:AD:50:FD:B0");
        genericNetworkDevice.setModel("AMO-10000-LT");
        genericNetworkDevice.setSerial("C410-4492");
        genericNetworkDevice.setSshPort(22);
        genericNetworkDevice.setState(Enum.NetworkDeviceState.IN_SERVICE);
        genericNetworkDevice.setResyncStatus(Enum.ResyncStatus.ONGOING);
        genericNetworkDevice.setCredential(genericCredential);
        networkDeviceRepository.save(genericNetworkDevice);


        genericPort = new Port();
        genericPort.setId(1L);
        genericPort.setConnector("SFP-1");
        genericPort.setMacAddress("00:15:AD:50:FD:B1");
        genericPort.setName("PORT-1");
        genericPort.setState(Enum.State.ENABLED);
        genericPort.setNetworkDevice(genericNetworkDevice);
        portRepository.save(genericPort);
        LoginDTO loginDTO = loginUtil.loginAsAdmin(testRestTemplate);
        accessToken = loginDTO.getAccess_token();
    }


    @Test
    void shouldReturn200WhenGetAllPort() {
        String url = baseUrl + "/api/v1/ports";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        ResponseEntity<List<PortDTO>> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<>() {
        });
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().size()).isOne();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        PortDTO portDTOResult = responseEntity.getBody().get(0);
        assertPortsIsEqual(portDTOResult, PortCreator.createDtoBy(genericPort));
    }


    @Test
    void shouldReturnPortAnd200WhenPortById() {
        String url = baseUrl + "/api/v1/ports/" + genericPort.getId();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        ResponseEntity<PortDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), PortDTO.class);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        PortDTO portDTOResult = responseEntity.getBody();
        assertPortsIsEqual(portDTOResult, PortCreator.createDtoBy(genericPort));
    }

    @Test
    void shouldReturn400WhenGetNotExistPortId() {
        String url = baseUrl + "/api/v1/ports/" + 100;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        ResponseEntity<PortDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), PortDTO.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void  shouldReturnPortAnd200WhenGetPortsByDeviceId() {
        String url = baseUrl + "/api/v1/ports/devices/" + genericPort.getNetworkDevice().getId();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        ResponseEntity<List<PortDTO>> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<>() {
        });
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().size()).isOne();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        PortDTO portDTOResult = responseEntity.getBody().get(0);

        assertPortsIsEqual(portDTOResult, PortCreator.createDtoBy(genericPort));
    }

    @Test
    void shouldReturn400WhenGetPortsByNotExistDevice() {
        String url = baseUrl + "/api/v1/ports/devices/" + 100;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        ResponseEntity responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<>() {
        });
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


    void assertPortsIsEqual(PortDTO thisPort, PortDTO thatPort) {
        assertThat(thisPort.getId()).isEqualTo(thatPort.getId());
        assertThat(thisPort.getConnector()).isEqualTo(thatPort.getConnector());
        assertThat(thisPort.getMacAddress()).isEqualTo(thatPort.getMacAddress());
        assertThat(thisPort.getName()).isEqualTo(thatPort.getName());
        assertThat(thisPort.getState()).isEqualTo(thatPort.getState());
        assertThat(thisPort.getNetworkDevice()).isEqualTo(thatPort.getNetworkDevice());
    }

}
