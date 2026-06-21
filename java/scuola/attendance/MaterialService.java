package scuola.attendance;

import scuola.lessons.Lesson;
import scuola.notifications.NotificationService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Serwis zarządzania materiałami dydaktycznymi przypiętymi do zajęć.
 * Po udanym przesłaniu powiadamia zapisanych uczniów.
 */
public class MaterialService {

    public static final double MAX_FILE_SIZE_MB = 100.0;

    private final NotificationService notificationService;

    public MaterialService() {
        this(new NotificationService());
    }

    public MaterialService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public boolean uploadMaterial(Lesson lesson, UploadedFile file) {
        if (lesson == null || file == null || !validateFileSize(file)) {
            return false;
        }
        Material material = new Material(file.getFileName(), file.getSizeMB(), LocalDateTime.now(), lesson);
        lesson.addMaterial(material);
        notificationService.sendMaterialNotice(material);
        return true;
    }

    public boolean deleteMaterial(Lesson lesson, UploadedFile file) {
        if (lesson == null || file == null) {
            return false;
        }
        Material found = null;
        for (Material material : lesson.getMaterials()) {
            if (material.getFileName() != null && material.getFileName().equals(file.getFileName())) {
                found = material;
                break;
            }
        }
        return found != null && lesson.removeMaterial(found);
    }

    public List<Material> listMaterials(Lesson lesson) {
        if (lesson == null) {
            return List.of();
        }
        return lesson.getMaterials();
    }

    public UploadedFile downloadMaterial(Material material) {
        if (material == null) {
            return null;
        }
        return new UploadedFile(material.getFileName(), material.getFileSizeMB(),
                "Zawartość pliku: " + material.getFileName());
    }

    private boolean validateFileSize(UploadedFile file) {
        return file.getSizeMB() > 0 && file.getSizeMB() <= MAX_FILE_SIZE_MB;
    }
}
