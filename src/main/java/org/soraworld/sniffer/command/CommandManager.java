package org.soraworld.sniffer.command;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {

    public static final CommandManager INSTANCE = new CommandManager();
    private final Map<String, ISubCommand> commandMap = new HashMap<>();

    public void registerCommand(ISubCommand command) {
        this.commandMap.put(command.name(), command);
        for (String alias : command.aliases()) {
            ISubCommand subCommand = this.commandMap.get(alias);
            if (subCommand == null || !subCommand.name().equals(alias)) {
                this.commandMap.put(alias, command);
            }
        }
    }

    ISubCommand getCommand(String name) {
        return commandMap.get(name);
    }
}
