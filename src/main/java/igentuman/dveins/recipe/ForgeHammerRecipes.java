package igentuman.dveins.recipe;

import com.google.common.collect.Lists;
import igentuman.dveins.recipe.ingridient.OreIngredient;
import igentuman.dveins.util.OreDictHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;

import java.util.ArrayList;
import java.util.List;

import static igentuman.dveins.util.OreDictHelper.*;

public class ForgeHammerRecipes extends BasicRecipeHandler {
	
	public ForgeHammerRecipes() {
		super("alloy_furnace", 2, 0, 1, 0);
	}
	
	@Override
	public void addRecipes() {
		addAlloyIngotIngotRecipes("Copper", 3, "Tin", 1, "Bronze", 4, 1D, 1D);
		if (OreDictHelper.oreExists("dustCoke") || OreDictHelper.oreExists("fuelCoke")) {
			addAlloyIngotIngotRecipes("Iron", 1, "Graphite", 4, "Steel", 1, 1D, 1D);
		}
	}
	
	public void addAlloyIngotIngotRecipes(String in1, int inSize1, String in2, int inSize2, String out, int outSize, double time, double power) {
		addAlloyRecipes(in1, inSize1, in2, inSize2, out, outSize, time, power, NUGGET_VOLUME_TYPES, INGOT_VOLUME_TYPES, BLOCK_VOLUME_TYPES, NUGGET_VOLUME_TYPES, INGOT_VOLUME_TYPES, BLOCK_VOLUME_TYPES);
	}
	
	public void addAlloyIngotDustRecipes(String in1, int inSize1, String in2, int inSize2, String out, int outSize, double time, double power) {
		addAlloyRecipes(in1, inSize1, in2, inSize2, out, outSize, time, power, NUGGET_VOLUME_TYPES, INGOT_VOLUME_TYPES, BLOCK_VOLUME_TYPES, TINYDUST_VOLUME_TYPES, DUST_VOLUME_TYPES, BLOCK_VOLUME_TYPES);
	}

	
	public void addAlloyRecipes(String in1, int inSize1, String in2, int inSize2, String out, int outSize, double time, double power, List<String> inNuggets1, List<String> inIngots1, List<String> inBlocks1, List<String> inNuggets2, List<String> inIngots2, List<String> inBlocks2) {
		addRecipe(typeStackList(in1, inIngots1, inSize1), typeStackList(in2, inIngots2, inSize2), oreStack("ingot" + out, outSize), time, power);
		addRecipe(typeStackList(in1, inNuggets1, inSize1), typeStackList(in2, inNuggets2, inSize2), oreStack("nugget" + out, outSize), time / 9D, power);
		addRecipe(typeStackList(in1, inBlocks1, inSize1), typeStackList(in2, inBlocks2, inSize2), oreStack("block" + out, outSize), time * 9D, power);
	}
	

	
	private static ArrayList<OreIngredient> typeStackList(String type, List<String> forms, int size) {
		ArrayList<OreIngredient> list = new ArrayList<>();
		for (String form : forms) {
			list.add(oreStack(form + type, size));
		}
		return list;
	}
	
	private static final List<String> SILICON = Lists.newArrayList("itemSilicon", "ingotSilicon");
	private static final List<Object> ENDER_PEARL = Lists.newArrayList(Items.ENDER_PEARL, "dustEnder");
	
	private static List<String> metalList(String name) {
		return Lists.newArrayList("ingot" + name, "dust" + name);
	}
	
	private static List<String> gemList(String name) {
		return Lists.newArrayList("gem" + name, "dust" + name);
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
