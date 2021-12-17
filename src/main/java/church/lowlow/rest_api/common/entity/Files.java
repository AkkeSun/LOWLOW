package church.lowlow.rest_api.common.entity;

import lombok.*;

import javax.persistence.Embeddable;
/**
 * [ 업로드 이미지 ]
 * originalName 원본명
 * uploadName 업로드명
 */
@Embeddable
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Files {
    private String originalName;
    private String uploadName;
}
