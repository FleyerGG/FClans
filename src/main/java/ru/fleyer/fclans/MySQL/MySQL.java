package ru.fleyer.fclans.MySQL;


import java.sql.*;
import java.util.Properties;

public class MySQL {
    private String host;
    private String username;
    private String password;
    private String database;
    private Connection connection;
    private String port;

    public MySQL(String host, String username, String password, String database, String port) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.database = database;
        this.port = port;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        this.execute("CREATE TABLE IF NOT EXISTS `test` (`id` int NOT NULL AUTO_INCREMENT, `UUID` varchar(255) NOT NULL, `NAME` varchar(255) NOT NULL, `URL` varchar(255) NOT NULL, `PICTURES` varchar(255) NOT NULL, PRIMARY KEY (`id`)) DEFAULT CHARSET=utf8 COLLATE utf8_bin AUTO_INCREMENT=0");
    }

    public Connection getConnection() {
        try {
            if (this.connection != null && !this.connection.isClosed() && this.connection.isValid(20)) {
                return this.connection;
            }
            Properties p = new Properties();
            p.setProperty("user", this.username);
            p.setProperty("password", this.password);
            p.setProperty("useUnicode", "true");
            p.setProperty("characterEncoding", "cp1251");
            p.setProperty("autoReconnect", "true");
            this.connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, p);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.connection;
    }

    public void execute(String query) {
        try {
            PreparedStatement statement = this.getConnection().prepareStatement(query);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void disconnect() {
        try {
            this.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


