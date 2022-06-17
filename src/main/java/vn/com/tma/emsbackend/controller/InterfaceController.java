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
import vn.com.tma.emsbackend.model.dto.ErrorDTO;
import vn.com.tma.emsbackend.model.dto.InterfaceDTO;
import vn.com.tma.emsbackend.model.dto.NetworkDeviceDTO;
import vn.com.tma.emsbackend.model.dto.PortDTO;
import vn.com.tma.emsbackend.service.deviceinterface.InterfaceService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/interfaces")
public class InterfaceController {
    private final InterfaceService interfaceService;

    @Operation(summary = "Get all interfaces")
    @ApiResponse(responseCode = "200",
            description = "Get all interfaces",
            content = {@Content(array = @ArraySchema(schema = @Schema(implementation = InterfaceDTO.class)))})
    @GetMapping()
    public Collection<InterfaceDTO> getAllInterfaces() {
        return interfaceService.getAll();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all interfaces", content = {
                    @Content(array = @ArraySchema(schema = @Schema(implementation = InterfaceDTO.class)))}),
            @ApiResponse(responseCode = "404", description = "Device not found", content = {
                    @Content(schema = @Schema(implementation = ErrorDTO.class))})
    })
    @GetMapping("/devices/{deviceId}")
    public Collection<InterfaceDTO> getInterfacesByDevice(@PathVariable(value = "deviceId") Long deviceId) {
        return interfaceService.getByNetworkDevice(deviceId);
    }

    @Operation(summary = "Get a specific interface by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the interface", content = {
                    @Content(schema = @Schema(implementation = InterfaceDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Interface not found", content = {
                    @Content(schema = @Schema(implementation = ErrorDTO.class))})
    })
    @GetMapping("/{id}")
    public InterfaceDTO getDeviceById(@PathVariable(value = "id") Long interfaceId) {
        return interfaceService.get(interfaceId);
    }

    @Operation(summary = "Get a specific interface by port")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the interface", content = {
                    @Content(schema = @Schema(implementation = InterfaceDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Interface not found", content = {
                    @Content(schema = @Schema(implementation = ErrorDTO.class))})
    })
    @GetMapping("/ports/{portId}")
    public InterfaceDTO getDeviceByPort(@PathVariable(value = "portId") Long portId) {
        return interfaceService.getByPort(portId);
    }

    @Operation(summary = "Add a new interface")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Added the interface", content = {
                    @Content(schema = @Schema(implementation = InterfaceDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Data is invalid", content = {
                    @Content(schema = @Schema(implementation = ErrorDTO.class))})})
    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping()
    public InterfaceDTO addInterface(@Valid @RequestBody InterfaceDTO interfaceDTO) {
        return interfaceService.add(interfaceDTO);
    }


    @Operation(summary = "Delete a specific interface by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted interface"),
            @ApiResponse(responseCode = "404", description = "Interface not found", content = {
                    @Content(schema = @Schema(implementation = ErrorDTO.class))})
    })
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteInterface(@PathVariable(value = "id") Long interfaceId) {
        interfaceService.delete(interfaceId);
    }


    @Operation(summary = "Update a specific interface by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edited the interface", content = {
                    @Content(schema = @Schema(implementation = NetworkDeviceDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Interface not found", content = {
                    @Content(schema = @Schema(implementation = ErrorDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid data", content = {
                    @Content(schema = @Schema(implementation = ErrorDTO.class))})
    })

    @PutMapping("/{id}")
    public InterfaceDTO updateDevice(@PathVariable(value = "id") Long interfaceId,
                                     @Valid @RequestBody InterfaceDTO interfaceDTO) {
        return interfaceService.update(interfaceId, interfaceDTO);
    }

    @Operation(summary = "Get interfaces by device label")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all interface", content = {
                    @Content(schema = @Schema(implementation = PortDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Device not found", content = {
                    @Content(schema = @Schema(implementation = ErrorDTO.class))})
    })
    @GetMapping("/devices/label/{label}")
    public List<InterfaceDTO> getByDeviceLabel(@PathVariable(value = "label") String deviceLabel) {
        return interfaceService.getByNetworkDeviceLabel(deviceLabel);
    }
}