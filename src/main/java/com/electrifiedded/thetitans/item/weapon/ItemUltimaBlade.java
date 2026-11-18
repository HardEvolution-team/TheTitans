package com.electrifiedded.thetitans.item.weapon;

import com.electrifiedded.thetitans.TheTitans;
import com.electrifiedded.thetitans.effect.DamageSourceExtra;
import com.electrifiedded.thetitans.entity.misc.EntityImmortalItem;
import com.electrifiedded.thetitans.entity.titan.EntityGammaLightning;
import com.electrifiedded.thetitans.entity.titan.EntityTitan;
import com.electrifiedded.thetitans.entity.titan.EntityTitanPart;
import com.electrifiedded.thetitans.entity.titan.EntityUrLightning;
import com.electrifiedded.thetitans.entity.titan.EntityWitherzilla;
import com.electrifiedded.thetitans.entity.titan.ITitanHitbox;
import com.electrifiedded.thetitans.proxy.ClientProxy;
import com.electrifiedded.thetitans.registry.TitanGear;
import com.electrifiedded.thetitans.registry.TitanAudio;
import com.electrifiedded.thetitans.registry.TitanTrophies;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockOre;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
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

public class ItemUltimaBlade extends ItemTitanSword {
    private int soundType;
    public static ToolMaterial toolMaterialUltimaEnum = EnumHelper.addToolMaterial("Ultima", Integer.MAX_VALUE, 2, 9999999F, Float.MAX_VALUE / 2.0F, 0);

    public ItemUltimaBlade() {
        super("", toolMaterialUltimaEnum);
        soundType = 0;
        setTranslationKey("ultima_blade");
    }

