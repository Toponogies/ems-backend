package vn.com.tma.emsbackend.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Mock
    private NetworkDeviceService networkDeviceService;
    private Credential genericCredential;

    private NetworkDevice genericNetworkDevice;

    private Port genericPort;

    private Interface genericInterface;

    private InterfaceDTO genericInterfacesDTO;

    private List<Port> mockInterfacesInDB;

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

        genericInterfacesDTO = InterfaceCreator.createDtoBy(genericInterface);

        mockInterfacesInDB = new ArrayList<>();
        mockInterfacesInDB.add(genericPort);
    }

    @Test
    void shouldGetAllInterfaceWhenGetAll() {
        //given
        when(interfaceRepository.findAll()).thenReturn(List.of(genericInterface));
        when(interfaceMapper.entitiesToDTOs(anyList())).thenReturn(List.of(genericInterfacesDTO));

        //when
        List<InterfaceDTO> interfaceDTOListResult = interfaceService.getAll();

        //then
        verify(interfaceRepository).findAll();

        assertInterfaceDTOsIsEquals(interfaceDTOListResult.get(0), genericInterfacesDTO);
    }

    @Test
    void shouldGetSpecificInterfaceWhenGetByPortId() {
        //given
        when(interfaceRepository.findByPort_Id(genericInterface.getPort().getId())).thenReturn(genericInterface);
        when(portService.existsById(genericInterface.getPort().getId())).thenReturn(true);
        when(interfaceMapper.entitiesToDTOs(anyList())).thenReturn(List.of(genericInterfacesDTO));

        //when
        InterfaceDTO interfaceDTOResult = interfaceService.getByPort(genericInterface.getPort().getId());

        //then
        verify(interfaceRepository).findByPort_Id(genericInterface.getPort().getId());
        verify(portService).existsById(genericInterface.getPort().getId());

        assertInterfaceDTOsIsEquals(interfaceDTOResult, genericInterfacesDTO);
    }


    @Test
    void shouldSpecificInterfaceWhenGetInterfaceById() {
        //given
        when(interfaceRepository.findById(anyLong())).thenReturn(Optional.of(genericInterface));
        when(interfaceMapper.entityToDTO(any(Interface.class))).thenReturn(genericInterfacesDTO);

        //when
        InterfaceDTO interfaceDTOResult = interfaceService.get(1L);

        //then

        assertInterfaceDTOsIsEquals(interfaceDTOResult, genericInterfacesDTO);
    }

    void assertInterfaceDTOsIsEquals(InterfaceDTO thisInterfaceDTO, InterfaceDTO thatInterfaceDTO){
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
