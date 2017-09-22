package org.soraworld.sniffer.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.soraworld.sniffer.BlockSniffer;
import org.soraworld.sniffer.api.SnifferAPI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

@SideOnly(Side.CLIENT)
public abstract class IICommand implements ICommand {

    protected final SnifferAPI api = BlockSniffer.getAPI();
    protected final HashMap<String, IICommand> subMap = new HashMap<>();

    public IICommand addSub(IICommand sub) {
        this.subMap.put(sub.getName(), sub);
        for (String alias : sub.getAliases()) {
            IICommand command = this.subMap.get(alias);
            if (command == null || !command.getName().equals(alias)) {
                this.subMap.put(alias, sub);
            }
        }
        return this;
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "";
    }

    @Nonnull
    @Override
    public List<String> getAliases() {
        return Collections.emptyList();
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] argArray) throws CommandException {
        execute(sender, new ArrayList<>(Arrays.asList(argArray)));
    }

    public void execute(ICommandSender sender, ArrayList<String> args) {
        if (args.isEmpty()) return;
        String alias = args.get(0);
        args.remove(0);
        IICommand sub = subMap.get(alias);
        if (sub != null) sub.execute(sender, args);
    }

    @Override
    public boolean checkPermission(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender) {
        return true;
    }

    @Nonnull
    @Override
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] argArray, @Nullable BlockPos targetPos) {
        return getTabCompletions(sender, new ArrayList<>(Arrays.asList(argArray)));
    }

    public List<String> getTabCompletions(ICommandSender sender, ArrayList<String> args) {
        return new ArrayList<>(subMap.keySet());
    }

    @Override
    public boolean isUsernameIndex(@Nonnull String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(@Nonnull ICommand command) {
        return getName().compareTo(command.getName());
    }
}
