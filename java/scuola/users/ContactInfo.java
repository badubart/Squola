package scuola.users;

/**
 * Dane kontaktowe użytkownika (kompozycja w klasie {@link User}).
 */
public class ContactInfo {

    private String email;
    private String phoneNumber;

    public ContactInfo() {
    }

    public ContactInfo(String email, String phoneNumber) {
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
