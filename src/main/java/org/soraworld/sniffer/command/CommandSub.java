package org.soraworld.sniffer.command;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.soraworld.sniffer.util.I19n;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public class CommandSub implements ISubCommand {
    @Override
    public String name() {
        return "sub";
    }

    @Override
    public List<String> aliases() {
        return Collections.emptyList();
    }

    @Override
    public void execute(ArrayList<String> args) {
        I19n.sendChat("execute CommandSub");
    }

    @Override
    public List<String> tabCompletions(ArrayList<String> args) {
        return null;
    }
}
