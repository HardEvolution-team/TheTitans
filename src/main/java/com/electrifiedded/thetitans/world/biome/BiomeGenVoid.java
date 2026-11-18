package com.electrifiedded.thetitans.world.biome;

import com.electrifiedded.thetitans.entity.titan.EntityWitherzillaMinion;

import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BiomeGenVoid extends Biome {
    // 1.12.2 Biome ctor takes BiomeProperties, not an int id. Kept the int param for
    // call-site compatibility; the numeric id is now assigned by the biome registry, not here.
    public BiomeGenVoid(int p_i1990_1_) {
        super(new Biome.BiomeProperties("Void Island").setRainDisabled().setTemperature(2.0F).setRainfall(0.0F));
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityWitherzillaMinion.class, 100, 12, 12));
        this.topBlock = Blocks.BEDROCK.getDefaultState();
        this.fillerBlock = Blocks.BEDROCK.getDefaultState();
        this.decorator = new BiomeVoidDecorator();
    }

    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float arg76731n1) {
        return 0;
    }
}
