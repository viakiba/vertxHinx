package io.github.viakiba.hinx.compoent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.bson.json.Converter;
import org.bson.json.JsonWriterSettings;
import org.bson.json.StrictJsonWriter;
import java.io.IOException;

@Slf4j
public class JsonUtil {
    public static ObjectMapper jackson = new ObjectMapper();

    static {
        jackson.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    //mongo long型转换 解决 从mongo取出数据的格式问题 { "$date" : { "$numberLong" : "<dateAsMilliseconds>" } }
    public static final JsonWriterSettings LONG_WRITER_SETTING = JsonWriterSettings.builder().int64Converter(new Converter<Long>() {
        @Override
        public void convert(Long value, StrictJsonWriter writer) {
            writer.writeNumber(value.toString());
        }
    }).build();

    public static String obj2Str(Object object) {
        try {
            return jackson.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T str2Obj(String jsonStr, Class<T> valueType) {
        try {
            return jackson.readValue(jsonStr, valueType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static <T> T byteArray2Obj(byte[] bytes, Class<T> valueType) {
        try {
            return jackson.readValue(bytes, valueType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T str2Obj(String str, TypeReference<T> type) {
        try {
            return jackson.readValue(str, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
