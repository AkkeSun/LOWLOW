package church.lowlow.user_api.admin.basicInfo.service;

import church.lowlow.rest_api.basicInfo.db.BasicInfoDto;
import church.lowlow.rest_api.common.entity.FileDto;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Map;

public interface BasicInfoService {

    BasicInfoDto getBasicInfo();
    BasicInfoDto createBasicInfo(BasicInfoDto dto);
    BasicInfoDto updateBasicInfo(BasicInfoDto updateDto);
    Map<String, MultipartFile> makeMultipartFileMap (MultipartHttpServletRequest mRequest);
    void fileDtoSave(String key, FileDto fileDto, BasicInfoDto basicInfoDto);
    void setWriter(BasicInfoDto basicInfoDto);
    void fileDtoPreDataSetting(BasicInfoDto preData, BasicInfoDto updateDto);
}

