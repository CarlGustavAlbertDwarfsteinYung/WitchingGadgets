package witchinggadgets.common.util;

import baubles.api.BaublesApi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.ForgeHooks;
// import travellersgear.api.TravellersGearAPI;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.common.WGConfig;
import witchinggadgets.common.WGContent;
import witchinggadgets.common.items.baubles.ItemMagicalBaubles;
import witchinggadgets.common.items.tools.ItemPrimordialGlove;
import witchinggadgets.common.util.network.message.MessagePrimordialGlove;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WGKeyHandler
{
	public static KeyBinding thaumcraftFKey;
	public static KeyBinding jumpKey;

	public boolean[] keyDown = {false,false,false};
	public static float gemRadial;
	public static boolean gemLock = false;
	private int multiJumps=0;

	public WGKeyHandler()
	{
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void playerTick(TickEvent.PlayerTickEvent event)
	{
		if (event.side == Side.SERVER)
			return;
		if (event.phase == TickEvent.Phase.START)
		{
			if(thaumcraftFKey==null)
			{
				for(KeyBinding kb : Minecraft.getMinecraft().gameSettings.keyBindings)
					if(kb.getKeyCategory()=="key.categories.misc" && kb.getKeyDescription()=="Change Wand Focus")
						thaumcraftFKey=kb;
			}
			if(jumpKey==null)
				jumpKey=Minecraft.getMinecraft().gameSettings.keyBindJump;

			EntityPlayer player = event.player;
			if(FMLClientHandler.instance().getClient().inGameHasFocus)
			{
				if (jumpKey.getIsKeyPressed() && event.player.onGround && Minecraft.getMinecraft().currentScreen==null)
				{
					multiJumps = 0;
				} else if (!jumpKey.getIsKeyPressed() && multiJumps == 0)
				{
						IInventory baubles = BaublesApi.getBaubles(event.player);
						if ((baubles.getStackInSlot(1) != null && baubles.getStackInSlot(1).getItem() instanceof ItemMagicalBaubles &&  baubles.getStackInSlot(1).getItemDamage()==0) || (baubles.getStackInSlot(2) != null && baubles.getStackInSlot(2).getItem() instanceof ItemMagicalBaubles &&  baubles.getStackInSlot(2).getItemDamage()==0))
							multiJumps = 1;
				} else if (jumpKey.getIsKeyPressed() && Minecraft.getMinecraft().currentScreen==null)
				{
					if (event.player.isAirBorne && multiJumps == 1)
					{
						event.player.motionY = 0.42D;
						event.player.fallDistance = 0;

						if (event.player.isPotionActive(Potion.jump))
							event.player.motionY += (double) ((float) (event.player.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);

						event.player.motionY *= 1.25f;
						ForgeHooks.onLivingJump(event.player);
						multiJumps = 2;
					}
				}

				/* if(jumpKey.getIsKeyPressed() && !keyDown[2] && Minecraft.getMinecraft().currentScreen==null)
				{
					if(isJumping && multiJumps>0)
					{
						event.player.motionY = 0.42D;
						event.player.fallDistance = 0;

						if (event.player.isPotionActive(Potion.jump))
							event.player.motionY += (double) ((float) (event.player.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
						event.player.motionY *= 1.2f;
						ForgeHooks.onLivingJump(event.player);
						multiJumps--;
					}
					if(!isJumping)
					{
						multiJumps = 0;
						isJumping = event.player.isAirBorne;

						IInventory baubles = BaublesApi.getBaubles(event.player);

						if ((baubles.getStackInSlot(1) != null && baubles.getStackInSlot(1).getItem() instanceof ItemMagicalBaubles &&  baubles.getStackInSlot(1).getItemDamage()==0) || (baubles.getStackInSlot(2) != null && baubles.getStackInSlot(2).getItem() instanceof ItemMagicalBaubles &&  baubles.getStackInSlot(2).getItemDamage()==0))
							multiJumps += 1;
					}
					keyDown[2] = true;
				}
				else if(keyDown[2])
					keyDown[2] = false; */
			}
			float step = WGConfig.radialSpeed;
			if(thaumcraftFKey!=null && thaumcraftFKey.getIsKeyPressed() && !keyDown[1])
			{
				if(player.isSneaking() && player.getCurrentEquippedItem()!=null && player.getCurrentEquippedItem().getItem() instanceof ItemPrimordialGlove)
					WitchingGadgets.packetHandler.sendToServer(new MessagePrimordialGlove(player, (byte)1, 0));
				else
					if(gemLock)
					{
						gemLock=false;
						keyDown[1] = true;
					}
					else if(FMLClientHandler.instance().getClient().inGameHasFocus && player.getCurrentEquippedItem()!=null && player.getCurrentEquippedItem().getItem() instanceof ItemPrimordialGlove)
					{
						if(gemRadial<1)
							gemRadial += step;
						if(gemRadial>1)
							gemRadial=1f;
						if(gemRadial>=1)	
						{
							gemLock=true;
							keyDown[1] = true;
						}
					}
			}
			else
			{
				if(keyDown[1] && !thaumcraftFKey.getIsKeyPressed())
					keyDown[1]=false;
				if(!gemLock)
				{
					if(gemRadial>0)
						gemRadial -= step;
					if(gemRadial<0)
						gemRadial=0f;
				}

			}
		}
		/* if(isJumping && event.player.onGround)
		{
			event.player.isAirBorne = false;
			isJumping=false;
		} */
	}

}