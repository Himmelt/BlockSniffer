package org.soraworld.sniffer.command;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.soraworld.sniffer.constant.ColorHelper;
import org.soraworld.sniffer.constant.Constants;
import org.soraworld.sniffer.core.TBlock;
import org.soraworld.sniffer.core.Target;
import org.soraworld.sniffer.util.I19n;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public class CommandTarget extends IICommand {

    @Nonnull
    @Override
    public String getName() {
        return "target";
    }

    @Nonnull
    @Override
    public List<String> getAliases() {
        return Collections.singletonList("t");
    }

    @Override
    public void execute(ICommandSender sender, ArrayList<String> args) {
        if (sender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) sender;
            if (!api.active || api.current == null) {
                I19n.sendChat("sf.target.not");
                return;
            }
            if (args.isEmpty()) {
                I19n.sendChat("sf.help.target");
                return;
            }
            String alias = args.get(0);
            args.remove(0);
            Target target = api.current;
            if (api.active && target != null) {
                switch (alias) {
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
                        processTargetAdd(player, args);
                        break;
                    default:
                        I19n.sendChat("sf.help.target");
                }
            }
        } else {
            I19n.sendChat("sf.cmd.error");
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
}
