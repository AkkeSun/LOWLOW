package church.lowlow.user_api.admin.file.service;

import church.lowlow.rest_api.common.entity.FileDto;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    FileDto fileUpload(MultipartFile image);
    void deleteFile(String uploadFileName);
}