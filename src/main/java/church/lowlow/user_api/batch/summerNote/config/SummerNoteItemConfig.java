package church.lowlow.user_api.batch.summerNote.config;


import church.lowlow.user_api.batch.summerNote.chunk.FileUploadWriter;
import church.lowlow.user_api.batch.summerNote.chunk.GalleryWriter;
import church.lowlow.user_api.batch.summerNote.chunk.NoticeWriter;
import church.lowlow.user_api.batch.summerNote.domain.SummerNoteVo;
import church.lowlow.user_api.batch.summerNote.service.SummerNoteService;
import church.lowlow.user_api.batch.summerNote.tasklet.GalleryTasklet;
import church.lowlow.user_api.batch.summerNote.tasklet.NoticeTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;

import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class SummerNoteItemConfig {

    private final StepBuilderFactory stepBuilderFactory;

    private final FileUploadWriter fileUploadWriter;

    private final GalleryWriter galleryWriter;

    private final NoticeWriter noticeWriter;

    private final SummerNoteService summerNoteService;

    private final GalleryTasklet galleryTasklet;

    private final NoticeTasklet noticeTasklet;

    private int chunkSize = 5;



    @Bean(name = "fileUploadFlow")
    public Flow fileUploadFlow(){

        ItemReaderAdapter adapter = new ItemReaderAdapter();
        adapter.setTargetObject(summerNoteService);
        adapter.setTargetMethod("getUploadFileList");

        Step fileUploadStep = stepBuilderFactory.get("fileUploadStep")
                    .<SummerNoteVo, SummerNoteVo>chunk(chunkSize)
                    .reader(adapter)
                    .writer(fileUploadWriter)
                    .build();

        return new FlowBuilder<Flow>("fileUploadFlow")
                .start(fileUploadStep).build();
    }


    @Bean(name = "galleryContentFlow")
    public Flow galleryContentFlow(){
        ItemReaderAdapter adapter = new ItemReaderAdapter();
        adapter.setTargetObject(summerNoteService);
        adapter.setTargetMethod("getGalleryList");

        TaskletStep GalleryContentStep = stepBuilderFactory.get("galleryContentStep")
                                .<SummerNoteVo, SummerNoteVo>chunk(chunkSize)
                                .reader(adapter)
                                .writer(galleryWriter)
                                .build();
        return new FlowBuilder<Flow>("galleryContentFlow")
                .start(GalleryContentStep).build();
    }


    @Bean(name = "noticeContentFlow")
    public Flow noticeContentFlow(){
        ItemReaderAdapter adapter = new ItemReaderAdapter();
        adapter.setTargetObject(summerNoteService);
        adapter.setTargetMethod("getNoticeList");

        TaskletStep noticeContentStep = stepBuilderFactory.get("noticeContentStep")
                .<SummerNoteVo, SummerNoteVo>chunk(chunkSize)
                .reader(adapter)
                .writer(noticeWriter)
                .build();
        return new FlowBuilder<Flow>("noticeContentFlow")
                .start(noticeContentStep).build();
    }



    @Bean(name = "galleryTaskletFlow")
    public Flow galleryTaskletFlow(){
        TaskletStep galleryTaskletStep = stepBuilderFactory.get("galleryTaskletStep")
                                    .tasklet(galleryTasklet)
                                    .build();
        return new FlowBuilder<Flow>("galleryTaskletFlow")
                .start(galleryTaskletStep).build();
    }


    @Bean(name = "noticeTaskletFlow")
    public Flow noticeTaskletFlow(){
        TaskletStep noticeTaskletStep = stepBuilderFactory.get("noticeTaskletStep")
                                    .tasklet(noticeTasklet)
                                    .build();
        return new FlowBuilder<Flow>("noticeTaskletFlow")
                .start(noticeTaskletStep).build();
    }


}
