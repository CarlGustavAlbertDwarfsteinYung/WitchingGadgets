package witchinggadgets.common.items.baubles;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.RenderPlayerEvent;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.item.IBaubleRender;
import witchinggadgets.client.render.ModelKama;
import witchinggadgets.common.util.Lib;
import baubles.api.BaubleType;
import baubles.api.IBauble;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemKama extends ItemCloak implements IBauble, IBaubleRender
{
	IIcon overlay;

	int colour;
	static ResourceLocation texBelt = new ResourceLocation("witchinggadgets:textures/models/magicalBaubles.png");

	public ItemKama()
	{
		super();
	}

	@Override
	public void registerIcons(IIconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon("witchinggadgets:kama");
		this.iconRaven = iconRegister.registerIcon("witchinggadgets:kama_raven");
		this.iconWolf = iconRegister.registerIcon("witchinggadgets:kama_wolf");
		this.overlay = iconRegister.registerIcon("witchinggadgets:kama_overlay");
	}
	@Override
	public IIcon getIconFromDamageForRenderPass(int meta, int pass)
	{
		if(pass==1)
			return overlay;
		return super.getIconFromDamageForRenderPass(meta, pass);
	}
	@Override
	public IIcon getIcon(ItemStack stack, int pass)
	{
		return getIconFromDamageForRenderPass(stack.getItemDamage(), pass);
	}
	@Override
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}
	@Override
	public int getColorFromItemStack(ItemStack stack, int pass)
	{
		if(pass==1)
			return 0xffffff;
		return super.getColorFromItemStack(stack, pass);
	}
	   
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot)
	{
		return new ModelKama(getColor(itemStack));
	}

	@Override
	public BaubleType getBaubleType(ItemStack stack)
	{
		return BaubleType.BELT;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer par2EntityPlayer, List list, boolean par4)
	{
		String type = "bauble."+getBaubleType(stack);
		list.add(StatCollector.translateToLocalFormatted(Lib.DESCRIPTION+"gearSlot."+type));

		//if(stack.hasTagCompound() && stack.getTagCompound().getBoolean("noGlide"))
	//		list.add(StatCollector.translateToLocal(Lib.DESCRIPTION+"noGlide"));

		if(Loader.isModLoaded("Botania"))
		{
			ItemStack cosmetic = getCosmeticItem(stack);
			if(cosmetic != null)
				list.add( String.format(StatCollector.translateToLocal("botaniamisc.hasCosmetic"), cosmetic.getDisplayName()).replaceAll("&","\u00a7") );
		}
	}
	
	@Override
	public void onEquipped(ItemStack stack, EntityLivingBase living)
	{
	}

	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase living)
	{
		if(stack.hasTagCompound() && stack.getTagCompound().getBoolean("isSpectral"))
			stack.getTagCompound().setBoolean("isSpectral",false);
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase living)
	{
		if(living instanceof EntityPlayer)
			super.onItemTicked((EntityPlayer) living, stack);
	}

	@Override
	public boolean canEquip(ItemStack arg0, EntityLivingBase arg1)
	{
		return true;
	}

	@Override
	public boolean canUnequip(ItemStack arg0, EntityLivingBase arg1)
	{
		return true;
	}

	@SideOnly(Side.CLIENT)
	public void onPlayerBaubleRender(ItemStack itemStack, RenderPlayerEvent renderPlayerEvent, IBaubleRender.RenderType renderType) {
		if(renderType == IBaubleRender.RenderType.BODY) {

			GL11.glPushMatrix();
			GL11.glEnable(3042);
			GL11.glBlendFunc(770, 771);

			Tessellator tessellator = Tessellator.instance;

			GL11.glTranslatef(0, 1.45f, 0);
			GL11.glScalef(1, -1, 1);
			GL11.glColor3f((colour>>16&255)/255.0f, (colour>>8&255)/255.0f, (colour&255)/255.0f);

			Minecraft.getMinecraft().getTextureManager().bindTexture(texture);

			float angleMax = renderPlayerEvent.entityPlayer.isSprinting()?60:70;

			double ap = .8125; //attachment point
			double hL = .0; //seam-height left
			double hR = .0; //seam-height right
			double dL = .250; //seam-offset left
			double dR = .250; //seam-offset right
			double h = .5625; //height
			double w = .038; //offset from body

			tessellator.startDrawingQuads();
			tessellator.setNormal(1,0,0);
			tessellator.addVertexWithUV( .43750, ap-h+.1,-.125, 0, 1);
			tessellator.addVertexWithUV( .25000+w, ap,-.125, 0, 0);
			tessellator.addVertexWithUV( .25000+w, ap, .125+w, .25, 0);
			tessellator.addVertexWithUV( .3750, ap-h+hL, dL, .25, 1);
			tessellator.draw();

			tessellator.startDrawing(9);
			tessellator.setNormal(0,0,1);
			tessellator.addVertexWithUV( .3750, ap-h+hL, dL, .25, 1);
			tessellator.addVertexWithUV(-.0, ap-h-.1+hL, dL, .515625, 1);
			tessellator.addVertexWithUV(-.03125, ap, .125+w, .515625, 0);
			tessellator.addVertexWithUV( .25000+w, ap, .125+w, .25, 0);
			tessellator.draw();

			tessellator.startDrawingQuads();
			tessellator.setNormal(0,0,1);
			tessellator.addVertexWithUV(-.0376, ap-h+hR, dR, .515625, 1);
			tessellator.addVertexWithUV(-.0376, ap, .125+w, .515625, 0);
			tessellator.addVertexWithUV(-.0376, ap, .125+w, .484375, 0);
			tessellator.addVertexWithUV(-.0376, ap-h+hL, dL, .484375, 1);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0,0,1);
			tessellator.addVertexWithUV( .0376, ap-h+hL, dL, .515625, 1);
			tessellator.addVertexWithUV( .0376, ap, .125+w, .515625, 0);
			tessellator.addVertexWithUV( .0376, ap, .125+w, .484375, 0);
			tessellator.addVertexWithUV( .0376, ap-h+hL, dL, .484375, 1);
			tessellator.draw();

			tessellator.startDrawing(9);
			tessellator.setNormal(0,0,1);
			tessellator.addVertexWithUV(-.3750, ap-h+hR, dR, .75, 1);
			tessellator.addVertexWithUV(-.25000-w, ap, .125+w, .75, 0);
			tessellator.addVertexWithUV( .03125, ap, .125+w, .484375, 0);
			tessellator.addVertexWithUV( .0, ap-h-.1+hR, dR, .484375, 1);
			tessellator.draw();

			tessellator.startDrawingQuads();
			tessellator.setNormal(-1,0,0);
			tessellator.addVertexWithUV(-.3750, ap-h+hR, dR, .75, 1);
			tessellator.addVertexWithUV(-.25000-w, ap, .125+w, .75, 0);
			tessellator.addVertexWithUV(-.25000-w, ap,-.125, 1, 0);
			tessellator.addVertexWithUV(-.43750, ap-h+.1,-.125, 1, 1);
			tessellator.draw();

			GL11.glColor3f(1,1,1);
			Minecraft.getMinecraft().getTextureManager().bindTexture(texBelt);
			tessellator.startDrawingQuads();
			tessellator.setNormal(-1,0,0);
			tessellator.addVertexWithUV(-.25-w, ap-.125, .125+w, .0, 1);
			tessellator.addVertexWithUV(-.25-w, ap+.125, .125+w, .0, .875);
			tessellator.addVertexWithUV(-.25-w, ap+.125,-.125-w, .0625, .875);
			tessellator.addVertexWithUV(-.25-w, ap-.125,-.125-w, .0625, 1);
			tessellator.draw();

			tessellator.startDrawingQuads();
			tessellator.setNormal(0,0,-1);
			tessellator.addVertexWithUV(-.25-w, ap-.125,-.125-w, .0625, 1);
			tessellator.addVertexWithUV(-.25-w, ap+.125,-.125-w, .0625, .875);
			tessellator.addVertexWithUV( .25+w, ap+.125,-.125-w, .1875, .875);
			tessellator.addVertexWithUV( .25+w, ap-.125,-.125-w, .1875, 1);
			tessellator.draw();

			tessellator.startDrawingQuads();
			tessellator.setNormal(1,0,0);
			tessellator.addVertexWithUV(.25+w, ap-.125,-.125-w, .1875, 1);
			tessellator.addVertexWithUV(.25+w, ap+.125,-.125-w, .1875, .875);
			tessellator.addVertexWithUV(.25+w, ap+.125, .125+w, .25, .875);
			tessellator.addVertexWithUV(.25+w, ap-.125, .125+w, .25, 1);
			tessellator.draw();

			tessellator.startDrawingQuads();
			tessellator.setNormal(0,0,-1);
			tessellator.addVertexWithUV( .25+w, ap-.125, .125+w, .25, 1);
			tessellator.addVertexWithUV( .25+w, ap+.125, .125+w, .25, .875);
			tessellator.addVertexWithUV(-.25-w, ap+.125, .125+w, .375, .875);
			tessellator.addVertexWithUV(-.25-w, ap-.125, .125+w, .375, 1);
			tessellator.draw();

			GL11.glDisable(3042);
			GL11.glPopMatrix();
		}
	}
}