package vn.com.tma.emsbackend.service.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StreamUtils;
import vn.com.tma.emsbackend.model.dto.NetworkDeviceDTO;
import vn.com.tma.emsbackend.model.mapper.NetworkDeviceMapper;
import vn.com.tma.emsbackend.service.common.ConfigurationCommonExternalService;
import vn.com.tma.emsbackend.service.device.NetworkDeviceService;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.UUID;
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
    public void downloadDeviceConfigFileById(List<Long> ids, HttpServletResponse response) {
        File mainFolder = new File("configTemp/");
        mainFolder.mkdirs();
        try {
            // Creating byteArray stream, make it bufferable and passing this buffer to ZipOutputStream
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
            ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
            String postFix = UUID.randomUUID().toString();
            for (Long id : ids) {
                try {
                    NetworkDeviceDTO networkDeviceDTO = networkDeviceService.get(id);
                    String result = configurationCommonExternalService.exportDeviceConfig(networkDeviceMapper.dtoToEntity(networkDeviceDTO));

                    File file = createFile(result.getBytes(), postFix, id);
                    FileSystemResource resource = new FileSystemResource(file);

                    ZipEntry ze = new ZipEntry(file.getPath());
                    zipOutputStream.putNextEntry(ze);

                    StreamUtils.copy(resource.getInputStream(), zipOutputStream);

                    // New zip entry and copying InputStream with file to ZipOutputStream, after all closing streams
                    zipOutputStream.closeEntry();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            zipOutputStream.finish();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            FileSystemUtils.deleteRecursively(mainFolder);
        }
    }

    private File createFile(byte[] data, String postFix, Long deviceId) throws IOException {
        File file = new File("configTemp" + File.separator + "download_" + postFix + File.separator + deviceId + ".txt");
        file.getParentFile().mkdirs();
        file.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(data);
        fileOutputStream.close();
        return file;
    }
}
