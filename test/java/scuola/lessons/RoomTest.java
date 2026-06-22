package scuola.lessons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoomTest {

    @Test
    void constructorStoresFields() {
        Room room = new Room("Sala 101", 20, "Projektor");
        assertEquals("Sala 101", room.getName());
        assertEquals(20, room.getCapacity());
        assertEquals("Projektor", room.getEquipment());
    }

    @Test
    void settersUpdateFields() {
        Room room = new Room();
        room.setName("Sala 202");
        room.setCapacity(15);
        room.setEquipment("Tablica");
        assertEquals("Sala 202", room.getName());
        assertEquals(15, room.getCapacity());
        assertEquals("Tablica", room.getEquipment());
    }
}
