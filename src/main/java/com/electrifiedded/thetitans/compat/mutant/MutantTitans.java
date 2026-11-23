package com.electrifiedded.thetitans.compat.mutant;

import java.lang.reflect.Field;
import java.util.List;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitanPart;
import com.electrifiedded.thetitans.entity.titan.mutant.EntityMutantCreeperTitan;
import com.electrifiedded.thetitans.entity.titan.mutant.EntityMutantEnderColossus;
import com.electrifiedded.thetitans.entity.titan.mutant.EntityMutantSkeletonTitan;
import com.electrifiedded.thetitans.entity.titan.mutant.EntityMutantZombieTitan;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.EntityRegistry;

/**
 * Optional integration with Mutant Creatures Legacy (modid {@code mutantbeasts}): the four hostile mutants get a
 * Titan form. Mutant titans extend the matching Titan, keep its whole kit and add the mutant's own moves; their
 * health/strength/size follow the real mutant-vs-vanilla ratios read from the installed mod's config, so a Mutant
 * Zombie Titan is to a Zombie Titan what a Mutant Zombie is to a Zombie. Models are the mutant rigs with extra
 * titan bones; textures/sounds are taken from the installed mod at runtime (nothing is copied into this jar).
 */
public final class MutantTitans {
    private MutantTitans() {
    }

    public static final String MODID = "mutantbeasts";
    private static int nextId = 300;

    public static boolean loaded() {
        return Loader.isModLoaded(MODID);
    }

    /** Called from preInit (both sides). */
    public static void registerEntities() {
        if (!loaded()) {
            TheTitans.info("Mutant Creatures Legacy not installed - Mutant Titans disabled.");
            return;
        }
        register(EntityMutantZombieTitan.class, "MutantZombieTitan", 0x70955C, 0x00AFAF);
        register(EntityMutantSkeletonTitan.class, "MutantSkeletonTitan", 0xC1C1C1, 0x494949);
        register(EntityMutantCreeperTitan.class, "MutantCreeperTitan", 0x5BBC4F, 0x0DA70B);
        register(EntityMutantEnderColossus.class, "MutantEnderColossus", 0x1A1A1A, 0xE079FA);
        EntityRegistry.registerModEntity(new ResourceLocation(TheTitans.MODID_V, "TitanBonePart"), com.electrifiedded.thetitans.entity.titan.mutant.EntityTitanBonePart.class,
                TheTitans.MODID_V + ".TitanBonePart", nextId++, TheTitans.modInstance, 512, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(TheTitans.MODID_V, "TitanThrownBlock"), com.electrifiedded.thetitans.entity.titan.mutant.EntityTitanThrownBlock.class,
                TheTitans.MODID_V + ".TitanThrownBlock", nextId++, TheTitans.modInstance, 512, 1, true);
        TheTitans.info("Mutant Creatures Legacy found - registered 4 Mutant Titans.");
    }

    private static void register(Class<? extends Entity> cls, String name, int egg1, int egg2) {
        EntityRegistry.registerModEntity(new ResourceLocation(TheTitans.MODID_V, name), cls, TheTitans.MODID_V + "." + name,
                nextId++, TheTitans.modInstance, 1024, 3, true, egg1, egg2);
    }

    // ------------------------------------------------------------------ stats from the mutant mod's config

    /** Reads {@code MBConfig.ENTITIES.<field>} of the installed mod, or {@code def} if unavailable. */
    public static double config(String field, double def) {
        try {
            Class<?> cfg = Class.forName("chumbanotz.mutantbeasts.MBConfig");
            Object entities = cfg.getField("ENTITIES").get(null);
            Field f = entities.getClass().getField(field);
            return ((Number)f.get(entities)).doubleValue();
        } catch (Throwable t) {
            return def;
        }
    }

    /** mutant max health / vanilla max health (e.g. Mutant Zombie 150 / Zombie 20 = 7.5). */
    public static double ratio(String mutantField, double mutantDefault, double vanillaValue) {
        double v = config(mutantField, mutantDefault);
        return v > 0.0D ? v / vanillaValue : mutantDefault / vanillaValue;
    }

    // ------------------------------------------------------------------ sounds of the installed mod

    public static SoundEvent sound(String name) {
        return SoundEvent.REGISTRY.getObject(new ResourceLocation(MODID, name));
    }

    public static void play(Entity e, String name, float volume, float pitch) {
        SoundEvent ev = sound(name);
        if (ev != null && !e.world.isRemote)
            e.world.playSound(null, e.posX, e.posY + e.height * 0.5D, e.posZ, ev, SoundCategory.HOSTILE, volume, pitch);
    }

    /** Plays "mutantbeasts:..." sound names through the mutant mod, anything else returns false. */
    public static boolean playIfMutant(Entity e, String name, float volume, float pitch) {
        if (name == null || !name.startsWith(MODID + ":"))
            return false;
        play(e, name.substring(MODID.length() + 1), volume, pitch);
        return true;
    }

    // ------------------------------------------------------------------ shared attack helpers

    /** Damages everything (except the titan, its parts and allies) inside a box; returns hit count. */
    public static int strikeBox(EntityTitan titan, AxisAlignedBB box, double baseDamage, int knockback, double lift) {
        List<Entity> list = titan.world.getEntitiesWithinAABBExcludingEntity(titan, box);
        int hits = 0;
        for (Entity e : list) {
            if (e instanceof EntityTitanPart || !(e instanceof EntityLivingBase) || !titan.allowStrikeGiant(e.getClass()))
                continue;
            if (!(e instanceof EntityTitan) && lift != 0.0D)
                e.motionY += lift;
            titan.strikeMobChoosen(e, (float)titan.fetchAmountStrike(baseDamage), titan.fetchAmountKnockback() + knockback);
            hits++;
        }
        return hits;
    }

    /** Sphere-ish strike around a point. */
    public static int strikeAround(EntityTitan titan, double x, double y, double z, double r, double baseDamage, int knockback, double lift) {
        return strikeBox(titan, new AxisAlignedBB(x - r, y - r * 0.5D, z - r, x + r, y + r, z + r), baseDamage, knockback, lift);
    }

    /** Ground block-dust burst (client sees the block break particles + sound) at a column. */
    public static void groundBurst(World world, double x, double y, double z, int count) {
        if (!(world instanceof WorldServer))
            return;
        BlockPos pos = new BlockPos(x, y - 0.5D, z);
        for (int i = 0; i < 6 && world.isAirBlock(pos) && pos.getY() > 0; i++)
            pos = pos.down();
        IBlockState st = world.getBlockState(pos);
        if (st.getBlock() == Blocks.AIR)
            st = Blocks.DIRT.getDefaultState();
        WorldServer ws = (WorldServer)world;
        ws.spawnParticle(EnumParticleTypes.BLOCK_DUST, x, pos.getY() + 1.1D, z, count, 1.5D, 0.6D, 1.5D, 0.3D, net.minecraft.block.Block.getStateId(st));
        ws.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, x, pos.getY() + 1.5D, z, Math.max(1, count / 12), 1.0D, 0.5D, 1.0D, 0.0D);
    }

    /** Yaw (degrees) from a titan to a point, in render-yaw convention. */
    public static float yawTo(Entity from, double x, double z) {
        return (float)(MathHelper.atan2(z - from.posZ, x - from.posX) * (180D / Math.PI)) - 90.0F;
    }
}
