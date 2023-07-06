package apistagefse.route.rest.basic;


import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class maps a plain user structure composed by:
 * - hostname/IP address
 * - username
 * - password
 *
 * @author b.amoruso
 */
public class PlainUser {

    private String host;
    private String username;
    private String password;

    /**
     * Create a new <code>PlainUser</code> from an array of (three) strings.
     * First string represents the hostname, second one the username and
     * the third the password
     *
     * @param user is the array of (three) strings.
     */
    public PlainUser(String[] user) {
        int index = 0;

        this.host = user.length > index ? user[index++].trim() : "";
        this.username = user.length > index ? user[index++].trim() : "";
        this.password = user.length > index ? user[index++].trim() : "";
    }

    /**
     * Return the hostname/IP address associated to the user
     *
     * @return the hostname/IP address
     */
    public String getHost() {
        return host;
    }

    /**
     * Return the username associated to the user
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Return the password associated to the user
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Check specified username/password pair against this object
     * @param username to check
     * @param password to check
     * @return true if username/password pair is equals to this object, false otherwise
     */
    public boolean check(String username, String password) {
        return this.username.equals(username) &&
                this.password.equals(password);
    }

    @Override
    public String toString() {
        return username + "@" + host;
    }

    /**
     * Convert a collection of raw users into a collection of <code>PlainUser</code> objects
     *
     * @param items to convert
     * @return a collection of <code>PlainUser</code> objects
     */
    public static Collection<PlainUser> toListOfUser(Collection<String> items) {
        return items.stream().map(
                t -> new PlainUser(t.split(","))).collect(Collectors.toList());
    }

    /**
     * Check a user extracted from a BASIC AUTHENTICATION (e.g. "Basic 5tyc0uiDat4") against
     * a collection of <code>PlainUser</code> objects
     *
     * @param users is the collection of <code>PlainUser</code> objects
     * @param host to check
     * @param auth the string in the BASIC AUTHENTICATION format
     * @return true if user extracted from the BASIC AUTHENTICATION string is present (and valid)
     * in the collection of <code>PlainUser</code> objects, false otherwise
     */
    public static boolean authenticate(Collection<PlainUser> users, String host, String auth) {
        // Header is in the format "Basic 5tyc0uiDat4"
        // We need to extract data before decoding it back to original string
        String[] authParts = auth != null ? auth.split("\\s+") : new String[0];

        if (authParts.length > 1) {
            String authInfo = authParts[1];
            // Decode the data back to original string
            byte[] bytes = Base64.getDecoder().decode(authInfo);

           //List<PlainUser> registered = users.stream().filter(
                 ///   u -> u.getHost().equals(host)).collect(Collectors.toList());

            List<PlainUser> registered = users.stream().toList();

            for (PlainUser u : registered) {
                String[] decoded = new String(bytes).split(":");

                if (decoded.length > 1 && u.check(decoded[0], decoded[1]))
                    return true;
            }
        }

        return false;
    }

}
