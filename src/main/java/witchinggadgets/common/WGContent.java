package witchinggadgets.common;

import java.util.Arrays;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import thaumcraft.api.ItemApi;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigItems;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.common.blocks.BlockModifiedAiry;
import witchinggadgets.common.blocks.BlockRoseVines;
import witchinggadgets.common.blocks.BlockVoidWalkway;
import witchinggadgets.common.blocks.BlockWGMetalDevice;
import witchinggadgets.common.blocks.BlockWGStoneDevice;
import witchinggadgets.common.blocks.BlockWGWoodenDevice;
import witchinggadgets.common.blocks.BlockWallMirror;
import witchinggadgets.common.blocks.ItemBlockMetalDevice;
import witchinggadgets.common.blocks.ItemBlockStoneDevice;
import witchinggadgets.common.blocks.ItemBlockWoodenDevice;
import witchinggadgets.common.blocks.tiles.*;
import witchinggadgets.common.mob.EntityScarecrow;
import witchinggadgets.common.items.EntityItemReforming;
import witchinggadgets.common.items.ItemClusters;
import witchinggadgets.common.items.ItemCrystalCapsule;
import witchinggadgets.common.items.ItemInfusedGem;
import witchinggadgets.common.items.ItemMagicFood;
import witchinggadgets.common.items.ItemMaterials;
import witchinggadgets.common.items.ItemThaumiumShears;
import witchinggadgets.common.items.ItemAdvancedScribingTools;
import witchinggadgets.common.items.armor.ItemAdvancedRobes;
import witchinggadgets.common.items.armor.ItemPrimordialArmor;
import witchinggadgets.common.items.baubles.ItemCloak;
import witchinggadgets.common.items.baubles.ItemKama;
import witchinggadgets.common.items.baubles.ItemMagicalBaubles;
import witchinggadgets.common.items.tools.ItemBag;
import witchinggadgets.common.items.tools.ItemPrimordialAxe;
import witchinggadgets.common.items.tools.ItemPrimordialGlove;
import witchinggadgets.common.items.tools.ItemPrimordialHammer;
import witchinggadgets.common.items.tools.ItemPrimordialSword;
import witchinggadgets.common.items.tools.ItemScanCamera;
import witchinggadgets.common.magic.*;
import witchinggadgets.common.util.Utilities;
import witchinggadgets.common.util.handler.WGMultiPartHandler;
import witchinggadgets.common.util.recipe.BagColourizationRecipe;
import witchinggadgets.common.util.recipe.CloakColourizationRecipe;
import witchinggadgets.common.util.recipe.InfernalBlastfurnaceRecipe;
import witchinggadgets.common.util.recipe.RobeColourizationRecipe;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class WGContent
{
	public static Block BlockWallMirror;
	public static Block BlockVoidWalkway;
	public static Block BlockPortal;
	public static Block BlockStoneDevice;
	public static Block BlockWoodenDevice;
	public static Block BlockMetalDevice;
	public static Block BlockMagicBed;
	public static Block BlockRoseVine;
	public static Block BlockCustomAiry;

	public static Item ItemMaterial;
	public static Item ItemCluster;
	public static Item ItemCapsule;
	public static Item ItemBag;
	public static Item ItemCloak;
	public static Item ItemKama;
	public static Item ItemThaumiumShears;
	public static Item ItemAdvancedRobeChest;
	public static Item ItemAdvancedRobeLegs;
	public static Item ItemMagicFoodstuffs;
	//public static Item ItemMagicBed;

	public static Item ItemAdvancedScribingTools;

	//	public static Item ItemEliteArmorHelm;
	//	public static Item ItemEliteArmorChest;
	//	public static Item ItemEliteArmorLegs;
	//	public static Item ItemEliteArmorBoots;
	public static Item ItemPrimordialGlove;
	public static Item ItemPrimordialHammer;
	public static Item ItemPrimordialAxe;
	public static Item ItemPrimordialSword;

	public static Item ItemPrimordialHelm;
	public static Item ItemPrimordialChest;
	public static Item ItemPrimordialLegs;
	public static Item ItemPrimordialBoots;

	public static Item ItemInfusedGem;
	public static Item ItemMagicalBaubles;
	public static Item ItemScanCamera;
	//public static Item ItemRelic;

	public static Potion pot_knockbackRes;
	public static Potion pot_dissolve;
	public static Potion pot_cinderCoat;
	public static Enchantment enc_gemstonePotency;
	public static Enchantment enc_gemstoneBrittle;
	public static Enchantment enc_invisibleGear;
	public static Enchantment enc_unveiling;
	public static Enchantment enc_stealth;
	public static Enchantment enc_backstab;
	public static Enchantment enc_rideProtect;
	public static Enchantment enc_soulbound;

	public static ArmorMaterial armorMatSpecialRobe = EnumHelper.addArmorMaterial("WG:ADVANCEDCLOTH", 25, new int[] { 2, 4, 3, 2 }, 25);
	public static ToolMaterial primordialTool = EnumHelper.addToolMaterial("WG:PRIMORDIALTOOL",4, 1500, 8, 6, 25);
	public static ArmorMaterial primordialArmor = EnumHelper.addArmorMaterial("WG:PRIMORDIALARMOR", 40, new int[] {3,7,6,3}, 30);

	public static void preInit()
	{
		preInitItems();
		preInitBlocks();
	}
	final static String UUIDBASE = "424C5553-5747-1694-4452-";
	public static void init()
	{
		initializeItems();
		initializeBlocks();
		initMobs();

		int k = Potion.potionTypes.length;
		int l = 3;
		if(k<128-l)
			Utilities.extendPotionArray(l);
		String s = new UUID(109406002307L, 01L).toString();
		int potionId = WGConfig.getPotionID(32, "Knockback Resistance");
		if(potionId>0)
			pot_knockbackRes = new WGPotion(potionId, false, 0x6e6e6e, 0, false, 1).setPotionName("wg.potionKnockbackRes").func_111184_a(SharedMonsterAttributes.knockbackResistance, s, 0.34D, 0);
		potionId = WGConfig.getPotionID(potionId, "Dissolve");
		if(potionId>0)
			pot_dissolve = new WGPotion(potionId, true, 0x450b45, 40, true, 2).setPotionName("wg.potionDissolve");
		potionId = WGConfig.getPotionID(potionId, "Cinder Coat");
		if(potionId>0)
			pot_cinderCoat = new WGPotion(potionId, true, 0x8f3f1f, 0, false, 3).setPotionName("wg.potionCinderCoat");

		int enchId = WGConfig.getEnchantmentID(64, "Gemstone Potency");
		if(enchId>0)
			enc_gemstonePotency = new WGEnchantGemPotency(enchId, 4);
		enchId = WGConfig.getEnchantmentID(enchId, "Gemstone Brittle");
		if(enchId>0)
			enc_gemstoneBrittle = new WGEnchantGemBrittle(enchId, 1);
		enchId = WGConfig.getEnchantmentID(enchId, "Gemstone Invisible Gear");
		if(enchId>0)
			enc_invisibleGear = new WGEnchantInvisibleGear(enchId);
		enchId = WGConfig.getEnchantmentID(enchId, "Gemstone Unveiling");
		if(enchId>0)
			enc_unveiling = new WGEnchantUnveiling(enchId);
		enchId = WGConfig.getEnchantmentID(enchId, "Gemstone Stealth");
		if(enchId>0)
			enc_stealth = new WGEnchantStealth(enchId);
		enchId = WGConfig.getEnchantmentID(enchId, "Gemstone Backstab");
		if(enchId>0)
			enc_backstab = new WGEnchantBackstab(enchId);
		enchId = WGConfig.getEnchantmentID(enchId, "Gemstone Ride Protection");
		if(enchId>0)
			enc_rideProtect = new WGEnchantRideProtect(enchId);
		enchId = WGConfig.getEnchantmentID(enchId, "Soulbound");
		if(enchId>0)
			enc_soulbound = new WGEnchantSoulbound(enchId);
	}
	public static void postInit()
	{
		postInitItems();
		postInitBlocks();
		postInitThaumcraft();
	}

	private static void preInitBlocks()
	{
		BlockWallMirror = new BlockWallMirror().setBlockName("WG_WallMirror");
		GameRegistry.registerBlock(BlockWallMirror, BlockWallMirror.getUnlocalizedName().substring("tile.".length()));

		BlockVoidWalkway = new BlockVoidWalkway().setBlockName("WG_VoidWalkway");
		GameRegistry.registerBlock(BlockVoidWalkway, BlockVoidWalkway.getUnlocalizedName().substring("tile.".length()));

		BlockStoneDevice = new BlockWGStoneDevice().setBlockName("WG_StoneDevice");
		GameRegistry.registerBlock(BlockStoneDevice, ItemBlockStoneDevice.class, BlockStoneDevice.getUnlocalizedName().substring("tile.".length()));

		BlockWoodenDevice = new BlockWGWoodenDevice().setBlockName("WG_WoodenDevice");
		GameRegistry.registerBlock(BlockWoodenDevice, ItemBlockWoodenDevice.class, BlockWoodenDevice.getUnlocalizedName().substring("tile.".length()));

		BlockMetalDevice = new BlockWGMetalDevice().setBlockName("WG_MetalDevice");
		GameRegistry.registerBlock(BlockMetalDevice, ItemBlockMetalDevice.class, BlockMetalDevice.getUnlocalizedName().substring("tile.".length()));

		BlockRoseVine = new BlockRoseVines().setBlockName("WG_RoseVine");
		GameRegistry.registerBlock(BlockRoseVine, BlockRoseVine.getUnlocalizedName().substring("tile.".length()));

		BlockCustomAiry = new BlockModifiedAiry().setBlockName("WG_CustomAir");
		GameRegistry.registerBlock(BlockCustomAiry, BlockCustomAiry.getUnlocalizedName().substring("tile.".length()));

		if (!WGModCompat.loaded_TBases) {
			OreDictionary.registerOre("blockVoid", new ItemStack(BlockMetalDevice, 1, 7));
		}
	}

	private static void initMobs()
	{
		EntityRegistry.registerModEntity(EntityScarecrow.class, "scarecrow", 240, WitchingGadgets.instance, 80, 3, true);
		EntityList.addMapping(EntityScarecrow.class, "scarecrow", 0,16247203,10987431);
	}

	private static void initializeBlocks()
	{
		if(Loader.isModLoaded("ForgeMultipart"))
			WGMultiPartHandler.instance.init();

		//UNIQUE
		registerTile(TileEntityWallMirror.class);
		registerTile(TileEntityVoidWalkway.class);
		registerTile(TileEntityTempLight.class);
		//STONE
		registerTile(TileEntityEtherealWall.class);
		registerTile(TileEntityMagicalTileLock.class);
		registerTile(TileEntitySarcophagus.class);
		registerTile(TileEntityAgeingStone.class);
		registerTile(TileEntityBlastfurnace.class);
		//WOODEN
		registerTile(TileEntitySpinningWheel.class);
		registerTile(TileEntitySnowGen.class);
		registerTile(TileEntityCobbleGen.class);
		registerTile(TileEntityCuttingTable.class);
		registerTile(TileEntitySaunaStove.class);
		registerTile(TileEntityLabelLibrary.class);
		//METAL
		registerTile(TileEntityEssentiaPump.class);
		registerTile(TileEntityTerraformer.class);
		registerTile(TileEntityTerraformFocus.class);

		//GameRegistry.registerTileEntity(TileEntityTotem.class, "TileEntityTotem");
		//GameRegistry.registerTileEntity(TileEntityEssentiaVapourizer.class, "TileEntityEssentiaVapourizer");
	}
	private static void registerTile(Class<? extends TileEntity> c)
	{
		GameRegistry.registerTileEntity(c, "WitchingGadgets_"+c.getCanonicalName().substring(c.getCanonicalName().lastIndexOf(".")));
	}

	private static void postInitBlocks()
	{
		boolean rc = WGModCompat.railcraftAllowBlastFurnace();
		for(int yy=0;yy<=1;yy++)
			for(int zz=0;zz<=2;zz++)
				for(int xx=0;xx<=2;xx++)
				{
					int pos = yy*9 + zz*3 + xx;
					if(rc)
						TileEntityBlastfurnace.brickBlock[pos] = GameRegistry.findBlock("Railcraft","brick.infernal");
					else			
						TileEntityBlastfurnace.brickBlock[pos] = pos<9&&pos!=4?Blocks.nether_brick: pos==10||pos==12||pos==13||pos==14||pos==16?Blocks.soul_sand: Blocks.obsidian;
				}

		TileEntityBlastfurnace.stairBlock = rc? GameRegistry.findBlock("Railcraft", "stair"): Blocks.nether_brick_stairs;
	}

	private static void preInitItems()
	{
		ItemMaterial = new ItemMaterials().setUnlocalizedName("WG_Material");
		GameRegistry.registerItem(ItemMaterial, ItemMaterial.getUnlocalizedName());

		ItemBag = new ItemBag().setUnlocalizedName("WG_Bag");
		GameRegistry.registerItem(ItemBag, ItemBag.getUnlocalizedName());

		ItemThaumiumShears = new ItemThaumiumShears().setUnlocalizedName("WG_ThaumiumShears");
		GameRegistry.registerItem(ItemThaumiumShears, ItemThaumiumShears.getUnlocalizedName());

		ItemAdvancedRobeChest = new ItemAdvancedRobes(armorMatSpecialRobe,2,1).setUnlocalizedName("WG_AdvancedRobeChest");
		GameRegistry.registerItem(ItemAdvancedRobeChest, ItemAdvancedRobeChest.getUnlocalizedName());
		ItemAdvancedRobeLegs = new ItemAdvancedRobes(armorMatSpecialRobe,2,2).setUnlocalizedName("WG_AdvancedRobeLegs");
		GameRegistry.registerItem(ItemAdvancedRobeLegs, ItemAdvancedRobeLegs.getUnlocalizedName());

		ItemMagicFoodstuffs = new ItemMagicFood().setUnlocalizedName("WG_MagicFood");
		GameRegistry.registerItem(ItemMagicFoodstuffs, ItemMagicFoodstuffs.getUnlocalizedName());

		ItemCloak = new ItemCloak().setUnlocalizedName("WG_Cloak");
		GameRegistry.registerItem(ItemCloak, ItemCloak.getUnlocalizedName());

		ItemKama = new ItemKama().setUnlocalizedName("WG_Kama");
		GameRegistry.registerItem(ItemKama, ItemKama.getUnlocalizedName());

		ItemInfusedGem = new ItemInfusedGem().setUnlocalizedName("WG_InfusedGem");
		GameRegistry.registerItem(ItemInfusedGem, ItemInfusedGem.getUnlocalizedName());

		ItemMagicalBaubles = new ItemMagicalBaubles().setUnlocalizedName("WG_Baubles");
		GameRegistry.registerItem(ItemMagicalBaubles, ItemMagicalBaubles.getUnlocalizedName());

		ItemScanCamera = new ItemScanCamera().setUnlocalizedName("WG_ScanCamera");
		GameRegistry.registerItem(ItemScanCamera, ItemScanCamera.getUnlocalizedName());

		ItemPrimordialGlove = new ItemPrimordialGlove().setUnlocalizedName("WG_PrimordialGlove");
		GameRegistry.registerItem(ItemPrimordialGlove, ItemPrimordialGlove.getUnlocalizedName());
		ItemPrimordialHammer = new ItemPrimordialHammer(primordialTool).setUnlocalizedName("WG_PrimordialHammer");
		GameRegistry.registerItem(ItemPrimordialHammer, ItemPrimordialHammer.getUnlocalizedName());
		ItemPrimordialAxe = new ItemPrimordialAxe(primordialTool).setUnlocalizedName("WG_PrimordialAxe");
		GameRegistry.registerItem(ItemPrimordialAxe, ItemPrimordialAxe.getUnlocalizedName());
		ItemPrimordialSword = new ItemPrimordialSword(primordialTool).setUnlocalizedName("WG_PrimordialSword");
		GameRegistry.registerItem(ItemPrimordialSword, ItemPrimordialSword.getUnlocalizedName());

		ItemPrimordialHelm = new ItemPrimordialArmor(primordialArmor, 4, 0).setUnlocalizedName("WG_PrimordialHelm");
		GameRegistry.registerItem(ItemPrimordialHelm, ItemPrimordialHelm.getUnlocalizedName());
		ItemPrimordialChest = new ItemPrimordialArmor(primordialArmor, 4, 1).setUnlocalizedName("WG_PrimordialChest");
		GameRegistry.registerItem(ItemPrimordialChest, ItemPrimordialChest.getUnlocalizedName());
		ItemPrimordialLegs = new ItemPrimordialArmor(primordialArmor, 4, 2).setUnlocalizedName("WG_PrimordialLegs");
		GameRegistry.registerItem(ItemPrimordialLegs, ItemPrimordialLegs.getUnlocalizedName());
		ItemPrimordialBoots = new ItemPrimordialArmor(primordialArmor, 4, 3).setUnlocalizedName("WG_PrimordialBoots");
		GameRegistry.registerItem(ItemPrimordialBoots, ItemPrimordialBoots.getUnlocalizedName());

		ItemCapsule = new ItemCrystalCapsule().setUnlocalizedName("WG_CrystalFlask");
		GameRegistry.registerItem(ItemCapsule, ItemCapsule.getUnlocalizedName());
		if(WGConfig.allowClusters)
		{
			ItemCluster = new ItemClusters().setUnlocalizedName("WG_Cluster");
			GameRegistry.registerItem(ItemCluster, ItemCluster.getUnlocalizedName());
		}
		//ItemMagicBed = new ItemMagicBed(WGConfig.ItemMagicBedID).setUnlocalizedName("WG_MagicBed");
		//GameRegistry.registerItem(ItemMagicBed, ItemMagicBed.getUnlocalizedName());
		//OreDictionary.registerOre("crystalNetherQuartz", new ItemStack(Items.quartz));
		OreDictionary.registerOre("scribingTools", new ItemStack(ConfigItems.itemInkwell,1,OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("scribingTools", new ItemStack(ItemAdvancedScribingTools,1,OreDictionary.WILDCARD_VALUE));
	}
	private static void initializeItems()
	{
		WGResearch.recipeList.put("THAUMIUMSHEARS", GameRegistry.addShapedRecipe(new ItemStack(ItemThaumiumShears), " t", "t ", 't', ItemApi.getItem("itemResource", 2)));

		BlockDispenser.dispenseBehaviorRegistry.putObject(ItemCapsule, new ItemCrystalCapsule.CapsuleDispenserBehaviour());	    

		GameRegistry.addRecipe(new RobeColourizationRecipe());
		GameRegistry.addRecipe(new CloakColourizationRecipe());
		GameRegistry.addRecipe(new BagColourizationRecipe());
		RecipeSorter.register("WitchingGadgets:advrobedye", RobeColourizationRecipe.class, RecipeSorter.Category.SHAPELESS, "after:forge:shapelessore");
		RecipeSorter.register("WitchingGadgets:cloakdye", CloakColourizationRecipe.class, RecipeSorter.Category.SHAPELESS, "after:forge:shapelessore");
		RecipeSorter.register("WitchingGadgets:bagdye", BagColourizationRecipe.class, RecipeSorter.Category.SHAPELESS, "after:forge:shapelessore");

		GameRegistry.addShapelessRecipe(new ItemStack(ItemMagicFoodstuffs,1,0), Items.nether_wart,Items.sugar);
		GameRegistry.addShapedRecipe(new ItemStack(ItemMagicFoodstuffs,1,1), "nnn","www", 'n',new ItemStack(ItemMagicFoodstuffs,1,0), 'w', Items.wheat);

		ItemAdvancedScribingTools = new ItemAdvancedScribingTools().setUnlocalizedName("WG_AdvancedScribingTools");
		GameRegistry.registerItem(ItemAdvancedScribingTools,ItemAdvancedScribingTools.getUnlocalizedName());

		EntityRegistry.registerModEntity(EntityItemReforming.class, "reformingItem", 0, WitchingGadgets.instance, 64, 1, true);

		if (!WGModCompat.loaded_TBases) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockMetalDevice, 1, 7), "vvv", "vvv", "vvv", 'v', "ingotVoid"));
			ItemStack voidIngot = OreDictionary.getOres("ingotVoid").get(0);
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(voidIngot.getItem(), 9, voidIngot.getItemDamage()), "blockVoid"));
		}

		if(WGConfig.allowClusters)
		{
			for(int iOre=0; iOre<ItemClusters.subNames.length; iOre++)
				OreDictionary.registerOre("cluster"+ItemClusters.subNames[iOre], new ItemStack(ItemCluster,1,iOre));
		}

		//FMLInterModComms.sendMessage("TravellersGear", "registerTravellersGear_0", new ItemStack(ItemCloak));
	}
	private static void postInitItems()
	{
		ChestGenHooks.getInfo("towerChestContents").addItem(new WeightedRandomChestContent(new ItemStack(ItemMaterial,1,8),1,1,8));
		ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(ItemMaterial,1,8),1,1,8));

		InfernalBlastfurnaceRecipe.tryAddIngotImprovement("Iron", "Steel", false);
		InfernalBlastfurnaceRecipe.tryAddSpecialOreMelting("Tungsten","Tungsten",true);
		InfernalBlastfurnaceRecipe.tryAddSpecialOreMelting("Rutile","Titanium",true);
	}


	private static void postInitThaumcraft()
	{
		//Add aspects where needed
		AspectList addAspects = new AspectList().add(Aspect.TREE, 4).add(Aspect.CLOTH, 2).add(Aspect.MECHANISM, 2).add(Aspect.AIR, 2);
		ThaumcraftApi.registerObjectTag(new ItemStack(BlockWoodenDevice,1,1),addAspects);
		addAspects = new AspectList().add(Aspect.TREE, 2).add(Aspect.CRYSTAL, 2).add(Aspect.CRAFT, 2);
		ThaumcraftApi.registerObjectTag(new ItemStack(BlockWoodenDevice,1,3),addAspects);

		addAspects = new AspectList().add(Aspect.MECHANISM,1).add(Aspect.EARTH,2);
		ThaumcraftApi.registerObjectTag(new ItemStack(BlockStoneDevice,1,0),addAspects);
		addAspects = new AspectList().add(Aspect.MECHANISM,3).add(Aspect.ELDRITCH,2);
		ThaumcraftApi.registerObjectTag(new ItemStack(BlockStoneDevice,1,1),addAspects);
		addAspects = new AspectList().add(Aspect.EARTH,1).add(Aspect.ELDRITCH,2).add(Aspect.DARKNESS,2);
		ThaumcraftApi.registerObjectTag(new ItemStack(BlockStoneDevice,1,2),addAspects);
		ThaumcraftApi.registerObjectTag(new ItemStack(BlockStoneDevice,1,3),addAspects);
		ThaumcraftApi.registerObjectTag(new ItemStack(BlockStoneDevice,1,4),addAspects);
		ThaumcraftApi.registerObjectTag(new ItemStack(BlockStoneDevice,1,5),addAspects);
		addAspects = new AspectList().add(Aspect.VOID,2).add(Aspect.ELDRITCH,1).add(Aspect.DARKNESS,2);
		ThaumcraftApi.registerObjectTag(new ItemStack(BlockStoneDevice,1,6),addAspects);

		addAspects = new AspectList().add(Aspect.PLANT,6).add(Aspect.ENTROPY,4).add(Aspect.MAGIC,4).add(Aspect.LIFE, 2);
		ThaumcraftApi.registerObjectTag(new ItemStack(BlockRoseVine,1, 32767),addAspects);

		addAspects = new AspectList().add(Aspect.CLOTH,1);
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemMaterial,1,0),addAspects);
		addAspects = new AspectList().add(Aspect.CLOTH,1).add(Aspect.GREED,1);
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemMaterial,1,1),addAspects);
		addAspects = new AspectList().add(Aspect.CLOTH,1).add(Aspect.METAL,1).add(Aspect.MAGIC,1);
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemMaterial,1,2),addAspects);

		addAspects = new AspectList().add(Aspect.CLOTH,3).add(Aspect.VOID,3);
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemMaterial,1,3),addAspects);
		addAspects = new AspectList().add(Aspect.CLOTH,3).add(Aspect.GREED,3);
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemMaterial,1,4),addAspects);
		addAspects = new AspectList().add(Aspect.CLOTH,3).add(Aspect.MAGIC,2).add(Aspect.TAINT,1);
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemMaterial,1,5),addAspects);

		addAspects = new AspectList().add(Aspect.CLOTH,2).add(Aspect.BEAST,3);
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemMaterial,1,6),addAspects);

		addAspects = new AspectList().add(Aspect.CRYSTAL,5).add(Aspect.SENSES,3).add(Aspect.EXCHANGE,2).add(Aspect.POISON,1);
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemMaterial,1,9),addAspects);
		addAspects = new AspectList().add(Aspect.AIR,2).add(Aspect.WATER,2).add(Aspect.ORDER,2).add(Aspect.SENSES,2).add(Aspect.MIND,2);
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemMaterial,1,10),addAspects);

		addAspects = new AspectList().add(Aspect.CRYSTAL,4).add(Aspect.VOID,4);
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemMaterial,1,12),addAspects);

		addAspects = new AspectList().add(Aspect.FIRE,1).add(Aspect.HUNGER,1);
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemMagicFoodstuffs,1,0),addAspects);
		addAspects = new AspectList().add(Aspect.MIND,3).add(Aspect.HUNGER,2).add(Aspect.FLESH,2);
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemMagicFoodstuffs,1,2),addAspects);

		addAspects = new AspectList().add(Aspect.SENSES,2).add(Aspect.MAN,1);
		ThaumcraftApi.registerObjectTag(new ItemStack(ItemMagicalBaubles,1,4),addAspects);

		//Metallurgy
		addOreAspects("Manasteel", new AspectList().add(Aspect.MAGIC, 1), false);
		addOreAspects("Terrasteel", new AspectList().add(Aspect.EARTH, 1).add(Aspect.MAGIC, 1), false);
		addOreAspects("ElvenElementium", new AspectList().add(Aspect.AURA, 1).add(Aspect.MAGIC, 2), true);
		addOreAspects("Aluminum", new AspectList().add(Aspect.AIR, 1), false);
		addOreAspects("Aluminium", new AspectList().add(Aspect.AIR, 1), false);
		addOreAspects("Nickel", new AspectList().add(Aspect.ENTROPY, 1), false);
		addOreAspects("Zinc", new AspectList().add(Aspect.ORDER, 1), false);
		addOreAspects("Brass", new AspectList().add(Aspect.CRAFT, 1), false);
		addOreAspects("Electrum", new AspectList().add(Aspect.ENERGY, 1), false);
		addOreAspects("Steel", new AspectList().add(Aspect.CRYSTAL, 2), false);
		addOreAspects("Constantan", new AspectList().add(Aspect.ARMOR, 1), false);

		addAspects = new AspectList().add(Aspect.MOTION, 4).add(Aspect.SOUL, 2);
		ThaumcraftApi.registerEntityTag("scarecrow", addAspects);

		WGResearch.setupResearchPages();
		WGResearch.registerRecipes();
		WGResearch.registerResearch();
		WGResearch.modifyStandardThaumcraftResearch();
	}

	// Check for ore dictionary entries and add the missing metal aspects
	static void addOreAspects(String ore, AspectList aspects, boolean isRareOre)
	{
		if(!OreDictionary.getOres("ore"+ore).isEmpty() && !oreHasAspects("ore"+ore))
		{
			AspectList al = new AspectList().add(Aspect.METAL, Math.max((isRareOre?3:2), (isRareOre?4:3)-aspects.visSize())).add(Aspect.EARTH, 1);
			for(Aspect aa : aspects.getAspects())
				al.merge(aa, 1);

			ThaumcraftApi.registerObjectTag("ore"+ore, al);
		}
		if(!OreDictionary.getOres("ingot"+ore).isEmpty() && !oreHasAspects("ingot"+ore))
		{
			AspectList al = new AspectList().add(Aspect.METAL, Math.max((isRareOre?3:2), (isRareOre?5:4)-aspects.visSize()));
			for(Aspect aa : aspects.getAspects())
				al.merge(aa, aspects.getAmount(aa));
			ThaumcraftApi.registerObjectTag("ingot"+ore, al);
		}
		if(!OreDictionary.getOres("nugget"+ore).isEmpty() && !oreHasAspects("nugget"+ore))
			ThaumcraftApi.registerObjectTag("nugget"+ore, new AspectList().add(Aspect.METAL, 1));
		if(!OreDictionary.getOres("dust"+ore).isEmpty() && !oreHasAspects("dust"+ore))
		{
			AspectList al = new AspectList().add(Aspect.METAL, Math.max((isRareOre?3:2), (isRareOre?4:3)-aspects.visSize())).add(Aspect.ENTROPY, 1);
			for(Aspect aa : aspects.getAspects())
				al.merge(aa, 1);
			ThaumcraftApi.registerObjectTag("dust"+ore, al);
		}
		if(!OreDictionary.getOres("block"+ore).isEmpty() && !oreHasAspects("block"+ore))
		{
			AspectList al = new AspectList().add(Aspect.METAL, Math.max((isRareOre?6:5), (isRareOre?7:6)-aspects.visSize())).add(Aspect.ENTROPY, 1);
			for(Aspect aa : aspects.getAspects())
				al.merge(aa, 1);
			ThaumcraftApi.registerObjectTag("block"+ore, al);
		}
	}
	static boolean oreHasAspects(String ore)
	{
		for(ItemStack stack : OreDictionary.getOres(ore))
			if(stack!=null)
				return ThaumcraftApi.objectTags.get(Arrays.asList(new Object[] { stack.getItem(), Integer.valueOf(stack.getItemDamage()) }))!=null;
		return false;
	}
}