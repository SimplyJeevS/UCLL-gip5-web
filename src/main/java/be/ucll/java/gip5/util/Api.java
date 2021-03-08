package be.ucll.java.gip5.util;

import be.ucll.java.gip5.dao.PersoonRepository;
import be.ucll.java.gip5.dao.ToewijzingRepository;
import be.ucll.java.gip5.exceptions.InvalidCredentialsException;
import be.ucll.java.gip5.model.Persoon;
import be.ucll.java.gip5.model.Rol;
import be.ucll.java.gip5.model.Toewijzing;
import com.vaadin.flow.component.UI;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;
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
        }
        Optional<Persoon> persoon = persoonRepository.findPersoonByApi(api);
        if(!persoon.isPresent()){
            UI.getCurrent().navigate("");
            throw new InvalidCredentialsException();
        }
        return persoon.get();
    }
    public static List<Rol> getAllRoles(String api, PersoonRepository persoonRepository, ToewijzingRepository toewijzingRepository) throws InvalidCredentialsException {
        Persoon persoon = checkApiKey(api,persoonRepository);
        List<Rol> rol = Collections.emptyList();
        rol.add(persoon.getDefault_rol());
        Optional<List<Toewijzing>> toewijzing = toewijzingRepository.findAllByPersoonId(persoon.getId());
        if(toewijzing.isPresent()){
            toewijzing.get().forEach(t-> {
                rol.add(t.getRol());
            });
        }
        return rol;
    }
}
