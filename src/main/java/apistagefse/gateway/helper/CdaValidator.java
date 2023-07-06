package apistagefse.gateway.helper;

import apistagefse.base.UniqueId;
import apistagefse.base.stream.StreamOp;
import apistagefse.gateway.data.SchemaMap;
import apistagefse.gateway.imported.dispatcher.PDFUtility;
import apistagefse.gateway.imported.dispatcher.dto.AttachmentDTO;
import apistagefse.gateway.imported.validator.CDAHelper;
import apistagefse.gateway.imported.validator.dto.SchematronValidationResultDTO;
import com.helger.commons.io.resource.IReadableResource;
import com.helger.commons.io.resource.inmemory.ReadableResourceInputStream;
import com.helger.schematron.xslt.SchematronResourceSCH;
import org.apache.logging.log4j.util.Strings;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class CdaValidator {

    private byte[] pdf;
    private File parent;
    private File source;

    private String originalFileName;

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }


    public CdaValidator(File parent, byte[] pdf) {
        this.pdf = pdf;
        this.parent = parent;
    }

    public CdaValidator(File parent, File pdf) throws IOException {
        this.pdf = Files.readAllBytes(pdf.toPath());
        this.parent = parent;
    }

    public File getSource() {
        return source;
    }

    public String getSourceAsString() {
        return source != null ? source.getAbsolutePath() : null;
    }

    public FseCdaExtractor getExtractor() throws Exception {
        StreamOp s = new StreamOp(new File(parent, new UniqueId().generateUuid() + ".pdf"));
        s.write(pdf);

        String out = PDFUtility.unenvelopeA2(pdf);
        if (Strings.isBlank(out)) {
            final Map<String, AttachmentDTO> attachments = PDFUtility.extractAttachments(pdf);

            if (!attachments.isEmpty()) {
                if (attachments.size() == 1) {
                    out = new String(attachments.values().iterator().next().getContent());

                }
            }
        }

        FseCdaExtractor extractor = new FseCdaExtractor(out.getBytes());

        extractor.setCda_xml(new String(out.getBytes()));


        source = s.getFile();


        return extractor;
    }

    public StringBuffer validateCda(String schematronPath, byte[] cda, String value) throws IOException {
        StringBuffer result = new StringBuffer();

        String schemaValue = SchemaMap.getSchemaMap().get(value);

        Path path = new File(schematronPath, schemaValue + ".sch").toPath();

        byte[] schematron = Files.readAllBytes(path);

        try (ByteArrayInputStream bytes = new ByteArrayInputStream(schematron)) {
            IReadableResource readableResource = new ReadableResourceInputStream(path.getFileName().toString(), bytes);
            SchematronResourceSCH schematronResource = new SchematronResourceSCH(readableResource);

            SchematronValidationResultDTO resultDTO = CDAHelper.validateXMLViaSchematronFull(schematronResource, cda);

            if (resultDTO.getFailedAssertions().size() > 0) {
                for (var ass : resultDTO.getFailedAssertions()) {
                    result.append(ass.getText() + "\r\n");
                    result.append("----> " + ass.getTest() + "\r\n");
                }
            } else {
                if (resultDTO.getValidSchematron()) {
                    result.append("true");
                } else if (resultDTO.getValidXML()) {
                    result.append("true");
                }

                return result;
            }
        } catch (Exception e) {
            result.append("cda not valid");
            result.append(e.getMessage());
        }

        return result;
    }

}
