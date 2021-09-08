package me.unturned.outpost.commands;

import me.unturned.outpost.Outpost;
import me.unturned.outpost.utils.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OutpostCommand implements CommandExecutor {
    private final Outpost outpost;

    public OutpostCommand(Outpost outpost) {
        this.outpost = outpost;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender.isOp())) {
            sender.sendMessage(Util.colour("&cYou do not have permission for this."));
            return false;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("setregion")) { // usage: /outpost setregion <1/2>
            final Player player = (Player) sender;

            if (args[1].equalsIgnoreCase("1")) {

                outpost.getRegion().setLocation1(player.getLocation());
                player.sendMessage(Util.colour("&bYou set the first location of the region."));
            }
            else if (args[1].equalsIgnoreCase("2")) {
                outpost.getRegion().setLocation2(player.getLocation());

                player.sendMessage(Util.colour("&bYou set the second location of the region."));
            }
            else {
                sender.sendMessage(Util.colour( "&cCorrect usage: /outpost setregion <1/2>"));
            }
            return true;
        }
        return false;
    }
}
