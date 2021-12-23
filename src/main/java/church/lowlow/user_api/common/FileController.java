package church.lowlow.user_api.common;

import church.lowlow.rest_api.common.entity.Files;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("/file")
public class FileController {

    // =================== 파일 업로드 =====================
    @PostMapping("/upload")
    public Files MultipartHttpServletRequestUpload(MultipartHttpServletRequest mRequest) throws JsonProcessingException {

        // properties 에서 받아오기
        String path = "C:/upload/";

        List<MultipartFile> fileList = mRequest.getFiles("image");
        Files files = new Files();

        for(MultipartFile mf : fileList) {

            String originalFilename = mf.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uploadFileName = UUID.randomUUID().toString().replaceAll("-", "") + fileExtension;

            // 파일명 저장
            files = Files.builder()
                    .uploadName(uploadFileName)
                    .originalName(originalFilename)
                    .build();

            // 파일 생성
            File file = new File(path + uploadFileName);

            try{
                mf.transferTo(file);
            } catch (Exception e) {
                log.info("[File Upload Fail]");
                log.info("[ERROR MSG] " + e.getMessage());
            }
        }
        return files;
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
