package com.qazima.habari.plugin.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sun.net.httpserver.HttpExchange;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpStatus;

import java.io.IOException;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "connectionType")
@JsonTypeName("com.qazima.habari.plugin.core.Plugin")
public abstract class Plugin {
    @Getter
    @Setter
    @JsonProperty("connectionType")
    private String connectionType;
    @Getter
    @Setter
    @JsonProperty("defaultPageSize")
    private int defaultPageSize = 50;
    @Getter
    @Setter
    @JsonProperty("deleteAllowed")
    private boolean deleteAllowed = false;
    @Getter
    @Setter
    @JsonProperty("getAllowed")
    private boolean getAllowed = true;
    @Getter
    @Setter
    @JsonProperty("metadataUri")
    private String metadataUri;
    @Getter
    @Setter
    @JsonProperty("postAllowed")
    private boolean postAllowed = false;
    @Getter
    @Setter
    @JsonProperty("putAllowed")
    private boolean putAllowed = false;
    @Getter
    @Setter
    @JsonProperty("uri")
    private String uri;

    public int process(HttpExchange httpExchange, Content content) {
        content = new Content();
        content.setType("text/plain");
        content.setStatusCode(HttpStatus.SC_OK);
        try {
            content.setBody(httpExchange.getRequestBody().readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return HttpStatus.SC_OK;
    }

    public int processConfigure(HttpExchange httpExchange, Content content) {
        content = new Content();
        content.setType("text/plain");
        content.setStatusCode(HttpStatus.SC_OK);
        try {
            content.setBody(httpExchange.getRequestBody().readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return HttpStatus.SC_OK;
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
        return HttpStatus.SC_OK;
    }
}
