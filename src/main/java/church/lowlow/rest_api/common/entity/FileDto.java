package church.lowlow.rest_api.common.entity;

import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(value = "실제 파일명")
    private String originalName;
    @ApiModelProperty(value = "업로드 파일명")
    private String uploadName;
    @ApiModelProperty(value = "업로드 폴더명")
    private String fileDir;
    @ApiModelProperty(value = "S3 경로")
    private String fullUrl;
}
