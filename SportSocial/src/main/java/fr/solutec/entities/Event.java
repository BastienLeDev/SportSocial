package fr.solutec.entities;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor @AllArgsConstructor
@Entity @Data
public class Event {
	@Id @GeneratedValue
	private Long idEvent;
	@ManyToOne
	private User authorEvent;
	private String titleEvent;
	private String descriptionEvent;
	private Timestamp dateStart;
	private Timestamp dateEnd;
	private float priceEvent;
	@ManyToMany
	private List<User> participants = new ArrayList<User>();
	@ManyToOne
	private Address addressEvent;
	@ManyToOne
	private Sport sportEvent;
	private Boolean pastDate = false;
	private Boolean done = false;
	private int score;
}
