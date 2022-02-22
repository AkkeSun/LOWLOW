package church.lowlow.user_api.batch.summerNote.listener;

import church.lowlow.user_api.batch.summerNote.singleton.SummerNoteSingleton;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import java.util.ArrayList;
import java.util.List;

public class SummerNoteJobListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        String jobName = jobExecution.getJobInstance().getJobName();
        String requestDate = jobExecution.getJobParameters().getString("requestDate");

        System.err.println();
        System.err.println();
        System.err.println("================================================================");
        System.err.println("[JOB] " + "\""+ jobName + "\"" + " 배치를 시작합니다");
        System.err.println("[JOB] 시작일 : " + requestDate);
        System.err.println();

    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        parallel2ProcessingLog();
        afterJobLog(jobExecution);
        singletonInitialize();
    }



    private void parallel2ProcessingLog(){
        System.err.println();
        System.err.println(">> [Parallel 2] Processing Success");

        SummerNoteSingleton instance = SummerNoteSingleton.getInstance();
        List<String> deleteGalleryFileList = instance.getDeleteGalleryFileList();
        List<String> deleteNoticeFileList = instance.getDeleteNoticeFileList();

        int deleteCnt = 0;

        if(deleteGalleryFileList != null){
            deleteCnt += deleteGalleryFileList.size();
            deleteGalleryFileList.forEach(deleteFile -> {
                System.err.println(">> [Parallel 2] 삭제된 파일 : " + deleteFile);
            });
        }
        if(deleteNoticeFileList != null){
            deleteCnt += deleteNoticeFileList.size();
            deleteNoticeFileList.forEach(deleteFile -> {
                System.err.println(">> [Parallel 2] 삭제된 파일 : " + deleteFile);
            });
        }
        System.err.println(">> [Parallel 2] 삭제된 파일 총 수 : " + deleteCnt);
    }


    private void afterJobLog(JobExecution jobExecution){
        String jobName = jobExecution.getJobInstance().getJobName();
        long startTime = jobExecution.getStartTime().getTime();
        long endTime = jobExecution.getEndTime().getTime();
        long time = endTime - startTime;
        BatchStatus status = jobExecution.getStatus();

        System.err.println();
        System.err.println("[JOB] Batch Status : "+ status);
        System.err.println("[JOB] 총 소요시간 : " + time + " ms");
        System.err.println("[JOB] " + "\""+ jobName + "\"" + " 배치를 종료합니다");
        System.err.println("================================================================");
        System.err.println();
        System.err.println();
    }

    private void singletonInitialize(){
        SummerNoteSingleton instance = SummerNoteSingleton.getInstance();
        instance.setGalleryUploadFileList(new ArrayList<>());
        instance.setNoticeUploadFileList(new ArrayList<>());
        instance.setNoticeContentList(new ArrayList<>());
        instance.setGalleryContentList(new ArrayList<>());
        instance.setDeleteGalleryFileList(new ArrayList<>());
        instance.setDeleteNoticeFileList(new ArrayList<>());
    }
}