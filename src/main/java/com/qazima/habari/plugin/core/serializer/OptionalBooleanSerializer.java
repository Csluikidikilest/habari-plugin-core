package com.qazima.habari.plugin.core.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Optional;

/**
 * <p>
 * This class if used for automatic serialization of an Optional&lt;Boolean&gt;.
 *
 * @author Clement BONET
 * @version 1.0.0
 */
public class OptionalBooleanSerializer extends StdSerializer<Optional<Boolean>> {
    /**
     * <p>
     * Default protected empty constructor with no parameter,
     * just here to permit JsonParser to call the constructor with no parameter.
     * <p>
     * Call the <code>StdSerializer&lt;Optional&lt;Boolean&gt;&gt;</code>
     * constructor with a <code>null</code> value.
     *
     * @author Clement BONET
     * @version 1.0.0
     */
    protected OptionalBooleanSerializer() {
        super((Class<Optional<Boolean>>) null);
    }

    /**
     * <p>
     * This method override the standard serialization to transform from an Optional&lt;Boolean&gt;.
     *
     * @param value The Optional&lt;Boolean&gt; to transform.
     * @param jsonGenerator The current generator.
     * @param serializerProvider the provider of the serialization.
     * @throws IOException Any I/O exception raised by the serialization.
     */
    @Override
    public void serialize(Optional<Boolean> value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (value.isEmpty()) {
            jsonGenerator.writeRawValue("null");
        } else {
            jsonGenerator.writeRawValue(value.get().toString());
        }
    }
}
