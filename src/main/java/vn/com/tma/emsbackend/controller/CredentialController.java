package vn.com.tma.emsbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.tma.emsbackend.dto.CredentialDto;
import vn.com.tma.emsbackend.dto.CredentialRequestDto;
import vn.com.tma.emsbackend.service.credential.CredentialService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/credentials")
public class CredentialController {
    private final CredentialService credentialService;

    @Autowired
    public CredentialController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @GetMapping()
    public ResponseEntity<List<CredentialDto>> getAllCredentials() {
        return ResponseEntity.ok(credentialService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CredentialDto> getCredentialById(@PathVariable(value = "id") Long credentialId) {
        CredentialDto credentialDto = credentialService.get(credentialId);
        return new ResponseEntity<>(credentialDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CredentialDto> addCredential(@RequestBody CredentialRequestDto credentialRequestDto) {
        CredentialDto credentialDto = credentialService.add(credentialRequestDto);
        return new ResponseEntity<>(credentialDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CredentialDto> updateCredential(@PathVariable(value = "id") Long credentialId, @RequestBody CredentialRequestDto credentialRequestDto) {
        CredentialDto credentialDto = credentialService.update(credentialId, credentialRequestDto);
        return new ResponseEntity<>(credentialDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CredentialDto> deleteCredential(@PathVariable(value = "id") Long credentialId) {
        credentialService.delete(credentialId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}
