package igentuman.dveins.client.gui;

import igentuman.dveins.DVeins;
import igentuman.dveins.common.container.ContainerForgehammer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiForgeHammer extends GuiContainer {
    private static final ResourceLocation background = new ResourceLocation(
            DVeins.MODID, "textures/gui/container/forge_hammer.png"
    );

    private final ContainerForgehammer container;

    public GuiForgeHammer(ContainerForgehammer inventorySlotsIn) {
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
    protected void drawItem(ItemStack itemstack, int x, int y) {
        this.zLevel = 100.0F;
        this.itemRender.zLevel = 100.0F;

        GlStateManager.enableDepth();
        this.itemRender.renderItemAndEffectIntoGUI(this.mc.player, itemstack, x, y);
        this.itemRender.renderItemOverlayIntoGUI(this.fontRenderer, itemstack, x, y, null);

        this.itemRender.zLevel = 0.0F;
        this.zLevel = 0.0F;
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
        this.fontRenderer.drawString(I18n.format("container.forge_hammer"), 28, 6, 4210752);
        this.fontRenderer.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }
}
