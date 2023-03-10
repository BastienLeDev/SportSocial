package fr.solutec.rest;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.solutec.entities.Message;
import fr.solutec.entities.Messagerie;
import fr.solutec.entities.Team;
import fr.solutec.entities.User;
import fr.solutec.repository.MessageRepository;
import fr.solutec.repository.MessagerieRepository;

import fr.solutec.repository.UserRepository;

@RestController
@CrossOrigin("*")
public class MessagerieRest {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private MessageRepository messageRepo;
	@Autowired
	private MessagerieRepository messagerieRepo;

	// Envoi de message

	@PostMapping("message/envoyer")
	public Messagerie sendMessage(@RequestBody Messagerie messagerie) {

		String contenu = messagerie.getMessage().getContentMessage();
		System.out.println("contenu " + contenu);
		Optional<User> uexp = userRepo.findByLoginUser(messagerie.getMessage().getExpediteurMessage().getLoginUser());
		Message m = new Message(null, null, false, contenu, uexp.get());

		Message msave = messageRepo.save(m);

		String destinataire = messagerie.getDestinataire().getLoginUser(); // récupération login du destinataire
		Optional<User> udest = userRepo.findByLoginUser(destinataire); // récupération du user
		Messagerie um = new Messagerie(udest.get(), msave);

		return messagerieRepo.save(um);

	}

	@PostMapping("message/envoyer/{dest}/{exp}")
	public Messagerie sendMessageById(@PathVariable Long dest, @PathVariable Long exp,
			@RequestBody Messagerie messagerie) {

		String contenu = messagerie.getMessage().getContentMessage();
		Optional<User> uexp = userRepo.findByIdUser(exp);
		Message m = new Message(null, null, false, contenu, uexp.get());
		Message msave = messageRepo.save(m);

		Optional<User> udest = userRepo.findByIdUser(dest);
		Messagerie msg = new Messagerie(udest.get(), msave);

		return messagerieRepo.save(msg);
	}

	// Voir les messages

	@GetMapping("message/me/{id}")
	List<Messagerie> getMyMessage(@PathVariable Long id) {
		return messagerieRepo.findByDestinataireIdUser(id);
		// return messagerieRepo.findAll();
	}

	// Voir les messages reçu en fonction du temps

	@GetMapping("message/me/{id}/asc")
	List<Messagerie> getMyMessageAsc(@PathVariable Long id) {
		return messagerieRepo.TrouverByDestinataireIdUserAsc(id);
	}

	// Voir les messages reçu en fonction du temps ET par expediteur

	@GetMapping("message/me/{dest}/{exp}/asc")
	List<Messagerie> getMyMessageByExpAsc(@PathVariable Long dest, @PathVariable Long exp) {
		return messagerieRepo.TrouverByDestinataireAndByExpediteurIdUserAsc(dest, exp);
	}

	// Voir les messages reçu et envoyé par expediteur et destinataire en fonction
	// du temps

	@GetMapping("message/me/{dest}/{exp}/combine")
	List<Messagerie> getMyMessageByExpCombine(@PathVariable Long dest, @PathVariable Long exp) {
		return messagerieRepo.TrouverByDestinataireAndByExpediteurIdUserCombine(dest, exp);
	}
	 //Affiche le dernier message 
	@GetMapping("messagelast/me/{dest}/{exp}/combine")
	List<Messagerie> LastMyMessageByExpCombine(@PathVariable Long dest, @PathVariable Long exp) {
		return messagerieRepo.LastByDestinataireAndByExpediteurIdUserCombine(dest, exp, PageRequest.of(0, 1));
	}

	@GetMapping("messagetrue/me/{dest}/{exp}/{idUser}/combine")
	List<Message> trueMyMessageByExpCombine(@PathVariable Long dest, @PathVariable Long exp, @PathVariable Long idUser) {
		List<Message> m = messagerieRepo.MessageByDestinataireAndByExpediteurIdUserCombine(dest, exp);
		Messagerie mess = messagerieRepo.LastByDestinataireAndByExpediteurIdUserCombine2(dest, exp, PageRequest.of(0, 1));
		
		Long id = mess.getDestinataire().getIdUser();
		
		if (idUser == id) {
		m.forEach(message -> message.setLu(true));
		messageRepo.saveAll(m);	
		}
		return m;
	}

	// Voir les messages qu'on a envoyé

	@GetMapping("message/wrote/{id}")
	List<Messagerie> getMessageSend(@PathVariable Long id) {

		return messagerieRepo.findByMessageExpediteurMessageIdUser(id);

	}

	@GetMapping("message/me/{dest}/{exp}/top")
	List<Messagerie> getMyMessageTop(@PathVariable Long dest, @PathVariable Long exp) {
		return messagerieRepo.TrouverByDestinataireAndByExpediteurIdUserDescTop(dest, exp);
	}
	
	@DeleteMapping("message/delete/{idUser}/{idMessage}")
	public Message deleteMyMessage(@PathVariable Long idUser, @PathVariable Long idMessage) {
		Message m = messagerieRepo.DeleteMyMessage(idUser, idMessage);
		
		messageRepo.delete(m);

		
		return m ;

		
	}
	
	

}
