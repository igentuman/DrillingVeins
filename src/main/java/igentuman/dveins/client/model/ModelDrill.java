package igentuman.dveins.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelDrill extends ModelBase {
        public ModelRenderer bag = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 32);

        public ModelDrill() {
                // this.bag.addBox(0.0F, -5.0F, -14.0F, 12, 12, 12, 0.0F);
                this.bag.addBox(0.0F, -5.0F, 0.0F, 12, 12, 12, 0.0F);
                this.bag.rotationPointX = 2.0F;
                this.bag.rotationPointY = 7.0F;
                this.bag.rotationPointZ = 16.0F;
        }

        public void render() {
                this.bag.render(0.0625F);
        }
}
