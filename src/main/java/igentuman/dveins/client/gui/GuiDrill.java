package igentuman.dveins.client.gui;

import igentuman.dveins.DVeins;
import igentuman.dveins.ModConfig;
import igentuman.dveins.common.container.ContainerDrill;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiDrill extends GuiContainer {
    private static final ResourceLocation background = new ResourceLocation(
            DVeins.MODID, "textures/gui/container/drill.png"
    );

    private final ContainerDrill container;

    public GuiDrill(ContainerDrill container) {
        super(container);
        this.container = container;
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    protected void drawItem(ItemStack itemstack, int x, int y) {
        this.zLevel = 100.0F;
        this.itemRender.zLevel = 100.0F;

        GlStateManager.enableDepth();
        this.itemRender.renderItemAndEffectIntoGUI(this.mc.player, itemstack, x, y);
        this.itemRender.renderItemOverlayIntoGUI(this.fontRenderer, itemstack, x, y, null);

        this.itemRender.zLevel = 0.0F;
        this.zLevel = 0.0F;
    }

    private int getProgressScaled(int pixels) {
        if(container.progress == 0 || ModConfig.drilling.energy_for_one_block == 0) return 0;
        return container.getKineticEnergy() * pixels / ModConfig.drilling.energy_for_one_block;
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
        drawItem(container.ore, 147, 11);
        GlStateManager.popMatrix();
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void renderHoveredToolTip(int mouseX, int mouseY) {
        if(!container.ore.isEmpty()
                && isPointInRegion(147, 11, 16, 16, mouseX, mouseY)) {
            this.renderToolTip(container.ore, mouseX, mouseY);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString(I18n.format("container.drill"), 28, 6, 4210752);
        if(container.ore != ItemStack.EMPTY) {
            this.fontRenderer.drawString(I18n.format("container.drill.ore", container.ore.getDisplayName()), 6, 16, 4210752);
            this.fontRenderer.drawString(I18n.format("container.drill.ore_left", container.getBlocksLeft()), 6, 26, 4210752);
            this.fontRenderer.drawString(I18n.format("container.drill.current_y", container.getCurrentY()), 6, 36, 4210752);
        }
    }
}
