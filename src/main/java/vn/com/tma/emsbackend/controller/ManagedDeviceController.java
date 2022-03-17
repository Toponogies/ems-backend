package vn.com.tma.emsbackend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import vn.com.tma.emsbackend.dto.ErrorDto;
import vn.com.tma.emsbackend.dto.ManagedDeviceDto;
import vn.com.tma.emsbackend.dto.ManagedDeviceRequestDto;
import vn.com.tma.emsbackend.service.manageddevice.ManagedDeviceService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/devices")
public class ManagedDeviceController {
    private final ManagedDeviceService managedDeviceService;

    @Operation(summary = "Get all managed devices")
    @ApiResponse(responseCode = "200", description = "Get all managed devices", content = {
            @Content(array = @ArraySchema(schema = @Schema(implementation = ManagedDeviceDto.class))) })
    @GetMapping()
    public List<ManagedDeviceDto> getAllManagedDevices() {
        return managedDeviceService.getAll();
    }

    @Operation(summary = "Get a specific managed device by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the managed device", content = {
                    @Content(schema = @Schema(implementation = ManagedDeviceDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Managed device not found", content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class)) })
    })

    @GetMapping("/{id}")
    public ManagedDeviceDto getDeviceById(@PathVariable(value = "id") Long manageDeviceId) {
        return managedDeviceService.get(manageDeviceId);
    }

    @Operation(summary = "Add a new credential")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Added the managed device", content = {
                    @Content(schema = @Schema(implementation = ManagedDeviceDto.class)) }),
            @ApiResponse(responseCode = "409", description = "Constraint violated", content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class)) }),
            @ApiResponse(responseCode = "400", description = "IP address is invalid", content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class)) }) })
    @PostMapping()
    public ManagedDeviceDto addDevice(@RequestBody ManagedDeviceRequestDto deviceRequestDto) {
        return managedDeviceService.add(deviceRequestDto);
    }

    @Operation(summary = "Delete a specific managed device by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted managed device", content = {
                    @Content(schema = @Schema(implementation = Void.class)) }),
            @ApiResponse(responseCode = "404", description = "Managed device not found", content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class)) }),
            @ApiResponse(responseCode = "409", description = "Constraint violated", content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class)) })
    })

    @DeleteMapping("/{id}")
    public void deleteDevice(@PathVariable(value = "id") Long deviceId) {
        managedDeviceService.delete(deviceId);
    }

    @Operation(summary = "Update a specific managed device by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edited the managed device", content = {
                    @Content(schema = @Schema(implementation = ManagedDeviceDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Managed device not found", content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class)) }),
            @ApiResponse(responseCode = "409", description = "Constraint violated", content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class)) })
    })

    @PutMapping("/{id}")
    public ManagedDeviceDto updateDevice(@PathVariable(value = "id") Long deviceId,
            @RequestBody ManagedDeviceRequestDto deviceRequestDto) {
        return managedDeviceService.update(deviceId, deviceRequestDto);
    }
}
