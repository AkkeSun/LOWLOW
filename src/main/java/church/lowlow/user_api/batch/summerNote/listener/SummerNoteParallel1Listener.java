package church.lowlow.user_api.batch.summerNote.listener;

import church.lowlow.user_api.batch.summerNote.domain.SummerNoteVo;
import church.lowlow.user_api.batch.summerNote.singleton.SummerNoteSingleton;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

import java.util.Date;
import java.util.List;

@Log4j2
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

        log.info("");
        log.info(">> [Parallel 1] Processing Success");

        if(galleryUploadFileList != null) {
            galleryUploadFileList.forEach( data -> {
                log.info(">> [Parallel 1] (Gallery) DB Saved File : " + data.getUploadName());
            });
        }
        if(galleryContentList != null) {
            galleryContentList.forEach (data -> {
                log.info(">> [Parallel 1] (Gallery) Content Saved File : " + data);
            });
        }
        if(noticeUploadFileList != null) {
            noticeUploadFileList.forEach( data -> {
                log.info(">> [Parallel 1] (Notice) DB Saved File " + data.getUploadName());
            });
        }
        if(noticeContentList != null) {
            noticeContentList.forEach (data -> {
                log.info(">> [Parallel 1] (Notice) Content Saved File : " + data);
            });
        }
    }
}
