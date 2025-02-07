package fr.eni.tp.filmotheque.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import fr.eni.tp.filmotheque.bll.FilmService;
import fr.eni.tp.filmotheque.bll.contexte.ContexteService;
import fr.eni.tp.filmotheque.bo.Membre;

@Controller
@SessionAttributes({"membreEnSession"})
public class LoginController {
	
	private ContexteService contexteService;

    public LoginController(ContexteService contexteService) {
        this.contexteService = contexteService;
    }
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/session")
	public String connexion(Principal principal, @ModelAttribute("membreEnSession") Membre membreEnSession) {
		Membre membre = this.contexteService.charger(principal.getName());
        if (membre != null) {
            membreEnSession.setId(membre.getId());
            membreEnSession.setNom(membre.getNom());
            membreEnSession.setPrenom(membre.getPrenom());
            membreEnSession.setPseudo(membre.getPseudo());
            membreEnSession.setAdmin(membre.isAdmin());
        } else {
            membreEnSession.setId(0L);
            membreEnSession.setNom((String)null);
            membreEnSession.setPrenom((String)null);
            membreEnSession.setPseudo((String)null);
            membreEnSession.setAdmin(false);
        }
		
		return "redirect:/films";
	}
	
	
	
	@ModelAttribute("membreEnSession")
	 public Membre addMembreEnSession() {
	  System.out.println("Add membre en session");
	  return new Membre();
	 }
}
