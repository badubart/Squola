package scuola.attendance;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UploadedFileTest {

    @Test
    void constructorStoresFields() {
        UploadedFile file = new UploadedFile("plik.pdf", 2.5, "treść");
        assertEquals("plik.pdf", file.getFileName());
        assertEquals(2.5, file.getSizeMB());
        assertEquals("treść", file.getContent());
    }

    @Test
    void shortConstructorDefaultsContentToEmpty() {
        UploadedFile file = new UploadedFile("plik.pdf", 2.5);
        assertEquals("", file.getContent());
    }

    @Test
    void settersUpdateFields() {
        UploadedFile file = new UploadedFile();
        file.setFileName("nowy.pdf");
        file.setSizeMB(9.9);
        assertEquals("nowy.pdf", file.getFileName());
        assertEquals(9.9, file.getSizeMB());
    }
}
