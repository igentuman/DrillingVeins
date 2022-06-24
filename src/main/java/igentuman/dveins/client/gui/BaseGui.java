package igentuman.dveins.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public abstract class BaseGui extends GuiContainer {
    public BaseGui(Container inventorySlotsIn) {
        super(inventorySlotsIn);
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
}
