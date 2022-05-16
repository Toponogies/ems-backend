package vn.com.tma.emsbackend.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import vn.com.tma.emsbackend.model.entity.Interface;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PortDTO {
    private Long id;

    private String connector;

    private String macAddress;

    private String name;

    private String state;

    @NotNull(message = "Port must belong to a device")
    private Long networkDeviceId;

    @JsonIgnore
    private Interface anInterface;
}
