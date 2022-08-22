package vn.com.tma.emsbackend.receiver;

import lombok.extern.slf4j.Slf4j;
import org.snmp4j.*;
import org.snmp4j.mp.*;
import org.snmp4j.security.*;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;
import org.springframework.stereotype.Component;
import vn.com.tma.emsbackend.common.commandgenerator.SnmpTrapConfigCommandGenerator;
import vn.com.tma.emsbackend.model.entity.Alarm;
import vn.com.tma.emsbackend.parser.snmp.SnmpTrapParser;
import vn.com.tma.emsbackend.service.alarm.AlarmService;

import java.io.IOException;

@Component
@Slf4j
public class SnmpReceiver implements CommandResponder {
    private final AlarmService alarmService;
    // some constants
    private static final String SNMP_RECEIVER_NAME = "SNMP-Receiver";
    private static final String SNMP4J_LISTEN_ADDRESS_PROPERTY = "snmp4j.listenAddress";
    private static final String UDP_GENERIC_ADDRESS = "udp:0.0.0.0/";
    private static final int DEFAULT_SNMP_RECEIVER_SIZE = 10;


    private Address listenAddress;
    private Address informListenAddress;
    private MultiThreadedMessageDispatcher dispatcher;
    private Snmp snmp;
    private ThreadPool threadPool;
    private TransportMapping<UdpAddress> transport;

    // the receiver size
    private int receiverSize = 0;

    public SnmpReceiver(AlarmService alarmService) {
        this.alarmService = alarmService;
        start();
    }


    public void start() {
        log.info(SnmpTrapConfigCommandGenerator.editSnmpTrapV2c());
        // instantiate the message dispatcher
        MessageDispatcherImpl mdi = new MessageDispatcherImpl();

        // create the thread pool used by the message dispatcher
        threadPool = ThreadPool.create(SNMP_RECEIVER_NAME, (receiverSize == 0 ? DEFAULT_SNMP_RECEIVER_SIZE : receiverSize));
        // instantiate the multi threaded message dispatcher
        dispatcher = new MultiThreadedMessageDispatcher(threadPool, mdi);
        // UDP address
        listenAddress = GenericAddress.parse(System.getProperty(
                "snmp4j.listenAddress", "udp:0.0.0.0/3002"));
        // INFORM address
        informListenAddress = GenericAddress.parse(System.getProperty("snmp4j.listenAddress",
                "udp:0.0.0.0/3001"));

        // set the transport mapping with the listen address
        if (listenAddress instanceof UdpAddress) {
            try {
                transport = new DefaultUdpTransportMapping((UdpAddress) listenAddress);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException();
        }

        // instantiate the SNMP API
        snmp = new Snmp(dispatcher, transport);
        // add this command responder instance to the SNMP API it is to be able
        // to process PDU (SNMP Traps)
        snmp.addCommandResponder(this);

        // respond to INFORMs
        snmp.addNotificationListener(informListenAddress, this);

        // prepare some security for SNMPv3.
        byte[] localEngineId = MPv3.createLocalEngineID();

        SecurityProtocols.getInstance().addDefaultProtocols();
        SecurityProtocols.getInstance().addAuthenticationProtocol(new AuthSHA());
        SecurityProtocols.getInstance().addPrivacyProtocol(new Priv3DES());
        SecurityProtocols.getInstance().addPrivacyProtocol(new PrivAES128());

        USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(localEngineId), 0);
        usm.setEngineDiscoveryEnabled(true);

        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv1());
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv2c());
        snmp.getMessageDispatcher().addMessageProcessingModel(new MPv3(usm));

        snmp.setLocalEngine( localEngineId, 0, 0 );
        SecurityModels.getInstance().addSecurityModel(usm);

        // listen and get ready to receive...
        try {
            UsmUser usmUser = new UsmUser( new OctetString( "MD5DES" ),
                    AuthMD5.ID,
                    new OctetString("authPassword"),
                    Priv3DES.ID,
                    new OctetString("privPassword"));
            snmp.getUSM().addUser( new OctetString("emsazerty!"), usmUser );
            snmp.listen();
        } catch (IOException ioe) {
            throw new RuntimeException();
        }
    }

    @Override
    public <A extends Address> void processPdu(CommandResponderEvent<A> commandResponderEvent) {
        PDU pdu = commandResponderEvent.getPDU();
        Alarm alarm = SnmpTrapParser.parseToAlarm(pdu);
        String ipAddress = SnmpTrapParser.parseIp(pdu);
        alarmService.sendNewAlarmWebSocket(alarm, ipAddress);
        log.info("Get new alarm from device with ip:" + ipAddress + " with content:" + alarm.toString());
    }
}
