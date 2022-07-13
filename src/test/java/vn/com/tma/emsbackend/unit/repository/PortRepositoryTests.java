package vn.com.tma.emsbackend.unit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tma.emsbackend.common.enums.Enum;
import vn.com.tma.emsbackend.model.entity.Credential;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.model.entity.Port;
import vn.com.tma.emsbackend.repository.CredentialRepository;
import vn.com.tma.emsbackend.repository.NetworkDeviceRepository;
import vn.com.tma.emsbackend.repository.PortRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PortRepositoryTests {

    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private NetworkDeviceRepository networkDeviceRepository;

    @Autowired
    private PortRepository portRepository;
    private Credential genericCredential;
    private NetworkDevice genericNetworkDevice;
    private Port genericPort;


    @BeforeEach
    void setUp() {
        genericCredential = new Credential();
        genericCredential.setName("admin");
        genericCredential.setPassword("admin");
        genericCredential.setUsername("admin");
        genericCredential.setId(1L);

        genericNetworkDevice = new NetworkDevice();
        genericNetworkDevice.setId(1L);
        genericNetworkDevice.setDeviceType(Enum.NetworkDeviceType.LT);
        genericNetworkDevice.setFirmware("AMO-10000-LT_7.9.4_24860");
        genericNetworkDevice.setIpAddress("10.220.4.5");
        genericNetworkDevice.setLabel("10.220.4.5");
        genericNetworkDevice.setMacAddress("00:15:AD:50:FD:B0");
        genericNetworkDevice.setModel("AMO-10000-LT");
        genericNetworkDevice.setSerial("C410-4492");
        genericNetworkDevice.setSshPort(22);
        genericNetworkDevice.setState(Enum.NetworkDeviceState.IN_SERVICE);
        genericNetworkDevice.setCredential(genericCredential);

        genericPort = new Port();
        genericPort.setId(1L);
        genericPort.setName("PORT-1");
        genericPort.setConnector("PORT-1");
        genericPort.setMacAddress("fe80::a08f:a8ff:fed5:aa1");
        genericPort.setNetworkDevice(genericNetworkDevice);
        genericPort.setState(Enum.State.ENABLED);

        credentialRepository.save(genericCredential);
        networkDeviceRepository.save(genericNetworkDevice);
        portRepository.save(genericPort);
    }


    @Test
    void shouldReturnPortWhenGetPortById() {
        //when
        Port portResult = portRepository.getById(genericPort.getId());

        //then
        assertPortsIsTheSame(portResult, genericPort);
    }

    @Test
    void shouldReturnPortWhenGetPortByDeviceId() {
        //when
        List<Port> portsResult = portRepository.findByNetworkDeviceId(genericPort.getNetworkDevice().getId());

        //then
        assertThat(portsResult).hasSize(1);
        assertPortsIsTheSame(portsResult.get(0), genericPort);
    }

    @Test
    void shouldReturnPortWhenGetPortByDeviceLabel() {
        //when
        List<Port> portsResult = portRepository.findByNetworkDeviceLabel(genericPort.getNetworkDevice().getLabel());

        //then
        assertThat(portsResult).hasSize(1);
        assertPortsIsTheSame(portsResult.get(0), genericPort);
    }

    @Test
    void shouldReturnAllPortWhenGetAllPort() {
        //when
        List<Port> portsResult = portRepository.findAll();

        //then
        assertThat(portsResult).hasSize(1);
        assertPortsIsTheSame(portsResult.get(0), genericPort);
    }

    @Test
    void shouldReturnPortWhenGetPortByDeviceLabelAndByPortName() {
        //when
        Port portResult = portRepository.findByNameAndNetworkDevice_Label(genericPort.getName(), genericPort.getNetworkDevice().getLabel());

        //then
        assertPortsIsTheSame(portResult, genericPort);
    }

    @Test
    void shouldAddPortWhenAddNewPort() {
        //given
        portRepository.save(genericPort);

        //when
        Port portResult = portRepository.findByNameAndNetworkDevice_Label(genericPort.getName(), genericPort.getNetworkDevice().getLabel());

        //then
        assertPortsIsTheSame(portResult, genericPort);
    }

    void assertPortsIsTheSame(Port portResult, Port port) {
        assertThat(portResult.getId()).isEqualTo(port.getId());
        assertThat(portResult.getName()).isEqualTo(port.getName());
        assertThat(portResult.getConnector()).isEqualTo(port.getConnector());
        assertThat(portResult.getMacAddress()).isEqualTo(port.getMacAddress());
        assertThat(portResult.getState()).isEqualTo(port.getState());
        assertThat(portResult.getNetworkDevice().getId()).isEqualTo(port.getNetworkDevice().getId());
    }


}
