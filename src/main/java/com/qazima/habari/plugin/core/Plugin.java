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
import java.nio.charset.StandardCharsets;

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
