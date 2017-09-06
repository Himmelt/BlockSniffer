package org.soraworld.sniffer.client.command;

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

import static org.soraworld.sniffer.BlockSniffer.proxy;

@SideOnly(Side.CLIENT)
public class CommandSniffer implements ICommand {

    private static void processTarget(EntityPlayer player, List<String> cmds) {
        if (cmds.size() >= 1) {
            String cmd = cmds.get(0);
            cmds.remove(0);
            Target target = proxy.sniffer.getTarget();
            if (proxy.sniffer.isActive() && target != null) {
                switch (cmd) {
                    case "i":
                    case "info":
                        proxy.addChatMessage("sf.target.info", target.getMode(), target.getColorValue(), target.getDepthL(), target.getDepthH(), target.getHrange(), target.getVrange());
                        break;
                    case "m":
                    case "mode":
                        if (cmds.isEmpty()) {
                            int m = target.getMode();
                            String mode = I18n.format(m == 0 ? "sf.mode.0" : "sf.mode.1");
                            proxy.addChatMessage("sf.target.m.get", mode);
                        } else {
                            if ("1".equals(cmds.get(0))) {
                                target.setMode(1);
                                proxy.addChatMessage("sf.target.m.set", I18n.format("sf.mode.1"));
                            } else {
                                target.setMode(0);
                                proxy.addChatMessage("sf.target.m.set", I18n.format("sf.mode.0"));
                            }
                        }
                        break;
                    case "h":
                    case "hrange":
                        if (cmds.isEmpty()) {
                            proxy.addChatMessage("sf.target.h.get", target.getHrange());
                        } else {
                            if (Constants.PATTERN_NUM.matcher(cmds.get(0)).matches()) {
                                target.setHrange(Integer.valueOf(cmds.get(0)));
                                proxy.addChatMessage("sf.target.h.set", target.getHrange());
                            } else {
                                proxy.addChatMessage("sf.invalid.num");
                            }
                        }
                        break;
                    case "v":
                    case "vrange":
                        if (cmds.isEmpty()) {
                            proxy.addChatMessage("sf.target.v.get", target.getVrange());
                        } else {
                            if (Constants.PATTERN_NUM.matcher(cmds.get(0)).matches()) {
                                target.setVrange(Integer.valueOf(cmds.get(0)));
                                proxy.addChatMessage("sf.target.v.set", target.getVrange());
                            } else {
                                proxy.addChatMessage("sf.invalid.num");
                            }
                        }
                        break;
                    case "d":
                    case "depth":
                        if (cmds.isEmpty()) {
                            proxy.addChatMessage("sf.target.d.get", target.getDepthL(), target.getDepthH());
                        } else if (cmds.size() >= 2) {
                            if (Constants.PATTERN_NUM.matcher(cmds.get(0)).matches() && Constants.PATTERN_NUM.matcher(cmds.get(1)).matches()) {
                                target.setDepth(Integer.valueOf(cmds.get(0)), Integer.valueOf(cmds.get(1)));
                                proxy.addChatMessage("sf.target.d.set", target.getDepthL(), target.getDepthH());
                            } else {
                                proxy.addChatMessage("sf.invalid.num");
                            }
                        } else {
                            proxy.addChatMessage("sf.invalid.num");
                        }
                        break;
                    case "c":
                    case "color":
                        if (cmds.isEmpty()) {
                            proxy.addChatMessage("sf.target.c.get", target.getColorValue());
                        } else {
                            String value = cmds.get(0);
                            if ("map".equals(value)) {
                                target.setColor(value);
                                proxy.addChatMessage("sf.target.c.map");
                            } else {
                                Color color = ColorHelper.getColor(value);
                                if (color != null) {
                                    target.setColor(value);
                                    proxy.addChatMessage("sf.target.c.set", target.getColorValue());
                                } else {
                                    proxy.addChatMessage("sf.invalid.color");
                                }
                            }
                        }
                        break;
                    case "rm":
                    case "remove":
                        proxy.sniffer.removeTarget();
                        break;
                    case "cla":
                    case "clear":
                        if (cmds.isEmpty()) {
                            proxy.addChatMessage("sf.target.cla.hint");
                        } else {
                            if ("confirm".equals(cmds.get(0))) {
                                proxy.sniffer.clearTargets();
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
                        proxy.addChatMessage("sf.target.not");
                        break;
                    case "cla":
                    case "clear":
                        if (cmds.isEmpty()) {
                            proxy.addChatMessage("sf.target.cla.hint");
                        } else {
                            if ("confirm".equals(cmds.get(0))) {
                                proxy.sniffer.clearTargets();
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

    private static void processTargetAdd(EntityPlayer player, List<String> cmds) {
        if (cmds.size() >= 1) {
            Block block = null;
            Integer meta = null;
            switch (cmds.get(0)) {
                case "hold":
                    ItemStack itemStack = player.getHeldItem(EnumHand.MAIN_HAND);
                    block = Block.getBlockFromItem(itemStack.getItem());
                    meta = itemStack.getItemDamage();
                    if (block.equals(Blocks.AIR)) {
                        proxy.addChatMessage("sf.add.holdair");
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
                        proxy.addChatMessage("sf.add.lookair");
                        return;
                    }
                    break;
                default:
                    block = Block.getBlockFromName(cmds.get(0));
                    meta = 0;
                    if (block == null || block.equals(Blocks.AIR)) {
                        proxy.addChatMessage("sf.add.notname");
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
            proxy.sniffer.addTarget(new Target(blk));
            proxy.addChatMessage("sf.target.add.ok", blk.getName());
        } else {
            showTargetHelp("add");
        }
    }

    private static void processSub(EntityPlayer player, List<String> cmds) {
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
                            proxy.sniffer.getTarget().removeBlock(uid);
                        } else {
                            proxy.addChatMessage("sf.invalid.num");
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

    private static void processSubAdd(EntityPlayer player, List<String> cmds) {
        if (cmds.size() >= 1) {
            Block block = null;
            Integer meta = null;
            switch (cmds.get(0)) {
                case "hold":
                    ItemStack itemStack = player.getHeldItem(EnumHand.MAIN_HAND);
                    block = Block.getBlockFromItem(itemStack.getItem());
                    meta = itemStack.getItemDamage();
                    if (block.equals(Blocks.AIR)) {
                        proxy.addChatMessage("sf.add.holdair");
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
                        proxy.addChatMessage("sf.add.lookair");
                        return;
                    }
                    break;
                default:
                    block = Block.getBlockFromName(cmds.get(0));
                    meta = 0;
                    if (block == null || block.equals(Blocks.AIR)) {
                        proxy.addChatMessage("sf.add.notname");
                        return;
                    }
            }
            if (cmds.size() == 1) {
                meta = null;
            } else if (cmds.size() == 3 && "meta".equals(cmds.get(1)) && Constants.PATTERN_NUM.matcher(cmds.get(2)).matches()) {
                meta = Integer.valueOf(cmds.get(2));
            }
            TBlock blk = new TBlock(block, meta);
            proxy.sniffer.getTarget().addBlock(blk);
            proxy.addChatMessage("sf.sub.add.ok", blk.getName());
        } else {
            showSubHelp();
        }
    }

    private static void showHelp() {
        proxy.addChatMessage("sf.help.0");
        proxy.addChatMessage("sf.help.1");
        proxy.addChatMessage("sf.help.2");
        proxy.addChatMessage("sf.help.3");
        proxy.addChatMessage("sf.help.4");
        proxy.addChatMessage("sf.help.5");
        proxy.addChatMessage("sf.help.6");
        proxy.addChatMessage("sf.help.7");
        proxy.addChatMessage("sf.help.0");
    }

    private static void showTargetHelp(String cmd) {
        switch (cmd) {
            case "add":
                proxy.addChatMessage("sf.help.0");
                proxy.addChatMessage("sf.help.add.0");
                proxy.addChatMessage("sf.help.add.1");
                proxy.addChatMessage("sf.help.add.2");
                proxy.addChatMessage("sf.help.add.3");
                proxy.addChatMessage("sf.help.0");
                break;
            case "clear":
                proxy.addChatMessage("sf.help.target.cla");
                break;
            default:
                proxy.addChatMessage("sf.help.0");
                proxy.addChatMessage("sf.help.target.0");
                proxy.addChatMessage("sf.help.target.1");
                proxy.addChatMessage("sf.help.target.2");
                proxy.addChatMessage("sf.help.target.3");
                proxy.addChatMessage("sf.help.target.4");
                proxy.addChatMessage("sf.help.target.5");
                proxy.addChatMessage("sf.help.target.6");
                proxy.addChatMessage("sf.help.target.7");
                proxy.addChatMessage("sf.help.target.8");
                proxy.addChatMessage("sf.help.target.9");
                proxy.addChatMessage("sf.help.target.10");
                proxy.addChatMessage("sf.help.0");
        }
    }

    private static void showSubHelp() {
        proxy.addChatMessage("sf.help.0");
        proxy.addChatMessage("sf.help.add.0");
        proxy.addChatMessage("sf.help.add.1");
        proxy.addChatMessage("sf.help.add.2");
        proxy.addChatMessage("sf.help.add.3");
        proxy.addChatMessage("sf.help.0");
    }

    private static void showSubList(int way) {
        StringBuilder list = new StringBuilder();
        Map<Integer, TBlock> map = proxy.sniffer.getTarget().getBlocks();
        for (Entry<Integer, TBlock> entry : map.entrySet()) {
            list.append(entry.getKey()).append(" -> ").append(entry.getValue().getName()).append("; ");
        }
        proxy.addChatMessage(way == 0 ? "sf.sub.list" : "sf.sub.rm.list");
        proxy.addChatMessage(list.toString());
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
                        proxy.addChatMessage("sf.version", Constants.VERSION);
                        break;
                    case "s":
                    case "save":
                        proxy.config.save();
                        proxy.addChatMessage("sf.save");
                        break;
                    case "reload":
                        proxy.config.reload();
                        proxy.addChatMessage("sf.reload");
                        break;
                    case "reset":
                        proxy.sniffer.reset();
                        proxy.addChatMessage("sf.reset");
                        break;
                    case "t":
                    case "target":
                        processTarget((EntityPlayer) sender, cmds);
                        break;
                    case "sub":
                        if (proxy.sniffer.isActive() && proxy.sniffer.getTarget() != null) {
                            processSub((EntityPlayer) sender, cmds);
                        } else {
                            proxy.addChatMessage("sf.target.not");
                        }
                        break;
                    case "g":
                    case "gamma":
                        if (cmds.isEmpty()) {
                            proxy.addChatMessage("sf.gamma.get", proxy.getGamma());
                        } else {
                            if (Constants.PATTERN_NUM.matcher(cmds.get(0)).matches()) {
                                proxy.setGamma(Integer.valueOf(cmds.get(0)));
                                proxy.addChatMessage("sf.gamma.set", proxy.getGamma());
                            } else {
                                proxy.addChatMessage("sf.invalid.num");
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
            Constants.LOGGER.info(I18n.format("sf.cmd.error"));
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
