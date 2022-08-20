package vn.com.tma.emsbackend.unit.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import vn.com.tma.emsbackend.common.enums.Enum;
import vn.com.tma.emsbackend.controller.InterfaceController;
import vn.com.tma.emsbackend.model.dto.InterfaceDTO;
import vn.com.tma.emsbackend.model.entity.Credential;
import vn.com.tma.emsbackend.model.entity.Interface;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.model.entity.Port;
import vn.com.tma.emsbackend.model.exception.DeviceNotFoundException;
import vn.com.tma.emsbackend.model.exception.PortNotFoundException;
import vn.com.tma.emsbackend.service.deviceinterface.InterfaceService;
import vn.com.tma.emsbackend.util.entity.creator.InterfaceCreator;

import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@WebMvcTest(InterfaceController.class)
@AutoConfigureMockMvc(addFilters = false)
public class InterfaceControllerTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private InterfaceService interfaceService;

    private Credential genericCredential;

    private NetworkDevice genericNetworkDevice;

    private Port genericPort;

    private Interface genericInterface;

    private final JsonMapper jsonMapper = new JsonMapper();


    @BeforeEach
    void setup(){
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

        genericInterface = new Interface();
        genericInterface.setId(1L);
        genericInterface.setName("Management");
        genericInterface.setState(Enum.State.ENABLED);
        genericInterface.setDhcp(Enum.State.ENABLED);
        genericInterface.setIpAddress("10.0.1.1");
        genericInterface.setNetmask("255.255.0.0");
        genericInterface.setGateway("10.0.0.1");
        genericInterface.setPort(genericPort);
        genericInterface.setNetworkDevice(genericNetworkDevice);
    }
    @Test
    @WithMockUser(roles = "admin")
    void shouldReturn200AndAllInterfaceWhenGetAllInterface() throws JsonProcessingException {
        //give
        when(interfaceService.getAll()).thenReturn(List.of(InterfaceCreator.createDtoBy(genericInterface)));

        //when
        given().get("/interfaces")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(is(jsonMapper.writeValueAsString(List.of(InterfaceCreator.createDtoBy(genericInterface)))));
    }

    @Test
    @WithMockUser(roles = "admin")
    void shouldReturn200AndInterfacesWhenGetInterfaceByDeviceId() throws JsonProcessingException {
        //give
        when(interfaceService.getByNetworkDevice(genericInterface.getId())).thenReturn(List.of(InterfaceCreator.createDtoBy(genericInterface)));

        //when
        given().get("/interfaces/devices/" + genericInterface.getNetworkDevice().getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(is(jsonMapper.writeValueAsString(List.of(InterfaceCreator.createDtoBy(genericInterface)))));
    }

    @Test
    @WithMockUser(roles = "admin")
    void shouldReturn404WhenGetInterfaceByNotExistDeviceId() throws JsonProcessingException {
        //give
        when(interfaceService.getByNetworkDevice(genericInterface.getId())).thenThrow(new DeviceNotFoundException(genericInterface.getNetworkDevice().getId().toString()));

        //when
        given().get("/interfaces/devices/" + genericInterface.getNetworkDevice().getId())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(ContentType.JSON);
    }

    @Test
    @WithMockUser(roles = "viewer")
    void shouldReturn200AndSpecificInterfaceWhenGetInterfaceByPort() throws JsonProcessingException {
        //give
        when(interfaceService.getByPort(genericInterface.getPort().getId())).thenReturn(InterfaceCreator.createDtoBy(genericInterface));

        //when
        given().get("/interfaces/ports/" + genericInterface.getPort().getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(is(jsonMapper.writeValueAsString(InterfaceCreator.createDtoBy(genericInterface))));
    }

    @Test
    @WithMockUser(roles = "viewer")
    void shouldReturn404WhenGetInterfaceByNotExistPort() throws JsonProcessingException {
        //give
        when(interfaceService.getByPort(genericInterface.getPort().getId())).thenThrow(new PortNotFoundException(genericPort.getId()));

        //when
        given().get("/interfaces/ports/" + genericInterface.getPort().getId())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(ContentType.JSON);
    }

    @Test
    @WithMockUser(roles = "admin")
    void shouldReturn201AndNewInterfaceWhenGetInterfaceById() throws JsonProcessingException {
        //give
        InterfaceDTO interfaceDTO = InterfaceCreator.createDtoBy(genericInterface);
        when(interfaceService.add(any(InterfaceDTO.class))).thenReturn(interfaceDTO);

        //when
        given().contentType(ContentType.JSON)
                .body(interfaceDTO)
                .post("/interfaces")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON)
                .body(is(jsonMapper.writeValueAsString(interfaceDTO)));
    }

    @Test
    @WithMockUser(roles = "viewer")
    void shouldReturn200AndInterfaceWhenUpdateInterface() throws JsonProcessingException {
        //give
        InterfaceDTO interfaceDTO = InterfaceCreator.createDtoBy(genericInterface);
        interfaceDTO.setName("new name");
        when(interfaceService.update(anyLong(), any(InterfaceDTO.class))).thenReturn(interfaceDTO);

        //when
        given().contentType(ContentType.JSON)
                .body(InterfaceCreator.createDtoBy(genericInterface))
                .put("/interfaces/" + genericInterface.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(is(jsonMapper.writeValueAsString(interfaceDTO)));
    }

    @Test
    @WithMockUser(roles = "admin")
    void shouldReturn204WhenDeleteInterface() throws JsonProcessingException {
        //give
        doNothing().when(interfaceService).delete(anyLong());

        //when
        given().contentType(ContentType.JSON)
                .body(InterfaceCreator.createDtoBy(genericInterface))
                .put("/interfaces/" + genericInterface.getId())
                .then()
                .statusCode(HttpStatus.OK.value());
    }
}
