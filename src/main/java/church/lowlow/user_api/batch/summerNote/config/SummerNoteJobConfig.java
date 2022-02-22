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
