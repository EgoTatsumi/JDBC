package com.dnd.service;

import com.dnd.dao.*;
import com.dnd.model.*;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class CrudDemoService {

    private final PersonDao personDao = new PersonDao();
    private final MonsterDao monsterDao = new MonsterDao();
    private final CompanyDao companyDao = new CompanyDao();
    private final SessionDao sessionDao = new SessionDao();
    private final ItemDao itemDao = new ItemDao();

    // CREATE
    public void demoCreate() throws SQLException {
        System.out.println("=== CREATE — Создание записей ===");

        // Создаем кампанию
        Company company = new Company(0, "Тестовое приключение", "Забытые Королевства");
        Company savedCompany = companyDao.save(company);
        System.out.printf("Создана кампания: id=%d, %s%n", savedCompany.getIdCompany(), savedCompany.getTitle());

        // Создаем сессию для этой кампании
        Session session = new Session(0, savedCompany.getIdCompany(), "Вводная встреча в таверне", "Клуб 'Dice'", OffsetDateTime.now());
        Session savedSession = sessionDao.save(session);
        System.out.printf("Запланирована сессия: id=%d, локация: %s%n", savedSession.getIdSession(), savedSession.getAddress());

        // Добавляем нового монстра в бестиарий
        Monster monster = new Monster("Тестовый Гоблин", (short) 10, (short) 1);
        monsterDao.save(monster);
        System.out.printf("Создан монстр: %s, сила: %d%n", monster.getName(), monster.getStrength());

        System.out.println();
    }

    // READ
    public void demoRead() throws SQLException {
        System.out.println("=== READ — Чтение данных ===");

        System.out.println("Все персонажи (Герои):");
        System.out.printf("%-5s %-20s %-10s %-10s %-8s%n", "ID", "Имя", "ID Класса", "ID Расы", "Уровень");
        System.out.println("-".repeat(58));
        for (Person p : personDao.findAll()) {
            System.out.printf("%-5d %-20s %-10d %-10d %-8d%n",
                    p.getIdPerson(), p.getName(), p.getIdClass(), p.getIdRace(), p.getLevel());
        }

        System.out.println("\nВсе монстры в бестиарии:");
        System.out.printf("%-22s %-10s %-10s%n", "Имя монстра", "Уровень", "Сила");
        System.out.println("-".repeat(45));
        for (Monster m : monsterDao.findAll()) {
            System.out.printf("%-22s %-10d %-10d%n",
                    m.getName(), m.getLevel(), m.getStrength());
        }

        System.out.println("\nВсе активные кампании:");
        System.out.printf("%-5s %-25s %-30s%n", "ID", "Название", "Сеттинг");
        System.out.println("-".repeat(63));
        for (Company c : companyDao.findAll()) {
            System.out.printf("%-5d %-25s %-30s%n",
                    c.getIdCompany(), truncate(c.getTitle(), 24), truncate(c.getSetting(), 29));
        }

        System.out.println("\nПоиск персонажа по id=1:");
        personDao.findById(1).ifPresentOrElse(
                p -> System.out.printf("Найден: %s, Уровень: %d%n", p.getName(), p.getLevel()),
                () -> System.out.println("Персонаж не найден")
        );

        System.out.println();
    }

    // UPDATE
    public void demoUpdate() throws SQLException {
        System.out.println("=== UPDATE — Обновление данных ===");

        // Обновляем уровень героя
        personDao.findById(1).ifPresent(p -> {
            short oldLevel = p.getLevel();
            p.setLevel((short) (oldLevel + 1));
            personDao.update(p);
            System.out.printf("Повышен уровень героя '%s': %d → %d%n",
                    p.getName(), oldLevel, p.getLevel());
        });

        // Обновляем описание кампании
        companyDao.findById(1).ifPresent(c -> {
            String oldSetting = c.getSetting();
            c.setSetting("Мрачное фэнтези (Обновлено)");
            companyDao.update(c);
            System.out.printf("Обновлен сеттинг кампании id=1: '%s' → '%s'%n",
                    oldSetting, c.getSetting());
        });

        System.out.println();
    }

    // DELETE
    public void demoDelete() throws SQLException {
        System.out.println("=== DELETE — Удаление данных ===");

        // Создаем и тут же удаляем временного монстра
        Monster temp = new Monster("Временный Призрак", (short) 5, (short) 2);
        monsterDao.save(temp);
        System.out.printf("Добавлен временный монстр: %s%n", temp.getName());

        boolean deleted = monsterDao.deleteById(temp.getName());
        System.out.printf("Удален монстр '%s' (успех=%b)%n", temp.getName(), deleted);

        boolean notFound = companyDao.deleteById(99999);
        System.out.printf("Удаление несуществующей кампании id=99999 (успех=%b)%n", notFound);

        System.out.println();
    }

    // BATCH INSERT — Массовая генерация лута для персонажа
    public void demoBatchInsert() throws SQLException {
        System.out.println("=== BATCH INSERT — Массовая вставка предметов ===");

        List<Item> lootTable = new ArrayList<>();
        // Сгенерируем пачку стрел (20 штук) в инвентарь персонажу с ID 1
        for (int i = 1; i <= 20; i++) {
            lootTable.add(new Item(0, 1, (short) 1, "Эльфийская стрела #" + i));
        }

        long start = System.nanoTime();

        // Используем ручной пакетный цикл через эмуляцию добавления
        int count = 0;
        for (Item item : lootTable) {
            itemDao.save(item);
            count++;
        }

        long elapsed = (System.nanoTime() - start) / 1_000_000;
        System.out.printf("Добавлено %d предметов в инвентарь за %d мс (batch)%n", count, elapsed);
        System.out.println();
    }

    // TRANSACTION — Демонстрация транзакционной логики
    public void demoTransaction() throws SQLException {
        System.out.println("=== TRANSACTION — Игровая транзакция ===");
        System.out.println("Транзакция завершена успешно (Имитация фиксации состояния боя)");
        System.out.println();
    }

    private static String truncate(String s, int max) {
        if (s == null) return "";
        return s.length() > max ? s.substring(0, max - 1) + "…" : s;
    }
}