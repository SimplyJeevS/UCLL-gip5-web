package be.ucll.java.gip5.exceptions;

public class AlreadyExistsException extends Exception{
    public AlreadyExistsException(String p) {
        super(p + " already exists!");
    }
}