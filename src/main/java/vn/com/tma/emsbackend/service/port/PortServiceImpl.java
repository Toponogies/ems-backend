package vn.com.tma.emsbackend.service.port;

import vn.com.tma.emsbackend.model.dto.PortDTO;

import java.util.List;

public class PortServiceImpl implements PortService {

    @Override
    public List<PortDTO> getAll() {
        return null;
    }

    @Override
    public PortDTO get(long id) {
        return null;
    }

    @Override
    public PortDTO add(PortDTO request) {
        return null;
    }

    @Override
    public PortDTO update(long id, PortDTO request) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(long id) {
        throw new UnsupportedOperationException();
    }
}
