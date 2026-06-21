package scuola.lessons;

import scuola.notifications.NotificationService;
import scuola.repository.LessonRepository;
import scuola.repository.RoomRepository;
import scuola.repository.UserRepository;
import scuola.users.Student;
import scuola.users.Teacher;
import scuola.users.TeacherAvailability;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serwis harmonogramu: sale, zastępstwa, anulowanie i listy zajęć.
 */
public class ScheduleService {

    private final LessonRepository lessonRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public ScheduleService(LessonRepository lessonRepository, RoomRepository roomRepository,
                           UserRepository userRepository, NotificationService notificationService) {
        this.lessonRepository = lessonRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public List<Lesson> listLessons() {
        return lessonRepository.findAll();
    }

    public List<Lesson> listLessons(Teacher teacher) {
        return lessonRepository.findByTeacher(teacher);
    }

    public List<Lesson> listLessons(Student student) {
        return lessonRepository.findByStudent(student);
    }


    public boolean assignRoom(Lesson lesson, Room room) {
        if (lesson == null || room == null || !isRoomAvailable(room, lesson.getStartTime(), lesson.getEndTime())) {
            return false;
        }
        lesson.setRoom(room);
        return true;
    }

    public List<Room> listAvailableRooms(LocalDateTime start, LocalDateTime end) {
        return roomRepository.findAll().stream()
                .filter(room -> isRoomAvailable(room, start, end))
                .collect(Collectors.toList());
    }

    public boolean releaseRoom(Lesson lesson) {
        if (lesson == null) {
            return false;
        }
        lesson.setRoom(null);
        return true;
    }


    public boolean substituteTeacher(Lesson lesson, Teacher teacher) {
        if (lesson == null || teacher == null) {
            return false;
        }
        lesson.setTeacher(teacher);
        return true;
    }

    public boolean cancelLesson(Lesson lesson, String reason) {
        if (lesson == null) {
            return false;
        }
        lesson.setStatus(LessonStatus.CANCELLED);
        notificationService.sendCancelNotice(lesson);
        for (Reservation reservation : lesson.getReservations()) {
            if (reservation.isActive()) {
                reservation.cancel();
            }
        }
        releaseRoom(lesson);
        return true;
    }

    public boolean rescheduleLesson(Lesson lesson, LocalDateTime newStart, LocalDateTime newEnd) {
        if (lesson == null || newStart == null || newEnd == null || !newStart.isBefore(newEnd)) {
            return false;
        }
        lesson.setStartTime(newStart);
        lesson.setEndTime(newEnd);
        return true;
    }

    public List<Teacher> listAvailableTeachers(String subject, LocalDateTime start, LocalDateTime end) {
        return userRepository.findTeachers().stream()
                .filter(teacher -> isAvailable(teacher, start, end))
                .filter(teacher -> isFree(teacher, start, end))
                .collect(Collectors.toList());
    }


    private boolean isRoomAvailable(Room room, LocalDateTime from, LocalDateTime to) {
        if (room == null) {
            return false;
        }
        if (from == null || to == null) {
            return true;
        }
        for (Lesson lesson : lessonRepository.findAll()) {
            if (lesson.getRoom() == room && lesson.getStatus() == LessonStatus.SCHEDULED
                    && overlaps(lesson.getStartTime(), lesson.getEndTime(), from, to)) {
                return false;
            }
        }
        return true;
    }

    private boolean isAvailable(Teacher teacher, LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null || teacher.getAvailabilities().isEmpty()) {
            return true; // brak deklaracji dostępności -> traktujemy jako dostępny
        }
        for (TeacherAvailability slot : teacher.getAvailabilities()) {
            if (slot.getDayOfWeek() == start.getDayOfWeek()
                    && !slot.getStartTime().isAfter(start.toLocalTime())
                    && !slot.getEndTime().isBefore(end.toLocalTime())) {
                return true;
            }
        }
        return false;
    }

    private boolean isFree(Teacher teacher, LocalDateTime start, LocalDateTime end) {
        for (Lesson lesson : lessonRepository.findByTeacher(teacher)) {
            if (lesson.getStatus() == LessonStatus.SCHEDULED
                    && overlaps(lesson.getStartTime(), lesson.getEndTime(), start, end)) {
                return false;
            }
        }
        return true;
    }

    private boolean overlaps(LocalDateTime s1, LocalDateTime e1, LocalDateTime s2, LocalDateTime e2) {
        if (s1 == null || e1 == null || s2 == null || e2 == null) {
            return false;
        }
        return s1.isBefore(e2) && s2.isBefore(e1);
    }
}
