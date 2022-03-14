package vn.com.tma.emsbackend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import vn.com.tma.emsbackend.dto.ManagedDeviceDto;
import vn.com.tma.emsbackend.dto.ManagedDeviceRequestDto;
import vn.com.tma.emsbackend.service.manageddevice.ManagedDeviceService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/devices")
public class ManagedDeviceController {
    private final ManagedDeviceService managedDeviceService;

    @Operation(summary = "Get all managed devices")
    @GetMapping()
    public ResponseEntity<List<ManagedDeviceDto>> getAllManagedDevices() {
        return ResponseEntity.ok(managedDeviceService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManagedDeviceDto> getDeviceById(@PathVariable(value = "id") Long manageDeviceId) {
        return new ResponseEntity<ManagedDeviceDto>(managedDeviceService.get(manageDeviceId), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<ManagedDeviceDto> addDevice(@RequestBody ManagedDeviceRequestDto deviceRequestDto) {
        return new ResponseEntity<ManagedDeviceDto>(managedDeviceService.add(deviceRequestDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ManagedDeviceDto> deleteDevice(@PathVariable(value = "id") Long deviceId) {
        managedDeviceService.delete(deviceId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ManagedDeviceDto> updateDevice(@PathVariable(value = "id") Long deviceId, @RequestBody
            ManagedDeviceRequestDto deviceRequestDto) {
        return new ResponseEntity<>(managedDeviceService.update(deviceId, deviceRequestDto), HttpStatus.OK);
    }
}
