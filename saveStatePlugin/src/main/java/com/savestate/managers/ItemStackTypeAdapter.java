package com.savestate.managers;

import com.google.gson.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.Base64;

public class ItemStackTypeAdapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {
    
    @Override
    public JsonElement serialize(ItemStack itemStack, Type type, JsonSerializationContext context) {
        if (itemStack == null) {
            return JsonNull.INSTANCE;
        }
        
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(itemStack);
            dataOutput.close();
            return new JsonPrimitive(Base64.getEncoder().encodeToString(outputStream.toByteArray()));
        } catch (Exception e) {
            throw new JsonParseException("Failed to serialize ItemStack", e);
        }
    }
    
    @Override
    public ItemStack deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (element.isJsonNull()) {
            return null;
        }
        
        try {
            String encoded = element.getAsString();
            byte[] data = Base64.getDecoder().decode(encoded);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack itemStack = (ItemStack) dataInput.readObject();
            dataInput.close();
            return itemStack;
        } catch (Exception e) {
            throw new JsonParseException("Failed to deserialize ItemStack", e);
        }
    }
}