package com.electrifiedded.thetitans.client.render.mutant;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.electrifiedded.archaiclib.client.texture.CompositeTexture;
import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.compat.mutant.MutantTitans;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Builds (once, lazily, on the render thread) the extended mutant titan textures from the textures of the
 * installed Mutant Creatures mod; rebuilt automatically on resource reload by the TextureManager.
 */
@SideOnly(Side.CLIENT)
public final class MutantTitanTextures {
    private MutantTitanTextures() {
    }

    /** Titan variant recolours: 0 normal, 1 jungle, 2 blood, 3 radioactive, 4 void. */
    public static final int[] VARIANT_TINT = { 0xFFFFFFFF, 0xFFA8FF88, 0xFFFF7060, 0xFFC8FF50, 0xFF9A70E0 };
    public static final float[] VARIANT_BRIGHT = { 1.0F, 1.0F, 1.0F, 1.15F, 0.75F };

    private static final Map<String, ResourceLocation> CACHE = new HashMap<>();

    public static ResourceLocation mutant(String path) {
        return new ResourceLocation(MutantTitans.MODID, path);
    }

    /**
     * @param key     cache key / dynamic texture name
     * @param base    texture of the mutant mod
     * @param painter adds the canvas size and painting ops
     */
    public static ResourceLocation get(String key, ResourceLocation base, int baseW, int baseH, Consumer<CompositeTexture.Builder> painter) {
        ResourceLocation id = CACHE.get(key);
        if (id == null) {
            CompositeTexture.Builder b = CompositeTexture.builder(base, baseW, baseH).seed(key.hashCode());
            painter.accept(b);
            id = b.register(new ResourceLocation(TheTitans.MODID_V, "dynamic/" + key));
            CACHE.put(key, id);
        }
        return id;
    }
}
