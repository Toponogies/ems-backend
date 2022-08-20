package vn.com.tma.emsbackend.unit.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import vn.com.tma.emsbackend.common.enums.Enum;
import vn.com.tma.emsbackend.controller.PortController;
import vn.com.tma.emsbackend.model.entity.Credential;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.model.entity.Port;
import vn.com.tma.emsbackend.service.port.PortService;
import vn.com.tma.emsbackend.util.entity.creator.PortCreator;

import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;


@WebMvcTest(PortController.class)
public class PortControllerTests {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private PortService portService;

    private Credential genericCredential;

    private NetworkDevice genericNetworkDevice;

    private Port genericPort;

    private final JsonMapper jsonMapper = new JsonMapper();


    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mvc);
        genericCredential = new Credential();
        genericCredential.setName("admin");
        genericCredential.setPassword("admin");
        genericCredential.setUsername("admin");
        genericCredential.setId(1L);


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

        genericPort = new Port();
        genericPort.setId(1L);
        genericPort.setName("PORT-1");
        genericPort.setConnector("PORT-1");
        genericPort.setMacAddress("fe80::a08f:a8ff:fed5:aa1");
        genericPort.setNetworkDevice(genericNetworkDevice);
        genericPort.setState(Enum.State.ENABLED);
    }

    @Test
    @WithMockUser(roles = "admin")
    void shouldReturn200AndAllPortWhenGetAllPort() throws JsonProcessingException {
        //give
        when(portService.getAll()).thenReturn(List.of(PortCreator.createDtoBy(genericPort)));

        //when
        given().get("/ports")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(is(jsonMapper.writeValueAsString(List.of(PortCreator.createDtoBy(genericPort)))));
    }
    @Test
    @WithMockUser(roles = "admin")
    void shouldReturn200AndSpecificPortWhenGetPortById() throws JsonProcessingException {
        //given
        when(portService.get(genericPort.getId())).thenReturn(PortCreator.createDtoBy(genericPort));

        //when
        given().get("/ports/" + genericPort.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(is(jsonMapper.writeValueAsString(PortCreator.createDtoBy(genericPort))));
    }

    @Test
    @WithMockUser(roles = "admin")
    void shouldReturn200AndPortBelongToDeviceWhenGetPortByIdDevice() throws JsonProcessingException {
        //given
        when(portService.getByNetworkDevice(genericPort.getNetworkDevice().getId())).thenReturn(List.of(PortCreator.createDtoBy(genericPort)));

        //when
        given().get("/ports/devices/" + genericPort.getNetworkDevice().getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(is(jsonMapper.writeValueAsString(List.of(PortCreator.createDtoBy(genericPort)))));
    }

    @Test
    @WithMockUser(roles = "admin")
    void shouldReturn200AndPortBelongToDeviceWhenGetPortByDeviceLabel() throws JsonProcessingException {
        //given
        when(portService.getByNetworkDeviceLabel(genericPort.getNetworkDevice().getLabel())).thenReturn(List.of(PortCreator.createDtoBy(genericPort)));

        //when
        given().get("/ports/devices/label/" + genericPort.getNetworkDevice().getLabel())
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(is(jsonMapper.writeValueAsString(List.of(PortCreator.createDtoBy(genericPort)))));
    }

    @Test
    void shouldReturn400WhenGetAllPortWithoutLogin() {
        //when
        given().get("/ports")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void shouldReturn400WhenGetPortByIdWithoutLogin() {
        //when
        given().get("/ports/" + genericPort.getId())
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void shouldReturn400WhenGetPortsByDeviceIdWithoutLogin() {
        //when
        given().get("/ports/devices/" + genericPort.getNetworkDevice().getId())
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void shouldReturn400WhenGetPortsByDeviceLabelWithoutLogin() {
        //when
        given().get("/ports/devices/label/" + genericPort.getNetworkDevice().getLabel())
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }
}
