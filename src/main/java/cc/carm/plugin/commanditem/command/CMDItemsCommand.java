package cc.carm.plugin.commanditem.command;

import cc.carm.plugin.commanditem.item.ItemSettings;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Completer;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class CMDItemsCommand {

    @Command(
            name = "commanditem", aliases = "cmdItem",
            description = "CommandItem的主要指令。",
            usage = "/CommandItem help",
            permission = "CommandItem.admin"
    )
    public void main(Context<CommandSender> sender) {
        help(sender);
    }

    @Command(name = "commanditem.help")
    public void help(Context<CommandSender> sender) {
        sender.getSender().sendMessage("§a§lCommandItem §f§l指令帮助");
    }

    @Command(name = "commanditem.apply", target = CommandTarget.PLAYER)
    @Completer(name = "settings")
    public void apply(Context<Player> sender,
                      ItemSettings settings) {
        Player player = sender.getSender();
        if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
            ItemStack applied = settings.applyItem(player.getInventory().getItemInMainHand());
            player.getInventory().setItemInMainHand(applied);
        }
        player.sendMessage("应用成功");
    }


}
