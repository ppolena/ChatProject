package ChatProject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(collectionResourceRel = "channel", path = "channel")
public interface ChannelRepository extends JpaRepository<Channel, String> {
    @RestResource(path = "findByChannelName")
    Channel findByChannelName(@Param("channelName") String channelName);
}
