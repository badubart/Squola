package scuola.attendance;

import scuola.lessons.Lesson;

import java.time.LocalDateTime;

/**
 * Plik z materiałami dydaktycznymi powiązany z zajęciami.
 */
public class Material {

    private String fileName;
    private double fileSizeMB;
    private LocalDateTime uploadDate;
    private Lesson lesson;

    public Material() {
    }

    public Material(String fileName, double fileSizeMB) {
        this(fileName, fileSizeMB, LocalDateTime.now(), null);
    }

    public Material(String fileName, double fileSizeMB, LocalDateTime uploadDate, Lesson lesson) {
        this.fileName = fileName;
        this.fileSizeMB = fileSizeMB;
        this.uploadDate = uploadDate;
        this.lesson = lesson;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public double getFileSizeMB() {
        return fileSizeMB;
    }

    public void setFileSizeMB(double fileSizeMB) {
        this.fileSizeMB = fileSizeMB;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }
}
