package vn.com.tma.emsbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.com.tma.emsbackend.model.dto.SSHCommandDTO;
import vn.com.tma.emsbackend.model.dto.SSHCommandResponseDTO;
import vn.com.tma.emsbackend.service.device.NetworkDeviceService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/ssh-command")
public class SSHCommandController {
    private final NetworkDeviceService networkDeviceService;

    @Operation(summary = "Send a command directly to device by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully execute command on the device", content = {
                    @Content(schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "500", description = "Have a error while execute command on device", content = {
                    @Content(schema = @Schema(implementation = String.class))}),
    })
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    @PostMapping("/{id}/generic-command")
    public SSHCommandResponseDTO sendCommand(@PathVariable(value = "id") Long deviceId, @RequestBody SSHCommandDTO command) {
        return networkDeviceService.sendCommand(deviceId, command);
    }
}
