package com.octo.kafka.dix.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

/**
 * Created by thm on 24/06/2016.
 */
public class JsonDeserializer<T> implements Deserializer<T> {

    private ObjectMapper objectMapper = new ObjectMapper();
    private Class<T> clazz;

    public JsonDeserializer(Class<T> clazz){
        this.clazz = clazz;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public T deserialize(String topic, byte[] bytes) {
        if (bytes == null) return null;
        try {
            return objectMapper.readValue(bytes, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void close() {

    }
}
