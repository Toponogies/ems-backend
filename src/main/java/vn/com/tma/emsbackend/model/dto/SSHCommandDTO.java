package vn.com.tma.emsbackend.model.dto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SSHCommandDTO {
    @NotNull(message = "Command field can not be null")
    @NotBlank(message = "Command filed can not be blank")
    String command;

}
