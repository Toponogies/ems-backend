package vn.com.tma.emsbackend.service.credential;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tma.emsbackend.model.dto.CredentialDTO;
import vn.com.tma.emsbackend.model.entity.Credential;
import vn.com.tma.emsbackend.model.exception.CredentialLinkedToDeviceException;
import vn.com.tma.emsbackend.model.exception.CredentialNameExistsException;
import vn.com.tma.emsbackend.model.exception.CredentialNotFoundException;
import vn.com.tma.emsbackend.model.mapper.CredentialMapper;
import vn.com.tma.emsbackend.repository.CredentialRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CredentialServiceImpl implements CredentialService {
    private final CredentialRepository credentialRepository;
    private final CredentialMapper credentialMapper;

    @Override
    public List<CredentialDTO> getAll() {
        log.info("Get all credential");

        List<Credential> credentials = credentialRepository.findAll();

        return credentialMapper.entitiesToDTOs(credentials);
    }

    @Override
    public CredentialDTO get(long id) {
        log.info("Get credential with id: {} ", id);

        Optional<Credential> credentialOptional = credentialRepository.findById(id);
        if (credentialOptional.isEmpty()) {
            throw new CredentialNotFoundException(String.valueOf(id));
        }
        return credentialMapper.entityToDTO(credentialOptional.get());
    }

    @Override
    public CredentialDTO getByName(String name) {
        Credential credential = credentialRepository.findByName(name);
        if (credential == null) {
            throw new CredentialNotFoundException(name);
        }

        return credentialMapper.entityToDTO(credential);
    }


    @Override
    @Transactional
    public CredentialDTO add(CredentialDTO credentialDto) {
        log.info("Add new credential");

        boolean checkIfExistedByName = credentialRepository.existsByName(credentialDto.getName());
        if (checkIfExistedByName) {
            throw new CredentialNameExistsException(credentialDto.getName());
        }

        Credential credential = credentialMapper.dtoToEntity(credentialDto);
        credential = credentialRepository.save(credential);

        return credentialMapper.entityToDTO(credential);
    }

    @Override
    @Transactional
    public CredentialDTO update(long id, CredentialDTO credentialDto) {
        log.info("Update credential with id:{}", id);

        boolean checkIfExistedById = credentialRepository.existsById(id);
        if (!checkIfExistedById) {
            throw new CredentialNotFoundException(String.valueOf(id));
        }

        Credential credentialWithDupName = credentialRepository.findByName(credentialDto.getName());
        if (credentialWithDupName != null && !credentialWithDupName.getId().equals(id)) {
            throw new CredentialNameExistsException(credentialDto.getName());
        }

        Credential credential = credentialMapper.dtoToEntity(credentialDto);
        credential.setId(id);
        credential = credentialRepository.save(credential);

        return credentialMapper.entityToDTO(credential);
    }

    @Override
    @Transactional
    public void delete(long id) {
        log.info("Delete credential with id: " + id);

        Optional<Credential> credentialOptional = credentialRepository.findById(id);
        if (credentialOptional.isEmpty()) {
            throw new CredentialNotFoundException(String.valueOf(id));
        }

        Credential credential = credentialOptional.get();

        if (credential.getDevices() != null && !credential.getDevices().isEmpty()) {
            throw new CredentialLinkedToDeviceException(id);
        }

        credentialRepository.deleteById(id);
    }
}
