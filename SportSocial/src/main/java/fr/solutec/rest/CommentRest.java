package fr.solutec.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.solutec.entities.Comment;
import fr.solutec.entities.Post;
import fr.solutec.entities.User;
import fr.solutec.repository.CommentRepository;
import fr.solutec.repository.PostRepository;
import fr.solutec.repository.UserRepository;

@RestController
@CrossOrigin("*")
public class CommentRest {

	@Autowired
	private CommentRepository commentRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PostRepository postRepo;
	
	@GetMapping("club/posts/comments/{idPost}")
	public Iterable<Comment> getCommentsInPost(@PathVariable Long idPost){
		Iterable<Comment> Comments = commentRepo.getCommentOfPost(idPost);
		for (Comment comment : Comments) {
			int res = commentRepo.countCommentsOfComment(comment.getIdComment());
			comment.setNumberComments(res);
			int numberLikes = commentRepo.countLikes(comment.getIdComment());
			comment.setNumberLikes(numberLikes);
		}
		
		return Comments;
	}
	
	@GetMapping(("club/posts/comments/comments/{idComment}"))
	public Iterable<Comment> getCommentsInComment(@PathVariable Long idComment){
		Iterable<Comment> Comments = commentRepo.getCommentsOfComment(idComment);
		
		for (Comment comment : Comments) {
			int res = commentRepo.countCommentsOfComment(comment.getIdComment());
			comment.setNumberComments(res);
			int numberLikes = commentRepo.countLikes(comment.getIdComment());
			comment.setNumberLikes(numberLikes);
		}
		
		
		return Comments;
	}
	
	@PostMapping("/post/comment/{idUser}/{idPost}")
	public Comment createCommentInPost(@PathVariable Long idUser, @PathVariable Long idPost, @RequestBody String contenu) {
		Optional<User> user = userRepo.findById(idUser);
		Optional<Post> post = postRepo.findById(idPost);
		Comment c = new Comment(null, null, contenu, user.get(),null, null, null, 0, 0);
		post.get().getCommentsPost().add(c);
		return commentRepo.save(c);
	}
	
	@PostMapping("/comment/comment/{idUser}/{idComment}")
	public Comment createCommentInComment(@PathVariable Long idUser, @PathVariable Long idComment, @RequestBody String contenu) {
		Optional<User> user = userRepo.findById(idUser);
		Optional<Comment> comment = commentRepo.findById(idComment);
		Comment c = new Comment(null, null, contenu, user.get(),null, null, null, 0, 0);
		comment.get().getComments().add(c);
		return commentRepo.save(c);
	}
	
	@PostMapping("/comment/response/{idUser}/{idComment}/{idCommentComment}")
	public Comment createCommentResponse(@PathVariable Long idUser, @PathVariable Long idComment, @PathVariable Long idCommentComment, @RequestBody String contenu) {
		Optional<User> user = userRepo.findById(idUser);
		Optional<Comment> comment = commentRepo.findById(idComment);
		Optional<Comment> commentComment = commentRepo.findById(idCommentComment);
		Comment c = new Comment(null, null, contenu, user.get(),commentComment.get().getCreateurComment(), null, null, 0, 0);
		comment.get().getComments().add(c);
		return commentRepo.save(c);
	}
	
	
	@GetMapping("comments/checkLike/{idUser}/{idComment}")
	public Boolean checkLike(@PathVariable Long idComment, @PathVariable Long idUser) {
		Optional<Comment> c = commentRepo.findById(idComment);
		List<User> likeComment = c.get().getLikeComments();
		Boolean check= false;
		for (User user : likeComment) {
			if (user.getIdUser() == idUser) {
				check = true;
			}
		}
		return check;
	}
	
	
	@PatchMapping("comment/like/{idUser}/{idComment}")
	public Comment likeComment(@PathVariable Long idComment, @PathVariable Long idUser) {
		Optional<User> u = userRepo.findById(idUser);
		Optional<Comment> c = commentRepo.findById(idComment);
		boolean check = checkLike(idComment, idUser);
		if (check == false) {
			c.get().getLikeComments().add(u.get());	
		}
		return commentRepo.save(c.get());
	}
	
	@PatchMapping("comment/unlike/{idUser}/{idComment}")
	public Comment unlikeComment(@PathVariable Long idComment, @PathVariable Long idUser){
		Optional<Comment> c = commentRepo.findById(idComment);
		c.get().getLikeComments().removeIf(u -> u.getIdUser()==idUser);
		return commentRepo.save(c.get());
	}
	
	
	@DeleteMapping("comment/delete/{idPost}/{idComment}")
	public void deleteComment(@PathVariable Long idPost, @PathVariable Long idComment) {
		Optional<Post> post = postRepo.findById(idPost);
		Optional<Comment> comment = commentRepo.findById(idComment);
		
		post.get().getCommentsPost().removeIf(c -> c.getIdComment()==idComment);
		
		List<Comment> commentsInComments = new ArrayList<>();
		
		if(comment.get().getComments()!=null) {
			commentsInComments.addAll(comment.get().getComments());
			comment.get().setComments(null);
			
			for (Comment comment2 : commentsInComments) {
				commentRepo.delete(comment2);
			}
		}
		
		commentRepo.delete(comment.get());
		
	}
	
	
	@DeleteMapping("comment/comment/delete/{idCommentSource}/{idComment}")
	public void deleteCommentInComment(@PathVariable Long idCommentSource, @PathVariable Long idComment) {
		Optional<Comment> commentSource = commentRepo.findById(idCommentSource);
		Optional<Comment> comment = commentRepo.findById(idComment);
		
		commentSource.get().getComments().removeIf(c -> c.getIdComment()==idComment);
		commentRepo.delete(comment.get());
	}
	
	
	
	
	
	
	
	
	
	
	
}
