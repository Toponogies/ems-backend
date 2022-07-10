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
import org.springframework.test.web.servlet.MockMvc;
import vn.com.tma.emsbackend.common.enums.Enum;
import vn.com.tma.emsbackend.controller.NetworkDeviceController;
import vn.com.tma.emsbackend.model.dto.NetworkDeviceDTO;
import vn.com.tma.emsbackend.model.entity.Credential;
import vn.com.tma.emsbackend.model.exception.DeviceNotFoundException;
import vn.com.tma.emsbackend.service.device.NetworkDeviceService;

import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@WebMvcTest(NetworkDeviceController.class)
@AutoConfigureMockMvc(addFilters = false)
class NetworkDeviceControllerTests {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private NetworkDeviceService networkDeviceService;

    private Credential genericCredential;

    private NetworkDeviceDTO testNetworkDevice;

    private final Long TEST_NETWORK_DEVICE_ID = 1L;

    private final JsonMapper jsonMapper = new JsonMapper();


    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mvc);
        genericCredential = new Credential();
        genericCredential.setName("admin");
        genericCredential.setPassword("admin");
        genericCredential.setUsername("admin");
        genericCredential.setId(1L);

        testNetworkDevice = new NetworkDeviceDTO();
        testNetworkDevice.setIpAddress("10.220.4.10");
        testNetworkDevice.setLabel("10.220.4.10");
        testNetworkDevice.setCredential(genericCredential.getName());
        testNetworkDevice.setSshPort(22);

    }

    @Test
    void shouldReturn200AndValidNetworkDeviceWhenGetAllDevices() throws JsonProcessingException {
        //Given
        when(networkDeviceService.getAll()).thenReturn(List.of(testNetworkDevice));

        given().get("/devices")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(is(jsonMapper.writeValueAsString(List.of(testNetworkDevice))));
    }

    @Test
    void shouldReturn200AndValidNetworkDeviceWhenGetDeviceWithId() throws JsonProcessingException {
        //Given
        when(networkDeviceService.get(TEST_NETWORK_DEVICE_ID)).thenReturn(testNetworkDevice);

        given().get("/devices/1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(is(jsonMapper.writeValueAsString((testNetworkDevice))));
    }

    @Test
    void shouldReturn201WhenAddNewValidNetworkDevice() throws JsonProcessingException {
        //Given
        when(networkDeviceService.add(any(NetworkDeviceDTO.class))).thenReturn(testNetworkDevice);

        //Given
        given().contentType(ContentType.JSON)
                .body(testNetworkDevice)
                .post("/devices")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON)
                .body(is(jsonMapper.writeValueAsString(testNetworkDevice)));
    }

    @Test
    void shouldReturn200WhenUpdateNewValidNetworkDevice() throws JsonProcessingException {
        //Given
        when(networkDeviceService.update(any(Long.class), any(NetworkDeviceDTO.class))).thenReturn(testNetworkDevice);

        //Given
        given().contentType(ContentType.JSON)
                .body(testNetworkDevice)
                .put("/devices/1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(is(jsonMapper.writeValueAsString(testNetworkDevice)));
    }

    @Test
    void shouldReturn200WhenDeleteNetworkDevice() {
        //Given
        doNothing().when(networkDeviceService).delete(anyLong());

        //Given
        given().contentType(ContentType.JSON)
                .body(testNetworkDevice)
                .delete("/devices/1")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void shouldReturn404WhenGetNotExistDevice() {
        //Given
        when(networkDeviceService.get(anyLong())).thenThrow(DeviceNotFoundException.class);

        given().get("/devices/1")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldReturn404WhenDeleteNotExistDevice() {
        //Given
        doThrow(new DeviceNotFoundException(TEST_NETWORK_DEVICE_ID.toString())).when(networkDeviceService).delete(TEST_NETWORK_DEVICE_ID);

        //Then
        given().delete("/devices/1")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldReturn200AndDevicesWithTypeWhenGetDeviceWithType() throws JsonProcessingException {
        when(networkDeviceService.getByDeviceType(any(Enum.NetworkDeviceType.class))).thenReturn(List.of(testNetworkDevice));

        given().get("/devices/type/GT")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(is(jsonMapper.writeValueAsString(List.of(testNetworkDevice))));
    }

    @Test
    void shouldReturn400WhenGetDeviceWithTypeThatInvalid() throws JsonProcessingException {
        when(networkDeviceService.getByDeviceType(any(Enum.NetworkDeviceType.class))).thenReturn(List.of(testNetworkDevice));

        given().get("/devices/type/some-invalid-type")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

}