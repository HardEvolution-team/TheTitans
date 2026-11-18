package com.electrifiedded.thetitans.client;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.api.GuiEntry;
import com.electrifiedded.thetitans.config.TitanConfig;
import com.electrifiedded.thetitans.entity.deity.EntityDeity;
import com.electrifiedded.thetitans.entity.deity.IDeity;
import com.electrifiedded.thetitans.entity.minion.EnumMinionType;
import com.electrifiedded.thetitans.entity.minion.IMinion;
import com.electrifiedded.thetitans.entity.titan.EntityBlazeTitan;
import com.electrifiedded.thetitans.entity.titan.EntityCaveSpiderTitan;
import com.electrifiedded.thetitans.entity.titan.EntityCreeperTitan;
import com.electrifiedded.thetitans.entity.titan.EntityEnderColossus;
import com.electrifiedded.thetitans.entity.titan.EntityExecutorDragon;
import com.electrifiedded.thetitans.entity.titan.EntityGargoyleTitan;
import com.electrifiedded.thetitans.entity.titan.EntityGhastTitan;
import com.electrifiedded.thetitans.entity.titan.EntityIronGolemTitan;
import com.electrifiedded.thetitans.entity.titan.EntityMagmaCubeTitan;
import com.electrifiedded.thetitans.entity.titan.EntityPigZombieTitan;
import com.electrifiedded.thetitans.entity.titan.EntitySilverfishTitan;
import com.electrifiedded.thetitans.entity.titan.EntitySkeletonTitan;
import com.electrifiedded.thetitans.entity.titan.EntitySlimeTitan;
import com.electrifiedded.thetitans.entity.titan.EntitySnowGolemTitan;
import com.electrifiedded.thetitans.entity.titan.EntitySpiderTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.entity.titan.EntityWitherzilla;
import com.electrifiedded.thetitans.entity.titan.EntityZombieTitan;
import com.electrifiedded.thetitans.entity.titan.classic.EntityOgPigZombieTitan;
import com.electrifiedded.thetitans.entity.titan.classic.EntityOgZombieTitan;
import com.electrifiedded.thetitans.handler.EventEntry;
import com.electrifiedded.thetitans.item.armor.ItemAdminiumArmor;
import com.electrifiedded.thetitans.item.armor.ItemDemontiumArmor;
import com.electrifiedded.thetitans.item.armor.ItemHarcadiumArmor;
import com.electrifiedded.thetitans.item.armor.ItemTitanArmor;
import com.electrifiedded.thetitans.item.armor.ItemVoidArmor;
import com.electrifiedded.thetitans.item.armor.ItemWithreniumArmor;
import com.electrifiedded.thetitans.proxy.CommonProxy;
import com.electrifiedded.thetitans.util.Maths;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Timer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameType;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.lwjgl.opengl.GL11;

import java.util.List;

public class TitanBossBarGui extends Gui {
    private Minecraft mc;
    private Profiler profiler;
    public String uuidRemove;

    public TitanBossBarGui(Minecraft mc) {
        this.mc = mc;
        if (mc != null)
            profiler = mc.profiler;
        else
            profiler = null;
    }

    public void removeQueue(String uuid) {
        uuidRemove = uuid;
    }

    private boolean errorLogged = false;

    // PERF вЂ” styles used to be selected by a 15-deep instanceof forest which allocated a new
    // ResourceLocation (and several path strings) for every titan, every frame. Styles are immutable; texture
    // locations are refreshed only when the configured texture-set changes, not every render frame.
    private static final class TitanBarStyle {
        private final String pathSkin;
        private final int barWidth;
        private final int barHeight;
        private final int nameOffset;
        private final int offsetWords;
        private final int color;
        private ResourceLocation texture;
        private int setSkin = Integer.MIN_VALUE;

        private TitanBarStyle(String pathSkin, int barWidth, int barHeight, int nameOffset, int offsetWords, int color) {
            this.pathSkin = pathSkin;
            this.barWidth = barWidth;
            this.barHeight = barHeight;
            this.nameOffset = nameOffset;
            this.offsetWords = offsetWords;
            this.color = color;
        }

        private ResourceLocation getTexture() {
            if (texture == null || setSkin != TitanConfig.textures) {
                setSkin = TitanConfig.textures;
                texture = new ResourceLocation(TheTitans.getTextures("textures/gui", pathSkin));
            }
            return texture;
        }
    }

