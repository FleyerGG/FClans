package ru.fleyer.fclans;

import java.util.ArrayList;
import java.util.List;
import ru.fleyer.fclans.FClans;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClanAdmin implements CommandExecutor {
    public static List<String> locationsRED;
    public static List<String> locationsBLUE;
    public static String locationsREDwait;
    public static String locationsBLUEwait;

    static void loadLocs() {
        if (FClans.getConfiguration().getString("spawn arena locations") == null) {
            locationsRED = new ArrayList<String>();
            locationsBLUE = new ArrayList<String>();
            locationsREDwait = null;
            locationsBLUEwait = null;
            return;
        }
        locationsRED = FClans.getConfiguration().getStringList("spawn arena locations.red");
        locationsBLUE = FClans.getConfiguration().getStringList("spawn arena locations.blue");
        locationsREDwait = FClans.getConfiguration().getString("spawn arena locations.redwait");
        locationsBLUEwait = FClans.getConfiguration().getString("spawn arena locations.bluewait");
    }

    private static String locToString(Location loc) {
        return String.format("%s;%s;%s;%s;%s;%s", loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ(), Float.valueOf(loc.getYaw()), Float.valueOf(loc.getPitch()));
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player)sender;
        if (!player.hasPermission("fclans.admin")) {
            player.sendMessage(" §c▷ §fВы не являетесь администратором!");
            return true;
        }
        if (args.length == 0) {
            player.sendMessage(" §c▷ §fПоставить точку спавна команды §c§lRED §7- §a/ca red");
            player.sendMessage(" §c▷ §fПоставить точку спавна команды §1§lBLUE §7- §a/ca blue");
            player.sendMessage(" §c▷ §fПоставить точку спавна трибун команды §c§lRED §7- §a/ca redwait");
            player.sendMessage(" §c▷ §fПоставить точку спавна трибун команды §1§lBLUE §7- §a/ca bluewait");
            return true;
        }
        if (args[0].equalsIgnoreCase("red")) {
            if (locationsRED.size() >= 5) {
                player.sendMessage(" §c▷ §fНельзя установить больше 5и точек спавна!");
                player.sendMessage(" §c▷ §fУдалите локации из конфига, чтобы продолжить");
                return true;
            }
            locationsRED.add(ClanAdmin.locToString(player.getLocation()));
            FClans.getConfiguration().set("spawn arena locations.red", locationsRED);
            FClans.getInstance().saveConfig();
            player.sendMessage(" §c▷ §fЛокация спавна арены для команды §c§lRED §fустановлена!");
            return true;
        }
        if (args[0].equalsIgnoreCase("blue")) {
            if (locationsBLUE.size() >= 5) {
                player.sendMessage(" §c▷ §fНельзя установить больше 5и точек спавна!");
                player.sendMessage(" §c▷ §fУдалите локации из конфига, чтобы продолжить");
                return true;
            }
            locationsBLUE.add(ClanAdmin.locToString(player.getLocation()));
            FClans.getConfiguration().set("spawn arena locations.blue", locationsBLUE);
            FClans.getInstance().saveConfig();
            player.sendMessage(" §c▷ §fЛокация спавна арены для команды §1§lBLUE §fустановлена!");
            return true;
        }
        if (args[0].equalsIgnoreCase("redwait")) {
            locationsREDwait = ClanAdmin.locToString(player.getLocation());
            FClans.getConfiguration().set("spawn arena locations.redwait", ClanAdmin.locToString(player.getLocation()));
            FClans.getInstance().saveConfig();
            player.sendMessage(" §c▷ §fЛокация спавна трибун для команды §c§lRED §fустановлена!");
        } else if (args[0].equalsIgnoreCase("bluewait")) {
            locationsBLUEwait = ClanAdmin.locToString(player.getLocation());
            FClans.getConfiguration().set("spawn arena locations.bluewait", ClanAdmin.locToString(player.getLocation()));
            FClans.getInstance().saveConfig();
            player.sendMessage(" §c▷ §fЛокация спавна трибун для команды §1§lBLUE §fустановлена!");
        } else {
            player.sendMessage(" §c▷ §fПоставить точку спавна команды §c§lRED §7- §a/ca red");
            player.sendMessage(" §c▷ §fПоставить точку спавна команды §1§lBLUE §7- §a/ca blue");
        }
        return true;
    }
}

