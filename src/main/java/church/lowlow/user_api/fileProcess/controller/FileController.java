package church.lowlow.user_api.fileProcess.controller;

import church.lowlow.rest_api.common.entity.FileDto;
import church.lowlow.user_api.fileProcess.service.CommonFileService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private CommonFileService service;


    @PostMapping("/upload/{folder}")
    public Map<String, FileDto> fileUpload(MultipartHttpServletRequest mRequest, @PathVariable(value = "folder") String folder) {
        return service.fileUpload(mRequest, folder);
    }

    @PostMapping("/delete")
    public void fileDelete(String uploadFileName, String folder){
        service.deleteFile(uploadFileName, folder);
    }

}
