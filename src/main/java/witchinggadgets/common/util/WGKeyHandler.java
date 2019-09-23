package witchinggadgets.common.util;

import baubles.api.BaublesApi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.ForgeHooks;
import witchinggadgets.WitchingGadgets;
import witchinggadgets.common.WGConfig;
import witchinggadgets.common.gui.ContainerCloak;
import witchinggadgets.common.items.baubles.ItemMagicalBaubles;
import witchinggadgets.common.items.tools.ItemPrimordialGlove;
import witchinggadgets.common.util.network.message.MessagePrimordialGlove;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WGKeyHandler {

	public static KeyBinding thaumcraftFKey;
	public static KeyBinding thaumcraftBKey;
	public static KeyBinding jumpKey;
	boolean keyDown = false;

	public static float gemRadial;
	public static boolean gemLock = false;
	private int multiJumps=0;

	public WGKeyHandler()
	{
		for(KeyBinding kb : Minecraft.getMinecraft().gameSettings.keyBindings) {
			if (kb.getKeyCategory() == "key.categories.misc" && kb.getKeyDescription() == "Change Wand Focus")
				thaumcraftFKey = kb;
			if (kb.getKeyCategory() == "key.categories.inventory" && kb.getKeyDescription() == "Baubles Inventory")
				thaumcraftBKey = kb;
		}

		jumpKey=Minecraft.getMinecraft().gameSettings.keyBindJump;
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void playerTick(TickEvent.PlayerTickEvent event) {
		if (event.side == Side.SERVER)
			return;
		if (event.phase == TickEvent.Phase.START) {
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;

			if (FMLClientHandler.instance().getClient().inGameHasFocus) {
				if (jumpKey.getIsKeyPressed() && player.onGround && Minecraft.getMinecraft().currentScreen == null) {
					multiJumps = 0;
				} else if (!jumpKey.getIsKeyPressed() && multiJumps == 0) {
					IInventory baubles = BaublesApi.getBaubles(player);
					if ((baubles.getStackInSlot(1) != null && baubles.getStackInSlot(1).getItem() instanceof ItemMagicalBaubles && baubles.getStackInSlot(1).getItemDamage() == 0) || (baubles.getStackInSlot(2) != null && baubles.getStackInSlot(2).getItem() instanceof ItemMagicalBaubles && baubles.getStackInSlot(2).getItemDamage() == 0))
						multiJumps = 1;
				} else if (jumpKey.getIsKeyPressed() && Minecraft.getMinecraft().currentScreen == null) {
					if (player.isAirBorne && multiJumps == 1) {
						player.motionY = 0.42D;
						player.fallDistance = 0;

						if (player.isPotionActive(Potion.jump))
							player.motionY += (float) (player.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F;

						player.motionY *= 1.25f;
						ForgeHooks.onLivingJump(player);
						player.fallDistance = 0; //rest fall distance on second jump
						multiJumps = 2;
					}
				}
			}

			if(thaumcraftFKey.getIsKeyPressed()) {
				if (player.getCurrentEquippedItem()!=null && player.getCurrentEquippedItem().getItem() instanceof ItemPrimordialGlove) {
					if (player.isSneaking()) {
						WitchingGadgets.packetHandler.sendToServer(new MessagePrimordialGlove(player, (byte)1, 0));
					}
					else if (FMLClientHandler.instance().getClient().inGameHasFocus && !keyDown) {
						keyDown = true;
					}
				}
			}

			/* if (player.isSneaking() && thaumcraftFKey.getIsKeyPressed()) {
				if (!player.worldObj.isRemote) {
					Minecraft.getMinecraft().thePlayer.sendChatMessage("B is pressed!");
					player.openGui(WitchingGadgets.instance, 4, player.worldObj, MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ));
				}
			} */

			if (keyDown) {
				if(gemRadial<1)
					gemRadial += WGConfig.radialSpeed;
				if(gemRadial>1)
					gemRadial=1;
				if(gemRadial>=1) {
					gemLock=true;
					keyDown = false;
				}
			} else if(!gemLock && gemRadial != 0) {
				if(gemRadial>0)
					gemRadial -= WGConfig.radialSpeed;
				if(gemRadial<=0) {
					gemRadial = 0;
				}
			}
		}
	}

}