    private static final java.util.Map<Class<?>, TitanBarStyle> BAR_STYLES_GIANT = new java.util.IdentityHashMap<Class<?>, TitanBarStyle>();
    private static final TitanBarStyle GOLEM_BAR_SNOW = new TitanBarStyle("titanbars/snow_golem_titan.png", 185, 27, 20, 0, 12369084);
    private static final TitanBarStyle BAR_SLIME = new TitanBarStyle("titanbars/slime_titan.png", 191, 22, 12, 0, 5349438);
    private static final TitanBarStyle CUBE_BAR_MAGMA = new TitanBarStyle("titanbars/magma_cube_titan.png", 193, 24, 14, 0, 16579584);
    private static final TitanBarStyle BAR_OMEGAFISH = new TitanBarStyle("titanbars/omegafish.png", 183, 38, 27, 0, 7237230);
    private static final TitanBarStyle BAR_SPIDER = new TitanBarStyle("titanbars/spider_titan.png", 183, 29, 19, 0, 11013646);
    private static final TitanBarStyle SPIDER_BAR_CAVE = new TitanBarStyle("titanbars/cave_spider_titan.png", 183, 29, 19, 0, 11013646);
    private static final TitanBarStyle BAR_SKELETON = new TitanBarStyle("titanbars/skeleton_titan.png", 185, 28, 18, 0, 12698049);
    private static final TitanBarStyle SKELETON_BAR_WITHER = new TitanBarStyle("titanbars/wither_skeleton_titan.png", 195, 28, 18, 0, 12698049);
    private static final TitanBarStyle BAR_ZOMBIE = new TitanBarStyle("titanbars/zombie_titan.png", 185, 26, 16, 0, 7969893);
    private static final TitanBarStyle BAR_CREEPER = new TitanBarStyle("titanbars/creeper_titan.png", 185, 28, 18, 0, 894731);
    private static final TitanBarStyle ZOMBIE_BAR_PIG = new TitanBarStyle("titanbars/zombie_pigman_titan.png", 185, 31, 22, -1, 15373203);
    private static final TitanBarStyle BAR_BLAZE = new TitanBarStyle("titanbars/blaze_titan.png", 185, 35, 25, 0, 16167425);
    private static final TitanBarStyle BAR_GHAST = new TitanBarStyle("titanbars/ghast_titan.png", 195, 37, 25, 0, 12369084);
    private static final TitanBarStyle BAR_GARGOYLE = new TitanBarStyle("titanbars/gargoyle_king.png", 213, 35, 25, 0, 7237230);
    private static final TitanBarStyle GOLEM_BAR_IRON = new TitanBarStyle("titanbars/ultima_iron_golem_titan.png", 196, 30, 20, 0, 7237230);
    private static final TitanBarStyle COLOSSUS_BAR_ENDER = new TitanBarStyle("titanbars/ender_colossus.png", 194, 32, 19, 0, 13369594);
    private static final TitanBarStyle BAR_WITHERZILLA = new TitanBarStyle("titanbars/witherzilla.png", 256, 36, 26, 0, 15728880);
    private static final TitanBarStyle DRAGON_BAR_EXECUTOR = new TitanBarStyle("titanbars/executor_dragon.png", 256, 40, 30, 0, 0xFF00EA);

    static {
        BAR_STYLES_GIANT.put(EntitySnowGolemTitan.class, GOLEM_BAR_SNOW);
        BAR_STYLES_GIANT.put(EntitySlimeTitan.class, BAR_SLIME);
        BAR_STYLES_GIANT.put(EntityMagmaCubeTitan.class, CUBE_BAR_MAGMA);
        BAR_STYLES_GIANT.put(EntitySilverfishTitan.class, BAR_OMEGAFISH);
        BAR_STYLES_GIANT.put(EntitySpiderTitan.class, BAR_SPIDER);
        BAR_STYLES_GIANT.put(EntityCaveSpiderTitan.class, SPIDER_BAR_CAVE);
        BAR_STYLES_GIANT.put(EntitySkeletonTitan.class, BAR_SKELETON);
        BAR_STYLES_GIANT.put(EntityZombieTitan.class, BAR_ZOMBIE);
        BAR_STYLES_GIANT.put(EntityCreeperTitan.class, BAR_CREEPER);
        BAR_STYLES_GIANT.put(EntityPigZombieTitan.class, ZOMBIE_BAR_PIG);
        BAR_STYLES_GIANT.put(EntityBlazeTitan.class, BAR_BLAZE);
        BAR_STYLES_GIANT.put(EntityGhastTitan.class, BAR_GHAST);
        BAR_STYLES_GIANT.put(EntityGargoyleTitan.class, BAR_GARGOYLE);
        BAR_STYLES_GIANT.put(EntityIronGolemTitan.class, GOLEM_BAR_IRON);
        BAR_STYLES_GIANT.put(EntityEnderColossus.class, COLOSSUS_BAR_ENDER);
        BAR_STYLES_GIANT.put(EntityWitherzilla.class, BAR_WITHERZILLA);
        BAR_STYLES_GIANT.put(EntityExecutorDragon.class, DRAGON_BAR_EXECUTOR);
    }

    private static TitanBarStyle fetchBarStyleGiant(EntityTitan titan) {
        if (titan instanceof EntitySkeletonTitan && ((EntitySkeletonTitan)titan).fetchKindSkeleton() == 1)
            return SKELETON_BAR_WITHER;
        return BAR_STYLES_GIANT.get(titan.getClass());
    }

    private long discoveryBeatPrev = Long.MIN_VALUE / 2;

