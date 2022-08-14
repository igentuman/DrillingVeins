package igentuman.dveins.recipe;

import java.util.ArrayList;
import java.util.List;

public class ForgeHammerRecipes extends BasicRecipeHandler {
	
	public ForgeHammerRecipes() {
		super("forge_hammer", 1, 0, 1, 0);
	}
	
	@Override
	public void addRecipes() {
		addHammerRecipe("ingotCopper", 1, "plateCopper", 1);
		addHammerRecipe("ingotIron", 1, "plateIron", 1);
		addHammerRecipe("ingotGold", 1, "plateGold", 1);
		addHammerRecipe("ingotLead", 1, "plateLead", 1);
		addHammerRecipe("ingotTin", 1, "plateTin", 1);

		addHammerRecipe("chunkIron", 2, "dustIron", 1);
		addHammerRecipe("chunkGold", 2, "dustGold", 1);
		addHammerRecipe("chunkCopper", 2, "dustCopper", 1);
		addHammerRecipe("chunkTin", 2, "dustTin", 1);
		addHammerRecipe("chunkLead", 2, "dustLead", 1);
	}

	public void addHammerRecipe(String in1, int inSize1, String out, int outSize) {
		addRecipe(oreStack(in1,  inSize1), oreStack(out, outSize));
	}

	@Override
	public List<Object> fixExtras(List<Object> extras) {
		List<Object> fixed = new ArrayList<>(3);
		fixed.add(extras.size() > 0 && extras.get(0) instanceof Double ? (double) extras.get(0) : 1D);
		fixed.add(extras.size() > 1 && extras.get(1) instanceof Double ? (double) extras.get(1) : 1D);
		fixed.add(extras.size() > 2 && extras.get(2) instanceof Double ? (double) extras.get(2) : 0D);
		return fixed;
	}
}
