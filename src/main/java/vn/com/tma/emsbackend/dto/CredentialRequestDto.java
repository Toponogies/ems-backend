package vn.com.tma.emsbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CredentialRequestDto {
    private String name;

    private String username;

    private String password;
}
