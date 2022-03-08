package church.lowlow.user_api.batch.summerNote.config;

import church.lowlow.user_api.batch.summerNote.listener.SummerNoteParallel1Listener;
import church.lowlow.user_api.batch.summerNote.listener.SummerNoteParallel2Listener;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.step.job.DefaultJobParametersExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@Configuration
@RequiredArgsConstructor
public class SummerNoteParallelConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final Flow fileUploadFlow;

    private final Flow galleryContentFlow;

    private final Flow galleryTaskletFlow;

    private final Flow noticeContentFlow;

    private final Flow noticeTaskletFlow;

    private final JobLauncher jobLauncher;



    @Bean
    public Job parallel1Job(){
        return jobBuilderFactory.get("parallel1Job")
                .start(fileUploadFlow)
                .split(parallelExecutor()).add(galleryContentFlow, noticeContentFlow)
                .end()
                .build();
    }


    @Bean
    public Job parallel2Job(){
        return jobBuilderFactory.get("parallel2Job")
                .start(galleryTaskletFlow)
                .split(parallelExecutor()).add(noticeTaskletFlow)
                .end()
                .build();
    }



    @Bean
    public Step parallel1Step(){
        return stepBuilderFactory.get("parallel1Step")
                .job(parallel1Job())
                .launcher(jobLauncher) // job 을 실행시키는 도구
                // Step 의 ExecutionContext 를 Job 이 실행되는데 필요한 JobParameter 로 변환
                .parametersExtractor(jobParametersExtractor())
                // stepExecutionContext 주입
                .listener(new SummerNoteParallel1Listener())
                .build();
    }



    @Bean
    public Step parallel2Step(){
        return stepBuilderFactory.get("parallel2Step")
                .job(parallel2Job())
                .launcher(jobLauncher)
                .parametersExtractor(jobParametersExtractor())
                .listener(new SummerNoteParallel2Listener())
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



    // ExecutionContext 의 key 값을 가져오는 함수
    private DefaultJobParametersExtractor jobParametersExtractor () {
        DefaultJobParametersExtractor extractor = new DefaultJobParametersExtractor();
        extractor.setKeys(new String[]{"parallel1_RequestDate", "parallel2_RequestDate"});
        return extractor;
    }

}
