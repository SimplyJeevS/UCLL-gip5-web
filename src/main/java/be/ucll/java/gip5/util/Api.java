package be.ucll.java.gip5.util;

import be.ucll.java.gip5.dao.PersoonRepository;
import be.ucll.java.gip5.exceptions.InvalidCredentialsException;
import be.ucll.java.gip5.model.Persoon;

import java.util.Optional;

public class Api {
    public Api(){}

    public static Persoon checkApiKey(String api, PersoonRepository persoonRepository) throws InvalidCredentialsException {
        if(api.equals("")){
            return null;
        }else{
            Optional<Persoon> persoon = persoonRepository.findPersoonByApi(api);
            if(!persoon.isPresent()){
                throw new InvalidCredentialsException();
            }
            return persoon.get();
        }
    }
}
