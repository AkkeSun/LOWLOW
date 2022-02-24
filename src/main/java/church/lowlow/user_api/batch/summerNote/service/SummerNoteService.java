package church.lowlow.user_api.batch.summerNote.service;

import church.lowlow.user_api.batch.summerNote.domain.SummerNoteVo;


public interface SummerNoteService {

    SummerNoteVo getUploadFileList();
    SummerNoteVo getGalleryList();
    SummerNoteVo getNoticeList();
    void deleteData(Integer id);

}
