package be.ucll.java.gip5.exceptions;

public class NotFoundException extends Exception{
    public NotFoundException(String p) {
        super(p + " was not found!");
    }
}
