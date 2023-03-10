package fr.solutec.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor@AllArgsConstructor
@Entity@Data

public class Team {
	@Id@GeneratedValue
	private Long idTeam;
	private String title;
	@ManyToOne
	private User admin;
	@ManyToOne
	private Image imageTeam;
	@ManyToMany
	private List<User> membres = new ArrayList<User>();
	@ManyToMany 
	private List<Message> conversation = new ArrayList<Message>();
	
}
