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
import vn.com.tma.emsbackend.service.configuration.ConfigurationService;

import javax.servlet.http.HttpServletResponse;


@RequiredArgsConstructor
@RestController
@RequestMapping("/devices/{id}/configuration")
public class ConfigurationController {
    private final ConfigurationService configurationService;

    @Operation(summary = "Download configuration file from network device")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully get configuration file from device", content = {
                    @Content(schema = @Schema(implementation = byte[].class))}),
            @ApiResponse(responseCode = "422", description = "Have a error while get configuration from device", content = {
                    @Content(schema = @Schema(implementation = ErrorDTO.class))}),
    })
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(produces = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public byte[] downloadDeviceConfigFileById(@PathVariable(value = "id") Long deviceId, HttpServletResponse response) {
        byte[] bytes = configurationService.downloadDeviceConfigFileById(deviceId);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + "configuration_" + deviceId + "\"");
        return bytes;
    }
}
