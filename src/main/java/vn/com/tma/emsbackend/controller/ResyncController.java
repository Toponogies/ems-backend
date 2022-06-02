package vn.com.tma.emsbackend.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.com.tma.emsbackend.model.dto.ErrorDTO;
import vn.com.tma.emsbackend.service.ssh.ResyncService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/resynchronization")
public class ResyncController {

    private final ResyncService resyncService;

    @Operation(summary = "Resync a list of network devices")
    @ApiResponses(value = {@ApiResponse(responseCode = "202", description = "Successfully add device to resync queue", content = {@Content(schema = @Schema())}), @ApiResponse(responseCode = "404", description = "Network device not found", content = {@Content(schema = @Schema(implementation = ErrorDTO.class))}),})
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    @PostMapping()
    public void resync(@RequestBody List<Long> deviceIds) {
        resyncService.addDevicesToResyncQueueById(deviceIds);
    }

}
