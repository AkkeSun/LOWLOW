package church.lowlow.rest_api.memberAttend.queryDsl;

import church.lowlow.rest_api.common.entity.PagingDto;
import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.rest_api.memberAttend.db.MemberAttend;
import church.lowlow.rest_api.memberAttend.db.MemberAttendListDto;
import church.lowlow.rest_api.memberAttend.db.QMemberAttend;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

import static church.lowlow.rest_api.common.util.StringUtil.objNullToStr;

public class M_AttendDslImpl implements M_AttendDsl {

    private final JPAQueryFactory jpaQueryFactory;

    public M_AttendDslImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    private QMemberAttend q1 = new QMemberAttend("q1");


    @Transactional
    //======================= 출석관리 리스트 로드 =======================
    public Map<String, Object> getMemberAttendList(SearchDto searchDto, PagingDto pagingDto, String belong) {

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

        // List setting
        List<LocalDate> dates = jpaQueryFactory.select(q1.checkDate)
                                                .from(q1)
                                                .where(builder)
                                                .groupBy(q1.checkDate)
                                                .limit(pageable.getPageSize())
                                                .offset(pageable.getOffset())
                                                .fetch();
        Collections.sort(dates, Collections.reverseOrder());
        List<MemberAttendListDto> returnList = new ArrayList<>();

        dates.forEach( date ->{
            Long trueCount  = jpaQueryFactory.select(q1.count()).from(q1).where((q1.checkDate.eq(date)), q1.isAttend.eq(true)).fetchOne();
            Long falseCount = jpaQueryFactory.select(q1.count()).from(q1).where((q1.checkDate.eq(date)), q1.isAttend.eq(false)).fetchOne();
            MemberAttendListDto build = MemberAttendListDto.builder().checkDate(date).isAttendTrue(trueCount).isAttendFalse(falseCount).total(trueCount + falseCount).build();
            returnList.add(build);
        });


        // totalContentCnt Setting
        int totalContentCnt = jpaQueryFactory.select(q1.count()).from(q1).groupBy(q1.checkDate).where(builder).fetch().size();

        // return
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("memberAttendList", returnList);
        returnMap.put("totalContentCnt", totalContentCnt);

        return returnMap;
    }





    @Transactional
    //======================= 출석괸리 디테일 =======================
    public List<MemberAttend> getMemberAttendDetail (String belong, String checkDate) {

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
                                              .orderBy(q1.modifiedDate.asc()).fetch();
        return memberAttendList;
    }


}
