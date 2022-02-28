package church.lowlow.user_api.admin.basicInfo.service;

import church.lowlow.rest_api.basicInfo.db.BasicInfo;
import church.lowlow.rest_api.basicInfo.db.BasicInfoDto;
import church.lowlow.rest_api.common.entity.FileDto;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Map;

public interface BasicInfoService {

    BasicInfo getBasicInfo();
    boolean createBasicInfo(BasicInfoDto dto);
    boolean updateBasicInfo(BasicInfoDto dto);
    Map<String, MultipartFile> makeMultipartFileMap (MultipartHttpServletRequest mRequest);
    void fileDtoSave(String key, FileDto fileDto, BasicInfoDto basicInfoDto);

}

