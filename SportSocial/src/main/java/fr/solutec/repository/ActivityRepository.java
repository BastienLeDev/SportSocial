package fr.solutec.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import fr.solutec.entities.Activity;
import fr.solutec.entities.UserActivity;

public interface ActivityRepository extends CrudRepository<Activity, Long>{
	
	@Query("SELECT a FROM UserActivity a WHERE a.activity.idActivity =?1")
	public UserActivity findByIdActivity (Long idActivity);

}
