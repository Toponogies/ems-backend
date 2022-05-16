package vn.com.tma.emsbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.tma.emsbackend.model.dto.ErrorDTO;
import vn.com.tma.emsbackend.model.dto.PortDTO;
import vn.com.tma.emsbackend.service.port.PortService;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/ports")
public class PortController {
    private final PortService portService;

    @Operation(summary = "Get all ports")
    @ApiResponse(responseCode = "200", description = "Get all ports", content = {
            @Content(array = @ArraySchema(schema = @Schema(implementation = PortDTO.class)))})
    @GetMapping()
    public Collection<PortDTO> getAllPorts() {
        return portService.getAll();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all ports", content = {
                    @Content(array = @ArraySchema(schema = @Schema(implementation = PortDTO.class)))}),
            @ApiResponse(responseCode = "404", description = "Device not found", content = {
                    @Content(schema = @Schema(implementation = ErrorDTO.class))})
    })
    @GetMapping("/devices/{deviceId}")
    public Collection<PortDTO> getPortsByDevice(@PathVariable(value = "deviceId") Long deviceId) {
        return portService.getByNetworkDevice(deviceId);
    }

    @Operation(summary = "Get a specific port by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the port", content = {
                    @Content(schema = @Schema(implementation = PortDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Port not found", content = {
                    @Content(schema = @Schema(implementation = ErrorDTO.class))})
    })
    @GetMapping("/{id}")
    public PortDTO getDeviceById(@PathVariable(value = "id") Long portId) {
        return portService.get(portId);
    }
}
