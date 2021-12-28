package church.lowlow.rest_api.accounting.queryDsl;

import church.lowlow.rest_api.accounting.db.Accounting;
import church.lowlow.rest_api.accounting.db.OfferingKind;
import church.lowlow.rest_api.accounting.db.QAccounting;
import church.lowlow.rest_api.common.entity.PagingDto;
import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.rest_api.member.db.ChurchOfficer;
import church.lowlow.rest_api.member.db.Member;
import church.lowlow.rest_api.member.db.QMember;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Log4j2
public class AccountingDslImpl implements AccountingDsl {

    private final JPAQueryFactory jpaQueryFactory;

    public AccountingDslImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    private QAccounting q1 = new QAccounting("q1");


    @Transactional
    //=================== 검색 및 페이징 처리 ===================
    public Page<Accounting> getAccountingPage(SearchDto searchDto, PagingDto pagingDto) {

        QueryResults<Accounting> result = null;

        String key          = searchDto.getSearchId();
        String val          = searchDto.getSearchData();
        LocalDate startDate = searchDto.getStartDate();
        LocalDate endDate   = searchDto.getEndDate();

        log.info("[검색 데이터] id : " + key + " || data : " + val + " || StartDate : " + startDate + " || EndDate : " + endDate);

        int nowPage = pagingDto.getNowPage();
        int totalPages = pagingDto.getTotalPages();

        // 시작일, 종료일 미입력시 전체 기간 검색
        if(startDate == null)
            startDate = LocalDate.of(1500,1,1);
        if(endDate == null)
            endDate   = LocalDate.of(2100,1,1);


        BooleanBuilder builder = new BooleanBuilder();
        builder.and(q1.offeringDate.between(startDate, endDate));


        // 이름 검색
        if(key.equals("name") && !val.equals(""))
            builder.and(q1.member.name.eq((String)val));

        // 헌금 종류 검색
        else if(key.equals("offeringKind"))
            builder.and(q1.offeringKind.eq(OfferingKind.valueOf((String)val)));

        // 교구 검색
        else if(key.equals("belong"))
            builder.and(q1.member.belong.eq((String)val));

        // 직분 검색
        else if(key.equals("churchOfficer"))
            builder.and(q1.member.churchOfficer.eq(ChurchOfficer.valueOf(churchOfficerConverter((String)val))));


        QueryResults<Accounting> queryResults = jpaQueryFactory.selectFrom(q1)
                                                .where(builder)
                                                .orderBy(q1.offeringDate.desc())
                                                .fetchResults();;

        Pageable pageable = PageRequest.of(nowPage, totalPages);
        return new PageImpl<>(queryResults.getResults(), pageable, queryResults.getTotal());
    }


    @Transactional
    //=================== 헌금 분석 (이름별 & 헌금종류별) ===================
    public List<Tuple> getAccountingStatistics (SearchDto searchDto, String countKind) {

        QueryResults<Accounting> result = null;

        String key          = searchDto.getSearchId();
        String val          = searchDto.getSearchData();
        LocalDate startDate = searchDto.getStartDate();
        LocalDate endDate   = searchDto.getEndDate();

        // 시작일, 종료일 미입력시 전체 기간 검색
        if(startDate == null)
            startDate = LocalDate.of(1500,1,1);
        if(endDate == null)
            endDate   = LocalDate.of(2100,1,1);


        BooleanBuilder builder = new BooleanBuilder();
        builder.and(q1.offeringDate.between(startDate, endDate));


        // 이름 검색
        if(key.equals("name") && !val.equals(""))
            builder.and(q1.member.name.eq((String)val));

        // 헌금 종류 검색
        else if(key.equals("offeringKind"))
            builder.and(q1.offeringKind.eq(OfferingKind.valueOf((String)val)));

        // 교구 검색
        else if(key.equals("belong"))
            builder.and(q1.member.belong.eq((String)val));

        // 직분 검색
        else if(key.equals("churchOfficer"))
            builder.and(q1.member.churchOfficer.eq(ChurchOfficer.valueOf(churchOfficerConverter((String)val))));


        // 카운트 종류에 따른 출력
        if(countKind.equals("offeringKind")){
            return jpaQueryFactory.select(q1.offeringKind, q1.money.sum())
                    .from(q1)
                    .where(builder)
                    .groupBy(q1.offeringKind)
                    .fetch();
        }
        else{
            return jpaQueryFactory.select(q1.member, q1.money.sum())
                    .from(q1)
                    .where(builder)
                    .groupBy(q1.member)
                    .fetch();
        }
    }

    // -------- 교회 직분 converter -------
    public String churchOfficerConverter(String churchOfficer){
        switch(churchOfficer){
            case "평신도" : return "LAYMAN";
            case "집사"   : return "DEACON";
            case "안수집사" : return "ORDAINED_DEACON";
            case "권사" : return "SENIOR_DEACONESS";
            case "장로" : return "ELDER";
            case "전도사" : return "JUNIOR_PASTOR";
            case "부목사" : return "ASSISTANT_PASTOR";
            case "담임목사" : return "SENIOR_PASTOR";
            case "사모" : return "WIFE";
        }
        return "NULL";
    }
}
