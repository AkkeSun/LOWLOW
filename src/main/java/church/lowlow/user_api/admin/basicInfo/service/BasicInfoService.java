package church.lowlow.user_api.admin.basicInfo.service;

import church.lowlow.rest_api.basicInfo.db.BasicInfo;

public interface BasicInfoService {

    BasicInfo getBasicInfo();
    BasicInfo createBasicInfo();
    BasicInfo updateBasicInfo();

}
