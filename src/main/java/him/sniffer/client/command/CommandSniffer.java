package him.sniffer.client.command;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import him.sniffer.constant.ModInfo;
import him.sniffer.core.SubTarget;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static him.sniffer.Sniffer.*;

@SideOnly(Side.CLIENT)
public class CommandSniffer implements ICommand {
    private static final Pattern PATTERN_NUM = Pattern.compile("[0-9]{1,2}");

    @Override
    public String getCommandName() {
        return ModInfo.MODID;
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
                    showVersion();
                    break;
                case "s":
                case "save":
                    proxy.config.save();
                    proxy.addChatMessage(I18n.format("sniffer.cfg.save"));
                    break;
                case "reload":
                    proxy.config.reload();
                    proxy.addChatMessage(I18n.format("sniffer.cfg.reload"));
                    break;
                case "reset":
                    proxy.sniffer.reset();
                    proxy.addChatMessage(I18n.format("sniffer.chat.reset"));
                    break;
                case "off":
                    proxy.sniffer.inActive();
                    break;
                case "target":
                    processTarget(cmds);
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
            logger.info(I18n.format("sniffer.cmd.err"));
            sender.addChatMessage(new ChatComponentText(I18n.format("sniffer.cmd.err")));
        }
    }

    private void processSub(EntityPlayer player, List<String> cmds) {
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

    private void processSubRemove(List<String> cmds) {

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

    private void showSubList() {

    }

    private void processTarget(List<String> cmds) {
        if (cmds.size() >= 1) {

        } else {
            showTargetHelp();
        }
    }

    private static void showVersion() {
        proxy.addChatMessage(I18n.format("sniffer.chat.version", ModInfo.VERSION));
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

    private void showTargetHelp() {

    }

    private static void showHelp() {
        proxy.addChatMessage(I18n.format("sniffer.help.0"));
        proxy.addChatMessage(I18n.format("sniffer.help.1"));
        proxy.addChatMessage(I18n.format("sniffer.help.2"));
        proxy.addChatMessage(I18n.format("sniffer.help.3"));
        proxy.addChatMessage(I18n.format("sniffer.help.4"));
        proxy.addChatMessage(I18n.format("sniffer.help.5"));
        proxy.addChatMessage(I18n.format("sniffer.help.0"));
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
        return compareTo((ICommand) command);
    }

    @Override
    public int hashCode() {
        return getCommandName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CommandSniffer;
    }

    public int compareTo(ICommand command) {
        return getCommandName().compareTo(command.getCommandName());
    }
}
