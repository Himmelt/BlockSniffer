package org.soraworld.sniffer.command;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.soraworld.sniffer.constant.Constants;
import org.soraworld.sniffer.core.TBlock;
import org.soraworld.sniffer.util.I19n;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class CommandSub extends IICommand {

    public CommandSub() {
        super("sub");
        addSub(new CommandAdd(this));
        addSub(new IICommand("list", "l") {
            @Override
            public void execute(ICommandSender sender, ArrayList<String> args) {
                showSubList(0);
            }
        });
        addSub(new IICommand("remove", "rm") {
            @Override
            public void execute(ICommandSender sender, ArrayList<String> args) {
                if (args.size() >= 1) {
                    if (Constants.PATTERN_NUM.matcher(args.get(0)).matches()) {
                        int uid = Integer.valueOf(args.get(0));
                        api.current.removeBlock(uid);
                    } else {
                        I19n.sendChat("sf.invalid.num");
                    }
                } else {
                    showSubList(1);
                }
            }
        });
    }

    @Override
    public void execute(ICommandSender sender, ArrayList<String> args) {
        if (api.active && api.current != null) super.execute(sender, args);
        else I19n.sendChat("sf.target.not");
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return I18n.format("sf.help.sub");
    }

    private void showSubList(int way) {
        StringBuilder list = new StringBuilder();
        Map<Integer, TBlock> map = api.current.getBlocks();
        for (Map.Entry<Integer, TBlock> entry : map.entrySet()) {
            list.append(entry.getKey()).append(" -> ").append(entry.getValue().getName()).append("; ");
        }
        I19n.sendChat(way == 0 ? "sf.sub.list" : "sf.sub.rm.list");
        I19n.sendChat(list.toString());
    }
}
