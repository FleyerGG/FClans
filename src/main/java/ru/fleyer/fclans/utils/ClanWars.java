package ru.fleyer.fclans.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import ru.fleyer.fclans.ClanAdmin;
import ru.fleyer.fclans.FClans;
import ru.fleyer.fclans.utils.ClanWarsManager;
import ru.fleyer.fclans.utils.ClanWarsPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class ClanWars {
    public static boolean war = false;
    public static HashMap<String, ClanWarsPlayer> inwar = new HashMap();
    public static HashMap<String, ClanWarsManager> clans = new HashMap();
    public static List<Player> players = new ArrayList<Player>();
    public static ArrayList<Player> pickedkits = new ArrayList();

    public static Location stringToLoc(String line) {
        String[] l = line.split(";");
        World world = Bukkit.getWorld((String)l[0]);
        if (world == null) {
            world = Bukkit.createWorld((WorldCreator)new WorldCreator(l[0]));
        }
        double x = Double.parseDouble(l[1]);
        double y = Double.parseDouble(l[2]);
        double z = Double.parseDouble(l[3]);
        float yaw = 0.0f;
        float pitch = 0.0f;
        try {
            yaw = Float.parseFloat(l[4]);
            pitch = Float.parseFloat(l[5]);
        }
        catch (Exception exception) {
            // empty catch block
        }
        return new Location(world, x, y, z, yaw, pitch);
    }

    public static void clearPlayer(Player player) {
        if (player != null) {
            player.getInventory().clear();
            player.getInventory().setBoots(new ItemStack(Material.AIR));
            player.getInventory().setLeggings(new ItemStack(Material.AIR));
            player.getInventory().setChestplate(new ItemStack(Material.AIR));
            player.getInventory().setHelmet(new ItemStack(Material.AIR));
            player.setAllowFlight(false);
            player.setHealth(20.0);
            player.setFoodLevel(20);
            player.setGameMode(GameMode.SURVIVAL);
        }
    }

    public static void warStarter(String clanname1, String clanname2) {
        int i;
        war = true;
        final ArrayList<Player> redplayers = new ArrayList<Player>();
        final ArrayList<Player> blueplayers = new ArrayList<Player>();
        redplayers.add(Bukkit.getPlayer(FClans.getClanManager().getOwner(clanname1)));
        blueplayers.add(Bukkit.getPlayer(FClans.getClanManager().getOwner(clanname2)));
        for (i = 0; i < 4; ++i) {
            redplayers.add(FClans.getClanManager().getOnlinePlayers(clanname1).stream().filter(player -> !redplayers.contains(player)).findAny().orElse(null));
        }
        for (i = 0; i < 4; ++i) {
            blueplayers.add(FClans.getClanManager().getOnlinePlayers(clanname2).stream().filter(player -> !blueplayers.contains(player)).findAny().orElse(null));
        }
        for (Player redplayer : redplayers) {
            redplayer.setGameMode(GameMode.SURVIVAL);
            Bukkit.getScheduler().runTaskLater((Plugin)FClans.getInstance(), () -> {
                inwar.put(redplayer.getName(), new ClanWarsPlayer(clanname1, redplayer.getInventory().getContents(), redplayer.getInventory().getArmorContents()));
                players.add(redplayer);
            }, 6L);
        }
        for (Player blueplayer : blueplayers) {
            blueplayer.setGameMode(GameMode.SURVIVAL);
            Bukkit.getScheduler().runTaskLater((Plugin)FClans.getInstance(), () -> {
                inwar.put(blueplayer.getName(), new ClanWarsPlayer(clanname2, blueplayer.getInventory().getContents(), blueplayer.getInventory().getArmorContents()));
                players.add(blueplayer);
            }, 6L);
        }
        Bukkit.getScheduler().runTaskLater((Plugin)FClans.getInstance(), () -> {
            clans.put(clanname1, new ClanWarsManager("red", redplayers));
            clans.put(clanname2, new ClanWarsManager("blue", blueplayers));
            List locRED = ClanAdmin.locationsRED.stream().map(loc -> ClanWars.stringToLoc(loc)).collect(Collectors.toList());
            List locBLUE = ClanAdmin.locationsBLUE.stream().map(loc -> ClanWars.stringToLoc(loc)).collect(Collectors.toList());
            int redi = 0;
            int bluei = 0;
            for (Player red : redplayers) {
                System.out.println(inwar.get(red.getName()).getInventory());
                players.add(red);
                red.setGameMode(GameMode.SURVIVAL);
                Bukkit.getScheduler().runTaskLater((Plugin)FClans.getInstance(), () -> ClanWars.clearPlayer(red), 10L);
                red.teleport((Location)locRED.get(redi));
                ++redi;
            }
            for (Player blue : blueplayers) {
                System.out.println(inwar.get(blue.getName()).getInventory());
                players.add(blue);
                blue.setGameMode(GameMode.SURVIVAL);
                Bukkit.getScheduler().runTaskLater((Plugin)FClans.getInstance(), () -> ClanWars.clearPlayer(blue), 10L);
                blue.teleport((Location)locBLUE.get(bluei));
                ++bluei;
            }
            new BukkitRunnable(){
                int i = 0;

                public void run() {
                    if (this.i == 10) {
                        for (Player red : redplayers) {
                            red.sendTitle("\u00a7c\u0411\u043e\u0439 \u00a7f\u043d\u0430\u0447\u0430\u043b\u0441\u044f!", "\u00a7a\u0423\u0434\u0430\u0447\u043d\u044b\u0445 \u0441\u0440\u0430\u0436\u0435\u043d\u0438\u0439!");
                            red.closeInventory();
                            red.getInventory().setHelmet(new ItemStack(Material.GREEN_BANNER, 1));
                        }
                        for (Player blue : blueplayers) {
                            blue.sendTitle("\u00a7c\u0411\u043e\u0439 \u00a7f\u043d\u0430\u0447\u0430\u043b\u0441\u044f!", "\u00a7a\u0423\u0434\u0430\u0447\u043d\u044b\u0445 \u0441\u0440\u0430\u0436\u0435\u043d\u0438\u0439!");
                            blue.closeInventory();
                            blue.getInventory().setHelmet(new ItemStack(Material.BLACK_BANNER, 1));
                        }
                        this.cancel();
                        return;
                    }
                    for (Player red : redplayers) {
                        red.sendTitle(ClanWars.score(this.i), "\u00a7f\u0414\u043e \u043d\u0430\u0447\u0430\u043b\u0430 \u00a7c\u0431\u043e\u044f");
                        red.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 10));
                        red.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 10));
                        if (pickedkits.contains((Object)red)) continue;
                        ClanWars.choiceMenu(red);
                    }
                    for (Player blue : blueplayers) {
                        blue.sendTitle(ClanWars.score(this.i), "\u00a7f\u0414\u043e \u043d\u0430\u0447\u0430\u043b\u0430 \u00a7c\u0431\u043e\u044f");
                        blue.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 10));
                        blue.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 10));
                        if (pickedkits.contains((Object)blue)) continue;
                        ClanWars.choiceMenu(blue);
                    }
                    ++this.i;
                }
            }.runTaskTimer((Plugin)FClans.getInstance(), 0L, 20L);
        }, 10L);
    }

    private static String score(int i) {
        if (i == 0) {
            return "\u00a7e\u2789";
        }
        if (i == 1) {
            return "\u00a7e\u2788";
        }
        if (i == 2) {
            return "\u00a7e\u2787";
        }
        if (i == 3) {
            return "\u00a7e\u2786";
        }
        if (i == 4) {
            return "\u00a7e\u2785";
        }
        if (i == 5) {
            return "\u00a7e\u2784";
        }
        if (i == 6) {
            return "\u00a7e\u2783";
        }
        if (i == 7) {
            return "\u00a7c\u2782";
        }
        if (i == 8) {
            return "\u00a7c\u2781";
        }
        if (i == 9) {
            return "\u00a7c\u2780";
        }
        return null;
    }

    public static void archer(Player player) {
        Material[] materials = new Material[]{Material.BOW, Material.ARROW, Material.BREAD, Material.GOLDEN_APPLE};
        int[] count = new int[]{1, 192, 5, 1};
        player.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
        player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
        for (int i = 0; i < 4; ++i) {
            ItemStack items = new ItemStack(materials[i], count[i]);
            player.getInventory().addItem(new ItemStack[]{items});
        }
    }

    public static void knight(Player player) {
        Material[] materials = new Material[]{Material.DIAMOND_SWORD, Material.BREAD, Material.GOLDEN_APPLE};
        int[] count = new int[]{1, 5, 1};
        player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
        player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
        for (int i = 0; i < 3; ++i) {
            ItemStack items = new ItemStack(materials[i], count[i]);
            player.getInventory().addItem(new ItemStack[]{items});
        }
    }

    public static void tank(Player player) {
        Material[] materials = new Material[]{Material.IRON_SWORD, Material.BREAD, Material.GOLDEN_APPLE};
        int[] count = new int[]{1, 5, 1};
        player.getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
        player.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
        for (int i = 0; i < 3; ++i) {
            ItemStack items = new ItemStack(materials[i], count[i]);
            player.getInventory().addItem(new ItemStack[]{items});
        }
    }

    public static void musketeer(Player player) {
        Material[] materials = new Material[]{Material.IRON_SWORD, Material.BREAD, Material.GOLDEN_APPLE};
        int[] count = new int[]{1, 5, 1};
        player.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
        player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
        for (int i = 0; i < 3; ++i) {
            ItemStack items = new ItemStack(materials[i], count[i]);
            player.getInventory().addItem(new ItemStack[]{items});
        }
    }

    private static void choiceMenu(Player player) {
        Inventory inventory = Bukkit.createInventory((InventoryHolder)player, (int)27, (String)"\u00a70\u0412\u044b\u0431\u043e\u0440 \u043a\u043b\u0430\u0441\u0441\u0430");
        ItemStack panecyan = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1);
        for (int i = 0; i < 27; ++i) {
            inventory.setItem(i, panecyan);
        }
        Material[] materials = new Material[]{Material.BOW, Material.IRON_SWORD, Material.DIAMOND_SWORD, Material.BONE};
        String[] names = new String[]{"\u00a7a\u041b\u0443\u0447\u043d\u0438\u043a", "\u00a7a\u0420\u044b\u0446\u0430\u0440\u044c", "\u00a7a\u0422\u0430\u043d\u043a", "\u00a7a\u041c\u0443\u0448\u043a\u0435\u0442\u0451\u0440"};
        String[][] lore = new String[][]{{"\u00a77\u041d\u0430\u0436\u043c\u0438\u0442\u0435 \u0441\u044e\u0434\u0430, \u0447\u0442\u043e\u0431\u044b \u0432\u044b\u0431\u0440\u0430\u0442\u044c", "\u00a77\u041a\u043b\u0430\u0441\u0441 \u00a7a\u043b\u0443\u0447\u043d\u0438\u043a"}, {"\u00a77\u041d\u0430\u0436\u043c\u0438\u0442\u0435 \u0441\u044e\u0434\u0430, \u0447\u0442\u043e\u0431\u044b \u0432\u044b\u0431\u0440\u0430\u0442\u044c", "\u00a77\u041a\u043b\u0430\u0441\u0441 \u00a7a\u0440\u044b\u0446\u0430\u0440\u044c"}, {"\u00a77\u041d\u0430\u0436\u043c\u0438\u0442\u0435 \u0441\u044e\u0434\u0430, \u0447\u0442\u043e\u0431\u044b \u0432\u044b\u0431\u0440\u0430\u0442\u044c", "\u00a77\u041a\u043b\u0430\u0441\u0441 \u00a7a\u0442\u0430\u043d\u043a"}, {"\u00a77\u0421\u043a\u043e\u0440\u043e.."}};
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
}

