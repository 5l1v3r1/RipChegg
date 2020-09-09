package me.agramon.ripchegg;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Help extends Command {

    public Help() {
        super.name = "help";
        super.help = "List of RipChegg commands";
    }

    @Override
    protected void execute(CommandEvent e) {
    }
}
