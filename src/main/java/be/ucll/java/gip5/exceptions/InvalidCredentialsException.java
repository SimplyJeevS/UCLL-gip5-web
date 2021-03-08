package be.ucll.java.gip5.exceptions;

import com.vaadin.flow.component.UI;

public class InvalidCredentialsException extends Exception{
    public InvalidCredentialsException(){
        super("The credentials you provided are not valid");

        UI.getCurrent().navigate("login");
    }
}