    @SubscribeEvent
    public void whenOverlayRender(RenderGameOverlayEvent.Pre event) {
        if (event.getType() != ElementType.BOSSHEALTH)
            return;
        if (mc == null || mc.player == null || mc.world == null)
            return;
        if (mc.playerController.getCurrentGameType() == GameType.SPECTATOR)
            return;
        boolean isCanceled = event.isCanceled();
        if (isCanceled)
            event.setCanceled(false);
        try {
            // DO NOT set GuiIngameForge.renderBossHealth=false вЂ” in 1.12.2 that method is what FIRES this
            // BOSSHEALTH event, so disabling it kills our own handler (bars vanished). Instead we cancel the event at
            // the end (below) so renderBossHealth() returns before drawing the vanilla purple GuiBossOverlay bar.
            FontRenderer fr = mc.fontRenderer;
            ScaledResolution res = new ScaledResolution(mc);
            String outstring = null;
            if (GuiEntry.instances.isEmpty() || !GuiEntry.instances.get(0).getEntry().equals(mc.player)) {
                GuiEntry.instances.clear();
                GuiEntry.create(mc.player, mc.player.getUniqueID(), false);
            }

            int offset = 0;
            int offsetB = 0;
            for (int i = 0; i < GuiEntry.instances.size(); i++) {
                if (GuiEntry.instances.get(i).uuidIs(uuidRemove)) {
                    uuidRemove = null;
                    GuiEntry.instances.remove(i);
                }

                else {
                    GuiEntry.instances.get(i).onUpdate();
                    if (i == 0)
                        if (TitanConfig.giantBarUse) {
                        boolean allowRender = false;
                        for (int ii = 0; ii < 4; ii++)
                            if (TitanConfig.giantBarPermUse || mc.player.getMaxHealth() >= 200.0F || mc.player.getAbsorptionAmount() >= 200.0F) {
                            allowRender = true;
                            break;
                        } else if (!mc.player.inventory.armorInventory.get(i).isEmpty() && mc.player.inventory.armorInventory.get(i).getItem() instanceof ItemTitanArmor)
                            if (!mc.player.capabilities.isCreativeMode) {
                            allowRender = true;
                            break;
                        }

                        if (GuiIngameForge.renderArmor == allowRender)
                            GuiIngameForge.renderArmor = !allowRender;
                        if (GuiIngameForge.renderHealth == allowRender)
                            GuiIngameForge.renderHealth = !allowRender;

                        if (allowRender)
                            drawUserBarMain(fr, GuiEntry.instances.get(i));
                    } else {
                        if (GuiIngameForge.renderArmor == false)
                            GuiIngameForge.renderArmor = true;
                        if (GuiIngameForge.renderHealth == false)
                            GuiIngameForge.renderHealth = true;
                    } else if (GuiEntry.instances.get(i).allowRender(mc.player))
                        if (GuiEntry.instances.get(i).getEntry() instanceof EntityLivingBase && TheTitans.checkFriendlyFire((EntityPlayer)mc.player, (EntityLivingBase)GuiEntry.instances.get(i).getEntry(), true)) {
                        if (offsetB < (int)(res.getScaledHeight() / 25)) {
                            drawBarUser(fr, offsetB, GuiEntry.instances.get(i));
                            offsetB ++;
                        }
                    } else if (TitanConfig.textures == 1) {
                        if (offset < (int)(res.getScaledHeight() / 45)) {
                            drawBarNew(fr, offset, GuiEntry.instances.get(i));
                            offset ++;
                        }
                    }
                }
            }

            List<?> entities = mc.world.loadedEntityList;
            List<EventEntry> events =  EventEntry.instances;
            if ((entities != null && !entities.isEmpty()) || (events != null && !events.isEmpty())) {
                if (TitanConfig.textures == 1) {
                    EntityTitan titan;
                    // PERF — this discovery pass walked every loaded entity (instanceof chain + checkFriendlyFire)
                    // on EVERY rendered frame. Existing bars update per frame through GuiEntry.onUpdate above; only the
                    // search for NEW bar owners is throttled to every 5 client ticks (<=0.25s delay before a bar appears).
                    long scanTick = mc.world.getTotalWorldTime();
                    boolean scan = scanTick - discoveryBeatPrev >= 5L || scanTick < discoveryBeatPrev;
                    if (scan)
                        discoveryBeatPrev = scanTick;
                    if (scan)
                        for (Object entity : entities)
                        if (entity != null) {
                        if (entity.equals(mc.player))
                            continue;
                        if (entity instanceof EntityTitan) {
                            titan = (EntityTitan)entity;
                            if(!titan.isDead && !GuiEntry.contains(titan.getUniqueID()))
                                GuiEntry.create(titan, titan.getUniqueID(), true);
                        }

                        else if (entity instanceof IMinion && ((IMinion)entity).fetchKindMinion() == EnumMinionType.LORD && !GuiEntry.contains(((Entity)entity).getUniqueID()))
                            GuiEntry.create(entity, ((Entity)entity).getUniqueID(), false).setColor(55, 55, 55);
                        else if (entity instanceof IMinion && ((IMinion)entity).getMaster() == null && ((IMinion)entity).fetchKindMinion() == EnumMinionType.GRANDMASTER && !GuiEntry.contains(((Entity)entity).getUniqueID()))
                            GuiEntry.create(entity, ((Entity)entity).getUniqueID(), false).setColor(255, 255, 255);
                        else if (entity instanceof IMinion && ((IMinion)entity).getMaster() == null && ((IMinion)entity).fetchKindMinion() == EnumMinionType.ARBITRATOR && !GuiEntry.contains(((Entity)entity).getUniqueID()))
                            GuiEntry.create(entity, ((Entity)entity).getUniqueID(), false).setColor(2, 2, 255);
                        else if (entity instanceof IMinion && ((IMinion)entity).getMaster() == null && ((IMinion)entity).fetchKindMinion() == EnumMinionType.TEMPLAR && !GuiEntry.contains(((Entity)entity).getUniqueID()))
                            GuiEntry.create(entity, ((Entity)entity).getUniqueID(), false).setColor(255, 2, 2);
                        else if (entity instanceof EntityWither && !GuiEntry.contains(((Entity)entity).getUniqueID()))
                            GuiEntry.create(entity, ((Entity)entity).getUniqueID(), false).setColor(66, 66, 66);
                        else if (entity instanceof EntityDragon && !GuiEntry.contains(((Entity)entity).getUniqueID()))
                            GuiEntry.create(entity, ((Entity)entity).getUniqueID(), false).setColor(202, 0, 252);

                        else if (entity instanceof com.electrifiedded.thetitans.entity.deity.EntityDeity && !((Entity)entity).isDead && !GuiEntry.contains(((Entity)entity).getUniqueID()))
                            GuiEntry.create(entity, ((Entity)entity).getUniqueID(), true);
                        else if (entity instanceof EntityOgPigZombieTitan && !GuiEntry.contains(((Entity)entity).getUniqueID()))
                            GuiEntry.create(entity, ((Entity)entity).getUniqueID(), false).setColor(233, 166, 166);
                        else if (entity instanceof EntityOgZombieTitan && !GuiEntry.contains(((Entity)entity).getUniqueID()))
                            GuiEntry.create(entity, ((Entity)entity).getUniqueID(), false).setColor(28, 105, 24);

                        else if (!((Entity)entity).isDead && entity instanceof EntityLivingBase && TheTitans.checkFriendlyFire((EntityPlayer)mc.player, (EntityLivingBase)entity, true) && !GuiEntry.contains(((Entity)entity).getUniqueID()))
                            GuiEntry.create(entity, ((Entity)entity).getUniqueID(), false);
                    }

                    for (EventEntry eve : events)
                        if (eve != null && !GuiEntry.contains(eve.fetchUUIDBar()))
                        GuiEntry.create(eve, eve.fetchUUIDBar(), null, false);
                }

                else {
                    int u = 0;
                    int v = 0;
                    int y = -12;
                    int barHeight = 24;
                    int i = 0;
                    offset = 0;
                    Entity entity = null;
                    for (int j = 0; j < entities.size(); ++j) {
                        entity = (Entity)entities.get(j);
                        if (entity != null && entity instanceof EntityTitan) {
                            EntityTitan titan = (EntityTitan)entity;
                            outstring = titan.getName();
                            if  (outstring != null && !(titan.isDead || titan.deathTicks > 185) && !titan.fetchWaiting() && !titan.isInvisible()) {
                                TitanBarStyle style = fetchBarStyleGiant(titan);
                                if (style != null) {
                                    barHeight = style.barHeight;
                                    y += i + barHeight;
                                    int textureV = v;
                                    if (titan instanceof EntityCreeperTitan && ((EntityCreeperTitan)titan).getPowered())
                                        textureV = barHeight + barHeight;
                                    int barColor = titan instanceof EntityWitherzilla
                                            ? 15728880 - (int)(MathHelper.cos(titan.ticksExisted * 0.05F) * 15728880)
                                            : style.color;
                                    drawBarX(style.getTexture(), titan, outstring, fr, !titan.isDead, titan.getHealth() / titan.getMaxHealth(), style.barWidth, barHeight, y, u, textureV, style.nameOffset, y + style.offsetWords, barColor, res.getScaledWidth(), mc.getRenderPartialTicks());
                                    i++;
                                }
                            }
                        }
                    }
                }
            }

            mc.renderEngine.bindTexture(Gui.ICONS);
            // cancel the BOSSHEALTH event so renderBossHealth() bails out before drawing the vanilla purple
            // GuiBossOverlay bar вЂ” our custom titan bars replace it. (Restores the vanilla bar only if the custom GUI
            // is disabled via giantBarUse, in which case we drew nothing.)
            if (TitanConfig.giantBarUse)
                event.setCanceled(true);
            else if (event.isCanceled() != isCanceled)
                event.setCanceled(isCanceled);
        } catch (Throwable e) {
            if (!errorLogged) {
                errorLogged = true;
                TheTitans.error("TitanBossBarGui render crashed (bars suppressed this frame) вЂ” reported once:", e);
            }
        }
    }

