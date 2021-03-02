package be.ucll.java.gip5.exceptions;

public class InvalidCredentialsException extends Exception{
    public InvalidCredentialsException(){
        super("The credentials you provided are not valid");
    }
}
