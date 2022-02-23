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

        SummerNoteSingleton instance = SummerNoteSingleton.getInstance();

        List<SummerNoteVo> galleryUploadFileList = galleryUploadFileListSetting(instance);
        List<SummerNoteVo> noticeUploadFileList = noticeUploadFileListSetting(instance);

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
        instance.setGalleryUploadFileList(galleryUploadFileList);
        instance.setNoticeUploadFileList(noticeUploadFileList);
    }



    public List<SummerNoteVo> galleryUploadFileListSetting (SummerNoteSingleton instance){

        if(instance.getGalleryUploadFileList() == null || instance.getGalleryUploadFileList().size() == 0)
            return new ArrayList<>();
        else
            return instance.getGalleryUploadFileList();
    }

    public List<SummerNoteVo> noticeUploadFileListSetting (SummerNoteSingleton instance){

        if(instance.getNoticeUploadFileList() == null || instance.getNoticeUploadFileList().size() == 0)
           return new ArrayList<>();
        else
            return instance.getNoticeUploadFileList();
    }


}
