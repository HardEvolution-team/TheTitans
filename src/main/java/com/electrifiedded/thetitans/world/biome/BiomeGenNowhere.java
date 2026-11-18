package com.electrifiedded.thetitans.world.biome;

import com.electrifiedded.thetitans.entity.minion.EntityDragonMinion;
import com.electrifiedded.thetitans.entity.mob.EntityEvilGolem;
import com.electrifiedded.thetitans.entity.mob.EntityUndeadOther;

import net.minecraft.entity.monster.*;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BiomeGenNowhere extends Biome {
    // 1.12.2 Biome ctor takes BiomeProperties, not an int id. Kept the int param for
    // call-site compatibility; the numeric id is now assigned by the biome registry, not here.
    public BiomeGenNowhere(int p_i1990_1_) {
        super(new Biome.BiomeProperties("The Nowhere").setRainDisabled().setTemperature(0.0F).setRainfall(0.0F));
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityEnderman.class, 100, 12, 12));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityDragonMinion.class, 10, 1, 1));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityUndeadOther.class, 50, 4, 4));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityEvilGolem.class, 10, 1, 2));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityZombie.class, 1, 1, 1));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityPigZombie.class, 1, 1, 1));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntitySkeleton.class, 1, 1, 1));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntitySpider.class, 1, 1, 1));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityCreeper.class, 1, 1, 1));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityCaveSpider.class, 1, 1, 1));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityGhast.class, 1, 1, 1));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntitySlime.class, 1, 1, 1));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityMagmaCube.class, 1, 1, 1));
        this.topBlock = Blocks.STONE.getDefaultState();
        this.fillerBlock = Blocks.STONE.getDefaultState();
        this.decorator = new BiomeNowhereDecorator();
    }

    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float arg76731n1) {
        return 0;
    }
}
