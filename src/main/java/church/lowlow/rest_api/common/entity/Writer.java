package church.lowlow.rest_api.common.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.Embeddable;

/**
 * [ 작성자 ]
 * writer 작성자 이름
 * writerIp 작성된 IP
 */
@Embeddable
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Writer {
    
    @ApiModelProperty(value = "작성자 아이디", example = "lowlow")
    private String writer;
    @ApiModelProperty(value = "작성자 IP", example = "123.1234.123.123")
    private String ip;
}