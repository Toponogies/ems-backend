package vn.com.tma.emsbackend.unit.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.com.tma.emsbackend.common.enums.Enum;
import vn.com.tma.emsbackend.model.dto.NetworkDeviceDTO;
import vn.com.tma.emsbackend.model.entity.Credential;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.model.exception.DeviceIPExistsException;
import vn.com.tma.emsbackend.model.exception.DeviceLabelExistsException;
import vn.com.tma.emsbackend.model.exception.DeviceNotFoundException;
import vn.com.tma.emsbackend.model.mapper.NetworkDeviceMapper;
import vn.com.tma.emsbackend.model.mapper.NetworkDeviceMapperImpl;
import vn.com.tma.emsbackend.repository.NetworkDeviceRepository;
import vn.com.tma.emsbackend.repository.ssh.NetworkDeviceSSHRepository;
import vn.com.tma.emsbackend.service.credential.CredentialService;
import vn.com.tma.emsbackend.service.device.NetworkDeviceService;
import vn.com.tma.emsbackend.service.device.NetworkDeviceServiceImpl;
import vn.com.tma.emsbackend.service.ssh.NetworkDeviceSSHService;
import vn.com.tma.emsbackend.service.ssh.utils.DeviceConnectionManager;
import vn.com.tma.emsbackend.service.ssh.utils.ResyncQueueManager;
import vn.com.tma.emsbackend.util.entity.creator.CredentialCreator;
import vn.com.tma.emsbackend.util.entity.creator.NetworkDeviceCreator;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NetworkDeviceServiceTests {
    @Mock
    private NetworkDeviceRepository networkDeviceRepository;

    @Mock
    private CredentialService credentialService;

    private final ResyncQueueManager resyncQueueManager = new ResyncQueueManager(networkDeviceRepository);

    @Mock
    private DeviceConnectionManager deviceConnectionManager = new DeviceConnectionManager(networkDeviceRepository);

    private final NetworkDeviceSSHRepository networkDeviceSSHRepository = new NetworkDeviceSSHRepository();
    private final NetworkDeviceSSHService networkDeviceSSHService = new NetworkDeviceSSHService(networkDeviceSSHRepository);

    private NetworkDeviceService networkDeviceService;

    private Credential genericCredential;

    private NetworkDeviceDTO networkDeviceDTO;

    private NetworkDevice networkDevice;


    @BeforeEach
    void setUp() {
        NetworkDeviceMapper networkDeviceMapper = new NetworkDeviceMapperImpl();
        networkDeviceService = new NetworkDeviceServiceImpl(
                networkDeviceRepository,
                networkDeviceSSHService,
                networkDeviceMapper,
                credentialService,
                resyncQueueManager
        );
        genericCredential = new Credential();
        genericCredential.setName("admin");
        genericCredential.setPassword("admin");
        genericCredential.setUsername("admin");
        genericCredential.setId(1L);


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

        networkDeviceDTO = NetworkDeviceCreator.createDTOBy(networkDevice);
    }

    @Test
    void shouldGetAllNetworkDeviceWhenGetAll() {
        //give
        when(networkDeviceRepository.findAll()).thenReturn(Collections.singletonList(networkDevice));

        //when
        List<NetworkDeviceDTO> networkDeviceDTOS = networkDeviceService.getAll();

        assertThat(networkDeviceDTOS).hasSize(1);
        NetworkDeviceDTO networkDeviceDTOResult = networkDeviceDTOS.get(0);

        assertThatNetworkDeviceIsEqual(networkDeviceDTOResult, networkDeviceDTO);
    }


    @Test
    void shouldGetNetworkDeviceWhenGetById() {
        //give
        when(networkDeviceRepository.findById(networkDevice.getId())).thenReturn(Optional.ofNullable(networkDevice));

        //when
        NetworkDeviceDTO networkDeviceDTOResult = networkDeviceService.get(networkDevice.getId());

        assertThatNetworkDeviceIsEqual(networkDeviceDTOResult, networkDeviceDTO);
    }


    @Test
    void shouldGetNetworkDeviceWhenGetByIpAddress() {
        //give
        when(networkDeviceRepository.findByIpAddress(networkDevice.getIpAddress())).thenReturn(networkDevice);

        NetworkDeviceDTO networkDeviceDTOResult = networkDeviceService.getByIpAddress(networkDevice.getIpAddress());

        assertThatNetworkDeviceIsEqual(networkDeviceDTOResult, networkDeviceDTO);
    }


    @Test
    void shouldGetNetworkDeviceWhenGetByDeviceType() {
        //give
        when(networkDeviceRepository.findAllByDeviceType(networkDevice.getDeviceType())).thenReturn(List.of(networkDevice));

        //when
        List<NetworkDeviceDTO> networkDevices = networkDeviceService.getByDeviceType(networkDevice.getDeviceType());
        NetworkDeviceDTO networkDeviceDTOResult = networkDevices.get(0);

        assertThatNetworkDeviceIsEqual(networkDeviceDTOResult, networkDeviceDTO);
    }


    @Test
    void shouldThrowDeviceNotFoundWhenGetNotExistDevice() {
        //give
        when(networkDeviceRepository.findById(anyLong())).thenReturn(Optional.empty());


        //when
        assertThatThrownBy(() -> networkDeviceService.get(2L)).isInstanceOf(DeviceNotFoundException.class);
    }


    @Test
    void shouldReturnEmptyArrayWhenGetByDeviceTypeHaveNoDevice() {
        //give
        when(networkDeviceRepository.findAllByDeviceType(any(Enum.NetworkDeviceType.class))).thenReturn(List.of());

        //when
        List<NetworkDeviceDTO> networkDevices = networkDeviceService.getByDeviceType(networkDevice.getDeviceType());

        assertThat(networkDevices).isEmpty();
    }

    @Test
    void shouldAddNetworkDeviceWhenAddNewDevice() {
        //given
        when(networkDeviceRepository.save(networkDevice)).thenReturn(networkDevice);
        when(credentialService.getByName(networkDevice.getCredential().getName())).thenReturn(CredentialCreator.createCredentialDtoBy(genericCredential));

        //when
        networkDeviceDTO = NetworkDeviceCreator.createDTOBy(networkDevice);
        NetworkDeviceDTO networkDeviceDTOResult = networkDeviceService.add(networkDeviceDTO);

        assertThat(resyncQueueManager.getResyncStatus(networkDevice.getId())).isEqualTo(Enum.ResyncStatus.ONGOING);
        assertThatNetworkDeviceIsEqualBeforeResync(networkDeviceDTOResult, networkDeviceDTO);
    }


    @Test
    void shouldThrowExceptionWhenAddNewDeviceWithExistedLabel() {
        //given
        when(networkDeviceRepository.existsByLabel(networkDevice.getLabel())).thenReturn(true);

        //when
        networkDeviceDTO = NetworkDeviceCreator.createDTOBy(networkDevice);
        assertThatThrownBy(() -> networkDeviceService.add(networkDeviceDTO)).isInstanceOf(DeviceLabelExistsException.class);

    }

    @Test
    void shouldThrowExceptionWhenAddNewDeviceWithExistedIpAddress() {
        //given
        when(networkDeviceRepository.existsByIpAddress(networkDevice.getLabel())).thenReturn(true);

        //when
        networkDeviceDTO = NetworkDeviceCreator.createDTOBy(networkDevice);
        assertThatThrownBy(() -> networkDeviceService.add(networkDeviceDTO)).isInstanceOf(DeviceIPExistsException.class);

    }



    void assertThatNetworkDeviceIsEqual(NetworkDeviceDTO networkDeviceDTOResult, NetworkDeviceDTO networkDeviceDTO) {
        assertThat(networkDeviceDTOResult.getId()).isEqualTo(networkDeviceDTO.getId());
        assertThat(networkDeviceDTOResult.getIpAddress()).isEqualTo(networkDeviceDTO.getIpAddress());
        assertThat(networkDeviceDTOResult.getDeviceType()).isEqualTo(networkDeviceDTO.getDeviceType());
        assertThat(networkDeviceDTOResult.getCredential()).isEqualTo(networkDeviceDTO.getCredential());
        assertThat(networkDeviceDTOResult.getDeviceType()).isEqualTo(networkDeviceDTO.getDeviceType());
        assertThat(networkDeviceDTOResult.getFirmware()).isEqualTo(networkDeviceDTO.getFirmware());
        assertThat(networkDeviceDTOResult.getMacAddress()).isEqualTo(networkDeviceDTO.getMacAddress());
        assertThat(networkDeviceDTOResult.getSshPort()).isEqualTo(networkDeviceDTO.getSshPort());
        assertThat(networkDeviceDTOResult.getLabel()).isEqualTo(networkDeviceDTO.getLabel());
    }

    void assertThatNetworkDeviceIsEqualBeforeResync(NetworkDeviceDTO networkDeviceDTOResult, NetworkDeviceDTO networkDeviceDTO) {
        assertThat(networkDeviceDTOResult.getId()).isEqualTo(networkDeviceDTO.getId());
        assertThat(networkDeviceDTOResult.getIpAddress()).isEqualTo(networkDeviceDTO.getIpAddress());
        assertThat(networkDeviceDTOResult.getCredential()).isEqualTo(networkDeviceDTO.getCredential());
        assertThat(networkDeviceDTOResult.getSshPort()).isEqualTo(networkDeviceDTO.getSshPort());
        assertThat(networkDeviceDTOResult.getResyncStatus()).isEqualTo(networkDeviceDTO.getResyncStatus());
        assertThat(networkDeviceDTOResult.getLabel()).isEqualTo(networkDeviceDTO.getLabel());
    }


}