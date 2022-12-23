package fr.solutec.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import fr.solutec.entities.User;
import fr.solutec.entities.UserSport;

public interface UserRepository extends CrudRepository<User, Long>{
	
	public Optional<User> findByLoginUserAndPasswordUser(String login, String password);
	
	@Query("SELECT u FROM User u INNER JOIN UserSport s ON s.user.idUser=u.idUser WHERE s.sport.nameSport = ?1 AND s.user.coachUser = true")
	public List<User> searchCoachBySport(String nameSport); 


    @Query ("SELECT u FROM User u WHERE u.lastNameUser = ?1 AND u.firstNameUser = ?2")
    public List<User> searchUserByname(String lastname, String firstname);


	public Optional<User> findByLoginUser(String login);
	
	@Query("SELECT c FROM User c WHERE coachUser = true")
    List<User> getCoachsById(Long id);

 
	public Optional<User> findByIdUser(Long exp);
	
	
	@Query("SELECT u FROM User u WHERE u.idUser != ?1")
	public List<User> findAllUserExceptConnected (Long idUser);
	
	@Query ("SELECT u FROM User u WHERE (u.id NOT IN ((SELECT f.applicant.id FROM Friend f WHERE (accept=true AND f.receiver.id = 6 AND f.applicant.id != 6)))AND (u.id NOT IN (SELECT f.receiver.id FROM Friend f WHERE (accept= true AND f.receiver.id != 6 AND f.applicant.id = 6)))) AND (u.id != 6 )")
	List<User> getNonFriends (Long idUser);
	
	


	

}