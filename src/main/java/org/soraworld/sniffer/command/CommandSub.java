package org.soraworld.sniffer.command;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.soraworld.sniffer.constant.Constants;
import org.soraworld.sniffer.core.TBlock;
import org.soraworld.sniffer.util.I19n;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class CommandSub extends IICommand {

    @Nonnull
    @Override
    public String getName() {
        return "sub";
    }

    @Override
    public void execute(ICommandSender sender, ArrayList<String> args) {
        processSub((EntityPlayer) sender, args);
    }

    private void processSub(EntityPlayer player, List<String> args) {
        if (args.size() >= 1) {
            String arg = args.get(0);
            args.remove(0);
            switch (arg) {
                case "l":
                case "list":
                    showSubList(0);
                    break;
                case "add":
                    processSubAdd(player, args);
                    break;
                case "rm":
                case "remove":
                    if (args.size() >= 1) {
                        if (Constants.PATTERN_NUM.matcher(args.get(0)).matches()) {
                            int uid = Integer.valueOf(args.get(0));
                            api.current.removeBlock(uid);
                        } else {
                            I19n.sendChat("sf.invalid.num");
                        }
                    } else {
                        showSubList(1);
                    }
                    break;
                default:
                    I19n.sendChat("sf.help.add");
            }
        } else {
            I19n.sendChat("sf.help.add");
        }
    }

    private void processSubAdd(EntityPlayer player, List<String> args) {
        if (args.size() >= 1) {
            Block block = null;
            Integer meta = null;
            switch (args.get(0)) {
                case "hold":
                    ItemStack itemStack = player.getHeldItem(EnumHand.MAIN_HAND);
                    block = Block.getBlockFromItem(itemStack.getItem());
                    meta = itemStack.getItemDamage();
                    if (block.equals(Blocks.AIR)) {
                        I19n.sendChat("sf.invalid.add");
                        return;
                    }
                    break;
                case "look":
                    RayTraceResult focused = Minecraft.getMinecraft().objectMouseOver;
                    if (focused != null && focused.typeOfHit == RayTraceResult.Type.BLOCK) {
                        IBlockState state = player.getEntityWorld().getBlockState(focused.getBlockPos());
                        block = state.getBlock();
                        meta = block.getMetaFromState(state);
                    }
                    if (block == null || block.equals(Blocks.AIR)) {
                        I19n.sendChat("sf.invalid.add");
                        return;
                    }
                    break;
                default:
                    block = Block.getBlockFromName(args.get(0));
                    meta = 0;
                    if (block == null || block.equals(Blocks.AIR)) {
                        I19n.sendChat("sf.invalid.name");
                        return;
                    }
            }
            if (args.size() == 1) {
                meta = null;
            } else if (args.size() == 3 && "meta".equals(args.get(1)) && Constants.PATTERN_NUM.matcher(args.get(2)).matches()) {
                meta = Integer.valueOf(args.get(2));
            }
            TBlock blk = new TBlock(block, meta);
            api.current.addBlock(blk);
            I19n.sendChat("sf.sub.add.ok", blk.getName());
        } else {
            I19n.sendChat("sf.help.add");
        }
    }

    private void showSubList(int way) {
        StringBuilder list = new StringBuilder();
        Map<Integer, TBlock> map = api.current.getBlocks();
        for (Map.Entry<Integer, TBlock> entry : map.entrySet()) {
            list.append(entry.getKey()).append(" -> ").append(entry.getValue().getName()).append("; ");
        }
        I19n.sendChat(way == 0 ? "sf.sub.list" : "sf.sub.rm.list");
        I19n.sendChat(list.toString());
    }

}
