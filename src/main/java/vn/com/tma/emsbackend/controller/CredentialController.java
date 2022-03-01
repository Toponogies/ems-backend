package vn.com.tma.emsbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.tma.emsbackend.dto.CredentialDto;
import vn.com.tma.emsbackend.dto.CredentialRequestDto;
import vn.com.tma.emsbackend.dto.ErrorDto;
import vn.com.tma.emsbackend.service.credential.CredentialService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/credentials")
public class CredentialController {
    private final CredentialService credentialService;

    @Operation(summary = "Get all credentials")
    @ApiResponse(responseCode = "200",
            description = "Found list of all credentials",
            content = {@Content(array = @ArraySchema(schema = @Schema(implementation = CredentialDto.class)))})
    @GetMapping()
    public ResponseEntity<List<CredentialDto>> getAllCredentials() {
        return ResponseEntity.ok(credentialService.getAll());
    }

    @Operation(summary = "Get a specific credential by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Found the credential",
                    content = {@Content(schema = @Schema(implementation = CredentialDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Credential not found",
                    content = {@Content(schema = @Schema(implementation = ErrorDto.class))})
    })
    @GetMapping("/{id}")
    public ResponseEntity<CredentialDto> getCredentialById(@PathVariable(value = "id") Long credentialId) {
        CredentialDto credentialDto = credentialService.get(credentialId);
        return new ResponseEntity<>(credentialDto, HttpStatus.OK);
    }

    @Operation(summary = "Add a new credential")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Added the credential",
                    content = {@Content(schema = @Schema(implementation = CredentialDto.class))}),
            @ApiResponse(responseCode = "409",
                    description = "Constraint violated",
                    content = {@Content(schema = @Schema(implementation = ErrorDto.class))})
    })
    @PostMapping
    public ResponseEntity<CredentialDto> addCredential(@RequestBody CredentialRequestDto credentialRequestDto) {
        CredentialDto credentialDto = credentialService.add(credentialRequestDto);
        return new ResponseEntity<>(credentialDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a specific credential by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Edited the credential",
                    content = {@Content(schema = @Schema(implementation = CredentialDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Credential not found",
                    content = {@Content(schema = @Schema(implementation = ErrorDto.class))}),
            @ApiResponse(responseCode = "409",
                    description = "Constraint violated",
                    content = {@Content(schema = @Schema(implementation = ErrorDto.class))})
    })
    @PutMapping("/{id}")
    public ResponseEntity<CredentialDto> updateCredential(@PathVariable(value = "id") Long credentialId, @RequestBody CredentialRequestDto credentialRequestDto) {
        CredentialDto credentialDto = credentialService.update(credentialId, credentialRequestDto);
        return new ResponseEntity<>(credentialDto, HttpStatus.OK);
    }

    @Operation(summary = "Delete a specific credential by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Edited the credential",
                    content = {@Content(schema = @Schema(implementation = CredentialDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Credential not found",
                    content = {@Content(schema = @Schema(implementation = ErrorDto.class))})
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<CredentialDto> deleteCredential(@PathVariable(value = "id") Long credentialId) {
        credentialService.delete(credentialId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}
