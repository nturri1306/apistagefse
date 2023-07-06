package apistagefse.gateway.data;

import java.util.HashMap;


/**
 * @author n.turri
 */
public class SchemaMap {

    static HashMap<String,String> maps = new HashMap<>();

/*
* R>                                schematronFSE
26/01/2023  10:41    <DIR>          schematronLDO
26/01/2023  10:41    <DIR>          schematronPSS
26/01/2023  10:41    <DIR>          schematronRAD
26/01/2023  10:41    <DIR>          schematronRSA
26/01/2023  10:41    <DIR>          schematronSinVACC
26/01/2023  10:41    <DIR>          schematronVACC
26/01/2023  10:41    <DIR>          schematronVPS
* */
    public static   HashMap<String,String> getSchemaMap()
    {
        maps.put("LAB","schematronFSE_LAB");
        maps.put("LDO","schematronFSE_LDO");
        maps.put("RAD","schematronFSE_RAD");
        maps.put("PSS","schematron_PSS");
        maps.put("RSA","schematron_RSA");
        maps.put("VPS","schematron_VPS");
        maps.put("SINVACC","schematron_singola_VACC");
        maps.put("VACC","schematron_certificato_VACC");
        return  maps;
    }

}
