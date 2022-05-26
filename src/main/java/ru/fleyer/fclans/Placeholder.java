package ru.fleyer.fclans;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Placeholder extends PlaceholderExpansion {
    private Plugin p;

    public Placeholder(Plugin p) {
        this.p = p;
    }

    @Override
    public String getAuthor() {
        return "Fleyer001";
    }

    @Override
    public String getIdentifier() {
        return "fclans";
    }


    @Override
    public String getVersion() {
        return p.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player p, String id) {
        if (p == null || !p.isOnline()) {
            return null;
        }
        if (id.equals("name")) {
            return String.valueOf(FClans.getClanManager().playersClan.get(p.getName()));
        }

        return null;
    }
}