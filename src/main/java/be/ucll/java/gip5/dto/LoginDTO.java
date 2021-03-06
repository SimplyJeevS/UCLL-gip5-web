package be.ucll.java.gip5.dto;

public class LoginDTO {
    private String wachtwoord;
    private String email;

    public LoginDTO(String wachtwoord, String email) {
        this.wachtwoord = wachtwoord;
        this.email = email;
    }

    public String getWachtwoord() {
        return wachtwoord;
    }

    public void setWachtwoord(String wachtwoord) {
        this.wachtwoord = wachtwoord;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
