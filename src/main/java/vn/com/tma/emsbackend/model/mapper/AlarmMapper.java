package vn.com.tma.emsbackend.model.mapper;

import org.mapstruct.Mapper;
import vn.com.tma.emsbackend.model.dto.AlarmDTO;
import vn.com.tma.emsbackend.model.entity.Alarm;

@Mapper(componentModel = "spring")
public interface AlarmMapper extends  IMapper<Alarm, AlarmDTO> {
    Alarm dtoToEntity(AlarmDTO alarmDTO);

    AlarmDTO entityToDTO(Alarm alarm);
}
