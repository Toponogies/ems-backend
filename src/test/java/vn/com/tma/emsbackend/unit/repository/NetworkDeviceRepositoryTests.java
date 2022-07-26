package vn.com.tma.emsbackend.unit.repository;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import vn.com.tma.emsbackend.common.enums.Enum;
import vn.com.tma.emsbackend.model.entity.Credential;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.repository.CredentialRepository;
import vn.com.tma.emsbackend.repository.NetworkDeviceRepository;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class NetworkDeviceRepositoryTests {

    @Autowired
    private CredentialRepository credentialRepository;
    @Autowired
    private NetworkDeviceRepository networkDeviceRepository;

    private NetworkDevice networkDevice;

    private Credential genericCredential;

    @BeforeEach
    void setUp() {
        genericCredential = new Credential();
        genericCredential.setId(1L);
        genericCredential.setName("admin");
        genericCredential.setPassword("admin");
        genericCredential.setUsername("admin");
        credentialRepository.save(genericCredential);

        genericCredential = credentialRepository.findByName("admin");
        networkDevice = new NetworkDevice();
        networkDevice.setId(1L);
        networkDevice.setDeviceType(Enum.NetworkDeviceType.LT);
        networkDevice.setFirmware("AMO-10000-LT_7.9.4_24860");
        networkDevice.setIpAddress("10.220.4.5");
        networkDevice.setLabel("10.220.4.5");
        networkDevice.setMacAddress("00:15:AD:50:FD:B0");
        networkDevice.setModel("AMO-10000-LT");
        networkDevice.setSerial("C410-4492");
        networkDevice.setSshPort(22);
        networkDevice.setState(Enum.NetworkDeviceState.IN_SERVICE);
        networkDevice.setCredential(genericCredential);
    }

    @Test
    void shouldAddNewNetworkDevice() {
        //when
        networkDeviceRepository.save(networkDevice);

        //then
        List<NetworkDevice> networkDevices = networkDeviceRepository.findAll();
        assertThat(networkDevices).hasSize(1);
        assertThatNetworkDeviceIsEqual(networkDevices.get(0), networkDevice);

    }

    @Test
    void shouldUpdateNewNetworkDevice() {
        //when
        networkDeviceRepository.save(networkDevice);
        networkDevice.setIpAddress("10.220.4.6");
        networkDevice.setLabel("10.220.4.6");
        networkDeviceRepository.save(networkDevice);

        //then
        List<NetworkDevice> networkDevices = networkDeviceRepository.findAll();
        assertThat(networkDevices).hasSize(1);
        assertThatNetworkDeviceIsEqual(networkDevices.get(0), networkDevice);

    }

    @Test
    void shouldReturnDeviceWithIpAddressWhenGetByIpAddress() {
        //give
        networkDeviceRepository.save(networkDevice);

        //when
        networkDeviceRepository.findByIpAddress(networkDevice.getIpAddress());

        //then
        NetworkDevice networkDeviceResult = networkDeviceRepository.findByIpAddress(networkDevice.getIpAddress());
        assertThatNetworkDeviceIsEqual(networkDeviceResult, networkDevice);
    }


    @Test
    void shouldReturnDevicesWithTypeWhenGetByType() {
        //give
        networkDeviceRepository.save(networkDevice);

        //when
        networkDeviceRepository.findAllByDeviceType(networkDevice.getDeviceType());

        //then
        List<NetworkDevice> networkDevices = networkDeviceRepository.findAllByDeviceType(Enum.NetworkDeviceType.LT);
        assertThat(networkDevices).hasSize(1);
        assertThatNetworkDeviceIsEqual(networkDevices.get(0), networkDevice);
    }

    @Test
    void shouldReturEmptyWhenGetByType() {
        //give
        networkDeviceRepository.save(networkDevice);

        //when
        networkDeviceRepository.findAllByDeviceType(networkDevice.getDeviceType());

        //then
        List<NetworkDevice> networkDevices = networkDeviceRepository.findAllByDeviceType(Enum.NetworkDeviceType.VCX);
        assertThat(networkDevices).isEmpty();
    }

    @Test
    void shouldReturnTrueWhenGetExistByLabel() {
        //give
        networkDeviceRepository.save(networkDevice);

        //when
        boolean isExisted  = networkDeviceRepository.existsByLabel(networkDevice.getLabel());

        //then
        assertThat(isExisted).isTrue();
    }

    @Test
    void shouldReturnFalseWhenGetExistByLabel() {
        //give
        networkDeviceRepository.save(networkDevice);

        //when
        boolean isExisted  = networkDeviceRepository.existsByLabel("random_label");

        //then
        assertThat(isExisted).isFalse();
    }

    @Test
    void shouldReturnTrueWhenGetExistByIpAddress() {
        //give
        networkDeviceRepository.save(networkDevice);

        //when
        boolean isExisted  = networkDeviceRepository.existsByIpAddress(networkDevice.getIpAddress());

        //then
        assertThat(isExisted).isTrue();
    }

    @Test
    void shouldReturnFalseWhenGetExistByIpAddress() {
        //give
        networkDeviceRepository.save(networkDevice);

        //when
        boolean isExisted  = networkDeviceRepository.existsByIpAddress("random_ip_address");

        //then
        assertThat(isExisted).isFalse();
    }




    @Test
    void shouldReturnNetworkDeviceWhenFindByLabel() {
        //give
        networkDeviceRepository.save(networkDevice);

        //when
        NetworkDevice networkDeviceResult  = networkDeviceRepository.findByLabel(networkDevice.getLabel());

        //then
        assertThatNetworkDeviceIsEqual(networkDeviceResult, networkDevice);
    }

    void assertThatNetworkDeviceIsEqual(NetworkDevice networkDeviceResult, NetworkDevice networkDevice) {
        assertThat(networkDeviceResult.getId()).isEqualTo(networkDevice.getId());
        assertThat(networkDeviceResult.getIpAddress()).isEqualTo(networkDevice.getIpAddress());
        assertThat(networkDeviceResult.getDeviceType()).isEqualTo(networkDevice.getDeviceType());
        assertThat(networkDeviceResult.getDeviceType()).isEqualTo(networkDevice.getDeviceType());
        assertThat(networkDeviceResult.getFirmware()).isEqualTo(networkDevice.getFirmware());
        assertThat(networkDeviceResult.getMacAddress()).isEqualTo(networkDevice.getMacAddress());
        assertThat(networkDeviceResult.getSshPort()).isEqualTo(networkDevice.getSshPort());
        assertThat(networkDeviceResult.getLabel()).isEqualTo(networkDevice.getLabel());

        assertThat(networkDeviceResult.getCredential().getId()).isEqualTo(networkDevice.getCredential().getId());
    }


}
