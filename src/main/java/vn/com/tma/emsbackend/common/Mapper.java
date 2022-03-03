package vn.com.tma.emsbackend.common;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class Mapper {
    private final ModelMapper modelMapper;

    public Mapper() {
        modelMapper = new ModelMapper();
    }

    public <S, D> D map(S source, Class<D> destination) {
        return modelMapper.map(source, destination);
    }


    public <S, D> List<D> mapList(List<S> sourceList, Class<D> destination) {
        return sourceList.stream()
                .map(source -> map(source, destination))
                .collect(Collectors.toList());
    }
}