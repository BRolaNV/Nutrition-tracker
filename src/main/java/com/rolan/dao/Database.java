package com.rolan.dao;

import java.sql.*;

public class Database {

    public static final String DB_URL = "jdbc:sqlite:nutrition.db";

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(DB_URL);
    }

    public static void createTables(){
        String createUserTable = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL
                );
                """;

        String createUserTargetsTable = """
                CREATE TABLE IF NOT EXISTS user_targets (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    protein REAL NOT NULL,
                    fat REAL NOT NULL,
                    carbohydrates REAL NOT NULL,
                    fiber REAL NOT NULL,
                    date TEXT NOT NULL,
                    FOREIGN KEY (user_id) REFERENCES users(id)
                );
                """;

        String createMealEntriesTable = """
                CREATE TABLE IF NOT EXISTS meal_entries (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    name TEXT NOT NULL,
                    protein REAL NOT NULL,
                    fat REAL NOT NULL,
                    carbohydrates REAL NOT NULL,
                    fiber REAL NOT NULL,
                    date TEXT NOT NULL,
                    FOREIGN KEY (user_id) REFERENCES users(id)
                );
                """;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(createUserTable);
            statement.execute(createMealEntriesTable);
            statement.execute(createUserTargetsTable);

            System.out.println("Tables were created successfully");
        } catch (SQLException e){
            System.err.println("Error creating tables: " + e.getMessage());
        }
    }
}
