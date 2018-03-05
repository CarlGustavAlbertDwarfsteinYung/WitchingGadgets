package witchinggadgets.common.util.handler;

import java.util.HashMap;

import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import witchinggadgets.common.util.Utilities;

public class InfusedGemHandler
{
	public static HashMap<String, Aspect[]> naturalAffinities = new HashMap<String, Aspect[]>();
	public static HashMap<String, Aspect[]> naturalAversions = new HashMap<String, Aspect[]>();

	static{
		registerAffinities("gemDiamond", Aspect.ORDER,Aspect.ENTROPY);
		registerAffinities("gemEmerald", Aspect.ORDER,Aspect.EARTH);
		registerAversions("gemEmerald", Aspect.WATER,Aspect.ENTROPY);
		registerAffinities("crystalNetherQuartz", Aspect.ENTROPY,Aspect.FIRE);
		registerAversions("crystalNetherQuartz", Aspect.ORDER,Aspect.WATER);
		registerAffinities("crystalCertusQuartz", Aspect.ORDER,Aspect.AIR);
		registerAversions("crystalCertusQuartz", Aspect.ENTROPY,Aspect.FIRE);

		// BOP
		registerAffinities("gemRuby", Aspect.FIRE);
		registerAversions("gemRuby", Aspect.ENTROPY,Aspect.WATER);
		registerAffinities("gemPeridot", Aspect.EARTH);
		registerAversions("gemPeridot", Aspect.ENTROPY,Aspect.FIRE);
		registerAffinities("gemTopaz", Aspect.AIR,Aspect.FIRE);
		registerAversions("gemTopaz", Aspect.ENTROPY,Aspect.WATER);
		registerAffinities("gemTanzanite", Aspect.WATER);
		registerAversions("gemTanzanite", Aspect.ENTROPY,Aspect.FIRE);
		registerAffinities("gemMalachite", Aspect.EARTH,Aspect.WATER);
		registerAversions("gemMalachite", Aspect.ENTROPY,Aspect.FIRE);
		registerAffinities("gemSapphire", Aspect.WATER);
		registerAversions("gemSapphire", Aspect.ENTROPY,Aspect.FIRE);
		registerAffinities("gemAmber", Aspect.AIR,Aspect.EARTH);
		registerAversions("gemAmber", Aspect.ENTROPY);

		// AM2
		registerAffinities("gemBlueTopaz", Aspect.AIR,Aspect.WATER);
		registerAversions("gemBlueTopaz", Aspect.FIRE,Aspect.ORDER,Aspect.ENTROPY);
		registerAffinities("gemMoonstone", Aspect.AIR,Aspect.WATER,Aspect.ENTROPY);
		registerAversions("gemMoonstone", Aspect.FIRE,Aspect.ORDER);
		registerAffinities("gemChimerite", Aspect.WATER,Aspect.ENTROPY,Aspect.ORDER);
		registerAversions("gemChimerite", Aspect.FIRE,Aspect.AIR);

		// FORESTRY
		registerAffinities("gemApatite", Aspect.WATER);
		registerAversions("gemApatite", Aspect.AIR,Aspect.FIRE,Aspect.WATER,Aspect.ORDER,Aspect.ENTROPY);

		registerAffinities("gemPrimordial", Aspect.AIR,Aspect.EARTH,Aspect.FIRE,Aspect.WATER,Aspect.ORDER,Aspect.ENTROPY);
	}


	public static void registerAffinities(Object o, Aspect... aspects)
	{
		if(o instanceof String)
			naturalAffinities.put((String)o, aspects);
		else if(o instanceof ItemStack)
			naturalAffinities.put(Utilities.getItemStackString((ItemStack)o), aspects);
	}
	public static void removeAffinities(Object o)
	{
		if(o instanceof String)
			naturalAffinities.remove((String)o);
		else if(o instanceof ItemStack)
			naturalAffinities.remove(Utilities.getItemStackString((ItemStack)o));
	}
	public static void registerAversions(Object o, Aspect... aspects)
	{
		if(o instanceof String)
			naturalAversions.put((String)o, aspects);
		else if(o instanceof ItemStack)
			naturalAversions.put(Utilities.getItemStackString((ItemStack)o), aspects);
	}
	public static void removeAversions(Object o)
	{
		if(o instanceof String)
			naturalAversions.remove((String)o);
		else if(o instanceof ItemStack)
			naturalAversions.remove(Utilities.getItemStackString((ItemStack)o));
	}


	public static Aspect[] getNaturalAffinities(ItemStack gem)
	{
		String s0 = Utilities.getItemStackString(gem);
		if(naturalAffinities.containsKey(s0))
			return naturalAffinities.get(s0);
		for(String s1 : naturalAffinities.keySet())
			if(Utilities.compareToOreName(gem, s1))
				return naturalAffinities.get(s1);
		return new Aspect[0];
	}
	public static Aspect[] getNaturalAversions(ItemStack gem)
	{
		String s0 = Utilities.getItemStackString(gem);
		if(naturalAversions.containsKey(s0))
			return naturalAversions.get(s0);
		for(String s1 : naturalAversions.keySet())
			if(Utilities.compareToOreName(gem, s1))
				return naturalAversions.get(s1);
		return new Aspect[0];
	}

	public static boolean isGem(ItemStack stack)
	{
		String s0 = Utilities.getItemStackString(stack);
		if(naturalAffinities.containsKey(s0) || naturalAversions.containsKey(s0))
			return true;
		for(String s : naturalAffinities.keySet())
			if(Utilities.compareToOreName(stack, s))
				return true;
		for(String s : naturalAversions.keySet())
			if(Utilities.compareToOreName(stack, s))
				return true;
		return false;
	}
}