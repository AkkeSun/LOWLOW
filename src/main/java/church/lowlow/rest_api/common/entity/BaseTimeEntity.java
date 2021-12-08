package church.lowlow.rest_api.common.entity;

import church.lowlow.rest_api.common.converter.LocalDateTimeConverter;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Convert;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * createdDate 작성일
 * modifiedDate 수정일
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

    @CreatedDate
    @Convert(converter= LocalDateTimeConverter.class)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Convert(converter= LocalDateTimeConverter.class)
    private LocalDateTime modifiedDate;
}
