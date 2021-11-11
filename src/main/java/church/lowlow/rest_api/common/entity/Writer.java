package church.lowlow.rest_api.common.entity;

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
    private String writer;
    private String ip;
}