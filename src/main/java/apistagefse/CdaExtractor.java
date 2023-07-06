package apistagefse;

import apistagefse.base.DateHelper;
import org.apache.logging.log4j.util.Strings;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * @author b.amoruso
 */
public class CdaExtractor {

    private final Document doc;
    private final byte[] content;

    /**
     * Create <code>CdaExtractor</code> for specified XML content
     *
     * @param data is the CDA content
     * @throws Exception if an exception occurs during data parsing
     */
    public CdaExtractor(byte[] data) throws Exception {
        if (data == null)
            throw new IOException("CDA content is null");

        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = domFactory.newDocumentBuilder();

        this.content = data;
        this.doc = builder.parse(new ByteArrayInputStream(data));
    }

    public static byte[] decode(String encoding, byte[] data) {
        if (encoding.equalsIgnoreCase("base64") || encoding.equalsIgnoreCase("b64"))
            return Base64.getDecoder().decode(data);

        return null;
    }

    public String getTitle() throws XPathExpressionException {
        Node id = getNode(
                "ClinicalDocument/title");

        return id != null ? id.getTextContent() : null;
    }

    public String getDocumentId() throws XPathExpressionException {
        return getDocumentId(false);
    }

    public String getDocumentId(boolean rootOnly) throws XPathExpressionException {
        Node root = getNode(
                "ClinicalDocument/id/@root");
        Node extension = getNode(
                "ClinicalDocument/id/@extension");

        String r = value(root);
        String e = value(extension);

        return rootOnly ? r : ((Strings.isNotBlank(r) ? r + "^" : "") + (Strings.isNotBlank(e) ? e : ""));
    }

    public String getLanguageCode() throws XPathExpressionException {
        Node id = getNode(
                "ClinicalDocument/languageCode/@code");

        return value(id);
    }

    public LocalDateTime getEffectiveTime() throws XPathExpressionException {
        Node id = getNode(
                "ClinicalDocument/effectiveTime/@value");

        return toLocalDateTime(value(id));
    }

    public byte[] getObservationMedia(String mimeType) throws XPathExpressionException {
        Node obs = getNode(
                "ClinicalDocument/component/structuredBody/component/section/entry/observationMedia/value");

        if (obs != null) {
            Node mediaType = obs.getAttributes().getNamedItem("mediaType");
            Node representation = obs.getAttributes().getNamedItem("representation");

            if (value(mediaType).equalsIgnoreCase(mimeType) && value(representation) != null)
                return decode(value(representation), obs.getTextContent().getBytes());
        }

        return null;
    }

    public byte[] getContent() {
        return content;
    }

    public String getConfidentialityCode() throws XPathExpressionException {
        return value(getNode("ClinicalDocument/confidentialityCode/@code")); // e.g. N
    }

    public String getFormatCode() throws XPathExpressionException {
        return value(getNode("ClinicalDocument/templateId/@root")); // e.g. 2.16.840.1.113883.2.9.10.1.1 for Laboratory
    }

    public String getTypeCode() throws XPathExpressionException {
        return value(getNode("ClinicalDocument/code/@code")); // e.g. 11502-2 for Laboratory
    }

    public String getTypeCode(String attribute) throws XPathExpressionException {
        return value(getNode("ClinicalDocument/code/@" + attribute));
    }

    public String getAssistedCode() throws XPathExpressionException {
        return value(getNode("ClinicalDocument/recordTarget/patientRole/id/@extension"));
    }

    public LocalDateTime getAuthenticatorTime() throws XPathExpressionException {
        return toLocalDateTime(value(
                getNode("ClinicalDocument/legalAuthenticator/time/@value")));
    }

    public String getAuthorFiscalCode() throws XPathExpressionException {
        Node author = getNode("ClinicalDocument/legalAuthenticator/assignedEntity/id/@extension");
        if (author == null)
            author = getNode("ClinicalDocument/author/assignedAuthor/id/@extension");

        return value(author);
    }

    public String getAuthorTelecom() throws XPathExpressionException {
        return value(getNode("ClinicalDocument/author/assignedAuthor/telecom/@value"));
    }

    public LocalDateTime getStartPerformance() throws XPathExpressionException {
        Node id = getNode(
                "ClinicalDocument/documentationOf/serviceEvent/effectiveTime/low/@value");

        return toLocalDateTime(value(id));
    }

    public LocalDateTime getEndPerformance() throws XPathExpressionException {
        Node id = getNode(
                "ClinicalDocument/documentationOf/serviceEvent/effectiveTime/high/@value");

        return toLocalDateTime(value(id));
    }

    public List<String> getPrescriptions() throws XPathExpressionException {
        NodeList nodes = getNodeList("ClinicalDocument/inFulfillmentOf/order/id");
        List<String> p = new ArrayList<>();

        // Example: 1500A4005000011^^^&amp;2.16.840.1.113883.2.9.4.3.8&amp;ISO^urn:ihe:iti:xds:2013:order
        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);

            String assigningAuthorityName = n.getAttributes().
                    getNamedItem("assigningAuthorityName").getNodeValue();

            if (assigningAuthorityName != null && assigningAuthorityName.equals("MEF")) {
                String root = n.getAttributes().getNamedItem("root").getNodeValue();
                String extension = n.getAttributes().getNamedItem("extension").getNodeValue();

                p.add(extension + "^^^&" + root + "&ISO^urn:ihe:iti:xds:2013:order");
            }
        }

        return p;
    }

    public Node getNode(String path) throws XPathExpressionException {
        return (Node) getXPath().evaluate(path, doc, XPathConstants.NODE);
    }

    public NodeList getNodeList(String path) throws XPathExpressionException {
        return (NodeList) getXPath().evaluate(path, this.doc, XPathConstants.NODESET);
    }

    protected String value(Node id) {
        return id != null ? id.getNodeValue() : null;
    }

    protected LocalDateTime toLocalDateTime(String date) {
        try {
            return DateHelper.toLocalDateTime(date, DateHelper.DATE_TIME_ZONE_PATTERN);
        } catch (DateTimeParseException ex1) {
            return DateHelper.toLocalDateTime(date, DateHelper.DATETIME_PATTERN);
        }
    }

    protected Document getDocument() {
        return doc;
    }

    private XPath getXPath() throws XPathExpressionException {
        check();

        return XPathFactory.newInstance().newXPath();
    }

    private void check() throws XPathExpressionException {
        if (doc == null)
            throw new XPathExpressionException("Document is null");
    }

}
