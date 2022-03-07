package church.lowlow.user_api.admin.file.service;

import church.lowlow.rest_api.common.entity.FileDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.util.Map;
import java.util.UUID;

@Service
@Log4j2
public class DefaultFileService implements FileService{


    @Value("${fileUploadPath}")
    private String fileUploadPath;

    @Override
    public FileDto fileUpload(MultipartFile image) {

        String originalFilename = image.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uploadFileName = UUID.randomUUID().toString().replaceAll("-", "") + fileExtension;

        FileDto fileDto = FileDto.builder()
                .uploadName(uploadFileName)
                .originalName(originalFilename)
                .build();

        // file upload
        fileUploadProcess(image, fileDto);

        return fileDto;
    }


    @Override
    public void deleteFile(String uploadFileName) {

        File deleteFile = new File(fileUploadPath + uploadFileName);

        if(deleteFile.exists()) {
            deleteFile.delete();
            log.info("[FILE DELETE SUCCESS] fileName : " + uploadFileName);
        } else {
            log.info("[FILE DELETE FAIL] MSG : " + "File Not Found");
        }

    }



    // ============ 파일 업로드 함수 ============
    public void fileUploadProcess(MultipartFile image, FileDto fileDto){

        File uploadFile = new File(fileUploadPath + fileDto.getUploadName());

        try{
            image.transferTo(uploadFile);
            log.info("[File Upload Success] originalFileName : " + fileDto.getOriginalName() + " ||  uploadFileName : " + fileDto.getUploadName());
        } catch (Exception e) {
            log.info("[File Upload Fail]");
            log.info("[ERROR MSG] " + e.getMessage());
        }
    }



}
