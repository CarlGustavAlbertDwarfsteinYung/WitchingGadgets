package witchinggadgets.common.items.baubles;

import java.util.List;
import java.util.UUID;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderPlayerEvent;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.items.armor.Hover;
import vazkii.botania.api.item.IBaubleRender;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.client.ClientUtilities;
import witchinggadgets.client.render.ModelCloak;
import witchinggadgets.common.WGConfig;
import witchinggadgets.common.WGModCompat;
import witchinggadgets.common.util.Lib;
import witchinggadgets.common.util.Utilities;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Optional.Interface(iface = "vazkii.botania.api.item.ICosmeticAttachable", modid = "Botania")
public class ItemCloak extends Item implements IBauble, vazkii.botania.api.item.ICosmeticAttachable {
	public static String[] subNames = {"standard", "spectral", "wolf", "raven", "storage"};
	IIcon iconRaven;
	IIcon iconWolf;

	private double[] circPos = new double[32]; // Circle Position
	int colour;
	private ResourceLocation texture;

	public ItemCloak() {
		this.setHasSubtypes(true);
		this.setCreativeTab(WitchingGadgets.tabWG);
		circPos[0] = 0.5;
		circPos[1] = 0.49039;
		circPos[2] = 0.46194;
		circPos[3] = 0.41573;
		circPos[4] = 0.35355;
		circPos[5] = 0.27779;
		circPos[6] = 0.19134;
		circPos[7] = 0.09755;
		circPos[8] = 0.0;
		circPos[9] = -0.09755;
		circPos[10] = -0.19134;
		circPos[11] = -0.27779;
		circPos[12] = -0.35355;
		circPos[13] = -0.41573;
		circPos[14] = -0.46194;
		circPos[15] = -0.49039;
		circPos[16] = -0.5;
		circPos[17] = -0.49039;
		circPos[18] = -0.46194;
		circPos[19] = -0.41573;
		circPos[20] = -0.35355;
		circPos[21] = -0.27779;
		circPos[22] = -0.19134;
		circPos[23] = -0.09755;
		circPos[24] = 0.0;
		circPos[25] = 0.09755;
		circPos[26] = 0.19134;
		circPos[27] = 0.27779;
		circPos[28] = 0.35355;
		circPos[29] = 0.41573;
		circPos[30] = 0.46194;
		circPos[31] = 0.49039;
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

	public String getArmorTexture(ItemStack itemstack) {
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

	@Override
	public void addInformation(ItemStack stack, EntityPlayer par2EntityPlayer, List list, boolean par4) {
		String type = "bauble." + getBaubleType(stack);
		list.add(StatCollector.translateToLocalFormatted(Lib.DESCRIPTION + "gearSlot." + type));

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
		texture = new ResourceLocation(getArmorTexture(stack));
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
		this.colour = getColor(stack);
	}

	public void onItemUnequipped(EntityPlayer player, ItemStack stack) {
	}


	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

		if (!world.isRemote) {
			if (subNames[stack.getItemDamage()].equals("storage") && !player.worldObj.isRemote) {
				player.openGui(WitchingGadgets.instance, 5, player.worldObj, MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ));
			}
		}

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

	@SideOnly(Side.CLIENT)
	public void onPlayerBaubleRender(ItemStack itemStack, RenderPlayerEvent renderPlayerEvent, IBaubleRender.RenderType renderType) {
		if(renderType == IBaubleRender.RenderType.BODY) {
			Minecraft.getMinecraft().renderEngine.bindTexture(texture);

			GL11.glPushMatrix();
			GL11.glEnable(3042);
			GL11.glBlendFunc(770, 771);

			Tessellator tessellator = Tessellator.instance;

			GL11.glTranslatef(0, 1.45f, 0);
			GL11.glScalef(1, -1, 1);
			GL11.glColor3f((colour>>16&255)/255.0f, (colour>>8&255)/255.0f, (colour&255)/255.0f);

			double d0_1 = circPos[0]*1;
			double d1_1 = circPos[1]*1;
			double d2_1 = circPos[24]*1;
			double d3_1 = circPos[25]*1;

			for(int i=0;i<15;i++)
			{
				int it0 = i;
				int it1 = it0+1;
				if(it1 > 31)it1-=31;
				int it2 = i+24;
				if(it2 > 31)it2-=31;
				int it3 = it2+1;
				if(it3 > 31)it3-=31;


				for(int j=0; j < 8;j++)
				{
					int jt0 = j;
					int jt1 = jt0+1;
					double h0 = (circPos[jt0]*circPos[jt0])*7;
					double h1 = (circPos[jt1]*circPos[jt1])*7;
					double dividerA[] = {0.3,0.725,0.75,0.8,0.825,0.9,1.0,1.1};
					//double dividerA[] = {1.1,1.0,0.9,0.825,0.8,0.75,0.725,0.3};
					double divider = dividerA[j];

					double d0 = circPos[it0]*1.5*divider;
					double d1 = circPos[it1]*1.5*divider;
					double d2 = circPos[it2]*1.5*divider;
					double d3 = circPos[it3]*1.5*divider;


					double f3 = i*0.0625;//icon.getMinU();
					double f4 = (i+1)*0.0625;//icon.getMaxU();
					double f5 = j * 0.125;//1 - (j+1)*0.125;//icon.getMinV();
					double f6 = (j+1)*0.125;//1 - j*0.125;//icon.getMaxV();

					if(j==2)h0*=0.975;
					if(j==1)
					{
						h1*=0.975;
						h0*=0.9;
					}
					if(j==0)
					{
						d0 *=0.0;
						d0_1 *=0.0;
						d1 *=0.0;
						d1_1 *=0.0;
						d2 *=0.0;
						d2_1 *=0.0;
						d3 *=0.0;
						d3_1 *=0.0;
						h1*=0.9;
						h0*=0.9;
					}

					tessellator.startDrawingQuads();
					tessellator.setNormal(0.0F, 1.0F, 0.0F);
					tessellator.addVertexWithUV(d0_1, h0, d2_1, f3, f5);
					tessellator.addVertexWithUV(d0  , h1, d2  , f3, f6);
					tessellator.addVertexWithUV(d1  , h1, d3  , f4, f6);
					tessellator.addVertexWithUV(d1_1, h0, d3_1, f4, f5);
					tessellator.draw();

					d0_1 = d0;
					d1_1 = d1;
					d2_1 = d2;
					d3_1 = d3;
				}
			}

			GL11.glColor3f(1,1,1);
			GL11.glDisable(3042);
			//GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glPopMatrix();
		}
	}
}