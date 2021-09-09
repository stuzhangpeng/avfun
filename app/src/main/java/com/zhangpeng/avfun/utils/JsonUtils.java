package com.zhangpeng.avfun.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * json工具类，实现long转换为date
 */
public class JsonUtils {
    public static Gson getGson(){
        GsonBuilder builder = new GsonBuilder();
        // Register an adapter to manage the date types as long values
        builder.registerTypeAdapter(Date
                .class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });
        Gson gson = builder.create();
        return  gson;
    }

    /**
     * json反序列化
     * @param content
     * @param classType
     * @param <T>
     * @return
     */
    public static  <T> T parseJson(String content,Class<T> classType){
        Gson gson = getGson();
        T t = gson.fromJson(content, classType);
        return  t;
    }

    /**
     *序列化
     * @param content
     * @param classType
     * @param <T>
     * @return
     */
    public  <T> String parseJson(String content,T classType){
        Gson gson = getGson();
        String json = gson.toJson(classType);
        return  json;
    }
}
