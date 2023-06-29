package com.qazima.habari.plugin.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpStatus;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.util.stream.Collectors.*;

/**
 * <p>
 * Abstract class Plugin used to create new plugin for Habari.
 *
 * @author Clement BONET
 * @version 1.0.0
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "connectionType")
@JsonPropertyOrder({"connectionType", "configuration"})
@JsonTypeName("com.qazima.habari.plugin.core.Plugin")
public abstract class Plugin {
    @Getter
    @Setter
    @JsonProperty("configuration")
    private Configuration configuration;
    @Getter
    @Setter
    @JsonProperty("connectionType")
    private String connectionType;

    /**
     * <p>
     * A string test to assert that the string is null or empty.
     *
     * @param string The string to test.
     * @return True if the string is null or empty, false otherwise.
     * @author Clement BONET
     * @version 1.0.0
     */
    protected boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    /**
     * <p>
     * A string test to assert that the string is null or contains only spaces (character 0x20).
     *
     * @param string The string to test.
     * @return True if the string is null or contains only spaces (character 0x20), false otherwise.
     * @author Clement BONET
     * @version 1.0.0
     */
    protected boolean isNullOrWhiteSpace(String string) {
        return isNullOrEmpty(string) || string.trim().isEmpty();
    }

    /**
     * <p>
     * Convert the parameters from a query string from a post/put call into a map of key/list of values.
     * <p>
     * <b>IE:</b> the query string: <code>[...]/index.html?param1=value1&amp;param2=value2&amp;param2=value3&amp;param3&amp;param4=</code>
     * will produce the following map with the values.
     * <ul>
     *     <li>param1
     *         <ul>
     *             <li>value1</li>
     *         </ul>
     *     </li>
     *     <li>param2
     *         <ul>
     *             <li>value2</li>
     *             <li>value3</li>
     *         </ul>
     *     </li>
     *     <li>param3
     *         <ul>
     *             <li><i>no value</i></li>
     *         </ul>
     *     </li>
     *     <li>param4
     *         <ul>
     *             <li><i>no value</i></li>
     *         </ul>
     *     </li>
     * </ul>
     *
     * @param parameters the query string to convert.
     * @return The computed map.
     * @throws IOException Any I/O exception raised by the transformation.
     * @author Clement BONET
     * @version 1.0.0
     */
    protected Map<String, List<String>> splitRequestBody(String parameters) throws IOException {
        Map<String, String> temp = Collections.emptyMap();
        Map<String, List<String>> result = Collections.emptyMap();
        if (!isNullOrWhiteSpace(parameters)) {
            ObjectMapper mapper = new ObjectMapper();
            temp = mapper.readValue(parameters, Map.class);
        }

        for (Map.Entry<String, String> element : temp.entrySet()) {
            if (!result.containsKey(element.getKey())) {
                result.put(element.getKey(), new ArrayList<>());
            }
            result.get(element.getKey()).add(element.getValue());
        }

        return result;
    }

    /**
     * <p>
     * Convert the parameters from a query string from a get call into a map of key/list of values.
     * <p>
     * <b>IE:</b> the query string: <code>[...]/index.html?param1=value1&amp;param2=value2&amp;param2=value3&amp;param3&amp;param4=</code>
     * will produce the following map with the values.
     * <ul>
     *     <li>param1
     *         <ul>
     *             <li>value1</li>
     *         </ul>
     *     </li>
     *     <li>param2
     *         <ul>
     *             <li>value2</li>
     *             <li>value3</li>
     *         </ul>
     *     </li>
     *     <li>param3
     *         <ul>
     *             <li><i>no value</i></li>
     *         </ul>
     *     </li>
     *     <li>param4
     *         <ul>
     *             <li><i>no value</i></li>
     *         </ul>
     *     </li>
     * </ul>
     *
     * @param parameters the query string to convert.
     * @return The computed map.
     * @author Clement BONET
     * @version 1.0.0
     */
    protected Map<String, List<String>> splitQuery(String parameters) {
        if (isNullOrWhiteSpace(parameters)) {
            return Collections.emptyMap();
        }
        return Arrays.stream(parameters.split("&"))
                .map(this::splitQueryParameter)
                .collect(groupingBy(AbstractMap.SimpleImmutableEntry::getKey, LinkedHashMap::new, mapping(Map.Entry::getValue, toList())));
    }

    /**
     * <p>
     * Convert a couple <code>param=value</code> into a entry key, value
     * to store it in the map.
     * <p>
     * <b>IE:</b>
     * <ul>
     *     <li>
     *         <code>param=value</code> will return key=param and value=value
     *     </li>
     *     <li>
     *         <code>param</code> will return key=param and value=null
     *     </li>
     *     <li>
     *         <code>param=</code> will return key=param and value=null
     *     </li>
     * </ul>
     *
     * @param it The parameter to compute.
     * @return The computed map entry.
     * @author Clement BONET
     * @version 1.0.0
     */
    protected AbstractMap.SimpleImmutableEntry<String, String> splitQueryParameter(String it) {
        final int idx = it.indexOf("=");
        final String key = idx > 0 ? it.substring(0, idx) : it;
        final String value = idx > 0 && it.length() > idx + 1 ? it.substring(idx + 1) : null;
        return new AbstractMap.SimpleImmutableEntry<>(
                URLDecoder.decode(key, StandardCharsets.UTF_8),
                URLDecoder.decode(value, StandardCharsets.UTF_8)
        );
    }

    /**
     * <p>
     * Process the standard query from uri configured in the standard field.
     *
     * @param httpExchange The current http exchange.
     * @param content The content from the previous call and the content given to the following one.
     * @return The http status code.
     * @author Clement BONET
     * @version 1.0.0
     */
    public int process(HttpExchange httpExchange, Content content) {
        content = new Content();
        content.setType("text/plain");
        content.setStatusCode(HttpStatus.SC_OK);
        try {
            content.setBody(httpExchange.getRequestBody().readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return content.getStatusCode();
    }

    /**
     * <p>
     * Process the standard query from uri configured in the configure field.
     *
     * @param httpExchange The current http exchange.
     * @param content The content from the previous call and the content given to the following one.
     * @return The http status code.
     * @author Clement BONET
     * @version 1.0.0
     */
    public int processConfigure(HttpExchange httpExchange, Content content) {
        content = new Content();
        content.setType("application/json");
        content.setStatusCode(HttpStatus.SC_OK);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            content.setBody(objectMapper.writeValueAsString(getConfiguration()).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            content.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            content.setType("text/plain");
            content.setBody(e.toString().getBytes(StandardCharsets.UTF_8));
            throw new RuntimeException(e);
        }
        return content.getStatusCode();
    }

    /**
     * <p>
     * Process the standard query from uri configured in the metadata field.
     *
     * @param httpExchange The current http exchange.
     * @param content The content from the previous call and the content given to the following one.
     * @return The http status code.
     * @author Clement BONET
     * @version 1.0.0
     */
    public int processMetadata(HttpExchange httpExchange, Content content) {
        content = new Content();
        content.setType("text/plain");
        content.setStatusCode(HttpStatus.SC_OK);
        try {
            content.setBody(httpExchange.getRequestBody().readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return content.getStatusCode();
    }
}
