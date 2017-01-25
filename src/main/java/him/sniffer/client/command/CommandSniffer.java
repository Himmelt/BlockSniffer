package him.sniffer.client.command;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import him.sniffer.constant.ColorHelper;
import him.sniffer.core.SubTarget;
import him.sniffer.core.Target;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static him.sniffer.Sniffer.*;
import static him.sniffer.constant.ModInfo.*;

@SideOnly(Side.CLIENT)
public class CommandSniffer implements ICommand {

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!proxy.sniffer.forbid && sender instanceof EntityPlayer && sender.getEntityWorld() instanceof WorldClient) {
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
                    proxy.addChatMessage(I18n.format("sf.version", VERSION));
                    break;
                case "s":
                case "save":
                    proxy.config.save();
                    proxy.addChatMessage(I18n.format("sf.save"));
                    break;
                case "reload":
                    proxy.config.reload();
                    proxy.addChatMessage(I18n.format("sf.reload"));
                    break;
                case "reset":
                    proxy.sniffer.reset();
                    proxy.addChatMessage(I18n.format("sf.reset"));
                    break;
                case "off":
                    proxy.sniffer.inActive();
                    break;
                case "target":
                    processTarget((EntityPlayer) sender, cmds);
                    break;
                case "sub":
                    processSub((EntityPlayer) sender, cmds);
                    break;
                default:
                    showHelp();
                }
            } else {
                showHelp();
            }
        } else {
            logger.info(I18n.format("sf.cmd.error"));
            sender.addChatMessage(new ChatComponentText(I18n.format("sf.cmd.error")));
        }
    }

    private static void processTarget(EntityPlayer player, List<String> cmds) {
        if (cmds.size() >= 1) {
            String cmd = cmds.get(0);
            cmds.remove(0);
            Target target = proxy.sniffer.getTarget();
            if (proxy.sniffer.isActive() && target != null) {
                switch (cmd) {
                case "i":
                case "info":
                    proxy.addChatMessage(I18n.format(
                            "sf.target.info", target.mode, target.colorValue, target.depth[0], target.depth[1],
                            target.hRange, target.vRange));
                    break;
                case "m":
                case "mode":
                    if (cmds.isEmpty()) {
                        int m = target.mode;
                        String mode = I18n.format(m == 0? "sf.mode.0" : "sf.mode.1");
                        proxy.addChatMessage(I18n.format("sf.target.m.get", mode));
                    } else {
                        if ("1".equals(cmds.get(0))) {
                            target.mode = 1;
                            proxy.addChatMessage(I18n.format("sf.target.m.set", I18n.format("sf.mode.1")));
                        } else {
                            target.mode = 0;
                            proxy.addChatMessage(I18n.format("sf.target.m.set", I18n.format("sf.mode.0")));
                        }
                    }
                    break;
                case "h":
                case "hrange":
                    if (cmds.isEmpty()) {
                        proxy.addChatMessage(I18n.format("sf.target.h.get", target.hRange));
                    } else {
                        if (PATTERN_NUM.matcher(cmds.get(0)).matches()) {
                            int h = Integer.valueOf(cmds.get(0));
                            if (h < 0 || h > 15) {
                                h = 1;
                            }
                            target.hRange = h;
                            proxy.addChatMessage(I18n.format("sf.target.h.set", h));
                        } else {
                            proxy.addChatMessage(I18n.format("sf.invalid.num"));
                        }
                    }
                    break;
                case "v":
                case "vrange":
                    if (cmds.isEmpty()) {
                        proxy.addChatMessage(I18n.format("sf.target.v.get", target.vRange));
                    } else {
                        if (PATTERN_NUM.matcher(cmds.get(0)).matches()) {
                            int v = Integer.valueOf(cmds.get(0));
                            if (v < 0 || v > 255) {
                                v = 16;
                            }
                            target.vRange = v;
                            proxy.addChatMessage(I18n.format("sf.target.v.set", v));
                        } else {
                            proxy.addChatMessage(I18n.format("sf.invalid.num"));
                        }
                    }
                    break;
                case "d":
                case "depth":
                    if (cmds.isEmpty()) {
                        proxy.addChatMessage(I18n.format("sf.target.d.get", target.depth[0], target.depth[1]));
                    } else if (cmds.size() >= 2) {
                        if (PATTERN_NUM.matcher(cmds.get(0)).matches() &&
                            PATTERN_NUM.matcher(cmds.get(1)).matches()) {
                            int dl = Integer.valueOf(cmds.get(0));
                            int dh = Integer.valueOf(cmds.get(1));
                            if (dl < 0 || dl > 255) {
                                dl = 0;
                            }
                            if (dh < 0 || dh > 255) {
                                dh = 64;
                            }
                            target.depth[0] = dl;
                            target.depth[1] = dh;
                            proxy.addChatMessage(I18n.format("sf.target.d.set", dl, dh));
                        } else {
                            proxy.addChatMessage(I18n.format("sf.invalid.num"));
                        }
                    } else {
                        proxy.addChatMessage(I18n.format("sf.invalid.num"));
                    }
                    break;
                case "c":
                case "color":
                    if (cmds.isEmpty()) {
                        proxy.addChatMessage(I18n.format("sf.target.c.get", target.colorValue));
                    } else {
                        String value = cmds.get(0);
                        if ("map".equals(value)) {
                            target.colorValue = value;
                            target.setColor(null);
                            proxy.addChatMessage(I18n.format("sf.target.c.setmap"));
                        } else {
                            Color color = ColorHelper.getColor(value);
                            if (color != null) {
                                target.colorValue = value;
                                target.setColor(color);
                                proxy.addChatMessage(I18n.format("sf.target.c.set", value));
                            } else {
                                proxy.addChatMessage(I18n.format("sf.invalid.color"));
                            }
                        }
                    }
                    break;
                case "rm":
                case "remove":
                    if (proxy.sniffer.removeTarget()) {
                        proxy.addChatMessage(I18n.format("sf.target.rm.ok"));
                    } else {
                        proxy.addChatMessage(I18n.format("sf.target.rm.fail"));
                    }
                    break;
                case "cla":
                case "clear":
                    if (cmds.isEmpty()) {
                        proxy.addChatMessage(I18n.format("sf.target.cla.hint"));
                    } else {
                        if ("confirm".equals(cmds.get(0))) {
                            proxy.sniffer.ClearTarget();
                            proxy.addChatMessage(I18n.format("sf.target.cla.ok"));
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
                    proxy.addChatMessage(I18n.format("sf.target.not"));
                    break;
                case "cla":
                case "clear":
                    if (cmds.isEmpty()) {
                        proxy.addChatMessage(I18n.format("sf.target.cla.hint"));
                    } else {
                        if ("confirm".equals(cmds.get(0))) {
                            proxy.sniffer.ClearTarget();
                            proxy.addChatMessage(I18n.format("sf.target.cla.ok"));
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
                ItemStack itemStack = player.getHeldItem();
                if (itemStack == null || itemStack.getItem() == null) {
                    proxy.addChatMessage(I18n.format("sf.sub.add.holdair"));
                    return;
                }
                block = Block.getBlockFromItem(itemStack.getItem());
                meta = itemStack.getItemDamage();
                if (block == null || block.equals(Blocks.air)) {
                    proxy.addChatMessage(I18n.format("sf.sub.add.holdair"));
                    return;
                }
                break;
            case "look":
                MovingObjectPosition focused = Minecraft.getMinecraft().objectMouseOver;
                if (focused != null && focused.typeOfHit == MovingObjectType.BLOCK) {
                    block = player.worldObj.getBlock(focused.blockX, focused.blockY, focused.blockZ);
                    meta = player.worldObj.getBlockMetadata(focused.blockX, focused.blockY, focused.blockZ);
                }
                if (block == null || block.equals(Blocks.air)) {
                    proxy.addChatMessage(I18n.format("sf.sub.add.lookair"));
                    return;
                }
                break;
            default:
                block = (Block) Block.blockRegistry.getObject(cmds.get(0));
                meta = 0;
                if (block == null || block.equals(Blocks.air)) {
                    proxy.addChatMessage(I18n.format("sf.sub.add.notname"));
                    return;
                }
                break;
            }
            if (cmds.size() == 1) {
                meta = null;
            } else if (cmds.size() == 2 && "meta".equals(cmds.get(1))) {
                //
            } else if (cmds.size() == 3 && "meta".equals(cmds.get(1)) && PATTERN_NUM.matcher(cmds.get(2)).matches()) {
                meta = Integer.valueOf(cmds.get(2));
                if (meta < 0 || meta > 15) {
                    meta = 0;
                }
            } else {
                showTargetHelp("add");
            }
            proxy.sniffer.addTarget(new Target(block, meta));
            proxy.addChatMessage(I18n.format("sf.target.add.ok"));
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
                showSubList();
                break;
            case "add":
                processSubAdd(player, cmds);
                break;
            case "rm":
            case "remove":
                processSubRemove(cmds);
                break;
            default:
                showSubHelp("");
            }
        } else {
            showSubHelp("");
        }
    }

    private static void processSubAdd(EntityPlayer player, List<String> cmds) {
        if (cmds.size() >= 1) {
            if (proxy.sniffer.isActive() && proxy.sniffer.getTarget() != null) {
                Block block = null;
                Integer meta = null;
                switch (cmds.get(0)) {
                case "hold":
                    ItemStack itemStack = player.getHeldItem();
                    if (itemStack == null || itemStack.getItem() == null) {
                        proxy.addChatMessage(I18n.format("sf.sub.add.holdair"));
                        return;
                    }
                    block = Block.getBlockFromItem(itemStack.getItem());
                    meta = itemStack.getItemDamage();
                    if (block == null || block.equals(Blocks.air)) {
                        proxy.addChatMessage(I18n.format("sf.sub.add.holdair"));
                        return;
                    }
                    break;
                case "look":
                    MovingObjectPosition focused = Minecraft.getMinecraft().objectMouseOver;
                    if (focused != null && focused.typeOfHit == MovingObjectType.BLOCK) {
                        block = player.worldObj.getBlock(focused.blockX, focused.blockY, focused.blockZ);
                        meta = player.worldObj.getBlockMetadata(focused.blockX, focused.blockY, focused.blockZ);
                    }
                    if (block == null || block.equals(Blocks.air)) {
                        proxy.addChatMessage(I18n.format("sf.sub.add.lookair"));
                        return;
                    }
                    break;
                default:
                    block = (Block) Block.blockRegistry.getObject(cmds.get(0));
                    meta = 0;
                    if (block == null || block.equals(Blocks.air)) {
                        proxy.addChatMessage(I18n.format("sf.sub.add.notname"));
                        return;
                    }
                    break;
                }
                if (cmds.size() == 1) {
                    meta = null;
                } else if (cmds.size() == 2 && "meta".equals(cmds.get(1))) {
                    //
                } else if (cmds.size() == 3 && "meta".equals(cmds.get(1)) &&
                           PATTERN_NUM.matcher(cmds.get(2)).matches()) {
                    meta = Integer.valueOf(cmds.get(2));
                    if (meta < 0 || meta > 15) {
                        meta = 0;
                    }
                } else {
                    showSubHelp("add");
                }
                proxy.sniffer.getTarget().addSubTarget(new SubTarget(block, meta));
                proxy.addChatMessage(I18n.format("sf.sub.add.ok"));
            } else {
                proxy.addChatMessage(I18n.format("sf.sub.add.not"));
            }
        } else {
            showSubHelp("add");
        }
    }

    private static void processSubRemove(List<String> cmds) {

    }

    private static void showHelp() {
        proxy.addChatMessage(I18n.format("sf.help.0"));
        proxy.addChatMessage(I18n.format("sf.help.1"));
        proxy.addChatMessage(I18n.format("sf.help.2"));
        proxy.addChatMessage(I18n.format("sf.help.3"));
        proxy.addChatMessage(I18n.format("sf.help.4"));
        proxy.addChatMessage(I18n.format("sf.help.5"));
        proxy.addChatMessage(I18n.format("sf.help.0"));
    }

    private static void showTargetHelp(String cmd) {
        switch (cmd) {
        case "add":
            proxy.addChatMessage("target add help");
            break;
        case "clear":
            proxy.addChatMessage("target clear help");
            break;
        default:
            proxy.addChatMessage("target help");
        }
    }

    private static void showSubHelp(String cmd) {
        switch (cmd) {
        case "add":
            break;
        case "remove":
            break;
        default:

        }
    }

    private static void showSubList() {

    }

    @Override
    public String getCommandName() {
        return MODID;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public List<String> getCommandAliases() {
        List<String> alias = new ArrayList<String>();
        alias.add("sf");
        return alias;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(Object command) {
        return getCommandName().compareTo(((ICommand) command).getCommandName());
    }

    @Override
    public int hashCode() {
        return getCommandName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CommandSniffer;
    }

}
