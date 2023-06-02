package com.rangers.redis.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Description——fastjson序列化类
 * Created By Rangers At 2023/6/2
 **/
public class FastJsonSerializer<T> implements RedisSerializer<T> {
    private final Class<T> clazz;

    public FastJsonSerializer(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (Objects.isNull(t)) {
            return new byte[0];
        }
        return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (Objects.isNull(bytes) || ArrayUtils.isEmpty(bytes)) {
            return null;
        }
        return JSON.parseObject(new String(bytes, StandardCharsets.UTF_8), clazz);
    }
}
