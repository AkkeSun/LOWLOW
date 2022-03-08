package church.lowlow.user_api.batch.summerNote.service;

import church.lowlow.user_api.batch.summerNote.domain.SummerNoteVo;
import church.lowlow.user_api.batch.summerNote.singleton.SummerNoteSingleton;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * summerNote Image 배치 처리를 위한 Service
 */
@Service
public class DefaultSummerNoteService implements SummerNoteService {

    @Autowired
    private WebClient webClient;



    private SummerNoteSingleton instance = SummerNoteSingleton.getInstance();

    @Override
    public SummerNoteVo getUploadFileList() {

        int uploadFileListCnt = instance.getUploadFileListCnt();

        // Data Load
        Mono<Map> mono = webClient
                .get()
                .uri("/summerNote")
                .retrieve()
                .bodyToMono(Map.class);
        Map<String, Object> resultMap = mono.block();

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
    public SummerNoteVo getGalleryList() {

        int contentCnt = instance.getGalleryContentCnt();

        // Data Load
        Mono<Map> mono = webClient
                .get()
                .uri("/galleries/list")
                .retrieve()
                .bodyToMono(Map.class);

        Map<String, Object> resultMap = mono.block();
        List<Map<String, Object>> bbsDataList = (List)resultMap.get("galleryList");


        // Data Converting
        List<SummerNoteVo> returnList = new ArrayList<>();
        for (Map<String, Object> data : bbsDataList) {

            Integer id = (Integer) data.get("id");
            Document doc = Jsoup.parse((String) data.get("contents"));
            Elements imgTag = doc.select("p img");

            if(imgTag != null) {
                imgTag.forEach(element-> {
                    String src = element.attr("src").replace("/upload/summernote/", "");
                    returnList.add(SummerNoteVo.builder().uploadName(src).build());
                });
            }
        }

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
    public SummerNoteVo getNoticeList() {

        int contentCnt = instance.getNoticeContentCnt();

        // Data Load
        Mono<Map> mono = webClient
                .get()
                .uri("/notices/list")
                .retrieve()
                .bodyToMono(Map.class);

        Map<String, Object> resultMap = mono.block();
        List<Map<String, Object>> bbsDataList = (List)resultMap.get("noticeList");


        // Data Converting
        List<SummerNoteVo> returnList = new ArrayList<>();
        for (Map<String, Object> data : bbsDataList) {

            Integer id = (Integer) data.get("id");
            Document doc = Jsoup.parse((String) data.get("contents"));
            Elements imgTag = doc.select("p img");

            if(imgTag != null) {
                imgTag.forEach(element-> {
                    String src = element.attr("src").replace("/upload/summernote/", "");
                    returnList.add(SummerNoteVo.builder().uploadName(src).build());
                });
            }
        }

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

        webClient.delete()
                .uri("/summerNote/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