    private void drawUserBarMain(FontRenderer fr, GuiEntry entry) {
        GlStateManager.pushMatrix();
        GL11.glEnable(2977);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        ScaledResolution res = new ScaledResolution(mc);
        String output;
        int shakeX = (int)(!entry.runDamaged() ? 0 : Maths.random(-5, 5) * MathHelper.clamp((float)(entry.fetchHarmLast() / (entry.getMax(0) / 5.0F)), 0.1F, 1.0F));
        int shakeY = (int)(!entry.runDamaged() ? 0 : Maths.random(-5, 5) * MathHelper.clamp((float)(entry.fetchHarmLast() / (entry.getMax(0) / 5.0F)), 0.1F, 1.0F));
        int width = (int)((res.getScaledWidth() / 2) - 95) + shakeX;
        int height = (int)(res.getScaledHeight() - 45) + shakeY;
        mc.getTextureManager().bindTexture(entry.getTexture());
        if (entry.runDamaged() || entry.getCurrent(0) <= 0.0F)
            drawTexturedModalRect(width, height, 0, TitanConfig.texturesBar == 1 ? 63 : 18, 90, 9);
        else if (entry.healed())
            drawTexturedModalRect(width, height, 0, TitanConfig.texturesBar == 1 ? 72 : 27, 90, 9);
        else
            drawTexturedModalRect(width, height, 0, TitanConfig.texturesBar == 1 ? 54 : 9, 90, 9);
        if (entry.fetchIndicator(0) > entry.getCurrent(0)) {
            color();
            drawBar(width, height, 0, TitanConfig.texturesBar == 1 ? 45 : 0, 90, 9, (float)entry.fetchPercIndicator(0));
        }

        if (entry.getCurrent(0) > 0.0D) {
            EntityPlayer p = (EntityPlayer)entry.getEntry();
            if (p.isPotionActive(CommonProxy.advancedWither))
                color(0.0F, 0.0F, 0.0F);
            else if (p.isPotionActive(CommonProxy.giantRadiationCreeper))
                color(0.0F, 128.0F, 0.0F);
            else if (p.isPotionActive(CommonProxy.electricJudgment))
                color(255.0F, 200.0F, 0.0F);
            else if (p.isPotionActive(MobEffects.POISON))
                color(55.0F, 100.0F, 0.0F);
            else if (p.isPotionActive(MobEffects.WITHER))
                color(55.0F, 10.0F, 10.0F);
            else if (p.isPotionActive(CommonProxy.bleeding))
                color(155.0F, 0.0F, 0.0F);
            else
                color(255.0F, 0.0F, 0.0F);
            drawBar(width, height, 0, TitanConfig.texturesBar == 1 ? 45 : 0, 90, 9, (float)entry.fetchPercSmoothCurrent(0));
            color(255.0F, 255.0F, 255.0F, 175.0F);
            drawBar(width, height, 0, TitanConfig.texturesBar == 1 ? 81 : 36, 90, 9, (float)entry.fetchPercSmoothCurrent(0));
        }

        if (entry.getCurrent(1) > 0.0D) {
            EntityPlayer p = (EntityPlayer)entry.getEntry();
            if (p.isPotionActive(CommonProxy.advancedWither))
                color(10.0F, 10.0F, 0.0F);
            else if (p.isPotionActive(CommonProxy.giantRadiationCreeper))
                color(10.0F, 128.0F, 0.0F);
            else if (p.isPotionActive(CommonProxy.electricJudgment))
                color(255.0F, 255.0F, 50.0F);
            else if (p.isPotionActive(MobEffects.POISON))
                color(75.0F, 100.0F, 0.0F);
            else if (p.isPotionActive(MobEffects.WITHER))
                color(55.0F, 55.0F, 10.0F);
            else if (p.isPotionActive(CommonProxy.bleeding))
                color(200.0F, 155.0F, 0.0F);
            else
                color(255.0F, 200.0F, 0.0F);
            drawBar(width, height, 0, TitanConfig.texturesBar == 1 ? 45 : 0, 90, 9, (float)entry.fetchPercSmoothCurrent(1));
            color(255.0F, 255.0F, 255.0F, 175.0F);
            drawBar(width, height, 0, TitanConfig.texturesBar == 1 ? 81 : 36, 90, 9, (float)entry.fetchPercSmoothCurrent(1));
        }

        color();
        float armor = 0;
        float armorResist = 0.0F;
        int armorType = -1;
        int armors[] = new int[5];
        for (int i = 0; i < 4; i++) {
            if (!((EntityPlayer)entry.getEntry()).inventory.armorInventory.get(i).isEmpty() && ((EntityPlayer)entry.getEntry()).inventory.armorInventory.get(i).getItem() instanceof ItemTitanArmor) {
                ItemTitanArmor item = ((ItemTitanArmor)((EntityPlayer)entry.getEntry()).inventory.armorInventory.get(i).getItem());
                armor += (int)(item.fetchArmourGiant());
                armorResist += item.fetchResistanceGiant();
                if (item instanceof ItemAdminiumArmor) {
                    if (armorType < 5)
                        armorType = 5;
                    armors[0] ++;
                }

                else if (item instanceof ItemVoidArmor) {
                    if (armorType < 4)
                        armorType = 4;
                    armors[1] ++;
                }

                else if (item instanceof ItemWithreniumArmor) {
                    if (armorType < 3)
                        armorType = 3;
                    armors[2] ++;
                }

                else if (item instanceof ItemHarcadiumArmor) {
                    if (armorType < 2)
                        armorType = 2;
                    armors[3] ++;
                }

                else if (item instanceof ItemDemontiumArmor) {
                    if (armorType < 1)
                        armorType = 1;
                    armors[4] ++;
                }
            }
        }

        if (armorType > -1) {
            int offsetA = 0;
            int offsetB = 0;
            if (armors[0] > 0)
                offsetA = 5 - armors[0];
            else if (armors[1] > 0)
                offsetA = 5 - armors[1];
            else if (armors[2] > 0)
                offsetA = 5 - armors[2];
            else if (armors[3] > 0)
                offsetA = 5 - armors[3];
            else if (armors[4] > 0)
                offsetA = 5 - armors[4];
            if (armorResist <= 0.05F)
                offsetB = 1;
            else if (armorResist <= 0.2F)
                offsetB = 2;
            else if (armorResist <= 0.12F)
                offsetB = 3;
            else if (armorResist <= 0.06F)
                offsetB = 4;
            drawTexturedModalRect(width + 5 + shakeX, height + shakeY - 12, 90 + (9 * offsetA), (armorType * 9), 9, 9);
            drawTexturedModalRect(width + 50 + shakeX, height + shakeY - 12, 90 + (9 * offsetB), (armorType * 9), 9, 9);
            output = parseFloat(armor);
            fr.drawStringWithShadow(output, (width + 16 + shakeX) + (output.length()), height + shakeY - 11, 0xFFE0E0E0);
            output = armorResist * 100.0F + "%";
            fr.drawStringWithShadow(output, (width + 58 + shakeX) + (output.length()), height + shakeY - 11, 0xFFE0E0E0);
        }

        else {
            int offsetA = 0;
            armor = ((EntityPlayer)entry.getEntry()).getTotalArmorValue();
            int offsetB = 0;
            for ( ; armor > 0 && offsetA < 10; ) {
                if (armor > 1) {
                    offsetB = 1;
                    armor -= 2;
                }

                else {
                    offsetB = 3;
                    armor --;
                }

                drawTexturedModalRect(width + (offsetA * 9) + shakeX, height + shakeY - 12, 90 + (9 * offsetB), 0, 9, 9);
                offsetA ++;
            }
        }

        output = parseFloat((float)entry.getCurrent(0) + (float)entry.getCurrent(1)) + (entry.getCurrent(0) < entry.getMax(0) ? " / " + parseFloat((float)entry.getMax(0)) : "");
        fr.drawStringWithShadow(output, (width + 45) - (fr.getStringWidth(output) / 2), height + 1, entry.getCurrent(0) > 0.0F ? 0xFFE0E0E0 : 0xFFE00000);
        color();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private void drawBarUser(FontRenderer fr, int offset, GuiEntry entry) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.75F, 0.75F, 0.75F);
        GL11.glEnable(2977);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        String output;
        int shakeX = (int)(!entry.runDamaged() ? 0 : Maths.random(-5, 5) * MathHelper.clamp((float)(entry.fetchHarmLast() / (entry.getMax(0) / 5.0F)), 0.1F, 1.0F));
        int shakeY = (int)(!entry.runDamaged() ? 0 : Maths.random(-5, 5) * MathHelper.clamp((float)(entry.fetchHarmLast() / (entry.getMax(0) / 5.0F)), 0.1F, 1.0F));
        int width = 10 + shakeX;
        int height = 30 + (offset * 32) + shakeY;
        mc.getTextureManager().bindTexture(entry.getTexture());
        if (entry.runDamaged() || entry.getCurrent(0) <= 0.0F)
            drawTexturedModalRect(width, height, 0, TitanConfig.texturesBar == 1 ? 63 : 18, 90, 9);
        else if (entry.healed())
            drawTexturedModalRect(width, height, 0, TitanConfig.texturesBar == 1 ? 72 : 27, 90, 9);
        else
            drawTexturedModalRect(width, height, 0, TitanConfig.texturesBar == 1 ? 54 : 9, 90, 9);
        if (entry.fetchIndicator(0) > entry.getCurrent(0)) {
            color();
            drawBar(width, height, 0, TitanConfig.texturesBar == 1 ? 45 : 0, 90, 9, (float)entry.fetchPercIndicator(0));
        }

