package vn.com.tma.emsbackend.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.com.tma.emsbackend.common.comparator.PortComparator;
import vn.com.tma.emsbackend.common.enums.Enum;
import vn.com.tma.emsbackend.model.dto.PortDTO;
import vn.com.tma.emsbackend.model.entity.Credential;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.model.entity.Port;
import vn.com.tma.emsbackend.model.mapper.PortMapper;
import vn.com.tma.emsbackend.repository.InterfaceRepository;
import vn.com.tma.emsbackend.repository.PortRepository;
import vn.com.tma.emsbackend.service.device.NetworkDeviceService;
import vn.com.tma.emsbackend.service.port.PortService;
import vn.com.tma.emsbackend.service.port.PortServiceImpl;
import vn.com.tma.emsbackend.service.ssh.PortSSHService;
import vn.com.tma.emsbackend.util.entity.creator.PortCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PortServiceTests {
    @Mock
    private PortRepository portRepository;

    @Mock
    private InterfaceRepository interfaceRepository;

    @Mock
    private PortMapper portMapper;

    @Mock
    private PortSSHService portSSHService;

    @Mock
    private NetworkDeviceService networkDeviceService;

    private PortService portService;

    private Credential genericCredential;

    private NetworkDevice genericNetworkDevice;

    private Port genericPort;

    private PortDTO genericPortDTO;

    private List<Port> mockPortsInDB;


    @BeforeEach
    void setUp() {
        portService = new PortServiceImpl(portRepository, interfaceRepository, portMapper, networkDeviceService, portSSHService);

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
        genericNetworkDevice.setResyncStatus(Enum.ResyncStatus.ONGOING);
        genericNetworkDevice.setCredential(genericCredential);

        genericPort = new Port();
        genericPort.setId(1L);
        genericPort.setName("PORT-1");
        genericPort.setConnector("PORT-1");
        genericPort.setMacAddress("fe80::a08f:a8ff:fed5:aa1");
        genericPort.setNetworkDevice(genericNetworkDevice);
        genericPort.setState(Enum.State.ENABLED);

        genericPortDTO = PortCreator.createDtoBy(genericPort);

        mockPortsInDB = new ArrayList<>();
        mockPortsInDB.add(genericPort);
    }

    @Test
    void shouldGetAllPortWhenGetAll() {
        //given
        when(portRepository.findAll()).thenReturn(List.of(genericPort));
        when(portMapper.entitiesToDTOs(anyList())).thenReturn(List.of(genericPortDTO));

        //when
        List<PortDTO> portDTOListResult = portService.getAll();

        //then
        verify(portRepository).findAll();

        assertPortDTOsIsEquals(portDTOListResult.get(0), genericPortDTO);
    }


    @Test
    void shouldGetSpecificPortWhenGetById() {
        //given
        when(portRepository.findById(anyLong())).thenReturn(Optional.ofNullable(genericPort));
        when(portMapper.entityToDTO(any(Port.class))).thenReturn(genericPortDTO);

        //when
        PortDTO portDTOResult = portService.get(genericPort.getId());

        //then
        verify(portRepository).findById(anyLong());
        assertPortDTOsIsEquals(portDTOResult, genericPortDTO);
    }

    @Test
    void shouldGetSpecificPortWhenGetByNameAndDeviceId() {
        //given
        when(portRepository.findByNameAndNetworkDevice_Label(genericPort.getName(), genericPort.getNetworkDevice().getLabel())).thenReturn(genericPort);
        when(portMapper.entityToDTO(any(Port.class))).thenReturn(genericPortDTO);

        //when
        PortDTO portDTOResult = portService.getByNameAndNetworkDevice(genericPort.getName(), genericPort.getNetworkDevice().getLabel());

        //then
        verify(portRepository).findByNameAndNetworkDevice_Label(genericPort.getName(), genericPort.getNetworkDevice().getLabel());
        assertPortDTOsIsEquals(portDTOResult, genericPortDTO);
    }

    @Test
    void shouldGetAllPortWhenGetByDeviceId() {
        //given
        when(networkDeviceService.existsById(genericPort.getNetworkDevice().getId())).thenReturn(true);
        when(portRepository.findByNetworkDeviceId(genericPort.getNetworkDevice().getId())).thenReturn(List.of(genericPort));
        when(portMapper.entitiesToDTOs(anyList())).thenReturn(List.of(genericPortDTO));

        //when
        List<PortDTO> portDTOsResult = portService.getByNetworkDevice(genericPort.getNetworkDevice().getId());

        //then
        verify(portRepository).findByNetworkDeviceId(genericPort.getNetworkDevice().getId());
        assertPortDTOsIsEquals(portDTOsResult.get(0), genericPortDTO);
    }

    @Test
    void shouldGetAllPortWhenGetByDeviceLabel() {
        //given
        when(networkDeviceService.existByLabel(genericPort.getNetworkDevice().getLabel())).thenReturn(true);
        when(portRepository.findByNetworkDeviceLabel(genericPort.getNetworkDevice().getLabel())).thenReturn(List.of(genericPort));
        when(portMapper.entitiesToDTOs(anyList())).thenReturn(List.of(genericPortDTO));

        //when
        List<PortDTO> portDTOsResult = portService.getByNetworkDeviceLabel(genericPort.getNetworkDevice().getLabel());

        //then
        verify(portRepository).findByNetworkDeviceLabel(genericPort.getNetworkDevice().getLabel());
        assertPortDTOsIsEquals(portDTOsResult.get(0), genericPortDTO);
    }

    @Test
    void shouldResyncWithDB(){
        //given
        //init data in device and database, make it different
        Port newGenericPort = new Port();
        newGenericPort.setId(2L);
        newGenericPort.setName("PORT-1");
        newGenericPort.setConnector("PORT-1");
        newGenericPort.setMacAddress("fe80::a08f:a8ff:fed5:aa1");
        newGenericPort.setNetworkDevice(genericNetworkDevice);
        newGenericPort.setState(Enum.State.ENABLED);
        mockPortsInDB.add(newGenericPort);

        List<Port> mockPortsInDevice = new ArrayList<>();
        mockPortsInDevice.add(genericPort);
        Port newChangeGenericPort = new Port();
        newChangeGenericPort.setId(2L);
        newChangeGenericPort.setName("PORT-2");
        newChangeGenericPort.setConnector("PORT-2");
        newChangeGenericPort.setMacAddress("fe80::a08f:a8ff:fed5:aa1");
        newChangeGenericPort.setNetworkDevice(genericNetworkDevice);
        newChangeGenericPort.setState(Enum.State.DISABLED);
        mockPortsInDevice.add(newChangeGenericPort);

        //assert that data in device and database is the not the same
        PortComparator portComparator = new PortComparator();
        mockPortsInDB.sort(portComparator);
        mockPortsInDevice.sort(portComparator);
        assertThat(mockPortsInDB.equals(mockPortsInDevice)).isFalse();

        when(portSSHService.getAllPort(anyLong())).thenReturn(new ArrayList<>(mockPortsInDevice));
        when(portRepository.findByNetworkDeviceId(anyLong())).thenReturn(new ArrayList<>(mockPortsInDB));

        doAnswer(invocationOnMock -> {
            mockPortsInDB.remove(newGenericPort);
            return null;
        }).when(portRepository).delete(newGenericPort);

        doNothing().when(interfaceRepository).deleteByPortId(anyLong());
        doAnswer(invocationOnMock -> {
            Port portSave =  invocationOnMock.getArgument(0);
            mockPortsInDB.add(portSave);
            return null;
        }).when(portRepository).save(any(Port.class));

        portService.resyncPortByDeviceId(1L);

        //assert that data in device and database is the same
        mockPortsInDB.sort(portComparator);
        mockPortsInDevice.sort(portComparator);
        assertThat(mockPortsInDB.equals(mockPortsInDevice)).isTrue();
    }

    void assertPortDTOsIsEquals(PortDTO portDTOResult, PortDTO portDTO) {
        assertThat(portDTOResult.getId()).isEqualTo(portDTO.getId());
        assertThat(portDTOResult.getName()).isEqualTo(portDTO.getName());
        assertThat(portDTOResult.getConnector()).isEqualTo(portDTO.getConnector());
        assertThat(portDTOResult.getMacAddress()).isEqualTo(portDTO.getMacAddress());
        assertThat(portDTOResult.getState()).isEqualTo(portDTO.getState());
        assertThat(portDTOResult.getNetworkDevice()).isEqualTo(portDTO.getNetworkDevice());
    }
}
