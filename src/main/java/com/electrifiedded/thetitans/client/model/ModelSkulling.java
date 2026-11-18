package com.electrifiedded.thetitans.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ModelHumanoidHead - Either Mojang or a mod author
 * Created using Tabula 4.1.1
 */
public class ModelSkulling extends ModelBase {
    public ModelRenderer body;
    public ModelRenderer leftclawV;
    public ModelRenderer rightclawV;
    public ModelRenderer rightleg1V;
    public ModelRenderer rightleg2V;
    public ModelRenderer rightleg3V;
    public ModelRenderer leftleg1V;
    public ModelRenderer leftleg2V;
    public ModelRenderer leftleg3V;
    public ModelRenderer skull;
    public ModelRenderer lefteye;
    public ModelRenderer righteye;

    public ModelSkulling() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.leftleg1V = new ModelRenderer(this, 0, 16);
        this.leftleg1V.setRotationPoint(1.0F, 21.0F, -2.0F);
        this.leftleg1V.addBox(0.0F, 0.0F, 0.0F, 6, 1, 1, 0.0F);
        this.assignRotationRotate(leftleg1V, 0.0F, 0.5918411493512771F, 0.36425021489121656F);
        this.righteye = new ModelRenderer(this, 0, 0);
        this.righteye.setRotationPoint(-1.0F, 4.5F, -0.5F);
        this.righteye.addBox(-0.5F, -3.0F, -0.5F, 1, 3, 1, 0.0F);
        this.assignRotationRotate(righteye, 2.2689280275926285F, 0.0F, 0.0F);
        this.leftleg2V = new ModelRenderer(this, 0, 16);
        this.leftleg2V.setRotationPoint(1.0F, 21.0F, -1.0F);
        this.leftleg2V.addBox(0.0F, 0.0F, 0.0F, 6, 1, 1, 0.0F);
        this.assignRotationRotate(leftleg2V, 0.0F, 0.136659280431156F, 0.36425021489121656F);
        this.rightleg3V = new ModelRenderer(this, 0, 16);
        this.rightleg3V.setRotationPoint(-1.0F, 21.0F, 0.0F);
        this.rightleg3V.addBox(0.0F, -1.0F, 0.0F, 6, 1, 1, 0.0F);
        this.assignRotationRotate(rightleg3V, 0.0F, -0.4553564018453205F, 2.777342438698576F);
        this.lefteye = new ModelRenderer(this, 0, 0);
        this.lefteye.setRotationPoint(1.0F, 4.5F, -0.5F);
        this.lefteye.addBox(-0.5F, -3.0F, -0.5F, 1, 3, 1, 0.0F);
        this.assignRotationRotate(lefteye, 2.2689280275926285F, 0.0F, 0.0F);
        this.rightleg2V = new ModelRenderer(this, 0, 16);
        this.rightleg2V.setRotationPoint(-1.0F, 21.0F, -1.6F);
        this.rightleg2V.addBox(0.0F, -1.0F, 0.0F, 6, 1, 1, 0.0F);
        this.assignRotationRotate(rightleg2V, 0.0F, 0.136659280431156F, 2.777342438698576F);
        this.leftleg3V = new ModelRenderer(this, 0, 16);
        this.leftleg3V.setRotationPoint(1.0F, 21.0F, 0.0F);
        this.leftleg3V.addBox(0.0F, 0.0F, 0.0F, 6, 1, 1, 0.0F);
        this.assignRotationRotate(leftleg3V, 0.0F, -0.36425021489121656F, 0.36425021489121656F);
        this.rightclawV = new ModelRenderer(this, 0, 16);
        this.rightclawV.setRotationPoint(-0.7F, 21.0F, -2.7F);
        this.rightclawV.addBox(0.0F, 0.0F, 0.0F, 6, 1, 1, 0.0F);
        this.assignRotationRotate(rightclawV, 0.0F, 2.231054382824351F, 0.0F);
        this.skull = new ModelRenderer(this, 0, 0);
        this.skull.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.skull.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
        this.assignRotationRotate(skull, 1.2292353921796064F, 0.0F, 0.0F);
        this.body = new ModelRenderer(this, 32, 0);
        this.body.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.body.addBox(-2.0F, -3.0F, -1.0F, 4, 8, 4, 0.0F);
        this.assignRotationRotate(body, -1.8668041679331349F, 0.0F, 0.0F);
        this.leftclawV = new ModelRenderer(this, 0, 16);
        this.leftclawV.setRotationPoint(0.0F, 21.0F, -3.5F);
        this.leftclawV.addBox(0.0F, 0.0F, 0.0F, 6, 1, 1, 0.0F);
        this.assignRotationRotate(leftclawV, 0.0F, 0.7285004297824331F, 0.0F);
        this.rightleg1V = new ModelRenderer(this, 0, 16);
        this.rightleg1V.setRotationPoint(-1.0F, 21.0F, -2.5F);
        this.rightleg1V.addBox(0.0F, -1.0F, 0.0F, 6, 1, 1, 0.0F);
        this.assignRotationRotate(rightleg1V, 0.0F, 0.5918411493512771F, 2.777342438698576F);
        this.body.addChild(this.righteye);
        this.body.addChild(this.lefteye);
        this.body.addChild(this.skull);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.leftleg1V.render(f5);
        this.leftleg2V.render(f5);
        this.rightleg3V.render(f5);
        this.rightleg2V.render(f5);
        this.leftleg3V.render(f5);
        this.rightclawV.render(f5);
        this.body.render(f5);
        this.leftclawV.render(f5);
        this.rightleg1V.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void assignRotationRotate(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
