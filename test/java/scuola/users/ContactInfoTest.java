package scuola.users;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ContactInfoTest {

    @Test
    void constructorStoresFields() {
        ContactInfo contact = new ContactInfo("jan@x.pl", "600100200");
        assertEquals("jan@x.pl", contact.getEmail());
        assertEquals("600100200", contact.getPhoneNumber());
    }

    @Test
    void settersUpdateFields() {
        ContactInfo contact = new ContactInfo();
        contact.setEmail("nowy@x.pl");
        contact.setPhoneNumber("700");
        assertEquals("nowy@x.pl", contact.getEmail());
        assertEquals("700", contact.getPhoneNumber());
    }
}
