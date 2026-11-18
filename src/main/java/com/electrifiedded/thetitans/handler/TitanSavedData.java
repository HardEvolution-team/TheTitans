package com.electrifiedded.thetitans.handler;

import com.electrifiedded.thetitans.TheTitans;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class TitanSavedData extends WorldSavedData {
    private NBTTagCompound data = new NBTTagCompound();
    public static final String DATA_NAME = TheTitans.MODID_V + "_TitanData";
    //Stages
    private boolean giantCompletePre;
    private boolean completeGiant;
    private boolean giantCompletePost;
    //Events
    private boolean eventPost[] = new boolean[10];
    private boolean enderDragonPost;
    private boolean witherPost;
    private boolean twilightPost;
    private boolean emperorSkullingPost;
    private boolean firstGiantPost;
    private boolean witherzillaPost;
    private boolean witherzillaOmegaPost;
    private boolean executorDragonPost;
    private boolean executorDragonOmegaPost;

    public TitanSavedData() {
        super(DATA_NAME);
        TheTitans.debug("Initializing TitanSavedData()");
    }

    public TitanSavedData(String s) {
        super(s);
        TheTitans.debug("Initializing TitanSavedData(String)");
    }

    public void readFromNBT(NBTTagCompound compound) {
        data = compound.getCompoundTag("TitansData");
        for (int i = 0; i < eventPost.length; i++)
            eventPost[i] = data.getBoolean("PostEvent" + i);
        giantCompletePre = data.getBoolean("PreTitanComplete");
        completeGiant = data.getBoolean("TitanComplete");
        giantCompletePost = data.getBoolean("PostTitanComplete");
        enderDragonPost = data.getBoolean("PostDragon");
        witherPost = data.getBoolean("PostWither");
        twilightPost = data.getBoolean("PostTwilight");
        emperorSkullingPost = data.getBoolean("PostKrabs");
        firstGiantPost = data.getBoolean("PostFirstTitan");
        witherzillaPost = data.getBoolean("PostWitherzilla");
        witherzillaOmegaPost = data.getBoolean("PostWitherzillaOmega");
        executorDragonPost = data.getBoolean("PostExecutorDragon");
        executorDragonOmegaPost = data.getBoolean("PostExecutorDragonOmega");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("TitansData", data);
        for (int i = 0; i < eventPost.length; i++)
            compound.setBoolean("PostEvent" + i, eventPost[i]);
        compound.setBoolean("PreTitanComplete", giantCompletePre);
        compound.setBoolean("TitanComplete", completeGiant);
        compound.setBoolean("PostTitanComplete", giantCompletePost);
        compound.setBoolean("PostDragon", enderDragonPost);
        compound.setBoolean("PostWither", witherPost);
        compound.setBoolean("PostTwilight", twilightPost);
        compound.setBoolean("PostKrabs", emperorSkullingPost);
        compound.setBoolean("PostFirstTitan", firstGiantPost);
        compound.setBoolean("PostWitherzilla", witherzillaPost);
        compound.setBoolean("PostWitherzillaOmega", witherzillaOmegaPost);
        compound.setBoolean("PostExecutorDragon", executorDragonPost);
        compound.setBoolean("PostExecutorDragonOmega", executorDragonOmegaPost);
        return compound;
    }

    public NBTTagCompound getData() {
        return data;
    }

    public static TitanSavedData fetchInfoStorage(World world) {
        if (world != null) {
            MapStorage storage = world.getMapStorage();
            TitanSavedData instance = (TitanSavedData)storage.getOrLoadData(TitanSavedData.class, DATA_NAME);
            if (instance == null) {
                TheTitans.debug("Creating a new instance of TitanSavedData as data returned null");
                instance = new TitanSavedData();
                storage.setData(DATA_NAME, instance);
            }

            return instance;
        }

        TheTitans.fatal("The world given was null", new NullPointerException());
        return null;
    }

    public boolean getBoolean(World world, String key) {
        return data.getBoolean(key);
    }

    public void setBoolean(World world, String key, boolean flag) {
        TitanSavedData instance = fetchInfoStorage(world);
        switch(key) {
            case "PostDragon": instance.enderDragonPost = flag; break;
            case "PostWither": instance.witherPost = flag; break;
            case "PostTwilight": instance.twilightPost = flag; break;
            case "PostKrabs": instance.emperorSkullingPost = flag; break;
            case "PostFirstTitan": instance.firstGiantPost = flag; break;
            case "PostWitherzilla": instance.witherzillaPost = flag; break;
            case "PostWitherzillaOmega": instance.witherzillaOmegaPost = flag; break;
            case "PostExecutorDragon": instance.executorDragonPost = flag; break;
            case "PostExecutorDragonOmega": instance.executorDragonOmegaPost = flag; break;
            case "PreTitanComplete": instance.giantCompletePre = flag; break;
            case "TitanComplete": instance.completeGiant = flag; break;
            case "PostTitanComplete": instance.giantCompletePost = flag; break;
            case "PostEventZombie": instance.eventPost[0] = flag; break;
            case "PostEventSpeeder": instance.eventPost[1] = flag; break;
            case "PostEventTiag": instance.eventPost[2] = flag; break;
            case "PostEventSiege": instance.eventPost[3] = flag; break;
            case "PostEventNether": instance.eventPost[4] = flag; break;
            case "PostEventEnd": instance.eventPost[5] = flag; break;
            case "PostEventAwake": instance.eventPost[6] = flag; break;
            case "PostEventRumble": instance.eventPost[7] = flag; break;
            case "PostEventTrueEnd": instance.eventPost[8] = flag; break;
            case "PostEventFinalEnd": instance.eventPost[9] = flag; break;
                // BUGFIX — unknown keys (TemplarKilled, PostLord, Killed:<mob id>, ...) used to be dropped with a
                // warning, so those milestones never persisted. The NBT map is the source of truthV; just store them.
            default: break;
        }

        data.setBoolean(key, flag);
        markDirty();
        if (instance != this) {
            instance.data.setBoolean(key, flag);
            instance.markDirty();
        }
    }
}
