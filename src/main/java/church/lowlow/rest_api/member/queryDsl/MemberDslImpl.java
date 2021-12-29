package church.lowlow.rest_api.member.queryDsl;

import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.rest_api.member.db.ChurchOfficer;
import church.lowlow.rest_api.member.db.Member;
import church.lowlow.rest_api.member.db.QMember;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;

public class MemberDslImpl implements MemberDsl {

    private final JPAQueryFactory jpaQueryFactory;

    public MemberDslImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    private QMember q1 = new QMember("q1");

    @Transactional
    public Page<Member> getMemberPage(SearchDto searchDto, Pageable pageable){

        QueryResults<Member> result = null;

        String key          = searchDto.getSearchId();
        String val          = searchDto.getSearchData();

        BooleanBuilder builder = new BooleanBuilder();
        builder.and((q1.name.ne("익명")));
        builder.and(q1.block.eq(false));

        // 이름 검색
        if(key.equals("name") && !val.equals(""))
            builder.and(q1.name.eq((String)val));

        // 직분 검색
        else if(key.equals("churchOfficer"))
            builder.and(q1.churchOfficer.eq(churchOfficerConverter((String)val)));

        // 교구 검색
        else if(key.equals("belong"))
            builder.and(q1.belong.eq((String)val));

        QueryResults<Member> queryResults = jpaQueryFactory.selectFrom(q1)
                .where(builder)
                .fetchResults();
        return new PageImpl<>(queryResults.getResults(), pageable, queryResults.getTotal());

    }



    // ================= 직분 영어 변경 함수  ====================
    public ChurchOfficer churchOfficerConverter(String churchOfficer){

        switch( churchOfficer ){
            case "평신도"  : return ChurchOfficer.valueOf("LAYMAN");
            case "집사"    : return ChurchOfficer.valueOf("DEACON");
            case "안수집사" : return ChurchOfficer.valueOf("ORDAINED_DEACON");
            case "권사"    : return ChurchOfficer.valueOf("SENIOR_DEACONESS");
            case "장로"    : return ChurchOfficer.valueOf("ELDER");
            case "전도사"   : return ChurchOfficer.valueOf("JUNIOR_PASTOR");
            case "부목사"   : return ChurchOfficer.valueOf("ASSISTANT_PASTOR");
            case "담임목사"  : return ChurchOfficer.valueOf("SENIOR_PASTOR");
            case "사모"     : return ChurchOfficer.valueOf("WIFE");
        };
        return ChurchOfficer.valueOf("NULL");
    }
}
