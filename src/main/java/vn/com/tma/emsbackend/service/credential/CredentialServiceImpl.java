package vn.com.tma.emsbackend.service.credential;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.com.tma.emsbackend.common.Constant;
import vn.com.tma.emsbackend.common.Mapper;
import vn.com.tma.emsbackend.dto.CredentialDto;
import vn.com.tma.emsbackend.dto.CredentialRequestDto;
import vn.com.tma.emsbackend.entity.Credential;
import vn.com.tma.emsbackend.exception.ResourceConstraintViolationException;
import vn.com.tma.emsbackend.exception.ResourceNotFoundException;
import vn.com.tma.emsbackend.repository.CredentialRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CredentialServiceImpl implements CredentialService {
    private final CredentialRepository credentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final Mapper mapper;

    private static final Logger logger = LoggerFactory.getLogger(CredentialServiceImpl.class);

    @Override
    public List<CredentialDto> getAll() {
        logger.info("Get all credential");
        
        List<Credential> credentials = credentialRepository.findAll();
        return mapper.mapList(credentials, CredentialDto.class);
    }

    @Override
    public CredentialDto get(long id) {
        logger.info("Get credential with id: {} ", id);

        Optional<Credential> credentialOptional = credentialRepository.findById(id);
        if (credentialOptional.isEmpty()) {
            logger.info(Constant.CREDENTIAL_NOT_FOUND + id);
            throw new ResourceNotFoundException(Constant.CREDENTIAL_NOT_FOUND + id);
        }
        return mapper.map(credentialOptional.get(), CredentialDto.class);
    }

    @Override
    public CredentialDto add(CredentialRequestDto credentialRequestDto) {
        logger.info("Add new credential");

        String encodedPassword = passwordEncoder.encode(credentialRequestDto.getPassword());
        Credential credential = mapper.map(credentialRequestDto, Credential.class);
        credential.setPassword(encodedPassword);
        try {
            credential = credentialRepository.save(credential);
        } catch (DataIntegrityViolationException e) {
            logger.error("Can not add new credential!", e);
            throw new ResourceConstraintViolationException(Constant.CONSTRAINT_VIOLATED + Credential.class.getName());
        }
        return mapper.map(credential, CredentialDto.class);
    }

    @Override
    public CredentialDto update(long id, CredentialRequestDto credentialRequestDto) {
        logger.info("Update credential with id:{}", id);

        boolean isCredentialExisted = credentialRepository.existsById(id);
        if (!isCredentialExisted) {
            logger.info(Constant.CREDENTIAL_NOT_FOUND + id);
            throw new ResourceNotFoundException(Constant.CREDENTIAL_NOT_FOUND + id);
        }
        String encodedPassword = passwordEncoder.encode(credentialRequestDto.getPassword());
        Credential credential = mapper.map(credentialRequestDto, Credential.class);
        credential.setId(id);
        credential.setPassword(encodedPassword);
        try {
            credential = credentialRepository.save(credential);
        } catch (DataIntegrityViolationException e) {
            logger.warn("Can not update credential with id:" + id, e);
            throw new ResourceConstraintViolationException(Constant.CONSTRAINT_VIOLATED + Credential.class.getName());
        }
        return mapper.map(credential, CredentialDto.class);
    }

    @Override
    public void delete(long id) {
        logger.info("Delete credential with id: " + id);

        try {
            credentialRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            logger.info("Can not delete credential with id: " + id, e.getMessage());
            throw new ResourceNotFoundException(Constant.CREDENTIAL_NOT_FOUND + id);
        }
    }
}
