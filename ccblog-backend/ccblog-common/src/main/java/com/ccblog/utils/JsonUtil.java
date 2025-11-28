package com.ccblog.utils;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.IOException;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author czc
 * @date 2024-10-30
 */
public class JsonUtil {
    private static final ObjectMapper MAPPER = objectMapper(); // 获得序列化器

    /* ========== JSON 工具 ========== */
    public static String toJsonString(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("JSON serialize error", e);
        }
    }

    public static JSONObject toJson(Object obj){
        try {
            String json = MAPPER.writeValueAsString(obj);
            return JSONUtil.parseObj(json);
        } catch (Exception e) {
            throw new RuntimeException("JSON serialize error", e);
        }
    }




    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("JSON deserialize error", e);
        }
    }

    public static <T> List<T> parseList(String json, Class<T> clazz) {
        try {
            return MAPPER.readValue(json,
                    MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (Exception e) {
            throw new RuntimeException("JSON deserialize list error", e);
        }
    }

    /**
     * 序列化器
     */
    private static ObjectMapper objectMapper() {
        return Jackson2ObjectMapperBuilder.json()
                .modules(new JavaTimeModule())          // 实现序列化LocalDateTime
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
    }

    public static <T> String toStr(T t) {
        try {
            return MAPPER.writeValueAsString(t);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public static  <T> T toObj(String str, Class<T> clz) {
        try {
            return MAPPER.readValue(str, clz);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    /**
     * 序列换成json时,将所有的long变成string
     * 因为js中得数字类型不能包含所有的java long值
     */
    public static SimpleModule bigIntToStrsimpleModule() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, newSerializer(s -> String.valueOf(s)));
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        simpleModule.addSerializer(long[].class, newSerializer((Function<Long, String>) String::valueOf));
        simpleModule.addSerializer(Long[].class, newSerializer((Function<Long, String>) String::valueOf));
        simpleModule.addSerializer(BigDecimal.class, newSerializer(BigDecimal::toString));
        simpleModule.addSerializer(BigDecimal[].class, newSerializer(BigDecimal::toString));
        simpleModule.addSerializer(BigInteger.class, ToStringSerializer.instance);
        simpleModule.addSerializer(BigInteger[].class, newSerializer((Function<BigInteger, String>) BigInteger::toString));
        return simpleModule;
    }

    public static <T, K> JsonSerializer<T> newSerializer(Function<K, String> func) {
        return new JsonSerializer<T>() {
            @Override
            public void serialize(T t, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                if (t == null) {
                    jsonGenerator.writeNull();
                    return;
                }

                if (t.getClass().isArray()) {
                    jsonGenerator.writeStartArray();
                    Stream.of(t).forEach(s -> {
                        try {
                            jsonGenerator.writeString(func.apply((K) s));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    jsonGenerator.writeEndArray();
                } else {
                    jsonGenerator.writeString(func.apply((K) t));
                }
            }
        };
    }


}
