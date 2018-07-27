package ChatProject;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Component("beforeSaveChannelValidator")
public class BeforeSaveChannelValidator implements Validator {

    private final ChannelRepository channelRepository;
    private final ValidatorService validatorService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Channel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Channel channel = (Channel) target;
        ValidationUtils.rejectIfEmpty(errors, "name", "name.empty", Response.EmptyName);
        ValidationUtils.rejectIfEmpty(errors, "status", "status.empty", Response.EmptyStatus);
        if(!validatorService.checkInvalidChannelIdEdit(channel)){
            errors.rejectValue("channelId", "channelId.invalidEdit", Response.InvalidChannelIdEdit);
        }
        else {
            if (!validatorService.checkInvalidNameEdit(channel.getChannelId(), channel.getName())) {
                errors.rejectValue("name", "name.invalidEdit", Response.InvalidNameEdit);
            }
            if (!validatorService.checkInvalidDateOfCreationEdit(channel.getChannelId(), channel.getDateOfCreation())) {
                errors.rejectValue("dateOfCreation", "dateOfCreation.invalidEdit", Response.InvalidDateOfCreationEdit);
            }
            if (!validatorService.checkInvalidDateOfClosingEdit(channel.getChannelId(), channel.getDateOfClosing())) {
                errors.rejectValue("dateOfClosing", "dateOfClosing.invalidEdit", Response.InvalidDateOfClosingEdit);
            }
            if (validatorService.getChannelStatusClosed(channel.getChannelId())) {
                errors.rejectValue("status", "status.closed", Response.ChannelStatus + Channel.Status.CLOSED);
            }
            else if (channel.getStatus().equals(Channel.Status.CLOSED)) {
                channelRepository.findById(channel.getChannelId()).get().setDateOfClosing(DateTimeFormatter.ISO_INSTANT.format(Instant.now()));
            }
        }
    }
}