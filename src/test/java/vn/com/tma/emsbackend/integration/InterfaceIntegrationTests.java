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
import vn.com.tma.emsbackend.model.dto.InterfaceDTO;
import vn.com.tma.emsbackend.model.entity.Credential;
import vn.com.tma.emsbackend.model.entity.Interface;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.model.entity.Port;
import vn.com.tma.emsbackend.repository.CredentialRepository;
import vn.com.tma.emsbackend.repository.InterfaceRepository;
import vn.com.tma.emsbackend.repository.NetworkDeviceRepository;
import vn.com.tma.emsbackend.repository.PortRepository;
import vn.com.tma.emsbackend.util.auth.LoginUtil;
import vn.com.tma.emsbackend.util.entity.creator.InterfaceCreator;
import vn.com.tma.emsbackend.util.entity.DTO.LoginDTO;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class InterfaceIntegrationTests {

    @Autowired
    private CredentialRepository credentialRepository;
    @Autowired
    private NetworkDeviceRepository networkDeviceRepository;
    @Autowired
    private PortRepository portRepository;
    @Autowired
    private InterfaceRepository interfaceRepository;
    @Autowired
    private TestRestTemplate testRestTemplate;
    private Port genericPort;
    private NetworkDevice genericNetworkDevice;
    private Credential genericCredential;
    private Interface genericInterface;

    @LocalServerPort
    private int port;
    String baseUrl;
    String accessToken;
    private final LoginUtil loginUtil = new LoginUtil();

    @BeforeEach
    void setUp(){
        baseUrl =  "http://localhost:" + port;
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
        genericNetworkDevice.setIpAddress("10.0.1.1");
        genericNetworkDevice.setLabel("10.0.1.1");
        genericNetworkDevice.setMacAddress("00:15:AD:50:FD:B0");
        genericNetworkDevice.setModel("AMO-10000-LT");
        genericNetworkDevice.setSerial("C410-4492");
        genericNetworkDevice.setSshPort(12022);
        genericNetworkDevice.setState(Enum.NetworkDeviceState.IN_SERVICE);
        genericNetworkDevice.setCredential(genericCredential);
        networkDeviceRepository.save(genericNetworkDevice);


        genericPort = new Port();
        genericPort.setId(1L);
        genericPort.setConnector("SFP-1");
        genericPort.setMacAddress("00:15:AD:50:FD:B1");
        genericPort.setName("LOCAL-1");
        genericPort.setState(Enum.State.ENABLED);
        genericPort.setNetworkDevice(genericNetworkDevice);
        portRepository.save(genericPort);

        genericInterface = new Interface();
        genericInterface.setId(1L);
        genericInterface.setDhcp(Enum.State.ENABLED);
        genericInterface.setGateway("10.220.0.1");
        genericInterface.setIpAddress("10.220.4.5");
        genericInterface.setName("NewInterface");
        genericInterface.setNetmask("255.255.0.0");
        genericInterface.setPort(genericPort);
        genericInterface.setState(Enum.State.ENABLED);
        genericInterface.setNetworkDevice(genericNetworkDevice);


        LoginDTO loginDTO = loginUtil.loginAsAdmin(testRestTemplate);
        accessToken = loginDTO.getAccess_token();
    }

    @Test
    void shouldReturn200AndAllInterfaceWhenGetAllInterface() {
        interfaceRepository.save(genericInterface);

        String url = baseUrl + "/api/v1/interfaces";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        ResponseEntity<List<InterfaceDTO>> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<>() {
        });
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().size()).isOne();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        InterfaceDTO interfaceDTOsResult = responseEntity.getBody().get(0);
        assertInterfacesIsEqual(interfaceDTOsResult, InterfaceCreator.createDtoBy(genericInterface));
    }

    @Test
    void shouldReturn200AndSpecificInterfaceWhenGetInterfaceById() {
        interfaceRepository.save(genericInterface);

        String url = baseUrl + "/api/v1/interfaces/" +  genericInterface.getId();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        ResponseEntity<InterfaceDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), InterfaceDTO.class);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        InterfaceDTO interfaceDTOsResult = responseEntity.getBody();
        assertInterfacesIsEqual(interfaceDTOsResult, InterfaceCreator.createDtoBy(genericInterface));
    }

    @Test
    void shouldReturn200AndSpecificInterfaceWhenGetInterfaceByPortId() {
        interfaceRepository.save(genericInterface);

        String url = baseUrl + "/api/v1/interfaces/ports/" +  genericInterface.getPort().getId();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        ResponseEntity<InterfaceDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), InterfaceDTO.class);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        InterfaceDTO interfaceDTOsResult = responseEntity.getBody();
        assertInterfacesIsEqual(interfaceDTOsResult, InterfaceCreator.createDtoBy(genericInterface));
    }

    @Test
    void shouldReturn201AndNewInterfaceWhenAddNewInterfaceAndDeleteThatInterface(){
        //add
        String url = baseUrl + "/api/v1/interfaces";

        InterfaceDTO interfaceDTO = InterfaceCreator.createDtoBy(genericInterface);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        ResponseEntity<InterfaceDTO> responseEntity = testRestTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(interfaceDTO,headers), InterfaceDTO.class);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        //delete
        url = baseUrl + "/api/v1/interfaces/" + genericInterface.getId();
        ResponseEntity<Void> deleteResponseEntity = testRestTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(interfaceDTO,headers), Void.class);

        InterfaceDTO interfaceDTOsResult = responseEntity.getBody();

        assertThat(deleteResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertInterfacesIsEqual(interfaceDTOsResult, InterfaceCreator.createDtoBy(genericInterface));
    }

    @Test
    void shouldReturn209AndNewInterfaceWhenUpdateNewInterfaceAndDeleteThatInterface(){
        //add
        String url = baseUrl + "/api/v1/interfaces";

        InterfaceDTO interfaceDTO = InterfaceCreator.createDtoBy(genericInterface);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        ResponseEntity<InterfaceDTO> addResponseEntity = testRestTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(interfaceDTO,headers), InterfaceDTO.class);
        assertThat(addResponseEntity.getBody()).isNotNull();
        assertThat(addResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        InterfaceDTO interfaceDTOsResult = addResponseEntity.getBody();
        assertInterfacesIsEqual(interfaceDTOsResult, InterfaceCreator.createDtoBy(genericInterface));

        //update
        url = baseUrl + "/api/v1/interfaces/" + interfaceDTOsResult.getId();
        genericInterface.setName("UpdatedName");
        interfaceDTO = InterfaceCreator.createDtoBy(genericInterface);
        ResponseEntity<InterfaceDTO> updateResponseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(interfaceDTO,headers), InterfaceDTO.class);
        assertThat(updateResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);


        //delete
        url = baseUrl + "/api/v1/interfaces/" + genericInterface.getId();
        ResponseEntity<Void> deleteResponseEntity = testRestTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(interfaceDTO,headers), Void.class);
        assertThat(deleteResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    void assertInterfacesIsEqual(InterfaceDTO thisInterface, InterfaceDTO thatInterface) {
        assertThat(thisInterface.getId()).isEqualTo(thatInterface.getId());
        assertThat(thisInterface.getDhcp()).isEqualTo(thatInterface.getDhcp());
        assertThat(thisInterface.getGateway()).isEqualTo(thatInterface.getGateway());
        assertThat(thisInterface.getIpAddress()).isEqualTo(thatInterface.getIpAddress());
        assertThat(thisInterface.getName()).isEqualTo(thatInterface.getName());
        assertThat(thisInterface.getId()).isEqualTo(thatInterface.getId());
        assertThat(thisInterface.getId()).isEqualTo(thatInterface.getId());
    }
}
