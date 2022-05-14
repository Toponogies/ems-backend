package vn.com.tma.emsbackend.service.credential;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import vn.com.tma.emsbackend.common.Constant;
import vn.com.tma.emsbackend.common.Mapper;
import vn.com.tma.emsbackend.dto.CredentialDto;
import vn.com.tma.emsbackend.entity.Credential;
import vn.com.tma.emsbackend.exception.ResourceConstraintViolationException;
import vn.com.tma.emsbackend.exception.ResourceNotFoundException;
import vn.com.tma.emsbackend.repository.CredentialRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CredentialServiceImpl implements CredentialService {
    private final CredentialRepository credentialRepository;
    private final Mapper mapper;

    @Override
    public List<CredentialDto> getAll() {
        log.info("Get all credential");
        
        List<Credential> credentials = credentialRepository.findAll();
        return mapper.mapList(credentials, CredentialDto.class);
    }

    @Override
    public CredentialDto get(long id) {
        log.info("Get credential with id: {} ", id);

        Optional<Credential> credentialOptional = credentialRepository.findById(id);
        if (credentialOptional.isEmpty()) {
            log.info(Constant.CREDENTIAL_NOT_FOUND + id);
            throw new ResourceNotFoundException(Constant.CREDENTIAL_NOT_FOUND + id);
        }
        return mapper.map(credentialOptional.get(), CredentialDto.class);
    }

    @Override
    public CredentialDto add(CredentialDto credentialDto) {
        log.info("Add new credential");

        Credential credential = mapper.map(credentialDto, Credential.class);
        try {
            credential = credentialRepository.save(credential);
        } catch (DataIntegrityViolationException e) {
            log.error("Can not add new credential!", e);
            throw new ResourceConstraintViolationException(Constant.CONSTRAINT_VIOLATED + Credential.class.getName());
        }
        return mapper.map(credential, CredentialDto.class);
    }

    @Override
    public CredentialDto update(long id, CredentialDto credentialDto) {
        log.info("Update credential with id:{}", id);

        boolean isCredentialExisted = credentialRepository.existsById(id);
        if (!isCredentialExisted) {
            log.info(Constant.CREDENTIAL_NOT_FOUND + id);
            throw new ResourceNotFoundException(Constant.CREDENTIAL_NOT_FOUND + id);
        }
        Credential credential = mapper.map(credentialDto, Credential.class);
        credential.setId(id);
        try {
            credential = credentialRepository.save(credential);
        } catch (DataIntegrityViolationException e) {
            log.warn("Can not update credential with id:" + id, e);
            throw new ResourceConstraintViolationException(Constant.CONSTRAINT_VIOLATED + Credential.class.getName());
        }
        return mapper.map(credential, CredentialDto.class);
    }

    @Override
    public void delete(long id) {
        log.info("Delete credential with id: " + id);

        try {
            credentialRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            log.info("Can not delete credential with id: {}" + id, e.getMessage());
            throw new ResourceNotFoundException(Constant.CREDENTIAL_NOT_FOUND + id);
        }
    }
}
