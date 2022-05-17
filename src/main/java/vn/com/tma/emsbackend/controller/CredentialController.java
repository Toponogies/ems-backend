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
import vn.com.tma.emsbackend.model.dto.CredentialDTO;
import vn.com.tma.emsbackend.model.dto.ErrorDTO;
import vn.com.tma.emsbackend.service.credential.CredentialService;

import javax.validation.Valid;
import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/credentials")
public class CredentialController {
    private final CredentialService credentialService;

    @Operation(summary = "Get all credentials")
    @ApiResponse(responseCode = "200",
            description = "Found list of all credentials",
            content = {@Content(array = @ArraySchema(schema = @Schema(implementation = CredentialDTO.class)))})
    @GetMapping()
    public Collection<CredentialDTO> getAllCredentials() {
        return credentialService.getAll();
    }

    @Operation(summary = "Get a specific credential by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Found the credential",
                    content = {@Content(schema = @Schema(implementation = CredentialDTO.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Credential not found",
                    content = {@Content(schema = @Schema(implementation = ErrorDTO.class))})
    })
    @GetMapping("/{id}")
    public CredentialDTO getCredentialById(@PathVariable(value = "id") Long credentialId) {
        return credentialService.get(credentialId);
    }

    @Operation(summary = "Add a new credential")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Added the credential",
                    content = {@Content(schema = @Schema(implementation = CredentialDTO.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid data",
                    content = {@Content(schema = @Schema(implementation = ErrorDTO.class))})
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CredentialDTO addCredential(@Valid @RequestBody CredentialDTO credentialDto) {
        return credentialService.add(credentialDto);
    }

    @Operation(summary = "Update a specific credential by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Edited the credential",
                    content = {@Content(schema = @Schema(implementation = CredentialDTO.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Credential not found",
                    content = {@Content(schema = @Schema(implementation = ErrorDTO.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid data",
                    content = {@Content(schema = @Schema(implementation = ErrorDTO.class))})
    })
    @PutMapping("/{id}")
    public CredentialDTO updateCredential(@PathVariable(value = "id") Long credentialId, @Valid @RequestBody CredentialDTO credentialDto) {
        return credentialService.update(credentialId, credentialDto);
    }

    @Operation(summary = "Delete a specific credential by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Edited the credential",
                    content = {@Content(schema = @Schema(implementation = CredentialDTO.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Credential not found",
                    content = {@Content(schema = @Schema(implementation = ErrorDTO.class))})
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCredential(@PathVariable(value = "id") Long credentialId) {
        credentialService.delete(credentialId);
    }
}
