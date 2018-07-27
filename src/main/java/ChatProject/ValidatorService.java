package ChatProject;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidatorService {

    private final ChannelRepository channelRepository;

    public boolean checkInvalidChannelIdEdit(Channel channel){
        return channel.equals(channelRepository.findById(channel.getChannelId()).get());
    }

    public boolean checkInvalidNameEdit(String channelId, String name){
        return channelRepository.findById(channelId).get().getName().equals(name);
    }

    public boolean checkInvalidDateOfCreationEdit(String channelId, String dateOfCreation){
        return channelRepository.findById(channelId).get().getDateOfCreation().equals(dateOfCreation);
    }

    public boolean checkInvalidDateOfClosingEdit(String channelId, String dateOfClosing){
        return channelRepository.findById(channelId).get().getDateOfCreation().equals(dateOfClosing) ||
                channelRepository.findById(channelId).get().getDateOfCreation() == dateOfClosing;
    }

    public boolean getChannelStatusClosed(String channelId){
        return channelRepository.findById(channelId).get().getStatus().equals(Channel.Status.CLOSED);
    }
}