        if (entry.getCurrent(0) > 0.0D) {
            if (((EntityLivingBase)entry.getEntry()).isPotionActive(MobEffects.WITHER))
                color(55.0F, 10.0F, 10.0F);
            else if (((EntityLivingBase)entry.getEntry()).isPotionActive(MobEffects.POISON))
                color(55.0F, 100.0F, 0.0F);
            else
                if (entry.getEntry() instanceof EntityTameable || entry.getEntry() instanceof EntityHorse)
                color(55.0F, 155.0F, 255.0F);
            else if (entry.getEntry() instanceof net.minecraft.entity.monster.EntityGolem)
                color(65.0F, 122.0F, 255.0F);
            else if (entry.getEntry() instanceof EntityPlayer)
                color(255.0F, 0.0F, 0.0F);
            else
                color(55.0F, 200.0F, 0.0F);
            drawBar(width, height, 0, TitanConfig.texturesBar == 1 ? 45 : 0, 90, 9, (float)entry.fetchPercSmoothCurrent(0));
            color(255.0F, 255.0F, 255.0F, 175.0F);
            drawBar(width, height, 0, TitanConfig.texturesBar == 1 ? 81 : 36, 90, 9, (float)entry.fetchPercSmoothCurrent(0));
        }

        if (entry.getCurrent(1) > 0.0D) {
            if (((EntityLivingBase)entry.getEntry()).isPotionActive(MobEffects.WITHER))
                color(55.0F, 55.0F, 10.0F);
            else if (((EntityLivingBase)entry.getEntry()).isPotionActive(MobEffects.POISON))
                color(55.0F, 155.0F, 0.0F);
            else
                if (entry.getEntry() instanceof EntityTameable)
                color(55.0F, 255.0F, 255.0F);
            else if (entry.getEntry() instanceof EntityPlayer)
                color(255.0F, 200.0F, 0.0F);
            else
                color(150.0F, 200.0F, 0.0F);
            drawBar(width, height, 0, TitanConfig.texturesBar == 1 ? 45 : 0, 90, 9, (float)entry.fetchPercSmoothCurrent(1));
            color(255.0F, 255.0F, 255.0F, 175.0F);
            drawBar(width, height, 0, TitanConfig.texturesBar == 1 ? 81 : 36, 90, 9, (float)entry.fetchPercSmoothCurrent(1));
        }

