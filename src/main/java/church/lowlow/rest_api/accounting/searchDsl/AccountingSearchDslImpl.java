package church.lowlow.rest_api.accounting.searchDsl;

import church.lowlow.rest_api.accounting.db.Accounting;
import church.lowlow.rest_api.accounting.db.OfferingKind;
import church.lowlow.rest_api.accounting.db.QAccounting;
import church.lowlow.rest_api.common.entity.PagingDto;
import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.rest_api.member.db.ChurchOfficer;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

import static church.lowlow.rest_api.common.util.StringUtil.StringNullCheck;

public class AccountingSearchDslImpl implements AccountingSearchDsl {

    @Autowired
    private final JPAQueryFactory jpaQueryFactory;

    private QAccounting q1 = new QAccounting("q1");

    public AccountingSearchDslImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }



    @Override
    public Page<Accounting> getAccountingPage(SearchDto searchDto, PagingDto pagingDto) {

        QueryResults<Accounting> result = null;

        String key          = searchDto.getSearchId();
        String val          = searchDto.getSearchData();
        LocalDate startDate = searchDto.getStartDate();
        LocalDate endDate   = searchDto.getEndDate();
        int nowPage = pagingDto.getNowPage();
        int totalPages = pagingDto.getTotalPages();
        
        // 시작일, 종료일 미입력시 전체 기간 검색
        if(startDate == null)
            startDate = LocalDate.of(1500,1,1);
        if(endDate == null)
            endDate   = LocalDate.of(2100,1,1);

        // 전체 검색
        if(StringNullCheck(val)){
            result = jpaQueryFactory.selectFrom(q1)
                    .where(q1.offeringDate.between(startDate, endDate))
                    .orderBy(q1.offeringDate.desc())
                    .fetchResults();
        }

        // 익명 검색
        else
        if(val.equals("익명")){
            result = jpaQueryFactory.selectFrom(q1)
                    .where(
                            q1.member.isNull(),
                            q1.offeringDate.between(startDate, endDate)
                    )
                    .orderBy(q1.offeringDate.desc())
                    .fetchResults();
        }

        // 이름 검색
        else
        if(key.equals("name")){
            result = jpaQueryFactory.selectFrom(q1)
                    .where(
                            q1.member.name.eq((String)val),
                            q1.offeringDate.between(startDate, endDate)
                    )
                    .orderBy(q1.offeringDate.desc())
                    .fetchResults();
        }

        // 헌금 종류 검색
        else
        if(key.equals("offeringKind")){
            result = jpaQueryFactory.selectFrom(q1)
                    .where(
                            q1.offeringKind.eq(OfferingKind.valueOf((String)val)),
                            q1.offeringDate.between(startDate, endDate)
                    )
                    .orderBy(q1.offeringDate.desc())
                    .fetchResults();
        }

        // 교구 검색
        else
        if(key.equals("belong")){
            result = jpaQueryFactory.selectFrom(q1)
                    .where(
                            q1.member.belong.eq((String)val),
                            q1.offeringDate.between(startDate, endDate)
                    )
                    .orderBy(q1.offeringDate.desc())
                    .fetchResults();
        }

        // 직분 검색
        else
        if(key.equals("churchOfficer")){
            result = jpaQueryFactory.selectFrom(q1)
                    .where(
                            q1.member.churchOfficer.eq(ChurchOfficer.valueOf((String)val)),
                            q1.offeringDate.between(startDate, endDate)
                    )
                    .orderBy(q1.offeringDate.desc())
                    .fetchResults();
        }

        Pageable pageable = PageRequest.of(nowPage, totalPages);
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

/*
    @Transactional
    public List<Accounting> getAccountingList(SearchDto searchDto) throws ParseException {

        String key          = searchDto.getSearchId();
        String val          = searchDto.getSearchData();
        LocalDate startDate = searchDto.getStartDate();
        LocalDate endDate   = searchDto.getEndDate();

        // 시작일, 종료일 미입력시 전체 기간으로 설정
        if(startDate == null)
            startDate = LocalDate.of(1500,1,1);
        if(endDate == null)
            endDate   = LocalDate.of(2100,1,1);

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(q1.offeringDate.between(startDate, endDate));

        // 익명 검색
        if(val.equals("익명"))
            builder.and(q1.member.isNull());

        // 이름 검색
        else if(key.equals("name"))
            builder.and(q1.member.name.eq((String)val));

        // 헌금 종류 검색
        else if(key.equals("offeringKind"))
            builder.and(q1.offeringKind.eq(OfferingKind.valueOf((String)val)));

        // 교구 검색
        else if(key.equals("belong"))
            builder.and(q1.member.belong.eq((String)val));

        else if(key.equals("churchOfficer"))
            builder.and( q1.member.churchOfficer.eq(ChurchOfficer.valueOf((String)val)));

        return jpaQueryFactory.selectFrom(q1)
                .where(builder)
                .orderBy(q1.offeringDate.desc())
                .fetch();
    }

 */
}
