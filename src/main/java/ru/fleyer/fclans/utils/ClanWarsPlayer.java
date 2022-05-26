package ru.fleyer.fclans.utils;

import org.bukkit.inventory.ItemStack;

public class ClanWarsPlayer {
    private String clanname;
    private ItemStack[] inventory;
    private ItemStack[] armor;

    public ClanWarsPlayer(String clanname, ItemStack[] inventory, ItemStack[] armor) {
        this.clanname = clanname;
        this.inventory = inventory;
        this.armor = armor;
    }

    public String getClanname() {
        return this.clanname;
    }

    public ItemStack[] getInventory() {
        return this.inventory;
    }

    public ItemStack[] getArmor() {
        return this.armor;
    }
}

