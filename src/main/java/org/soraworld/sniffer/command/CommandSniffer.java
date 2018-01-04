package org.soraworld.sniffer.command;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.soraworld.sniffer.constant.Constants;
import org.soraworld.sniffer.util.I19n;

import javax.annotation.Nonnull;
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
        addSub(new IICommand("setblock") {
            @Override
            public void execute(ICommandSender sender, ArrayList<String> args) {
                if (sender instanceof EntityPlayerSP) {
                    if (args.size() == 2 && args.get(0).equals("look")) {
                        Block block = Block.getBlockFromName(args.get(1));
                        if (block != null) {
                            RayTraceResult focused = Minecraft.getMinecraft().objectMouseOver;
                            if (focused != null && focused.typeOfHit == RayTraceResult.Type.BLOCK) {
                                sender.getEntityWorld().setBlockState(focused.getBlockPos(), block.getDefaultState());
                            }
                        }
                    }
                    if (args.size() == 4) {
                        try {
                            int x = Integer.valueOf(args.get(0));
                            int y = Integer.valueOf(args.get(1));
                            int z = Integer.valueOf(args.get(2));
                            Block block = Block.getBlockFromName(args.get(3));
                            if (block != null) {
                                sender.getEntityWorld().setBlockState(new BlockPos(x, y, z), block.getDefaultState());
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return I18n.format("sf.help");
    }

    @Override
    public void execute(ICommandSender sender, ArrayList<String> args) {
        if (sender instanceof EntityPlayer) {
            super.execute(sender, args);
        } else {
            api.LOGGER.info(I18n.format("sf.cmd.error"));
            sender.sendMessage(new TextComponentString(I18n.format("sf.cmd.error")));
        }
    }
}
