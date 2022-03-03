package church.lowlow.user_api.admin.basicInfo.service;

import church.lowlow.rest_api.basicInfo.db.BasicInfoDto;
import church.lowlow.rest_api.common.entity.FileDto;
import church.lowlow.rest_api.common.entity.Writer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Service
public class DefaultBasicInfoService implements BasicInfoService{

    @Autowired
    private WebClient webClient;


    @Override
    public BasicInfoDto getBasicInfo() {

        Mono<ResponseEntity<Object>> responseEntityMono = webClient.get()
                .uri("/basicInfo/list")
                .exchange()
                .flatMap(clientResponse -> clientResponse.toEntity(Object.class));

        ResponseEntity<Object> block = responseEntityMono.block();
        Map<String, Object> body = (Map<String, Object>) block.getBody();
        List <LinkedHashMap<String, Object>> basicInfoList = (List<LinkedHashMap<String, Object>>) body.get("basicInfoList");

        if (basicInfoList.size() == 0)
            return null;

        return basicInfoBuilder(basicInfoList.get(0));
    }

    @Override
    public BasicInfoDto createBasicInfo(BasicInfoDto dto) {

        ResponseEntity<Object> block = webClient.post()
                .uri("/basicInfo")
                .body(BodyInserters.fromObject(dto))
                .exchange()
                .flatMap(clientResponse -> clientResponse.toEntity(Object.class)).block();

        if(!block.getStatusCode().is2xxSuccessful())
            return null;

        Map<String, Object> body = (Map<String, Object>) block.getBody();
        return basicInfoBuilder(body);
    }



    @Override
    public BasicInfoDto updateBasicInfo(BasicInfoDto dto) {
        return null;
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

    @Override
    // api/basicInfo 는 csrf() 예외 처리를 하므로 username 을 불러올 수 없어서 dto로 처리
    public void setWriter(BasicInfoDto basicInfoDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        String ip = details.getRemoteAddress();

        Object principal = authentication.getPrincipal();
        String writer = ((UserDetails) principal).getUsername();

        Writer saveWriter = Writer.builder().writer(writer).ip(ip).build();
        basicInfoDto.setWriter(saveWriter);
    }


    public BasicInfoDto basicInfoBuilder(Map<String, Object> map) {

        BasicInfoDto build = BasicInfoDto.builder()
                .basicInfo((String) map.get("basicInfo"))
                .detailInfo((String) map.get("detailInfo"))
                .name((String) map.get("name"))
                .senior_pastor_name((String) map.get("senior_pastor_name"))
                .basicAddress((String) map.get("basicAddress"))
                .detailAddress((String) map.get("detailAddress"))
                .callNumber((String) map.get("callNumber"))
                .kakaoPage((String) map.get("kakaoPage"))
                .youtubeURL((String) map.get("youtubeURL"))
                .blog((String) map.get("blog"))
                .Info1_OriginalName((String) map.get("Info1_OriginalName"))
                .Info1_UploadName((String) map.get("Info1_UploadName"))
                .Info2_OriginalName((String) map.get("Info2_OriginalName"))
                .Info2_UploadName((String) map.get("Info2_UploadName"))
                .Info3_OriginalName((String) map.get("Info3_OriginalName"))
                .Info3_UploadName((String) map.get("Info3_UploadName"))
                .Info4_OriginalName((String) map.get("Info4_OriginalName"))
                .Info4_UploadName((String) map.get("Info4_UploadName"))
                .Info5_OriginalName((String) map.get("Info5_OriginalName"))
                .Info5_UploadName((String) map.get("Info5_UploadName"))
                .Info6_OriginalName((String) map.get("Info6_OriginalName"))
                .Info6_UploadName((String) map.get("Info6_UploadName"))
                .Car1_OriginalName((String) map.get("Car1_OriginalName"))
                .Car1_UploadName((String) map.get("Car1_UploadName"))
                .Car2_OriginalName((String) map.get("Car2_OriginalName"))
                .Car2_UploadName((String) map.get("Car2_UploadName"))
                .Car3_OriginalName((String) map.get("Car3_OriginalName"))
                .Car3_UploadName((String) map.get("Car3_UploadName"))
                .Car4_OriginalName((String) map.get("Car4_OriginalName"))
                .Car4_UploadName((String) map.get("Car4_UploadName"))
                .Car5_OriginalName((String) map.get("Car5_OriginalName"))
                .Car5_UploadName((String) map.get("Car5_UploadName"))
                .Car6_OriginalName((String) map.get("Car6_OriginalName"))
                .Car6_UploadName((String) map.get("Car6_UploadName"))
                .Chart1_OriginalName((String) map.get("Chart1_OriginalName"))
                .Chart1_UploadName((String) map.get("Chart1_UploadName"))
                .Chart2_OriginalName((String) map.get("Chart2_OriginalName"))
                .Chart2_UploadName((String) map.get("Chart2_UploadName"))
                .Chart3_OriginalName((String) map.get("Chart3_OriginalName"))
                .Chart3_UploadName((String) map.get("Chart3_UploadName"))
                .build();

        return build;
    }
}
