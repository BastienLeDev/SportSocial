package fr.solutec.repository;



import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import fr.solutec.entities.Event;
import fr.solutec.entities.Sport;
import fr.solutec.entities.User;

public interface EventRepository extends CrudRepository<Event, Long>{
	

	@Query("SELECT e FROM Event e WHERE e.dateStart > CURRENT_TIMESTAMP order by e.dateStart asc")
    Iterable<Event> eventsToCome ();
	
	@Query("SELECT e FROM Event e WHERE e.idEvent = ?1")
	Event findByIdEvent(Long idEvent);
	
	@Query("SELECT e FROM Event e WHERE e.titleEvent LIKE %?1%  order by e.dateStart asc")
    Iterable<Event> searchAllEvents (String titleEvent);
	
	@Query("SELECT e FROM Event e WHERE e.dateStart > CURRENT_TIMESTAMP AND (e.titleEvent LIKE %?1%) order by e.dateStart asc")
	Iterable<Event> searchEventsToCome (String titleEvent);
	
	@Query("SELECT e from Event e where sportEvent = ?1")
	List<Event> eventsBySport(Sport sport);
	
	@Query("SELECT e FROM Event e INNER JOIN e.participants ep WHERE ep.idUser = ?1 order by e.dateStart desc")
	Iterable<Event> eventsOfOneUser(Long idUser);
	
	@Query("SELECT e FROM Event e INNER JOIN e.participants ep WHERE ep.idUser = ?1 AND (e.titleEvent LIKE %?2%) order by e.dateStart desc")
	Iterable<Event> searchEventsOfOneUser(Long idUser, String titleEvent);
	
	@Query("SELECT e FROM Event e INNER JOIN e.participants ep WHERE ep.idUser = ?1 AND (e.titleEvent LIKE %?2%) order by e.dateStart desc")
	List<Event> searchEventsOfOneUserList(Long idUser, String titleEvent);
}
