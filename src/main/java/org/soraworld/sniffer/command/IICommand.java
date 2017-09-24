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
import org.soraworld.sniffer.util.I19n;
import org.soraworld.sniffer.util.Lists;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

@SideOnly(Side.CLIENT)
public abstract class IICommand implements ICommand {

    private final String name;
    private final List<String> aliases;
    protected final SnifferAPI api = BlockSniffer.getAPI();
    protected final TreeMap<String, IICommand> subMap = new TreeMap<>();

    private IICommand() {
        name = "invalid";
        aliases = new ArrayList<>();
    }

    public IICommand(String name, String... aliases) {
        this.name = name;
        this.aliases = Lists.arrayList(aliases);
    }

    protected static List<String> getMatchList(String arg, Collection<String> possibles) {
        if (arg.isEmpty()) return new ArrayList<>(possibles);
        return possibles.stream().filter(s -> s.startsWith(arg)).collect(Collectors.toList());
    }

    final void addSub(IICommand sub) {
        this.subMap.put(sub.getCommandName(), sub);
        for (String alias : sub.getCommandAliases()) {
            IICommand command = this.subMap.get(alias);
            if (command == null || !command.getCommandName().equals(alias)) {
                this.subMap.put(alias, sub);
            }
        }
    }

    @Nonnull
    @Override
    public String getCommandUsage(@Nonnull ICommandSender sender) {
        return "";
    }

    @Nonnull
    @Override
    public final String getCommandName() {
        return name;
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] argArray) throws CommandException {
        execute(sender, Lists.arrayList(argArray));
    }

    @Nonnull
    @Override
    public final List<String> getCommandAliases() {
        return aliases;
    }

    @Override
    public boolean checkPermission(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender) {
        return true;
    }

    @Nonnull
    @Override
    public List<String> getTabCompletionOptions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] argArray, @Nullable BlockPos targetPos) {
        return getTabCompletions(sender, new ArrayList<>(Arrays.asList(argArray)));
    }

    public void execute(ICommandSender sender, ArrayList<String> args) {
        if (args.size() >= 1) {
            IICommand sub = subMap.get(args.remove(0));
            if (sub != null) {
                sub.execute(sender, args);
                return;
            }
        }
        I19n.sendChat2(getCommandUsage(sender));
    }

    @Override
    public boolean isUsernameIndex(@Nonnull String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(@Nonnull ICommand command) {
        return getCommandName().compareTo(command.getCommandName());
    }

    protected List<String> getTabCompletions(ICommandSender sender, ArrayList<String> args) {
        if (args.size() == 1) {
            return getMatchList(args.get(0), subMap.keySet());
        } else if (args.size() >= 2) {
            IICommand sub = subMap.get(args.remove(0));
            if (sub != null) return sub.getTabCompletions(sender, args);
            else return new ArrayList<>();
        } else {
            return new ArrayList<>(subMap.keySet());
        }
    }
}
