package org.soraworld.sniffer.command;

import java.util.ArrayList;
import java.util.List;

public class CommandTarget implements ISubCommand {

    @Override
    public String name() {
        return null;
    }

    @Override
    public List<String> aliases() {
        return null;
    }

    @Override
    public void execute(ArrayList<String> args) {

    }

    @Override
    public List<String> tabCompletions(ArrayList<String> args) {
        return null;
    }
}
