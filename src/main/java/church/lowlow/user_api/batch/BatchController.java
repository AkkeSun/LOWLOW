package church.lowlow.user_api.batch;


import church.lowlow.user_api.batch.summerNote.singleton.SummerNoteSingleton;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;


@RestController
public class BatchController {

    @Autowired
    private JobOperator jobOperator;


    @GetMapping("/batch/summernote")
    public String summerNoteBatchStart() throws JobInstanceAlreadyExistsException, NoSuchJobException, JobParametersInvalidException {
        summerNoteSingletonInitialize();
        jobOperator.start("SummerNoteJob", "requestDate=" + new Date());
        return "<script> alert('summernote 배치가 실행되었습니다'); location.href='/admin';</script>";
    }

    private void summerNoteSingletonInitialize(){
        SummerNoteSingleton instance = SummerNoteSingleton.getInstance();
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