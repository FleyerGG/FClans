package ru.fleyer.fclans.utils;

import ru.fleyer.fclans.FClans;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Methods {
    public static String getRankByInt(int value) {
        if (FClans.getConfiguration().getConfigurationSection("ranks") == null) {
            return null;
        }
        for (String rank : FClans.getConfiguration().getConfigurationSection("ranks").getKeys(false)) {
            if (!rank.equalsIgnoreCase(String.valueOf(value))) continue;
            return FClans.getConfiguration().getString("ranks." + value);
        }
        return null;
    }

    public static void messageToClanPlayers(String clanname, String message) {
        for (String clanPlayers : FClans.getClanManager().getPlayers(clanname).keySet()) {
            Player all = Bukkit.getPlayer(clanPlayers);
            if (all == null) continue;
            all.sendMessage(message);
        }
    }

    public static void titleToClanPlayers(String clanname, String title, String subtitle) {
        for (String clanPlayers : FClans.getClanManager().getPlayers(clanname).keySet()) {
            Player all = Bukkit.getPlayer(clanPlayers);
            if (all == null) continue;
            all.sendTitle(title, subtitle);
        }
    }

    private void lalala() {
        String[] s = new String[]{"§d§m╴                     ╴§b§m", "§6⋅ §fПлюшки§7: §a/gadget", "§6⋅ §fСемья§7: §a/family", "§6⋅ §fПитомцы§7: §a/pet", "§6⋅ §fМеню варпов§7: §a/warp", "§6ꉩ §bЛегендарное§7: §e/legacy", "§d§m                       §b§m", "§d", "§c⚠ §eДонат§7: §b/donate"};
    }
}

