package com.electrifiedded.thetitans.core;

import com.electrifiedded.thetitans.TheTitans;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class PrivateHelper {
    public static <T, E> void set(Class<? super T> classToAccess, T instance, E value, String... fieldNames) {
        Field field = ReflectionHelper.findField(classToAccess, ObfuscationReflectionHelper.remapFieldNames(classToAccess.getName(), fieldNames));
        try {
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(field, field.getModifiers() & -17);
            field.set(instance, value);
        } catch (Exception e) {
            TheTitans.error("Failed to set field", e);
        }
    }

    public static <T> Object get(Class<? super T> classToAccess, T instance, String... fieldNames) {
        Field field = ReflectionHelper.findField(classToAccess, ObfuscationReflectionHelper.remapFieldNames(classToAccess.getName(), fieldNames));
        try {
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(field, field.getModifiers() & -17);
            return field.get(instance);
        } catch (Exception e) {
            TheTitans.error("Failed to get field", e);
            return null;
        }
    }

    public static void view(EntityPlayerMP player, String... classes) {
        int index = 0;
        for (int i = 0; i < classes.length; ++i) {
            try {
                Class<?> clazz = Class.forName(classes[i]);
                Field[] fields = clazz.getDeclaredFields();
                for (int ii = 0; ii < fields.length; ++ii) {
                    try {
                        if (player != null) {
                            player.sendMessage(new TextComponentTranslation("command.thetitans.debugger.view.success", new Object[]{TextFormatting.GOLD + Modifier.toString(fields[ii].getModifiers()) + " " + TextFormatting.AQUA + fields[ii].getType().getSimpleName() + " " + TextFormatting.BOLD + TextFormatting.YELLOW + fields[ii].getName()}));
                        }
                        TheTitans.debug("Found variable: [ " + Modifier.toString(fields[ii].getModifiers()) + " " + fields[ii].getType().getSimpleName() + " " + fields[ii].getName() + "; ]");
                        ++index;
                    } catch (Exception e) {
                        TheTitans.error("Cannot show variable", e);
                    }
                }
            } catch (Exception e) {
                TheTitans.error("Failed to find class file", e);
            }
        }

        if (index < 1 && player != null) {
            player.sendMessage(new TextComponentTranslation("command.thetitans.debugger.view.failed", new Object[0]));
        }
    }
}
