package vn.com.tma.emsbackend.util.entity.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {
    private String access_token;
    private String expires_in;
    private String refresh_expires_in;
    private String refresh_token;
    private String token_type;
}
