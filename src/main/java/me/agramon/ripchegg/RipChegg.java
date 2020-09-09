package me.agramon.ripchegg;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class RipChegg {

    public static void main(String[] args) throws LoginException {
        JDA jda = JDABuilder.createDefault(Config.get("TOKEN")).build();
        jda.addEventListener(new Log());

        CommandClientBuilder ccb = new CommandClientBuilder();
        ccb.setPrefix(Config.get("PREFIX"));
        ccb.setOwnerId(Config.get("OWNERID"));
        ccb.setHelpWord("help");

        ccb.addCommand(new Help());
        ccb.addCommand(new Rip());

        CommandClient client = ccb.build();
        jda.addEventListener(client);
    }

}