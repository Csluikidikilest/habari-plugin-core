package com.qazima.habari.plugin.core.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Optional;

public class OptionalBooleanDeserializer extends StdDeserializer<Optional<Boolean>> {
    protected OptionalBooleanDeserializer() {
        super((Class<?>) null);
    }

    @Override
    public Optional<Boolean> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return Optional.of(jsonParser.getBooleanValue());
    }
}
