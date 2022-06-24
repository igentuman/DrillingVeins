package igentuman.dveins.client.gui;

import igentuman.dveins.DVeins;
import igentuman.dveins.ModConfig;
import igentuman.dveins.common.container.ContainerDrill;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiDrill extends BaseGui {
    private static final ResourceLocation background = new ResourceLocation(
            DVeins.MODID, "textures/gui/container/drill.png"
    );

    private final ContainerDrill container;

    public GuiDrill(ContainerDrill inventorySlotsIn) {
        super(inventorySlotsIn);
        this.container = inventorySlotsIn;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(background);

        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        int progress = this.getProgressScaled(26);
        this.drawTexturedModalRect(
                guiLeft + 86, guiTop + 35,
                176, 0,
                progress + 1, 18
        );
    }

    private int getProgressScaled(int pixels) {
        if(container.progress == 0 || container.requiredProgress == 0) return 0;
        return container.progress * pixels / container.requiredProgress;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)this.guiLeft, (float)this.guiTop, 0.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableRescaleNormal();

        drawItem(container.result, 124, 35);

        GlStateManager.popMatrix();

        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void renderHoveredToolTip(int mouseX, int mouseY) {
        if(!this.mc.player.inventory.getItemStack().isEmpty()) return;

        if(getSlotUnderMouse() != null) {
            super.renderHoveredToolTip(mouseX, mouseY);
        }
        else if(!container.result.isEmpty()
                && isPointInRegion(124, 35, 16, 16, mouseX, mouseY)) {
            this.renderToolTip(container.result, mouseX, mouseY);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString(I18n.format("container.drill"), 28, 6, 4210752);
        this.fontRenderer.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);

        if(!ModConfig.DEBUG) return;

        String progressString = String.format(
                "Progress: %d/%d\nResult: %s\nResult empty: %s",
                container.progress,
                container.requiredProgress,
                container.result.getDisplayName(),
                container.result.isEmpty()
        );
        this.fontRenderer.drawSplitString(
                progressString, 8, 8, 80, 255 << 16
        );
    }
}
