package vn.com.tma.emsbackend.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import vn.com.tma.emsbackend.common.Enum;
import vn.com.tma.emsbackend.dto.ErrorDto;
import vn.com.tma.emsbackend.dto.NetworkDeviceDto;
import vn.com.tma.emsbackend.service.networkdevice.NetworkDeviceService;

import javax.validation.Valid;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/devices")
public class NetworkDeviceController {
    private final NetworkDeviceService networkDeviceService;

    @Operation(summary = "Get all network devices")
    @ApiResponse(responseCode = "200", description = "Get all network devices", content = {
            @Content(array = @ArraySchema(schema = @Schema(implementation = NetworkDeviceDto.class)))})
    @GetMapping()
    public Collection<NetworkDeviceDto> getAllNetworkDevices() {
        return networkDeviceService.getAll();
    }


    @Operation(summary = "Get a specific network device by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the network device", content = {
                    @Content(schema = @Schema(implementation = NetworkDeviceDto.class))}),
            @ApiResponse(responseCode = "404", description = "Network device not found", content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class))})
    })
    @GetMapping("/{id}")
    public NetworkDeviceDto getDeviceById(@PathVariable(value = "id") Long networkDeviceId) {
        return networkDeviceService.get(networkDeviceId);
    }


    @Operation(summary = "Get a specific network device by ip address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the network device", content = {
                    @Content(schema = @Schema(implementation = NetworkDeviceDto.class))}),
            @ApiResponse(responseCode = "404", description = "Network device not found", content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class))})
    })
    @GetMapping("/ip/{ip_address}")
    public NetworkDeviceDto getDeviceByIpAddress(@PathVariable(value = "ip_address") String ipAddress) {
        return networkDeviceService.getByIpAddress(ipAddress);
    }

    @Operation(summary = "Get all network device by device type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the network device", content = {
                    @Content(schema = @Schema(implementation = NetworkDeviceDto.class))}),
            @ApiResponse(responseCode = "404", description = "Network device not found", content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class))})
    })
    @GetMapping("/type/{device_type}")
    public List<NetworkDeviceDto> getDevicesByDeviceType(@PathVariable(value = "device_type") Enum.NetworkDeviceType deviceType) {
        return networkDeviceService.getByDeviceType(deviceType);
    }

    @Operation(summary = "Add a new device")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Added the network device", content = {
                    @Content(schema = @Schema(implementation = NetworkDeviceDto.class))}),
            @ApiResponse(responseCode = "409", description = "Constraint violated", content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class))}),
            @ApiResponse(responseCode = "400", description = "IP address is invalid", content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class))})})

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping()
    public NetworkDeviceDto addDevice(@Valid @RequestBody NetworkDeviceDto networkDeviceDto) {
        return networkDeviceService.add(networkDeviceDto);
    }


    @Operation(summary = "Delete a specific network device by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted network device", content = {
                    @Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "network device not found", content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class))}),
            @ApiResponse(responseCode = "409", description = "Constraint violated", content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class))})
    })
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteDevice(@PathVariable(value = "id") Long deviceId) {
        networkDeviceService.delete(deviceId);
    }


    @Operation(summary = "Update a specific network device by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edited the network device", content = {
                    @Content(schema = @Schema(implementation = NetworkDeviceDto.class))}),
            @ApiResponse(responseCode = "404", description = "network device not found", content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class))}),
            @ApiResponse(responseCode = "409", description = "Constraint violated", content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class))})
    })

    @PutMapping("/{id}")
    public NetworkDeviceDto updateDevice(@PathVariable(value = "id") Long deviceId,
                                         @RequestBody NetworkDeviceDto networkDeviceDto) {
        return networkDeviceService.update(deviceId, networkDeviceDto);
    }
}
