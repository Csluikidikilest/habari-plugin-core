package com.qazima.habari.plugin.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.qazima.habari.plugin.core.deserializer.OptionalBooleanDeserializer;
import com.qazima.habari.plugin.core.serializer.OptionalBooleanSerializer;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@JsonPropertyOrder(alphabetic = true)
public class Configuration {
    @Getter
    @Setter
    @JsonProperty("defaultPageSize")
    private int defaultPageSize = 50;
    @Getter
    @Setter
    @JsonProperty("allowDelete")
    @JsonDeserialize(using = OptionalBooleanDeserializer.class)
    @JsonSerialize(using = OptionalBooleanSerializer.class)
    private Optional<Boolean> deleteAllowed = Optional.empty();
    @Getter
    @Setter
    @JsonProperty("allowGet")
    @JsonDeserialize(using = OptionalBooleanDeserializer.class)
    @JsonSerialize(using = OptionalBooleanSerializer.class)
    private Optional<Boolean> getAllowed = Optional.empty();
    @Getter
    @Setter
    @JsonProperty("allowPost")
    @JsonDeserialize(using = OptionalBooleanDeserializer.class)
    @JsonSerialize(using = OptionalBooleanSerializer.class)
    private Optional<Boolean> postAllowed = Optional.empty();
    @Getter
    @Setter
    @JsonProperty("allowPut")
    @JsonDeserialize(using = OptionalBooleanDeserializer.class)
    @JsonSerialize(using = OptionalBooleanSerializer.class)
    private Optional<Boolean> putAllowed = Optional.empty();
    @Getter
    @Setter
    @JsonProperty("uri")
    private String uri;
}
