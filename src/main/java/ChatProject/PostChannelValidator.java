package ChatProject;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component("beforeCreateChannelValidator")
public class PostChannelValidator implements Validator {

    private final ChannelRepository channelRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Channel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Channel channel = (Channel) target;
        ValidationUtils.rejectIfEmpty(errors, "name", "name.empty", Response.BadNameField);
        ValidationUtils.rejectIfEmpty(errors, "status", "status.empty", Response.BadStatusField);
        if(channelRepository.findByName(channel.getName()) != null){
            errors.rejectValue("name", "name.exists", Response.ChannelAlreadyExists);
        }
    }
}