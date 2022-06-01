package vn.com.tma.emsbackend.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class PortDTO {
    private Long id;

    @NotBlank(message = "Name field can not empty")
    private String name;

    private String connector;

    private String macAddress;

    private String state;

    @NotBlank(message = "Port must belong to a device")
    private String networkDevice;

    private List<String> interfaces;
}
