package com.richie.utils.okhttp;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Richie on 2018.12.22
 * JSON 转换器
 */
public class GsonConverter {

    public static <T> T fromJson(String json, Class<T> type) throws JsonIOException, JsonSyntaxException {
        return GsonHolder.GSON.fromJson(json, type);
    }

    public static <T> T fromJson(String json, Type type) {
        return GsonHolder.GSON.fromJson(json, type);
    }

    public static <T> T fromJson(JsonReader reader, Type typeOfT) throws JsonIOException, JsonSyntaxException {
        return GsonHolder.GSON.fromJson(reader, typeOfT);
    }

    public static <T> T fromJson(Reader json, Class<T> classOfT) throws JsonSyntaxException, JsonIOException {
        return GsonHolder.GSON.fromJson(json, classOfT);
    }

    public static <T> T fromJson(Reader json, Type typeOfT) throws JsonIOException, JsonSyntaxException {
        return GsonHolder.GSON.fromJson(json, typeOfT);
    }

    public static <T> List<T> fromJsonList(String json, Class<T> type) {
        Type listType = new TypeToken<ArrayList<T>>() {
        }.getType();
        return GsonHolder.GSON.fromJson(json, listType);
    }

    public static String toJson(Object src) {
        return GsonHolder.GSON.toJson(src);
    }

    public static String toJson(Object src, Type typeOfSrc) {
        return GsonHolder.GSON.toJson(src, typeOfSrc);
    }

    private static class GsonHolder {
        private final static Gson GSON = new Gson();
    }

}