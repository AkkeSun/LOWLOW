package church.lowlow.user_api.common.fileProcess.service.aws;

import church.lowlow.rest_api.common.entity.FileDto;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class DefaultAwsS3Service implements AwsS3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.cloud_front.file_url_format}")
    private String cloud_front_url;

    private final AmazonS3Client amazonS3Client;


    /***************************************
     *              파일 업로드
     ***************************************/
    @Override
    public Map<String, FileDto> fileUpload(MultipartHttpServletRequest mRequest, String dirName) {

        // input type file 의 name 추출
        Iterator<String> iterator = mRequest.getFileNames();
        Map<String, FileDto> returnMap = new HashMap<>();

        while(iterator.hasNext()){
            String formData_Name = iterator.next();
            MultipartFile file = mRequest.getFile(formData_Name);

            if(file.getSize() > 0 ){
                FileDto fileDto = fileUploadProcess(file, dirName);
                returnMap.put(formData_Name, fileDto);
            }
        }

        return returnMap;
    }


    /***************************************
     *              파일 삭제
     ***************************************/
    @Override
    public void deleteFile(String fName, String dirName) {
        String fileAndDir =  getFileAndDir(fName, dirName);
        try{
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileAndDir));
            log.info("[S3 FILE DELETE SUCCESS] fileName : " + fileAndDir);
        } catch (Exception e) {
            log.info("[S3 FILE DELETE FAILED]");
            log.info(e);
        }
    }


    /***************************************
     *             파일 다운로드
     ***************************************/
    @Override
    public void fileDownload (String fName, String dirName, HttpServletResponse response) {

        try
        {
            String fileAndDir =  getFileAndDir(fName, dirName);
            S3Object object = amazonS3Client.getObject(new GetObjectRequest(bucket, fileAndDir));
            S3ObjectInputStream objectInputStream = object.getObjectContent();
            byte[] bytes = IOUtils.toByteArray(objectInputStream);

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(fName, "UTF-8")+"\";");
            response.setHeader("Content-Transfer-Encoding", "binary");

            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        }
        catch(Exception e)
        {
            log.info("[FILE DOWNLOAD ERROR]");
            log.info(e);
        }

    }



    //=================== 파일 업로드 프로세스 ====================
    public FileDto fileUploadProcess(MultipartFile file, String dirName) {

        FileDto fileDto = setFileDto(file, dirName);

        File uploadFile = convert(file, fileDto)
                .orElseThrow(()->new IllegalArgumentException("[CONVERTING ERROR] MultipartFile -> File"));

        String uploadUrl = putS3(fileDto, uploadFile);
        removeLocalFile(uploadFile);
        fileDto.setFullUrl(uploadUrl);

        return fileDto;
    }



    //============ MultipartFile -> File 로 컨버팅 ==============
    private Optional<File> convert(MultipartFile file, FileDto dto) {

        try
        {
            File convertFile = new File(dto.getUploadName());

            if (convertFile.createNewFile()) {
                FileOutputStream fos = new FileOutputStream(convertFile);
                fos.write(file.getBytes());
                return Optional.of(convertFile);
            }

        }
        catch (Exception e)
        {
            log.info(e);
        }

        return Optional.empty();
    }


    //==================== 파일 dto 설정 함수 ==================
    private FileDto setFileDto (MultipartFile file, String dirName) {

        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uploadFileName = UUID.randomUUID().toString().replaceAll("-", "") + fileExtension;


        return  FileDto.builder()
                .uploadName(uploadFileName)
                .originalName(originalFilename)
                .fileDir(dirName)
                .build();
    }

    //==================== 하위 디렉토리가 있다면 파일명에 디렉토리를 붙여주는 함수 ==================
    private String getFileAndDir(String fileName, String fileDir){
        return (fileDir == null || fileDir.equals("")) ? fileName : fileDir + "/" +fileName;
    }



    //================ S3 에 파일을 업로드하는 함수 =================
    private String putS3(FileDto dto, File uploadFile){

        String fileAndDir =  getFileAndDir(dto.getUploadName(), dto.getFileDir());

        try{
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileAndDir, uploadFile)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            log.info("[S3 Upload Success] originalFileName : " + dto.getOriginalName() + " ||  uploadFileName : " + dto.getUploadName());
            log.info("[S3 URL] " + cloud_front_url + "/" + fileAndDir);
        } catch (Exception e) {
            log.info("[FILE UPLOAD FAILED]");
            log.info("[ERROR MSG] " + e.getMessage());
        }

        return cloud_front_url + "/" + fileAndDir;
    }


    //================== 생성한 로컬 파일을 삭제하는 함수 ====================
    private void removeLocalFile(File targetFile) {

        try {
            Files.delete(Paths.get(targetFile.getPath()));
            log.info("[LOCAL FILE DELETE SUCCESS]");
        } catch (NoSuchFileException e) {
            System.out.println("[LOCAL FILE DELETE FAIL] File not Found");
        } catch (DirectoryNotEmptyException e) {
            System.out.println("[LOCAL FILE DELETE FAIL] Directory is not Empty");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
