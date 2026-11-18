package com.electrifiedded.thetitans.handler;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.network.NetworkHandler;
import com.electrifiedded.thetitans.network.packet.PacketRemoveBar;
import com.electrifiedded.thetitans.util.Maths.Vec;
import com.electrifiedded.thetitans.util.Maths;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EventEntry {
    public static final List<EventEntry> instances = new ArrayList<EventEntry>();
    protected UUID uuid;
    private boolean[] allowedTimes = {true, true, true, true};

    public int time;
    public int ticksExisted;
    public World world;
    protected String unlocalizedName;
    protected boolean enabled;
    protected boolean worldwideV;
    protected double chance;
    public double range;
    public Vec position;
    private int[] limitMob = {1, 1, 40, 20, 5, 5};
    private int[] entities = {0, 0, 0, 0, 0, 0};
    private int entitiesCap;
    private int killed;
    private final List<Class<?>> bossesV = new ArrayList<Class<?>>();
    private final List<Class<?>> bossesMini = new ArrayList<Class<?>>();
    private final List<Class<?>> entitiesCommon = new ArrayList<Class<?>>();
    private final List<Class<?>> entitiesUncommon = new ArrayList<Class<?>>();
    private final List<Class<?>> entitiesRare = new ArrayList<Class<?>>();
    private final List<Class<?>> entitiesSpecial = new ArrayList<Class<?>>();

    EventEntry(Entity entity, World world, String unlocalizedName, double chance, double range) {
        if (entity == null)
            worldwideV = true;
        else
            position = new Vec(entity.posX, entity.posZ);
        uuid = UUID.randomUUID();
        this.world = world;
        this.unlocalizedName = unlocalizedName;
        this.chance = chance;
        this.range = range;
    }

    public static EventEntry create(Entity entity, World world, String unlocalizedName, double chance, double range) {
        int dimensionID = world.provider.getDimension();
        instances.add(new EventEntry(entity, world, unlocalizedName, chance, range));
        for (EventEntry event : instances)
            if (instances.get(instances.size() - 1).uuid.equals(event.uuid) && event.worldwideV && instances.get(instances.size() - 1).worldwideV && event.world.provider.getDimension() == dimensionID) {
            instances.remove(instances.size() - 1);
            return null;
        }

        return instances.get(instances.size() - 1);
    }

    public String getName() {
        return TheTitans.translate("event", unlocalizedName);
    }

    public EventEntry setTranslationKey(String unlocalizedName) {
        this.unlocalizedName = unlocalizedName;
        return this;
    }

    public String getTranslationKey() {
        return unlocalizedName;
    }

    public void start(int bossesV, int minibosses, int entities, int... limits) {
        EventStore.sendMessage(world, TheTitans.translate("event", unlocalizedName + "_start_" + Maths.random(1, 5)));
        enabled = true;
        entitiesCap = (bossesV * 30) + (minibosses * 5) + entities;
        killed = 0;
        if (limits.length > 0)
            for (int i = 0; i < limits.length; i++)
            limitMob[i] = limits[i];
    }

    public void stop() {
        EventStore.sendMessage(world, TheTitans.translate("event", unlocalizedName + "_stop_" + Maths.random(1, 5)));
        enabled = false;
        allowedTimes = new boolean[] {true, true, true, true};
        limitMob = new int[] {1, 1, 40, 20, 5, 5};
        entities = new int[] {0, 0, 0, 0, 0, 0};
        entitiesCap = 0;
        killed = 0;
        NetworkHandler.dispatchPacketClient(new PacketRemoveBar(uuid.toString()));
        instances.remove(this);
    }

    public void onUpdate(Side side) {
        time = (int)((world.getWorldTime() % 24000L) / 6000L);
        if (enabled) {
            if (killed >= entitiesCap)
                stop();
            if (!allowedTimes[time])
                stop();
        }

        if (enabled)
            if (side == Side.CLIENT)
            whenUpdateClient();
        else if (side == Side.SERVER)
            whenUpdateServer();
        ticksExisted ++;
    }

    @SideOnly(Side.CLIENT)
    protected void whenUpdateClient() {
    }

    protected void whenUpdateServer() {
        if (ticksExisted % 10 == 0) {
            List<?> players = world.playerEntities;
            Vec position;
            Vec offset;
            for (Object player : players) {
                position = new Vec(((EntityPlayer)player).posX, ((EntityPlayer)player).posZ);
                if (!worldwideV && this.position != null && position.distance(this.position) > range)
                    break;
                for (int i = 0; i < bossesV.size(); i++)
                    if (Maths.chance(0.0005F)) {
                    offset = new Vec(Math.floor(position.xVec) + 0.5D + Maths.random(-32, 32), Math.floor(position.yVec) + 0.5D + Maths.random(-32, 32));
                    spawnEntity(0, Maths.random(bossesV.size() - 1), offset.xVec, world.getTopSolidOrLiquidBlock(new BlockPos((int)offset.xVec, 0, (int)offset.yVec)).getY(), offset.yVec);
                    break;
                }

                for (int i = 0; i < bossesMini.size(); i++)
                    if (Maths.chance(0.01F)) {
                    offset = new Vec(Math.floor(position.xVec) + 0.5D + Maths.random(-32, 32), Math.floor(position.yVec) + 0.5D + Maths.random(-32, 32));
                    spawnEntity(1, Maths.random(bossesMini.size() - 1), offset.xVec, world.getTopSolidOrLiquidBlock(new BlockPos((int)offset.xVec, 0, (int)offset.yVec)).getY(), offset.yVec);
                    break;
                }

                for (int i = 0; i < entitiesCommon.size(); i++)
                    if (Maths.chance(0.3F)) {
                    offset = new Vec(Math.floor(position.xVec) + 0.5D + Maths.random(-32, 32), Math.floor(position.yVec) + 0.5D + Maths.random(-32, 32));
                    spawnEntity(2, Maths.random(entitiesCommon.size() - 1), offset.xVec, world.getTopSolidOrLiquidBlock(new BlockPos((int)offset.xVec, 0, (int)offset.yVec)).getY(), offset.yVec);
                    break;
                }

                for (int i = 0; i < entitiesUncommon.size(); i++)
                    if (Maths.chance(0.075F)) {
                    offset = new Vec(Math.floor(position.xVec) + 0.5D + Maths.random(-32, 32), Math.floor(position.yVec) + 0.5D + Maths.random(-32, 32));
                    spawnEntity(3, Maths.random(entitiesUncommon.size() - 1), offset.xVec, world.getTopSolidOrLiquidBlock(new BlockPos((int)offset.xVec, 0, (int)offset.yVec)).getY(), offset.yVec);
                    break;
                }

                for (int i = 0; i < entitiesRare.size(); i++)
                    if (Maths.chance(0.001F)) {
                    offset = new Vec(Math.floor(position.xVec) + 0.5D + Maths.random(-32, 32), Math.floor(position.yVec) + 0.5D + Maths.random(-32, 32));
                    spawnEntity(4, Maths.random(entitiesRare.size() - 1), offset.xVec, world.getTopSolidOrLiquidBlock(new BlockPos((int)offset.xVec, 0, (int)offset.yVec)).getY(), offset.yVec);
                    break;
                }

                for (int i = 0; i < entitiesSpecial.size(); i++)
                    if (Maths.chance(0.025F)) {
                    offset = new Vec(Math.floor(position.xVec) + 0.5D + Maths.random(-32, 32), Math.floor(position.yVec) + 0.5D + Maths.random(-32, 32));
                    spawnEntity(5, Maths.random(entitiesSpecial.size() - 1), offset.xVec, world.getTopSolidOrLiquidBlock(new BlockPos((int)offset.xVec, 0, (int)offset.yVec)).getY(), offset.yVec);
                    break;
                }
            }
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    protected int fetchAmountMob(int type) {
        switch(type) {
            case 0: return 30;
            case 1: return 5;
            case 5: return 2;
            default: return 1;
        }
    }

    public int fetchMaxAmount() {
        return entitiesCap;
    }

    public int fetchLeftAmount() {
        return entitiesCap - killed;
    }

    public int fetchDeadAmount() {
        return killed;
    }

    public int fetchAliveAmount() {
        return (entities[0] * 30) + (entities[1] * 5) + entities[2] + entities[3] + entities[4] + (entities[5] * 2);
    }

    public boolean canSee(Entity entity) {
        return enabled && entity.dimension == world.provider.getDimension() && (worldwideV || position.distance(entity.posX, entity.posZ) < range);
    }

    public EventEntry assignReachTicks(boolean... flags) {
        for(int i = 0; i < flags.length; i++)
            allowedTimes[i] = flags[i];
        return this;
    }

    protected boolean spawnEntity(int type, int index, double x, double y, double z) {
        List<Class<?>> list = null;
        switch(type) {
            case 0: list = bossesV; break;
            case 1: list = bossesMini; break;
            case 2: list = entitiesCommon; break;
            case 3: list = entitiesUncommon; break;
            case 4: list = entitiesRare; break;
            case 5: list = entitiesSpecial;
        }

        if (world == null || list == null || list.isEmpty() || fetchAliveAmount() + fetchAmountMob(type) >= entitiesCap || entities[type] >= limitMob[type])
            return false;
        Entity entity;
        try {
            entity = (Entity) list.get(MathHelper.clamp(index, 0, Math.max(list.size() - 1, 0))).getConstructor(World.class).newInstance(world);
        }

        catch (Exception e) {
            TheTitans.error("Entity from class " + list.get(MathHelper.clamp(index, 0, Math.max(list.size() - 1, 0))).getSimpleName() + " does not have the constructor Entity(World)", e);
            return false;
        }

        if (entity instanceof EntityLiving)
            ((EntityLiving)entity).onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entity)), (IEntityLivingData)null);
        entity.setLocationAndAngles(x, y, z, 0.0F, 0.0F);
        entity.getEntityData().setString("TitanEvent", uuid.toString());
        ((EntityLiving)entity).setAttackTarget(world.getClosestPlayer(position.xVec, world.getTopSolidOrLiquidBlock(new BlockPos((int)position.xVec, 0, (int)position.yVec)).getY(), position.yVec, range, false));
        world.spawnEntity(entity);
        entities[type] ++;
        return true;
    }

    public boolean onDeath(Entity entity) {
        if (!enabled || !entity.getEntityData().getString("TitanEvent").equals(uuid.toString()))
            return false;
        entity.getEntityData().setString("TitanEvent", "");
        for (int i = 0; i < 6; i++)
            switch(i) {
            case 0: {
                    for (Class<?> clazz : bossesV) {
                        if (entities[i] <= 0)
                            break;
                        if (entity.getClass().equals(clazz)) {
                            entities[i] --;
                            killed += 30;
                            return true;
                        }
                    }

                    break;
                }

            case 1: {
                    for (Class<?> clazz : bossesMini) {
                        if (entities[i] <= 0)
                            break;
                        if (entity.getClass().equals(clazz)) {
                            entities[i] --;
                            killed += 5;
                            return true;
                        }
                    }

                    break;
                }

            case 2: {
                    for (Class<?> clazz : entitiesCommon) {
                        if (entities[i] <= 0)
                            break;
                        if (entity.getClass().equals(clazz)) {
                            entities[i] --;
                            killed ++;
                            return true;
                        }
                    }

                    break;
                }

            case 3: {
                    for (Class<?> clazz : entitiesUncommon) {
                        if (entities[i] <= 0)
                            break;
                        if (entity.getClass().equals(clazz)) {
                            entities[i] --;
                            killed ++;
                            return true;
                        }
                    }

                    break;
                }

            case 4: {
                    for (Class<?> clazz : entitiesRare) {
                        if (entities[i] <= 0)
                            break;
                        if (entity.getClass().equals(clazz)) {
                            entities[i] --;
                            killed ++;
                            return true;
                        }
                    }

                    break;
                }

            case 5: {
                    for (Class<?> clazz : entitiesSpecial) {
                        if (entities[i] <= 0)
                            break;
                        if (entity.getClass().equals(clazz)) {
                            entities[i] --;
                            killed += 2;
                            return true;
                        }
                    }
                }
        }

        return false;
    }

    public EventEntry appendBosses(Class<?>... entities) {
        for (Class<?> entity : entities)
            if (entity != null && Entity.class.isAssignableFrom(entity))
            bossesV.add(entity);
        return this;
    }

    public EventEntry appendBossesMini(Class<?>... entities) {
        for (Class<?> entity : entities)
            if (entity != null && Entity.class.isAssignableFrom(entity))
            bossesMini.add(entity);
        return this;
    }

    public EventEntry appendCommonX(Class<?>... entities) {
        for (Class<?> entity : entities)
            if (entity != null && Entity.class.isAssignableFrom(entity))
            entitiesCommon.add(entity);
        return this;
    }

    public EventEntry appendUncommon(Class<?>... entities) {
        for (Class<?> entity : entities)
            if (entity != null && Entity.class.isAssignableFrom(entity))
            entitiesUncommon.add(entity);
        return this;
    }

    public EventEntry appendRare(Class<?>... entities) {
        for (Class<?> entity : entities)
            if (entity != null && Entity.class.isAssignableFrom(entity))
            entitiesRare.add(entity);
        return this;
    }

    public EventEntry appendSpecial(Class<?>... entities) {
        for (Class<?> entity : entities)
            if (entity != null && Entity.class.isAssignableFrom(entity))
            entitiesSpecial.add(entity);
        return this;
    }

    public UUID fetchUUIDBar() {
        return uuid;
    }
}
