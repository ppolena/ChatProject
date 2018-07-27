package ChatProject;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Component("beforeSaveChannelValidator")
public class PutPatchChannelValidator implements Validator {

    private final ChannelRepository channelRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Channel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Channel channel = (Channel) target;
        if(channel.getName() != null){
            errors.rejectValue("name", "name.isUnique", Response.NotAuthorized);
        }
        ValidationUtils.rejectIfEmpty(errors, "status", "status.empty", Response.BadStatusField);
        if(channelRepository.findByName(channel.getName()) == null){
            errors.rejectValue("name", "name.exists", Response.ChannelNotFound);
        }
        if(channelRepository.findByName(channel.getName()).getStatus().equals(Channel.Status.CLOSED)){
            errors.rejectValue("name", "name.exists", Response.ChannelStatus + Channel.Status.CLOSED);
        }
        if(channel.getStatus().equals(Channel.Status.CLOSED)){
            channelRepository.findByName(channel.getName()).setDateOfClosing(DateTimeFormatter
                    .ISO_DATE
                    .format(LocalDateTime.now()));
            /*channel.setDateOfClosing(DateTimeFormatter
                    .ISO_DATE
                    .format(LocalDateTime.now()));*/
        }
    }
}