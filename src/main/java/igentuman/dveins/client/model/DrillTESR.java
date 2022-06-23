package igentuman.dveins.client.model;

import igentuman.dveins.DVeins;
import igentuman.dveins.common.tile.TileDrill;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

public class DrillTESR extends TileEntitySpecialRenderer<TileDrill> {
    private static final ResourceLocation BELLOWS_BAG_TEXTURE = new ResourceLocation(
            DVeins.MODID,
            "textures/blocks/bellows_bag2.png"
    );
    private final ModelDrill modelBag = new ModelDrill();

    @Override
    public void render(TileDrill te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        GlStateManager.translate(x, y, z);
        GlStateManager.enableRescaleNormal();

        int direction = 0;

        if (te.hasWorld()) {
            direction = te.getBlockMetadata();
        }

        int angle = (360 - direction * 90) % 360;

       // double rotation = Math.toRadians(te.currentRotation + te.getRotationSpeed() * partialTicks);
       // double pistonScale = 0.75 + Math.sin(rotation) * 0.25;
       // double pistonOffset = 0.125 + 0.75 * (1.0 - pistonScale);

        GlStateManager.translate(0.5F, 0.5F, 0.5F);
        GlStateManager.rotate((float)angle, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);

       // GlStateManager.translate(0, 0, pistonOffset);
       // GlStateManager.scale(1, 1, pistonScale);
        GlStateManager.translate(0, 0, -1);

        renderBag();

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

    private void renderBag() {
        this.bindTexture(BELLOWS_BAG_TEXTURE);
        modelBag.render();
    }
}
