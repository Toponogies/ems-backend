package vn.com.tma.emsbackend.model.dto;

import lombok.Getter;
import lombok.Setter;
import vn.com.tma.emsbackend.model.validation.IpAddress;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class InterfaceDTO {
    private Long id;

    @NotNull(message = "Name field can not empty")
    private String name;

    private String state;

    private String dhcp;

    @IpAddress(message = "Invalid ip address")
    @NotNull(message = "IP address field can not empty")
    private String ipAddress;

    private String netmask;

    private String gateway;

    Long portId;

    @NotNull(message = "Interface must belong to a device")
    Long networkDeviceId;
}
