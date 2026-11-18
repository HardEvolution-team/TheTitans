package com.electrifiedded.thehippomaster.AnimationAPI.client;

import com.electrifiedded.thehippomaster.AnimationAPI.AnimationAPI;
import com.electrifiedded.thehippomaster.AnimationAPI.IAnimatedEntity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;

@SideOnly(Side.CLIENT)
public class Animator {
    public Animator(ModelBase model) {
        beatTemp = 0;
        motionCorrect = false;
        mainModel = model;
        tableTransform = new HashMap<ModelRenderer, Transform>();
        transformTablePrev = new HashMap<ModelRenderer, Transform>();
    }

    public IAnimatedEntity getEntity() {
        return mobMotion;
    }

    public void update(IAnimatedEntity entity) {
        beatTemp = tempBeatPrev = 0;
        motionCorrect = false;
        mobMotion = entity;
        tableTransform.clear();
        transformTablePrev.clear();
        for(int i = 0; i < mainModel.boxList.size(); i++) {
            ModelRenderer box = (ModelRenderer)mainModel.boxList.get(i);
            box.rotateAngleX = 0F;
            box.rotateAngleY = 0F;
            box.rotateAngleZ = 0F;
        }
    }

    public boolean setAnim(int keyMotion) {
        beatTemp = tempBeatPrev = 0;
        motionCorrect = mobMotion.getAnimID() == keyMotion;
        return motionCorrect;
    }

    public void startPhase(int duration) {
        if(!motionCorrect) return;
        tempBeatPrev = beatTemp;
        beatTemp += duration;
    }

    public void setStationaryPhase(int duration) {
        startPhase(duration);
        endPhase(true);
    }

    public void resetPhase(int duration) {
        startPhase(duration);
        endPhase();
    }

    public void rotate(ModelRenderer box, float x, float y, float z) {
        if(!motionCorrect) return;
        if(!tableTransform.containsKey(box)) tableTransform.put(box, new Transform(x, y, z));
        else tableTransform.get(box).addRot(x, y, z);
    }

    public void move(ModelRenderer box, float x, float y, float z) {
        if(!motionCorrect) return;
        if(!tableTransform.containsKey(box)) tableTransform.put(box, new Transform(x, y, z, 0F, 0F, 0F));
        else tableTransform.get(box).addOffset(x, y, z);
    }

    public void endPhase() {
        endPhase(false);
    }

    private void endPhase(boolean stationary) {
        if(!motionCorrect) return;
        int animTick = mobMotion.getAnimTick();
        if(animTick >= tempBeatPrev && animTick < beatTemp) {
            if(stationary) {
                for(ModelRenderer box : transformTablePrev.keySet()) {
                    Transform transform = transformTablePrev.get(box);
                    box.rotateAngleX += transform.rotX;
                    box.rotateAngleY += transform.rotY;
                    box.rotateAngleZ += transform.rotZ;
                    box.rotationPointX += transform.offsetX;
                    box.rotationPointY += transform.offsetY;
                    box.rotationPointZ += transform.offsetZ;
                }
            }

            else {
                float tick = (animTick - tempBeatPrev + AnimationAPI.proxy.getPartialTick()) / (beatTemp - tempBeatPrev);
                float inc = MathHelper.sin(tick * PI / 2F), dec = 1F - inc;
                for(ModelRenderer box : transformTablePrev.keySet()) {
                    Transform transform = transformTablePrev.get(box);
                    box.rotateAngleX += dec * transform.rotX;
                    box.rotateAngleY += dec * transform.rotY;
                    box.rotateAngleZ += dec * transform.rotZ;
                    box.rotationPointX += dec * transform.offsetX;
                    box.rotationPointY += dec * transform.offsetY;
                    box.rotationPointZ += dec * transform.offsetZ;
                }

                for(ModelRenderer box : tableTransform.keySet()) {
                    Transform transform = tableTransform.get(box);
                    box.rotateAngleX += inc * transform.rotX;
                    box.rotateAngleY += inc * transform.rotY;
                    box.rotateAngleZ += inc * transform.rotZ;
                    box.rotationPointX += inc * transform.offsetX;
                    box.rotationPointY += inc * transform.offsetY;
                    box.rotationPointZ += inc * transform.offsetZ;
                }
            }
        }

        if(!stationary) {
            transformTablePrev.clear();
            transformTablePrev.putAll(tableTransform);
            tableTransform.clear();
        }
    }

    private int beatTemp, tempBeatPrev;
    private boolean motionCorrect;
    private ModelBase mainModel;
    private IAnimatedEntity mobMotion;
    private HashMap<ModelRenderer, Transform> tableTransform, transformTablePrev;
    public static final float PI = (float)Math.PI;
}
