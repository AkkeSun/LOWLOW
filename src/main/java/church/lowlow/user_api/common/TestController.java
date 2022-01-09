package church.lowlow.user_api.common;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class TestController{

    @ResponseBody
    @PostMapping("/uploadSummernoteImageFile.do")
    public String Test1( @RequestParam("file") MultipartFile fileload,
                         HttpServletRequest req) {


        // upload 경로 설정
        String webPath = "C:/upload/";
        String originalFileName = fileload.getOriginalFilename();

        //new fileName 만들어서 넣기
        File file = new File(webPath + originalFileName);

        try {
            FileUtils.writeByteArrayToFile(file, fileload.getBytes());
            System.out.println("File Upload Success");

        } catch (IOException e) {
            file.delete();	//저장된 파일 삭제
            System.out.println("File Upload Fail");
            e.printStackTrace();
        }
        return "/upload/"+originalFileName;
    }
}