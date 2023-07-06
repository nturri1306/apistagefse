package apistagefse.gateway.imported.dispatcher.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AttachmentDTO {
    private String fileName;
    private byte[] content;
    private String mimeType;
}
