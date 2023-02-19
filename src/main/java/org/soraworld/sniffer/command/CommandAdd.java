package org.soraworld.sniffer.command;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import org.soraworld.sniffer.constant.Constants;
import org.soraworld.sniffer.core.TBlock;
import org.soraworld.sniffer.core.Target;
import org.soraworld.sniffer.util.I19n;
import org.soraworld.sniffer.util.Lists;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CommandAdd extends IICommand {

    private final IICommand parent;
    private static final List<String> list = getAddMatchList();

    public CommandAdd(IICommand parent) {
        super("add");
        this.parent = parent;
    }

    private static List<String> getAddMatchList() {
        List<String> list = new ArrayList<>();
        list.add("hold");
        list.add("look");
        Block.REGISTRY.getKeys().forEach(key -> list.add(key.toString()));
        return list;
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return I18n.format("sf.help.add");
    }

    @Override
    public void execute(ICommandSender sender, ArrayList<String> args) {
        if (args.size() >= 1 && (parent instanceof CommandTarget || parent instanceof CommandSub) && sender instanceof EntityPlayer) {
            Block block = null;
            Integer meta = null;
            EntityPlayer player = (EntityPlayer) sender;
            String arg0 = args.remove(0);
            switch (arg0) {
                case "hold":
                    ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
                    if (stack != null) {
                        Item item = stack.getItem();
                        if (item != null && item != Items.AIR) {
                            block = Block.getBlockFromItem(item);
                            meta = stack.getItemDamage();
                            if (block != null && !block.equals(Blocks.AIR)) break;
                        }
                    }
                    I19n.sendChat("sf.invalid.add");
                    return;
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
                    block = Block.getBlockFromName(arg0);
                    meta = 0;
                    if (block == null || block.equals(Blocks.AIR)) {
                        I19n.sendChat("sf.invalid.name");
                        return;
                    }
                    break;
            }

            if (args.size() == 0) {
                meta = null;
            } else if ("meta".equals(args.get(0))) {
                if (args.size() == 2) {
                    if (Constants.PATTERN_NUM.matcher(args.get(1)).matches()) {
                        meta = Integer.valueOf(args.get(1));
                    } else {
                        I19n.sendChat("sf.invalid.num");
                        return;
                    }
                } else if (args.size() >= 3) {
                    I19n.sendChat2(getUsage(sender));
                    return;
                }
            } else {
                I19n.sendChat2(getUsage(sender));
                return;
            }

            TBlock blk = new TBlock(block, meta);
            if (parent instanceof CommandTarget) {
                api.addTarget(new Target(blk));
                I19n.sendChat("sf.target.add.ok", blk.getName());
            } else {
                api.current.addBlock(blk);
                I19n.sendChat("sf.sub.add.ok", blk.getName());
            }
        } else {
            I19n.sendChat2(getUsage(sender));
        }
    }

    @Override
    protected List<String> getTabCompletions(ICommandSender sender, ArrayList<String> args) {
        if (args.size() == 1) {
            return getMatchList(args.get(0), list);
        } else if (args.size() >= 2) {
            if (list.contains(args.remove(0))) {
                return Lists.arrayList("meta");
            }
        }
        return new ArrayList<>();
    }
}
