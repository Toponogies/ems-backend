package vn.com.tma.emsbackend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
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
import vn.com.tma.emsbackend.dto.ErrorDto;
import vn.com.tma.emsbackend.dto.ManagedDeviceDto;
import vn.com.tma.emsbackend.dto.ManagedDeviceRequestDto;
import vn.com.tma.emsbackend.service.manageddevice.ManagedDeviceService;

import javax.validation.Valid;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/devices")
public class ManagedDeviceController {
    private final ManagedDeviceService managedDeviceService;

    //GET /
    @Operation(summary = "Get all managed devices")
    @ApiResponse(responseCode = "200", description = "Get all managed devices", content = {
            @Content(array = @ArraySchema(schema = @Schema(implementation = ManagedDeviceDto.class)))})
    @GetMapping()
    public List<ManagedDeviceDto> getAllManagedDevices() {
        return managedDeviceService.getAll();
    }



     // GET /{id}
    @Operation(summary = "Get a specific managed device by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the managed device", content = {
                    @Content(schema = @Schema(implementation = ManagedDeviceDto.class))}),
            @ApiResponse(responseCode = "404", description = "Managed device not found", content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class))})
    })
    @GetMapping("/{id}")
    public ManagedDeviceDto getDeviceById(@PathVariable(value = "id") Long manageDeviceId) {
        return managedDeviceService.getById(manageDeviceId);
    }


    // GET /ip/{ip_address}
    @Operation(summary = "Get a specific managed device by ip address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the managed device", content = {
                    @Content(schema = @Schema(implementation = ManagedDeviceDto.class))}),
            @ApiResponse(responseCode = "404", description = "Managed device not found", content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class))})
    })
    @GetMapping("/ip/{ip_address}")
    public ManagedDeviceDto getDeviceByIpAddress(@PathVariable(value = "ip_address") String ipAddress) {
        return managedDeviceService.getByIpAddress(ipAddress);
    }



     //POST /
    @Operation(summary = "Add a new credential")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Added the managed device", content = {
                    @Content(schema = @Schema(implementation = ManagedDeviceDto.class))}),
            @ApiResponse(responseCode = "409", description = "Constraint violated", content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class))}),
            @ApiResponse(responseCode = "400", description = "IP address is invalid", content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class))})})

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping()
    public ManagedDeviceDto addDevice(@Valid @RequestBody ManagedDeviceRequestDto deviceRequestDto) {
        return managedDeviceService.add(deviceRequestDto);
    }


    // DELETE /{id}
    @Operation(summary = "Delete a specific managed device by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted managed device", content = {
                    @Content(schema = @Schema(implementation = Void.class))}),
            @ApiResponse(responseCode = "404", description = "Managed device not found", content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class))}),
            @ApiResponse(responseCode = "409", description = "Constraint violated", content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class))})
    })
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteDevice(@PathVariable(value = "id") Long deviceId) {
        managedDeviceService.delete(deviceId);
    }


    //PUT /{id}
    @Operation(summary = "Update a specific managed device by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edited the managed device", content = {
                    @Content(schema = @Schema(implementation = ManagedDeviceDto.class))}),
            @ApiResponse(responseCode = "404", description = "Managed device not found", content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class))}),
            @ApiResponse(responseCode = "409", description = "Constraint violated", content = {
                    @Content(schema = @Schema(implementation = ErrorDto.class))})
    })

    @PutMapping("/{id}")
    public ManagedDeviceDto updateDevice(@PathVariable(value = "id") Long deviceId,
                                         @RequestBody ManagedDeviceRequestDto deviceRequestDto) {
        return managedDeviceService.update(deviceId, deviceRequestDto);
    }
}
