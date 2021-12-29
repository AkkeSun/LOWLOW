package church.lowlow.rest_api.gallery.db;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import static church.lowlow.rest_api.common.util.StringUtil.StringNullCheck;


@Component
public class GalleryValidation {

    public void validate(GalleryDto galleryDto, Errors errors) {

        if(StringNullCheck(galleryDto.getTitle()))
            errors.rejectValue("title", "wrongTitle", "제목을 입력하지 않았습니다");
    }
}
