package church.lowlow.user_api.batch.summerNote.service;

import church.lowlow.user_api.batch.summerNote.domain.SummerNoteVo;
import church.lowlow.user_api.batch.summerNote.singleton.SummerNoteSingleton;
import church.lowlow.user_api.common.restApiService.RestApiService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * summerNote Image 배치 처리를 위한 Service
 */
@Service
public class DefaultSummerNoteService implements SummerNoteService {

    @Value("${cloud.aws.cloud_front.file_url_format}")
    private String awsUrl;

    @Autowired
    private RestApiService restApiService;

    private SummerNoteSingleton instance = SummerNoteSingleton.getInstance();

    @Override
    // 업로드된 이미지 정보가 들어있는 데이터 로드
    public SummerNoteVo getUploadFileList() {

        int uploadFileListCnt = instance.getUploadFileListCnt();
        Map resultMap = restApiService.getRestApiMap( "/summerNote", "GET", null);

        List<Map<String, Object>> summernoteImgList = (List)resultMap.get("summernoteImgList");


        // Data Converting
        List<SummerNoteVo> returnList = new ArrayList<>();
        summernoteImgList.forEach(data -> {

            String uploadName = ((Map<String, String>) data.get("image")).get("uploadName");
            String bbsType = (String) data.get("bbsType");
            Integer id = (Integer) data.get("id");

            SummerNoteVo vo = SummerNoteVo.builder().uploadName(uploadName).bbsType(bbsType).id(id).build();
            returnList.add(vo);
        });


        // return
        SummerNoteVo summerNoteVo = null;
        if(uploadFileListCnt < returnList.size()){
            summerNoteVo = returnList.get(uploadFileListCnt);
            uploadFileListCnt++;
        }

        instance.setUploadFileListCnt(uploadFileListCnt);

        return summerNoteVo;
    }



    @Override
    // gallery summernote 본문에 삽입된 파일명 추출
    public SummerNoteVo getGalleryList() {

        int contentCnt = instance.getGalleryContentCnt();

        Map resultMap = restApiService.getRestApiMap( "/galleries/list", "GET", null);
        List<Map<String, Object>> bbsDataList = (List)resultMap.get("galleryList");

        // data converting
        List<SummerNoteVo> returnList = getSummerNoteImgName(bbsDataList);

        // return
        SummerNoteVo summerNoteVo = null;
        if(contentCnt < returnList.size()){
            summerNoteVo = returnList.get(contentCnt);
            contentCnt++;
        }

        instance.setGalleryContentCnt(contentCnt);

        return summerNoteVo;
    }



    @Override
    // notice summernote 본문에 삽입된 파일명 추출
    public SummerNoteVo getNoticeList() {

        int contentCnt = instance.getNoticeContentCnt();

        Map resultMap = restApiService.getRestApiMap( "/notices/list", "GET", null);
        List<Map<String, Object>> bbsDataList = (List)resultMap.get("noticeList");

        // 본문에서 이미지 파일명만 추출
        List<SummerNoteVo> returnList = getSummerNoteImgName(bbsDataList);

        // return
        SummerNoteVo summerNoteVo = null;
        if(contentCnt < returnList.size()){
            summerNoteVo = returnList.get(contentCnt);
            contentCnt++;
        }

        instance.setGalleryContentCnt(contentCnt);

        return summerNoteVo;
    }




    @Override
    public void deleteData(Integer id) {
        restApiService.getRestApiMap("/summerNote/" + id, "DELETE", null);
    }




    // 본문에 삽입된 내용 중 이미지명만 추출하는 함수
    private List<SummerNoteVo> getSummerNoteImgName( List<Map<String, Object>> bbsDataList) {

        List<SummerNoteVo> returnList = new ArrayList<>();

        for (Map<String, Object> data : bbsDataList) {

            Document doc = Jsoup.parse((String) data.get("contents"));
            Elements imgTag = doc.select("p img");

            if(imgTag.size() > 0)
            {
                imgTag.forEach(element-> {
                    String src = element.attr("src").replace("/upload/summernote/", "")      // 일반 업로드 타입의 path 삭제
                                                              .replace(awsUrl + "/summernote/", "");   // aws client front path 삭제
                    returnList.add(SummerNoteVo.builder().uploadName(src).build());
                });
            }
        }
        return returnList;
    }

}
