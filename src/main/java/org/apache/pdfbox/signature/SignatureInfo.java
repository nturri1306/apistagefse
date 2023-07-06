package org.apache.pdfbox.signature;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SignatureInfo {
    private String signerName;
    private String location;
    private String reason;
    private Date signatureDate;
    private Date validityStartDate;
    private Date validityEndDate;
    private String title;
    private String author;
    private String keywords;
    private Date creationDate;
    private Date modificationDate;
}
