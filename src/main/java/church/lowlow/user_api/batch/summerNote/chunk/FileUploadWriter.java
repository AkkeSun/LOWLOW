package church.lowlow.user_api.batch.summerNote.chunk;

import church.lowlow.user_api.batch.summerNote.domain.SummerNoteVo;
import church.lowlow.user_api.batch.summerNote.singleton.SummerNoteSingleton;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class FileUploadWriter implements ItemWriter<SummerNoteVo> {

    @Override
    public void write(List<? extends SummerNoteVo> list) throws Exception {

        List<SummerNoteVo> galleryUploadFileList = new ArrayList<>();
        List<SummerNoteVo> noticeUploadFileList = new ArrayList<>();

        for (SummerNoteVo summerNoteVo : list) {

            if(summerNoteVo.getBbsType() != null) {
                if(summerNoteVo.getBbsType().equals("gallery")){
                    galleryUploadFileList.add(summerNoteVo);
                }
                else if(summerNoteVo.getBbsType().equals("notice"))
                    noticeUploadFileList.add(summerNoteVo);
            }

        }

        // singleton save
        SummerNoteSingleton instance = SummerNoteSingleton.getInstance();
        instance.setGalleryUploadFileList(galleryUploadFileList);
        instance.setNoticeUploadFileList(noticeUploadFileList);
    }
}
