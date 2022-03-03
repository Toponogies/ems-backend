package vn.com.tma.emsbackend.service.credential;

import lombok.RequiredArgsConstructor;
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

    @Override
    public List<CredentialDto> getAll() {
        List<Credential> credentials = credentialRepository.findAll();
        return mapper.mapList(credentials, CredentialDto.class);
    }

    @Override
    public CredentialDto get(long id) {
        Optional<Credential> credentialOptional = credentialRepository.findById(id);
        if (credentialOptional.isEmpty()) throw new ResourceNotFoundException(Constant.CREDENTIAL_NOT_FOUND + id);
        return mapper.map(credentialOptional.get(), CredentialDto.class);
    }

    @Override
    public CredentialDto add(CredentialRequestDto credentialRequestDto) {
        String encodedPassword = passwordEncoder.encode(credentialRequestDto.getPassword());
        Credential credential = mapper.map(credentialRequestDto, Credential.class);
        credential.setPassword(encodedPassword);
        try {
            credential = credentialRepository.save(credential);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceConstraintViolationException(Constant.CONSTRAINT_VIOLATED + Credential.class.getName());
        }
        return mapper.map(credential, CredentialDto.class);
    }

    @Override
    public CredentialDto update(long id, CredentialRequestDto credentialRequestDto) {
        boolean isCredentialExisted = credentialRepository.existsById(id);
        if (!isCredentialExisted) throw new ResourceNotFoundException(Constant.CREDENTIAL_NOT_FOUND + id);

        String encodedPassword = passwordEncoder.encode(credentialRequestDto.getPassword());
        Credential credential = mapper.map(credentialRequestDto, Credential.class);
        credential.setId(id);
        credential.setPassword(encodedPassword);
        try {
            credential = credentialRepository.save(credential);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceConstraintViolationException(Constant.CONSTRAINT_VIOLATED + Credential.class.getName());
        }
        return mapper.map(credential, CredentialDto.class);
    }

    @Override
    public void delete(long id) {
        try {
            credentialRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(Constant.CREDENTIAL_NOT_FOUND + id);
        }
    }
}
