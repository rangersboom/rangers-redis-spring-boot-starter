package com.rangers.redis.key;

import lombok.Builder;
import lombok.Data;

/**
 * Description——RedisKey构造器
 * Created By Rangers At 2023/6/2
 **/
@Data
@Builder
public final class RedisKey {

    public static final String SEPARATOR = ".";

    /**
     * Redis key 的前缀
     */
    private String prefix;

    /**
     * Redis key 的内容
     */
    private String suffix;

    public String of() {
        return String.format("%s.%s", prefix, suffix);
    }
}
