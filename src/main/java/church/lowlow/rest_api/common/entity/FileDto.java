package church.lowlow.rest_api.common.entity;

import lombok.*;

import javax.persistence.Embeddable;
/**
 * [ 파일 업로드 정보 ]
 * originalName 원본명
 * uploadName   업로드명
 * fileDir      추가 파일 디렉토리
 * fullUrl      AWS clientFront 전용 파일 url
 */
@Embeddable
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class FileDto {
    private String originalName;
    private String uploadName;
    private String fileDir;
    private String fullUrl;
}
