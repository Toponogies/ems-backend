package vn.com.tma.emsbackend.common.constant;

import org.snmp4j.smi.OID;

public class MibDetails {
    public static final OID SEVERITY = new OID("1.3.6.1.4.1.22420.2.1.10.1.5.1");
    public static final OID LAST_TIME_CHANGE = new OID("1.3.6.1.4.1.22420.2.1.11.1.4.1");
    public static final OID ALARM_NUMBER = new OID("1.3.6.1.4.1.22420.2.1.10.1.7.1");
    public static final OID DESCRIPTION = new OID("1.3.6.1.4.1.22420.2.1.10.1.3.1");
    public static final OID CONDITION_TYPE = new OID("1.3.6.1.4.1.22420.2.1.10.1.8.1");
    public static final OID IP = new OID("1.3.6.1.6.3.18.1.3.0");
}

