package vn.com.tma.emsbackend.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CredentialDTO {
    private Long id;

    @NotBlank(message = "Credential must not have empty name.")
    private String name;

    @NotBlank(message = "Credential must not have empty username.")
    private String username;

    @NotBlank(message = "Credential must not have empty password.")
    private String password;

    private String devices;

}
