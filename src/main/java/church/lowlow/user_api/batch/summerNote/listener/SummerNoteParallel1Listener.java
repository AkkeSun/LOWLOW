package church.lowlow.user_api.batch.summerNote.listener;

import church.lowlow.user_api.batch.summerNote.domain.SummerNoteVo;
import church.lowlow.user_api.batch.summerNote.singleton.SummerNoteSingleton;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

import java.util.Date;
import java.util.List;

public class SummerNoteParallel1Listener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        // job 을 실행시키기 위한 전달
        stepExecution.getExecutionContext().put("parallel1_RequestDate", new Date());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        parallel1Logging();
        return null;
    }

    public void parallel1Logging(){

        SummerNoteSingleton instance = SummerNoteSingleton.getInstance();
        List<SummerNoteVo> galleryUploadFileList = instance.getGalleryUploadFileList();
        List<SummerNoteVo> noticeUploadFileList = instance.getNoticeUploadFileList();
        List<String> galleryContentList = instance.getGalleryContentList();
        List<String> noticeContentList = instance.getNoticeContentList();

        System.err.println();
        System.err.println(">> [Parallel 1] Processing Success");

        if(galleryUploadFileList != null) {
            galleryUploadFileList.forEach( data -> {
                System.err.println(">> [Parallel 1] (Gallery) DB에 저장된 파일: " + data.getUploadName());
            });
        }
        if(galleryContentList != null) {
            galleryContentList.forEach (data -> {
                System.err.println(">> [Parallel 1] (Gallery) 본문에 포함된 파일 : " + data);
            });
        }
        if(noticeUploadFileList != null) {
            noticeUploadFileList.forEach( data -> {
                System.err.println(">> [Parallel 1] (Notice) DB에 저장된 파일: " + data.getUploadName());
            });
        }
        if(noticeContentList != null) {
            noticeContentList.forEach (data -> {
                System.err.println(">> [Parallel 1] (Notice) 본문에 포함된 파일 : " + data);
            });
        }
    }
}
