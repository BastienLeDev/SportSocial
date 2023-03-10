package fr.solutec.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.aspectj.apache.bcel.generic.MULTIANEWARRAY;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.solutec.entities.Club;
import fr.solutec.entities.Comment;
import fr.solutec.entities.Friend;
import fr.solutec.entities.Image;
import fr.solutec.entities.Post;
import fr.solutec.entities.Team;
import fr.solutec.entities.User;
import fr.solutec.repository.ClubRepository;
import fr.solutec.repository.CommentRepository;
import fr.solutec.repository.FriendRepository;
import fr.solutec.repository.PostRepository;
import fr.solutec.repository.UserRepository;

@RestController
@CrossOrigin("*")
public class ClubRest {

	@Autowired
	private ClubRepository clubRepo;
	

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PostRepository postRepo;
	
	@Autowired
	private CommentRepository commentRepo;
	
	
	@GetMapping("club")
	public Iterable<Club> getAllClub(){
		return clubRepo.findAll();
	}
	
	@GetMapping("mesClubs/{idUser}")
	public Iterable<Club> getMyClubs(@PathVariable Long idUser){
		return clubRepo.getMyClubs(idUser);
	}
	
	@GetMapping("autresClubs/{idUser}")
	public Iterable<Club> getOtherClubs(@PathVariable Long idUser){
		return clubRepo.getOtherClubs(idUser);
	}
	
	
	@PostMapping("club/{idUser}")
	public Club createClub(@PathVariable Long idUser, @RequestBody Club c) {
		Optional<User> user = this.userRepo.findByIdUser(idUser);
		Club clubCreated = c;
		clubCreated.setCreateur(user.get());
		List<User> admin = new ArrayList<User>();
		admin.add(user.get());
		clubCreated.setAdmin(admin);
		Image imageSport = clubCreated.getSportClub().getImageSport();
		clubCreated.setImageClub(imageSport);
		clubRepo.save(clubCreated);
		
		return clubCreated;
	}
	
	@PatchMapping("club/rejoindre/{idUser}/{idClub}")
	public Club addUserToClub(@PathVariable Long idUser, @PathVariable Long idClub) {
		Optional<User> u = userRepo.findById(idUser);
		Optional<Club> c = clubRepo.findById(idClub);
		c.get().getMembres().add(u.get());
		return clubRepo.save(c.get());
	}
	
	@PatchMapping("club/desister/{idUser}/{idClub}")
	public Club removeUserFromClub(@PathVariable Long idUser, @PathVariable Long idClub) {
		Club c = clubRepo.findById(idClub).get();
		c.getMembres().removeIf(u -> u.getIdUser()==idUser);
		return clubRepo.save(c);
	}
	
	
	//r??cup??rer les amis qui sont dans le m??me club que nous
	
	@GetMapping("club/amis/{idUser}/{idClub}")
	public Iterable<User> getFriendsInClub(@PathVariable Long idUser, @PathVariable Long idClub){
		return userRepo.getFriendsInClub(idUser, idClub);
	}
	
	// r??cup??rer les non amis qui sont dans le m??me club que nous
	@GetMapping("club/nonamis/{idUser}/{idClub}")
	public Iterable<User> getNonFriendsInClub(@PathVariable Long idUser, @PathVariable Long idClub){
		return userRepo.getNonFriendsInClub(idUser, idClub);
	}
	
	// r??cup??rer les amis demand??s ou qui nous ont demand?? qui sont dans le m??me club que nous
		@GetMapping("club/amisdemandes/{idUser}/{idClub}")
		public Iterable<User> getAskedFriendsInClub(@PathVariable Long idUser, @PathVariable Long idClub){
			return userRepo.getAskedFriendsInClub(idUser, idClub);
		}
		
	
	  @GetMapping("club/search/{titleClub}/{idUser}")
	  public List<Club> searchMyClub (@PathVariable Long idUser, @PathVariable String titleClub) {
	   List<Club> club = clubRepo.searchOtherClubs(idUser, titleClub);
	    return club;
	    }
	  
	  @GetMapping("club/filter/{idUser}/{nameSport}")
	  public List<Club> filterBySport (@PathVariable Long idUser, @PathVariable String nameSport){
		  List<Club> club = clubRepo.filterOtherClubs(idUser, nameSport);
		  return club;
	  }

	  //supprimer un club
	  @DeleteMapping("club/delete/{idClub}")
	  public void deleteClub(@PathVariable Long idClub){
		  Iterable<Post> posts = new ArrayList<>();
		  posts= postRepo.findAll();
		  
		  List<Post> postsToDelete = new ArrayList<>();
		  List<Comment> commentsToDelete = new ArrayList<>();
		  List<Comment> commentsInComments = new ArrayList<>();
		  
		  // On r??cup??re les posts du  club ?? supprimer, et on leur enl??ve la valeur du club
		  for (Post post : posts) {
			if(post.getClubPost().getIdClub()==idClub) {
				postsToDelete.add(post);
				post.setClubPost(null);
			}
		  }
		  
		  // On r??cup??re les commentaires associ??s aux posts et on les retire des posts
		  for (Post post : postsToDelete) {
			  if(post.getCommentsPost()!=null) {
				  for (Comment comment : post.getCommentsPost()) {
					  commentsToDelete.add(comment);
					  post.setCommentsPost(null);				}  
			  }
		  }
		  
		  // on r??cup??re les r??ponses aux commentaires et on les retires des commentaires
		  for (Comment comment : commentsToDelete) {
			  if(comment.getComments()!=null) {
				  List<Comment> listComments = new ArrayList<>();
				  listComments = comment.getComments();
				  commentsInComments.addAll(listComments);
				  comment.setComments(null);			  }
		  }
		  
		  // on supprime les r??ponses aux commentaires
		  for (Comment comment : commentsInComments) {
			  commentRepo.delete(comment);
		  }
		  
		  // on supprime les commentaires
		  for (Comment comment1 : commentsToDelete) {
			  commentRepo.delete(comment1);
		  }
		  
		  // on supprime les posts
		  for (Post post : postsToDelete) {
			postRepo.delete(post);
		}
		  // on supprime le club
		  Optional<Club> club = clubRepo.findById(idClub);
		  clubRepo.delete(club.get());		
	  }
	  
	
	  @PatchMapping("club/admin/add/{idUser}/{idClub}")
	  public void addAdmin(@PathVariable Long idUser, @PathVariable Long idClub) {
		Optional<Club> club = clubRepo.findById(idClub);
		Optional<User> user = userRepo.findById(idUser);
		club.get().getAdmin().add(user.get());
		clubRepo.save(club.get());
	  }
	  
	  @PatchMapping("club/admin/delete/{idUser}/{idClub}")
	  public void deleteAdmin(@PathVariable Long idUser, @PathVariable Long idClub) {
		Optional<Club> club = clubRepo.findById(idClub);
		club.get().getAdmin().removeIf(u -> u.getIdUser()==idUser);
		clubRepo.save(club.get());
	  }
	  
	  
	
}

