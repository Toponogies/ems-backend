package vn.com.tma.emsbackend.model.entity;

import vn.com.tma.emsbackend.common.enums.Enum;

public class SnmpTrap {
    private int hostId;
    private Enum.State hostState;
    private String hostCommunicationString;
    private int hostPort;
}
