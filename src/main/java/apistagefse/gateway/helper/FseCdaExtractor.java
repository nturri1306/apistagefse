package apistagefse.gateway.helper;


import apistagefse.CdaExtractor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.w3c.dom.Node;

import javax.xml.xpath.XPathExpressionException;
import java.time.LocalDateTime;

public class FseCdaExtractor extends CdaExtractor {


    /**
     * Create <code>CdaExtractor</code> for specified XML content
     *
     * @param data is the CDA content
     * @throws Exception if an exception occurs during data parsing
     */

    private String cda_xml;

    private String cda_json;

    public String getCda_json() {
        return cda_json;
    }

    public void setCda_json(String cda_json) {
        this.cda_json = cda_json;
    }

    public String getCda_xml() {
        return cda_xml;
    }

    public void setCda_xml(String cda_xml) {
        this.cda_xml = cda_xml;
        setJson();
    }


    private void setJson() {


        try {


            XmlMapper xmlMapper = new XmlMapper();

            JsonNode jsonNode = xmlMapper.readTree(cda_xml.getBytes());


            ObjectMapper objectMapper = new ObjectMapper();

            String jsonString = objectMapper.writeValueAsString(jsonNode);

            setCda_json(jsonString);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public FseCdaExtractor(byte[] data) throws Exception {
        super(data);

    }

    public String getAssistedAuthority() throws XPathExpressionException {
        return value(getNode("ClinicalDocument/recordTarget/patientRole/id/@root"));
    }

    public String getAuthorId() throws XPathExpressionException {
        return value(getNode("ClinicalDocument/author/assignedAuthor/id/@extension"));
    }

    public String getAuthorAuthority() throws XPathExpressionException {
        return value(getNode("ClinicalDocument/author/assignedAuthor/id/@root"));
    }

    public LocalDateTime getAuthorTime() throws XPathExpressionException {
        Node id = getNode("ClinicalDocument/author/time/@extension");

        return toLocalDateTime(value(id));
    }

    public String getCustodianId() throws XPathExpressionException {
        return value(getNode("ClinicalDocument/custodian/assignedCustodian/representedCustodianOrganization/id/@extension"));
    }

    public String getCustodianAuthority() throws XPathExpressionException {
        return value(getNode("ClinicalDocument/custodian/assignedCustodian/representedCustodianOrganization/id/@root"));
    }

    public String getAuthenticatorId() throws XPathExpressionException {
        return value(getNode("ClinicalDocument/legalAuthenticator/assignedEntity/id/@extension"));
    }

    public String getAuthenticatorAuthority() throws XPathExpressionException {
        return value(getNode("ClinicalDocument/legalAuthenticator/assignedEntity/id/@root"));
    }

    public LocalDateTime getAuthenticatorTime() throws XPathExpressionException {
        Node id = getNode("ClinicalDocument/legalAuthenticator/time/@extension");

        return toLocalDateTime(value(id));
    }

}
