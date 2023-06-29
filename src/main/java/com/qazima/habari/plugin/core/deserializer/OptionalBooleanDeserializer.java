package com.qazima.habari.plugin.core.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Optional;

/**
 * <p>
 * This class if used for automatic deserialization of an Optional&lt;Boolean&gt;.
 *
 * @author Clement BONET
 * @version 1.0.0
 */
public class OptionalBooleanDeserializer extends StdDeserializer<Optional<Boolean>> {
    /**
     * <p>
     * Default protected empty constructor with no parameter,
     * just here to permit JsonParser to call the constructor with no parameter.
     * <p>
     * Call the <code>StdDeserializer&lt;Optional&lt;Boolean&gt;&gt;</code>
     * constructor with a <code>null</code> value.
     *
     * @author Clement BONET
     * @version 1.0.0
     */
    protected OptionalBooleanDeserializer() {
        super((Class<?>) null);
    }

    /**
     * <p>
     * This method override the standard deserialization to transform into an Optional&lt;Boolean&gt;.
     *
     * @param jsonParser The current parser containing the json to compute.
     * @param deserializationContext The context of the deserialization.
     * @return Optional&lt;Boolean&gt; The result of the deserialization.
     * @throws IOException Any I/O exception raised by the deserialization.
     * @author Clement BONET
     * @version 1.0.0
     */
    @Override
    public Optional<Boolean> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return Optional.of(jsonParser.getBooleanValue());
    }
}
