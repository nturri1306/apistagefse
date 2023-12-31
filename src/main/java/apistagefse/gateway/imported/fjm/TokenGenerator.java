package apistagefse.gateway.imported.fjm;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


/**
 * Copyright (c) 2022, Ministero della Salute
 * <p>
 * original is Launcher class.
 */
public class TokenGenerator {

    static final Logger LOGGER = Utility.getLogger(TokenGenerator.class.getName());

    static String jsonData = null;
    static String pathFileToPublish = null;
    static String aliasP12 = null;
    static char[] pwdP12 = null;
    static Integer nHour = 24;
    static boolean flagMalformedInput = false;
    static boolean flagNeedHelp = false;
    static boolean flagVerbose = false;
    static boolean flagValidation = false;

    private String jwt;

    private String claimsJwt;


    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getClaimsJwt() {
        return claimsJwt;
    }

    public void setClaimsJwt(String claimsJwt) {
        this.claimsJwt = claimsJwt;
    }


    /**
     * Main method.
     *
     * @param args arguments
     */


    public void execute(String[] args) {
        LOGGER.info(" _____  _____  ___       __  _ _ _  _____    _____       _             ");
        LOGGER.info("|   __||   __||_  |   __|  || | | ||_   _|  |     | ___ | |_  ___  ___ ");
        LOGGER.info("|   __||__   ||  _|  |  |  || | | |  | |    | | | || .'|| '_|| -_||  _|");
        LOGGER.info("|__|   |_____||___|  |_____||_____|  |_|    |_|_|_||__,||_,_||___||_|  \n");

        try {

            checkArgs(args);

            if (flagNeedHelp) {
                showHelp(LOGGER);
            } else if (flagMalformedInput || Utility.nullOrEmpty(jsonData) || Utility.nullOrEmpty(aliasP12)
                    || pwdP12 == null) {
                LOGGER.info(
                        "Please check for malformed input; please remember that alias p12, password p12 and json data are mandatory.");
            } else {
                buildToken();
            }
        } catch (Exception e) {
            LOGGER.info("An error occur while trying to generate JWT, hope this can help:");
            LOGGER.info(String.format("EXCEPTION: ", e.getMessage()));
            LOGGER.info(ExceptionUtils.getStackTrace(e));
        }
    }


    private static void checkArgs(String[] args) {

        for (int i = 0; i < args.length; ) {
            String key = args[i].toLowerCase();
            ArgumentEnum arg = ArgumentEnum.getByKey(key);

            if (arg == null) {
                flagMalformedInput = true;
                break;
            } else {
                if (arg.getFlagHasValue()) {
                    String value = null;
                    if (i + 1 < args.length) {
                        value = args[i + 1];
                    } else {
                        flagMalformedInput = true;
                        break;
                    }

                    checkValueArg(arg, value);

                    i += 2;
                } else {
                    i++;
                    checkNoValueArg(arg);
                }
            }
        }

    }

    private static void checkValueArg(ArgumentEnum arg, String value) {

        if (ArgumentEnum.JSON_DATA.equals(arg)) {
            jsonData = value;
        } else if (ArgumentEnum.DURATION_JWT.equals(arg)) {
            nHour = Integer.valueOf(value);
        } else if (ArgumentEnum.FILE_TO_PUBLISH.equals(arg)) {
            pathFileToPublish = value;
        } else if (ArgumentEnum.P12_ALIAS.equals(arg)) {
            aliasP12 = value;
        } else if (ArgumentEnum.P12_PWD.equals(arg)) {
            pwdP12 = value.toCharArray();
        }

    }


    private static void checkNoValueArg(ArgumentEnum arg) {
        if (ArgumentEnum.HELP_MODE.equals(arg)) {
            flagNeedHelp = true;
        } else if (ArgumentEnum.VERBOSE_MODE.equals(arg)) {
            flagVerbose = true;
        } else if (ArgumentEnum.VALIDATION_MODE.equals(arg)) {
            flagValidation = true;
        }
    }


