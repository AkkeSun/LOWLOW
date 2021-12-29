package church.lowlow.rest_api.common.entity;

import lombok.*;

import javax.persistence.Embeddable;
/**
 * [ 파일 업로드 정보 ]
 * originalName 원본명
 * uploadName 업로드명
 */
@Embeddable
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class FileDto {
    private String originalName;
    private String uploadName;
}
