package church.lowlow.user_api.batch.summerNote.service;

import church.lowlow.rest_api.summerNote.repository.SummerNoteImgRepository;
import church.lowlow.user_api.admin.file.service.FileService;
import church.lowlow.user_api.batch.summerNote.domain.SummerNoteVo;
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

    @Autowired
    private SummerNoteImgRepository repository;

    private int uploadFileListCnt;

    private int galleryContentCnt;
    private int noticeContentCnt;


    @Override
    public SummerNoteVo getUploadFileList() {

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

        return summerNoteVo;
    }



    @Override
    public SummerNoteVo getGalleryList() {

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
                    String src = element.attr("src").replace("/upload/", "");
                    returnList.add(SummerNoteVo.builder().uploadName(src).build());
                });
            }
        }

        // return
        SummerNoteVo summerNoteVo = null;
        if(galleryContentCnt < returnList.size()){
            summerNoteVo = returnList.get(galleryContentCnt);
            galleryContentCnt++;
        }

        return summerNoteVo;
    }

    @Override
    public SummerNoteVo getNoticeList() {
        return null;
    }



    @Override
    public void deleteData(Integer id) {
       // repository.deleteById(id);
        // 이거 수정 (delete 는 void로 가면 되나>
        webClient.delete().uri("/summerNote/{id}", id).retrieve().bodyToMono(Void.class);
    }


}
