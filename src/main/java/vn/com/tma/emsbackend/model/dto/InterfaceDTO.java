package vn.com.tma.emsbackend.model.dto;

import lombok.Getter;
import lombok.Setter;
import vn.com.tma.emsbackend.model.validation.IpAddress;

import javax.validation.constraints.NotBlank;

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

    @NotBlank(message = "Port field can not empty")
    String port;

    @NotBlank(message = "Interface must belong to a device")
    String networkDevice;
}
