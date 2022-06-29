package church.lowlow.user_api.batch;


import church.lowlow.user_api.batch.summerNote.singleton.SummerNoteSingleton;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
public class BatchController {

    @Autowired
    private JobOperator jobOperator;

    @PostMapping("/batch/summernote")
    public String summerNoteBatchStart(@RequestBody Map<String, Object> requestMap) throws JobInstanceAlreadyExistsException, NoSuchJobException, JobParametersInvalidException {
        summerNoteSingletonInitialize(requestMap);
        jobOperator.start("SummerNoteJob", "requestDate=" + new Date());
        return "<script> alert('summernote 배치가 실행되었습니다'); location.href='/admin';</script>";
    }


    private void summerNoteSingletonInitialize(Map<String, Object> requestMap){
        SummerNoteSingleton instance = SummerNoteSingleton.getInstance();
        instance.setSummernoteImgList((List<Map<String, Object>>) requestMap.get("summernoteImgList"));
        instance.setGalleryList((List<Map<String, Object>>) requestMap.get("galleryList"));
        instance.setNoticeList((List<Map<String, Object>>) requestMap.get("noticeList"));
        instance.setGalleryUploadFileList(new ArrayList<>());
        instance.setNoticeUploadFileList(new ArrayList<>());
        instance.setNoticeContentList(new ArrayList<>());
        instance.setGalleryContentList(new ArrayList<>());
        instance.setDeleteGalleryFileList(new ArrayList<>());
        instance.setDeleteNoticeFileList(new ArrayList<>());
        instance.setNoticeContentCnt(0);
        instance.setGalleryContentCnt(0);
        instance.setUploadFileListCnt(0);
    }
}