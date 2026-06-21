package com.dnd;

import com.dnd.db.ConnectionManager;
import com.dnd.db.SchemaInitializer;
import com.dnd.service.ConsoleInterface;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== JDBC D&D ИГРОВОЙ ЦЕНТР (Java 21 · PostgreSQL 17 · HikariCP) ===\n");

        try {
            // Вызываем правильный метод initialize() вместо старого initDatabase()
            SchemaInitializer.initialize();
            System.out.println("БД готова.\n");

            // Запуск упрощенного консольного интерфейса
            ConsoleInterface ui = new ConsoleInterface();
            ui.start();

        } catch (Exception e) {
            System.err.println("Критическая ошибка во время работы приложения: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Закрытие пула соединений HikariCP при выходе из меню
            ConnectionManager.close();
            System.out.println("\nHikariCP connection pool закрыт. Программа успешно завершена.");
        }
    }
}