package apistagefse.gateway.data;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum SchemaCode {

    /*
  60591-5 Profilo Sanitario Sintetico
  11502-2 Referto di Laboratorio
  57829-4 Prescrizione per prodotto o apparecchiature mediche

 34105-7 Lettera di dimissione ospedaliera
 18842-5 Lettera di dimissione non ospedaliera

 59258-4 Verbale di pronto soccorso

 68604-8 Referto di radiologia
 11526-1 Referto di anatomia patologica

 59284-0 Documento dei consensi
 28653-4 Certificato di malattia
 57832-8 Prescrizione diagnostica o specialistica

 29304-3 Erogazione farmaceutica
 11488-4 Referto specialistico

 57827-8 Esenzione da reddito
 81223-0 Erogazione specialistica

 18776-5 Piano terapeutico

 97500-3 Certificazione verde Covid-19 (Digital Green Certificate)1
 87273-9 Scheda singola vaccinazione

 82593-5 Certificato vaccinale
 97499-8 Certificato di guarigione da Covid-19
    * */


    CODE_60591_5("60591-5", "LAB", "schematronFSE_LAB"),

    CODE_57829_4("57829-4", "LAB", "schematronFSE_LAB"),

    CODE_11502_2("11502-2", "LAB", "schematronFSE_LAB"),

    CODE_34105_7("34105-7", "LDO", "schematronFSE_LDO"),

    CODE_18842_5("18842-5", "LDO", "schematronFSE_LDO"),

    CODE_68604_8("68604-8", "RAD", "schematronFSE_RAD"),

    CODE_11526_1("11526-1", "RAD", "schematronFSE_RAD"),

    CODE_11488_4("11488-4", "RSA", "schematron_RSA"),

    CODE_81223_0("81223-0", "RSA", "schematron_RSA"),

    CODE_59258_4("59258-4", "VPS", "schematron_VPS"),

    CODE_97500_3("97500-3", "SINVACC", "schematron_singola_VACC"),

    CODE_87273_9("87273-9", "SINVACC", "schematron_singola_VACC"),

    CODE_82593_5("82593-5", "VACC", "schematron_certificato_VACC"),

    CODE_97499_8("97499-8", "VACC", "schematron_certificato_VACC");





    private final String code;
    private final String documentType;
    private final String schematron;

    public static SchemaCode getByCode(String code) {
        for (SchemaCode documentTypeEnum : SchemaCode.values()) {
            if (documentTypeEnum.getCode().equals(code)) {
                return documentTypeEnum;
            }
        }
        return null;
    }

    public static SchemaCode getDocumentType(String documentType) {
        for (SchemaCode documentTypeEnum : SchemaCode.values()) {
            if (documentTypeEnum.documentType.equals(documentType)) {
                return documentTypeEnum;
            }
        }
        return null;
    }

}
