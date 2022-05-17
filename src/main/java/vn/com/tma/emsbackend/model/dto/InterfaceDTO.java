package vn.com.tma.emsbackend.model.dto;

import lombok.Getter;
import lombok.Setter;
import vn.com.tma.emsbackend.model.validation.IpAddress;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class InterfaceDTO {
    private Long id;

    @NotBlank(message = "Name field can not empty")
    private String name;

    private String state;

    private String dhcp;

    @NotBlank(message = "IP address field can not empty")
    @IpAddress(message = "Invalid ip address")
    private String ipAddress;

    private String netmask;

    private String gateway;

    Long portId;

    @NotNull(message = "Interface must belong to a device")
    Long networkDeviceId;
}
