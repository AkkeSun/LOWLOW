package church.lowlow.user_api.batch;

import church.lowlow.user_api.batch.summerNote.singleton.SummerNoteSingleton;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;

@Component
public class BatchRunner {

    @Autowired
    private JobOperator jobOperator;


    @Scheduled(cron = "0 6 0 1 * *") // 매 월 1일 06시에 실행
    public void summerNoteBatch() throws JobInstanceAlreadyExistsException, NoSuchJobException, JobParametersInvalidException {
        summerNoteSingletonInitialize();
        jobOperator.start("SummerNoteJob", "requestDate=" + new Date());
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
