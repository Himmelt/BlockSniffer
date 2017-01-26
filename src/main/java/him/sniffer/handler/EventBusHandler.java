package him.sniffer.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import him.sniffer.core.BlockSniffer;
import him.sniffer.core.ScanResult;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

import static him.sniffer.Sniffer.*;

@SideOnly(Side.CLIENT)
public class EventBusHandler {
    @SubscribeEvent
    public void onPlayerRightClickBlock(PlayerInteractEvent event) {
        if (!proxy.sniffer.forbid && event.world instanceof WorldClient) {
            final BlockSniffer sniffer = proxy.sniffer;
            if (event.action == Action.RIGHT_CLICK_BLOCK && sniffer.isActive() &&
                sniffer.last + sniffer.delay < System.currentTimeMillis()) {
                final EntityPlayer player = event.entityPlayer;
                sniffer.last = System.currentTimeMillis();
                new Thread(() -> {
                    //logger.info("Sniffer Thread Started!");
                    sniffer.scanWorld(player);
                    ScanResult r = sniffer.result;
                    World w = player.worldObj;
                    double px = player.posX, py = player.posY, pz = player.posZ;
                    if (r != null) {
                        //logger.info("Sniffer Found Target!");
                        sniffer.spawn(w, px, py, pz, r.x, r.y, r.z, r.getColor());
                    }
                }).start();
            }
        }
    }
}
