package church.lowlow.user_api.batch.summerNote.tasklet;

import church.lowlow.user_api.fileProcess.service.basic.FileService;
import church.lowlow.user_api.batch.summerNote.domain.SummerNoteVo;
import church.lowlow.user_api.batch.summerNote.service.SummerNoteService;
import church.lowlow.user_api.batch.summerNote.singleton.SummerNoteSingleton;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NoticeTasklet implements Tasklet {

    private final FileService fileService;
    private final SummerNoteService summerNoteService;
    private boolean isFileExist = false;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        SummerNoteSingleton instance = SummerNoteSingleton.getInstance();

        List<SummerNoteVo> noticeUploadFileList = instance.getNoticeUploadFileList();
        List<String> noticeContentList = instance.getNoticeContentList();
        List<String> deleteFileList = new ArrayList<>();

        if(noticeUploadFileList.size() != 0 && noticeContentList.size() != 0) {
            deleteFileList = fileCheckProcess(noticeUploadFileList, noticeContentList);
        }

        instance.setDeleteNoticeFileList(deleteFileList);

        return RepeatStatus.FINISHED;
    }


    public List<String> fileCheckProcess(List<SummerNoteVo> noticeUploadFileList, List<String> noticeContentList) {

        List<String> deleteFileList = new ArrayList<>();

        noticeUploadFileList.forEach(summerNoteVo -> {

            // db에 저장된 파일이 본문에 수록된 파일인지 체크
            noticeContentList.forEach( contentFileName -> {
                if(contentFileName.equals(summerNoteVo.getUploadName())) {
                    isFileExist = true;
                    return;
                }
            });

            // 본문에 수록된 파일이 아니라면 삭제처리
            if(!isFileExist) {
                fileDeleteProcess(summerNoteVo);
                deleteFileList.add(summerNoteVo.getUploadName());
            }
            isFileExist = false;
        });

        return deleteFileList;
    }


    public void fileDeleteProcess(SummerNoteVo summerNoteVo) {
        summerNoteService.deleteData(summerNoteVo.getId());
        fileService.deleteFile(summerNoteVo.getUploadName(), "summernote");
    }


}
