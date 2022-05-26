package ru.fleyer.fclans.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import ru.fleyer.fclans.FClans;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

public class ClanManager {
    public HashMap<String, ClanManager> clans = new HashMap();
    public HashMap<String, String> playersClan = new HashMap();
    public HashMap<String, Integer> topClans = new HashMap();
    public HashMap<String, Integer> sortedTopClans = new HashMap();
    public ArrayList<String> sortedClans = new ArrayList();
    private int identifier;
    private String owner;
    private HashMap<String, ClanPlayer> players;
    private int raiting;
    private int balance;
    private Location home;

    public ClanManager() {
    }

    public ClanManager(int identifier, String owner, HashMap<String, ClanPlayer> players, int raiting, int balance, Location home) {
        this.identifier = identifier;
        this.owner = owner;
        this.players = players;
        this.raiting = raiting;
        this.balance = balance;
        this.home = home;
    }

    public int getIdentifier() {
        return this.identifier;
    }

    public String getOwner() {
        return this.owner;
    }

    public HashMap<String, ClanPlayer> getPlayers() {
        return this.players;
    }

    public int getRaiting() {
        return this.raiting;
    }

    public int getBalance() {
        return this.balance;
    }

    public Location getHome() {
        return this.home;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setPlayers(HashMap<String, ClanPlayer> players) {
        this.players = players;
    }

    public void setRaiting(int raiting) {
        this.raiting = raiting;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setHome(Location home) {
        this.home = home;
    }

    private static String locToString(Location loc) {
        return String.format("%s;%s;%s;%s;%s;%s", loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ(), Float.valueOf(loc.getYaw()), Float.valueOf(loc.getPitch()));
    }

    public void updateStat() {
        this.sortedClans.clear();
        this.sortTopClans();
    }

    public void sortTopClans() {
        Object[] sort = this.topClans.entrySet().toArray();
        Arrays.sort(sort, (o1, o2) -> ((Integer)((Map.Entry)o2).getValue()).compareTo((Integer)((Map.Entry)o1).getValue()));
        int i = 1;
        for (Object e : sort) {
            if (i == 4) break;
            String clanname = (String) ((Map.Entry)e).getKey();
            int raiting = FClans.getClanManager().getRaiting(clanname);
            int users = FClans.getClanManager().getPlayers(clanname).size();
            int balance = FClans.getClanManager().getBalance(clanname);
            switch (i) {
                case 1: {
                    this.sortedClans.add("§8  §e①Место §8- §c" + clanname.replace("&", "§"));
                    this.sortedClans.add("§a Рейтинг§8: §b⚔" + raiting + " §8| §aУчастников§8: §b☺" + users + " §8| §aБаланс§8: §e⛂" + balance);
                    break;
                }
                case 2: {
                    this.sortedClans.add("§f  ②Место §8- §c" + clanname.replace("&", "§"));
                    this.sortedClans.add("§a Рейтинг§8: §b⚔" + raiting + " §8| §aУчастников§8: §b☺" + users + " §8| §aБаланс§8: §e⛂" + balance);
                    break;
                }
                case 3: {
                    this.sortedClans.add("§6  ③Место §8- §c" + clanname.replace("&", "§"));
                    this.sortedClans.add("§a Рейтинг§8: §b⚔" + raiting + " §8| §aУчастников§8: §b☺" + users + " §8| §aБаланс§8: §e⛂" + balance);
                }
            }
            ++i;
        }
        int sorted = 0;
        for (Object e : sort) {
            this.sortedTopClans.put((String) ((Map.Entry)e).getKey(), ++sorted);
        }
    }

    private void fillTopClans() {
    }

    public boolean isOwnerOnline(String clanname) {
        String ownername = this.getOwner(clanname);
        Player owner = Bukkit.getPlayer(ownername);
        return owner != null;
    }

    public void setHome(Location location, String clanname) {
        this.clans.get(clanname).home = location;
        FClans.getConfiguration().set("clans." + clanname + ".home", ClanManager.locToString(location));
        FClans.getInstance().saveConfig();
    }

    private Location stringToLoc(String line) {
        String[] l = line.split(";");
        World world = Bukkit.getWorld(l[0]);
        if (world == null) {
            world = Bukkit.createWorld(new WorldCreator(l[0]));
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

    public Location getHome(String clanname) {
        try {
            return this.clans.get(clanname).getHome();
        }
        catch (NullPointerException nullPointerException) {
            return Bukkit.getWorld("world").getSpawnLocation();
        }
    }

    public List<Player> getOnlinePlayers(String clanname) {
        ArrayList<Player> onlinePlayers = new ArrayList<Player>();
        for (String clanPlayer : this.getPlayers(clanname).keySet()) {
            Player player = Bukkit.getPlayer(clanPlayer);
            if (player == null) continue;
            onlinePlayers.add(player);
        }
        return onlinePlayers;
    }

    public int getIdentifier(String clanname) {
        return this.clans.get(clanname).getIdentifier();
    }

    public String getClanNameByID(int identifier) {
        for (String clan : this.clans.keySet()) {
            if (identifier != this.clans.get(clan).getIdentifier()) continue;
            return clan;
        }
        return null;
    }

    public String getOwner(String clanname) {
        return this.clans.get(clanname).getOwner();
    }

    public int getRaiting(String clanname) {
        return this.clans.get(clanname).getRaiting();
    }

    public int getBalance(String clanname) {
        return this.clans.get(clanname).getBalance();
    }

    public HashMap<String, ClanPlayer> getPlayers(String clanname) {
        return this.clans.get(clanname).getPlayers();
    }

    private HashMap<String, ClanPlayer> clanPlayersFromString(List<String> list) {
        HashMap<String, ClanPlayer> players = new HashMap<String, ClanPlayer>();
        for (String player : list) {
            String[] split = player.split(":");
            String name = split[0];
            int rank = Integer.parseInt(split[1]);
            int deposit = Integer.parseInt(split[2]);
            long jointime = Long.parseLong(split[3]);
            players.put(name, new ClanPlayer(rank, deposit, jointime));
        }
        return players;
    }

    private List<String> clanPlayersToString(HashMap<String, ClanPlayer> hashmap) {
        ArrayList<String> players = new ArrayList<String>();
        for (String player : hashmap.keySet()) {
            int rank = hashmap.get(player).getRank();
            long jointime = hashmap.get(player).getJointime();
            int deposit = hashmap.get(player).getDeposit();
            players.add(player + ":" + rank + ":" + deposit + ":" + jointime);
        }
        return players;
    }

    public void addPlayer(String clanname, String player) {
        this.clans.get(clanname).getPlayers().put(player, new ClanPlayer(1, 0, System.currentTimeMillis()));
        FClans.getConfiguration().set("clans." + clanname + ".players", this.clanPlayersToString(this.getPlayers(clanname)));
        FClans.getInstance().saveConfig();
        this.playersClan.put(player, clanname);
    }

    public void savePlayers(String clanname) {
        FClans.getConfiguration().set("clans." + clanname + ".players", this.clanPlayersToString(this.getPlayers(clanname)));
        FClans.getInstance().saveConfig();
    }

    public void removePlayer(String clanname, String player) {
        this.clans.get(clanname).getPlayers().remove(player);
        this.playersClan.remove(player);
        FClans.getConfiguration().set("clans." + clanname + ".players", this.clanPlayersToString(this.getPlayers(clanname)));
        FClans.getInstance().saveConfig();
    }

    public void addRaiting(String clanname, int raiting) {
        this.clans.get(clanname).setRaiting(this.clans.get(clanname).getRaiting() + raiting);
        FClans.getConfiguration().set("clans." + clanname + ".raiting", this.clans.get(clanname).getRaiting());
        FClans.getInstance().saveConfig();
    }

    public void removeRaiting(String clanname, int raiting) {
        this.clans.get(clanname).setRaiting(this.clans.get(clanname).getRaiting() - raiting);
        FClans.getConfiguration().set("clans." + clanname + ".raiting", this.clans.get(clanname).getRaiting());
        FClans.getInstance().saveConfig();
    }

    public void addBalance(String clanname, int balance) {
        this.clans.get(clanname).setBalance(this.clans.get(clanname).getBalance() + balance);
        FClans.getConfiguration().set("clans." + clanname + ".balance", this.clans.get(clanname).getBalance());
        FClans.getInstance().saveConfig();
    }

    public void removeBalance(String clanname, int balance) {
        this.clans.get(clanname).setBalance(this.clans.get(clanname).getBalance() - balance);
        FClans.getConfiguration().set("clans." + clanname + ".balance", this.clans.get(clanname).getBalance());
        FClans.getInstance().saveConfig();
    }

    public void removeClan(String clanname) {
        Methods.messageToClanPlayers(clanname, "§e[!] §fКлан " + clanname.replace("&", "§") + " §fбыл расформирован!");
        for (String clanPlayer : this.clans.get(clanname).getPlayers().keySet()) {
            this.playersClan.remove(clanPlayer);
        }
        this.clans.remove(clanname);
        FClans.getConfiguration().set("clans." + clanname, null);
        FClans.getInstance().saveConfig();
    }

    public void createClan(String clanname, String owner) {
        HashMap<String, ClanPlayer> created = new HashMap<String, ClanPlayer>();
        created.put(owner, new ClanPlayer(9, 0, System.currentTimeMillis()));
        int random = ThreadLocalRandom.current().nextInt(10000, 99999);
        this.clans.put(clanname, new ClanManager(random, owner, created, 0, 0, null));
        FClans.getConfiguration().set("clans." + clanname + ".identifier", random);
        FClans.getConfiguration().set("clans." + clanname + ".owner", owner);
        FClans.getConfiguration().set("clans." + clanname + ".players", this.clanPlayersToString(created));
        FClans.getConfiguration().set("clans." + clanname + ".raiting", 0);
        FClans.getConfiguration().set("clans." + clanname + ".balance", 0);
        FClans.getInstance().saveConfig();
        this.playersClan.put(owner, clanname);
    }

    public void load() {
        if (FClans.getConfiguration().getConfigurationSection("clans") == null) {
            System.out.println("[FClans] Кланов не найдено");
            return;
        }
        for (String clan : FClans.getConfiguration().getConfigurationSection("clans").getKeys(false)) {
            String owner = FClans.getConfiguration().getString("clans." + clan + ".owner");
            int identifier = FClans.getConfiguration().getInt("clans." + clan + ".identifier");
            int raiting = FClans.getConfiguration().getInt("clans." + clan + ".raiting");
            int balance = FClans.getConfiguration().getInt("clans." + clan + ".balance");
            List players = FClans.getConfiguration().getStringList("clans." + clan + ".players");
            if (FClans.getConfiguration().getString("clans." + clan + ".home") != null) {
                String home = FClans.getConfiguration().getString("clans." + clan + ".home");
                this.clans.put(clan, new ClanManager(identifier, owner, this.clanPlayersFromString(players), raiting, balance, this.stringToLoc(home)));
            } else {
                this.clans.put(clan, new ClanManager(identifier, owner, this.clanPlayersFromString(players), raiting, balance, null));
            }
            this.topClans.put(clan, raiting);
            for (String player : this.clans.get(clan).getPlayers().keySet()) {
                this.playersClan.put(player, clan);
            }
        }
        System.out.println("[FClans] Загрузка кланов завершена, загружено " + FClans.getConfiguration().getConfigurationSection("clans").getKeys(false).size() + " кланов");
        this.sortTopClans();
    }
}

