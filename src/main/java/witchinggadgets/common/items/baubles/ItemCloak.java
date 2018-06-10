package witchinggadgets.common.items.baubles;

import java.util.List;
import java.util.UUID;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.items.armor.Hover;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.client.ClientUtilities;
import witchinggadgets.client.render.ModelCloak;
import witchinggadgets.common.WGModCompat;
import witchinggadgets.common.util.Lib;
import witchinggadgets.common.util.Utilities;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Optional.Interface(iface = "vazkii.botania.api.item.ICosmeticAttachable", modid = "Botania")
public class ItemCloak extends Item implements IBauble, vazkii.botania.api.item.ICosmeticAttachable {
	public static String[] subNames = {"standard", "spectral", "wolf", "raven"};
	IIcon iconRaven;
	IIcon iconWolf;

	public ItemCloak() {
		this.setHasSubtypes(true);
		this.setCreativeTab(WitchingGadgets.tabWG);
	}

	@Override
	public boolean isItemTool(ItemStack stack) {
		return stack.stackSize == 1;
	}

	@Override
	public void registerIcons(IIconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon("witchinggadgets:cloak");
		this.iconRaven = iconRegister.registerIcon("witchinggadgets:cloak_raven");
		this.iconWolf = iconRegister.registerIcon("witchinggadgets:cloak_wolf");
	}

	@Override
	public IIcon getIconFromDamage(int meta) {
		if (meta == 2)
			return this.iconWolf;
		if (meta == 3)
			return this.iconRaven;
		return this.itemIcon;
	}

	public boolean hasColor(ItemStack stack) {
		return true;
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int pass) {
		return getColor(stack);
	}

	public int getColor(ItemStack stack) {
		if (stack == null)
			return 0xffffff;
		int meta = stack.getItemDamage();
		if (meta == 0) {
			NBTTagCompound tag = stack.getTagCompound();
			if (tag == null)
				return ClientUtilities.colour_CloakBlue;
			NBTTagCompound tagDisplay = tag.getCompoundTag("display");
			return tagDisplay == null ? ClientUtilities.colour_CloakBlue : (tagDisplay.hasKey("color") ? tagDisplay.getInteger("color") : ClientUtilities.colour_CloakBlue);
		}
		return meta == 1 ? Aspect.DARKNESS.getColor() :  0xffffff;
	}

	public void removeColor(ItemStack stack) {
		if (stack == null)
			return;
		NBTTagCompound tag = stack.getTagCompound();
		if (tag != null) {
			NBTTagCompound tagDisplay = tag.getCompoundTag("display");

			if (tagDisplay.hasKey("color")) {
				tagDisplay.removeTag("color");
			}
		}
	}

	public void setColour(ItemStack stack, int colour) {
		NBTTagCompound nbttagcompound = stack.getTagCompound();

		if (nbttagcompound == null) {
			nbttagcompound = new NBTTagCompound();
			stack.setTagCompound(nbttagcompound);
		}

		NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

		if (!nbttagcompound.hasKey("display")) {
			nbttagcompound.setTag("display", nbttagcompound1);
		}

		nbttagcompound1.setInteger("color", colour);
	}

	@Override
	public String getArmorTexture(ItemStack itemstack, Entity entity, int slot, String layer) {
		if (itemstack.getItemDamage() < subNames.length)
			if (subNames[itemstack.getItemDamage()].equals("wolf"))
				return "witchinggadgets:textures/models/cloakWolf.png";
			else if (subNames[itemstack.getItemDamage()].equals("raven"))
				return "witchinggadgets:textures/models/cloakRaven.png";
		return "witchinggadgets:textures/models/cloak.png";
	}

	@Override
	public int getMaxDamage(ItemStack stack) {
		return 0;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
		return new ModelCloak(getColor(itemStack));
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return getUnlocalizedName() + "." + subNames[stack.getItemDamage()];
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List itemList) {
		for (int i = 0; i < subNames.length; i++)
			if (i != 3 || WGModCompat.loaded_Twilight)
				itemList.add(new ItemStack(item, 1, i));
	}

	public ItemStack[] getStoredItems(ItemStack item) {
		ItemStack[] stackList = new ItemStack[27];

		if (item.hasTagCompound()) {
			NBTTagList inv = item.getTagCompound().getTagList("InternalInventory", 10);

			for (int i = 0; i < inv.tagCount(); i++) {
				NBTTagCompound tag = inv.getCompoundTagAt(i);
				int slot = tag.getByte("Slot") & 0xFF;

				if ((slot >= 0) && (slot < stackList.length)) {
					stackList[slot] = ItemStack.loadItemStackFromNBT(tag);
				}
			}
		}
		return stackList;
	}

