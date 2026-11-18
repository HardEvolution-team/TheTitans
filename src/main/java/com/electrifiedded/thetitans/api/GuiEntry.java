package com.electrifiedded.thetitans.api;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.entity.titan.EntityBlazeTitan;
import com.electrifiedded.thetitans.entity.titan.EntityCreeperTitan;
import com.electrifiedded.thetitans.entity.titan.EntityEnderColossus;
import com.electrifiedded.thetitans.entity.titan.EntityGhastTitan;
import com.electrifiedded.thetitans.entity.titan.EntityMagmaCubeTitan;
import com.electrifiedded.thetitans.entity.titan.EntityPigZombieTitan;
import com.electrifiedded.thetitans.entity.titan.EntitySilverfishTitan;
import com.electrifiedded.thetitans.entity.titan.EntitySlimeTitan;
import com.electrifiedded.thetitans.entity.titan.EntitySnowGolemTitan;
import com.electrifiedded.thetitans.entity.titan.EntitySpiderTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.entity.titan.EntityWitherzilla;
import com.electrifiedded.thetitans.entity.titan.EntityZombieTitan;
import com.electrifiedded.thetitans.handler.EventEntry;
import com.electrifiedded.thetitans.proxy.ClientProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GuiEntry {
    public static final List<GuiEntry> instances = new ArrayList<GuiEntry>();
    private ResourceLocation texture;
    public int[] rgb = {255, 0, 0, 255, 135, 0};
    private Object entry;
    private UUID uuid;
    private double lastDamage;
    private double totalHarmPrev;
    private double mainAmountPrev;
    private double staminaAmountPrev;
    public int ticksExisted;
    public int deathTicks;
    private int ticksHeal;
    private int hurtTime;
    private int lengthShake;
    private double indicatorSmoothMain;
    private double indicatorSmoothSecondary;
    private double indicatorSmoothStamina;
    private double indicatorMain;
    private double indicatorStamina;
    private boolean staminaBarRender;

    GuiEntry(Object entry, UUID uuid, String texture, boolean... flags) {
        this.entry = entry;
        this.uuid = uuid;
        if (texture == null)
            if (entry instanceof EventEntry)
            this.texture = new ResourceLocation(TheTitans.getTextures("textures/gui", "eventbars/base.png"));
        else if (entry instanceof EntityLivingBase && (entry instanceof EntityPlayer ||(TheTitans.checkFriendlyFire(Minecraft.getMinecraft().player, (EntityLivingBase) entry, true))))
            this.texture = new ResourceLocation(TheTitans.getTextures("textures/gui", "normalbars/base.png"));
        else
            this.texture = new ResourceLocation(TheTitans.getTextures("textures/gui", "titanbars/base.png"));
        else
            if (entry instanceof EventEntry)
            this.texture = new ResourceLocation(TheTitans.getTextures("textures/gui", "eventbars/" + texture + ".png"));
        else if (entry instanceof EntityLivingBase && (entry instanceof EntityPlayer ||(TheTitans.checkFriendlyFire(Minecraft.getMinecraft().player, (EntityLivingBase) entry, true))))
            this.texture = new ResourceLocation(TheTitans.getTextures("textures/gui", "normalbars/" + texture + ".png"));
        else
            this.texture = new ResourceLocation(TheTitans.getTextures("textures/gui", "titanbars/" + texture + ".png"));
        lengthShake = 40;
        for (int i = 0; i < flags.length; i++)
            switch(i) {
            case 0: staminaBarRender = flags[0];
        }
    }

    public static GuiEntry create(Object entry, UUID uuid, boolean... flags) {
        return create(entry, uuid, null, flags);
    }

    public static GuiEntry create(Object entry, UUID uuid, String texture, boolean... flags) {
        instances.add(new GuiEntry(entry, uuid, texture, flags));
        return instances.get(instances.size() - 1);
    }

    public static boolean contains(UUID uuid) {
        for (GuiEntry ent : instances)
            if (ent.uuidIs(uuid.toString()))
            return true;
        return false;
    }

    public void onUpdate() {
        if (entry instanceof EntityLivingBase) {
            EntityLivingBase t = (EntityLivingBase) entry;
            if (t instanceof EntityWitherzilla)
                setColor(40, 40, 40, t.getRNG().nextInt(255), t.getRNG().nextInt(255), t.getRNG().nextInt(255));
            else if (t instanceof EntityEnderColossus)
                switch (((EntityEnderColossus) t).fetchVariantGiant()) {
                case 1: setColor(104, 201, 101, 156, 156, 156); break;
                case 2: setColor(255, 200, 24, 255, 0, 0); break;
                default: setColor(202, 3, 252, 252, 3, 252);
            } else if (t instanceof EntityGhastTitan)
                switch (((EntityGhastTitan) t).fetchVariantGiant()) {
                case 1: setColor(167, 241, 229, 91, 229, 204); break;
                case 2: setColor(60, 48, 86, 48, 0, 53); break;
                default: setColor(255, 255, 255, 155, 155, 155);
            } else if (t instanceof EntitySnowGolemTitan) {
                if (t.getHealth() > t.getMaxHealth() * 0.5F) setColor(233, 255, 255, 255, 200, 0);
                else if (t.getHealth() > t.getMaxHealth() * 0.25F) setColor(233, 255, 255, 255, 135, 0);
                else setColor(233, 255, 255, 233, 255, 255);
            } else if (t instanceof EntityMagmaCubeTitan)
                setColor(191, 54, 0, 41, 0, 0);
            else if (t instanceof EntitySlimeTitan)
                setColor(88, 222, 75, 53, 138, 45);
            else if (t instanceof EntityBlazeTitan)
                switch (((EntityBlazeTitan) t).fetchVariantGiant()) {
                case 1: setColor(0, 153, 255, 0, 255, 255); break;
                case 2: setColor(0, 200, 0, 0, 50, 0); break;
                case 3: setColor(153, 102, 51, 102, 51, 0); break;
                case 4: setColor(49, 49, 49, 0, 0, 0); break;
                default: setColor(255, 50, 0, 255, 100, 0);
            } else if (t instanceof EntityZombieTitan)
                switch (((EntityZombieTitan) t).fetchVariantGiant()) {
                case 1: setColor(104, 201, 101, 156, 156, 156); break;
                case 2: setColor(255, 200, 24, 255, 0, 0); break;
                case 3: setColor(89, 209, 0, 48, 115, 0); break;
                case 4: setColor(49, 49, 49, 0, 0, 0); break;
                default: setColor(28, 105, 24, 102, 57, 15);
            } else if (t instanceof EntityPigZombieTitan)
                switch (((EntityPigZombieTitan) t).fetchVariantGiant()) {
                case 1: setColor(104, 201, 101, 156, 156, 156); break;
                case 2: setColor(255, 200, 24, 255, 0, 0); break;
                case 3: setColor(89, 209, 0, 48, 115, 0); break;
                case 4: setColor(49, 49, 49, 0, 0, 0); break;
                default: setColor(226, 156, 156, 28, 105, 24);
            } else if (t instanceof EntitySilverfishTitan)
                switch (((EntitySilverfishTitan) t).fetchVariantGiant()) {
                case 1: setColor(224, 197, 117, 130, 117, 78); break;
                case 2: setColor(120, 250, 246, 0, 214, 207); break;
                case 3: setColor(0, 162, 255, 0, 107, 168); break;
                case 4: setColor(7, 219, 0, 1, 38, 0); break;
                case 5: setColor(220, 51, 0, 220, 0, 0); break;
                case 6: setColor(49, 49, 49, 0, 0, 0); break;
                default: setColor(156, 156, 156, 80, 80, 80);
            } else if (t instanceof EntitySpiderTitan)
                switch (((EntitySpiderTitan) t).fetchVariantGiant()) {
                case 1: setColor(143, 81, 46, 71, 38, 19); break;
                case 2: setColor(115, 84, 103, 56, 39, 50); break;
                case 3: setColor(145, 218, 255, 125, 160, 255); break;
                case 4: setColor(49, 49, 49, 0, 0, 0); break;
                default: setColor(89, 70, 59, 61, 52, 47);
            } else if (t instanceof EntityCreeperTitan) {
                boolean p = ((EntityCreeperTitan) t).getPowered();
                switch (((EntityCreeperTitan) t).fetchVariantGiant()) {
                    case 1: if (p) setColor(94, 94, 94, 250, 11, 2); else setColor(94, 94, 94, 137, 130, 103); break;
                    case 2: if (p) setColor(255, 255, 255, 255, 255, 255); else setColor(255, 255, 255, 214, 255, 255); break;
                    case 3: if (p) setColor(188, 0, 212, 232, 56, 255); else setColor(188, 0, 212, 148, 31, 163); break;
                    case 4: if (p) setColor(49, 49, 49, 50, 0, 50); else setColor(49, 49, 49, 0, 0, 0); break;
                    default: if (p) setColor(0, 255, 0, 51, 204, 255); else setColor(0, 255, 0, 0, 120, 0);
                }
            }
        }
        if (deathTicks > 0)
            deathTicks ++;
        if (hurtTime > 0)
            hurtTime --;
        if (ticksHeal > 0)
            ticksHeal --;
        if (hurtTime <= 0) {
            lastDamage = 0.0D;
            totalHarmPrev = 0.0D;
        }

        if (getCurrent(0) <= 0.0D)
            deathTicks ++;
        else if (getCurrent(0) > 0.0D && deathTicks > 0)
            deathTicks = 0;
        if (getCurrent(0) < mainAmountPrev || getCurrent(2) < staminaAmountPrev) {
            hurtTime = 120;
            lastDamage = Math.max(mainAmountPrev - getCurrent(0), 0.0D);
            totalHarmPrev += lastDamage;
            mainAmountPrev = getCurrent(0);
            staminaAmountPrev = getCurrent(2);
        }

        else if (getCurrent(0) != mainAmountPrev || getCurrent(2) != staminaAmountPrev) {
            ticksHeal = 120;
            mainAmountPrev = getCurrent(0);
            staminaAmountPrev = getCurrent(2);
        }

        if (indicatorMain > getMax(0))
            indicatorMain = getCurrent(0);
        else if (hurtTime <= 0 && indicatorMain > getCurrent(0))
            indicatorMain -= getMax(0) * 0.0015D;
        else if (indicatorMain < getCurrent(0))
            indicatorMain = getCurrent(0);
        if (indicatorStamina > getMax(2))
            indicatorStamina = getCurrent(2);
        else if (hurtTime <= 0 && indicatorStamina > getCurrent(2))
            indicatorStamina -= getMax(2) * 0.005D;
        else if (indicatorStamina < getCurrent(2))
            indicatorStamina = getCurrent(2);
        if (indicatorSmoothMain > getCurrent(0) && indicatorSmoothMain < getMax(0))
            indicatorSmoothMain -= getMax(0) * 0.0095D;
        else if (indicatorSmoothMain < getCurrent(0))
            indicatorSmoothMain += getMax(0) * 0.0095D;
        else
            indicatorSmoothMain = getCurrent(0);
        if (indicatorSmoothSecondary > getCurrent(1) && indicatorSmoothSecondary < getMax(1))
            indicatorSmoothSecondary -= getMax(1) * 0.0095D;
        else if (indicatorSmoothSecondary < getCurrent(1))
            indicatorSmoothSecondary += getMax(1) * 0.0095D;
        else
            indicatorSmoothSecondary = getCurrent(1);
        if (indicatorSmoothStamina > getCurrent(2) && indicatorSmoothStamina < getMax(2))
            indicatorSmoothStamina -= getMax(2) * 0.0095D;
        else if (indicatorSmoothStamina < getCurrent(2))
            indicatorSmoothStamina += getMax(2) * 0.0095D;
        else
            indicatorSmoothStamina = getCurrent(2);
        if (ticksExisted % 20 == 0)
            if (entry == null || entry instanceof Entity && ((Entity)entry).isDead)
            ClientProxy.guiIngameZ.removeQueue(uuid.toString());
        ticksExisted++;
    }

    public double fetchHarmLast() {
        return lastDamage;
    }

    public double fetchTotalHarmLast() {
        return totalHarmPrev;
    }

    public double fetchPercIndicator(int type) {
        switch(type) {
            case 1: return indicatorStamina / getMax(type);
            default: return indicatorMain / getMax(type);
        }
    }

    public double fetchIndicator(int type) {
        switch(type) {
            case 1: return indicatorStamina;
            default: return indicatorMain;
        }
    }

    public double fetchPercCurrent(int type) {
        return getCurrent(type) / getMax(type);
    }

    public double getCurrent(int type) {
        if (entry != null)
            switch(type) {
            case 1: {
                    if (entry instanceof EntityLivingBase)
                        return ((EntityLivingBase)entry).getAbsorptionAmount();
                    else if (entry instanceof EventEntry)
                        return ((EventEntry)entry).fetchLeftAmount();
                }

            case 2: {
                    if (entry instanceof EntityTitan)
                        return ((EntityTitan)entry).stunnedIs ? 0.0D : ((EntityTitan)entry).fetchStamina();
                    break;
                }

            default: if (entry instanceof EntityLivingBase)
                return ((EntityLivingBase)entry).getHealth();
                else if (entry instanceof EventEntry)
                    return ((EventEntry)entry).fetchLeftAmount();
        }

        return Double.MAX_VALUE;
    }

    public double getMax(int type) {
        if (entry != null)
            switch(type) {
            case 1: {
                    if (entry instanceof EntityLivingBase)
                        return ((EntityLivingBase)entry).getMaxHealth();
                    else
                        return 1020.0F;
                }

            case 2: {
                    if (entry instanceof EntityTitan)
                        return ((EntityTitan)entry).fetchStaminaMax();
                    break;
                }

            default: if (entry instanceof EntityLivingBase)
                return ((EntityLivingBase)entry).getMaxHealth();
                else if (entry instanceof EventEntry)
                    return ((EventEntry)entry).fetchMaxAmount();
        }

        return Double.MAX_VALUE;
    }

    public double fetchPercSmoothCurrent(int type) {
        return fetchSmoothCurrent(type) / getMax(type);
    }

    public double fetchSmoothCurrent(int type) {
        if (entry != null)
            switch(type) {
            case 1: return indicatorSmoothSecondary;
            case 2: return indicatorSmoothStamina;
            default: return indicatorSmoothMain;
        }

        return Double.MAX_VALUE;
    }

    public boolean is(Class<?>... classes) {
        for (Class<?> clazz : classes)
            if (clazz.isAssignableFrom(entry.getClass()))
            return true;
        return false;
    }

    public boolean uuidIs(String uuid) {
        return this.uuid.toString().equals(uuid);
    }

    public boolean healed() {
        return entry == null || entry instanceof EventEntry ? false : 120 - lengthShake < ticksHeal;
    }

    public boolean runDamaged() {
        return entry == null || entry instanceof EventEntry ? false : 120 - lengthShake < hurtTime;
    }

    public boolean allowRender(Entity entity) {
        if (entry != null)
            if (entry instanceof EntityTitan)
            return !((EntityTitan)entry).isDead && !((EntityTitan)entry).fetchWaiting() && !((EntityTitan)entry).isInvisible();
        else if (entry instanceof EntityLivingBase)
            return !((EntityLivingBase)entry).isDead && !((EntityLivingBase)entry).isInvisible() && ((EntityLivingBase)entry).ticksExisted > 2 && ((EntityLivingBase)entry).isInRangeToRenderDist(entity.getDistanceSq(((EntityLivingBase)entry).posX, ((EntityLivingBase)entry).posY, ((EntityLivingBase)entry).posZ));
        else if (entry instanceof EventEntry)
            return ((EventEntry)entry).canSee(entity);
        return false;
    }

    public boolean allowStaminaRender() {
        return staminaBarRender;
    }

    public GuiEntry setColor(int... colors) {
        for (int i = 0; i < rgb.length && i < colors.length; i++)
            rgb[i] = colors[i];
        return this;
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public Object getEntry() {
        return entry;
    }

    public int fetchTicksHurt() {
        return hurtTime;
    }
}
