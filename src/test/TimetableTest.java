import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник вернулось одно занятие
        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySessions.size());
        assertEquals(singleTrainingSession, mondaySessions.get(0));
        //Проверить, что за вторник не вернулось занятий
        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySessions.size());
        assertEquals(mondayChildTrainingSession, mondaySessions.get(0));

        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        List<TrainingSession> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertEquals(2, thursdaySessions.size());
        assertEquals(thursdayChildTrainingSession, thursdaySessions.get(0)); // 13:00
        assertEquals(thursdayAdultTrainingSession, thursdaySessions.get(1)); // 20:00

        // Проверить, что за вторник не вернулось занятий
        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        // Проверить, что за понедельник в 13:00 вернулось одно занятие
        List<TrainingSession> sessions1300 = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        assertEquals(1, sessions1300.size());
        assertEquals(singleTrainingSession, sessions1300.get(0));

        // Проверить, что за понедельник в 14:00 не вернулось занятий
        List<TrainingSession> sessions1400 = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        assertTrue(sessions1400.isEmpty());
    }

    @Test
    void testGetCountByCoachesSingleCoach() {
        Timetable timetable = new Timetable();


        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group group1 = new Group("Группа 1", Age.ADULT, 60);
        Group group2 = new Group("Группа 2", Age.CHILD, 45);

        // Добавляем три занятия одного тренера в разные дни
        TrainingSession session1 = new TrainingSession(group1, coach,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        TrainingSession session2 = new TrainingSession(group2, coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(15, 0));
        TrainingSession session3 = new TrainingSession(group1, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(18, 0));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);
        timetable.addNewTrainingSession(session3);

        List<Timetable.CounterOfTrainings> stats = timetable.getCountByCoaches();

        assertEquals(1, stats.size());

        Timetable.CounterOfTrainings coachStat = stats.get(0);
        assertEquals(coach, coachStat.getCoach());
        assertEquals(3, coachStat.getCount());
    }

    @Test
    void testGetCountByCoachesMultipleCoachesDifferentCounts() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Иванов", "Иван", "Иванович"); // 2 занятия
        Coach coach2 = new Coach("Петров", "Пётр", "Петрович"); // 3 занятия
        Coach coach3 = new Coach("Сидоров", "Сергей", "Сергеевич"); // 1 занятие

        Group group = new Group("Общая группа", Age.ADULT, 90);

        // Тренер 1 — 2 занятия
        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.THURSDAY, new TimeOfDay(16, 0)));

        // Тренер 2 — 3 занятия
        timetable.addNewTrainingSession(new TrainingSession(group, coach2,
                DayOfWeek.TUESDAY, new TimeOfDay(11, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2,
                DayOfWeek.FRIDAY, new TimeOfDay(14, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0)));

        // Тренер 3 — 1 занятие
        timetable.addNewTrainingSession(new TrainingSession(group, coach3,
                DayOfWeek.WEDNESDAY, new TimeOfDay(13, 0)));

        List<Timetable.CounterOfTrainings> stats = timetable.getCountByCoaches();
        assertEquals(3, stats.size());

        // Проверяем сортировку: сначала тренер с наибольшим количеством занятий
        assertEquals(coach2, stats.get(0).getCoach()); // 3 занятия
        assertEquals(3, stats.get(0).getCount());

        assertEquals(coach1, stats.get(1).getCoach()); // 2 занятия
        assertEquals(2, stats.get(1).getCount());

        assertEquals(coach3, stats.get(2).getCoach()); // 1 занятие
        assertEquals(1, stats.get(2).getCount());
    }

    @Test
    void testGetCountByCoachesSameCountMultipleCoaches() {
        Timetable timetable = new Timetable();

        Coach coachA = new Coach("Алексеев", "Алексей", "Алексеевич");
        Coach coachB = new Coach("Борисов", "Борис", "Борисович");

        Group group = new Group("Равная группа", Age.ADULT, 60);

        timetable.addNewTrainingSession(new TrainingSession(group, coachA,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coachA,
                DayOfWeek.WEDNESDAY, new TimeOfDay(15, 0)));

        timetable.addNewTrainingSession(new TrainingSession(group, coachB,
                DayOfWeek.TUESDAY, new TimeOfDay(12, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coachB,
                DayOfWeek.THURSDAY, new TimeOfDay(17, 0)));


        List<Timetable.CounterOfTrainings> stats = timetable.getCountByCoaches();
        assertEquals(2, stats.size());

        for (Timetable.CounterOfTrainings stat : stats) {
            assertEquals(2, stat.getCount(),
                    "Каждый тренер должен иметь ровно 2 занятия");
        }
    }
}
