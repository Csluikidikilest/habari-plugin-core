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
 * The abstract class used to create plugins for Habari.
 * </p>
 * @author Clément BONET
 * @since 2022-10-01
 * @version 0.1
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "connectionType")
@JsonPropertyOrder({"connectionType", "configuration" })
@JsonTypeName("com.qazima.habari.plugin.core.Plugin")
public abstract class Plugin {
    /**
     * <p>
     * The configuration of the plugin.
     * </p>
     */
    @Getter
    @Setter
    @JsonProperty("configuration")
    private Configuration configuration;
    /**
     * <p>
     * The type of the plugin.
     * This field is used to authenticate each plugin during the automatic load.
     * </p>
     */
    @Getter
    @Setter
    @JsonProperty("connectionType")
    private String connectionType;

    /**
     * <p>
     * This method is used to test if the given string is null or empty.
     * </p>
     * @param string
     * @return true if the string is null or empty, else false.
     */
    protected boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    /**
     * <p>
     * This method is used to test if the given string is null, empty or only contains white spaces. (character 0x20 in ascii table).
     * </p>
     * @param string
     * @return true if the string is null, empty or only contains white spaces, else false.
     */
    protected boolean isNullOrWhiteSpace(String string) {
        return isNullOrEmpty(string) || string.trim().isEmpty();
    }

    /**
     * <p>
     * Split given parameters into a map grouped by the key.
     * ie:
     * the following string
     * <code>{
     *   "key1": "value1",
     *   "key2": "value2",
     *   "key1": "value3",
     *   "key3"
     * }</code>
     * will give
     * <code>key1 -> [value1, value3]
     * key2 -> [value2]
     * key3 -> []</code>
     * </p>
     * @param parameters
     * @return The map of the values grouped by the key
     * @throws IOException
     */
    protected Map<String, List<String>> splitRequestBody(String parameters) throws IOException {
        Map<String, String> temp = Collections.emptyMap();
        Map<String, List<String>> result = Collections.emptyMap();
        if (!isNullOrWhiteSpace(parameters)) {
            ObjectMapper mapper = new ObjectMapper();
            temp = mapper.readValue(parameters, Map.class);
        }

        for (Map.Entry<String, String> element : temp.entrySet()) {
            if(!result.containsKey(element.getKey())) {
                result.put(element.getKey(), new ArrayList<>());
            }
            result.get(element.getKey()).add(element.getValue());
        }

        return result;
    }

    /**
     * Split given parameters into a map grouped by the key.
     * ie:
     * the following string
     * <code>key1=value1&key2=value2&key1=value3&key3</code>
     * will give
     * <code>key1 -> [value1, value3]
     * key2 -> [value2]
     * key3 -> []</code>
     * </p>
     * @param parameters
     * @return The map of the values grouped by the key
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
     * Split given parameters into a map<key, value>.
     * ie:
     * the following string
     * <code>key1=value1</code>
     * will give
     * <code>key1 -> value1</code>
     * <code>key2</code>
     * will give
     * <code>key2 -> null</code>
     * <code>key3=</code>
     * will give
     * <code>key3 -> null</code>
     * </p>
     * @param parameters
     * @return The map entry of key value.
     */
    protected AbstractMap.SimpleImmutableEntry<String, String> splitQueryParameter(String parameters) {
        final int idx = parameters.indexOf("=");
        final String key = idx > 0 ? parameters.substring(0, idx) : parameters;
        final String value = idx > 0 && parameters.length() > idx + 1 ? parameters.substring(idx + 1) : null;
        return new AbstractMap.SimpleImmutableEntry<>(
                URLDecoder.decode(key, StandardCharsets.UTF_8),
                URLDecoder.decode(value, StandardCharsets.UTF_8)
        );
    }

    /**
     * This method is the one to override to perform desired actions on the uri match.
     * The httpExchange parameter contains the current query.
     * The content parameter is used to give current result to others plugins (in a cascade mode) and is used to send
     * the final result to the client.
     * @param httpExchange
     * @param content
     * @return An HTTP status code to inform other plugin the current status.
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
     * This method is the one to override to perform desired actions on the configuration match.
     * The httpExchange parameter contains the current query.
     * The content parameter is used to give current result to others plugins (in a cascade mode) and is used to send
     * the final result to the client.
     * @param httpExchange
     * @param content
     * @return An HTTP status code to inform other plugin the current status.
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
     * This method is the one to override to perform desired actions on the metadata match.
     * The httpExchange parameter contains the current query.
     * The content parameter is used to give current result to others plugins (in a cascade mode) and is used to send
     * the final result to the client.
     * @param httpExchange
     * @param content
     * @return An HTTP status code to inform other plugin the current status.
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
