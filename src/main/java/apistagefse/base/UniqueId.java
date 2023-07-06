package apistagefse.base;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author b.amoruso
 */
public class UniqueId {

    public static final String PREFIX = "urn:uuid:";

    private static final String UUID_PATTERN =
            "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}";

    private int counter;
    private Random r = new Random();
    private String uuid;

    /**
     * Create a new instance of <code>UniqueId</code>
     */
    public UniqueId() {
        this(null);
    }

    /**
     * Create a new instance of <code>UniqueId</code>
     * and initialize it on passed UUID
     *
     * @param uuid to initialize the instance
     */
    public UniqueId(String uuid) {
        this.uuid = uuid;
    }

    /**
     * Extract unique id from Content-Id of a SOAP attachment.
     * Standard format is:
     * "rootpart*"+" globally unique value "+"@"+"jaxws.cosminexus.com"
     * A "cid" URL is converted to the corresponding Content-ID message header by
     * removing the "cid:" prefix (ref. RFC 2392)
     *
     * @param contentId
     * @param prefix    to place before extracted UUID (urn:uuid).
     * @return extracted id
     */
    public static String fromContentId(String contentId, boolean prefix) {
        String id = contentId.toLowerCase();
        id = id.replaceAll(".*cid:", "");
        id = id.replaceAll(".*" + PREFIX, "");
        id = id.replaceAll("@.*", "");

        return (prefix ? PREFIX : "") + id;
    }

    /**
     * Generate a UUID (Universally Unique Identifier), also known as
     * GUID (Globally Unique Identifier) that represents a 128-bit long
     * value which guarantees uniqueness across time and space
     * (e.g. 123e4567-e89b-12d3-a456-556642440000) without any prefix
     *
     * @return generated UUID with hex digits
     */
    public String generateUuid() {
        return generateUuid(false);
    }

    /**
     * Generate a UUID (Universally Unique Identifier), also known as
     * GUID (Globally Unique Identifier) that represents a 128-bit long
     * value which guarantees uniqueness across time and space
     * (e.g. 123e4567-e89b-12d3-a456-556642440000).
     *
     * @param prefix to place before generated UUID (urn:uuid).
     * @return generated UUID with hex digits
     */
    public String generateUuid(boolean prefix) {
        return last((prefix ? PREFIX : "") + UUID.randomUUID().toString());
    }

    /**
     * Generate a custom unique id by using current time-stamp and a
     * progressive number dot separated (e.g 1.40.20180319151540.1).
     *
     * @param prefix to place before generated UUID.
     * @return generated unique id
     */
    public String timestamped(String prefix) {
        return last(prefix + "." + getTimestamp() + "." + ++counter);
    }

    /**
     * Get last generated UUID
     *
     * @param prefix if the UUID will be returned with prefix
     *                (urn:uuid) or not
     * @return last generated UUID with or without prefix
     */
    public String getLastUuid(boolean prefix) {
        if (uuid == null)
            return null;

        boolean hasPrefix = uuid.toLowerCase().startsWith(PREFIX);

        if (prefix && !hasPrefix)
            uuid = PREFIX + uuid;
        else if (!prefix && hasPrefix)
            uuid = uuid.replace(PREFIX, "");

        return uuid;
    }

    /**
     * Add <code>urn:uuid:</code> to specified identifier, also if it isn't a UUID
     *
     * @param id the identifier to enrich
     * @return the enrich identifier
     */
    public String withPrefix(String id) {
        return !id.toLowerCase().startsWith(PREFIX) ? (PREFIX + id) : id;
    }

    /**
     * Check if last identifier is a valid UUID
     *
     * @return true if last is a valid UUID, false otherwise (e.g. a custom unique id)
     */
    public boolean isUuid() {
        return isUuid(uuid);
    }

    /**
     * Check if specified identifier is a UUID
     *
     * @param id the identifier to check
     * @return true if it is a UUID, false otherwise
     */
    public boolean isUuid(String id) {
        Pattern r = Pattern.compile(UUID_PATTERN);
        Matcher m = r.matcher(id);

        return m.find();
    }

    /**
     * Remove every dash character from passed UUID
     *
     * @param prefix if the UUID will be returned with prefix (urn:uuid) or not
     * @return last generated UUID with or without prefix and no dashs
     */
    public String getLastNoDashUuid(boolean prefix) {
        String last = getLastUuid(prefix);
        if (last != null)
            last = last.replaceAll("-", "");

        return last;
    }

    /**
     * Returns an effectively pseudorandom int value, conforming to the
     * given lower (inclusive) and upper (exclusive).
     *
     * @param lower the origin (inclusive) of the random value
     * @param upper the bound (exclusive) of the random value
     * @return a pseudorandom int value
     */
    public int random(int lower, int upper) {
        return r.ints(lower, (upper + 1)).limit(1).findFirst().getAsInt();
    }

    private String last(String uuid) {
        return this.uuid = uuid;
    }

    private String getTimestamp() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

}
