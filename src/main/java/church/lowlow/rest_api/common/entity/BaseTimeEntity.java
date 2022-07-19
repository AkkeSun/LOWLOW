package church.lowlow.rest_api.common.entity;

import church.lowlow.rest_api.common.converter.LocalDateTimeConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * createdDate 작성일
 * modifiedDate 수정일
 */
@Getter @Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

    @CreatedDate
    @Column(updatable = false)
    @Convert(converter= LocalDateTimeConverter.class)
    @ApiModelProperty(value = "신규 등록일", example = "2022-05-17 15:00:00")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Convert(converter= LocalDateTimeConverter.class)
    @ApiModelProperty(value = "수정일", example = "2022-05-17 15:00:00")
    private LocalDateTime modifiedDate;
}
