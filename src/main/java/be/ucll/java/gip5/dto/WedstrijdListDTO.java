package be.ucll.java.gip5.dto;

import java.util.List;

public class WedstrijdListDTO {
    private List<WedstrijdMetPloegenDTO> wedstrijden;

    public WedstrijdListDTO(List<WedstrijdMetPloegenDTO> wedstrijden) {
        this.wedstrijden = wedstrijden;
    }

    public List<WedstrijdMetPloegenDTO> getWedstrijden() {
        return wedstrijden;
    }

    public void setWedstrijden(List<WedstrijdMetPloegenDTO> wedstrijden) {
        this.wedstrijden = wedstrijden;
    }
}
