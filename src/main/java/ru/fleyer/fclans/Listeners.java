package ru.fleyer.fclans;

import java.util.HashMap;
import ru.fleyer.fclans.utils.ClanWars;
import ru.fleyer.fclans.utils.ClanWarsManager;
import ru.fleyer.fclans.utils.Menu;
import ru.fleyer.fclans.utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class Listeners implements Listener {
    private HashMap<String, String> inmenu = new HashMap();

    private void later(Runnable runnable, int ticks) {
        Bukkit.getScheduler().runTaskLater(FClans.getInstance(), runnable, ticks);
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        if (!FClans.getClanManager().playersClan.containsKey(player.getName())) {
            e.setFormat(e.getFormat().replace("!clan!", ""));
            e.setMessage(e.getMessage().replace("!clan!", ""));
            return;
        }
        String clan = FClans.getClanManager().playersClan.get(player.getName());
        e.setFormat(e.getFormat().replace("!clan!", clan.replace("&", "§")));
        e.setMessage(e.getMessage().replace("!clan!", clan.replace("&", "§")));
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        Player killer = player.getKiller();
        if (!ClanWars.inwar.containsKey(player.getName())) {
            return;
        }
        player.spigot().respawn();
        player.sendMessage(" §c▷ §fВы погибли и наблюдаете за сражением!");
        String clanname = ClanWars.inwar.get(player.getName()).getClanname();
        String color = ClanWars.clans.get(clanname).getColor();
        player.getInventory().setContents(ClanWars.inwar.get(player.getName()).getInventory());
        player.getInventory().setArmorContents(ClanWars.inwar.get(player.getName()).getArmor());
        ClanWars.pickedkits.remove(player);
        if (color == "red") {
            this.later(() -> player.teleport(ClanWars.stringToLoc(ClanAdmin.locationsREDwait)), 2);
        } else {
            this.later(() -> player.teleport(ClanWars.stringToLoc(ClanAdmin.locationsBLUEwait)), 2);
        }
        ClanWars.clans.get(clanname).getPlayers().remove(player);
        if (killer == null) {
            return;
        }
        if (ClanWars.clans.get(clanname).getPlayers().size() <= 0) {
            String clanwinner = ClanWars.inwar.get(killer.getName()).getClanname();
            Methods.messageToClanPlayers(clanname, " §c▷ §fНаш клан проиграл сражение клану " + clanwinner.replace("&", "§") + "§f!");
            Methods.messageToClanPlayers(clanwinner, " §c▷ §fНаш клан выиграл сражение у клана " + clanname.replace("&", "§") + "§f!");
            Methods.titleToClanPlayers(clanname, "§c● §fПобедил§f клан§8: " + clanwinner.replace("&", "§"), "§c-鐠 §fрейтинга");
            Methods.titleToClanPlayers(clanwinner, "§a● §fПобедил§f клан§8: " + clanwinner.replace("&", "§"), "§b+鑐 §fрейтинга");
            for (Player all : Bukkit.getOnlinePlayers()) {
                all.sendMessage("");
                all.sendMessage("       §e[КЛАНОВАЯ ⚔ БИТВА]");
                all.sendMessage(" §a☀ §fПобедил клан§8: " + clanwinner.replace("&", "§") + " §8| §c☠ §fПроиграл клан§8: " + clanname.replace("&", "§"));
                all.sendMessage(" §fУчавствуй в гонке кланов §c§l[!] §fПодробнее§8: §b§lVK.COM/LATTYCRAFT");
                all.sendMessage("");
            }
            ClanWars.pickedkits.clear();
            ClanWars.war = false;
            ClanWars.inwar.clear();
            this.later(() -> {
                ClanWars.players.forEach(clanplayer -> {
                    ClanWars.clearPlayer(clanplayer);
                    clanplayer.teleport(clanplayer.getLocation().getWorld().getSpawnLocation());
                    try {
                        clanplayer.getInventory().setContents(ClanWars.inwar.get(clanplayer.getName()).getInventory());
                        clanplayer.getInventory().setArmorContents(ClanWars.inwar.get(clanplayer.getName()).getArmor());
                    }
                    catch (NullPointerException nullPointerException) {
                        // empty catch block
                    }
                });
                FClans.getClanManager().addRaiting(clanwinner, 50);
                FClans.getClanManager().removeRaiting(clanname, 20);
            }, 100);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (!ClanWars.inwar.containsKey(player.getName())) {
            return;
        }
        player.spigot().respawn();
        String clanname = ClanWars.inwar.get(player.getName()).getClanname();
        player.getInventory().setContents(ClanWars.inwar.get(player.getName()).getInventory());
        player.getInventory().setArmorContents(ClanWars.inwar.get(player.getName()).getArmor());
        for (String clan : ClanWars.clans.keySet()) {
            if (clan.equals(clanname)) continue;
            String clanwinner = clan;
            player.getInventory().setContents(ClanWars.inwar.get(player.getName()).getInventory());
            player.getInventory().setArmorContents(ClanWars.inwar.get(player.getName()).getArmor());
            ClanWarsManager cwm = ClanWars.clans.get(clanname);
            if (cwm != null && cwm.getPlayers().size() > 0) {
                cwm.getPlayers().remove(player);
            }
            ClanWars.inwar.remove(player.getName());
            if (ClanWars.clans.get(clanname).getPlayers().size() != 0) break;
            Methods.messageToClanPlayers(clanname, " §c▷ §fНаш клан проиграл сражение клану " + clanwinner.replace("&", "§") + "§f!");
            Methods.messageToClanPlayers(clanwinner, " §c▷ §fНаш клан выиграл сражение у клана " + clanname.replace("&", "§") + "§f!");
            Methods.titleToClanPlayers(clanname, "§c● §fПобедил§f клан§8: " + clanwinner.replace("&", "§"), "§c-鐠 §fрейтинга");
            Methods.titleToClanPlayers(clanwinner, "§a● §fПобедил§f клан§8: " + clanwinner.replace("&", "§"), "§b+鑐 §fрейтинга");
            for (Player all : Bukkit.getOnlinePlayers()) {
                all.sendMessage("");
                all.sendMessage("       §e[КЛАНОВАЯ ⚔ БИТВА]");
                all.sendMessage(" §a☀ §fПобедил клан§8: " + clanwinner.replace("&", "§") + " §8| §c☠ §fПроиграл клан§8: " + clanname.replace("&", "§"));
                all.sendMessage(" §fУчавствуй в гонке кланов §c§l[!] §fПодробнее§8: §b§lVK.COM/LATTYCRAFT");
                all.sendMessage("");
            }
            ClanWars.pickedkits.clear();
            this.later(() -> {
                ClanWars.players.forEach(clanplayer -> {
                    ClanWars.clearPlayer(clanplayer);
                    clanplayer.teleport(clanplayer.getLocation().getWorld().getSpawnLocation());
                    try {
                        clanplayer.getInventory().setContents(ClanWars.inwar.get(clanplayer.getName()).getInventory());
                        clanplayer.getInventory().setArmorContents(ClanWars.inwar.get(clanplayer.getName()).getArmor());
                    }
                    catch (NullPointerException nullPointerException) {
                        // empty catch block
                    }
                });
                FClans.getClanManager().addRaiting(clanwinner, 50);
                FClans.getClanManager().removeRaiting(clanname, 20);
                ClanWars.war = false;
                ClanWars.inwar.clear();
            }, 100);
            break;
        }
    }

    @EventHandler
    public void onChat(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        String message = e.getMessage().toLowerCase();
        if (ClanWars.war && message.startsWith("kill")) {
            player.sendMessage(" §c▷ §fНельзя использовать эту команду во время кланового сражения!");
            e.setCancelled(true);
        }
        if (ClanWars.pickedkits.contains(player)) {
            player.sendMessage(" §c▷ §fНельзя выполнять команды в клановом сражении!");
            e.setCancelled(true);
            return;
        }
        if (ClanWars.war) {
            for (Player all : ClanWars.pickedkits) {
                if (!message.contains(all.getName().toLowerCase())) continue;
                player.sendMessage(" §c▷ §fНельзя указывать в команде ник игрока, который находится в клановом сражении!");
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Arrow arrow;
            if (e.getDamager() instanceof Player) {
                String damagerclan;
                Player player = (Player)e.getEntity();
                if (!FClans.getClanManager().playersClan.containsKey(player.getName())) {
                    return;
                }
                Player damager = (Player)e.getDamager();
                String playerclan = FClans.getClanManager().playersClan.get(player.getName());
                if (playerclan == (damagerclan = FClans.getClanManager().playersClan.get(damager.getName()))) {
                    e.setCancelled(true);
                }
            } else if (e.getDamager() instanceof Arrow && (arrow = (Arrow)e.getDamager()).getShooter() instanceof Player) {
                String damagerclan;
                Player player = (Player)e.getEntity();
                if (!FClans.getClanManager().playersClan.containsKey(player.getName())) {
                    return;
                }
                Player damager = (Player)arrow.getShooter();
                String playerclan = FClans.getClanManager().playersClan.get(player.getName());
                if (playerclan == (damagerclan = FClans.getClanManager().playersClan.get(damager.getName()))) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        String clanname;
        Player player = (Player)e.getWhoClicked();
        if (player.getOpenInventory().getTitle().equalsIgnoreCase("§8§lМеню клана")) {
            clanname = FClans.getClanManager().playersClan.get(player.getName());
            e.setCancelled(true);
            if (e.getCurrentItem() == null) {
                return;
            }
            if (e.getCurrentItem().getType().equals(Material.AIR)) {
                return;
            }
            if (e.getCurrentItem().getType().equals(Material.GRAY_STAINED_GLASS_PANE)) {
                return;
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§fИгрок §7- §a" + FClans.getClanManager().getOwner(clanname))) {
                player.sendMessage(" §c▷ §fНельзя управлять §c[Главой] §fклана!");
                return;
            }
            for (String players : FClans.getClanManager().getPlayers(clanname).keySet()) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§fИгрок §7- §a" + players)) {
                    if (!FClans.getClanManager().getOwner(clanname).equalsIgnoreCase(player.getName())) {
                        player.sendMessage(" §c▷ §fВы не являетесь владельцем клана!");
                        return;
                    }
                    Menu.playerMenu(player);
                    this.inmenu.put(player.getName(), players);
                    Bukkit.getScheduler().runTaskLater((Plugin)FClans.getInstance(), () -> this.inmenu.remove(player.getName()), 1200L);
                }
                switch (e.getCurrentItem().getItemMeta().getDisplayName()) {
                    case " §8▪ §aУправление кланом": {
                        if (!FClans.getClanManager().getOwner(clanname).equalsIgnoreCase(player.getName())) {
                            player.sendMessage(" §c▷ §fВы не являетесь §c[Главой] §fклана!");
                            return;
                        }
                        Menu.controlMenu(player);
                        break;
                    }
                }
            }
        }
        if (e.getWhoClicked().getOpenInventory().getTitle().equalsIgnoreCase("§0Выбор класса")) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) {
                return;
            }
            if (e.getCurrentItem().getType().equals(Material.AIR)) {
                return;
            }
            if (e.getCurrentItem().getType().equals(Material.GREEN_STAINED_GLASS_PANE)) {
                return;
            }
            switch (e.getCurrentItem().getItemMeta().getDisplayName()) {
                case "§aЛучник": {
                    player.closeInventory();
                    ClanWars.archer(player);
                    ClanWars.pickedkits.add(player);
                    break;
                }
                case "§aРыцарь": {
                    player.closeInventory();
                    ClanWars.knight(player);
                    ClanWars.pickedkits.add(player);
                    break;
                }
                case "§aТанк": {
                    player.closeInventory();
                    ClanWars.tank(player);
                    ClanWars.pickedkits.add(player);
                    break;
                }
            }
        }
        if (e.getWhoClicked().getOpenInventory().getTitle().equalsIgnoreCase(" §8▪ §0Управление кланом")) {
            clanname = FClans.getClanManager().playersClan.get(player.getName());
            e.setCancelled(true);
            if (e.getCurrentItem() == null) {
                return;
            }
            if (e.getCurrentItem().getType().equals(Material.AIR)) {
                return;
            }
            if (e.getCurrentItem().getType().equals(Material.RED_STAINED_GLASS_PANE)) {
                return;
            }
            if (e.getCurrentItem().getType().equals(Material.BONE)) {
                return;
            }
            if (e.getCurrentItem().getItemMeta() == null) {
                return;
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName() == null) {
                return;
            }
            switch (e.getCurrentItem().getItemMeta().getDisplayName()) {
                case " §8▪ §aУдалить клан": {
                    FClans.getClanManager().removeClan(clanname);
                    FClans.getClanManager().updateStat();
                    player.closeInventory();
                }
            }
        }
        if (e.getWhoClicked().getOpenInventory().getTitle().equalsIgnoreCase("§8§lУправление игроком")) {
            if (!this.inmenu.containsKey(player.getName())) {
                player.closeInventory();
                return;
            }
            clanname = FClans.getClanManager().playersClan.get(player.getName());
            e.setCancelled(true);
            if (e.getCurrentItem() == null) {
                return;
            }
            if (e.getCurrentItem().getType().equals(Material.AIR)) {
                return;
            }
            if (e.getCurrentItem().getType().equals(Material.BLUE_STAINED_GLASS_PANE)) {
                return;
            }
            switch (e.getCurrentItem().getItemMeta().getDisplayName()) {
                case "§cИсключить игрока из клана": {
                    player.closeInventory();
                    Methods.messageToClanPlayers(clanname, " §c▷ §fИгрок §c" + this.inmenu.get(player.getName()) + " §fбыл исключён из клана!");
                    FClans.getClanManager().removePlayer(clanname, this.inmenu.get(player.getName()));
                    this.inmenu.remove(player.getName());
                    break;
                }
                case "§aНастроить ранг игрока": {
                    Menu.playerUpRankMenu(player);
                }
            }
        }
        if (e.getWhoClicked().getOpenInventory().getTitle().equalsIgnoreCase("§8§lНастройка ранга")) {
            if (!this.inmenu.containsKey(player.getName())) {
                player.closeInventory();
                return;
            }
            clanname = FClans.getClanManager().playersClan.get(player.getName());
            e.setCancelled(true);
            if (e.getCurrentItem() == null) {
                return;
            }
            if (e.getCurrentItem().getType().equals(Material.AIR)) {
                return;
            }
            if (e.getCurrentItem().getType().equals(Material.YELLOW_STAINED_GLASS_PANE)) {
                return;
            }
            for (int i = 1; i < 9; ++i) {
                if (!e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§c" + i + " ранг")) continue;
                FClans.getClanManager().getPlayers(clanname).get(this.inmenu.get(player.getName())).setRank(i);
                FClans.getClanManager().savePlayers(clanname);
                Methods.messageToClanPlayers(clanname, " §c▷ §fРанг игроку §c" + this.inmenu.get(player.getName()) + " §fустановлен на " + Methods.getRankByInt(i));
                player.closeInventory();
                return;
            }
        }
    }
}

