package church.lowlow.rest_api.accounting.searchBox;

import church.lowlow.rest_api.accounting.db.Accounting;
import church.lowlow.rest_api.accounting.db.OfferingKind;
import church.lowlow.rest_api.accounting.db.QAccounting;
import church.lowlow.rest_api.member.db.ChurchOfficer;
import church.lowlow.rest_api.member.db.QMember;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public class SearchBoxDslImpl implements SearchBoxDsl {

    @Autowired
    private final JPAQueryFactory jpaQueryFactory;

    private QAccounting q1 = new QAccounting("q1");

    public SearchBoxDslImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }



    @Override
    public Page<Accounting> searchBox(SearchBox searchBox, Pageable pageable) {

        QueryResults<Accounting> result = null;

        String key = searchBox.getKey();
        Object val = searchBox.getVal();
        LocalDate startDate = searchBox.getStartDate();
        LocalDate endDate = searchBox.getEndDate();
        
        // 시작일, 종료일 미입력시 전체 기간 검색
        if(startDate == null)
            startDate = LocalDate.of(1500,1,1);
        if(endDate == null)
            endDate   = LocalDate.of(2100,1,1);

        // 전체 검색
        if(key == null){
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

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }
}
