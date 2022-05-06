package vn.com.tma.emsbackend.dto;

import vn.com.tma.emsbackend.common.Enum;

public class NDInterfaceDto {
    private String name;

    private Enum.State state;

    private Enum.InterfaceDHCP dhcp;

    private String ipAddress;

    private String netmask;

    private String gateway;
}
