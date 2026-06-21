package javafxmvc.model.database;

public class DatabaseFactory {
    public static Database getDatabase(String nome) {
        return new DatabasePostgreSQL();
    }
}
