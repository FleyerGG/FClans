package ru.fleyer.fclans.utils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import ru.fleyer.fclans.FClans;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class Menu {
    public static void playerUpRankMenu(Player player) {
        int i;
        Inventory inventory = Bukkit.createInventory(player, 54, "§8§lНастройка ранга");
        ItemStack panecyan = new ItemStack(Material.GREEN_STAINED_GLASS, 1);
        for (i = 0; i < 9; ++i) {
            inventory.setItem(i, panecyan);
        }
        for (i = 46; i < 54; ++i) {
            inventory.setItem(i, panecyan);
        }
        inventory.setItem(9, panecyan);
        inventory.setItem(18, panecyan);
        inventory.setItem(27, panecyan);
        inventory.setItem(36, panecyan);
        inventory.setItem(45, panecyan);
        inventory.setItem(17, panecyan);
        inventory.setItem(26, panecyan);
        inventory.setItem(35, panecyan);
        inventory.setItem(44, panecyan);
        inventory.setItem(53, panecyan);
        Material[] materials = new Material[]{Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.DIAMOND_SWORD, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.DIAMOND_CHESTPLATE};
        String[] names = new String[]{"§c1 ранг", "§c2 ранг", "§c3 ранг", "§c4 ранг", "§c5 ранг", "§c6 ранг", "§c7 ранг", "§c8 ранг"};
        String[][] lore = new String[][]{{"§7Назначить игрока §c" + Methods.getRankByInt(1)}, {"§7Назначить игрока §c" + Methods.getRankByInt(2)}, {"§7Назначить игрока §c" + Methods.getRankByInt(3)}, {"§7Назначить игрока §c" + Methods.getRankByInt(4)}, {"§7Назначить игрока §c" + Methods.getRankByInt(5)}, {"§7Назначить игрока §c" + Methods.getRankByInt(6)}, {"§7Назначить игрока §c" + Methods.getRankByInt(7)}, {"§7Назначить игрока §c" + Methods.getRankByInt(8)}};
        int[] positions = new int[]{20, 21, 22, 23, 24, 30, 31, 32};
        for (int i2 = 0; i2 < 8; ++i2) {
            ItemStack item = new ItemStack(materials[i2]);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(names[i2]);
            meta.setLore(Arrays.asList(lore[i2]));
            item.setItemMeta(meta);
            inventory.setItem(positions[i2], item);
        }
        player.openInventory(inventory);
    }

    public static void playerMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 27, "§8§lУправление игроком");
        ItemStack panecyan = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE, 1);
        for (int i = 0; i < 27; ++i) {
            inventory.setItem(i, panecyan);
        }
        Material[] material = new Material[]{Material.BARRIER, Material.NAME_TAG};
        String[] names = new String[]{"§cИсключить игрока из клана", "§aНастроить ранг игрока"};
        int[] positions = new int[]{11, 15};
        for (int i = 0; i < 2; ++i) {
            ItemStack item = new ItemStack(material[i]);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(names[i]);
            item.setItemMeta(meta);
            inventory.setItem(positions[i], item);
        }
        player.openInventory(inventory);
    }


    public static void controlMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 27, " §8▪ §0Управление кланом");
        ItemStack panecyan = new ItemStack(Material.BROWN_STAINED_GLASS_PANE, 1);
        for (int i = 0; i < 27; ++i) {
            inventory.setItem(i, panecyan);
        }
        Material[] materials = new Material[]{Material.BARRIER, Material.BONE, Material.BONE, Material.BONE};
        String[] names = new String[]{" §8▪ §aУдалить клан", "§eСкоро...", "§eСкоро...", "§eСкоро..."};
        String[][] lore = new String[][]{{"§c§lВНИМАНИЕ! §r§fПосле §cудаления §fклана", "§fВесь §cрейтинг §fи §cденьги §fпропадут безвозвратно!"}, {"§eСкоро..."}, {"§eСкоро..."}, {"§eСкоро..."}};
        int[] positions = new int[]{10, 12, 14, 16};
        for (int i = 0; i < 4; ++i) {
            ItemStack item = new ItemStack(materials[i]);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(names[i]);
            meta.setLore(Arrays.asList(lore[i]));
            item.setItemMeta(meta);
            inventory.setItem(positions[i], item);
        }
        player.openInventory(inventory);
    }

    public static void clanMenu(Player player, String clanname) {
        int i;
        Inventory inventory = Bukkit.createInventory(player, 54, "§8§lМеню клана");
        ItemStack panegray = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
        ItemStack panecyan = new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE, 1);
        for (i = 0; i < 7; ++i) {
            inventory.setItem(i, panegray);
        }
        for (i = 46; i < 53; ++i) {
            inventory.setItem(i, panegray);
        }
        inventory.setItem(9, panegray);
        inventory.setItem(18, panegray);
        inventory.setItem(27, panegray);
        inventory.setItem(36, panegray);
        inventory.setItem(45, panegray);
        inventory.setItem(15, panegray);
        inventory.setItem(24, panegray);
        inventory.setItem(33, panegray);
        inventory.setItem(42, panegray);
        inventory.setItem(51, panegray);
        inventory.setItem(7, panecyan);
        inventory.setItem(8, panecyan);
        inventory.setItem(16, panecyan);
        inventory.setItem(17, panecyan);
        inventory.setItem(25, panecyan);
        inventory.setItem(26, panecyan);
        inventory.setItem(34, panecyan);
        inventory.setItem(35, panecyan);
        inventory.setItem(43, panecyan);
        inventory.setItem(44, panecyan);
        inventory.setItem(52, panecyan);
        inventory.setItem(53, panecyan);
        Material[] materials = new Material[]{Material.LEGACY_REDSTONE_COMPARATOR, Material.FEATHER};
        String[] names = new String[8];
        names[0] = " §8▪ §aУправление кланом";
        names[1] = " §8▪ §aИнформация о клане";
        String[][] lore = new String[][]{{"§fНажмите сюда,", "§fДля управления кланом"}, {"", "§bМесто §fклана §8- §3" + FClans.getClanManager().sortedTopClans.get(clanname), "§fКоличество участников §8- §3" + FClans.getClanManager().getPlayers(clanname).size(), "§fРейтинг клана §8- §c⚔" + FClans.getClanManager().getRaiting(clanname), "§fБаланс банка клана §8- §e⛂" + FClans.getClanManager().getBalance(clanname), "", "§c[!] §fID клана §8- §6§l" + FClans.getClanManager().getIdentifier(clanname), ""}};
        int[] positions = new int[]{26, 35};
        for (int i2 = 0; i2 < 2; ++i2) {
            ItemStack item = new ItemStack(materials[i2]);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(names[i2]);
            meta.setLore(Arrays.asList(lore[i2]));
            item.setItemMeta(meta);
            inventory.setItem(positions[i2], item);
        }
        for (String clanPlayer : FClans.getClanManager().getPlayers(clanname).keySet()) {
            int empty = inventory.firstEmpty();
            inventory.setItem(empty, Menu.head(clanname, clanPlayer));
        }
        player.openInventory(inventory);
    }

    private static ItemStack head(String clanname, String player) {
        ClanPlayer cp = FClans.getClanManager().getPlayers(clanname).get(player);
        String name = "§fИгрок §7- §a" + player;
        String online = "§cНЕ В СЕТИ";
        if (Bukkit.getPlayer(player) != null) {
            online = "§aВ СЕТИ";
        }
        String[] lore = new String[]{"", "§7Ранг§8: " + Methods.getRankByInt(cp.getRank()), "§7Вступил в клан§8: §3" + Menu.timestampConvertor(cp.getJointime()), "§7Вложил в клан§8: §e⛂" + cp.getDeposit(), online, ""};
        ItemStack skull = new ItemStack(Material.LEGACY_SKULL_ITEM, 1);
        SkullMeta meta = (SkullMeta)skull.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        meta.setOwner(player);
        skull.setItemMeta(meta);
        return skull;
    }

    private static String timestampConvertor(long timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("§6dd§8.§6MM§8.§6yyyy");
        Date date = new Date(timestamp);
        return dateFormat.format(date);
    }
}

