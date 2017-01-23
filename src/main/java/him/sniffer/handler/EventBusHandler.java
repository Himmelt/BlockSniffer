package him.sniffer.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import him.sniffer.client.BlockSniffer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

import static him.sniffer.Sniffer.*;

@SideOnly(Side.CLIENT)
public class EventBusHandler {
    @SubscribeEvent
    public void onPlayerRightClickBlock(PlayerInteractEvent event) {
        if (event.world instanceof WorldClient) {
            final BlockSniffer sniffer = proxy.sniffer;
            if (event.action == Action.RIGHT_CLICK_BLOCK && sniffer.isActive() &&
                sniffer.last + BlockSniffer.delay < System.currentTimeMillis()) {
                final EntityPlayer player = event.entityPlayer;
                sniffer.last = System.currentTimeMillis();
                new Thread(() -> {
                    sniffer.scanWorld(player);
                    if (sniffer.result != null) {
                        sniffer.particle.spawn(
                                player.worldObj, player.posX, player.posY, player.posZ,
                                sniffer.result.x, sniffer.result.y,
                                sniffer.result.z, sniffer.getTarget().getColor()
                        );
                    }
                }).start();
            }
        }
    }

    //@SubscribeEvent
    public void onPlayerJoinWorld(EntityJoinWorldEvent event) {
        if (event.entity instanceof EntityPlayerSP && event.world instanceof WorldClient) {
            proxy.config.reload();
        }
    }
}
