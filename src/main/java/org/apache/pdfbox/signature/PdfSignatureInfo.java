package org.apache.pdfbox.signature;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class PdfSignatureInfo {

    public static SignatureInfo getSignatureInfo(String filePath) {
        SignatureInfo signatureInfo = new SignatureInfo();
        try (PDDocument document = PDDocument.load(new File(filePath))) {
            PDSignature signature = document.getLastSignatureDictionary();

            signatureInfo.setSignerName(signature.getName());
            signatureInfo.setLocation(signature.getLocation());
            signatureInfo.setReason(signature.getReason());
            signatureInfo.setSignatureDate(signature.getSignDate().getTime());

           /* COSDictionary signatureDictionary = signature.getCOSObject();

            for (COSName key : signatureDictionary.keySet()) {
                COSBase value = signatureDictionary.getItem(key);
                System.out.println("Key: " + key.getName() + " - Value: " + value);
            }*/


            signatureInfo.setValidityStartDate(signature.getSignDate().getTime());


            Calendar calendar = Calendar.getInstance();
            calendar.setTime(signature.getSignDate().getTime());
            calendar.add(Calendar.YEAR, 2); //2 year validation pdf sign ?

            signatureInfo.setValidityEndDate(calendar.getTime());

            signatureInfo.setTitle(document.getDocumentInformation().getTitle());
            signatureInfo.setAuthor(document.getDocumentInformation().getAuthor());
            signatureInfo.setKeywords(document.getDocumentInformation().getKeywords());
            //   signatureInfo.setCreationDate(document.getDocumentInformation().getCreationDate().getTime());
            // signatureInfo.setModificationDate(document.getDocumentInformation().getModificationDate().getTime());


        } catch (IOException e) {
            e.printStackTrace();
        }

        return signatureInfo;
    }

}
