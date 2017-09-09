package org.soraworld.sniffer.command;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
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

    private void processTarget(EntityPlayer player, List<String> cmds) {
        if (cmds.size() >= 1) {
            String cmd = cmds.get(0);
            cmds.remove(0);
            Target target = api.current;
            if (api.active && target != null) {
                switch (cmd) {
                    case "i":
                    case "info":
                        api.sendChat("sf.target.info", target.getMode(), target.getChatColor(), target.getDepthL(), target.getDepthH(), target.getHRange(), target.getVRange());
                        break;
                    case "m":
                    case "mode":
                        if (cmds.isEmpty()) {
                            int m = target.getMode();
                            String mode = I18n.format(m == 0 ? "sf.mode.0" : "sf.mode.1");
                            api.sendChat("sf.target.m.get", mode);
                        } else {
                            if ("1".equals(cmds.get(0))) {
                                target.setMode(1);
                                api.sendChat("sf.target.m.set", I18n.format("sf.mode.1"));
                            } else {
                                target.setMode(0);
                                api.sendChat("sf.target.m.set", I18n.format("sf.mode.0"));
                            }
                        }
                        break;
                    case "h":
                    case "hrange":
                        if (cmds.isEmpty()) {
                            api.sendChat("sf.target.h.get", target.getHRange());
                        } else {
                            if (Constants.PATTERN_NUM.matcher(cmds.get(0)).matches()) {
                                target.setHRange(Integer.valueOf(cmds.get(0)));
                                api.sendChat("sf.target.h.set", target.getHRange());
                            } else {
                                api.sendChat("sf.invalid.num");
                            }
                        }
                        break;
                    case "v":
                    case "vrange":
                        if (cmds.isEmpty()) {
                            api.sendChat("sf.target.v.get", target.getVRange());
                        } else {
                            if (Constants.PATTERN_NUM.matcher(cmds.get(0)).matches()) {
                                target.setVRange(Integer.valueOf(cmds.get(0)));
                                api.sendChat("sf.target.v.set", target.getVRange());
                            } else {
                                api.sendChat("sf.invalid.num");
                            }
                        }
                        break;
                    case "d":
                    case "depth":
                        if (cmds.isEmpty()) {
                            api.sendChat("sf.target.d.get", target.getDepthL(), target.getDepthH());
                        } else if (cmds.size() >= 2) {
                            if (Constants.PATTERN_NUM.matcher(cmds.get(0)).matches() && Constants.PATTERN_NUM.matcher(cmds.get(1)).matches()) {
                                target.setDepth(Integer.valueOf(cmds.get(0)), Integer.valueOf(cmds.get(1)));
                                api.sendChat("sf.target.d.set", target.getDepthL(), target.getDepthH());
                            } else {
                                api.sendChat("sf.invalid.num");
                            }
                        } else {
                            api.sendChat("sf.invalid.num");
                        }
                        break;
                    case "c":
                    case "color":
                        if (cmds.isEmpty()) {
                            api.sendChat("sf.target.c.get", target.getChatColor());
                        } else {
                            String value = cmds.get(0);
                            if ("map".equals(value)) {
                                target.setColor(value);
                                api.sendChat("sf.target.c.map");
                            } else {
                                Color color = ColorHelper.getColor(value);
                                if (color != null) {
                                    target.setColor(value);
                                    api.sendChat("sf.target.c.set", target.getChatColor());
                                } else {
                                    api.sendChat("sf.invalid.color");
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
                        if (cmds.isEmpty()) {
                            api.sendChat("sf.target.cla.hint");
                        } else {
                            if ("confirm".equals(cmds.get(0))) {
                                api.clearTargets();
                            } else {
                                showTargetHelp("clear");
                            }
                        }
                        break;
                    case "add":
                        processTargetAdd(player, cmds);
                        break;
                    default:
                        showTargetHelp("");
                }
            } else {
                switch (cmd) {
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
                        api.sendChat("sf.target.not");
                        break;
                    case "cla":
                    case "clear":
                        if (cmds.isEmpty()) {
                            api.sendChat("sf.target.cla.hint");
                        } else {
                            if ("confirm".equals(cmds.get(0))) {
                                api.clearTargets();
                            } else {
                                showTargetHelp("clear");
                            }
                        }
                        break;
                    case "add":
                        processTargetAdd(player, cmds);
                        break;
                    default:
                        showTargetHelp("");
                }
            }
        } else {
            showTargetHelp("");
        }
    }

    private void processTargetAdd(EntityPlayer player, List<String> cmds) {
        if (cmds.size() >= 1) {
            Block block = null;
            Integer meta = null;
            switch (cmds.get(0)) {
                case "hold":
                    ItemStack itemStack = player.getHeldItem(EnumHand.MAIN_HAND);
                    block = Block.getBlockFromItem(itemStack.getItem());
                    meta = itemStack.getItemDamage();
                    if (block.equals(Blocks.AIR)) {
                        api.sendChat("sf.add.holdair");
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
                        api.sendChat("sf.add.lookair");
                        return;
                    }
                    break;
                default:
                    block = Block.getBlockFromName(cmds.get(0));
                    meta = 0;
                    if (block == null || block.equals(Blocks.AIR)) {
                        api.sendChat("sf.add.notname");
                        return;
                    }
                    break;
            }
            if (cmds.size() == 1) {
                meta = null;
            } else if (cmds.size() == 3 && "meta".equals(cmds.get(1)) && Constants.PATTERN_NUM.matcher(cmds.get(2)).matches()) {
                meta = Integer.valueOf(cmds.get(2));
            }
            TBlock blk = new TBlock(block, meta);
            api.addTarget(new Target(blk));
            api.sendChat("sf.target.add.ok", blk.getName());
        } else {
            showTargetHelp("add");
        }
    }

    private void processSub(EntityPlayer player, List<String> cmds) {
        if (cmds.size() >= 1) {
            String cmd = cmds.get(0);
            cmds.remove(0);
            switch (cmd) {
                case "l":
                case "list":
                    showSubList(0);
                    break;
                case "add":
                    processSubAdd(player, cmds);
                    break;
                case "rm":
                case "remove":
                    if (cmds.size() >= 1) {
                        if (Constants.PATTERN_NUM.matcher(cmds.get(0)).matches()) {
                            int uid = Integer.valueOf(cmds.get(0));
                            api.current.removeBlock(uid);
                        } else {
                            api.sendChat("sf.invalid.num");
                        }
                    } else {
                        showSubList(1);
                    }
                    break;
                default:
                    showSubHelp();
            }
        } else {
            showSubHelp();
        }
    }

    private void processSubAdd(EntityPlayer player, List<String> cmds) {
        if (cmds.size() >= 1) {
            Block block = null;
            Integer meta = null;
            switch (cmds.get(0)) {
                case "hold":
                    ItemStack itemStack = player.getHeldItem(EnumHand.MAIN_HAND);
                    block = Block.getBlockFromItem(itemStack.getItem());
                    meta = itemStack.getItemDamage();
                    if (block.equals(Blocks.AIR)) {
                        api.sendChat("sf.add.holdair");
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
                        api.sendChat("sf.add.lookair");
                        return;
                    }
                    break;
                default:
                    block = Block.getBlockFromName(cmds.get(0));
                    meta = 0;
                    if (block == null || block.equals(Blocks.AIR)) {
                        api.sendChat("sf.add.notname");
                        return;
                    }
            }
            if (cmds.size() == 1) {
                meta = null;
            } else if (cmds.size() == 3 && "meta".equals(cmds.get(1)) && Constants.PATTERN_NUM.matcher(cmds.get(2)).matches()) {
                meta = Integer.valueOf(cmds.get(2));
            }
            TBlock blk = new TBlock(block, meta);
            api.current.addBlock(blk);
            api.sendChat("sf.sub.add.ok", blk.getName());
        } else {
            showSubHelp();
        }
    }

    private void showHelp() {
        api.sendChat("sf.help.0");
        api.sendChat("sf.help.1");
        api.sendChat("sf.help.2");
        api.sendChat("sf.help.3");
        api.sendChat("sf.help.4");
        api.sendChat("sf.help.5");
        api.sendChat("sf.help.6");
        api.sendChat("sf.help.7");
        api.sendChat("sf.help.0");
    }

    private void showTargetHelp(String cmd) {
        switch (cmd) {
            case "add":
                api.sendChat("sf.help.0");
                api.sendChat("sf.help.add.0");
                api.sendChat("sf.help.add.1");
                api.sendChat("sf.help.add.2");
                api.sendChat("sf.help.add.3");
                api.sendChat("sf.help.0");
                break;
            case "clear":
                api.sendChat("sf.help.target.cla");
                break;
            default:
                api.sendChat("sf.help.0");
                api.sendChat("sf.help.target.0");
                api.sendChat("sf.help.target.1");
                api.sendChat("sf.help.target.2");
                api.sendChat("sf.help.target.3");
                api.sendChat("sf.help.target.4");
                api.sendChat("sf.help.target.5");
                api.sendChat("sf.help.target.6");
                api.sendChat("sf.help.target.7");
                api.sendChat("sf.help.target.8");
                api.sendChat("sf.help.target.9");
                api.sendChat("sf.help.target.10");
                api.sendChat("sf.help.0");
        }
    }

    private void showSubHelp() {
        api.sendChat("sf.help.0");
        api.sendChat("sf.help.add.0");
        api.sendChat("sf.help.add.1");
        api.sendChat("sf.help.add.2");
        api.sendChat("sf.help.add.3");
        api.sendChat("sf.help.0");
    }

    private void showSubList(int way) {
        StringBuilder list = new StringBuilder();
        Map<Integer, TBlock> map = api.current.getBlocks();
        for (Entry<Integer, TBlock> entry : map.entrySet()) {
            list.append(entry.getKey()).append(" -> ").append(entry.getValue().getName()).append("; ");
        }
        api.sendChat(way == 0 ? "sf.sub.list" : "sf.sub.rm.list");
        api.sendChat(list.toString());
    }

    @Nonnull
    @Override
    public String getName() {
        return Constants.MODID;
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "";
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
        if (sender instanceof EntityPlayer && sender.getEntityWorld() instanceof WorldClient) {
            List<String> cmds = new ArrayList<>(Arrays.asList(args));
            if (cmds.size() >= 1) {
                cmds.remove(0);
                switch (args[0]) {
                    case "h":
                    case "help":
                        showHelp();
                        break;
                    case "v":
                    case "version":
                        api.sendChat("sf.version", Constants.VERSION);
                        break;
                    case "s":
                    case "save":
                        api.save();
                        api.sendChat("sf.save");
                        break;
                    case "reload":
                        api.reload();
                        api.sendChat("sf.reload");
                        break;
                    case "reset":
                        api.reset();
                        api.sendChat("sf.reset");
                        break;
                    case "t":
                    case "target":
                        processTarget((EntityPlayer) sender, cmds);
                        break;
                    case "sub":
                        if (api.active && api.current != null) {
                            processSub((EntityPlayer) sender, cmds);
                        } else {
                            api.sendChat("sf.target.not");
                        }
                        break;
                    case "g":
                    case "gamma":
                        if (cmds.isEmpty()) {
                            api.sendChat("sf.gamma.get", api.getGamma());
                        } else {
                            if (Constants.PATTERN_NUM.matcher(cmds.get(0)).matches()) {
                                api.setGamma(Integer.valueOf(cmds.get(0)));
                                api.sendChat("sf.gamma.set", api.getGamma());
                            } else {
                                api.sendChat("sf.invalid.num");
                            }
                        }
                        break;
                    default:
                        showHelp();
                }
            } else {
                showHelp();
            }
        } else {
            api.LOGGER.info(I18n.format("sf.cmd.error"));
            sender.sendMessage(new TextComponentString(I18n.format("sf.cmd.error")));
        }
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
