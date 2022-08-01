package vn.com.tma.emsbackend.unit.ssh.parser;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vn.com.tma.emsbackend.common.comparator.InterfaceComparator;
import vn.com.tma.emsbackend.common.enums.Enum;
import vn.com.tma.emsbackend.model.dto.InterfaceDTO;
import vn.com.tma.emsbackend.model.entity.Interface;
import vn.com.tma.emsbackend.model.entity.Port;
import vn.com.tma.emsbackend.parser.InterfaceCommandParser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class InterfaceCommandParserTests {
    private List<Interface> interfacesInit;

    private String interfaceShowResult = "This is a private simulator system. Unauthorized access or use may lead to prosecution.\n" +
            "\n" +
            "\n" +
            "NIDSIM-10.0.1.2: interface show\n" +
            "interface show\n" +
            "\n" +
            "Interface name    State    DHCP     IP address      Netmask         Gateway         Info\n" +
            "----------------- -------- -------- --------------- --------------- --------------- --------------\n" +
            "Management        Enabled  Disabled 10.0.1.2        255.255.255.0   ---             ---            \n" +
            "Network           Enabled  Disabled ---             255.255.255.0   ---             ---            \n" +
            "Auto              Disabled Enabled  ---             255.255.255.0   ---             ---            \n" +
            "\n" +
            "NIDSIM-10.0.1.2:\n";

    private String interfaceDetailResult = "This is a private simulator system. Unauthorized access or use may lead to prosecution.\n" +
            "\n" +
            "\n" +
            "NIDSIM-10.0.1.2: interface show Management\n" +
            "interface show Management\n" +
            "\n" +
            "Interface name: Management\n" +
            "   Interface type  : Standard\n" +
            "   Interface state : Enabled\n" +
            "   IPv4 forwarding : Disabled\n" +
            "   DHCP relay      : Disabled\n" +
            "   On port         : Management\n" +
            "   DHCP            : No\n" +
            "   Static+DHCP     : No\n" +
            "   IP address      : 10.0.1.2\n" +
            "   Netmask         : 255.255.255.0\n" +
            "   Default gateway : 0.0.0.0\n" +
            "   Auxiliary MAC   : Disabled\n" +
            "\n" +
            "NIDSIM-10.0.1.2: \n";

    @BeforeEach
    void setUp() {
        Interface anInterface1 = new Interface();
        anInterface1.setName("Management");
        anInterface1.setState(Enum.State.ENABLED);
        anInterface1.setDhcp(Enum.State.DISABLED);
        anInterface1.setIpAddress("10.0.1.2");
        anInterface1.setNetmask("255.255.255.0");
        anInterface1.setGateway(null);

        Interface anInterface2 = new Interface();
        anInterface2.setName("Network");
        anInterface2.setState(Enum.State.ENABLED);
        anInterface2.setDhcp(Enum.State.DISABLED);
        anInterface2.setIpAddress(null);
        anInterface2.setNetmask("255.255.255.0");
        anInterface2.setGateway(null);

        Interface anInterface3 = new Interface();
        anInterface3.setName("Auto");
        anInterface3.setState(Enum.State.DISABLED);
        anInterface3.setDhcp(Enum.State.ENABLED);
        anInterface2.setIpAddress(null);
        anInterface3.setNetmask("255.255.255.0");
        anInterface3.setGateway(null);

        interfacesInit = new ArrayList<>(Arrays.asList(anInterface1, anInterface2, anInterface3));
    }

    @Test
    void shouldParseCorrectInterfaces() {
        List<Interface> interfaces = InterfaceCommandParser.interfaceShowParse(interfaceShowResult);
        interfacesInit.sort(new InterfaceComparator());
        interfaces.sort(new InterfaceComparator());
        Assertions.assertThat(interfaces.size()).isEqualTo(interfacesInit.size());
        for (int index = 0; index < interfaces.size(); index++) {
            assertInterfacesIsEquals(interfaces.get(index), interfacesInit.get(index));
        }
    }

    @Test
    void shouldParseCorrectInterfaceDetail(){

        Interface anInterface1 = new Interface();
        anInterface1.setName("Management");
        anInterface1.setState(Enum.State.ENABLED);
        anInterface1.setDhcp(Enum.State.DISABLED);
        anInterface1.setIpAddress("10.0.1.2");
        anInterface1.setNetmask("255.255.255.0");
        anInterface1.setGateway("0.0.0.0");

        Interface anInterface = InterfaceCommandParser.interfaceShowDetailParse(interfaceDetailResult);
        Port port = new Port();
        port.setName("Management");
        assertInterfacesIsEquals(anInterface1, anInterface);
    }

    void assertInterfacesIsEquals(Interface thisInterface, Interface thatInterface) {
        assertThat(thisInterface.getName()).isEqualTo(thatInterface.getName());
        assertThat(thisInterface.getGateway()).isEqualTo(thatInterface.getGateway());
        assertThat(thisInterface.getNetmask()).isEqualTo(thatInterface.getNetmask());
        assertThat(thisInterface.getIpAddress()).isEqualTo(thatInterface.getIpAddress());
        assertThat(thisInterface.getDhcp()).isEqualTo(thatInterface.getDhcp());
        assertThat(thisInterface.getState()).isEqualTo(thatInterface.getState());
    }
}
