package vn.com.tma.emsbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import vn.com.tma.emsbackend.model.dto.ErrorDTO;
import vn.com.tma.emsbackend.model.dto.SSHCommandDTO;
import vn.com.tma.emsbackend.service.ssh.GenericCommandService;


@RequiredArgsConstructor
@RestController
@RequestMapping("/devices/{id}/generic-command")
public class GenericCommandController {
    private final GenericCommandService genericCommandService;

    @Operation(summary = "Send a command directly to device by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully execute command on the device", content = {
                    @Content(schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "422", description = "Have a error while execute command on device", content = {
                    @Content(schema = @Schema(implementation = ErrorDTO.class))}),
    })
    @PostMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public String sendCommandToDeviceById(@PathVariable(value = "id") Long deviceId, @RequestBody SSHCommandDTO sshCommandDTO) {
        return genericCommandService.sendCommandToDeviceById(deviceId, sshCommandDTO);
    }

}
