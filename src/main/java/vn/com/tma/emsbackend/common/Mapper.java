package vn.com.tma.emsbackend.common;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class Mapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    private Mapper() {
    }

    public static <S, D> D map(S source, Class<D> destination) {
        return modelMapper.map(source, destination);
    }


    public static <S, D> List<D> mapList(List<S> sourceList, Class<D> destination) {
        return sourceList.stream()
                .map(source -> map(source, destination))
                .collect(Collectors.toList());
    }
}