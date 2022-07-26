package church.lowlow.rest_api.common.fileProcess.service;

import church.lowlow.rest_api.common.entity.FileDto;
import church.lowlow.rest_api.common.fileProcess.service.aws.AwsS3Service;
import church.lowlow.rest_api.common.fileProcess.service.basic.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Map;

/**
 * AWS S3 사용 유무에 따라 다른 매소드를 호출하기 위한 서비스
 */
@Service
public class CommonFileService {

    @Autowired
    private FileService fileService;

    @Autowired
    private AwsS3Service s3Service;

    @Value("${aws.s3.use}")
    private String aws_s3_use;


    public Map<String, FileDto> fileUpload(MultipartHttpServletRequest mRequest, String folder){
        if("true".equals(aws_s3_use))
            return s3Service.fileUpload(mRequest, folder);
        else
            return fileService.fileUpload(mRequest, folder);
    }


    public void deleteFile(String uploadFileName, String folder){
        if("true".equals(aws_s3_use))
            s3Service.deleteFile(uploadFileName, folder);
        else
            fileService.deleteFile(uploadFileName, folder);
    }


}
