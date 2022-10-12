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

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "connectionType")
@JsonPropertyOrder({"connectionType", "configuration" })
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

    protected boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    protected boolean isNullOrWhiteSpace(String string) {
        return isNullOrEmpty(string) || string.trim().isEmpty();
    }

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

    protected Map<String, List<String>> splitQuery(String parameters) {
        if (isNullOrWhiteSpace(parameters)) {
            return Collections.emptyMap();
        }
        return Arrays.stream(parameters.split("&"))
                .map(this::splitQueryParameter)
                .collect(groupingBy(AbstractMap.SimpleImmutableEntry::getKey, LinkedHashMap::new, mapping(Map.Entry::getValue, toList())));
    }

    protected AbstractMap.SimpleImmutableEntry<String, String> splitQueryParameter(String it) {
        final int idx = it.indexOf("=");
        final String key = idx > 0 ? it.substring(0, idx) : it;
        final String value = idx > 0 && it.length() > idx + 1 ? it.substring(idx + 1) : null;
        return new AbstractMap.SimpleImmutableEntry<>(
                URLDecoder.decode(key, StandardCharsets.UTF_8),
                URLDecoder.decode(value, StandardCharsets.UTF_8)
        );
    }

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
