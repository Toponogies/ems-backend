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
import vn.com.tma.emsbackend.service.configuration.ConfigurationService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.zip.ZipOutputStream;


@RequiredArgsConstructor
@RestController
@RequestMapping("/devices/configuration")
public class ConfigurationController {
    private final ConfigurationService configurationService;

    @Operation(summary = "Download configuration file from network device")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully get configuration file from device", content = {
                    @Content(schema = @Schema(implementation = byte[].class))}),
            @ApiResponse(responseCode = "422", description = "Have a error while get configuration from device", content = {
                    @Content(schema = @Schema(implementation = ErrorDTO.class))}),
    })
    @PostMapping()
    public byte[] downloadDeviceConfigFileById(@RequestBody List<Long> deviceIds, HttpServletResponse response) {
        try {
            response.setHeader("Content-Disposition", "attachment; filename=\"download.zip\"");
            return configurationService.downloadDeviceConfigFileById(deviceIds);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
