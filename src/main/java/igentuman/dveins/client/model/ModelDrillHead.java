package igentuman.dveins.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelDrillHead extends ModelBase {
        public ModelRenderer drillHead = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);

        public ModelDrillHead() {

                // this.bag.addBox(0.0F, -5.0F, -14.0F, 12, 12, 12, 0.0F);
                //this.bag.addBox(0.0F, -5.0F, 0.0F, 12, 12, 12, 0.0F);
                this.drillHead.rotationPointX = 0F;
                this.drillHead.rotationPointY = 0F;
                this.drillHead.rotationPointZ = 45.0F;
        }

        public void render() {
                this.drillHead.render(1);
        }
}
