package vn.com.tma.emsbackend.common;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.stereotype.Component;

import vn.com.tma.emsbackend.dto.ManagedDeviceRequestDto;
import vn.com.tma.emsbackend.entity.ManagedDevice;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class Mapper {
    private final ModelMapper modelMapper;

    public Mapper() {
        modelMapper = new ModelMapper();

        //custom mapping
        modelMapper.addMappings(getManagedDeviceRequestDtoMapping());
    }

    public <S, D> D map(S source, Class<D> destination) {
        return modelMapper.map(source, destination);
    }

    public <S, D> List<D> mapList(List<S> sourceList, Class<D> destination) {
        return sourceList.stream()
                .map(source -> map(source, destination))
                .collect(Collectors.toList());
    }

    private PropertyMap<ManagedDeviceRequestDto, ManagedDevice> getManagedDeviceRequestDtoMapping() {
        PropertyMap<ManagedDeviceRequestDto, ManagedDevice> managedDeviceRequestDtoMap = new PropertyMap<ManagedDeviceRequestDto, ManagedDevice>() {
            @Override
            protected void configure() {
                map().getCredential().setId(source.getCredentialId());
                map().setId(null);
            }
        };
        return managedDeviceRequestDtoMap;
    }
}