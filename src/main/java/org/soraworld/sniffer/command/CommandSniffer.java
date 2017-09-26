package org.soraworld.sniffer.command;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import org.soraworld.sniffer.constant.Constants;
import org.soraworld.sniffer.util.I19n;

import java.util.ArrayList;

@SideOnly(Side.CLIENT)
public class CommandSniffer extends IICommand {

    public CommandSniffer() {
        super(Constants.MODID, "sf");
        addSub(new CommandSub());
        addSub(new CommandTarget());
        addSub(new IICommand("reload") {
            @Override
            public void execute(ICommandSender sender, ArrayList<String> args) {
                api.reload();
                I19n.sendChat("sf.reload");
            }
        });
        addSub(new IICommand("reset") {
            @Override
            public void execute(ICommandSender sender, ArrayList<String> args) {
                api.reset();
                I19n.sendChat("sf.reset");
            }
        });
        addSub(new IICommand("gamma", "g") {
            @Override
            public void execute(ICommandSender sender, ArrayList<String> args) {
                if (args.isEmpty()) {
                    I19n.sendChat("sf.gamma.get", api.getGamma());
                    return;
                }
                if (Constants.PATTERN_NUM.matcher(args.get(0)).matches()) {
                    api.setGamma(Integer.valueOf(args.get(0)));
                    I19n.sendChat("sf.gamma.set", api.getGamma());
                } else {
                    I19n.sendChat("sf.invalid.num");
                }
            }
        });
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return I18n.format("sf.help");
    }

    @Override
    public void execute(ICommandSender sender, ArrayList<String> args) {
        if (sender instanceof EntityPlayer) {
            super.execute(sender, args);
        } else {
            api.LOGGER.info(I18n.format("sf.cmd.error"));
            sender.addChatMessage(new ChatComponentText(I18n.format("sf.cmd.error")));
        }
    }
}
