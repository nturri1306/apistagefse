package apistagefse.route.rest.service.helper;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
public class ParamRequest {

    private MultipartFile multipartFile;

    private String documentId;
    private Object requestBody;

    public ParamRequest(String documentId) {
        this.documentId = documentId;
    }

    public ParamRequest(String documentId, Object requestBody, MultipartFile multipartFile) {
        this.documentId = documentId;
        this.requestBody = requestBody;
        this.multipartFile = multipartFile;
    }


    public ParamRequest(Object requestBody, MultipartFile multipartFile) {
        this.requestBody = requestBody;
        this.multipartFile = multipartFile;
    }

}
