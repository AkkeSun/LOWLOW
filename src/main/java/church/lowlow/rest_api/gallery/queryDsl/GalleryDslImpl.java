package church.lowlow.rest_api.gallery.queryDsl;

import church.lowlow.rest_api.common.entity.PagingDto;
import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.rest_api.gallery.db.Gallery;
import church.lowlow.rest_api.gallery.db.QGallery;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;

import static church.lowlow.rest_api.common.util.StringUtil.objNullToStr;

public class GalleryDslImpl implements GalleryDsl{

    private final JPAQueryFactory jpaQueryFactory;

    private QGallery q1 = new QGallery("q1");

    public GalleryDslImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Transactional
    public Page<Gallery> getGalleryPage(SearchDto searchDto, PagingDto pagingDto) {

        String key          = objNullToStr(searchDto.getSearchId());
        String val          = objNullToStr(searchDto.getSearchData());
        int nowPage         = pagingDto.getNowPage();

        BooleanBuilder builder = new BooleanBuilder();

        // 제목 검색
        if(key.equals("title") && !val.equals(""))
            builder.and(q1.title.contains((String)val));

        // 작성자 검색
        else if(key.equals("writer"))
            builder.and(q1.writer.writer.eq(((String)val)));


        Pageable pageable = PageRequest.of(nowPage, 10);

        QueryResults<Gallery> queryResults = jpaQueryFactory.selectFrom(q1)
                .where(builder)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(q1.createdDate.desc())
                .fetchResults();;

        return new PageImpl<>(queryResults.getResults(), pageable, queryResults.getTotal());
    }
}
