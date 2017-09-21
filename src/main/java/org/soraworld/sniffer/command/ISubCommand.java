package org.soraworld.sniffer.command;

import java.util.ArrayList;
import java.util.List;

public interface ISubCommand {

    String name();

    List<String> aliases();

    void execute(ArrayList<String> args);

    List<String> tabCompletions(ArrayList<String> args);
}
