package ru.fleyer.fclans.utils;

import java.util.List;
import org.bukkit.entity.Player;

public class ClanWarsManager {
    private String color;
    private List<Player> players;

    public ClanWarsManager(String color, List<Player> players) {
        this.color = color;
        this.players = players;
    }

    public String getColor() {
        return this.color;
    }

    public List<Player> getPlayers() {
        return this.players;
    }
}

