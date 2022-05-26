package ru.fleyer.fclans;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ru.fleyer.fclans.utils.ClanWars;
import ru.fleyer.fclans.utils.Menu;
import ru.fleyer.fclans.utils.Methods;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClanCommand implements CommandExecutor {
    private HashMap<String, String> inrequest = new HashMap();
    private HashMap<String, String> warrequest = new HashMap();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player)sender;
        if (args.length == 0) {
            this.message(player);
            return true;
        }
        if (args[0].equalsIgnoreCase("war")) {
            if (!FClans.getClanManager().playersClan.containsKey(player.getName())) {
                player.sendMessage(" §c▷ §fВы не состоите в клане!");
                return true;
            }
            if (!FClans.getClanManager().getOwner(FClans.getClanManager().playersClan.get(player.getName())).equalsIgnoreCase(player.getName())) {
                player.sendMessage(" §c▷ §fВы не являетесь владельцем клана!");
                return true;
            }
            if (ClanWars.war) {
                player.sendMessage(" §c▷ §fНа арене проходит сражение, подождите!");
                return true;
            }
            if (args.length == 1) {
                player.sendMessage(" §c▷ §fУкажите ID клана!");
                return true;
            }
            Pattern pattern = Pattern.compile("^([+-]?[1-9]\\d*|0)$");
            Matcher matcher = pattern.matcher(args[1]);
            if (!matcher.find()) {
                player.sendMessage(" §c▷ §fУкажите правильное число!");
                return true;
            }
            String prenumber = args[1].substring(matcher.start(), matcher.end());
            if (prenumber.length() != 5) {
                player.sendMessage(" §c▷ §fID клана должен содержать 5 цифр!");
                return true;
            }
            int id = Integer.parseInt(prenumber);
            String clanname = FClans.getClanManager().getClanNameByID(id);
            if (clanname == null) {
                player.sendMessage(" §c▷ §fКлан с таким ID не найден!");
                return true;
            }
            if (!FClans.getClanManager().isOwnerOnline(clanname)) {
                player.sendMessage(" §c▷ §fВладелец клана не на сервере!");
                return true;
            }
            if (FClans.getClanManager().getOnlinePlayers(clanname).size() < 5) {
                player.sendMessage(" §c▷ §fКоличество игроков клана в онлайне меньше 5!");
                return true;
            }
            String clansender = FClans.getClanManager().playersClan.get(player.getName());
            if (FClans.getClanManager().getOnlinePlayers(clansender).size() < 5) {
                player.sendMessage(" §c▷ §fКоличество игроков Вашего клана в онлайне меньше 5!");
                return true;
            }
            int clan1rainting = FClans.getClanManager().getRaiting(clanname);
            int clan2rainting = FClans.getClanManager().getRaiting(clansender);
            if ((clan1rainting >= 200 || clan2rainting >= 200) && Math.abs(Math.abs(clan1rainting) - Math.abs(clan2rainting)) > 200) {
                player.sendMessage(" §c▷ §fРазница рейтингов кланов больше 200!");
                return true;
            }
            this.warrequest.put(clanname, clansender);
            String owner = FClans.getClanManager().getOwner(clanname);
            Player powner = Bukkit.getPlayer(owner);
            Bukkit.getScheduler().runTaskLater(FClans.getInstance(), () -> this.warrequest.remove(clanname), 1200L);
            Methods.messageToClanPlayers(clansender, " §c▷ §c⚔ §fНаш клан предложил сразиться на арене клану " + clanname.replace("&", "§") + "§f!");
            Methods.messageToClanPlayers(clanname, " §c▷ §c⚔ §fКлан " + clansender.replace("&", "§") + " §fпредложил нам сразиться на арене!");
            powner.sendMessage(" §c▷ §fДля принятия запроса напишите §c/c yes §fдля отмены §c/c no");
            Methods.titleToClanPlayers(clanname, " §c⚔ §fНам предложили клановый §cБОЙ", "§c[Глава] §fпринимает §aрешение§7...");
            powner.sendTitle("§fПринять§8: §a/c yes §8|  §fОтклонить§8: §c/c no", "§c⚔ §fКлан§8: " + clansender.replace("&", "§") + " §fвызвает на §cКлановый БОЙ");
            Bukkit.getScheduler().runTaskLater(FClans.getInstance(), () -> {
                Methods.titleToClanPlayers(clanname, " §c⚔ §fНам предложили клановый §cБОЙ", "§c[Глава] §fпринимает §aрешение§7...");
                powner.sendTitle("§fПринять§8: §a/c yes §8|  §fОтклонить§8: §c/c no", "§c⚔ §fКлан§8: " + clansender.replace("&", "§") + " §fвызвает на §cКлановый БОЙ");
            }, 100L);
            return true;
        }
        if (args[0].equalsIgnoreCase("yes")) {
            if (!FClans.getClanManager().playersClan.containsKey(player.getName())) {
                player.sendMessage(" §c▷ §fВы не состоите в клане!");
                return true;
            }
            String clanname = FClans.getClanManager().playersClan.get(player.getName());
            if (!FClans.getClanManager().getOwner(clanname).equalsIgnoreCase(player.getName())) {
                player.sendMessage(" §c▷ §fВы не являетесь владельцем клана!");
                return true;
            }
            if (ClanWars.war) {
                player.sendMessage(" §c▷ §fНа арене проходит сражение, подождите!");
                return true;
            }
            if (!this.warrequest.containsKey(clanname)) {
                player.sendMessage(" §c▷ §fВам не отправляли запрос о сражении на арене!");
                return true;
            }
            String clansender = this.warrequest.get(clanname);
            if (FClans.getClanManager().getOnlinePlayers(clansender).size() < 5) {
                player.sendMessage(" §c▷ §fКоличество игроков клана в онлайне меньше 5!");
                return true;
            }
            if (FClans.getClanManager().getOnlinePlayers(clanname).size() < 5) {
                player.sendMessage(" §c▷ §fКоличество игроков Вашего клана в онлайне меньше 5!");
                return true;
            }
            Methods.messageToClanPlayers(clansender, " §c▷ §fКлан " + clanname.replace("&", "§") + " §fпринял запрос на сражение!");
            Methods.messageToClanPlayers(clanname, " §c▷ §fНаш клан принял запрос " + clansender.replace("&", "§") + " §fна сражение!");
            ClanWars.warStarter(clansender, clanname);
            return true;
        }
        if (args[0].equalsIgnoreCase("no")) {
            if (!FClans.getClanManager().playersClan.containsKey(player.getName())) {
                player.sendMessage(" §c▷ §fВы не состоите в клане!");
                return true;
            }
            String clanname = FClans.getClanManager().playersClan.get(player.getName());
            if (!FClans.getClanManager().getOwner(clanname).equalsIgnoreCase(player.getName())) {
                player.sendMessage(" §c▷ §fВы не являетесь владельцем клана!");
                return true;
            }
            if (!this.warrequest.containsKey(clanname)) {
                player.sendMessage(" §c▷ §fВам не отправляли запрос о сражении на арене!");
                return true;
            }
            String clansender = this.warrequest.get(clanname);
            Methods.messageToClanPlayers(clansender, " §c▷ §fКлан " + clanname.replace("&", "§") + " §fотклонил запрос на сражение!");
            Methods.messageToClanPlayers(clanname, " §c▷ §fНаш клан отклонил запрос " + clansender.replace("&", "§") + " §fна сражение!");
            this.warrequest.remove(clanname);
            return true;
        }
        if (args[0].equalsIgnoreCase("deposit")) {
            if (!FClans.getClanManager().playersClan.containsKey(player.getName())) {
                player.sendMessage(" §c▷ §fВы не состоите в клане!");
                return true;
            }
            if (args.length == 1) {
                player.sendMessage(" §c▷ §fУкажите сумму!");
                return true;
            }
            String clanname = FClans.getClanManager().playersClan.get(player.getName());
            Pattern pattern = Pattern.compile("^([+-]?[1-9]\\d*|0)$");
            Matcher matcher = pattern.matcher(args[1]);
            if (!matcher.find()) {
                player.sendMessage(" §c▷ §fУкажите правильное число!");
                return true;
            }
            String prenumber = args[1].substring(matcher.start(), matcher.end());
            if (prenumber.length() > 5) {
                player.sendMessage(" §c▷ §fУкажите правильное число!");
                return true;
            }
            int balance = Integer.parseInt(prenumber);
            if (FClans.getEconomy().getBalance(player) < balance) {
                player.sendMessage(" §c▷ §fУ Вас недостаточно денег!");
                return true;
            }
            if (balance < 0) {
                player.sendMessage(" §c▷ §fУкажите правильное число!");
                return true;
            }
            if (FClans.getClanManager().getBalance(clanname) > 500000) {
                player.sendMessage(" §c▷ §fБаланс банка клана не может превышать §e§f!");
                return true;
            }
            if (FClans.getClanManager().getBalance(clanname) + balance > 500000) {
                player.sendMessage(" §c▷ §fПроизведя перевод на эту сумму Вы превысите лимит баланса банка клана в §e§f!");
                return true;
            }
            FClans.getEconomy().withdrawPlayer(player, balance);
            FClans.getClanManager().addBalance(clanname, balance);
            FClans.getClanManager().getPlayers(clanname).get(player.getName()).setDeposit(balance + FClans.getClanManager().getPlayers(clanname).get(player.getName()).getDeposit());
            FClans.getClanManager().savePlayers(clanname);
            Methods.messageToClanPlayers(clanname, " §c▷ §fИгрок §c" + player.getName() + " §fположил §e⛂" + balance + " §fна счёт клана!");
            return true;
        }
        if (args[0].equalsIgnoreCase("withdraw")) {
            int clanbalance;
            if (!FClans.getClanManager().playersClan.containsKey(player.getName())) {
                player.sendMessage(" §c▷ §fВы не состоите в клане!");
                return true;
            }
            String clanname = FClans.getClanManager().playersClan.get(player.getName());
            if (!FClans.getClanManager().getOwner(clanname).equalsIgnoreCase(player.getName())) {
                player.sendMessage(" §c▷ §fВы не являетесь владельцем клана!");
                return true;
            }
            if (args.length == 1) {
                player.sendMessage(" §c▷ §fУкажите сумму!");
                return true;
            }
            Pattern pattern = Pattern.compile("^([+-]?[1-9]\\d*|0)$");
            Matcher matcher = pattern.matcher(args[1]);
            if (!matcher.find()) {
                player.sendMessage(" §c▷ §fУкажите правильное число!");
                return true;
            }
            String prenumber = args[1].substring(matcher.start(), matcher.end());
            if (prenumber.length() > 5) {
                player.sendMessage(" §c▷ §fУкажите правильное число!");
                return true;
            }
            int amount = Integer.parseInt(prenumber);
            if (amount > (clanbalance = FClans.getClanManager().getBalance(clanname))) {
                player.sendMessage(" §c▷ §fНа счету клана недостаточно денег!");
                return true;
            }
            if (amount < 0) {
                player.sendMessage(" §c▷ §fУкажите правильное число!");
                return true;
            }
            FClans.getClanManager().removeBalance(clanname, amount);
            FClans.getEconomy().depositPlayer(player, amount);
            Methods.messageToClanPlayers(clanname, " §c▷ §fИгрок §c" + player.getName() + " §fснял §e⛂" + amount + " §fс счёта клана!");
            return true;
        }
        if (args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("chat")) {
            if (!FClans.getClanManager().playersClan.containsKey(player.getName())) {
                player.sendMessage(" §c▷ §fВы не состоите в клане!");
                return true;
            }
            if (args.length == 1) {
                player.sendMessage(" §c▷ §fВведите сообщение!");
                return true;
            }
        } else {
            if (args[0].equalsIgnoreCase("create")) {
                if (FClans.getClanManager().playersClan.containsKey(player.getName())) {
                    player.sendMessage(" §c▷ §fВы состоите в клане!");
                    return true;
                }
                if (args.length == 1) {
                    player.sendMessage(" §c▷ §fУкажите название клана!");
                    return true;
                }
                if (args[1].length() > 12) {
                    player.sendMessage(" §c▷ §fМаксимальная длинна клана - 12 символов!");
                    return true;
                }
                if (FClans.getEconomy().getBalance(player) < 5000.0) {
                    player.sendMessage(" §c▷ §fДля создания клана необходимо §e倀");
                    return true;
                }
                FClans.getEconomy().withdrawPlayer(player, 10000.0);
                FClans.getClanManager().createClan(args[1], player.getName());
                Menu.clanMenu(player, FClans.getClanManager().playersClan.get(player.getName()));
                player.sendMessage(" §a▷ §fВы успешно создали клан!");
                return true;
            }
            if (args[0].equalsIgnoreCase("menu")) {
                if (!FClans.getClanManager().playersClan.containsKey(player.getName())) {
                    player.sendMessage(" §c▷ §fВы не состоите в клане!");
                    return true;
                }
                Menu.clanMenu(player, FClans.getClanManager().playersClan.get(player.getName()));
                return true;
            }
            if (args[0].equalsIgnoreCase("invite")) {
                if (!FClans.getClanManager().playersClan.containsKey(player.getName())) {
                    player.sendMessage(" §c▷ §fВы не состоите в клане!");
                    return true;
                }
                String clanname = FClans.getClanManager().playersClan.get(player.getName());
                if (!FClans.getClanManager().getOwner(clanname).equalsIgnoreCase(player.getName())) {
                    player.sendMessage(" §c▷ §fВы не являетесь создателем клана!");
                    return true;
                }
                if (args.length == 1) {
                    player.sendMessage(" §c▷ §fУкажите игрока!");
                    return true;
                }
                if (FClans.getClanManager().getPlayers(clanname).size() >= 20) {
                    player.sendMessage(" §c▷ §fДостигнуто максимальное количество игроков в клане!");
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    player.sendMessage(" §c▷ §fИгрок должен быть онлайн!");
                    return true;
                }
                if (FClans.getClanManager().getPlayers(clanname).containsKey(target.getName())) {
                    player.sendMessage(" §c▷ §fИгрок уже находится в Вашем клане!");
                    return true;
                }
                if (FClans.getClanManager().playersClan.containsKey(target.getName())) {
                    player.sendMessage(" §c▷ §fИгрок уже находится в другом клане!");
                    return true;
                }
                if (this.inrequest.containsKey(target.getName())) {
                    player.sendMessage(" §c▷ §fИгроку уже отправили запрос о вступлении в клан!");
                    return true;
                }
                this.inrequest.put(target.getName(), clanname);
                Bukkit.getScheduler().runTaskLater(FClans.getInstance(), () -> this.inrequest.remove(target.getName()), 1200L);
                ClickEvent accept = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/c accept");
                ClickEvent decline = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/c decline");
                HoverEvent hovera = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aНажмите сюда, чтобы принять запрос").create());
                HoverEvent hoverd = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cНажмите сюда, чтобы отклонить запрос").create());
                BaseComponent[] comp = new ComponentBuilder("§f§lНажмите ").append("§a§l[ПРИНЯТЬ]").event(accept).event(hovera).append(" §f§lили ").append("§c§l[ОТКЛОНИТЬ]").event(decline).event(hoverd).create();
                player.sendMessage(" §c▷ §fВы пригласили игрока §c" + target.getName());
                target.sendMessage("§f=============================================");
                target.sendMessage("§f§lВас пригласили в клан §r " + clanname.replace("&", "§") + "§f§l!");
                target.spigot().sendMessage(comp);
                target.sendMessage("§f§lИли используйте §c§l/c accept §f§lили §c§l/c decline");
                target.sendMessage("§f=============================================");
                return true;
            }
            if (args[0].equalsIgnoreCase("accept")) {
                if (!this.inrequest.containsKey(player.getName())) {
                    player.sendMessage(" §c▷ §fВам не отправляли запрос на вступление в клан, либо он истёк!");
                    return true;
                }
                String clanname = this.inrequest.get(player.getName());
                FClans.getClanManager().addPlayer(clanname, player.getName());
                Methods.messageToClanPlayers(clanname, " §c▷ §fИгрок §c" + player.getName() + " §fвступил в клан!");
                this.inrequest.remove(player.getName());
                return true;
            }
            if (args[0].equalsIgnoreCase("decline")) {
                if (!this.inrequest.containsKey(player.getName())) {
                    player.sendMessage(" §c▷ §fВам не отправляли запрос на вступление в клан, либо он истёк!");
                    return true;
                }
                player.sendMessage(" §c▷ §fВы отказались от вступления в клан " + this.inrequest.get(player.getName()).replace("&", "§") + "§f!");
                this.inrequest.remove(player.getName());
                return true;
            }
            if (args[0].equalsIgnoreCase("leave")) {
                if (!FClans.getClanManager().playersClan.containsKey(player.getName())) {
                    player.sendMessage(" §c▷ §fВы не состоите в клане!");
                    return true;
                }
                String clanname = FClans.getClanManager().playersClan.get(player.getName());
                if (FClans.getClanManager().getOwner(clanname).equalsIgnoreCase(player.getName())) {
                    player.sendMessage(" §c▷ §fВы не можете покинуть свой клан!");
                    return true;
                }
                Methods.messageToClanPlayers(clanname, " §c▷ §fИгрок §c" + player.getName() + " §fпокинул клан!");
                FClans.getClanManager().removePlayer(clanname, player.getName());
                return true;
            }
            if (args[0].equalsIgnoreCase("top")) {
                FClans.getClanManager().updateStat();
                player.sendMessage("");
                player.sendMessage("§a     §8§m          §e  [Топ кланов] §8§m          §e");
//                FClans.getClanManager().sortedClans.forEach(player::sendMessage);
                if (FClans.getClanManager().clans == null){
                    player.sendMessage("упс кланов нема");
                }else {
                    FClans.getClanManager().sortedClans.forEach(player::sendMessage);

                }
                player.sendMessage("§a     §8§m                                  §a");
                player.sendMessage("");
                return true;
            }
            if (args[0].equalsIgnoreCase("sethome")) {
                if (!FClans.getClanManager().playersClan.containsKey(player.getName())) {
                    player.sendMessage(" §c▷ §fВы не состоите в клане!");
                    return true;
                }
                String clanname = FClans.getClanManager().playersClan.get(player.getName());
                if (!FClans.getClanManager().getOwner(clanname).equalsIgnoreCase(player.getName())) {
                    player.sendMessage(" §c▷ §fВы не являетесь создателем клана!");
                    return true;
                }
                FClans.getClanManager().setHome(player.getLocation(), clanname);
                player.sendMessage(" §c▷ §fТочка локации спавна дома установлена!");
                return true;
            }
            if (args[0].equalsIgnoreCase("home")) {
                if (!FClans.getClanManager().playersClan.containsKey(player.getName())) {
                    player.sendMessage(" §c▷ §fВы не состоите в клане!");
                    return true;
                }
                String clanname = FClans.getClanManager().playersClan.get(player.getName());
                if (FClans.getClanManager().getHome(clanname) == null) {
                    player.sendMessage(" §c▷ §fДом клана не установлен!");
                    return true;
                }
                player.teleport(FClans.getClanManager().getHome(clanname));
                player.sendMessage(" §c▷ §fВы телепортированы в клановый дом!");
                return true;
            }
            this.message(player);
            return true;
        }
        String clanname = FClans.getClanManager().playersClan.get(player.getName());
        String message = StringUtils.join(args, " ", 1, args.length);
        String prefixplayer = Methods.getRankByInt(FClans.getClanManager().getPlayers(clanname).get(player.getName()).getRank()) + " §e" + player.getName();
        String format = prefixplayer + " §7> §f" + message;
        Methods.messageToClanPlayers(clanname, format);
        return true;
    }

    private void message(Player player) {
        player.sendMessage("");
        if (!FClans.getClanManager().playersClan.containsKey(player.getName())) {
            player.sendMessage(" §c▷ §fСоздать клан §7- §a/c create название");
            player.sendMessage(" §c▷ §fТоп кланов §7- §a/c top");
            player.sendMessage("");
            return;
        }
        String clanname = FClans.getClanManager().playersClan.get(player.getName());
        player.sendMessage(" §c▷ §fМеню клана §7- §a/c menu");
        player.sendMessage(" §c▷ §fТелепортироваться в клановый дом §7- §a/c home");
        player.sendMessage(" §c▷ §fПокинуть клан §7- §a/c leave");
        player.sendMessage(" §c▷ §fЧат клана §7- §a/c c сообщение");
        player.sendMessage(" §c▷ §fПоложить монетки на счёт клана §7- §a/c deposit сумма");
        player.sendMessage(" §c▷ §fТоп кланов §7- §a/c top");
        if (FClans.getClanManager().getOwner(clanname).equalsIgnoreCase(player.getName())) {
            player.sendMessage(" §c▷ §fУстановить локацию дома клана §7- §a/c sethome");
            player.sendMessage(" §c▷ §fСнять монетки со счёта клана §7- §a/c withdraw сумма");
            player.sendMessage(" §c▷ §fНачать войну с другим кланом §7- §a/c war ID");
            player.sendMessage(" §c▷ §fПригласить игрока в клан §7- §a/c invite игрок");
        }
        player.sendMessage("");
    }
}

