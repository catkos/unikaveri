package com.example.unikaveri;

import android.os.Build;
import androidx.annotation.RequiresApi;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Adapter for serializing and deserializing SleepNote Object's LocalDateTime variables.
 *
 * @author Kerttu
 */
public class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

    /**
     * Serialize LocalDateTime to Json.
     *
     * @param date LocalDateTime
     * @param type Type
     * @param context JsonSerializationContext
     * @return JsonPrimitive
     * @throws JsonParseException
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public JsonElement serialize(LocalDateTime date, Type type, JsonSerializationContext context) throws JsonParseException {
        return new JsonPrimitive(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
    }

    /**
     * Deserialize Json to LocalDateTime if it's patterned "dd.MM.yyyy HH:mm".
     *
     * @param json JsonElement
     * @param type Type
     * @param context JsonDeserializationContext
     * @return LocalDateTime
     * @throws JsonParseException
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return LocalDateTime.parse(json.getAsJsonPrimitive().getAsString(), formatter);
    }
}