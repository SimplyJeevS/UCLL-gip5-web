package be.ucll.java.gip5.util;

import be.ucll.java.gip5.dao.PersoonRepository;
import be.ucll.java.gip5.exceptions.InvalidCredentialsException;
import be.ucll.java.gip5.model.Persoon;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.Optional;

public class Api {
    public Api(){}

    public static Persoon checkApiKey(String api, PersoonRepository persoonRepository) throws InvalidCredentialsException {
        if(api.equals("")){
            ServletRequestAttributes attr = (ServletRequestAttributes)
                    RequestContextHolder.currentRequestAttributes();
            HttpSession session= attr.getRequest().getSession(true); // true == allow create
            api = (String) session.getAttribute("api");
            System.out.println(api);
            return null;
        }
        Optional<Persoon> persoon = persoonRepository.findPersoonByApi(api);
        if(!persoon.isPresent()){
            throw new InvalidCredentialsException();
        }
        return persoon.get();
    }
}
