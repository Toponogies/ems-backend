package vn.com.tma.emsbackend.service.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tma.emsbackend.model.dto.NetworkDeviceDTO;
import vn.com.tma.emsbackend.model.exception.DeviceNotFoundException;
import vn.com.tma.emsbackend.model.mapper.NetworkDeviceMapper;
import vn.com.tma.emsbackend.service.common.ConfigurationCommonExternalService;
import vn.com.tma.emsbackend.service.device.NetworkDeviceService;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConfigurationServiceImpl implements ConfigurationService {
    private final NetworkDeviceService networkDeviceService;

    private final ConfigurationCommonExternalService configurationCommonExternalService;

    private final NetworkDeviceMapper networkDeviceMapper;


    @Override
    @Transactional
    public byte[] downloadDeviceConfigFileById(List<Long> ids) throws DeviceNotFoundException {
        try {
            // Creating byteArray stream, make it bufferable and passing this buffer to ZipOutputStream
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
            ZipOutputStream zipOutputStream = new ZipOutputStream(bufferedOutputStream);
            for (Long id : ids) {
                NetworkDeviceDTO networkDeviceDTO = networkDeviceService.get(id);

                String result = configurationCommonExternalService.exportDeviceConfig(networkDeviceMapper.dtoToEntity(networkDeviceDTO));
                // New zip entry and copying InputStream with file to ZipOutputStream, after all closing streams
                zipOutputStream.putNextEntry(new ZipEntry("random"));
                FileInputStream fileInputStream = new FileInputStream(result);

                IOUtils.copy(fileInputStream, zipOutputStream);

                fileInputStream.close();
                zipOutputStream.closeEntry();

                if (zipOutputStream != null) {
                    zipOutputStream.finish();
                    zipOutputStream.flush();
                    IOUtils.closeQuietly(zipOutputStream);
                }
                IOUtils.closeQuietly(bufferedOutputStream);
                IOUtils.closeQuietly(byteArrayOutputStream);

            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
}