	public void setStoredItems(ItemStack item, ItemStack[] stackList) {
		NBTTagList inv = new NBTTagList();

		for (int i = 0; i < stackList.length; i++) {
			if (stackList[i] != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				stackList[i].writeToNBT(tag);
				inv.appendTag(tag);
			}
		}
		if (!item.hasTagCompound()) {
			item.setTagCompound(new NBTTagCompound());
		}
		item.getTagCompound().setTag("InternalInventory", inv);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer par2EntityPlayer, List list, boolean par4) {
		String type = "bauble." + getBaubleType(stack);
		list.add(StatCollector.translateToLocalFormatted(Lib.DESCRIPTION + "gearSlot." + type));

		//if (stack.hasTagCompound() && stack.getTagCompound().getBoolean("noGlide"))
		//	list.add(StatCollector.translateToLocal(Lib.DESCRIPTION + "noGlide"));

		if (Loader.isModLoaded("Botania")) {
			ItemStack cosmetic = getCosmeticItem(stack);
			if (cosmetic != null)
				list.add(String.format(StatCollector.translateToLocal("botaniamisc.hasCosmetic"), cosmetic.getDisplayName()).replaceAll("&", "\u00a7"));
		}
	}

	@Override
	public boolean canEquip(ItemStack stack, EntityLivingBase living) {
		return true;
	}

	@Override
	public boolean canUnequip(ItemStack stack, EntityLivingBase living) {
		return !subNames[stack.getItemDamage()].contains("binding");
	}

	@Override
	public BaubleType getBaubleType(ItemStack stack) {
		return BaubleType.AMULET;
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase living) {
		onItemTicked((EntityPlayer) living, stack);
	}

	@Override
	public void onEquipped(ItemStack stack, EntityLivingBase living) {
		onItemEquipped((EntityPlayer) living, stack);
	}

	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase living) {
		onItemUnequipped((EntityPlayer) living, stack);
	}


	public void onItemTicked(EntityPlayer player, ItemStack stack) {
		if (player.ticksExisted < 1) {
			onItemUnequipped(player, stack);
			onItemEquipped(player, stack);
		}

		if (stack.getItemDamage() < subNames.length) {
			if (subNames[stack.getItemDamage()].equals("raven")) {
				if (!player.onGround) {
					if (player.capabilities.isFlying || Hover.getHover(player.getEntityId())) {
						if (player.moveForward > 0)
							player.moveFlying(0, 1, .01f);
						player.motionY *= .75;
					} else if (player.motionY < 0) {
						float mod = player.isSneaking() ? .05f : .01f;
						player.motionY *= player.isSneaking() ? .75 : .5;
						double x = Math.cos(Math.toRadians(player.rotationYawHead + 90)) * mod;
						double z = Math.sin(Math.toRadians(player.rotationYawHead + 90)) * mod;
						player.motionX += x;
						player.motionZ += z;
					}
					player.fallDistance = 0f;
				}

			}

			if (subNames[stack.getItemDamage()].equals("wolf") && stack.hasTagCompound() && stack.getTagCompound().hasKey("wolfPotion")) {
				int amp = stack.getTagCompound().getInteger("wolfPotion");
				player.addPotionEffect(new PotionEffect(Potion.damageBoost.id, 60, amp));
				player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 60, amp));
				player.addPotionEffect(new PotionEffect(Potion.resistance.id, 60, amp));
				stack.getTagCompound().removeTag("wolfPotion");
				if (stack.getTagCompound().hasNoTags())
					stack.setTagCompound(null);
			}
		}
	}

	public void onItemEquipped(EntityPlayer player, ItemStack stack) {
	}

	public void onItemUnequipped(EntityPlayer player, ItemStack stack) {
	}


	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

		/* if (!world.isRemote) {
			if(subNames[stack.getItemDamage()].equals("storage") && !player.worldObj.isRemote)
					player.openGui(WitchingGadgets.instance, 4, player.worldObj, MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ));
		} */

		return stack;
	}

	@Optional.Method(modid = "Botania")
	public ItemStack getCosmeticItem(ItemStack stack)
	{
		if(!stack.hasTagCompound())
			return null;
		ItemStack cosmetic = ItemStack.loadItemStackFromNBT(stack.getTagCompound().getCompoundTag("botaniaCosmeticOverride"));
		return cosmetic;
	}

	@Optional.Method(modid = "Botania")
	public void setCosmeticItem(ItemStack stack, ItemStack cosmetic)
	{
		if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		NBTTagCompound cosTag = cosmetic.writeToNBT(new NBTTagCompound());
		stack.getTagCompound().setTag("botaniaCosmeticOverride",cosTag);
	}
}