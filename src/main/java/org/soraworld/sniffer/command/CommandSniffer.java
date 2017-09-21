package org.soraworld.sniffer.command;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.soraworld.sniffer.BlockSniffer;
import org.soraworld.sniffer.api.SnifferAPI;
import org.soraworld.sniffer.constant.ColorHelper;
import org.soraworld.sniffer.constant.Constants;
import org.soraworld.sniffer.core.TBlock;
import org.soraworld.sniffer.core.Target;
import org.soraworld.sniffer.util.I19n;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@SideOnly(Side.CLIENT)
public class CommandSniffer implements ICommand {

    private final SnifferAPI api = BlockSniffer.getAPI();

    private void processTarget(EntityPlayer player, List<String> args) {
        if (args.size() >= 1) {
            String arg = args.get(0);
            args.remove(0);
            Target target = api.current;
            if (api.active && target != null) {
                switch (arg) {
                    case "i":
                    case "info":
                        I19n.sendChat("sf.target.info", target.getMode(), target.getChatColor(), target.getDepthL(), target.getDepthH(), target.getHRange(), target.getVRange());
                        break;
                    case "m":
                    case "mode":
                        if (args.isEmpty()) {
                            int m = target.getMode();
                            String mode = I18n.format(m == 0 ? "sf.mode.0" : "sf.mode.1");
                            I19n.sendChat("sf.target.m.get", mode);
                        } else {
                            if ("1".equals(args.get(0))) {
                                target.setMode(1);
                                I19n.sendChat("sf.target.m.set", I18n.format("sf.mode.1"));
                            } else {
                                target.setMode(0);
                                I19n.sendChat("sf.target.m.set", I18n.format("sf.mode.0"));
                            }
                        }
                        break;
                    case "h":
                    case "hrange":
                        if (args.isEmpty()) {
                            I19n.sendChat("sf.target.h.get", target.getHRange());
                        } else {
                            if (Constants.PATTERN_NUM.matcher(args.get(0)).matches()) {
                                target.setHRange(Integer.valueOf(args.get(0)));
                                I19n.sendChat("sf.target.h.set", target.getHRange());
                            } else {
                                I19n.sendChat("sf.invalid.num");
                            }
                        }
                        break;
                    case "v":
                    case "vrange":
                        if (args.isEmpty()) {
                            I19n.sendChat("sf.target.v.get", target.getVRange());
                        } else {
                            if (Constants.PATTERN_NUM.matcher(args.get(0)).matches()) {
                                target.setVRange(Integer.valueOf(args.get(0)));
                                I19n.sendChat("sf.target.v.set", target.getVRange());
                            } else {
                                I19n.sendChat("sf.invalid.num");
                            }
                        }
                        break;
                    case "d":
                    case "depth":
                        if (args.isEmpty()) {
                            I19n.sendChat("sf.target.d.get", target.getDepthL(), target.getDepthH());
                        } else if (args.size() >= 2) {
                            if (Constants.PATTERN_NUM.matcher(args.get(0)).matches() && Constants.PATTERN_NUM.matcher(args.get(1)).matches()) {
                                target.setDepth(Integer.valueOf(args.get(0)), Integer.valueOf(args.get(1)));
                                I19n.sendChat("sf.target.d.set", target.getDepthL(), target.getDepthH());
                            } else {
                                I19n.sendChat("sf.invalid.num");
                            }
                        } else {
                            I19n.sendChat("sf.invalid.num");
                        }
                        break;
                    case "c":
                    case "color":
                        if (args.isEmpty()) {
                            I19n.sendChat("sf.target.c.get", target.getChatColor());
                        } else {
                            String value = args.get(0);
                            if ("map".equals(value)) {
                                target.setColor(value);
                                I19n.sendChat("sf.target.c.map");
                            } else {
                                Color color = ColorHelper.getColor(value);
                                if (color != null) {
                                    target.setColor(value);
                                    I19n.sendChat("sf.target.c.set", target.getChatColor());
                                } else {
                                    I19n.sendChat("sf.invalid.color");
                                }
                            }
                        }
                        break;
                    case "rm":
                    case "remove":
                        api.removeTarget();
                        break;
                    case "cla":
                    case "clear":
                        if (args.isEmpty()) {
                            I19n.sendChat("sf.target.cla.hint");
                        } else {
                            if ("confirm".equals(args.get(0))) {
                                api.clearTargets();
                            } else {
                                I19n.sendChat("sf.help.target.cla");
                            }
                        }
                        break;
                    case "add":
                        processTargetAdd(player, args);
                        break;
                    default:
                        I19n.sendChat("sf.help.target");
                }
            } else {
                switch (arg) {
                    case "i":
                    case "info":
                    case "m":
                    case "mode":
                    case "h":
                    case "hrange":
                    case "v":
                    case "vrange":
                    case "d":
                    case "depth":
                    case "c":
                    case "color":
                    case "rm":
                    case "remove":
                        I19n.sendChat("sf.target.not");
                        break;
                    case "cla":
                    case "clear":
                        if (args.isEmpty()) {
                            I19n.sendChat("sf.target.cla.hint");
                        } else {
                            if ("confirm".equals(args.get(0))) {
                                api.clearTargets();
                            } else {
                                I19n.sendChat("sf.help.target.cla");
                            }
                        }
                        break;
                    case "add":
                        processTargetAdd(player, args);
                        break;
                    default:
                        I19n.sendChat("sf.help.target");
                }
            }
        } else {
            I19n.sendChat("sf.help.target");
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
        for (Entry<Integer, TBlock> entry : map.entrySet()) {
            list.append(entry.getKey()).append(" -> ").append(entry.getValue().getName()).append("; ");
        }
        I19n.sendChat(way == 0 ? "sf.sub.list" : "sf.sub.rm.list");
        I19n.sendChat(list.toString());
    }

    @Nonnull
    @Override
    public String getName() {
        return Constants.MODID;
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return I18n.format("sf.help");
    }

    @Nonnull
    @Override
    public List<String> getAliases() {
        List<String> alias = new ArrayList<>();
        alias.add("sf");
        return alias;
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if (sender instanceof EntityPlayer) {
            List<String> argList = new ArrayList<>(Arrays.asList(args));
            if (argList.size() >= 1) {
                argList.remove(0);
                switch (args[0]) {
                    case "reload":
                        api.reload();
                        I19n.sendChat("sf.reload");
                        break;
                    case "reset":
                        api.reset();
                        I19n.sendChat("sf.reset");
                        break;
                    case "t":
                    case "target":
                        processTarget((EntityPlayer) sender, argList);
                        break;
                    case "sub":
                        if (api.active && api.current != null) {
                            processSub((EntityPlayer) sender, argList);
                        } else {
                            I19n.sendChat("sf.target.not");
                        }
                        break;
                    case "g":
                    case "gamma":
                        if (argList.isEmpty()) {
                            I19n.sendChat("sf.gamma.get", api.getGamma());
                        } else {
                            if (Constants.PATTERN_NUM.matcher(argList.get(0)).matches()) {
                                api.setGamma(Integer.valueOf(argList.get(0)));
                                I19n.sendChat("sf.gamma.set", api.getGamma());
                            } else {
                                I19n.sendChat("sf.invalid.num");
                            }
                        }
                        break;
                    default:
                        I19n.sendChat("sf.help");
                }
            } else {
                I19n.sendChat("sf.help");
            }
        } else {
            api.LOGGER.info(I18n.format("sf.cmd.error"));
            sender.sendMessage(new TextComponentString(I18n.format("sf.cmd.error")));
        }
        api.save();
    }

    @Override
    public boolean checkPermission(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender) {
        return true;
    }

    @Nonnull
    @Override
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        return new ArrayList<>();
    }

    @Override
    public boolean isUsernameIndex(@Nonnull String[] args, int index) {
        return false;
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CommandSniffer;
    }

    @Override
    public int compareTo(@Nonnull ICommand command) {
        return getName().compareTo(command.getName());
    }
}
