package vn.com.tma.emsbackend.unit.ssh.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vn.com.tma.emsbackend.common.comparator.PortComparator;
import vn.com.tma.emsbackend.common.enums.Enum;
import vn.com.tma.emsbackend.model.entity.Port;
import vn.com.tma.emsbackend.parser.PortCommandParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PortCommandParserTests {
    private List<Port> portsInit;
    private String portShowResult =
            "This is a private simulator system. Unauthorized access or use may lead to prosecution.\n" +
                    "\n" +
                    "\n" +
                    "NIDSIM-10.0.1.2: port show configuration\n" +
                    "port show configuration\n" +
                    "Connector  Port name         State    Speed     MTU   MDI  MAC address\n" +
                    "---------- ----------------- -----    --------- ----- ---- -----------------\n" +
                    "SFP-1      PORT-1            Enabled  100Mbps   2000  Auto 00:15:AD:b1:c1:6e\n" +
                    "SFP-2      PORT-2            Enabled  100Mbps   2000  Auto 00:15:AD:c5:97:40\n" +
                    "RJ45-3     PORT-3            Enabled  100Mbps   2000  Auto 00:15:AD:b7:aa:2b\n" +
                    "RJ45-4     PORT-4            Enabled  100Mbps   2000  Auto 00:15:AD:9d:dc:b7\n" +
                    "Management Management        Enabled  100Mbps   2000  Auto 00:15:AD:3e:a2:39\n" +
                    "---        LAG-1             Enabled  ---       ---   ---  00:15:AD:b1:c1:6e\n" +
                    "\n" +
                    "NIDSIM-10.0.1.2: \n";

    @BeforeEach
    void setUp() {
        Port port1 = new Port();
        port1.setConnector("SFP-1");
        port1.setName("PORT-1");
        port1.setState(Enum.State.ENABLED);
        port1.setMacAddress("00:15:AD:b1:c1:6e");

        Port port2 = new Port();
        port2.setConnector("SFP-2");
        port2.setName("PORT-2");
        port2.setState(Enum.State.ENABLED);
        port2.setMacAddress("00:15:AD:c5:97:40");

        Port port3 = new Port();
        port3.setConnector("RJ45-3");
        port3.setName("PORT-3");
        port3.setState(Enum.State.ENABLED);
        port3.setMacAddress("00:15:AD:b7:aa:2b");

        Port port4 = new Port();
        port4.setConnector("RJ45-4");
        port4.setName("PORT-4");
        port4.setState(Enum.State.ENABLED);
        port4.setMacAddress("00:15:AD:9d:dc:b7");

        Port port5 = new Port();
        port5.setConnector("Management");
        port5.setName("Management");
        port5.setState(Enum.State.ENABLED);
        port5.setMacAddress("00:15:AD:3e:a2:39");

        Port port6 = new Port();
        port6.setConnector(null);
        port6.setName("LAG-1");
        port6.setState(Enum.State.ENABLED);
        port6.setMacAddress("00:15:AD:b1:c1:6e");
        portsInit = new ArrayList<>(Arrays.asList(port1, port2, port3, port4, port5, port6));
    }

    @Test
    void shouldParseAllPort() {
        List<Port> ports = PortCommandParser.portShowConfigCommand(portShowResult);
        ports.sort(new PortComparator());
        portsInit.sort(new PortComparator());
        assertThat(ports.size()).isEqualTo(portsInit.size());
        for (int index = 0; index < ports.size(); index++) {
            assertPortsIsEquals(ports.get(index), portsInit.get(index));
        }
    }

    void assertPortsIsEquals(Port portResult, Port port) {
        assertThat(portResult.getName()).isEqualTo(port.getName());
        assertThat(portResult.getConnector()).isEqualTo(port.getConnector());
        assertThat(portResult.getMacAddress()).isEqualTo(port.getMacAddress());
        assertThat(portResult.getState()).isEqualTo(port.getState());
    }
}
