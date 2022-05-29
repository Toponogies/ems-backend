package vn.com.tma.emsbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.com.tma.emsbackend.common.enums.Enum;
import vn.com.tma.emsbackend.model.dto.ErrorDTO;
import vn.com.tma.emsbackend.model.dto.NetworkDeviceDTO;
import vn.com.tma.emsbackend.model.dto.SSHCommandDTO;
import vn.com.tma.emsbackend.model.dto.SSHCommandResponseDTO;
import vn.com.tma.emsbackend.service.device.NetworkDeviceService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/devices")
public class NetworkDeviceController {
    private final NetworkDeviceService networkDeviceService;

    @Operation(summary = "Get all network devices")
    @ApiResponse(responseCode = "200", description = "Get all network devices", content = {
            @Content(array = @ArraySchema(schema = @Schema(implementation = NetworkDeviceDTO.class)))})
    @GetMapping()
    public Collection<NetworkDeviceDTO> getAllNetworkDevices() {
        return networkDeviceService.getAll();
    }


    @Operation(summary = "Get a specific network device by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the network device", content = {
                    @Content(schema = @Schema(implementation = NetworkDeviceDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Network device not found", content = {
                    @Content(schema = @Schema(implementation = ErrorDTO.class))})
    })
    @GetMapping("/{id}")
    public NetworkDeviceDTO getDeviceById(@PathVariable(value = "id") Long networkDeviceId) {
        return networkDeviceService.get(networkDeviceId);
    }


    @Operation(summary = "Get a specific network device by ip address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the network device", content = {
                    @Content(schema = @Schema(implementation = NetworkDeviceDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Network device not found", content = {
                    @Content(schema = @Schema(implementation = ErrorDTO.class))})
    })
    @GetMapping("/ip/{ip_address}")
    public NetworkDeviceDTO getDeviceByIpAddress(@PathVariable(value = "ip_address") String ipAddress) {
        return networkDeviceService.getByIpAddress(ipAddress);
    }

    @Operation(summary = "Get all network device by device type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the network device", content = {
                    @Content(schema = @Schema(implementation = NetworkDeviceDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Network device not found", content = {
                    @Content(schema = @Schema(implementation = ErrorDTO.class))})
    })
    @GetMapping("/type/{device_type}")
    public List<NetworkDeviceDTO> getDevicesByDeviceType(@PathVariable(value = "device_type") Enum.NetworkDeviceType deviceType) {
        return networkDeviceService.getByDeviceType(deviceType);
    }

    @Operation(summary = "Add a new device")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Added the network device", content = {
                    @Content(schema = @Schema(implementation = NetworkDeviceDTO.class))}),
            @ApiResponse(responseCode = "400", description = "IP address is invalid", content = {
                    @Content(schema = @Schema(implementation = ErrorDTO.class))})})
    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping()
    public NetworkDeviceDTO addDevice(@Valid @RequestBody NetworkDeviceDTO networkDeviceDto) {
        return networkDeviceService.add(networkDeviceDto);
    }

    @Operation(summary = "Update a specific network device by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edited the network device", content = {
                    @Content(schema = @Schema(implementation = NetworkDeviceDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Network device not found", content = {
                    @Content(schema = @Schema(implementation = ErrorDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Data is invalid", content = {
                    @Content(schema = @Schema(implementation = ErrorDTO.class))})
    })
    @PutMapping("/{id}")
    public NetworkDeviceDTO updateDevice(@PathVariable(value = "id") Long deviceId,
                                         @Valid @RequestBody NetworkDeviceDTO networkDeviceDto) {
        return networkDeviceService.update(deviceId, networkDeviceDto);
    }

    @Operation(summary = "Delete a specific network device by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted network device", content = {
                    @Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Network device not found", content = {
                    @Content(schema = @Schema(implementation = ErrorDTO.class))})
    })
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteDevice(@PathVariable(value = "id") Long deviceId) {
        networkDeviceService.delete(deviceId);
    }
}
