package org.soraworld.sniffer.command;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.ICommandSender;
import org.soraworld.sniffer.constant.Constants;
import org.soraworld.sniffer.util.ColorHelper;
import org.soraworld.sniffer.util.I19n;
import org.soraworld.sniffer.util.Lists;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class CommandTarget extends IICommand {

    public CommandTarget() {
        super("target", "t");
        addSub(new CommandAdd(this));
        addSub(new IICommand("name", "n") {
            @Override
            public void execute(ICommandSender sender, ArrayList<String> args) {
                if (api.active && api.current != null) {
                    if (args.isEmpty()) {
                        I19n.sendChat("sf.target.n.get", api.current.getDisplay());
                    } else {
                        if (args.get(0).equals("null")) {
                            api.current.setDisplay(null);
                            I19n.sendChat("sf.target.n.nul");
                        } else {
                            api.current.setDisplay(args.get(0));
                            I19n.sendChat("sf.target.n.set", api.current.getDisplay());
                        }
                    }
                } else {
                    I19n.sendChat("sf.target.not");
                }
            }

            @Override
            protected List<String> getTabCompletions(ICommandSender sender, ArrayList<String> args) {
                return Lists.arrayList("null");
            }
        });
        addSub(new IICommand("info", "i") {
            @Override
            public void execute(ICommandSender sender, ArrayList<String> args) {
                if (api.active && api.current != null) {
                    I19n.sendChat("sf.target.info", api.current.getMode(), api.current.getChatColor(),
                            api.current.getDepthL(), api.current.getDepthH(), api.current.getHRange(), api.current.getVRange());
                } else {
                    I19n.sendChat("sf.target.not");
                }
            }
        });
        addSub(new IICommand("mode", "m") {
            @Override
            public void execute(ICommandSender sender, ArrayList<String> args) {
                if (api.active && api.current != null) {
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
                } else {
                    I19n.sendChat("sf.target.not");
                }
            }
        });
        addSub(new IICommand("hrange", "h") {
            @Override
            public void execute(ICommandSender sender, ArrayList<String> args) {
                if (api.active && api.current != null) {
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
                } else {
                    I19n.sendChat("sf.target.not");
                }
            }
        });
        addSub(new IICommand("vrange", "v") {
            @Override
            public void execute(ICommandSender sender, ArrayList<String> args) {
                if (api.active && api.current != null) {
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
                } else {
                    I19n.sendChat("sf.target.not");
                }
            }
        });
        addSub(new IICommand("depth", "d") {
            @Override
            public void execute(ICommandSender sender, ArrayList<String> args) {
                if (api.active && api.current != null) {
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
                } else {
                    I19n.sendChat("sf.target.not");
                }
            }
        });
        addSub(new IICommand("color", "c") {
            @Override
            public void execute(ICommandSender sender, ArrayList<String> args) {
                if (api.active && api.current != null) {
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
                } else {
                    I19n.sendChat("sf.target.not");
                }
            }

            @Override
            protected List<String> getTabCompletions(ICommandSender sender, ArrayList<String> args) {
                if (args.size() >= 1) return getMatchList(args.get(0), ColorHelper.getNames());
                return ColorHelper.getNames();
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
                if (api.active && api.current != null) {
                    api.removeTarget();
                } else {
                    I19n.sendChat("sf.target.not");
                }
            }
        });
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return I18n.format("sf.help.target");
    }
}
