package church.lowlow.user_api.batch;


import church.lowlow.user_api.batch.summerNote.service.SummerNoteService;
import church.lowlow.user_api.batch.summerNote.singleton.SummerNoteSingleton;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;


@RestController
public class BatchRunner {

    @Autowired
    private JobOperator jobOperator;

    @Autowired
    private SummerNoteService service;

    @GetMapping("/test")
    public String job1Start() throws JobInstanceAlreadyExistsException, NoSuchJobException, JobParametersInvalidException {
        summerNoteSingletonInitialize();
        jobOperator.start("SummerNoteJob", "requestDate=" + new Date());
        return "BATCH START";
    }

    @GetMapping("/test2")
    public String test()  {
        System.out.println("in");
        service.getGalleryList();
        return "BATCH START";
    }

    @PostMapping("/test3")
    public String test3()  {
        System.out.println("in");
        return "BATCH START";
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