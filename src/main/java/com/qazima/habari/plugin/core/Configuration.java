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

/**
 * <p>
 * The configuration of the plugin.
 * It is used to define the behavior of the current plugin.
 *
 * @author Clement BONET
 * @version 1.0.0
 */
@JsonPropertyOrder(alphabetic = true)
public class Configuration {
    /**
     * <p>
     * The default number of elements shown by page.
     *
     * @author Clement BONET
     * @version 1.0.0
     */
    @Getter
    @Setter
    @JsonProperty("defaultPageSize")
    private int defaultPageSize = 50;
    /**
     * <p>
     * A boolean value (true/false) to indication if
     * the delete operation is allowed.
     *
     * @author Clement BONET
     * @version 1.0.0
     */
    @Getter
    @Setter
    @JsonProperty("allowDelete")
    @JsonDeserialize(using = OptionalBooleanDeserializer.class)
    @JsonSerialize(using = OptionalBooleanSerializer.class)
    private Optional<Boolean> deleteAllowed = Optional.empty();
    /**
     * <p>
     * A boolean value (true/false) to indication if
     * the get operation is allowed.
     *
     * @author Clement BONET
     * @version 1.0.0
     */
    @Getter
    @Setter
    @JsonProperty("allowGet")
    @JsonDeserialize(using = OptionalBooleanDeserializer.class)
    @JsonSerialize(using = OptionalBooleanSerializer.class)
    private Optional<Boolean> getAllowed = Optional.empty();
    /**
     * <p>
     * A boolean value (true/false) to indication if
     * the post operation is allowed.
     *
     * @author Clement BONET
     * @version 1.0.0
     */
    @Getter
    @Setter
    @JsonProperty("allowPost")
    @JsonDeserialize(using = OptionalBooleanDeserializer.class)
    @JsonSerialize(using = OptionalBooleanSerializer.class)
    private Optional<Boolean> postAllowed = Optional.empty();
    /**
     * <p>
     * A boolean value (true/false) to indication if
     * the put operation is allowed.
     *
     * @author Clement BONET
     * @version 1.0.0
     */
    @Getter
    @Setter
    @JsonProperty("allowPut")
    @JsonDeserialize(using = OptionalBooleanDeserializer.class)
    @JsonSerialize(using = OptionalBooleanSerializer.class)
    private Optional<Boolean> putAllowed = Optional.empty();
    /**
     * <p>
     * The uri where the user can reach the plugin.
     *
     * @author Clement BONET
     * @version 1.0.0
     */
    @Getter
    @Setter
    @JsonProperty("uri")
    private String uri;
}
