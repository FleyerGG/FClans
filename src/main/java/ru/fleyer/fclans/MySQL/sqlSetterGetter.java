package ru.fleyer.fclans.MySQL;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ru.fleyer.fclans.FClans;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class sqlSetterGetter {



    static FClans plugin = FClans.getInstance();


    public static boolean playerExists(UUID uuid) {
        try {
            PreparedStatement statement = FClans.mysql.getConnection()
                    .prepareStatement("SELECT * FROM `test` WHERE UUID=?");
            statement.setString(1, uuid.toString());

            ResultSet results = statement.executeQuery();
            if (results.next()) {
                plugin.getServer().broadcastMessage(ChatColor.YELLOW + "Player Found");
                return true;
            }
            plugin.getServer().broadcastMessage(ChatColor.RED + "Player NOT Found");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void createPlayer(final UUID uuid,  Player player, String url, String picture) {
        try {
            PreparedStatement statement = FClans.mysql.getConnection()
                    .prepareStatement("SELECT * FROM `test` WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            System.out.print(1);
            if (playerExists(uuid) == false) {
                PreparedStatement insert = FClans.mysql.getConnection()
                        .prepareStatement("INSERT INTO `test` (UUID,NAME,URL,PICTURES) VALUES (?,?,?,?)");
                insert.setString(1, uuid.toString());
                insert.setString(2, player.getName());
                insert.setString(3, url);
                insert.setString(4, picture);
                insert.executeUpdate();

                plugin.getServer().broadcastMessage(ChatColor.GREEN + "Player Inserted");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public static List<String> getClan(UUID uuid, String clan){
        List<String> urllist = new ArrayList<>();
        String hystory = "SELECT * FROM test WHERE CLAN = '" + clan + "' AND UUID = '"+ uuid + "';";

        try {
            PreparedStatement statement = FClans.mysql.getConnection().prepareStatement(hystory);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                urllist.add(resultSet.getString("URL"));
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return urllist;
    }

}
