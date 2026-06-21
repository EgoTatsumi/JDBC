package com.dnd.service;

import com.dnd.dao.*;
import com.dnd.model.*;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Scanner;

public class ConsoleInterface {

    private final PersonDao personDao = new PersonDao();
    private final MonsterDao monsterDao = new MonsterDao();
    private final CompanyDao companyDao = new CompanyDao();
    private final SessionDao sessionDao = new SessionDao();
    private final ItemDao itemDao = new ItemDao();

    private final BusinessQueryService queryService = new BusinessQueryService();
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== D&D ИГРОВОЙ ЦЕНТР ===");
            System.out.println("1. Управление сущностями (CRUD)");
            System.out.println("2. Бизнес-запросы (Аналитика)");
            System.out.println("0. Выход");
            System.out.print("Выбор: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> showCrudMenu();
                case "2" -> showBusinessQueriesMenu();
                case "0" -> running = false;
                default -> System.out.println("Ошибка ввода. Повторите.");
            }
        }
    }

    private void showCrudMenu() {
        boolean inCrud = true;
        while (inCrud) {
            System.out.println("\n--- РАБОТА С СУЩНОСТЯМИ ---");
            System.out.println("1. Персонажи (Герои)");
            System.out.println("2. Монстры (Бестиарий)");
            System.out.println("3. Кампании (Приключения)");
            System.out.println("4. Игровые Сессии");
            System.out.println("5. Магические Предметы");
            System.out.println("0. Назад");
            System.out.print("Выбор категории: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> personCrudSubMenu();
                case "2" -> monsterCrudSubMenu();
                case "3" -> companyCrudSubMenu();
                case "4" -> sessionCrudSubMenu();
                case "5" -> itemCrudSubMenu();
                case "0" -> inCrud = false;
                default -> System.out.println("Неверный пункт.");
            }
        }
    }

    // === ПЕРСОНАЖИ ===
    private void personCrudSubMenu() {
        System.out.println("\n[Герои] 1.Список | 2.Создать | 3.Уровень | 4.Удалить");
        System.out.print("Действие: ");
        switch (scanner.nextLine()) {
            case "1" -> printAllCharacters();
            case "2" -> createCharacter();
            case "3" -> updateCharacterLevel();
            case "4" -> deleteCharacter();
        }
    }

    private void printAllCharacters() {
        System.out.println("\nID | Имя | ID Класса | ID Расы | Уровень");
        List<Person> heroes = personDao.findAll();
        for (Person p : heroes) {
            System.out.printf("%d | %s | %d | %d | %d%n",
                    p.getIdPerson(), p.getName(), p.getIdClass(), p.getIdRace(), p.getLevel());
        }
    }

    private void createCharacter() {
        System.out.print("Имя: ");
        String name = scanner.nextLine();
        System.out.print("Уровень: ");
        short level = Short.parseShort(scanner.nextLine());
        // Для демонстрации привязываем к первому классу и расе (ID 1)
        Person p = personDao.save(new Person(0, 1, 1, level, name));
        System.out.println("Создан герой с ID: " + p.getIdPerson());
    }

    private void updateCharacterLevel() {
        System.out.print("ID героя: ");
        int id = Integer.parseInt(scanner.nextLine());
        personDao.findById(id).ifPresentOrElse(p -> {
            System.out.print("Новый уровень: ");
            p.setLevel(Short.parseShort(scanner.nextLine()));
            personDao.update(p);
            System.out.println("Уровень изменен.");
        }, () -> System.out.println("Не найден."));
    }

    private void deleteCharacter() {
        System.out.print("ID для удаления: ");
        int id = Integer.parseInt(scanner.nextLine());
        if (personDao.deleteById(id)) System.out.println("Удален.");
        else System.out.println("Не найден.");
    }

    // === МОНСТРЫ ===
    private void monsterCrudSubMenu() {
        System.out.println("\n[Бестиарий] 1.Список | 2.Добавить | 3.Сила | 4.Удалить");
        System.out.print("Действие: ");
        switch (scanner.nextLine()) {
            case "1" -> printAllMonsters();
            case "2" -> createMonster();
            case "3" -> updateMonster();
            case "4" -> deleteMonster();
        }
    }

    private void printAllMonsters() {
        System.out.println("\nИмя | Уровень | Сила");
        List<Monster> monsters = monsterDao.findAll();
        for (Monster m : monsters) {
            System.out.printf("%s | %d | %d%n", m.getName(), m.getLevel(), m.getStrength());
        }
    }

    private void createMonster() {
        System.out.print("Имя: ");
        String name = scanner.nextLine();
        System.out.print("Уровень опасности: ");
        short lvl = Short.parseShort(scanner.nextLine());
        System.out.print("Сила: ");
        short str = Short.parseShort(scanner.nextLine());
        monsterDao.save(new Monster(name, str, lvl));
        System.out.println("Монстр добавлен.");
    }

    private void updateMonster() {
        System.out.print("Имя монстра: ");
        String name = scanner.nextLine();
        monsterDao.findById(name).ifPresentOrElse(m -> {
            System.out.print("Новая сила: ");
            m.setStrength(Short.parseShort(scanner.nextLine()));
            monsterDao.update(m);
            System.out.println("Обновлено.");
        }, () -> System.out.println("Не найден."));
    }

    private void deleteMonster() {
        System.out.print("Имя для удаления: ");
        String name = scanner.nextLine();
        if (monsterDao.deleteById(name)) System.out.println("Удален.");
        else System.out.println("Не найден.");
    }

    // === КАМПАНИИ ===
    private void companyCrudSubMenu() {
        System.out.println("\n[Приключения] 1.Список | 2.Создать | 3.Сеттинг | 4.Удалить");
        System.out.print("Действие: ");
        switch (scanner.nextLine()) {
            case "1" -> printAllCompanies();
            case "2" -> createCompany();
            case "3" -> updateCompany();
            case "4" -> deleteCompany();
        }
    }

    private void printAllCompanies() {
        System.out.println("\nID | Название кампании");
        List<Company> companies = companyDao.findAll();
        for (Company c : companies) {
            System.out.printf("%d | %s%n", c.getIdCompany(), c.getTitle());
        }
    }

    private void createCompany() {
        System.out.print("Название: ");
        String title = scanner.nextLine();
        System.out.print("Сеттинг: ");
        String setting = scanner.nextLine();
        Company c = companyDao.save(new Company(0, title, setting));
        System.out.println("Кампания создана. ID: " + c.getIdCompany());
    }

    private void updateCompany() {
        System.out.print("ID кампании: ");
        int id = Integer.parseInt(scanner.nextLine());
        companyDao.findById(id).ifPresentOrElse(c -> {
            System.out.print("Новое описание сеттинга: ");
            c.setSetting(scanner.nextLine());
            companyDao.update(c);
            System.out.println("Изменено.");
        }, () -> System.out.println("Не найдена."));
    }

    private void deleteCompany() {
        System.out.print("ID для удаления: ");
        int id = Integer.parseInt(scanner.nextLine());
        if (companyDao.deleteById(id)) System.out.println("Удалена.");
        else System.out.println("Не найдена.");
    }

    // === СЕССИИ ===
    private void sessionCrudSubMenu() {
        System.out.println("\n[Сессии] 1.Список | 2.Назначить | 3.Перенести | 4.Отменить");
        System.out.print("Действие: ");
        switch (scanner.nextLine()) {
            case "1" -> printAllSessions();
            case "2" -> createSession();
            case "3" -> updateSession();
            case "4" -> deleteSession();
        }
    }

    private void printAllSessions() {
        System.out.println("\nID | Локация | Дата");
        List<Session> sessions = sessionDao.findAll();
        for (Session s : sessions) {
            System.out.printf("%d | %s | %s%n", s.getIdSession(), s.getAddress(), s.getDate());
        }
    }

    private void createSession() {
        System.out.print("Адрес проведения: ");
        String addr = scanner.nextLine();
        System.out.print("Описание: ");
        String content = scanner.nextLine();
        // Привязываем к первой попавшейся кампании (ID 1)
        Session s = sessionDao.save(new Session(0, 1, content, addr, OffsetDateTime.now().plusDays(7)));
        System.out.println("Сессия добавлена. ID: " + s.getIdSession());
    }

    private void updateSession() {
        System.out.print("ID сессии: ");
        int id = Integer.parseInt(scanner.nextLine());
        sessionDao.findById(id).ifPresentOrElse(s -> {
            System.out.print("Новый адрес: ");
            s.setAddress(scanner.nextLine());
            sessionDao.update(s);
            System.out.println("Адрес перенесен.");
        }, () -> System.out.println("Не найдена."));
    }

    private void deleteSession() {
        System.out.print("ID для удаления: ");
        int id = Integer.parseInt(scanner.nextLine());
        if (sessionDao.deleteById(id)) System.out.println("Отменена.");
        else System.out.println("Не найдена.");
    }

    // === ПРЕДМЕТЫ ===
    private void itemCrudSubMenu() {
        System.out.println("\n[Инвентарь] 1.Список | 2.Выдать | 3.Свойства | 4.Удалить");
        System.out.print("Действие: ");
        switch (scanner.nextLine()) {
            case "1" -> printAllItems();
            case "2" -> createItem();
            case "3" -> updateItem();
            case "4" -> deleteItem();
        }
    }

    private void printAllItems() {
        System.out.println("\nID | Свойства артефакта | Вес");
        List<Item> items = itemDao.findAll();
        for (Item i : items) {
            System.out.printf("%d | %s | %d%n", i.getIdItem(), i.getMagicProperties(), i.getWeight());
        }
    }

    private void createItem() {
        System.out.print("Описание артефакта: ");
        String prop = scanner.nextLine();
        System.out.print("Вес: ");
        short weight = Short.parseShort(scanner.nextLine());
        // Выдаем персонажу с ID 1
        Item i = itemDao.save(new Item(0, 1, weight, prop));
        System.out.println("Предмет выдан.");
    }

    private void updateItem() {
        System.out.print("ID артефакта: ");
        int id = Integer.parseInt(scanner.nextLine());
        itemDao.findById(id).ifPresentOrElse(i -> {
            System.out.print("Новые свойства: ");
            i.setMagicProperties(scanner.nextLine());
            itemDao.update(i);
            System.out.println("Изменено.");
        }, () -> System.out.println("Не найден."));
    }

    private void deleteItem() {
        System.out.print("ID для удаления: ");
        int id = Integer.parseInt(scanner.nextLine());
        if (itemDao.deleteById(id)) System.out.println("Уничтожен.");
        else System.out.println("Не найден.");
    }

    // === АНАЛИТИКА ===
    private void showBusinessQueriesMenu() {
        boolean inQueries = true;
        while (inQueries) {
            System.out.println("\n--- АНАЛИТИКА JPQL ---");
            System.out.println("1.Монстры | 2.Расы | 3.Топ Героев | 4.Предметы | 5.Сессии");
            System.out.println("6.Классы  | 7.Сила | 8.Одиночки   | 9.Резерв   | 10.Кампании");
            System.out.println("0.Назад");
            System.out.print("Запрос: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> queryService.fightsByMonster();
                case "2" -> queryService.raceLevelStatistics();
                case "3" -> queryService.topHighLevelCharacters();
                case "4" -> queryService.charactersWithItemCount();
                case "5" -> queryService.sessionsWithDetails();
                case "6" -> queryService.classPopularityInFights();
                case "7" -> queryService.avgMonsterStrengthBySession();
                case "8" -> queryService.lonelyCharactersWithoutCompanies();
                case "9" -> queryService.unusedMonstersForSessions();
                case "10" -> queryService.companyStatistics();
                case "0" -> inQueries = false;
                default -> System.out.println("Неверный выбор.");
            }
        }
    }
}