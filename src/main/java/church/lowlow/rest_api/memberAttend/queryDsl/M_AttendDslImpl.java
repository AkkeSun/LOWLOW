package church.lowlow.rest_api.memberAttend.queryDsl;

import church.lowlow.rest_api.accounting.db.Accounting;
import church.lowlow.rest_api.accounting.db.OfferingKind;
import church.lowlow.rest_api.accounting.db.QAccounting;
import church.lowlow.rest_api.common.converter.LocalDateConverter;
import church.lowlow.rest_api.common.entity.PagingDto;
import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.rest_api.member.db.ChurchOfficer;
import church.lowlow.rest_api.member.db.QMember;
import church.lowlow.rest_api.memberAttend.db.MemberAttend;
import church.lowlow.rest_api.memberAttend.db.QMemberAttend;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

import static church.lowlow.rest_api.common.util.StringUtil.objNullToStr;

public class M_AttendDslImpl implements M_AttendDsl {

    private final JPAQueryFactory jpaQueryFactory;

    public M_AttendDslImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    private QMemberAttend q1 = new QMemberAttend("q1");


    @Transactional
    //======================= 출석관리 리스트 로드 =======================
    public Page<Tuple> getMemberAttendList(SearchDto searchDto, PagingDto pagingDto, String belong) {

        String val          = objNullToStr(searchDto.getSearchData());
        int nowPage         = pagingDto.getNowPage();

        BooleanBuilder builder = new BooleanBuilder();

        if(!val.equals("")) {
            LocalDate date = LocalDate.parse(val);
            builder.and(q1.checkDate.between(date.minusDays(1), date));
        }

        if(!belong.equals(""))
            builder.and(q1.member.belong.eq(belong));

        Pageable pageable = PageRequest.of(nowPage, 10);


        QueryResults<Tuple> queryResults = jpaQueryFactory.select(
                                                            q1.count(),
                                                            q1.checkDate,
                                                            jpaQueryFactory.select(q1.count())
                                                                            .from(q1)
                                                                            .where(q1.isAttend.eq(true)),
                                                            jpaQueryFactory.select(q1.count())
                                                                            .from(q1)
                                                                            .where(q1.isAttend.eq(false))
                                                             )
                                                            .from(q1)
                                                            .where(builder)
                                                            .limit(pageable.getPageSize())
                                                            .offset(pageable.getOffset())
                                                            .orderBy(q1.modifiedDate.desc())
                                                            .fetchResults();

        return new PageImpl<>(queryResults.getResults(), pageable, queryResults.getTotal());
    }





    @Transactional
    //======================= 출석괸리 디테일 =======================
    public List<MemberAttend> getMemberAttendDetail(String belong, String checkDate) {

        BooleanBuilder builder = new BooleanBuilder();

        // 교구 리더라면 교구의 성도만 출력
        if(!belong.equals(""))
            builder.and(q1.member.belong.eq(belong));

        if(!checkDate.equals("")) {
            LocalDate date = LocalDate.parse(checkDate);
            builder.and(q1.checkDate.eq(date));
        }

        List<MemberAttend> memberAttendList = jpaQueryFactory.selectFrom(q1)
                                            .where(builder)
                                            .orderBy(q1.modifiedDate.desc()).fetch();
        return memberAttendList;
    }


}
