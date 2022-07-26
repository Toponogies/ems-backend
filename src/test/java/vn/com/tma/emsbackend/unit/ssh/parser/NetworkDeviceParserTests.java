package vn.com.tma.emsbackend.unit.ssh.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vn.com.tma.emsbackend.common.enums.Enum;
import vn.com.tma.emsbackend.model.dto.NetworkDeviceDTO;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.parser.NetworkDeviceCommandParser;

import static org.assertj.core.api.Assertions.assertThat;

public class NetworkDeviceParserTests {

    private String networkDetailResult =
            "This is a private simulator system. Unauthorized access or use may lead to prosecution.\n" +
            "\n" +
            "\n" +
            "NIDSIM-10.0.1.2: board show info\n" +
            "board show info\n" +
            "\n" +
            "Product name       : AMN-1000-GT\n" +
            "MAC base address   : 02:9d:b4:c3:df:18\n" +
            "Unit identifier    : NIDSIM-10.0.1.2\n" +
            "Firmware version   : AMT_7.0_2123\n" +
            "Serial number      : GtSN10-0-1-2\n" +
            "Assembly           : 500-032004:19:22:00\n" +
            "Hardware options   : GtHW\n" +
            "Manufacture config : 0000\n" +
            "HTTPS port         : 8081\n" +
            "HTTP port          : 8080\n" +
            "\n" +
            "NIDSIM-10.0.1.2: \n";

    private NetworkDevice genericNetworkDevice;

    @BeforeEach
    void setUp() {
        genericNetworkDevice = new NetworkDevice();
        genericNetworkDevice.setDeviceType(Enum.NetworkDeviceType.GT);
        genericNetworkDevice.setFirmware("AMT_7.0_2123");
        genericNetworkDevice.setIpAddress("10.0.1.2");
        genericNetworkDevice.setLabel("10.0.1.2");
        genericNetworkDevice.setMacAddress("02:9d:b4:c3:df:18");
        genericNetworkDevice.setModel("AMN-1000-GT");
        genericNetworkDevice.setSerial("GtSN10-0-1-2");
    }

    @Test
    void shouldParseCorrectInformation(){
        NetworkDevice networkDevice = NetworkDeviceCommandParser.boardShowInfoCommandParse(networkDetailResult);
        assertThatNetworkDeviceIsEqual(networkDevice, genericNetworkDevice);
    }

    void assertThatNetworkDeviceIsEqual(NetworkDevice thisNetworkDevice, NetworkDevice thatNetworkDevice) {
        assertThat(thisNetworkDevice.getDeviceType()).isEqualTo(thatNetworkDevice.getDeviceType());
        assertThat(thisNetworkDevice.getFirmware()).isEqualTo(thatNetworkDevice.getFirmware());
        assertThat(thisNetworkDevice.getMacAddress()).isEqualTo(thatNetworkDevice.getMacAddress());
        assertThat(thisNetworkDevice.getSerial()).isEqualTo(thatNetworkDevice.getSerial());
        assertThat(thisNetworkDevice.getMacAddress()).isEqualTo(thatNetworkDevice.getMacAddress());
        assertThat(thisNetworkDevice.getModel()).isEqualTo(thatNetworkDevice.getModel());

    }
}
