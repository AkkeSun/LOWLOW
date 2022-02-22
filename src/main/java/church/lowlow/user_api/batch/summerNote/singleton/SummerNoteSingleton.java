package church.lowlow.user_api.batch.summerNote.singleton;


import church.lowlow.user_api.batch.summerNote.domain.SummerNoteVo;

import java.util.List;

/**
 * Batch 에서 데이터를 주고받기 위한 singleton
 */
public class SummerNoteSingleton {

    private List<SummerNoteVo> galleryUploadFileList;
    private List<SummerNoteVo> noticeUploadFileList;
    private List<String> galleryContentList;
    private List<String> noticeContentList;
    private List<String> deleteGalleryFileList;
    private List<String> deleteNoticeFileList;


    private SummerNoteSingleton() {
    }
    private static class SingletonHolder {
        private static final SummerNoteSingleton INSTANCE = new SummerNoteSingleton();
    }
    public static SummerNoteSingleton getInstance() {
        return SingletonHolder.INSTANCE;
    }
    public void setGalleryUploadFileList( List<SummerNoteVo> galleryUploadFileList) {
        this.galleryUploadFileList = galleryUploadFileList;
    }
    public void setNoticeUploadFileList( List<SummerNoteVo> noticeUploadFileList) {
        this.noticeUploadFileList = noticeUploadFileList;
    }
    public void setGalleryContentList( List<String> galleryContentList) {
        this.galleryContentList = galleryContentList;
    }
    public void setNoticeContentList( List<String> noticeContentList) {
        this.noticeContentList = noticeContentList;
    }
    public void setDeleteGalleryFileList( List<String> deleteGalleryFileList) {
        this.deleteGalleryFileList = deleteGalleryFileList;
    }
    public void setDeleteNoticeFileList( List<String> deleteNoticeFileList) {
        this.deleteNoticeFileList = deleteNoticeFileList;
    }

    public List<SummerNoteVo> getGalleryUploadFileList() {
        return galleryUploadFileList;
    }
    public List<SummerNoteVo> getNoticeUploadFileList() {
        return noticeUploadFileList;
    }
    public List<String> getGalleryContentList() {
        return galleryContentList;
    }
    public List<String> getNoticeContentList() {
        return noticeContentList;
    }
    public List<String> getDeleteGalleryFileList() {
        return deleteGalleryFileList;
    }
    public List<String> getDeleteNoticeFileList() {
        return noticeContentList;
    }

}
