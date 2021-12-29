package church.lowlow.rest_api.gallery.queryDsl;

import church.lowlow.rest_api.common.entity.PagingDto;
import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.rest_api.gallery.db.Gallery;
import org.springframework.data.domain.Page;

public interface GalleryDsl {

    Page<Gallery> getGalleryPage(SearchDto searchDto, PagingDto pagingDto);
}
