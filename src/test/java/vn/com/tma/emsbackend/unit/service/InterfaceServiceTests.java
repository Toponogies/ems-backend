package vn.com.tma.emsbackend.unit.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.com.tma.emsbackend.common.comparator.InterfaceComparator;
import vn.com.tma.emsbackend.common.enums.Enum;
import vn.com.tma.emsbackend.model.dto.InterfaceDTO;
import vn.com.tma.emsbackend.model.entity.Credential;
import vn.com.tma.emsbackend.model.entity.Interface;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.model.entity.Port;
import vn.com.tma.emsbackend.model.mapper.InterfaceMapper;
import vn.com.tma.emsbackend.repository.InterfaceRepository;
import vn.com.tma.emsbackend.service.device.NetworkDeviceService;
import vn.com.tma.emsbackend.service.deviceinterface.InterfaceService;
import vn.com.tma.emsbackend.service.deviceinterface.InterfaceServiceImpl;
import vn.com.tma.emsbackend.service.port.PortService;
import vn.com.tma.emsbackend.service.ssh.InterfaceSSHService;
import vn.com.tma.emsbackend.util.entity.creator.InterfaceCreator;
import vn.com.tma.emsbackend.util.entity.creator.NetworkDeviceCreator;
import vn.com.tma.emsbackend.util.entity.creator.PortCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InterfaceServiceTests {
    @Mock
    private InterfaceRepository interfaceRepository;
    @Mock
    private InterfaceMapper interfaceMapper;
    @Mock
    private InterfaceSSHService interfaceSSHService;
    @Mock
    private NetworkDeviceService networkDeviceService;

    @Mock
    private InterfaceService interfaceService;

    @Mock
    private PortService portService;

    private Credential genericCredential;

    private NetworkDevice genericNetworkDevice;

    private Port genericPort;

    private Interface genericInterface;

    private InterfaceDTO genericInterfaceDTO;

    private List<Interface> mockInterfacesInDB;

    @BeforeEach
    void setUp() {
        interfaceService = new InterfaceServiceImpl(interfaceRepository, interfaceMapper, networkDeviceService, portService, interfaceSSHService);

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

        genericInterface = new Interface();
        genericInterface.setId(1L);
        genericInterface.setName("Management");
        genericInterface.setState(Enum.State.ENABLED);
        genericInterface.setDhcp(Enum.State.ENABLED);
        genericInterface.setIpAddress("10.0.1.1");
        genericInterface.setNetmask("255.255.0.0");
        genericInterface.setGateway("10.0.0.1");
        genericInterface.setPort(genericPort);
        genericInterface.setNetworkDevice(genericNetworkDevice);

        genericInterfaceDTO = InterfaceCreator.createDtoBy(genericInterface);

        mockInterfacesInDB = new ArrayList<>();
        mockInterfacesInDB.add(genericInterface);
    }

    @Test
    void shouldGetAllInterfaceWhenGetAll() {
        //given
        when(interfaceRepository.findAll()).thenReturn(List.of(genericInterface));
        when(interfaceMapper.entitiesToDTOs(anyList())).thenReturn(List.of(genericInterfaceDTO));

        //when
        List<InterfaceDTO> interfaceDTOListResult = interfaceService.getAll();

        //then
        verify(interfaceRepository).findAll();

        assertInterfaceDTOsIsEquals(interfaceDTOListResult.get(0), genericInterfaceDTO);
    }

    @Test
    void shouldGetSpecificInterfaceWhenGetByPortId() {
        //given
        when(interfaceRepository.findByPort_Id(genericInterface.getPort().getId())).thenReturn(genericInterface);
        when(portService.existsById(genericInterface.getPort().getId())).thenReturn(true);
        when(interfaceMapper.entitiesToDTOs(anyList())).thenReturn(List.of(genericInterfaceDTO));

        //when
        InterfaceDTO interfaceDTOResult = interfaceService.getByPort(genericInterface.getPort().getId());

        //then
        verify(interfaceRepository).findByPort_Id(genericInterface.getPort().getId());
        verify(portService).existsById(genericInterface.getPort().getId());

        assertInterfaceDTOsIsEquals(interfaceDTOResult, genericInterfaceDTO);
    }


    @Test
    void shouldSpecificInterfaceWhenGetInterfaceById() {
        //given
        when(interfaceRepository.findById(anyLong())).thenReturn(Optional.of(genericInterface));
        when(interfaceMapper.entityToDTO(any(Interface.class))).thenReturn(genericInterfaceDTO);

        //when
        InterfaceDTO interfaceDTOResult = interfaceService.get(1L);

        //then
        assertInterfaceDTOsIsEquals(interfaceDTOResult, genericInterfaceDTO);
    }

    @Test
    void shouldReturnInterfaceDTOWhenAddNewInterface() {
        //given
        when(interfaceRepository.existsByName(genericInterface.getName())).thenReturn(false);
        when(networkDeviceService.getByLabel(genericInterface.getNetworkDevice().getLabel())).thenReturn(NetworkDeviceCreator.createDTOBy(genericNetworkDevice));
        when(portService.getByNameAndNetworkDevice(genericInterface.getPort().getName(), genericPort.getNetworkDevice().getLabel())).thenReturn(PortCreator.createDtoBy(genericPort));
        when(interfaceMapper.dtoToEntity(any(InterfaceDTO.class))).thenReturn(genericInterface);
        when(interfaceRepository.save(any(Interface.class))).thenReturn(genericInterface);
        when(interfaceMapper.entityToDTO(any(Interface.class))).thenReturn(genericInterfaceDTO);
        doNothing().when(interfaceSSHService).add(any(Interface.class));

        //when
        InterfaceDTO interfaceDTOResult = interfaceService.add(genericInterfaceDTO);


        verify(interfaceRepository).save(any(Interface.class));
        verify(interfaceSSHService).add(any(Interface.class));
        assertInterfaceDTOsIsEquals(interfaceDTOResult, genericInterfaceDTO);
    }


    @Test
    void shouldReturnInterfaceDTOWhenUpdateInterface() {
        //given
        when(networkDeviceService.getByLabel(genericInterface.getNetworkDevice().getLabel())).thenReturn(NetworkDeviceCreator.createDTOBy(genericNetworkDevice));
        when(portService.getByNameAndNetworkDevice(genericInterface.getPort().getName(), genericPort.getNetworkDevice().getLabel())).thenReturn(PortCreator.createDtoBy(genericPort));
        when(interfaceMapper.dtoToEntity(any(InterfaceDTO.class))).thenReturn(genericInterface);
        when(interfaceRepository.findById(anyLong())).thenReturn(Optional.of(genericInterface));
        when(interfaceRepository.findByName(anyString())).thenReturn(null);

        when(interfaceRepository.save(any(Interface.class))).thenReturn(genericInterface);
        when(interfaceMapper.entityToDTO(any(Interface.class))).thenReturn(genericInterfaceDTO);
        doNothing().when(interfaceSSHService).edit(anyString(), any(Interface.class));

        //when
        InterfaceDTO interfaceDTOResult = interfaceService.update(genericInterfaceDTO.getId(), genericInterfaceDTO);

        verify(interfaceRepository).save(any(Interface.class));
        verify(interfaceSSHService).edit(anyString(), any(Interface.class));

        assertInterfaceDTOsIsEquals(interfaceDTOResult, genericInterfaceDTO);
    }

    @Test
    void shouldCallDeleteOnDatabaseAndDeviceWhenDelete(){
        when(interfaceRepository.findById(anyLong())).thenReturn(Optional.of(genericInterface));

       interfaceService.delete(genericInterfaceDTO.getId());

        verify(interfaceRepository).deleteById(anyLong());
        verify(interfaceSSHService).delete(any(Interface.class));
    }

    @Test
    void shouldReturnAllInterfaceOfDeviceOfDeviceWhenGetInterfaceByNetworkDeviceId(){
        when(networkDeviceService.existByLabel(anyString())).thenReturn(true);
        when(interfaceRepository.findByNetworkDeviceLabel(anyString())).thenReturn(List.of(genericInterface));
        when(interfaceMapper.entitiesToDTOs(anyList())).thenReturn(List.of(genericInterfaceDTO));

        List<InterfaceDTO> interfaceDTOListResult = interfaceService.getByNetworkDeviceLabel(genericInterface.getNetworkDevice().getLabel());

        verify(interfaceRepository).findByNetworkDeviceLabel(anyString());
        verify(networkDeviceService).existByLabel(anyString());

        assertInterfaceDTOsIsEquals(interfaceDTOListResult.get(0), genericInterfaceDTO);
    }

    @Test
    void shouldResyncWithDB(){
        //given
        Port newGenericPort = new Port();
        newGenericPort.setId(2L);
        newGenericPort.setName("PORT-2");
        newGenericPort.setConnector("PORT-2");
        newGenericPort.setMacAddress("fe80::a08f:a8ff:fed5:aa2");
        newGenericPort.setNetworkDevice(genericNetworkDevice);
        newGenericPort.setState(Enum.State.ENABLED);

        //init data in device and database, make it different
        Interface newGenericInterface = new Interface();
        newGenericInterface.setId(2L);
        newGenericInterface.setName("Auto");
        newGenericInterface.setState(Enum.State.ENABLED);
        newGenericInterface.setDhcp(Enum.State.ENABLED);
        newGenericInterface.setIpAddress("10.0.1.1");
        newGenericInterface.setNetmask("255.255.0.0");
        newGenericInterface.setGateway("10.0.0.1");
        newGenericInterface.setPort(newGenericPort);
        newGenericInterface.setNetworkDevice(genericNetworkDevice);
        mockInterfacesInDB.add(newGenericInterface);

        List<Interface> mockInterfacesInDevice = new ArrayList<>();
        mockInterfacesInDevice.add(genericInterface);
        Interface newChangeGenericInterface = new Interface();
        newChangeGenericInterface.setId(2L);
        newChangeGenericInterface.setName("Auto_2");
        newChangeGenericInterface.setState(Enum.State.ENABLED);
        newChangeGenericInterface.setDhcp(Enum.State.ENABLED);
        newChangeGenericInterface.setIpAddress("10.0.1.1");
        newChangeGenericInterface.setNetmask("255.255.0.0");
        newChangeGenericInterface.setGateway("10.0.0.1");
        newChangeGenericInterface.setPort(newGenericPort);
        newChangeGenericInterface.setNetworkDevice(genericNetworkDevice);
        mockInterfacesInDevice.add(newChangeGenericInterface);

        //assert that data in device and database is not the same
        InterfaceComparator interfaceComparator = new InterfaceComparator();
        mockInterfacesInDevice.sort(interfaceComparator);
        mockInterfacesInDB.sort(interfaceComparator);
        Assertions.assertThat(mockInterfacesInDB).isNotEqualTo(mockInterfacesInDevice);

        when(interfaceSSHService.getAllInterface(anyLong(), anyList())).thenReturn(new ArrayList<>(mockInterfacesInDevice));
        when(interfaceRepository.findByNetworkDeviceId(anyLong())).thenReturn(new ArrayList<>(mockInterfacesInDB));

        doAnswer(invocationOnMock -> {
            mockInterfacesInDB.remove(newGenericInterface);
            return null;
        }).when(interfaceRepository).delete(newGenericInterface);

        doAnswer(invocationOnMock -> {
            Interface interfaceSave =  invocationOnMock.getArgument(0);
            mockInterfacesInDB.add(interfaceSave);
            return null;
        }).when(interfaceRepository).save(any(Interface.class));

        interfaceService.resyncInterfaceByDeviceId(1L);

        //assert that data in device and database is the same
        mockInterfacesInDB.sort(interfaceComparator);
        mockInterfacesInDevice.sort(interfaceComparator);
        Assertions.assertThat(mockInterfacesInDB.equals(mockInterfacesInDevice)).isTrue();
    }


    void assertInterfaceDTOsIsEquals(InterfaceDTO thisInterfaceDTO, InterfaceDTO thatInterfaceDTO) {
        assertThat(thisInterfaceDTO.getId()).isEqualTo(thatInterfaceDTO.getId());
        assertThat(thisInterfaceDTO.getName()).isEqualTo(thatInterfaceDTO.getName());
        assertThat(thisInterfaceDTO.getGateway()).isEqualTo(thatInterfaceDTO.getGateway());
        assertThat(thisInterfaceDTO.getIpAddress()).isEqualTo(thatInterfaceDTO.getIpAddress());
        assertThat(thisInterfaceDTO.getNetworkDevice()).isEqualTo(thatInterfaceDTO.getNetworkDevice());
        assertThat(thisInterfaceDTO.getDhcp()).isEqualTo(thatInterfaceDTO.getDhcp());
        assertThat(thisInterfaceDTO.getPort()).isEqualTo(thatInterfaceDTO.getPort());
        assertThat(thisInterfaceDTO.getState()).isEqualTo(thatInterfaceDTO.getState());
    }
}
