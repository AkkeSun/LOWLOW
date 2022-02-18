package church.lowlow.user_api.admin.common.controller;

import church.lowlow.rest_api.common.entity.FileDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("/file")
public class FileController {

    // =================== 파일 업로드 =====================
    @PostMapping("/upload")
    public FileDto MultipartHttpServletRequestUpload(MultipartFile image) {

        // properties 에서 받아오기
        String path = "C:/upload/";

        String originalFilename = image.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uploadFileName = UUID.randomUUID().toString().replaceAll("-", "") + fileExtension;

        // 파일명 저장
        FileDto fileDto = FileDto.builder()
                        .uploadName(uploadFileName)
                        .originalName(originalFilename)
                        .build();

        // 파일 생성
        File uploadFile = new File(path + uploadFileName);
        

        try{
            image.transferTo(uploadFile);
        } catch (Exception e) {
            log.info("[File Upload Fail]");
            log.info("[ERROR MSG] " + e.getMessage());
        }

        return fileDto;
    }


    // =================== 파일 삭제 =====================
    @PostMapping("delete")
    public void fileDelete(String uploadFileName){

        // properties 에서 받아오기
        String path = "C:/upload/"+uploadFileName;

        File deleteFile = new File(path);

        if(deleteFile.exists()) {
            deleteFile.delete();
            log.info("File Delete Success");
        } else {
            log.info("File Delete Fail");
        }
    }

}
