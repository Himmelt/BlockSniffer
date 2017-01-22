package him.sniffer.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import him.sniffer.client.BlockSniffer;
import him.sniffer.client.BlockSniffer.ScanResult;
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
                sniffer.last + sniffer.delay < System.currentTimeMillis()) {
                final EntityPlayer player = event.entityPlayer;
                sniffer.last = System.currentTimeMillis();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
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
                }).start();
            }
        }
    }

    @SubscribeEvent
    public void onPlayerJoinWorld(EntityJoinWorldEvent event) {
        if (event.entity instanceof EntityPlayerSP && event.world instanceof WorldClient) {
            proxy.config.reload();
        }
    }
}
