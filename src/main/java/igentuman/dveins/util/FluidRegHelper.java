package igentuman.dveins.util;

import net.minecraftforge.fluids.FluidRegistry;

import java.util.Locale;

public class FluidRegHelper {
	
	public static boolean fluidExists(String name) {
		return FluidRegistry.getRegisteredFluids().containsKey(name.toLowerCase(Locale.ROOT));
	}
	
	public static boolean fluidsExist(String... names) {
		for (String name : names) {
			if (!fluidExists(name)) {
				return false;
			}
		}
		return true;
	}
}