        color();
        int offsetA = 0;
        int armor = ((EntityLivingBase)entry.getEntry()).getTotalArmorValue();
        int offsetB = 0;
        for ( ; armor > 0 && offsetA < 10; ) {
            if (armor > 1) {
                offsetB = 1;
                armor -= 2;
            }

            else {
                offsetB = 3;
                armor --;
            }

            drawTexturedModalRect(width + (offsetA * 9) + shakeX, height + shakeY + 10, 90 + (9 * offsetB), 0, 9, 9);
            offsetA ++;
        }

        output = parseFloat((float)entry.getCurrent(0) + (float)entry.getCurrent(1)) + (entry.getCurrent(0) < entry.getMax(0) ? " / " + parseFloat((float)entry.getMax(0)) : "");
        fr.drawStringWithShadow(output, (width + 45) - (fr.getStringWidth(output) / 2), height + 1, entry.getCurrent(0) > 0.0F ? 0xFFE0E0E0 : 0xFFE00000);
        if (entry.is(Entity.class))
            output = ((Entity)entry.getEntry()).getName();
        else
            output = "Unknown";
        fr.drawStringWithShadow(output, (width + 45) - (fr.getStringWidth(output) / 2), height - 9, 0xFFC0C0C0);
        color();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private void drawBarX(ResourceLocation texture, EntityTitan titan, String outstring, FontRenderer fr, boolean flag, float gfHealth, int barWidth, int barHeight, int y, int u, int v, int offset, int namey, int color, int screenWidth, float partialTicks) {
        barWidth = barWidth - titan.deathTicks;
        u = u + (titan.deathTicks / 2);
        if (flag && barWidth > 0) {
            // PERF — use resolution/partialTicks already computed for this overlay frame; the old code
            // allocated a new Timer and ScaledResolution once per titan bar every frame.
            int width = screenWidth;
            int barWidthFilled = (int)(gfHealth * (barWidth + 1));
            int x = width / 2 - barWidth / 2;
            mc.getTextureManager().bindTexture(texture);
            GL11.glEnable(2977);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            drawTexturedModalRect(x, y, u, v, barWidth, barHeight);
            if (barWidthFilled > 0)
                drawTexturedModalRect(x, y, u, v + barHeight, barWidthFilled, barHeight);
            if (titan instanceof EntityCreeperTitan && ((EntityCreeperTitan)titan).getPowered())
                drawTexturedModalRect(x, y, u + (titan.ticksExisted * (!titan.isEntityAlive() ? 1 : 3)) + (int)partialTicks, v + (barHeight * 4), barWidth, barHeight);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            fr.drawStringWithShadow(outstring, (width / 2) - (fr.getStringWidth(outstring) / 2) - offset, namey, color);
            String health = "";
            health = parseFloat(titan.getHealth());
            fr.drawStringWithShadow(health, (width / 2) - (fr.getStringWidth(health) / 2), namey - 10, 12369084);
            GlStateManager.disableBlend();
        }
    }

