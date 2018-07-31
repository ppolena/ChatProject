package ChatProject;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@Entity
@Table(name = "channel")
public class Channel {

    @Id
    @NotNull
    @Column(updatable=false)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String channelId;

    @NotNull
    @Column(updatable=false)
    private String name;

    @NotNull
    private Status status;

    @NotNull
    @Column(updatable=false)
    private String dateOfCreation = DateTimeFormatter.ISO_INSTANT.format(Instant.now());

    private String dateOfClosing;

    @OneToMany(fetch = FetchType.EAGER, mappedBy="parent", cascade = CascadeType.ALL)
    private List<Message> listOfMessages;

    public enum Status{
        DRAFT, ACTIVE, CLOSED
    }
}
