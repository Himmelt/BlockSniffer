package org.soraworld.sniffer.command;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.soraworld.sniffer.util.I19n;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public class CommandTarget implements ISubCommand {

    @Override
    public String name() {
        return "target";
    }

    @Override
    public List<String> aliases() {
        return Collections.singletonList("t");
    }

    @Override
    public void execute(ArrayList<String> args) {
        I19n.sendChat("execute CommandTarget");
    }

    @Override
    public List<String> tabCompletions(ArrayList<String> args) {
        return Collections.emptyList();
    }
}
