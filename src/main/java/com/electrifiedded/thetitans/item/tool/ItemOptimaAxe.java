package com.electrifiedded.thetitans.item.tool;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.entity.misc.EntityImmortalItem;
import com.electrifiedded.thetitans.entity.titan.EntityGammaLightning;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitanPart;
import com.electrifiedded.thetitans.entity.titan.EntityTitanSpirit;
import com.electrifiedded.thetitans.entity.titan.EntityUrLightning;
import com.electrifiedded.thetitans.entity.titan.EntityWitherzilla;
import com.electrifiedded.thetitans.item.weapon.ItemTitanSword;
import com.electrifiedded.thetitans.proxy.ClientProxy;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.registry.TitanAudio;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockOre;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemOptimaAxe extends ItemTitanSword {
    public static ToolMaterial toolMaterialOptimaEnum = EnumHelper.addToolMaterial("Optima", Integer.MAX_VALUE, 2, 9999999F, Float.MAX_VALUE / 2.0F, 0);
    private int soundType;

    public ItemOptimaAxe() {
        super("", toolMaterialOptimaEnum);
        soundType = 0;
        setTranslationKey("optima_axe");
        setRegistryName(new ResourceLocation(TheTitans.MODID_V, "optima_axe"));
    }

    public boolean isToolStack(ItemStack stack) {
        return true;
    }

    public boolean canHarvestBlock(IBlockState state) {
        return true;
    }

    public boolean isDamageable() {
        return false;
    }

    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        return new EntityImmortalItem(world, location, itemstack);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        if (stack.getItemDamage() > 0) {
            tooltip.add("§3§lThe ultimate weapon.");
            tooltip.add("§3§lCuts through all defences, even hacks.");
            tooltip.add("§3§lNormally owned by Frotheru");
        }

        else {
            tooltip.add("§l§k§3§lThe ultimate weapon.");
            tooltip.add("§l§k§3§lCuts through all defences, even hacks.");
            tooltip.add("§l§k§3§lNormally owned by Frotheru");
        }
    }

    public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entity) {
        return true;
    }

    private void grantAdvice(EntityPlayer player) {
        List<Entity> found1 = player.world.getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().grow(16.0D, 16.0D, 16.0D));
        if ((found1 != null) && (!found1.isEmpty())) {
            for (Entity entity : found1) {
                if (((entity instanceof IMob || entity instanceof EntityPlayer))) {
                    if (player.canEntityBeSeen(entity)) {
                        player.sendMessage(new TextComponentString("Optima Axe: §l§oThe " + ((EntityLivingBase)entity).getName() +" is " + player.getDistance(entity) + "§l§o blocks away from you."));
                    }

                    else {
                        player.sendMessage(new TextComponentString("Optima Axe: §l§oAn unseen mob is located out of your §l§osight " + player.getDistance(entity) + " blocks away from you."));
                        player.sendMessage(new TextComponentString("Optima Axe: §l§oThe mob's health: " + ((EntityLivingBase)entity).getHealth() + "/" + ((EntityLivingBase)entity).getMaxHealth()));
                        player.sendMessage(new TextComponentString("Optima Axe: §l§oThe mob's name: " + ((EntityLivingBase)entity).getName()));
                        player.playSound(net.minecraft.init.SoundEvents.ENTITY_WITHER_HURT, 10.0F, (player.getRNG().nextFloat() - player.getRNG().nextFloat()) * 0.2F + 0.5F);
                    }
                }
            }
        }
    }

    private void adviceOffer(EntityPlayer player) {
        int y = MathHelper.floor(player.posY);
        int x = MathHelper.floor(player.posX);
        int z = MathHelper.floor(player.posZ);
        for (int l1 = -8; l1 <= 8; l1++) {
            for (int i2 = -8; i2 <= 8; i2++) {
                for (int j = -8; j <= 8; j++) {
                    int j2 = x + l1;
                    int k = y + j;
                    int l = z + i2;
                    Block block = player.world.getBlockState(new BlockPos(j2, k, l)).getBlock();
                    if ((block instanceof BlockOre)) {
                        player.sendMessage(new TextComponentString("Optima Axe: §l§oI sense a " + block.getLocalizedName() + "§l§o within atleast " + player.getDistance(j2, k, l) + " blocks of you."));
                        player.playSound(net.minecraft.init.SoundEvents.ENTITY_WITHER_HURT, 10.0F, 0.6F);
                    }
                }
            }
        }
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack heldStack = player.getHeldItem(hand);
        // engage the bow-draw dash like the base ItemTitanSword (this override dropped setActiveHand,
        // which is why the Optima Axe lost its flight). onPlayerStoppedUsing then fires the launch.
        player.setActiveHand(hand);
        if (player.world.isRemote) {
            if (player.isSneaking())
                grantAdvice(player);
            else
                adviceOffer(player);
        }

        if (playerUltimaHas(player)) {
            if (!world.isRemote) {
                MinecraftServer server = player.getServer();
                WorldServer worldserver = server.worlds[0];
                WorldInfo worldinfo = worldserver.getWorldInfo();
                worldinfo.setRainTime(9999999);
                worldinfo.setThunderTime(1000000);
                worldinfo.setRaining(true);
                worldinfo.setThundering(true);
            }

            List<Entity> list = player.world.getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().grow(200.0D, 100.0D, 200.0D));
            if ((list != null) && (!list.isEmpty())) {
                for (Entity entity : list) {
                    if ((entity != null) && entity.isEntityAlive() && entity instanceof EntityLivingBase && !(entity instanceof EntityTitan) && entity != player) {
                        entity.copyLocationAndAnglesFrom(player);
                        entity.attackEntityFrom(DamageSourceExtra.lightningBolt, 49F);
                        player.world.addWeatherEffect(new EntityGammaLightning(player.world, entity.posX, entity.posY + entity.height, entity.posZ, player.getRNG().nextFloat(), player.getRNG().nextFloat(), player.getRNG().nextFloat()));
                        entity.attackEntityFrom(DamageSource.causePlayerDamage(player).setDamageBypassesArmor().setDamageIsAbsolute(), 49F);
                        if (entity instanceof EntityLivingBase && !(entity instanceof EntityTitan) && (entity.height >= 6.0F || entity.isEntityInvulnerable(DamageSource.OUT_OF_WORLD))) {
                            ((EntityLivingBase)entity).setDead();
                            ((EntityLivingBase)entity).setHealth(0.0F);
                            ((EntityLivingBase)entity).attackEntityFrom(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
                        }
                    }
                }
            }
        }

        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, heldStack);
    }

    private boolean playerUltimaHas(EntityPlayer player) {
        for (ItemStack s : player.inventory.mainInventory)
            if (!s.isEmpty() && s.getItem() == TitanGear.ultimaBlade)
            return true;
        return false;
    }

    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        attacker.world.getGameRules().setOrCreateGameRule("keepInventory", "true");
        float extradamage = EnchantmentHelper.getModifierForCreature(attacker.getHeldItemMainhand(), target.getCreatureAttribute());
        int knockbackAmount = EnchantmentHelper.getKnockbackModifier(attacker);
        int bonusdamage = 1;
        if (target != null) {
            target.setFire(Integer.MAX_VALUE);
            if (target.world.isRemote)
                target.addPotionEffect(new PotionEffect(ClientProxy.electricJudgment, Integer.MAX_VALUE, 19));
            target.hurtResistantTime = 0;
            if (((target instanceof EntityTitan)) && (((EntityTitan)target).allowHurtByUserBe())) {
                if (target instanceof EntityWitherzilla)
                    bonusdamage = 40;
                if (attacker instanceof EntityPlayer)
                    ((EntityTitan)target).attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)attacker), getAttackDamage());
                ((EntityTitan)target).hurt((10000.0F * bonusdamage) - extradamage);
                ((EntityTitan)target).setAttackTarget(attacker);
                ((EntityTitan)target).appendVelocityGiant(-MathHelper.sin(attacker.rotationYaw * 3.1415927F / 180.0F) * 2F + knockbackAmount, 0.5D + knockbackAmount, MathHelper.cos(attacker.rotationYaw * 3.1415927F / 180.0F) * 2F + knockbackAmount);
            }

            else if (!(target instanceof EntityTitan)) {
                if ((target.height == 50F && target.width == 15F) || EntityTitan.isSpawnBossToExemptOre(target)) {
                    try {
                        ReflectionHelper.findField(target.getClass(), new String[] {
                            "hurt_timer"
                        }
                        ).setInt(target, 0);
                    }

                    catch (Exception e) {
                        target.hurtResistantTime = 0;
                    }

                    double originalHealth = target.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue();
                    target.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(0D);
                    target.attackEntityFrom(DamageSourceExtra.causeGiantHarmAnti(attacker).setDamageBypassesArmor().setDamageIsAbsolute(), 40.0F);
                    if (target.world.isRemote)
                        target.addPotionEffect(new PotionEffect(ClientProxy.death, Integer.MAX_VALUE, 19));
                    ++target.deathTime;
                    target.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(originalHealth);
                }

                target.setHealth(MathHelper.clamp(target.getHealth() - this.getAttackDamage() - extradamage, 0.0F, target.getMaxHealth()));
                target.motionX = -MathHelper.sin(attacker.rotationYaw * 3.1415927F / 180.0F) * 6F + knockbackAmount;
                target.motionY = 6D + knockbackAmount;
                target.motionZ = MathHelper.cos(attacker.rotationYaw * 3.1415927F / 180.0F) * 6F + knockbackAmount;
                if (!target.isEntityAlive()) {
                    target.onDeath(DamageSource.MAGIC);
                    target.world.setEntityState(target, (byte)3);
                }
            }
        }

        return super.hitEntity(stack, target, attacker);
    }

    public EnumRarity getRarity(ItemStack stack) {
        return TheTitans.titan;
    }

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
        appendEnch(stack, Enchantments.SHARPNESS);
        appendEnch(stack, Enchantments.SMITE);
        appendEnch(stack, Enchantments.BANE_OF_ARTHROPODS);
        appendEnch(stack, Enchantments.FIRE_ASPECT);
        appendEnch(stack, Enchantments.LOOTING);
    }

    private void appendEnch(ItemStack stack, net.minecraft.enchantment.Enchantment ench) {
        if (EnchantmentHelper.getEnchantmentLevel(ench, stack) <= 0)
            stack.addEnchantment(ench, 100);
    }

    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());
        else
            stack.getTagCompound().setBoolean("Unbreakable", true);
        entityIn.extinguish();
        if ((entityIn instanceof EntityPlayer) && !worldIn.isRemote) {
            EntityPlayer p = (EntityPlayer)entityIn;
            // TODO advancements — TitanTrophies.optimaAxe is now an advancement-id String; runGrant via CriteriaTriggers once advancements are added
            if (playerUltimaHas(p)) {
                if (p.getRNG().nextInt(5) == 0) {
                    for (int l = 0; l < 5; l++) {
                        int i = MathHelper.floor(p.posX + (p.getRNG().nextInt() * 200 - 100));
                        int j = MathHelper.floor(p.posY + (p.getRNG().nextInt() * 100 - 50));
                        int k = MathHelper.floor(p.posZ + (p.getRNG().nextInt() * 200 - 100));
                        EntityUrLightning entitylightning = new EntityUrLightning(p.world, i, j, k);
                        BlockPos below = new BlockPos(i, j - 1, k);
                        if (p.world.getBlockState(below).isSideSolid(p.world, below, net.minecraft.util.EnumFacing.UP)
                            && p.world.checkNoEntityCollision(entitylightning.getEntityBoundingBox())
                            && p.world.getCollisionBoxes(entitylightning, entitylightning.getEntityBoundingBox()).isEmpty()
                            && !p.world.containsAnyLiquid(entitylightning.getEntityBoundingBox())) {
                            p.world.addWeatherEffect(entitylightning);
                        }
                    }
                }

                List<Entity> list = p.world.getEntitiesWithinAABBExcludingEntity(p, p.getEntityBoundingBox().grow(200.0D, 100.0D, 200.0D));
                if ((list != null) && (!list.isEmpty())) {
                    for (Entity entity : list) {
                        if ((entity != null) && entity.isEntityAlive() && entity instanceof EntityLivingBase && p.getRNG().nextInt(100) == 0 && entity != entityIn) {
                            entity.addVelocity(-MathHelper.sin(entityIn.rotationYaw * 3.1415927F / 180.0F) * 1.25F, 1D, MathHelper.cos(entityIn.rotationYaw * 3.1415927F / 180.0F) * 1.25F);
                            entity.attackEntityFrom(DamageSourceExtra.lightningBolt, 49F);
                            p.world.addWeatherEffect(new EntityGammaLightning(p.world, entity.posX, entity.posY + entity.height, entity.posZ, p.getRNG().nextFloat(), p.getRNG().nextFloat(), p.getRNG().nextFloat()));
                            entity.attackEntityFrom(DamageSource.causePlayerDamage(p).setDamageBypassesArmor().setDamageIsAbsolute(), 49F);
                        }
                    }
                }
            }
        }

        if (entityIn.posY < -45D)
            entityIn.motionY += 10F;
        stack.setStackDisplayName("§lThe Optima Axe");
        for (int i = 0; i < 3; i++) {
            entityIn.world.spawnParticle(EnumParticleTypes.PORTAL, entityIn.posX + (entityIn.world.rand.nextDouble() - 0.5D) * entityIn.width, entityIn.posY + entityIn.world.rand.nextDouble() * entityIn.height, entityIn.posZ + (entityIn.world.rand.nextDouble() - 0.5D) * entityIn.width, (entityIn.world.rand.nextDouble() - 0.5D) * 2.0D, 1.0D, (entityIn.world.rand.nextDouble() - 0.5D) * 2.0D);
            entityIn.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, entityIn.posX + (entityIn.world.rand.nextDouble() - 0.5D) * entityIn.width, entityIn.posY + entityIn.world.rand.nextDouble() * entityIn.height, entityIn.posZ + (entityIn.world.rand.nextDouble() - 0.5D) * entityIn.width, (entityIn.world.rand.nextDouble() - 0.5D) * 2.0D, 1.0D, (entityIn.world.rand.nextDouble() - 0.5D) * 2.0D);
        }

        if (((entityIn instanceof EntityPlayer)) && (entityIn != null)) {
            EntityPlayer p = (EntityPlayer)entityIn;
            if (p.isActiveItemStackBlocking()) {
                List<Entity> found1 = entityIn.world.getEntitiesWithinAABBExcludingEntity(entityIn, entityIn.getEntityBoundingBox().grow(16D, 16D, 16D));
                if ((found1 != null) && (!found1.isEmpty())) {
                    for (Entity mobRef1 : found1) {
                        if (mobRef1 != null && entityIn.getDistanceSq(mobRef1) < 100D) {
                            if (mobRef1 instanceof EntityArrow || mobRef1 instanceof EntityFireball || mobRef1 instanceof EntityThrowable || mobRef1 instanceof EntityTNTPrimed) {
                                mobRef1.world.newExplosion(entityIn, mobRef1.posX, mobRef1.posY, mobRef1.posZ, mobRef1.width, false, false);
                                mobRef1.setDead();
                            }
                        }
                    }
                }
            }

            p.heal(p.getHealth() * 0.01F);
            if (!worldIn.isRemote && p.getHealth() >= p.getMaxHealth() && p.getAbsorptionAmount() <= p.getHealth() * 0.75F) {
                p.setAbsorptionAmount(p.getAbsorptionAmount() + p.getHealth() * 0.01F);
            }
        }

        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
        if (!entityLiving.world.isRemote && entityLiving instanceof EntityPlayer && ((EntityPlayer)entityLiving).isSneaking()) {
            List<Entity> list = entityLiving.world.getEntitiesWithinAABBExcludingEntity(entityLiving, entityLiving.getEntityBoundingBox().grow(64D, 64D, 64D));
            if ((list != null) && (!list.isEmpty())) {
                for (Entity entity : list) {
                    if ((entity != null) && !(entity instanceof EntityTitan)) {
                        entity.playSound(TitanAudio.get("thetitans:titanpunch"), 2.0F, 0.5F + entityLiving.getRNG().nextFloat() * 0.25F);
                        entity.playSound(net.minecraft.init.SoundEvents.BLOCK_FIRE_EXTINGUISH, 2.0F, 2.0F);
                        entity.attackEntityFrom(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
                        if (entity instanceof EntityLivingBase)
                            this.hitEntity(stack, (EntityLivingBase)entity, entityLiving);
                        else if (!(entity instanceof EntityTitanSpirit) && !(entity instanceof EntityTitanPart))
                            entity.setDead();
                    }
                }
            }
        }

        if (!entityLiving.world.isRemote && entityLiving instanceof EntityPlayer) {
            Vec3d vec3 = entityLiving.getLook(1.0F);
            double dx = vec3.x * 16;
            double dy = entityLiving.getEyeHeight() + vec3.y * 16;
            double dz = vec3.z * 16;
            for (int k1 = -4; k1 <= 4; ++k1) {
                for (int l1 = -2; l1 <= 2; ++l1) {
                    for (int i2 = -4; i2 <= 4; ++i2) {
                        int y = MathHelper.floor(entityLiving.posY + dy + l1);
                        int x = MathHelper.floor(entityLiving.posX + dx + k1);
                        int z = MathHelper.floor(entityLiving.posZ + dz + i2);
                        BlockPos pos = new BlockPos(x, y, z);
                        if (!entityLiving.world.isRemote && !entityLiving.world.isAirBlock(pos))
                            breakBlock(entityLiving, pos);
                    }
                }
            }
        }

        if (!entityLiving.world.isRemote && entityLiving instanceof EntityPlayer) {
            for (int i1 = 0; i1 < 12; i1++) {
                Vec3d vec3 = entityLiving.getLook(1.0F);
                double dx = vec3.x * i1;
                double dy = entityLiving.getEyeHeight() + vec3.y * i1;
                double dz = vec3.z * i1;
                int y = MathHelper.floor(entityLiving.posY + dy);
                int x = MathHelper.floor(entityLiving.posX + dx);
                int z = MathHelper.floor(entityLiving.posZ + dz);
                BlockPos pos = new BlockPos(x, y, z);
                if (!entityLiving.world.isAirBlock(pos))
                    entityLiving.world.playEvent(null, 1012, pos, 0);
                if (!entityLiving.world.isRemote && !entityLiving.world.isAirBlock(pos))
                    breakBlock(entityLiving, pos);
            }
        }

        entityLiving.playSound(TitanAudio.get("thetitans:titanSwing"), 10.0F, 1.0F);
        Vec3d vec3 = entityLiving.getLook(1.0F);
        double dx = vec3.x * 12;
        double dy = entityLiving.getEyeHeight() + vec3.y * 12;
        double dz = vec3.z * 12;
        List<Entity> list = entityLiving.world.getEntitiesWithinAABBExcludingEntity(entityLiving, entityLiving.getEntityBoundingBox().grow(12D, 8D, 12D).offset(dx, dy, dz));
        if ((list != null) && (!list.isEmpty())) {
            soundType = 0;
            entityLiving.playSound(TitanAudio.get("thetitans:titanpunch"), 2.0F, 0.5F + entityLiving.getRNG().nextFloat() * 0.25F);
            entityLiving.playSound(net.minecraft.init.SoundEvents.BLOCK_FIRE_EXTINGUISH, 2.0F, 2.0F);
            for (Entity entity : list) {
                if ((entity != null) && !(entity instanceof EntityTitan)) {
                    entity.attackEntityFrom(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
                    if (entity instanceof EntityLivingBase) {
                        this.hitEntity(stack, (EntityLivingBase)entity, entityLiving);
                        if (soundType < 4) {
                            soundType++;
                            if (entity instanceof EntityTitan)
                                entity.playSound(TitanAudio.get("thetitans:titanpunch"), 10.0F, 1.0F);
                            else
                                entity.playSound(TitanAudio.get("thetitans:slashFlesh"), 10.0F, 1.0F);
                        }
                    }
                }
            }
        }

        return false;
    }

    private void breakBlock(EntityLivingBase entityLiving, BlockPos pos) {
        IBlockState state = entityLiving.world.getBlockState(pos);
        Block block = state.getBlock();
        if ((block instanceof IGrowable && !(block instanceof BlockGrass)) || state.getMaterial() == Material.CIRCUITS || block instanceof BlockOre) {
            entityLiving.world.destroyBlock(pos, true);
        }

        else {
            entityLiving.world.playEvent(2001, pos, Block.getStateId(state));
            EntityItem entityitem = new EntityItem(entityLiving.world, pos.getX(), pos.getY(), pos.getZ(), TheTitans.createBlockDrop(entityLiving.world, block, state, pos, entityLiving));
            entityLiving.world.spawnEntity(entityitem);
            entityLiving.world.setBlockToAir(pos);
            entityitem.addVelocity(-MathHelper.sin(entityLiving.rotationYaw * 3.1415927F / 180.0F) * 1.0F, 0.75D, MathHelper.cos(entityLiving.rotationYaw * 3.1415927F / 180.0F) * 1.0F);
        }
    }

    protected double getRange() {
        return 24.0D;
    }

    protected double fetchPaceDash() {
        return 10.0D;
    }

    public int fetchTicksUse() {
        return 5;
    }

    public int fetchTicksHurt() {
        return 0;
    }

    protected int getMaxUses() {
        return 10;
    }
}
