package church.lowlow.user_api.batch.summerNote.listener;

import church.lowlow.user_api.batch.summerNote.domain.SummerNoteVo;
import church.lowlow.user_api.batch.summerNote.service.SummerNoteService;
import church.lowlow.user_api.batch.summerNote.singleton.SummerNoteSingleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class SummerNoteJobListener implements JobExecutionListener {

    private int deleteCnt = 0;

    @Autowired
    private final SummerNoteService summerNoteService;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        String jobName = jobExecution.getJobInstance().getJobName();
        String requestDate = jobExecution.getJobParameters().getString("requestDate");

        log.info("");
        log.info("================================================================");
        log.info("[JOB] " + "\""+ jobName + "\"" + " Batch Start");
        log.info("[JOB] Start Date : " + requestDate);
        log.info("");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        parallel2FileDelete();
        afterJobLog(jobExecution);
        deleteCnt = 0;
    }



    private void parallel2FileDelete() {
        log.info("");
        log.info(">> [Parallel 2] Processing Success");

        SummerNoteSingleton instance = SummerNoteSingleton.getInstance();
        List<SummerNoteVo> deleteGalleryFileList = instance.getDeleteGalleryFileList();
        List<SummerNoteVo> deleteNoticeFileList = instance.getDeleteNoticeFileList();

        if(deleteGalleryFileList.size() != 0){
            deleteCnt += deleteGalleryFileList.size();
            deleteGalleryFileList.forEach(deleteFile -> {
                summerNoteService.deleteData(deleteFile);
                log.info(">> [Parallel 2] Delete File : " + deleteFile.getUploadName());
            });
        }
        if(deleteNoticeFileList.size() != 0){
            deleteCnt += deleteNoticeFileList.size();
            deleteNoticeFileList.forEach(deleteFile -> {
                summerNoteService.deleteData(deleteFile);
                log.info(">> [Parallel 2] Delete File  : " + deleteFile.getUploadName());
            });
        }
        log.info(">> [Parallel 2] Delete Total count : " + deleteCnt);
    }


    private void afterJobLog(JobExecution jobExecution){
        String jobName = jobExecution.getJobInstance().getJobName();
        long startTime = jobExecution.getStartTime().getTime();
        long endTime = jobExecution.getEndTime().getTime();
        long time = endTime - startTime;
        BatchStatus status = jobExecution.getStatus();

        log.info("");
        log.info("[JOB] Batch Status : "+ status);
        log.info("[JOB] Total Time : " + time + " ms");
        log.info("[JOB] " + "\""+ jobName + "\"" + " Batch End");
        log.info("================================================================");
        log.info("");
        log.info("");
    }

}