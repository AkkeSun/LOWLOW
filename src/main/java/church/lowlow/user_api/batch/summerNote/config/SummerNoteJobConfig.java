package church.lowlow.user_api.batch.summerNote.config;

import church.lowlow.user_api.batch.summerNote.listener.SummerNoteJobListener;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * SummerNote 를 사용하는 테이블 이미지를 처리하는 Batch
 *
 * [Parallel 1]
 *  1) fileUploadFlow
 *      Reader : ItemReaderAdapter -> SummerNoteService.getUploadFileList()
 *             : summerNoteImg 테이블에 저장된 데이터 로드
 *      writer : FileUploadWriter
 *             : bbsType 에 따라 데이터를 분류하여 singleton 에 저장
 *  2) galleryContentFlow
 *      Reader : ItemReaderAdapter -> SummerNoteService.getGalleryList()
 *             : Gallery 본문에 삽입된 이미지 데이터 로드
 *      writer : GalleryWriter
 *             :singleton 에 저장
 *  3) noticeContentFlow
 *      Reader : ItemReaderAdapter -> SummerNoteService.getNoticeList()
 *             : Notice 본문에 삽입된 이미지 데이터 로드
 *      writer : NoticeWriter
 *             : singleton 에 저장
 *
 * [Parallel 2]
 *  1) GalleryTasklet
 *      - bbsType 이 Gallery 인 summerNoteImg 테이블 데이터가 본문에 수록된 데이터인지 체크
 *      - 본문에 수록된 데이터가 아니라면 파일 및 DB 데이터 삭제
 *  2) NoticeTasklet
 *      - bbsType 이 Notice 인 summerNoteImg 테이블 데이터가 본문에 수록된 데이터인지 체크
 *      - 본문에 수록된 데이터가 아니라면 파일 및 DB 데이터 삭제
 *
 */
@Configuration
@RequiredArgsConstructor
public class SummerNoteJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final Flow fileUploadFlow;
    private final Flow galleryContentFlow;
    private final Flow galleryTaskletFlow;
    private final JobRegistry jobRegistry;

    @Bean
    public Job SummerNoteJob(){
        return jobBuilderFactory.get("SummerNoteJob")
                .listener(new SummerNoteJobListener())
                .start(fileUploadFlow)
                .split(parallelExecutor())
                   .add(galleryContentFlow)
                .next(galleryTaskletFlow)
                .end()
                .build();
    }

    @Bean
    public TaskExecutor parallelExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setThreadNamePrefix("parallel-thread");
        taskExecutor.setCorePoolSize(2);
        taskExecutor.setMaxPoolSize(4);
        return taskExecutor;
    }

    @Bean
    public BeanPostProcessor jobRegistryBeanPostProcessor(){
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
        return jobRegistryBeanPostProcessor;
    }
}
