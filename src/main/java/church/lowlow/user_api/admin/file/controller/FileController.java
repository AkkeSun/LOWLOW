package church.lowlow.user_api.admin.file.controller;

import church.lowlow.rest_api.common.entity.FileDto;
import church.lowlow.user_api.admin.file.service.FileService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService service;


    @PostMapping("/upload")
    public FileDto MultipartHttpServletRequestUpload(MultipartFile image) {
        return service.fileUpload(image);
    }

    @PostMapping("/upload2")
    public FileDto multiUpload(MultipartFile image) {
        return service.fileUpload(image);
    }

    @PostMapping("/delete")
    public void fileDelete(String uploadFileName){
        service.deleteFile(uploadFileName);
    }

}
