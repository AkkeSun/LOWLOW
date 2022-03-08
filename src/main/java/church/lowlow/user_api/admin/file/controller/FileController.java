package church.lowlow.user_api.admin.file.controller;

import church.lowlow.rest_api.common.entity.FileDto;
import church.lowlow.user_api.admin.file.service.FileService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.util.Map;
import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService service;


    @PostMapping("/upload/{folder}")
    public Map<String, FileDto> MultipartHttpServletRequestUpload(MultipartHttpServletRequest mRequest,
                                                                  @PathVariable(value = "folder") String folder) {
        return service.fileUpload(mRequest, folder);
    }

    @PostMapping("/delete")
    public void fileDelete(String uploadFileName, String folder){
        service.deleteFile(uploadFileName, folder);
    }

}
