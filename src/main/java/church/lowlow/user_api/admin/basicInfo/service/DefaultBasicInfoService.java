package church.lowlow.user_api.admin.basicInfo.service;

import church.lowlow.rest_api.basicInfo.db.BasicInfo;
import church.lowlow.rest_api.basicInfo.db.BasicInfoDto;
import church.lowlow.rest_api.common.entity.FileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Service
public class DefaultBasicInfoService implements BasicInfoService{

    @Autowired
    private WebClient webClientBasicAUth;

    @Override
    public BasicInfo getBasicInfo() {

        Mono<ResponseEntity<Object>> responseEntityMono = webClientBasicAUth.get()
                .uri("/basicInfo/list")
                .exchange()
                .flatMap(clientResponse -> clientResponse.toEntity(Object.class));

        ResponseEntity<Object> block = responseEntityMono.block();
        Map<String, Object> body = (Map<String, Object>) block.getBody();
        List <BasicInfo> basicInfoList = (List<BasicInfo>) body.get("basicInfoList");

        if (basicInfoList.size() == 0)
            return null;
        return basicInfoList.get(0);
    }

    @Override
    public boolean createBasicInfo(BasicInfoDto dto) {

        // 406 에러가 나온다!!!!!!! 타입 오류
        ResponseEntity<Object> block = webClientBasicAUth.post()
                .uri("/basicInfo")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(dto))
                .exchange()
                .flatMap(clientResponse -> clientResponse.toEntity(Object.class)).block();

        HttpStatus statusCode = block.getStatusCode();

        if(statusCode.is2xxSuccessful())
            return true;
        return false;
    }



    @Override
    public boolean updateBasicInfo(BasicInfoDto dto) {
        return false;
    }


    @Override
    public Map<String, MultipartFile> makeMultipartFileMap(MultipartHttpServletRequest mRequest) {
        Map<String, MultipartFile> fileMap = new LinkedHashMap<>();

        fileMap.put("infoImg1", mRequest.getFile("infoImg1"));
        fileMap.put("infoImg2", mRequest.getFile("infoImg2"));
        fileMap.put("infoImg3", mRequest.getFile("infoImg3"));
        fileMap.put("infoImg4", mRequest.getFile("infoImg4"));
        fileMap.put("infoImg5", mRequest.getFile("infoImg5"));
        fileMap.put("infoImg6", mRequest.getFile("infoImg6"));
        fileMap.put("carImg1", mRequest.getFile("carImg1"));
        fileMap.put("carImg2", mRequest.getFile("carImg2"));
        fileMap.put("carImg3", mRequest.getFile("carImg3"));
        fileMap.put("carImg4", mRequest.getFile("carImg4"));
        fileMap.put("carImg5", mRequest.getFile("carImg5"));
        fileMap.put("carImg6", mRequest.getFile("carImg6"));
        fileMap.put("chartImg1", mRequest.getFile("carImg1"));
        fileMap.put("chartImg2", mRequest.getFile("carImg2"));
        fileMap.put("chartImg3", mRequest.getFile("carImg3"));

        return fileMap;
    }


    @Override
    public void fileDtoSave(String key, FileDto fileDto, BasicInfoDto basicInfoDto) {
        switch(key) {
            case "infoImg1":
                basicInfoDto.setInfo1_OriginalName(fileDto.getOriginalName());
                basicInfoDto.setInfo1_UploadName(fileDto.getUploadName());
                break;
            case "infoImg2":
                basicInfoDto.setInfo2_OriginalName(fileDto.getOriginalName());
                basicInfoDto.setInfo2_UploadName(fileDto.getUploadName());
                break;
            case "infoImg3":
                basicInfoDto.setInfo3_OriginalName(fileDto.getOriginalName());
                basicInfoDto.setInfo3_UploadName(fileDto.getUploadName());
                break;
            case "infoImg4":
                basicInfoDto.setInfo4_OriginalName(fileDto.getOriginalName());
                basicInfoDto.setInfo4_UploadName(fileDto.getUploadName());
                break;
            case "infoImg5":
                basicInfoDto.setInfo5_OriginalName(fileDto.getOriginalName());
                basicInfoDto.setInfo5_UploadName(fileDto.getUploadName());
                break;
            case "infoImg6":
                basicInfoDto.setInfo6_OriginalName(fileDto.getOriginalName());
                basicInfoDto.setInfo6_UploadName(fileDto.getUploadName());
                break;
            case "carImg1":
                basicInfoDto.setCar1_OriginalName(fileDto.getOriginalName());
                basicInfoDto.setCar1_UploadName(fileDto.getUploadName());
                break;
            case "carImg2":
                basicInfoDto.setCar2_OriginalName(fileDto.getOriginalName());
                basicInfoDto.setCar2_UploadName(fileDto.getUploadName());
                break;
            case "carImg3":
                basicInfoDto.setCar3_OriginalName(fileDto.getOriginalName());
                basicInfoDto.setCar3_UploadName(fileDto.getUploadName());
                break;
            case "carImg4":
                basicInfoDto.setCar4_OriginalName(fileDto.getOriginalName());
                basicInfoDto.setCar4_UploadName(fileDto.getUploadName());
                break;
            case "carImg5":
                basicInfoDto.setCar5_OriginalName(fileDto.getOriginalName());
                basicInfoDto.setCar5_UploadName(fileDto.getUploadName());
                break;
            case "carImg6":
                basicInfoDto.setCar6_OriginalName(fileDto.getOriginalName());
                basicInfoDto.setCar6_UploadName(fileDto.getUploadName());
                break;
            case "chartImg1":
                basicInfoDto.setChart1_OriginalName(fileDto.getOriginalName());
                basicInfoDto.setChart1_UploadName(fileDto.getUploadName());
                break;
            case "chartImg2":
                basicInfoDto.setChart2_OriginalName(fileDto.getOriginalName());
                basicInfoDto.setChart2_UploadName(fileDto.getUploadName());
                break;
            case "chartImg3":
                basicInfoDto.setChart3_OriginalName(fileDto.getOriginalName());
                basicInfoDto.setChart3_UploadName(fileDto.getUploadName());
                break;
        }
    }
}
