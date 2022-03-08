package church.lowlow.rest_api.worshipVideo.queryDsl;

import church.lowlow.rest_api.common.entity.PagingDto;
import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.rest_api.weekly.db.QWeekly;
import church.lowlow.rest_api.weekly.db.Weekly;
import church.lowlow.rest_api.weekly.queryDsl.WeeklyDsl;
import church.lowlow.rest_api.worshipVideo.db.QWorshipVideo;
import church.lowlow.rest_api.worshipVideo.db.WorshipVideo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;

import static church.lowlow.rest_api.common.util.StringUtil.objNullToStr;

public class WorshipVideoDslImpl implements WorshipVideoDsl {

    private final JPAQueryFactory jpaQueryFactory;

    public WorshipVideoDslImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    private QWorshipVideo q1 = new QWorshipVideo("q1");

    @Transactional
    public Page<WorshipVideo> getWorshipVideList(SearchDto searchDto, PagingDto pagingDto){

        QueryResults<WorshipVideo> result = null;

        String key          = objNullToStr(searchDto.getSearchId());
        String val          = objNullToStr(searchDto.getSearchData());
        int nowPage         = pagingDto.getNowPage();

        BooleanBuilder builder = new BooleanBuilder();

        // 제목 검색
        if(key.equals("title") && !val.equals(""))
            builder.and(q1.title.contains((String)val));


        Pageable pageable = PageRequest.of(nowPage, 10);

        QueryResults<WorshipVideo> queryResults = jpaQueryFactory.selectFrom(q1)
                .limit(pageable.getPageSize()) // 출력할 데이터 수
                .offset(pageable.getOffset())  // 출력할 페이지 인덱스
                .orderBy(q1.modifiedDate.desc())
                .where(builder)
                .fetchResults();

        return new PageImpl<>(queryResults.getResults(), pageable, queryResults.getTotal());

    }
}
