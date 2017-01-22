package him.sniffer.client.command;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import him.sniffer.constant.ModInfo;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

import static him.sniffer.Sniffer.*;

@SideOnly(Side.CLIENT)
public class CommandSniffer implements ICommand {
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
        if (sender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) sender;
            World world = player.getEntityWorld();
            if (world instanceof WorldClient) {
                int length = args.length;
                if (length >= 1) {
                    String sub = args[0];
                    if ("help".equals(sub)) {
                        showHelp();
                    } else if ("target".equals(sub)) {
                        if (length == 1) {
                            showTargetHelp();
                        }
                        proxy.addChatMessage("target is executed");
                    } else if ("sub".equals(sub)) {
                        if (length == 1) {
                            showSubHelp();
                        }
                        proxy.addChatMessage("sub is executed");
                    } else if ("save".equals(sub)) {
                        proxy.config.save();
                        proxy.addChatMessage(I18n.format("sniffer.cfg.save"));
                    } else if ("reload".equals(sub)) {
                        proxy.config.reload();
                        proxy.addChatMessage(I18n.format("sniffer.cfg.reload"));
                    } else {
                        proxy.addChatMessage("No this sub command,see the usage!");
                        showHelp();
                    }
                } else {
                    showHelp();
                }
            } else {
                logger.info(I18n.format("sniffer.log.worlderr"));
                proxy.addChatMessage(I18n.format("sniffer.log.worlderr"));
            }
        } else {
            logger.info(I18n.format("sniffer.log.notplayer"));
            sender.addChatMessage(new ChatComponentText(I18n.format("sniffer.log.notplayer")));
        }
    }

    private void showSubHelp() {

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
