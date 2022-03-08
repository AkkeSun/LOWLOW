package church.lowlow.user_api.admin.basicInfo.service;

import church.lowlow.rest_api.basicInfo.db.BasicInfoDto;
import church.lowlow.rest_api.common.entity.FileDto;
import church.lowlow.rest_api.common.entity.Writer;

import org.springframework.beans.factory.annotation.Autowired;
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
    public BasicInfoDto updateBasicInfo(BasicInfoDto updateDto) {

        ResponseEntity<Object> block = webClient.put()
                .uri("/basicInfo/{id}", updateDto.getId())
                .body(BodyInserters.fromObject(updateDto))
                .exchange()
                .flatMap(clientResponse -> clientResponse.toEntity(Object.class)).block();

        if(!block.getStatusCode().is2xxSuccessful())
            return null;

        Map<String, Object> body = (Map<String, Object>) block.getBody();
        return basicInfoBuilder(body);
    }



    @Override
    public void fileDtoSave(String key, FileDto fileDto, BasicInfoDto basicInfoDto) {
        switch(key) {
            case "infoImg1": basicInfoDto.setInfoImage1(fileDto); break;
            case "infoImg2": basicInfoDto.setInfoImage2(fileDto); break;
            case "infoImg3": basicInfoDto.setInfoImage3(fileDto); break;
            case "infoImg4": basicInfoDto.setInfoImage4(fileDto); break;
            case "infoImg5": basicInfoDto.setInfoImage5(fileDto); break;
            case "infoImg6": basicInfoDto.setInfoImage6(fileDto); break;
            case "carImg1": basicInfoDto.setCarouselImg1(fileDto); break;
            case "carImg2": basicInfoDto.setCarouselImg2(fileDto); break;
            case "carImg3": basicInfoDto.setCarouselImg3(fileDto); break;
            case "carImg4": basicInfoDto.setCarouselImg4(fileDto); break;
            case "carImg5": basicInfoDto.setCarouselImg5(fileDto); break;
            case "carImg6": basicInfoDto.setCarouselImg6(fileDto); break;
            case "chartImg1": basicInfoDto.setOrganizationChart1(fileDto); break;
            case "chartImg2": basicInfoDto.setOrganizationChart2(fileDto); break;
            case "chartImg3": basicInfoDto.setOrganizationChart3(fileDto); break;
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


    @Override
    public void fileDtoPreDataSetting(BasicInfoDto preData, BasicInfoDto updateDto) {
        updateDto.setOrganizationChart1(preData.getOrganizationChart1());
        updateDto.setOrganizationChart2(preData.getOrganizationChart2());
        updateDto.setOrganizationChart3(preData.getOrganizationChart3());
        updateDto.setCarouselImg1(preData.getCarouselImg1());
        updateDto.setCarouselImg2(preData.getCarouselImg2());
        updateDto.setCarouselImg3(preData.getCarouselImg3());
        updateDto.setCarouselImg4(preData.getCarouselImg4());
        updateDto.setCarouselImg5(preData.getCarouselImg5());
        updateDto.setCarouselImg6(preData.getCarouselImg6());
        updateDto.setInfoImage1(preData.getInfoImage1());
        updateDto.setInfoImage2(preData.getInfoImage2());
        updateDto.setInfoImage3(preData.getInfoImage3());
        updateDto.setInfoImage4(preData.getInfoImage4());
        updateDto.setInfoImage5(preData.getInfoImage5());
        updateDto.setInfoImage6(preData.getInfoImage6());
    }




    public BasicInfoDto basicInfoBuilder(Map<String, Object> map) {
        BasicInfoDto build = BasicInfoDto.builder()
                .id((Integer)map.get("id"))
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
                .infoImage1(fileDtoConverter(map, "infoImage1"))
                .infoImage2(fileDtoConverter(map, "infoImage2"))
                .infoImage3(fileDtoConverter(map, "infoImage3"))
                .infoImage4(fileDtoConverter(map, "infoImage4"))
                .infoImage5(fileDtoConverter(map, "infoImage5"))
                .infoImage6(fileDtoConverter(map, "infoImage6"))
                .carouselImg1(fileDtoConverter(map, "carouselImg1"))
                .carouselImg2(fileDtoConverter(map, "carouselImg2"))
                .carouselImg3(fileDtoConverter(map, "carouselImg3"))
                .carouselImg4(fileDtoConverter(map, "carouselImg4"))
                .carouselImg5(fileDtoConverter(map, "carouselImg5"))
                .carouselImg6(fileDtoConverter(map, "carouselImg6"))
                .organizationChart1(fileDtoConverter(map, "organizationChart1"))
                .organizationChart2(fileDtoConverter(map, "organizationChart2"))
                .organizationChart3(fileDtoConverter(map, "organizationChart3"))
                .build();
        return build;
    }

    public FileDto fileDtoConverter(Map<String,Object> map, String key){

        LinkedHashMap<String,Object> imageMap = (LinkedHashMap<String, Object>) map.get(key);
        if(imageMap == null)
            return null;
        return FileDto.builder().originalName((String)imageMap.get("originalName")).uploadName((String)imageMap.get("uploadName")).build();
    }

}