    private void drawBarNew(FontRenderer fr, int offset, GuiEntry entry) {
        GlStateManager.pushMatrix();
        GL11.glEnable(2977);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        ScaledResolution res = new ScaledResolution(mc);
        String output;
        int shakeX = (int)(!entry.runDamaged() ? 0 : Maths.random(-5, 5) * MathHelper.clamp((float)(entry.fetchHarmLast() / (entry.getMax(0) / 100.0F)), 0.1F, 1.0F));
        int shakeY = (int)(!entry.runDamaged() ? 0 : Maths.random(-5, 5) * MathHelper.clamp((float)(entry.fetchHarmLast() / (entry.getMax(0) / 100.0F)), 0.1F, 1.0F));
        int width = (res.getScaledWidth() / 2) - 104 + shakeX;
        int height = (offset * 35) + shakeY;
        float size = !entry.allowStaminaRender() ? 0.0F : MathHelper.clamp((((150.0F * 4.0F) - entry.deathTicks) / (65.0F * 4.0F)) - 0.8F, 0.0F, 1.0F);
        mc.getTextureManager().bindTexture(entry.getTexture());
        drawTexturedModalRect(width, height, 0, 0, 208, 30);
        if (entry.allowStaminaRender() && entry.deathTicks < 150)
            paintBarMirrored(width + 25, height + 20, 0, 62, 80, 3, size);
        if (entry.fetchIndicator(0) > entry.getCurrent(0)) {
            color();
            drawBar(width + 2, height + 14, 0, 45, 204, 5, (float)entry.fetchPercIndicator(0));
            drawBar(width + 2, height + 14, 0, 40, 204, 5, (float)entry.fetchPercIndicator(0));
        }

        if (entry.getCurrent(0) > 0.0D) {
            color(entry.rgb[0], entry.rgb[1], entry.rgb[2]);
            drawBar(width + 2, height + 14, 0, 35, 204, 5, (float)entry.fetchPercCurrent(0));
            color();
            drawBar(width + 2, height + 14, 0, 30, 204, 5, (float)entry.fetchPercCurrent(0));
        }

        if (entry.allowStaminaRender() && entry.fetchIndicator(1) > entry.getCurrent(2)) {
            color();
            paintBarMirrored(width + 25, height + 20, 0, 53, 80, 3, (float)(entry.fetchPercIndicator(2) * size));
            paintBarMirrored(width + 25, height + 20, 0, 50, 80, 3, (float)(entry.fetchPercIndicator(2) * size));
        }

        if (entry.allowStaminaRender() && entry.getCurrent(2) > 0.0F) {
            color(entry.rgb[3], entry.rgb[4], entry.rgb[5]);
            paintBarMirrored(width + 25, height + 20, 0, 53, 80, 3, (float)(entry.fetchPercCurrent(2) * size));
            color();
            paintBarMirrored(width + 25, height + 20, 0, 50, 80, 3, (float)(entry.fetchPercCurrent(2) * size));
        }

        int nameColor = 0xC0C0C0;
        if (entry.getEntry() instanceof com.electrifiedded.thetitans.entity.deity.IDeity)
            nameColor = ((com.electrifiedded.thetitans.entity.deity.IDeity)entry.getEntry()).fetchTintLabel();
        else if (entry.getEntry() instanceof IMinion)
            nameColor = TheTitans.getMinionColors(((IMinion)entry.getEntry()).fetchKindIntMinion());
        else if (entry.getEntry() instanceof EntityWither)
            nameColor = 0x312F31;
        else if (entry.getEntry() instanceof EntityDragon)
            nameColor = 0xB4013F;
        if (entry.is(Entity.class))
            output = ((Entity)entry.getEntry()).getName();
        else if (entry.is(EventEntry.class))
            output = ((EventEntry)entry.getEntry()).getName();
        else
            output = "Unknown";
        fr.drawStringWithShadow(output, (width + 104) - (fr.getStringWidth(output) / 2), height + 3, 0xFF000000 | nameColor);
        output = parseFloat((float)entry.getCurrent(0));
        fr.drawStringWithShadow(output, (width + 104) - (fr.getStringWidth(output) / 2), height + 14, 0xFFC0C0C0);
        if (entry.fetchTotalHarmLast() != 0.0D) {
            output = parseFloat((float)entry.fetchTotalHarmLast());
            fr.drawStringWithShadow(entry.fetchTotalHarmLast() >= entry.getMax(0) ? entry.fetchTotalHarmLast() >= entry.getMax(0) + (entry.getMax(0) * 0.1) ? "Overkill" : "Killed" : output, width + 200 - (fr.getStringWidth(output) / 2) + (entry.fetchTicksHurt() / 4), height + 4, 0xFFC02000);
        }

        color();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private String parseFloat(float value) {
        String input;
        if (value >= 1000000000000000000000000000000000000.0F)
            input = "Infinity (" + (int)Math.floor((value / Float.MAX_VALUE) * 100) + "%)";
        else if (value >= 1000000000000000000000000000000000.0F)
            input = Math.floor(value * 0.00000000000000000000000000000001F) * 0.1F + " Decillion";
        else if (value >= 1000000000000000000000000000000.0F)
            input = Math.floor(value * 0.00000000000000000000000000001F) / 10 + " Nonillion";
        else if (value >= 1000000000000000000000000000.0F)
            input = Math.floor(value * 0.00000000000000000000000001F) / 10 + " Octillion";
        else if (value >= 1000000000000000000000000.0F)
            input = Math.floor(value * 0.00000000000000000000001F) / 10 + " Septillion";
        else if (value >= 1000000000000000000000.0F)
            input = Math.floor(value * 0.00000000000000000001F) / 10F + " Sextillion";
        else if (value >= 1000000000000000000.0F)
            input = Math.floor(value * 0.00000000000000001F) / 10 + " Quintillion";
        else if (value >= 1000000000000000.0F)
            input = Math.floor(value * 0.00000000000001F) / 10 + " Quadrillion";
        else if (value >= 1000000000000.0F)
            input = Math.floor(value * 0.00000000001F) / 10 + " Trillion";
        else if (value >= 1000000000.0F)
            input = Math.floor(value * 0.00000001F) / 10 + " Billion";
        else if (value >= 1000000.0F)
            input =  Math.floor(value * 0.00001F) / 10 + " Million";
        else
            input = (int)Math.floor(value) + "";
        return input;
    }

    private void drawBar(int x, int y, int u, int v, int width, int height, float amount) {
        amount = MathHelper.clamp(amount, 0.0F, 1.0F);
        drawTexturedModalRect(x, y, u, v, (int)(width * amount), height);
    }

    private void paintBarMirrored(int x, int y, int u, int v, int width, int height, float amount) {
        amount = MathHelper.clamp(amount, 0.0F, 1.0F);
        drawTexturedModalRect(x + (int)(width - (width * amount)), y, u + (int)(width - (width * amount)), v, (int)(width * amount), height);
        drawTexturedModalRect(x + width, y, width, v, (int)(width * amount), height);
    }

    private void color(float... values) {
        int index = 0;
        float[] rgba = {255.0F, 255.0F, 255.0F, 255.0F};
        for (int i = 0; i < values.length; i++)
            if (index < 4) {
            rgba[index] = values[i];
            index ++;
        }

        else
            break;
        GlStateManager.color(MathHelper.clamp(rgba[0] / 255.0F, 0.0F, 1.0F), MathHelper.clamp(rgba[1] / 255.0F, 0.0F, 1.0F), MathHelper.clamp(rgba[2] / 255.0F, 0.0F, 1.0F), MathHelper.clamp(rgba[3] / 255.0F, 0.0F, 1.0F));
    }
}
