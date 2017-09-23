package org.soraworld.sniffer.command;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.soraworld.sniffer.constant.ColorHelper;
import org.soraworld.sniffer.constant.Constants;
import org.soraworld.sniffer.core.Target;
import org.soraworld.sniffer.util.I19n;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;

@SideOnly(Side.CLIENT)
public class CommandTarget extends IICommand {

    public CommandTarget() {
        super("target", "t");
        addSub(new CommandAdd(this));
        addSub(new IICommand("info", "i") {
            @Override
            public void execute(ICommandSender sender, ArrayList<String> args) {
                I19n.sendChat("sf.target.info", api.current.getMode(), api.current.getChatColor(),
                        api.current.getDepthL(), api.current.getDepthH(), api.current.getHRange(), api.current.getVRange());
            }
        });
        addSub(new IICommand("mode", "m") {
            @Override
            public void execute(ICommandSender sender, ArrayList<String> args) {
                if (args.isEmpty()) {
                    int m = api.current.getMode();
                    String mode = I18n.format(m == 0 ? "sf.mode.0" : "sf.mode.1");
                    I19n.sendChat("sf.target.m.get", mode);
                } else {
                    if ("1".equals(args.get(0))) {
                        api.current.setMode(1);
                        I19n.sendChat("sf.target.m.set", I18n.format("sf.mode.1"));
                    } else {
                        api.current.setMode(0);
                        I19n.sendChat("sf.target.m.set", I18n.format("sf.mode.0"));
                    }
                }
            }
        });
        addSub(new IICommand("hrange", "h") {
            @Override
            public void execute(ICommandSender sender, ArrayList<String> args) {
                if (args.isEmpty()) {
                    I19n.sendChat("sf.target.h.get", api.current.getHRange());
                } else {
                    if (Constants.PATTERN_NUM.matcher(args.get(0)).matches()) {
                        api.current.setHRange(Integer.valueOf(args.get(0)));
                        I19n.sendChat("sf.target.h.set", api.current.getHRange());
                    } else {
                        I19n.sendChat("sf.invalid.num");
                    }
                }
            }
        });
        addSub(new IICommand("vrange", "v") {
            @Override
            public void execute(ICommandSender sender, ArrayList<String> args) {
                if (args.isEmpty()) {
                    I19n.sendChat("sf.target.v.get", api.current.getVRange());
                } else {
                    if (Constants.PATTERN_NUM.matcher(args.get(0)).matches()) {
                        api.current.setVRange(Integer.valueOf(args.get(0)));
                        I19n.sendChat("sf.target.v.set", api.current.getVRange());
                    } else {
                        I19n.sendChat("sf.invalid.num");
                    }
                }
            }
        });
        addSub(new IICommand("depth", "d") {
            @Override
            public void execute(ICommandSender sender, ArrayList<String> args) {
                if (args.isEmpty()) {
                    I19n.sendChat("sf.target.d.get", api.current.getDepthL(), api.current.getDepthH());
                } else if (args.size() >= 2) {
                    if (Constants.PATTERN_NUM.matcher(args.get(0)).matches() && Constants.PATTERN_NUM.matcher(args.get(1)).matches()) {
                        api.current.setDepth(Integer.valueOf(args.get(0)), Integer.valueOf(args.get(1)));
                        I19n.sendChat("sf.target.d.set", api.current.getDepthL(), api.current.getDepthH());
                    } else {
                        I19n.sendChat("sf.invalid.num");
                    }
                } else {
                    I19n.sendChat("sf.invalid.num");
                }
            }
        });
        addSub(new IICommand("color", "c") {
            @Override
            public void execute(ICommandSender sender, ArrayList<String> args) {
                if (args.isEmpty()) {
                    I19n.sendChat("sf.target.c.get", api.current.getChatColor());
                } else {
                    String value = args.get(0);
                    if ("map".equals(value)) {
                        api.current.setColor(value);
                        I19n.sendChat("sf.target.c.map");
                    } else {
                        Color color = ColorHelper.getColor(value);
                        if (color != null) {
                            api.current.setColor(value);
                            I19n.sendChat("sf.target.c.set", api.current.getChatColor());
                        } else {
                            I19n.sendChat("sf.invalid.color");
                        }
                    }
                }
            }
        });
        addSub(new IICommand("clear", "cla") {
            @Override
            public void execute(ICommandSender sender, ArrayList<String> args) {
                if (args.isEmpty()) {
                    I19n.sendChat("sf.target.cla.hint");
                } else {
                    if ("confirm".equals(args.get(0))) {
                        api.clearTargets();
                    } else {
                        I19n.sendChat("sf.help.target.cla");
                    }
                }
            }
        });
        addSub(new IICommand("remove", "rm") {
            @Override
            public void execute(ICommandSender sender, ArrayList<String> args) {
                api.removeTarget();
            }
        });
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return I18n.format("sf.help.target");
    }

    @Override
    public void execute(ICommandSender sender, ArrayList<String> args) {
        if (sender instanceof EntityPlayer) {
            if (args.isEmpty()) {
                I19n.sendChat("sf.help.target");
                return;
            }
            String alias = args.get(0);
            args.remove(0);
            Target target = api.current;
            if (api.active && target != null) {
                IICommand sub = subMap.get(alias);
                if (sub != null) {
                    sub.execute(sender, args);
                }
            } else {
                switch (alias) {
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
                        subMap.get("add").execute(sender, args);
                        break;
                    default:
                        I19n.sendChat("sf.help.target");
                }
            }
        } else {
            I19n.sendChat("sf.cmd.error");
        }
    }
}
