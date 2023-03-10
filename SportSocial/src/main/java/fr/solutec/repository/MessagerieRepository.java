package fr.solutec.repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import fr.solutec.entities.Message;
import fr.solutec.entities.Messagerie;

public interface MessagerieRepository extends CrudRepository<Messagerie, Long>{

	List<Messagerie> findByDestinataireIdUser(Long idUser);

	List<Messagerie> findByMessageExpediteurMessageIdUser(Long id);

	
	@Query("SELECT m FROM Messagerie m Where m.destinataire.idUser=?1 Order By m.message.dateSendMessage Asc ")
	List<Messagerie> TrouverByDestinataireIdUserAsc(Long idUser);

	@Query("SELECT m FROM Messagerie m Where (m.destinataire.idUser=?1 AND m.message.expediteurMessage.idUser=?2) Order By m.message.dateSendMessage Asc ")
	List<Messagerie> TrouverByDestinataireAndByExpediteurIdUserAsc(Long idDest, Long idExp);
	
	@Query("SELECT m FROM Messagerie m Where (m.destinataire.idUser=?1 AND m.message.expediteurMessage.idUser=?2) OR (m.destinataire.idUser=?2 AND m.message.expediteurMessage.idUser=?1) Order By m.message.dateSendMessage Asc ")
	List<Messagerie>TrouverByDestinataireAndByExpediteurIdUserCombine(Long idDest, Long idExp);
	
	@Query("SELECT m FROM Messagerie m Where (m.destinataire.idUser=?1 AND m.message.expediteurMessage.idUser=?2) OR (m.destinataire.idUser=?2 AND m.message.expediteurMessage.idUser=?1) Order By m.message.dateSendMessage Desc ")
	List<Messagerie>LastByDestinataireAndByExpediteurIdUserCombine(Long idDest, Long idExp, PageRequest pageable);
	
	@Query("SELECT m FROM Messagerie m Where (m.destinataire.idUser=?1 AND m.message.expediteurMessage.idUser=?2) OR (m.destinataire.idUser=?2 AND m.message.expediteurMessage.idUser=?1) Order By m.message.dateSendMessage Desc ")
	Messagerie LastByDestinataireAndByExpediteurIdUserCombine2 (Long idDest, Long idExp, PageRequest pageable);
	
	@Query("SELECT m.message FROM Messagerie m Where (m.destinataire.idUser=?1 AND m.message.expediteurMessage.idUser=?2) OR (m.destinataire.idUser=?2 AND m.message.expediteurMessage.idUser=?1) Order By m.message.dateSendMessage Asc ")
	List<Message>MessageByDestinataireAndByExpediteurIdUserCombine(Long idDest, Long idExp);
	
	@Query(value="SELECT 1 m FROM Messagerie m Where (m.destinataire.idUser=?1 AND m.message.expediteurMessage.idUser=?2) Order By m.message.dateSendMessage Desc LIMIT 1", nativeQuery=true)
	List<Messagerie> TrouverByDestinataireAndByExpediteurIdUserDescTop(Long idDest, Long idExp);
	
	@Query("SELECT m FROM Message m Where (m.expediteurMessage.idUser=?1 AND m.idMessage =?2)")
	Message DeleteMyMessage(Long idUser, Long idMessage);

	

}
