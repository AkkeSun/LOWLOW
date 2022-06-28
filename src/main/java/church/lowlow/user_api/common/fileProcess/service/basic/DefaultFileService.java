package church.lowlow.user_api.common.fileProcess.service.basic;

import church.lowlow.rest_api.common.entity.FileDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.transaction.Transactional;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

@Service
@Log4j2
public class DefaultFileService implements FileService{


    @Value("${fileUploadPath}")
    private String fileUploadPath;


    @Override
    @Transactional
    public Map<String, FileDto> fileUpload(MultipartHttpServletRequest mRequest, String folder){

        // input type file 의 name 추출
        Iterator<String> iterator = mRequest.getFileNames();

        uploadFolderSetting(folder);
        Map<String, FileDto> returnMap = new HashMap<>();

        while(iterator.hasNext()){
            String formData_Name = iterator.next();
            MultipartFile file = mRequest.getFile(formData_Name);

            if(file.getSize() > 0 ){
                FileDto fileDto = fileUploadProcess(file, folder);
                returnMap.put(formData_Name, fileDto);
            }
        }

        return returnMap;
    }


    @Override
    @Transactional
    public void deleteFile(String uploadFileName, String folder) {

        File deleteFile = new File(fileUploadPath + "/" + folder + "/" + uploadFileName);

        if(deleteFile.exists()) {
            deleteFile.delete();
            log.info("[FILE DELETE SUCCESS] fileName : " + uploadFileName);
        } else {
            log.info("[FILE DELETE FAIL] MSG : " + "File Not Found");
        }

    }



    // ============ 파일 업로드 함수 ============
    public FileDto fileUploadProcess(MultipartFile file, String folder){

        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uploadFileName = UUID.randomUUID().toString().replaceAll("-", "") + fileExtension;

        FileDto fileDto = FileDto.builder()
                .uploadName(uploadFileName)
                .originalName(originalFilename)
                .build();

        File uploadFile = new File(fileUploadPath + "/" + folder + "/" +fileDto.getUploadName());

        try{
            file.transferTo(uploadFile);
            log.info("[File Upload Success] originalFileName : " + fileDto.getOriginalName() + " ||  uploadFileName : " + fileDto.getUploadName());
        } catch (Exception e) {
            log.info("[File Upload Fail]");
            log.info("[ERROR MSG] " + e.getMessage());
        }

        return fileDto;
    }


    //============== 파일 업로드 폴더 셋팅 ===========
    public void uploadFolderSetting (String folderPath){
        File folder = new File(fileUploadPath + "/" + folderPath);
        if (!folder.exists()) {
            folder.mkdir();
        }
    }


}
