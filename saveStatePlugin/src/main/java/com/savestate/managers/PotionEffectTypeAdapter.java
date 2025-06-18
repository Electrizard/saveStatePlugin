package com.savestate.managers;

import com.google.gson.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Type;

public class PotionEffectTypeAdapter implements JsonSerializer<PotionEffect>, JsonDeserializer<PotionEffect> {
    
    @Override
    public JsonElement serialize(PotionEffect potionEffect, Type type, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", potionEffect.getType().getKey().toString());
        obj.addProperty("duration", potionEffect.getDuration());
        obj.addProperty("amplifier", potionEffect.getAmplifier());
        obj.addProperty("ambient", potionEffect.isAmbient());
        obj.addProperty("particles", potionEffect.hasParticles());
        obj.addProperty("icon", potionEffect.hasIcon());
        return obj;
    }
    
    @Override
    public PotionEffect deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = element.getAsJsonObject();
        
        String typeKey = obj.get("type").getAsString();
        PotionEffectType effectType = PotionEffectType.getByKey(org.bukkit.NamespacedKey.fromString(typeKey));
        
        if (effectType == null) {
            throw new JsonParseException("Unknown potion effect type: " + typeKey);
        }
        
        int duration = obj.get("duration").getAsInt();
        int amplifier = obj.get("amplifier").getAsInt();
        boolean ambient = obj.get("ambient").getAsBoolean();
        boolean particles = obj.get("particles").getAsBoolean();
        boolean icon = obj.get("icon").getAsBoolean();
        
        return new PotionEffect(effectType, duration, amplifier, ambient, particles, icon);
    }
}