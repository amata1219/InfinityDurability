package amata1219.infinity.durability;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class InfinityDurability extends JavaPlugin implements Listener, CommandExecutor {
    private List<Material> list = new ArrayList();

    public InfinityDurability() {
    }

    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("idcheck").setExecutor(this);
        this.saveDefaultConfig();
        this.getConfig().getStringList("Material").forEach((s) -> {
            this.list.add(Material.valueOf(s));
        });
    }

    public void onDisable() {
        HandlerList.unregisterAll((Plugin) this);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        } else if (args.length == 0) {
            ItemStack item = ((Player)sender).getInventory().getItemInMainHand();
            if (item != null && item.getType() != Material.AIR) {
                sender.sendMessage(ChatColor.GRAY + "(Material: " + item.getType().name() + ")");
                return true;
            } else {
                return true;
            }
        } else if (args[0].equalsIgnoreCase("reload")) {
            this.reloadConfig();
            this.list.clear();
            this.getConfig().getStringList("Material").forEach((s) -> {
                this.list.add(Material.valueOf(s));
            });
            sender.sendMessage(ChatColor.AQUA + "コンフィグをリロードしました。");
            return true;
        } else {
            return true;
        }
    }

    @EventHandler
    public void onDmage(PlayerItemDamageEvent e) {
        if (e.getItem() != null && e.getItem().getType() != Material.AIR) {
            if (this.list.contains(e.getItem().getType())) {
                e.setCancelled(true);
            }

        }
    }
}
