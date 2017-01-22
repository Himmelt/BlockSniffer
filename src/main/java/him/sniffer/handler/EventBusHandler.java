package him.sniffer.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import him.sniffer.client.BlockSniffer;
import him.sniffer.client.BlockSniffer.ScanResult;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

import static him.sniffer.Sniffer.*;

public class EventBusHandler {
    @SubscribeEvent
    public void onPlayerRightClickBlock(PlayerInteractEvent event) {
        if (event.world instanceof WorldClient) {
            logger.info("onPlayerRightClickBlock");
            BlockSniffer sniffer = proxy.sniffer;
            logger.info(sniffer.isActive());
            if (event.action == Action.RIGHT_CLICK_BLOCK && sniffer.isActive() &&
                sniffer.last + sniffer.delay < System.currentTimeMillis()) {
                EntityPlayer player = event.entityPlayer;
                logger.info(player.getDisplayName());
                sniffer.last = System.currentTimeMillis();
                ScanResult result = sniffer.scanWorld(player);
                if (result.found) {
                    sniffer.spawnParticles(
                            player.worldObj, player.posX, player.posY, player.posZ, result.x, result.y,
                            result.z, sniffer.getTarget().getColor()
                    );
                    sniffer.result = result;
                } else {
                    sniffer.result = new ScanResult(null, false, 0, 0, 0);
                }
            }
        }
    }

}
