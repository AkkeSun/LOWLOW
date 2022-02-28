package church.lowlow.rest_api.common.aop;

import church.lowlow.rest_api.accounting.db.AccountingDto;
import church.lowlow.rest_api.basicInfo.db.BasicInfoDto;
import church.lowlow.rest_api.calendar.db.CalendarDto;
import church.lowlow.rest_api.common.entity.PagingDto;
import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.rest_api.gallery.db.GalleryDto;
import church.lowlow.rest_api.member.db.MemberDto;
import church.lowlow.rest_api.notice.db.NoticeDto;
import church.lowlow.rest_api.summerNote.db.SummerNoteImgDto;
import church.lowlow.rest_api.weekly.db.WeeklyDto;
import church.lowlow.rest_api.worshipVideo.db.WorshipVideoDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

/**
 * Request Parameter Logging Component
 */
@Component
@Log4j2
public class LogComponent {

    public void accountingDtoLogging(AccountingDto dto){
        log.info("[REQUEST] memberId : \"" + dto.getMemberId() + "\"");
        log.info("[REQUEST] money : \"" + dto.getMoney() + "\"");
        log.info("[REQUEST] OfferingKind : \"" + dto.getOfferingKind() + "\"");
        log.info("[REQUEST] OfferingDate : \"" + dto.getOfferingDate() + "\"");
        log.info("[REQUEST] note : \"" + dto.getNote() + "\"");
        log.info("[REQUEST] name : \"" + dto.getName() + "\"");
    }

    public void basicInfoDtoLogging(BasicInfoDto dto){
        log.info("[REQUEST] basicInfo : \"" + dto.getBasicInfo() + "\"");
        log.info("[REQUEST] detailInfo : \"" + dto.getDetailInfo() + "\"");
        log.info("[REQUEST] name : \"" + dto.getName() + "\"");
        log.info("[REQUEST] senior_pastor_name : \" " + dto.getSenior_pastor_name() + "\"");
        log.info("[REQUEST] basicAddress : \"" + dto.getBasicAddress() + "\"");
        log.info("[REQUEST] detailAddress : \"" + dto.getDetailAddress() + "\"");
        log.info("[REQUEST] callNumber : \"" + dto.getCallNumber() + "\"");
        log.info("[REQUEST] kakaoPage : \"" + dto.getKakaoPage() + "\"");
        log.info("[REQUEST] youtubeURL : \"" + dto.getYoutubeURL() + "\"");
        log.info("[REQUEST] instagram : \"" + dto.getInstagram() + "\"");
        log.info("[REQUEST] blog : \"" + dto.getBlog() + "\"");
        log.info("[REQUEST] infoImage1 : \"" + dto.getInfo1_OriginalName() + "\"");
        log.info("[REQUEST] infoImage2 : \"" + dto.getInfo2_OriginalName() + "\"");
        log.info("[REQUEST] infoImage3 : \"" + dto.getInfo3_OriginalName() + "\"");
        log.info("[REQUEST] infoImage4 : \"" + dto.getInfo4_OriginalName() + "\"");
        log.info("[REQUEST] infoImage5 : \"" + dto.getInfo5_OriginalName() + "\"");
        log.info("[REQUEST] infoImage6 : \"" + dto.getInfo6_OriginalName() + "\"");
        log.info("[REQUEST] carouselImg1 : \"" + dto.getCar1_OriginalName() + "\"");
        log.info("[REQUEST] carouselImg2 : \"" + dto.getCar2_OriginalName() + "\"");
        log.info("[REQUEST] carouselImg3 : \"" + dto.getCar3_OriginalName() + "\"");
        log.info("[REQUEST] carouselImg4 : \"" + dto.getCar4_OriginalName() + "\"");
        log.info("[REQUEST] carouselImg5 : \"" + dto.getCar5_OriginalName() + "\"");
        log.info("[REQUEST] carouselImg6 : \"" + dto.getCar6_OriginalName() + "\"");
        log.info("[REQUEST] organizationChart1 : \"" + dto.getChart1_OriginalName() + "\"");
        log.info("[REQUEST] organizationChart2 : \"" + dto.getChart2_OriginalName() + "\"");
        log.info("[REQUEST] organizationChart3 : \"" + dto.getChart3_OriginalName() + "\"");
    }


    public void calendarDtoLogging(CalendarDto dto){
        log.info("[REQUEST] title : \"" + dto.getTitle() + "\"");
        log.info("[REQUEST] start : \"" + dto.getStart() + "\"");
        log.info("[REQUEST] end : \"" + dto.getEnd() + "\"");
    }


    public void galleryDtoLogging(GalleryDto dto){
        log.info("[REQUEST] title : \"" + dto.getTitle() + "\"");
        log.info("[REQUEST] contents : \"" + dto.getContents() + "\"");
    }

    public void memberDtoLogging(MemberDto dto){
        log.info("[REQUEST] name : \"" + dto.getRegiDate() + "\"");
        log.info("[REQUEST] phoneNumber : \"" + dto.getPhoneNumber() + "\"");
        log.info("[REQUEST] birthDay : \"" + dto.getBirthDay() + "\"");
        log.info("[REQUEST] belong : \"" + dto.getBelong() + "\"");
        log.info("[REQUEST] churchOfficer : \"" + dto.getChurchOfficer() + "\"");
        log.info("[REQUEST] regiDate : \"" + dto.getRegiDate() + " \"");
        log.info("[REQUEST] img_originalName : \"" + dto.getOriginalName() + "\"");
        log.info("[REQUEST] img_uploadName : \"" + dto.getUploadName() + "\"");
    }

    public void noticeDtoLogging(NoticeDto dto){
        log.info("[REQUEST] title : \"" + dto.getTitle() + "\"");
        log.info("[REQUEST] contents : \"" + dto.getContents() + "\"");
    }

    public void weeklyDtoLogging(WeeklyDto dto){
        log.info("[REQUEST] title : \"" + dto.getTitle() + "\"");
        log.info("[REQUEST] img1 : \"" + dto.getImg1() + "\"");
        log.info("[REQUEST] img2 : \"" + dto.getImg2() + "\"");
        log.info("[REQUEST] img3 : \"" + dto.getImg3() + "\"");
    }

    public void worshipVideoDtoLogging(WorshipVideoDto dto){
        log.info("[REQUEST] title : \"" + dto.getTitle() + "\"");
        log.info("[REQUEST] link : \"" + dto.getLink() + "\"");
    }

    public void summernoteDtoLogging(SummerNoteImgDto dto){
        log.info("[REQUEST] originalName : \"" + dto.getOriginalName() + "\"");
        log.info("[REQUEST] uploadName : \"" + dto.getUploadName() + "\"");
        log.info("[REQUEST] BbsType : \"" + dto.getBbsType() + "\"");

    }

    public void searchDtoLogging(SearchDto dto){
        log.info("[REQUEST] SEARCH_searchId : \"" + dto.getSearchId() + "\"");
        log.info("[REQUEST] SEARCH_searchData : \"" + dto.getSearchData() + "\"");
        log.info("[REQUEST] SEARCH_startDate : \"" + dto.getStartDate() + "\"");
        log.info("[REQUEST] SEARCH_endDate : \"" + dto.getEndDate() + "\"");
    }

    public void pagingDtoLogging(PagingDto dto){
        log.info("[REQUEST] PAGING_nowPage : \"" + dto.getNowPage() + "\"");
        log.info("[REQUEST] PAGING_totalPages : \"" + dto.getTotalPages() + "\"");
    }

}
