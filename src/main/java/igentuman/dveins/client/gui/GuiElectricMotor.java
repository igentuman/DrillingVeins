package igentuman.dveins.client.gui;

import igentuman.dveins.DVeins;
import igentuman.dveins.common.container.ContainerElectricMotor;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;


public class GuiElectricMotor extends GuiContainer {
    private static final ResourceLocation background = new ResourceLocation(
            DVeins.MODID, "textures/gui/container/motor.png"
    );

    private final ContainerElectricMotor container;

    public GuiElectricMotor(ContainerElectricMotor inventorySlotsIn) {
        super(inventorySlotsIn);
        this.container = inventorySlotsIn;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
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

        GlStateManager.popMatrix();
    }


    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString(I18n.format("container.electric_motor"), 8, 6, 4210752);
        this.fontRenderer.drawString(I18n.format("container.electric_motor.energy", container.getEnergyStored()), 8, 16, 4210752);
    }
}