    @SuppressWarnings("unchecked")
    private void buildToken() throws Exception {

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        Map<String, String> mapJD = new Gson().fromJson(new String(Utility.getFileFromFS(jsonData)), Map.class);

        byte[] privateKeyP12 = Utility.getFileFromFS(get(mapJD, JWTAuthEnum.P12_PATH));
        Key privateKey = Utility.extractKeyByAliasFromP12(pwdP12, aliasP12, privateKeyP12);

        byte[] pem = Utility.getFileFromFS(get(mapJD, JWTAuthEnum.PEM_PATH));

        String cleanedPEM = new String(pem)
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PUBLIC KEY-----", "")
                .replace("-----BEGIN CERTIFICATE-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END CERTIFICATE-----", "")
                .replace("\n", "");

        String publicKey = cleanedPEM;

        dumpVerboseMsg(flagVerbose, "Analyzing data\n");
        String iss = get(mapJD, JWTAuthEnum.ISS);
        Date iat = new Date();
        Date exp = Utility.addHoursToJavaUtilDate(iat, nHour);

        dumpVerboseMsg(flagVerbose, "Issued At Time: " + iat);
        dumpVerboseMsg(flagVerbose, "EXPiration time: " + exp);
        if (privateKey != null) {
            dumpVerboseMsg(flagVerbose, "Private key founded.");
        } else {
            dumpVerboseMsg(flagVerbose, "Private key NOT FOUNDED!");
        }
        if (!Utility.nullOrEmpty(publicKey)) {
            dumpVerboseMsg(flagVerbose, "Public key founded.");
        } else {
            dumpVerboseMsg(flagVerbose, "Public key NOT FOUNDED!");
        }
        Integer nDataItems = 0;
        if (mapJD.size() > 0) {
            nDataItems = mapJD.size();
        }
        dumpVerboseMsg(flagVerbose, "Json data items founded: " + nDataItems + ".\n");


        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(mapJD);

        json = json.replaceAll("\\r", "");

        LOGGER.info(json);

        LOGGER.info("private key:" + privateKey);
        LOGGER.info("public key: " + publicKey);
        LOGGER.info("" + iat);
        LOGGER.info("" + exp);
        LOGGER.info(iss);


        String _jwt = generateAuthJWT(mapJD, privateKey, publicKey, iat, exp, iss);
        String _claimsJwt = generateClaimsJWT(mapJD, privateKey, publicKey, iat, exp, iss, pathFileToPublish);

        setJwt(_jwt);
        setClaimsJwt(_claimsJwt);

        dumpVerboseMsg(flagVerbose, "Generating Authorization Bearer Token\n");
        dumpVerboseMsg(flagVerbose, "AUTHORIZATION BEARER TOKEN START HERE");
        LOGGER.info("------------- Authorization Bearer Token ---------------\n");
        LOGGER.info(jwt);


        dumpVerboseMsg(flagVerbose, "AUTHORIZATION BEARER TOKEN END HERE\n");

        dumpVerboseMsg(flagVerbose, "Generating FSE-JWT-Signature\n");
        dumpVerboseMsg(flagVerbose, "FSE-JWT-SIGNATURE START HERE");
        LOGGER.info("\n------------- FSE-JWT-Signature ---------------\n");
        LOGGER.info(claimsJwt);

        LOGGER.info("\n");
        dumpVerboseMsg(flagVerbose, "FSE-JWT-SIGNATURE END HERE\n");

        if (flagValidation) {
            // Authorization Token Validation
            LOGGER.info("Validating Authorization Token\n");
            Jws<Claims> token = parse(jwt, privateKey);
            LOGGER.info("HEADER: " + token.getHeader());
            LOGGER.info("BODY: " + token.getBody());
            boolean outValidation = validate(jwt, pem);
            String signatureStatus = "VALID";
            if (!outValidation) {
                signatureStatus = "NOT VALID";
            }
            LOGGER.info("SIGNATURE: " + signatureStatus + "\n");

            // Claims Token Validation
            LOGGER.info("Validating Claims Token\n");
            Jws<Claims> claimsToken = parse(claimsJwt, privateKey);
            LOGGER.info("HEADER: " + claimsToken.getHeader());
            LOGGER.info("BODY: " + claimsToken.getBody());
            boolean outValidationClaimsToken = validate(claimsJwt, pem);
            String signatureStatusClaimsToken = "VALID";
            if (!outValidationClaimsToken) {
                signatureStatusClaimsToken = "NOT VALID";
            }
            LOGGER.info("SIGNATURE: " + signatureStatusClaimsToken + "\n");

        }

    }

    /**
     * Show help info.
     */
    private static void showHelp(final Logger logger) {

        logger.info("NAME");
        logger.info("\t\tFS2 JWT Maker (fjm) - JWT generator for FS2 Gateway\n");
        logger.info("SYNOPSIS");
        logger.info(
                "\t\tjava -jar fjm -d JSON_DATA_FILE -a ALIAS_P12 -p PASSWORD_P12 [-f PDF_FILE_TO_PUBLISH] [-t TOKEN_DURATION] [-v] [-x] [-h]");
        logger.info("");
        logger.info("DESCRIPTION");
        logger.info("\t\tGenerate JWT for FS2 Gateway");
        logger.info("");
        logger.info("\t\tArguments:");
        for (ArgumentEnum ae : ArgumentEnum.values()) {
            logger.info("");
            logger.info("\t\t" + ae.getKey() + "\t" + ae.getDescription());
        }
    }

    /**
     * Get argument value from map.
     *
     * @param mapJD map
     * @param jdk   argument
     * @return value
     */
    private static String get(Map<String, String> mapJD, JWTAuthEnum jdk) {
        return mapJD.get(jdk.getKey());
    }

