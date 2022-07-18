package vn.com.tma.emsbackend.model.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public interface IMapper<E, D> {
    E dtoToEntity(D dto);

    D entityToDTO(E entity);

    default List<E> dtosToEntities(List<D> dtos) {
        if (dtos == null) return Collections.emptyList();
        return dtos.stream().map(this::dtoToEntity).collect(Collectors.toList());
    }

    default List<D> entitiesToDTOs(List<E> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream().map(this::entityToDTO).collect(Collectors.toList());
    }
}
