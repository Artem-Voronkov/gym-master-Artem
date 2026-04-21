import java.util.*;

public class App {
private static final Scanner scanner = new Scanner(System.in);
private static final Timetable timetable = new Timetable();

public static void main(String[] args) {
    System.out.println("---Панель управления---");

    boolean running = true;
    while (running) {
        displayAdminMenu();
        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1:
                viewTrainingsByDay();
                break;
            case 2:
                viewTrainingsByDayAndTime();
                break;
            case 3:
                addNewTraining();
                break;
            case 4:
                viewCoachWeeklyStats();
                break;
            case 0:
                System.out.println("Выход из администраторского режима. До свидания!");
                running = false;
                break;
            default:
                System.out.println("Неверный выбор. Попробуйте снова.");
        }
        System.out.println();
    }

}

private static void displayAdminMenu() {
    System.out.println("\n=== АДМИНИСТРАТОРСКОЕ МЕНЮ ===");
    System.out.println("1. Посмотреть тренировки за день (упорядоченные по времени)");
    System.out.println("2. Посмотреть тренировки в конкретное время за день");
    System.out.println("3. Добавить новую тренировку");
    System.out.println("4. Статистика: количество занятий по тренерам за неделю");
    System.out.println("0. Выход");
    System.out.print("Выберите действие: ");
}

    // 1. Получение всех тренировок за день, упорядоченных по времени начала
    private static void viewTrainingsByDay() {
        System.out.println("\n--- ТРЕНИРОВКИ ЗА ДЕНЬ ---");
        System.out.print("Введите день недели (MONDAY, TUESDAY, ...): ");

            DayOfWeek day = DayOfWeek.valueOf(scanner.nextLine().toUpperCase());
            List<TrainingSession> sessions = timetable.getTrainingSessionsForDay(day);

            if (sessions.isEmpty()) {
                System.out.println("В этот день тренировок нет.");
                return;
            }

            System.out.printf("\nТРЕНИРОВКИ НА %s:\n", day);
            for (TrainingSession session : sessions) {
                Group group = session.getGroup();
                Coach coach = session.getCoach();
                TimeOfDay time = session.getTimeOfDay();
                System.out.printf("  %02d:%02d — %s (тренер: %s %s %s, %d мин)\n",
                        time.getHours(), time.getMinutes(),
                        group.getTitle(),
                        coach.getSurname(), coach.getName(), coach.getMiddleName(),
                        group.getDuration());
            }
    }

    private static void viewTrainingsByDayAndTime() {
        System.out.println("\n--- ТРЕНИРОВКИ В КОНКРЕТНОЕ ВРЕМЯ ---");
        System.out.print("Введите день недели: ");
        DayOfWeek day;

        day = DayOfWeek.valueOf(scanner.nextLine().toUpperCase());

        System.out.print("Часы (0–23): ");
        int hours = Integer.parseInt(scanner.nextLine());
        System.out.print("Минуты (0–59): ");
        int minutes = Integer.parseInt(scanner.nextLine());

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(
                day, new TimeOfDay(hours, minutes)
        );

        if (sessions.isEmpty()) {
            System.out.printf("В %s в %02d:%02d тренировок нет.\n", day, hours, minutes);
            return;
        }

        System.out.printf("\nТРЕНИРОВКИ В %s В %02d:%02d:\n", day, hours, minutes);
        for (TrainingSession session : sessions) {
            Group group = session.getGroup();
            Coach coach = session.getCoach();
            System.out.printf("  - %s (тренер: %s, %d мин)\n",
                    group.getTitle(), coach.getSurname(), group.getDuration());
        }
    }

    private static void addNewTraining() {
        System.out.println("\n--- ДОБАВЛЕНИЕ НОВОЙ ТРЕНИРОВКИ ---");

        System.out.print("Название группы: ");
        String groupTitle = scanner.nextLine();

        System.out.print("Возрастная категория (ADULT/CHILD): ");
        Age ageCategory = Age.valueOf(scanner.nextLine().toUpperCase());

        System.out.print("Длительность (в минутах): ");
        int duration = Integer.parseInt(scanner.nextLine());
        Group group = new Group(groupTitle, ageCategory, duration);

        System.out.print("Фамилия тренера: ");
        String surname = scanner.nextLine();
        System.out.print("Имя тренера: ");
        String name = scanner.nextLine();
        System.out.print("Отчество тренера: ");
        String middleName = scanner.nextLine();
        Coach coach = new Coach(surname, name, middleName);

        System.out.print("День недели: ");
        DayOfWeek day = DayOfWeek.valueOf(scanner.nextLine().toUpperCase());

        System.out.print("Часы: ");
        int hours = Integer.parseInt(scanner.nextLine());
        System.out.print("Минуты: ");
        int minutes = Integer.parseInt(scanner.nextLine());
        TimeOfDay time = new TimeOfDay(hours, minutes);

        TrainingSession session = new TrainingSession(group, coach, day, time);
        timetable.addNewTrainingSession(session);

        System.out.println("Тренировка успешно добавлена в расписание!");
    }

    private static void viewCoachWeeklyStats() {
        System.out.println("\n--- СТАТИСТИКА: КОЛИЧЕСТВО ЗАНЯТИЙ ПО ТРЕНЕРАМ ЗА НЕДЕЛЮ ---");
        List<Timetable.CounterOfTrainings> stats = timetable.getCountByCoaches();

        if (stats.isEmpty()) {
            System.out.println("Нет данных о тренировках.");
            return;
        }

        System.out.println("ТРЕНЕРЫ ПО КОЛИЧЕСТВУ ТРЕНИРОВОК (по убыванию):");
        int count = 1;
        for (Timetable.CounterOfTrainings stat : stats) {
            System.out.printf("%d место: %s\n", count, stat);
            count++;
        }
    }
}

