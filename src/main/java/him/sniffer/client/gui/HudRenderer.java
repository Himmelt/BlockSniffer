package him.sniffer.client.gui;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import him.sniffer.constant.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class HudRenderer {

    private final RenderItem itemRenderer = new RenderItem();
    private final Minecraft minecraft = FMLClientHandler.instance().getClient();

    public void renderItem(ItemStack itemStack, float x, float y) {
        GL11.glEnable(2929);
        GL11.glPushMatrix();
        GL11.glRotatef(120.0F, 1.0F, 0.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable('\u803a');
        itemRenderer.zLevel = 100.0F;

        try {
            itemRenderer.renderItemAndEffectIntoGUI(minecraft.fontRenderer, minecraft.renderEngine, itemStack, 0, 0);
        } catch (RuntimeException e) {
            Mod.logger.catching(e);
        } finally {
            itemRenderer.zLevel = 0.0F;
            GL11.glDisable('\u803a');
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(2929);
            GL11.glDisable(2896);
            GL11.glPopMatrix();
        }
    }
}
