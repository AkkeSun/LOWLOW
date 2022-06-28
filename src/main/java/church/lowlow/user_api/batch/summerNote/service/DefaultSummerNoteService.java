package church.lowlow.user_api.batch.summerNote.service;

import church.lowlow.rest_api.gallery.db.Gallery;
import church.lowlow.rest_api.gallery.repository.GalleryRepository;
import church.lowlow.rest_api.notice.db.Notice;
import church.lowlow.rest_api.notice.repository.NoticeRepository;
import church.lowlow.rest_api.summerNote.db.SummerNoteImg;
import church.lowlow.rest_api.summerNote.repository.SummerNoteImgRepository;
import church.lowlow.user_api.batch.summerNote.domain.SummerNoteVo;
import church.lowlow.user_api.batch.summerNote.singleton.SummerNoteSingleton;
import church.lowlow.user_api.common.restApiService.RestApiService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * summerNote Image 배치 처리를 위한 Service
 * 
 * 가비아 서버를 사용하여 소캣 통신 (RestTemplate, WebClient, HttpURLConnection)을 사용하는 경우
 * Timeout 을 늘려도 java.net.SocketTimeoutException 이 발생함으로 
 * JpaRepository 로 데이터를 가져오도록 임시 개발
 */
@Service
public class DefaultSummerNoteService implements SummerNoteService {

    @Value("${cloud.aws.cloud_front.file_url_format}")
    private String awsUrl;

    @Value("${serverGbn}")
    private String serverGbn;

    @Autowired
    private RestApiService restApiService;

    @Autowired
    private SummerNoteImgRepository summerNoteImgRepository;

    @Autowired
    private GalleryRepository galleryRepository;

    @Autowired
    private NoticeRepository noticeRepository;

    private SummerNoteSingleton instance = SummerNoteSingleton.getInstance();

    private ObjectMapper om = new ObjectMapper();


    @Override
    // 업로드된 이미지 정보가 들어있는 데이터 로드
    public SummerNoteVo getUploadFileList() {

        List<SummerNoteVo> returnList = null;

        if("gabia".equals(serverGbn))
            returnList = getUploadFileListFromJpa();
        else
            returnList = getUploadFileListFromHttp();


        // return
        int uploadFileListCnt = instance.getUploadFileListCnt();
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

        List<SummerNoteVo> returnList = null;

        if("gabia".equals(serverGbn))
            returnList = getGalleryListFromJap();
        else
            returnList = getGalleryListFromHttp();


        // return
        int contentCnt = instance.getGalleryContentCnt();
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

        List<SummerNoteVo> returnList = null;

        if("gabia".equals(serverGbn))
            returnList = getNoticeListFromJap();
        else
            returnList = getNoticeListFromHttp();


        // return
        int contentCnt = instance.getNoticeContentCnt();
        SummerNoteVo summerNoteVo = null;

        if(contentCnt < returnList.size()){
            summerNoteVo = returnList.get(contentCnt);
            contentCnt++;
        }

        instance.setNoticeContentCnt(contentCnt);

        return summerNoteVo;
    }

    
    
    @Override
    // 파일 분석 후 이미지 데이터 삭제
    public void deleteData(Integer id) {

        if("gabia".equals(serverGbn))
            deleteDataFromJpa(id);
        else
            deleteDataFromHttp(id);
    }






    //============================  HttpURLConnection 통신 매소드 ============================
    private List<SummerNoteVo> getUploadFileListFromHttp() {

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

        return returnList;
    }

    private List<SummerNoteVo> getGalleryListFromHttp() {
        Map resultMap = restApiService.getRestApiMap( "/galleries/list", "GET", null);
        List<Map<String, Object>> bbsDataList = (List)resultMap.get("galleryList");

        // 파일명만 추출
        List<SummerNoteVo> returnList = getSummerNoteImgName(bbsDataList);

        return returnList;
    }

    private List<SummerNoteVo> getNoticeListFromHttp() {
        Map resultMap = restApiService.getRestApiMap( "/notices/list", "GET", null);
        List<Map<String, Object>> bbsDataList = (List)resultMap.get("noticeList");

        // 파일명만 추출
        List<SummerNoteVo> returnList = getSummerNoteImgName(bbsDataList);

        return returnList;
    }

    public void deleteDataFromHttp(Integer id) {
        restApiService.getRestApiMap("/summerNote/" + id, "DELETE", null);
    }





    //============================ JpaRepository 사용 (for Gabia Server) ============================
    private List<SummerNoteVo> getUploadFileListFromJpa() {

        List<SummerNoteImg> list = summerNoteImgRepository.findAll();
        List<Map<String, Object>> convertList = null;

        try {
            String jsonString = om.writeValueAsString(list);
            convertList = om.readValue(jsonString, new TypeReference<List<Map<String, Object>>>(){});

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Data Converting
        List<SummerNoteVo> returnList = new ArrayList<>();
        convertList.forEach(data -> {

            String uploadName = ((Map<String, String>) data.get("image")).get("uploadName");
            String bbsType = (String) data.get("bbsType");
            Integer id = (Integer) data.get("id");

            SummerNoteVo vo = SummerNoteVo.builder().uploadName(uploadName).bbsType(bbsType).id(id).build();
            returnList.add(vo);
        });
        return returnList;
    }

    private List<SummerNoteVo> getGalleryListFromJap() {

        List<Gallery> list = galleryRepository.findAll();
        List<Map<String, Object>> convertList = null;

        try {
            String jsonString = om.writeValueAsString(list);
            convertList = om.readValue(jsonString, new TypeReference<List<Map<String, Object>>>(){});

        } catch (IOException e) {
            e.printStackTrace();
        }

        // 파일명만 추출
        List<SummerNoteVo> returnList = getSummerNoteImgName(convertList);

        return returnList;
    }

    private List<SummerNoteVo> getNoticeListFromJap() {
        List<Notice> list = noticeRepository.findAll();
        List<Map<String, Object>> convertList = null;

        try {
            String jsonString = om.writeValueAsString(list);
            convertList = om.readValue(jsonString, new TypeReference<List<Map<String, Object>>>(){});

        } catch (IOException e) {
            e.printStackTrace();
        }

        // 파일명만 추출
        List<SummerNoteVo> returnList = getSummerNoteImgName(convertList);

        return returnList;
    }

    public void deleteDataFromJpa(Integer id) {
        summerNoteImgRepository.deleteById(id);
    }




    //============================ 본문에 삽입된 내용 중 이미지명만 추출하는 함수 ============================
    private List<SummerNoteVo> getSummerNoteImgName( List<Map<String, Object>> bbsDataList ) {

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
