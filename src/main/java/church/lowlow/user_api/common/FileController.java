package church.lowlow.user_api.common;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("/fileUpload")
public class FileController {

    @PostMapping
    public  List<HashMap<String, String>> MultipartHttpServletRequestUpload(MultipartHttpServletRequest mRequest, RedirectAttributes attributes) {

        // properties 에서 받아오기
        String path = "C:/upload/";

        List<MultipartFile> fileList = mRequest.getFiles("image");
        List<HashMap<String, String>> upLoadFileList = new ArrayList<>();

        for(MultipartFile mf : fileList) {

            String originalFilename = mf.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uploadFileName = UUID.randomUUID().toString().replaceAll("-", "") + fileExtension;

            // 파일명 저장
            HashMap<String, String> map = new HashMap<>();
            map.put("originalName", originalFilename);
            map.put("uploadName", uploadFileName);

            // 파일 생성
            File file = new File(path + uploadFileName);

            try
                {
                    mf.transferTo(file);
                    upLoadFileList.add(map);
                }
            catch (Exception e)
                {
                    log.info("[File Upload Fail]");
                    log.info("[ERROR MSG] " + e.getMessage());
                }
        }
        System.err.println(upLoadFileList);
        return upLoadFileList;
    }
}
