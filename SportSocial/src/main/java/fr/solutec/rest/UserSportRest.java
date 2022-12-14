package fr.solutec.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.solutec.entities.UserSport;
import fr.solutec.repository.UserSportRepository;

@RestController @CrossOrigin("*")
public class UserSportRest {

	@Autowired
	private UserSportRepository userSportRepo;
	
	@GetMapping("classement/{nameSport}")
	public List<UserSport> rankingBySport(@PathVariable String nameSport){
		return userSportRepo.searchScoreBySportAsc(nameSport);
	}
}
