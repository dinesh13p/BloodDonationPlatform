package bloodbank.repository;

import bloodbank.entity.Document;
import bloodbank.entity.DocumentType;
import bloodbank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByUser(User user);
    Optional<Document> findByUserAndDocumentType(User user, DocumentType documentType);
}


