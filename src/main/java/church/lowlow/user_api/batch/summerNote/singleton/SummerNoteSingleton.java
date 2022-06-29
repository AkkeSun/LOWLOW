package church.lowlow.user_api.batch.summerNote.singleton;


import church.lowlow.user_api.batch.summerNote.domain.SummerNoteVo;

import java.util.List;
import java.util.Map;

/**
 * Batch 에서 데이터를 주고받기 위한 singleton
 */
public class SummerNoteSingleton {

    private List<Map<String, Object>> summernoteImgList;
    private List<Map<String, Object>> galleryList;
    private List<Map<String, Object>> noticeList;
    private List<SummerNoteVo> galleryUploadFileList;
    private List<SummerNoteVo> noticeUploadFileList;
    private List<String> galleryContentList;
    private List<String> noticeContentList;
    private List<SummerNoteVo> deleteGalleryFileList;
    private List<SummerNoteVo> deleteNoticeFileList;
    private int uploadFileListCnt;
    private int galleryContentCnt;
    private int noticeContentCnt;

    private SummerNoteSingleton() {
    }
    private static class SingletonHolder {
        private static final SummerNoteSingleton INSTANCE = new SummerNoteSingleton();
    }
    public static SummerNoteSingleton getInstance() {
        return SingletonHolder.INSTANCE;
    }
    public void setSummernoteImgList( List<Map<String, Object>>  summernoteImgList) {
        this.summernoteImgList = summernoteImgList;
    }
    public void setGalleryList( List<Map<String, Object>>  galleryList) {
        this.galleryList = galleryList;
    }
    public void setNoticeList( List<Map<String, Object>>  noticeList) {
        this.noticeList = noticeList;
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
    public void setDeleteGalleryFileList( List<SummerNoteVo> deleteGalleryFileList) {
        this.deleteGalleryFileList = deleteGalleryFileList;
    }
    public void setDeleteNoticeFileList( List<SummerNoteVo> deleteNoticeFileList) {
        this.deleteNoticeFileList = deleteNoticeFileList;
    }
    public void setUploadFileListCnt (int uploadFileListCnt){
        this.uploadFileListCnt = uploadFileListCnt;
    }
    public void setGalleryContentCnt (int galleryContentCnt){
        this.galleryContentCnt = galleryContentCnt;
    }

    public void setNoticeContentCnt (int noticeContentCnt){
        this.noticeContentCnt = noticeContentCnt;
    }
    public List<Map<String, Object>> getSummernoteImgList() {
        return this.summernoteImgList;
    }
    public List<Map<String, Object>> getGalleryList() {
        return this.galleryList;
    }
    public List<Map<String, Object>> getNoticeList() {
        return this.noticeList;
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
    public List<SummerNoteVo> getDeleteGalleryFileList() {
        return deleteGalleryFileList;
    }
    public List<SummerNoteVo> getDeleteNoticeFileList() {
        return deleteNoticeFileList;
    }
    public int getUploadFileListCnt() {
        return uploadFileListCnt;
    }
    public int getGalleryContentCnt(){
        return galleryContentCnt;
    }
    public int getNoticeContentCnt(){
        return galleryContentCnt;
    }
}
