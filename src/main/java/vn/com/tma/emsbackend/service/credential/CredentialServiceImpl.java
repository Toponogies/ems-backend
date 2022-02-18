package vn.com.tma.emsbackend.service.credential;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.com.tma.emsbackend.common.Mapper;
import vn.com.tma.emsbackend.dto.CredentialDto;
import vn.com.tma.emsbackend.dto.CredentialRequestDto;
import vn.com.tma.emsbackend.entity.Credential;
import vn.com.tma.emsbackend.exception.ResourceNotFoundException;
import vn.com.tma.emsbackend.repository.CredentialRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CredentialServiceImpl implements CredentialService {
    private final CredentialRepository credentialRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CredentialServiceImpl(CredentialRepository credentialRepository, PasswordEncoder passwordEncoder) {
        this.credentialRepository = credentialRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<CredentialDto> getAll() {
        List<Credential> credentials = credentialRepository.findAll();
        return Mapper.mapList(credentials, CredentialDto.class);
    }

    @Override
    public CredentialDto get(long id) {
        Optional<Credential> credentialOptional = credentialRepository.findById(id);
        if (credentialOptional.isEmpty()) throw new ResourceNotFoundException("Credential not found on :: " + id);
        return Mapper.map(credentialOptional.get(), CredentialDto.class);
    }

    @Override
    public CredentialDto add(CredentialRequestDto credentialRequestDto) {
        String encodedPassword = passwordEncoder.encode(credentialRequestDto.getPassword());
        credentialRequestDto.setPassword(encodedPassword);
        Credential credential = Mapper.map(credentialRequestDto, Credential.class);
        credential = credentialRepository.save(credential);
        return Mapper.map(credential, CredentialDto.class);
    }

    @Override
    public CredentialDto update(long id, CredentialRequestDto credentialRequestDto) {
        Optional<Credential> credentialOptional = credentialRepository.findById(id);
        if (credentialOptional.isEmpty()) throw new ResourceNotFoundException("Credential not found on :: " + id);

        String encodedPassword = passwordEncoder.encode(credentialRequestDto.getPassword());
        credentialRequestDto.setPassword(encodedPassword);
        Credential credential = Mapper.map(credentialRequestDto, Credential.class);
        credential.setId(id);
        credential = credentialRepository.save(credential);
        return Mapper.map(credential, CredentialDto.class);
    }

    @Override
    public void delete(long id) {
        try {
            credentialRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Credential not found on :: " + id);
        }
    }
}
