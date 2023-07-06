package apistagefse.gateway.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

/**
 * @author b.amoruso
 */
public class JsonHelper {

    public static <T> T from(String content, Class<T> result) throws JsonProcessingException {
        return new ObjectMapper().readValue(content, result);
    }

    public static <T> T from(File file, Class<T> result) throws IOException {
        return new ObjectMapper().readValue(file, result);
    }

    public static String toString(Object body) throws JsonProcessingException {
        return toString(body, false);
    }

    public static String toString(Object body, boolean pretty) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        return pretty ? mapper.writerWithDefaultPrettyPrinter().writeValueAsString(body) : mapper.writeValueAsString(body);
    }

    public static void flush(Object object, File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(file, object);
    }

}
