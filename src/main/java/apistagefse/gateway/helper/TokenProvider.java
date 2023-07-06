package apistagefse.gateway.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import apistagefse.base.UniqueId;
import apistagefse.gateway.data.DataSign;
import apistagefse.gateway.data.ParamToken;
import apistagefse.gateway.imported.dispatcher.enums.IniActivityEnum;
import apistagefse.gateway.imported.dispatcher.enums.PurposeOfUseEnum;
import apistagefse.CdaExtractor;

import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;

public class TokenProvider {

    public static ParamToken forValidation(CdaValidator cda) throws Exception {
        ParamToken paramToken = init(makeCreateParam(), cda.getExtractor());
        paramToken.setPdfFile(cda.getSourceAsString());

        return paramToken;
    }


    public static ParamToken forUpdateDocument(CdaValidator cda) throws Exception {
        ParamToken paramToken = init(makeCreateParam(), cda.getExtractor());
        paramToken.setPdfFile(cda.getSourceAsString());

        return paramToken;
    }

    public static ParamToken forUpdateMetaData(CdaValidator cda) throws Exception {
        ParamToken paramToken = init(makeUpdateParam(), cda.getExtractor());
        paramToken.setPdfFile(cda.getSourceAsString());

        return paramToken;
    }

    public static ParamToken forDelete(CdaValidator cda) throws Exception {
        ParamToken paramToken = init(makeDeleteParam(), cda.getExtractor());
        paramToken.setPdfFile(cda.getSourceAsString());

        return paramToken;
    }

    public static ParamToken fromCDA(CdaExtractor cda) throws IOException, XPathExpressionException {
//        return getParamToken(cda);
        return null;
    }

    public static ParamToken makeCreateParam() {
        return makeParamToken(IniActivityEnum.CREATE, PurposeOfUseEnum.TREATMENT);
    }

    public static ParamToken makeUpdateParam() {
        return makeParamToken(IniActivityEnum.UPDATE, PurposeOfUseEnum.UPDATE);
    }

    public static ParamToken makeDeleteParam() {
        return makeParamToken(IniActivityEnum.DELETE, PurposeOfUseEnum.UPDATE);
    }

    private static ParamToken init(ParamToken param, CdaExtractor cda) throws XPathExpressionException {
        String docType = "('" + cda.getTypeCode() + "^^" + cda.getTypeCode("codeSystem") + "')";
        String personId = cda.getAssistedCode();
        String autority = cda.getAuthorFiscalCode();

        param.setDocType(docType);
        param.setPersonId(personId);
        param.setAutority(autority);
        param.setPdfFile("");

        return param;
    }

    private static ParamToken makeParamToken(IniActivityEnum activity, PurposeOfUseEnum purpose) {
        ParamToken param = new ParamToken();
        param.setAction_id(activity.toString());
        param.setPurpose_of_use(purpose.toString());

        return param;
    }

    public static String getFileDataSign(ParamToken paramToken, File parent, String assigningAuthority) throws Exception {
//        JSONParser parser = new JSONParser();
//
//        Object obj = parser.parse(new FileReader(paramToken.getFileBaseSign()));
//        Gson g = new Gson();
//
//        DataSign dataSign = g.fromJson(obj.toString(), DataSign.class);

        DataSign dataSign = JsonHelper.from(new File(paramToken.getFileBaseSign()), DataSign.class);

        dataSign.setSub(paramToken.getAutority() + "^^^&2.16.840.1.113883.2.9.4.3.2&ISO");
       // dataSign.setSub(PatientIdHelper.getIdentifiableAsString(
       //     PatientIdHelper.getIdentifiable(paramToken.getAutority(), assigningAuthority)));
        dataSign.setJti(String.valueOf(new UniqueId().random(10000000, 90000000)));
        dataSign.setAction_id(paramToken.getAction_id());
        dataSign.setPurpose_of_use(paramToken.getPurpose_of_use());
        dataSign.setResource_hl7_type(paramToken.getDocType());
        dataSign.setPerson_id(paramToken.getPersonId() + "^^^&2.16.840.1.113883.2.9.4.3.2&ISO");
        //dataSign.setPerson_id(PatientIdHelper.getIdentifiableAsString(
          //  PatientIdHelper.getIdentifiable(paramToken.getPersonId(), assigningAuthority)));

        //*******attenzione il path dei cerficati andrebbe fissato perche cambia in base all' ambiente

        dataSign.setPem_path(System.getProperty("user.dir")+"/"+dataSign.getPem_path());
        dataSign.setP12_path(System.getProperty("user.dir")+"/"+dataSign.getP12_path());

      //  dataSign.setPem_path(new File(parent, dataSign.getPem_path()).getAbsolutePath());
      //  dataSign.setP12_path(new File(parent, dataSign.getP12_path()).getAbsolutePath());

        ObjectMapper mapper = new ObjectMapper();

        File ffile = new File(parent, "sign" + new UniqueId().generateUuid() + ".json");

        try {
            mapper.writeValue(ffile, dataSign);

            return ffile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //delete external before token gen
            //ffile.delete();
        }

        return null;
    }

}