    /**
     * Generate JWT.
     *
     * @param mapJD             arguments map
     * @param privateKey        private key
     * @param x5c               public key
     * @param iat               issuing time
     * @param exp               expiring time
     * @param pathFileToPublish file to hash
     * @return jwt
     * @throws Exception
     */
    private static String generateAuthJWT(Map<String, String> mapJD, Key privateKey, String x5c, Date iat, Date exp, String iss) throws Exception {
        Map<String, Object> headerParams = new HashMap<>();
        headerParams.put(JWTAuthEnum.ALG.getKey(), SignatureAlgorithm.RS256);
        headerParams.put(JWTAuthEnum.TYP.getKey(), JWTAuthEnum.JWT.getKey());
        headerParams.put(JWTAuthEnum.X5C.getKey(), Arrays.asList(x5c).toArray());

        Map<String, Object> claims = new HashMap<>();
        for (JWTAuthEnum k : JWTAuthEnum.values()) {
            if (k.getAutoFlagPayloadClaim()) {
                claims.put(k.getKey(), mapJD.get(k.getKey()));
            }
        }
        claims.put(JWTAuthEnum.IAT.getKey(), iat.getTime() / 1000);
        claims.put(JWTAuthEnum.EXP.getKey(), exp.getTime() / 1000);
        claims.put(JWTAuthEnum.ISS.getKey(), "auth:" + iss);

        return Jwts.builder().setHeaderParams(headerParams).setClaims(claims).signWith(SignatureAlgorithm.RS256, privateKey).compact();
    }

    /**
     * Generate Claims JWT.
     *
     * @param mapJD             arguments map
     * @param privateKey        private key
     * @param x5c               public key
     * @param iat               issuing time
     * @param exp               expiring time
     * @param pathFileToPublish file to hash
     * @return jwt
     * @throws Exception
     */
    private static String generateClaimsJWT(Map<String, String> mapJD, Key privateKey, String x5c, Date iat, Date exp, String iss, String pathFileToPublish) throws Exception {
        Map<String, Object> headerParams = new HashMap<>();
        headerParams.put(JWTClaimsEnum.ALG.getKey(), SignatureAlgorithm.RS256);
        headerParams.put(JWTClaimsEnum.TYP.getKey(), JWTClaimsEnum.JWT.getKey());
        headerParams.put(JWTClaimsEnum.X5C.getKey(), Arrays.asList(x5c).toArray());

        Map<String, Object> claims = new HashMap<>();
        for (JWTClaimsEnum k : JWTClaimsEnum.values()) {
            if (k.getAutoFlagPayloadClaim()) {
                claims.put(k.getKey(), mapJD.get(k.getKey()));
            }
        }
        claims.put(JWTClaimsEnum.PATIENT_CONSENT.getKey(), true);
        claims.put(JWTClaimsEnum.IAT.getKey(), iat.getTime() / 1000);
        claims.put(JWTClaimsEnum.EXP.getKey(), exp.getTime() / 1000);
        claims.put(JWTAuthEnum.ISS.getKey(), "integrity:" + iss);


        claims.put("subject_application_id", mapJD.get("subject_application_id"));
        claims.put("subject_application_vendor", mapJD.get("subject_application_vendor"));
        claims.put("subject_application_version", mapJD.get("subject_application_version"));


        if (!Utility.nullOrEmpty(pathFileToPublish)) {
            byte[] fileToHash = Utility.getFileFromFS(pathFileToPublish);
            if (Utility.isPdf(fileToHash)) {
                String hash = Utility.encodeSHA256(fileToHash);
                claims.put(JWTClaimsEnum.ATTACHMENT_HASH.getKey(), hash);
            }
        }

        return Jwts.builder().setHeaderParams(headerParams).setClaims(claims)
                .signWith(SignatureAlgorithm.RS256, privateKey).compact();
    }


    /**
     * Validate JWT.
     *
     * @param jwt jwt to validate
     * @param pem perm certificate
     * @return flag
     */
    private static boolean validate(String jwt, byte[] pem) {
        boolean out = true;
        try {
            final RSAPublicKey key = getPublicKey(pem);
            final Algorithm algorithm = Algorithm.RSA256(key, null);
            final DecodedJWT decodedJWT = JWT.decode(jwt);
            algorithm.verify(decodedJWT);
        } catch (Exception e) {
            out = false;
        }
        return out;
    }

    /**
     * Get public key.
     *
     * @param pem pem
     * @return public key
     * @throws Exception
     */
    public static RSAPublicKey getPublicKey(byte[] pem) throws Exception {
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        InputStream in = new ByteArrayInputStream(pem);
        X509Certificate certificate = (X509Certificate) certFactory.generateCertificate(in);

        return (RSAPublicKey) certificate.getPublicKey();
    }

    /**
     * Dump message if in verbose mode.
     *
     * @param flagVerbose flag verbose mode
     * @param msg         messagge
     */
    private static void dumpVerboseMsg(boolean flagVerbose, String msg) {
        if (flagVerbose) {
            LOGGER.info(msg);
        }
    }

    /**
     * Parse JWT Token.
     *
     * @param token   String
     * @param private key
     * @return token JWT
     */
    private static Jws<Claims> parse(String token, Key privateKey) {
        return Jwts.parser().setSigningKey(privateKey).parseClaimsJws(token);
    }

}

