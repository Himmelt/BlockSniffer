package org.soraworld.sniffer.command;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.soraworld.sniffer.constant.Constants;
import org.soraworld.sniffer.util.I19n;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public class CommandSniffer extends IICommand {

    public CommandSniffer() {
        addSub(new CommandTarget());
        addSub(new CommandSub());
        addSub(new IICommand() {
            @Nonnull
            @Override
            public String getName() {
                return "reload";
            }

            @Override
            public void execute(ICommandSender sender, ArrayList<String> args) {
                api.reload();
                I19n.sendChat("sf.reload");
            }
        });
        addSub(new IICommand() {
            @Nonnull
            @Override
            public String getName() {
                return "reset";
            }

            @Override
            public void execute(ICommandSender sender, ArrayList<String> args) {
                api.reset();
                I19n.sendChat("sf.reset");
            }
        });
        addSub(new IICommand() {
            @Nonnull
            @Override
            public String getName() {
                return "gamma";
            }

            @Nonnull
            @Override
            public List<String> getAliases() {
                return Collections.singletonList("g");
            }

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

    @Nonnull
    @Override
    public String getName() {
        return Constants.MODID;
    }

    @Nonnull
    @Override
    public List<String> getAliases() {
        return Collections.singletonList("sf");
    }

    @Override
    public void execute(ICommandSender sender, ArrayList<String> args) {
        if (sender instanceof EntityPlayer) {
            if (args.isEmpty()) {
                I19n.sendChat("sf.help");
                return;
            }
            String alias = args.get(0);
            args.remove(0);
            IICommand sub = subMap.get(alias);
            if (sub != null) sub.execute(sender, args);
            else I19n.sendChat("sf.help");

            /*switch (argArray[0]) {
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
                    processTarget((EntityPlayer) sender, args);
                    break;
                case "sub":
                    if (api.active && api.current != null) {
                        processSub((EntityPlayer) sender, args);
                    } else {
                        I19n.sendChat("sf.target.not");
                    }
                    break;
                default:
                    I19n.sendChat("sf.help");
            }*/
        } else {
            api.LOGGER.info(I18n.format("sf.cmd.error"));
            sender.sendMessage(new TextComponentString(I18n.format("sf.cmd.error")));
        }
    }
}
