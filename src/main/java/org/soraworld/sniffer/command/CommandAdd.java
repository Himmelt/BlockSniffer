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
import org.soraworld.sniffer.constant.Constants;
import org.soraworld.sniffer.core.TBlock;
import org.soraworld.sniffer.core.Target;
import org.soraworld.sniffer.util.I19n;

import java.util.ArrayList;
import java.util.List;

public class CommandAdd extends IICommand {

    private final IICommand parent;

    public CommandAdd(IICommand parent) {
        super("add");
        this.parent = parent;
    }

    @Override
    public void execute(ICommandSender sender, ArrayList<String> args) {
        if (parent instanceof CommandTarget) {
            processTargetAdd((EntityPlayer) sender, args);
        } else if (parent instanceof CommandSub) {
            processSubAdd((EntityPlayer) sender, args);
        } else {
            super.execute(sender, args);
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

    private void processTargetAdd(EntityPlayer player, List<String> args) {
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
                    break;
            }
            if (args.size() == 1) {
                meta = null;
            } else if (args.size() == 3 && "meta".equals(args.get(1)) && Constants.PATTERN_NUM.matcher(args.get(2)).matches()) {
                meta = Integer.valueOf(args.get(2));
            }
            TBlock blk = new TBlock(block, meta);
            api.addTarget(new Target(blk));
            I19n.sendChat("sf.target.add.ok", blk.getName());
        } else {
            I19n.sendChat("sf.help.add");
        }
    }
}
