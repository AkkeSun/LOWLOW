package church.lowlow.rest_api.common.fileProcess.service.basic;

import church.lowlow.rest_api.common.entity.FileDto;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Map;

public interface FileService {

    Map<String, FileDto> fileUpload(MultipartHttpServletRequest mRequest, String folder);
    void deleteFile(String uploadFileName, String folder);
}