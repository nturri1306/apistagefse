package apistagefse.base.stream;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author b.amoruso
 */
public class StreamDigest {

    private static final int HASH_BUFSIZE = 16384;
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    private InputStream is;

    /**
     * @param is InputStream for which compute hash value
     */
    public StreamDigest(InputStream is) {
        this.is = is;
    }

    /**
     * @param file
     * @throws FileNotFoundException
     */
    public StreamDigest(File file) throws FileNotFoundException {
        this(new FileInputStream(file));
    }

    /**
     * @param bytes
     */
    public StreamDigest(byte[] bytes) {
        this(new ByteArrayInputStream(bytes));
    }

    public static String encodeHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];

        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }

        return new String(hexChars);
    }

    /**
     * Calculates the MD5 of the given content stream.
     *
     * @return the MD5.
     * @throws IOException
     */
    public String md5() throws IOException {
        return hash("MD5");
    }

    /**
     * Calculates the SHA-1 of the given content stream.
     *
     * @return the SHA-1.
     * @throws IOException
     */
    public String sha1() throws IOException {
        return hash("SHA-1");
    }

    /**
     * Calculates the SHA-256 of the given content stream.
     *
     * @return the SHA-256.
     * @throws IOException
     */
    public String sha256() throws IOException {
        return hash("SHA-256");
    }

    /**
     * Calculates the SHA-512 of the given content stream.
     *
     * @return the SHA-512.
     * @throws IOException
     */
    public String sha512() throws IOException {
        return hash("SHA-512");
    }

    /**
     * Generate Hash value of passed file by using <code>IndexProducer</code> algorithm
     * Test computed hash on https://defuse.ca/checksums.htm
     *
     * @param algorithm is the hash algorithm
     * @return a string representing the hash value
     * @throws IOException an error occurs during hash computing
     */
    private String hash(String algorithm) throws IOException {
        // Load message digest by using specific algorithm...
        MessageDigest digest = null;

        try {
            digest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException ex) {
            throw new IOException(ex);
        }

        // Load specific stream....
        try (BufferedInputStream in = new BufferedInputStream(is)) {
            byte[] buffer = new byte[HASH_BUFSIZE];
            int sizeRead = -1;
            while ((sizeRead = in.read(buffer)) != -1) {
                digest.update(buffer, 0, sizeRead);
            }

            // Compute hash string...
            byte[] hash = new byte[digest.getDigestLength()];
            hash = digest.digest();

            return new String(encodeHex(hash));
        }
    }

}
