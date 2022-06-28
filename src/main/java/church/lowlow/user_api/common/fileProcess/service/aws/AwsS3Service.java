package church.lowlow.user_api.common.fileProcess.service.aws;

import church.lowlow.rest_api.common.entity.FileDto;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface AwsS3Service {

    Map <String, FileDto> fileUpload(MultipartHttpServletRequest mRequest, String dirName) ;
    void deleteFile(String fName, String dirName);
    void fileDownload (String fName, String dirName, HttpServletResponse response);

}
