package vn.com.tma.emsbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import vn.com.tma.emsbackend.model.dto.ErrorDTO;
import vn.com.tma.emsbackend.model.dto.SSHCommandDTO;
import vn.com.tma.emsbackend.model.dto.SSHCommandResponseDTO;
import vn.com.tma.emsbackend.service.device.NetworkDeviceService;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/devices/{id}/generic-command")
public class SSHCommandController {
    private final NetworkDeviceService networkDeviceService;

    @Operation(summary = "Send a command directly to device by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully execute command on the device", content = {
                    @Content(schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "502", description = "Have a error while execute command on device", content = {
                    @Content(schema = @Schema(implementation = ErrorDTO.class))}),
    })
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    @PostMapping()
    public SSHCommandResponseDTO sendCommandToDeviceById(@PathVariable(value = "id") Long deviceId, @RequestBody SSHCommandDTO sshCommandDTO) {
        return networkDeviceService.sendCommandToDeviceById(deviceId, sshCommandDTO);
    }


    @Operation(summary = "Download configuration file from network device")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully get configuration file from device", content = {
                    @Content(schema = @Schema(implementation = byte[].class))}),
            @ApiResponse(responseCode = "502", description = "Have a error while get configuration from device", content = {
                    @Content(schema = @Schema(implementation = ErrorDTO.class))}),
    })
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    @GetMapping(path = "/configuration", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public byte[] downloadDeviceConfigFileById(@PathVariable(value = "id") Long deviceId, HttpServletResponse response) {
        byte[] bytes = networkDeviceService.downloadDeviceConfigFileById(deviceId);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + "configuration_" + deviceId + "\"");
        return bytes;
    }

}
