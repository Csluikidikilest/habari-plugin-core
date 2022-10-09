package com.qazima.habari.plugin.core.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Optional;

public class OptionalBooleanSerializer extends StdSerializer<Optional<Boolean>> {
    protected OptionalBooleanSerializer() {
        super((Class<Optional<Boolean>>) null);
    }

    @Override
    public void serialize(Optional<Boolean> value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if(value.isEmpty()) {
            jsonGenerator.writeRawValue("null");
        } else {
            jsonGenerator.writeRawValue(value.get().toString());
        }
    }
}
