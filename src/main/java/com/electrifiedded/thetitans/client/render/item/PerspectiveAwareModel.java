package com.electrifiedded.thetitans.client.render.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import javax.vecmath.Matrix4f;

public class PerspectiveAwareModel implements IBakedModel {
    // Set by handlePerspective on the render thread just before renderByItem runs; read by the TEISRs.
    public static TransformType transformLive = TransformType.NONE;
    private final IBakedModel base;

    public PerspectiveAwareModel(IBakedModel base) {
        this.base = base;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType type) {
        transformLive = type;
        Pair<? extends IBakedModel, Matrix4f> pair = net.minecraftforge.client.ForgeHooksClient.handlePerspective(base, type);
        // Return THIS wrapper (not base) so isBuiltInRenderer() stays true and the TEISR is invoked.
        return Pair.of(this, pair.getRight());
    }

    @Override public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) { return base.getQuads(state, side, rand); }
    @Override public boolean isAmbientOcclusion() { return base.isAmbientOcclusion(); }
    @Override public boolean isGui3d() { return base.isGui3d(); }
    @Override public boolean isBuiltInRenderer() { return true; }
    @Override public TextureAtlasSprite getParticleTexture() { return base.getParticleTexture(); }
    @Override public ItemCameraTransforms getItemCameraTransforms() { return base.getItemCameraTransforms(); }
    @Override public ItemOverrideList getOverrides() { return base.getOverrides(); }
}
