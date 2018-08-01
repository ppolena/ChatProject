package ChatProject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "messages", path = "messages")
public interface MessageRepository extends JpaRepository<Message, String>{

    @RestResource(path = "findByMessageId")
    Message findByMessageId(@Param("messageId") String messageId);

    @RestResource(path = "findByAccountId")
    List<Message> findByAccountId(@Param("accountId") String accountId);

    @RestResource(path = "listMessages")
    List<Message> findByParentAndDateOfCreationGreaterThan(
            @Param("parent")Channel parent,
            @Param("history") String history);
}
