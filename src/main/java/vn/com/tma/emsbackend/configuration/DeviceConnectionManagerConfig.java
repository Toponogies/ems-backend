package vn.com.tma.emsbackend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tma.emsbackend.common.DeviceConnectionManager;
import vn.com.tma.emsbackend.entity.NDInterface;
import vn.com.tma.emsbackend.parser.InterfaceCommandParser;
import vn.com.tma.emsbackend.parser.PortCommandParser;
import vn.com.tma.emsbackend.repository.NetworkDeviceRepository;

@Configuration
public class DeviceConnectionManagerConfig {
    @Bean
    @Transactional
    public DeviceConnectionManager deviceConnectionManager(NetworkDeviceRepository networkDeviceRepository) {
        var a = PortCommandParser.portShowConfigCommand("This is a private computer system. Unauthorized access or use may lead to prosecution.\n" +
                "\n" +
                "58794D56-71B3-AEC7-FF8D-0A6BB9EC37CC: port show configuration \n" +
                "\n" +
                "Connector  Port name                       State    Speed     MTU   MDI  MAC address         \n" +
                "---------- ------------------------------- -------- --------- ----- ---- --------------------\n" +
                "---        LOCAL-1                         Enabled  Auto      ---   Auto 00:0C:29:EC:37:CC   \n" +
                "---        LOCAL-2                         Enabled  Auto      ---   Auto 04:0A:34:EF:21:B4   \n" +
                "---        LOCAL-3                         Enabled  Auto      ---   Auto 00:0C:29:EC:37:E0   \n" +
                "RJ45-1     C404-1671-NNI                   Enabled  Auto      10240 Auto 00:15:AD:3B:50:30   \n" +
                "SFP-2      C404-1671-UNI                   Enabled  Auto      10240 Auto 00:15:AD:3B:50:30   \n" +
                "---        C404-1671-vNNI                  Enabled  Auto      ---   Auto 02:15:AD:3B:50:30   \n" +
                "---        C404-1671-vUNI                  Enabled  Auto      ---   Auto 02:15:AD:3B:50:31   \n" +
                "\n" +
                "58794D56-71B3-AEC7-FF8D-0A6BB9EC37CC: \n");
        return new DeviceConnectionManager(networkDeviceRepository);
    }
}
