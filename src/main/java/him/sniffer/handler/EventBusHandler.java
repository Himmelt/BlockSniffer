package him.sniffer.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import him.sniffer.core.BlockSniffer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

import static him.sniffer.Sniffer.*;

@SideOnly(Side.CLIENT)
public class EventBusHandler {
    @SubscribeEvent
    public void onPlayerRightClickBlock(PlayerInteractEvent event) {
        final BlockSniffer sniffer = proxy.sniffer;
        if (event.world instanceof WorldClient) {
            if (event.action == Action.RIGHT_CLICK_BLOCK && sniffer.isActive() && sniffer.last + sniffer.delay < System.currentTimeMillis()) {
                final EntityPlayer player = event.entityPlayer;
                sniffer.last = System.currentTimeMillis();
                new Thread(() -> {
                    //logger.info("Sniffer Thread Started!");
                    sniffer.scanWorld(player);
                    if (sniffer.result != null) {
                        //logger.info("Sniffer Found Target!");
                        sniffer.spawn(player, sniffer.result);
                    }
                }).start();
            }
        }
    }
}
