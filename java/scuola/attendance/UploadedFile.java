package scuola.attendance;

/**
 * Uproszczona reprezentacja pliku przekazywanego do/z systemu
 * (odpowiednik typu {@code File} z diagramu UML). Trzymana w pamięci, aby
 * ułatwić testowanie bez operacji na dysku.
 */
public class UploadedFile {

    private String fileName;
    private double sizeMB;
    private String content;

    public UploadedFile() {
    }

    public UploadedFile(String fileName, double sizeMB) {
        this(fileName, sizeMB, "");
    }

    public UploadedFile(String fileName, double sizeMB, String content) {
        this.fileName = fileName;
        this.sizeMB = sizeMB;
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public double getSizeMB() {
        return sizeMB;
    }

    public void setSizeMB(double sizeMB) {
        this.sizeMB = sizeMB;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