    private static boolean hasStack(EntityPlayer player, Item item) {
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack s = player.inventory.getStackInSlot(i);
            if (!s.isEmpty() && s.getItem() == item)
                return true;
        }
        return false;
    }

    public boolean isToolStack(ItemStack arg77616n1) {
        return true;
    }

    public boolean canHarvestBlock(IBlockState blockIn) {
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
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (stack.getItemDamage() > 0)

            {
            tooltip.add("§3§lThe ultimate weapon.");
            tooltip.add("§3§lCuts through all defences, even hacks.");
            tooltip.add("§3§lNormally owned by Regnator");
        }

        else {
            tooltip.add("§l§k§3§lThe ultimate weapon.");
            tooltip.add("§l§k§3§lCuts through all defences, even hacks.");
            tooltip.add("§l§k§3§lNormally owned by Regnator");
        }
    }

    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        return true;
    }

    private void grantAdvice(EntityPlayer player) {
        List<?> found1 = player.world.getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().grow(16.0D, 16.0D, 16.0D));
        if ((found1 != null) && (!found1.isEmpty())) {
            for (int i1 = 0; i1 < found1.size(); i1++) {
                Entity entity = (Entity)found1.get(i1);
                if (((entity instanceof IMob || entity instanceof EntityPlayer))) {
                    if (player.canEntityBeSeen(entity)) {
                        player.sendMessage(new TextComponentString("Ultima Blade: §l§oThe " + ((EntityLivingBase)entity).getName() +" is " + player.getDistance(entity) + "§l§o blocks away from you."));
                    }

                    else {
                        player.sendMessage(new TextComponentString("Ultima Blade: §l§oAn unseen mob is located out of your §l§osight " + player.getDistance(entity) + " blocks away from you."));
                        player.sendMessage(new TextComponentString("Ultima Blade: §l§oThe mob's health: " + ((EntityLivingBase)entity).getHealth() + "/" + ((EntityLivingBase)entity).getMaxHealth()));
                        player.sendMessage(new TextComponentString("Ultima Blade: §l§oThe mob's name: " + ((EntityLivingBase)entity).getName()));
                        player.playSound(SoundEvents.ENTITY_WITHER_HURT, 10.0F, (player.getRNG().nextFloat() - player.getRNG().nextFloat()) * 0.2F + 0.5F);
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
                        player.sendMessage(new TextComponentString("Ultima Blade: §l§oI sense a " + block.getLocalizedName() + "§l§o within atleast " + player.getDistance(j2, k, l) + " blocks of you."));
                        player.playSound(SoundEvents.ENTITY_WITHER_HURT, 10.0F, 0.6F);
                    }
                }
            }
        }
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (player.world.isRemote) {
            if (player.isSneaking())
                grantAdvice(player);
            else
                adviceOffer(player);
        }

        if (hasStack(player, TitanGear.optimaAxe)) {
            MinecraftServer server = world.getMinecraftServer();
            if (server != null) {
                WorldServer worldserver = server.getWorld(0);
                WorldInfo worldinfo = worldserver.getWorldInfo();
                worldinfo.setRainTime(9999999);
                worldinfo.setThunderTime(1000000);
                worldinfo.setRaining(true);
                worldinfo.setThundering(true);
            }

            List<?> list = player.world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(200.0D, 100.0D, 200.0D), e -> e != player);
            if ((list != null) && (!list.isEmpty())) {
                for (int i1 = 0; i1 < list.size(); i1++) {
                    Entity entity = (Entity)list.get(i1);
                    if ((entity != null) && entity.isEntityAlive() && entity instanceof EntityLivingBase && !(entity instanceof EntityTitan) && entity != player) {
                        if (world.isRemote)
                            ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(ClientProxy.electricJudgment, Integer.MAX_VALUE, 19));
                        entity.addVelocity(-MathHelper.sin(player.rotationYaw * 3.1415927F / 180.0F) * 1.25F, 1D, MathHelper.cos(player.rotationYaw * 3.1415927F / 180.0F) * 1.25F);
                        entity.attackEntityFrom(DamageSourceExtra.lightningBolt, 49F);
                        player.world.addWeatherEffect(new EntityGammaLightning(player.world, entity.posX, entity.posY + entity.height, entity.posZ, player.getRNG().nextFloat(), player.getRNG().nextFloat(), player.getRNG().nextFloat()));
                        entity.attackEntityFrom(DamageSource.causePlayerDamage(player).setDamageBypassesArmor().setDamageIsAbsolute(), 49F);
                        if (entity instanceof EntityLivingBase && !(entity instanceof EntityTitan) && (entity.height >= 6.0F || entity.isEntityInvulnerable(DamageSource.OUT_OF_WORLD))) {
                            ((EntityLivingBase)entity).setDead();
                            ((EntityLivingBase)entity).setHealth(MathHelper.clamp(0.0F, 0.0F, ((EntityLivingBase)entity).getMaxHealth()));
                            ((EntityLivingBase)entity).attackEntityFrom(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
                        }
                    }
                }
            }
        }

        return super.onItemRightClick(world, player, hand);
    }

    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        attacker.world.getGameRules().setOrCreateGameRule("keepInventory", "true");
        float extradamage = EnchantmentHelper.getModifierForCreature(attacker.getHeldItemMainhand(), target.getCreatureAttribute());
        int knockbackAmount = EnchantmentHelper.getKnockbackModifier(attacker);
        int bonusdamage = 1;
        if (target != null) {
            target.setFire(Integer.MAX_VALUE);
            if (attacker.world.isRemote)
                target.addPotionEffect(new PotionEffect(ClientProxy.electricJudgment, Integer.MAX_VALUE, 19));
            target.hurtResistantTime = 0;
            if (((target instanceof EntityTitan))) {
                if (target instanceof EntityWitherzilla)
                    bonusdamage = 50;
                if (attacker instanceof EntityPlayer)
                    ((EntityTitan)target).attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)attacker), getAttackDamage());
                ((EntityTitan)target).hurt((40000.0F * bonusdamage) - extradamage);
                ((EntityTitan)target).setAttackTarget(attacker);
                ((EntityTitan)target).appendVelocityGiant(-MathHelper.sin(attacker.rotationYaw * 3.1415927F / 180.0F) * 3F + knockbackAmount, 0.5D + knockbackAmount, MathHelper.cos(attacker.rotationYaw * 3.1415927F / 180.0F) * 3F + knockbackAmount);
            }

            else {
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
                    if (attacker.world.isRemote)
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
        int sharpness = EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack);
        if (sharpness <= 0) {
            stack.addEnchantment(Enchantments.SHARPNESS, 100);
        }

        int smite = EnchantmentHelper.getEnchantmentLevel(Enchantments.SMITE, stack);
        if (smite <= 0) {
            stack.addEnchantment(Enchantments.SMITE, 100);
        }

        int bug = EnchantmentHelper.getEnchantmentLevel(Enchantments.BANE_OF_ARTHROPODS, stack);
        if (bug <= 0) {
            stack.addEnchantment(Enchantments.BANE_OF_ARTHROPODS, 100);
        }

        int fire = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT, stack);
        if (fire <= 0) {
            stack.addEnchantment(Enchantments.FIRE_ASPECT, 100);
        }

        int titanslaying = EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, stack);
        if (titanslaying <= 0) {
            stack.addEnchantment(Enchantments.LOOTING, 100);
        }
    }

    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());
        else
            stack.getTagCompound().setBoolean("Unbreakable", true);
        entityIn.extinguish();
        if ((entityIn instanceof EntityPlayer) && !worldIn.isRemote) {
            if (hasStack((EntityPlayer)entityIn, TitanGear.optimaAxe)) {
                if ((entityIn.motionY > 1.0D) && (!entityIn.isDead))entityIn.motionY /= entityIn.motionY;
                if ((!entityIn.onGround) && (entityIn.motionY < 0.0D))
                    entityIn.motionY *= 0.75D;
                if ((entityIn.motionX > 1.0D) && (!entityIn.isDead))entityIn.motionX = 1D;
                if ((entityIn.motionX < -1.0D) && (!entityIn.isDead))entityIn.motionX = -1D;
                if ((entityIn.motionZ > 1.0D) && (!entityIn.isDead))entityIn.motionZ = 1D;
                if ((entityIn.motionZ < -1.0D) && (!entityIn.isDead))entityIn.motionZ = -1D;
                if (((EntityPlayer)entityIn).isActiveItemStackBlocking()) {
                    ((EntityPlayer)entityIn).motionX = 0D;
                    ((EntityPlayer)entityIn).motionY = 0D;
                    ((EntityPlayer)entityIn).motionZ = 0D;
                    ((EntityPlayer)entityIn).posX = ((EntityPlayer)entityIn).lastTickPosX;
                    ((EntityPlayer)entityIn).posY = ((EntityPlayer)entityIn).lastTickPosY;
                    ((EntityPlayer)entityIn).posZ = ((EntityPlayer)entityIn).lastTickPosZ;
                }

                if (((EntityPlayer)entityIn).getRNG().nextInt(5) == 0) {
                    for (int l = 0; l < 5; l++) {
                        int i = MathHelper.floor(((EntityPlayer)entityIn).posX + (((EntityPlayer)entityIn).getRNG().nextInt() * 200 - 100));
                        int j = MathHelper.floor(((EntityPlayer)entityIn).posY + (((EntityPlayer)entityIn).getRNG().nextInt() * 100 - 50));
                        int k = MathHelper.floor(((EntityPlayer)entityIn).posZ + (((EntityPlayer)entityIn).getRNG().nextInt() * 200 - 100));
                        EntityUrLightning entitylightning = new EntityUrLightning(((EntityPlayer)entityIn).world, i, j, k);
                        if ((entityIn.world.isSideSolid(new BlockPos(i, j - 1, k), net.minecraft.util.EnumFacing.UP)) && (((EntityPlayer)entityIn).world.checkNoEntityCollision(entitylightning.getEntityBoundingBox())) && (((EntityPlayer)entityIn).world.getCollisionBoxes(entitylightning, entitylightning.getEntityBoundingBox()).isEmpty()) && (!((EntityPlayer)entityIn).world.containsAnyLiquid(entitylightning.getEntityBoundingBox()))) {
                            ((EntityPlayer)entityIn).world.addWeatherEffect(entitylightning);
                        }
                    }
                }

                List<?> list = ((EntityPlayer)entityIn).world.getEntitiesWithinAABBExcludingEntity(((EntityPlayer)entityIn), ((EntityPlayer)entityIn).getEntityBoundingBox().grow(200.0D, 100.0D, 200.0D));
                if ((list != null) && (!list.isEmpty())) {
                    for (int i1 = 0; i1 < list.size(); i1++) {
                        Entity entity = (Entity)list.get(i1);
                        if ((entity != null) && entity.isEntityAlive() && entity instanceof EntityLivingBase && ((EntityPlayer)entityIn).getRNG().nextInt(entity instanceof EntityTitan ? 10 : 100) == 0 && entity != entityIn) {
                            if (entity != null && entity.isEntityAlive() && !(entity instanceof EntityTitanPart)) {
                                if (entity instanceof EntityPlayer) {
                                    ((EntityPlayer)entity).sendMessage(new TextComponentString("§k" + ((EntityPlayer)entity).getRNG().nextInt(1234567890)));
                                    if (hasStack((EntityPlayer)entity, TitanGear.optimaAxe)) {
                                        ((EntityPlayer)entity).motionX = 0D;
                                        ((EntityPlayer)entity).motionY = 0D;
                                        ((EntityPlayer)entity).motionZ = 0D;
                                        return;
                                    }
                                }

                                EntityUrLightning lightning = new EntityUrLightning(entity.world, entity.posX, entity.posY, entity.posZ);
                                lightning.copyLocationAndAnglesFrom(entity);
                                entity.world.addWeatherEffect(lightning);
                                if ((entity != null) && entity instanceof EntityLivingBase && entity.height >= 6.0F && (!(entity instanceof EntityTitan))) {
                                    ((EntityLivingBase)entity).setHealth(0.0F);
                                }

                                if (entity instanceof EntityTitan && entity instanceof ITitanHitbox) {
                                    Entity parts[] = ((ITitanHitbox)entity).getArray();
                                    if (parts != null) {
                                        for (int j = 0; j < parts.length; j++) {
                                            entity.world.addWeatherEffect(new EntityUrLightning(entity.world, parts[j].posX, parts[j].posY, parts[j].posZ));
                                        }
                                    }
                                }

                                if ((entity instanceof EntityTitan))
                                    ((EntityTitan)entity).setInvulTime(((EntityTitan)entity).getInvulTime() - 20);
                                else
                                    entity.motionY += 0.5D;
                                entity.attackEntityFrom(new EntityDamageSource("playerwithgodpower", (EntityPlayer)entityIn).setDamageAllowedInCreativeMode().setDamageBypassesArmor().setDamageIsAbsolute(), entity instanceof EntityTitan || entity.height >= 6.0F ? 20000F : 500F);
                            }
                        }
                    }
                }
            }
        }

        if (entityIn.posY < -45D)
            entityIn.motionY += 10F;
        stack.setStackDisplayName("§lThe Ultima Blade");
        entityIn.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, entityIn.posX + (((EntityLivingBase)entityIn).getRNG().nextDouble() - 0.5D) * entityIn.width, entityIn.posY + ((EntityLivingBase)entityIn).getRNG().nextDouble() * entityIn.height, entityIn.posZ + (((EntityLivingBase)entityIn).getRNG().nextDouble() - 0.5D) * entityIn.width, (((EntityLivingBase)entityIn).getRNG().nextDouble() - 0.5D) * 2.0D, 1.0D, (((EntityLivingBase)entityIn).getRNG().nextDouble() - 0.5D) * 2.0D);
        entityIn.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, entityIn.posX + (((EntityLivingBase)entityIn).getRNG().nextDouble() - 0.5D) * entityIn.width, entityIn.posY + ((EntityLivingBase)entityIn).getRNG().nextDouble() * entityIn.height, entityIn.posZ + (((EntityLivingBase)entityIn).getRNG().nextDouble() - 0.5D) * entityIn.width, (((EntityLivingBase)entityIn).getRNG().nextDouble() - 0.5D) * 2.0D, 1.0D, (((EntityLivingBase)entityIn).getRNG().nextDouble() - 0.5D) * 2.0D);
        if (entityIn instanceof EntityPlayer && ((EntityPlayer)entityIn).isActiveItemStackBlocking() && !worldIn.isRemote) {
            List<?> found1 = entityIn.world.getEntitiesWithinAABBExcludingEntity(entityIn, entityIn.getEntityBoundingBox().grow(16D, 16D, 16D));
            if ((found1 != null) && (!found1.isEmpty())) {
                for (int i11 = 0; i11 < found1.size(); i11++) {
                    Entity mobRef1 = (Entity)found1.get(i11);
                    if (mobRef1 != null && entityIn.getDistanceSq(mobRef1) < 100D) {
                        if (mobRef1 instanceof EntityArrow || mobRef1 instanceof EntityFireball || mobRef1 instanceof EntityThrowable || mobRef1 instanceof EntityTNTPrimed) {
                            mobRef1.world.newExplosion(entityIn, mobRef1.posX, mobRef1.posY, mobRef1.posZ, mobRef1.width, false, false);
                            mobRef1.setDead();
                        }
                    }
                }
            }

            ((EntityPlayer)entityIn).heal(((EntityPlayer)entityIn).getMaxHealth());
        }

        if (((((EntityPlayer)entityIn).getActivePotionEffect(MobEffects.SATURATION) == null) || (((EntityPlayer)entityIn).getActivePotionEffect(MobEffects.SATURATION).getDuration() <= 1)))
            ((EntityPlayer)entityIn).addPotionEffect(new PotionEffect(MobEffects.SATURATION, 800, 299, false, true));
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
        if (!entityLiving.world.isRemote && entityLiving instanceof EntityPlayer && ((EntityPlayer)entityLiving).isSneaking()) {
            List<?> list = entityLiving.world.getEntitiesWithinAABBExcludingEntity(entityLiving, entityLiving.getEntityBoundingBox().grow(64D, 64D, 64D));
            if ((list != null) && (!list.isEmpty())) {
                soundType = 0;
                net.minecraft.util.SoundEvent tp = com.electrifiedded.thetitans.registry.TitanAudio.get("thetitans:titanpunch");
                if (tp != null)
                    entityLiving.playSound(tp, 2F, 0.5F + (entityLiving.getRNG().nextFloat() * 0.25F));
                entityLiving.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 2F, 2F);
                for (int i1 = 0; i1 < list.size(); i1++) {
                    Entity entity = (Entity)list.get(i1);
                    if ((entity != null) && !(entity instanceof EntityTitan)) {
                        entity.attackEntityFrom(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
                        if (entity instanceof EntityLivingBase) {
                            this.hitEntity(stack, (EntityLivingBase)entity, entityLiving);
                            if (soundType < 4)
                                if (entity instanceof EntityTitan) {
                                soundType ++;
                                net.minecraft.util.SoundEvent tpunch = com.electrifiedded.thetitans.registry.TitanAudio.get("thetitans:titanpunch");
                                if (tpunch != null)
                                    entity.playSound(tpunch, 10.0F, 1.0F);
                            }

                            else {
                                soundType ++;
                                net.minecraft.util.SoundEvent slash = com.electrifiedded.thetitans.registry.TitanAudio.get("thetitans:slashFlesh");
                                if (slash != null)
                                    entity.playSound(slash, 10.0F, 1.0F);
                            }
                        }
                    }
                }
            }
        }

        if (!entityLiving.world.isRemote && entityLiving instanceof EntityPlayer) {
            for (int i1 = 0; i1 < 24; i1++) {
                Vec3d vec3 = ((EntityPlayer)entityLiving).getLook(1.0F);
                double dx = vec3.x * i1;
                double dy = entityLiving.getEyeHeight() + vec3.y * i1;
                double dz = vec3.z * i1;
                int y = MathHelper.floor(entityLiving.posY + dy);
                int x = MathHelper.floor(entityLiving.posX + dx);
                int z = MathHelper.floor(entityLiving.posZ + dz);
                if (!entityLiving.world.isAirBlock(new BlockPos(x, y, z)))
                    entityLiving.world.playEvent((EntityPlayer)null, 1012, new BlockPos(x, y, z), 0);
                if (!entityLiving.world.isRemote && !entityLiving.world.isAirBlock(new BlockPos(x, y, z))) {
                    IBlockState state = entityLiving.world.getBlockState(new BlockPos(x, y, z));
                    Block block = state.getBlock();
                    if ((block instanceof IGrowable && !(block instanceof BlockGrass)) || state.getMaterial() == Material.CIRCUITS || block instanceof BlockOre) {
                        entityLiving.world.destroyBlock(new BlockPos(x, y, z), true);
                    }

                    else {
                        int l = block.getMetaFromState(state);
                        entityLiving.world.playEvent(2001, new BlockPos(x, y, z), Block.getIdFromBlock(block) + (l << 12));
                        EntityItem entityitem = new EntityItem(entityLiving.world, x, y, z, TheTitans.createBlockDrop(entityLiving.world, block, state, new BlockPos(x, y, z), entityLiving));
                        entityLiving.world.spawnEntity(entityitem);
                        entityLiving.world.setBlockToAir(new BlockPos(x, y, z));
                        entityitem.addVelocity(-MathHelper.sin(entityLiving.rotationYaw * 3.1415927F / 180.0F) * 1.0F, 0.75D, MathHelper.cos(entityLiving.rotationYaw * 3.1415927F / 180.0F) * 1.0F);
                    }
                }
            }
        }

        net.minecraft.util.SoundEvent swing = com.electrifiedded.thetitans.registry.TitanAudio.get("thetitans:titanSwing");
        if (swing != null)
            entityLiving.playSound(swing, 10.0F, 1.0F);
        if (!entityLiving.world.isRemote) {
            for (int i1 = 0; i1 < 24; i1++) {
                Vec3d vec3 = entityLiving.getLook(1.0F);
                double dx = vec3.x * i1;
                double dy = entityLiving.getEyeHeight() + vec3.y * i1;
                double dz = vec3.z * i1;
                List<?> found1 = entityLiving.world.getEntitiesWithinAABBExcludingEntity(entityLiving, entityLiving.getEntityBoundingBox().grow(3D, 3D, 3D).offset(dx, dy, dz));
                if ((found1 != null) && (!found1.isEmpty())) {
                    for (int i11 = 0; i11 < found1.size(); i11++) {
                        Entity mobRef1 = (Entity)found1.get(i11);
                        if (((mobRef1 instanceof EntityTNTPrimed)) && (!mobRef1.isEntityInvulnerable(DamageSource.OUT_OF_WORLD))) {
                            mobRef1.world.newExplosion(entityLiving, mobRef1.posX, mobRef1.posY, mobRef1.posZ, 4.0F, false, false);
                            mobRef1.setDead();
                        }

                        if (((mobRef1 instanceof EntityFireball)) && (!mobRef1.isEntityInvulnerable(DamageSource.OUT_OF_WORLD))) {
                            mobRef1.world.newExplosion(entityLiving, mobRef1.posX, mobRef1.posY, mobRef1.posZ, 0.0F, false, false);
                            mobRef1.setDead();
                        }

                        if (((mobRef1 instanceof EntityLivingBase)) && mobRef1.isEntityAlive() && (mobRef1 != entityLiving)) {
                            mobRef1.setFire(Integer.MAX_VALUE);
                            entityLiving.world.getGameRules().setOrCreateGameRule("keepInventory", "true");
                            float extradamage = EnchantmentHelper.getModifierForCreature(entityLiving.getHeldItemMainhand(), ((EntityLivingBase)mobRef1).getCreatureAttribute());
                            EnchantmentHelper.getKnockbackModifier(entityLiving);
                            if (mobRef1 != null) {
                                mobRef1.attackEntityFrom(DamageSourceExtra.causeGiantHarmAnti(entityLiving), 20000.0F + extradamage);
                                mobRef1.hurtResistantTime = 0;
                                this.hitEntity(stack, (EntityLivingBase) mobRef1, entityLiving);
                                if (mobRef1 instanceof EntityTitan) {
                                    ((EntityTitan)mobRef1).setAttackTarget(entityLiving);
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
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
