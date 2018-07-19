package ChatProject;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class ChannelController {

    private final ChannelRepository channelRepository;
    private final ChannelService channelService;

    @PostMapping("/channel")
    public Response createChannel(  @RequestParam(value="name") String name,
                                    @RequestParam(value="status") Channel.Status status){
        if(channelRepository.findByName(name) == null){
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime dateOfCreation = LocalDateTime.now();
            channelRepository.save(new Channel(name, status, dtf.format(dateOfCreation)));
            return new Success(Response.ChannelCreationSuccess);
        }
        return new Error(Response.ChannelCreationFailed);
    }

    @GetMapping("/channel")
    public List<Channel> listOfChannels(){
        return channelRepository.findAll();
    }

    @GetMapping("/channel/findbyname/{channel-name}")
    public Channel findChannelByName(@PathVariable(value="channel-name") String channelName){
        return channelRepository.findByName(channelName);
    }

    @GetMapping("/channel/findbyname/{channel-name}/messages")
    public List<Message> listOfMessages(@PathVariable(value="channel-name") String channelName,
                                        @RequestParam(value="history", defaultValue = "0") Long history){
        if(history != 0){
            List<Message> result = new ArrayList<>();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            for(Message m : channelRepository.findByName(channelName).getListOfMessages()){
                if(LocalDateTime.parse(m.getDateOfCreation(), dtf).isAfter(LocalDateTime.now().minusMinutes(history))){
                    result.add(m);
                }
            }
            return result;
        }
        return channelRepository.findByName(channelName).getListOfMessages();
    }

    @PostMapping("/channel/{channel-name}/message")
    public Response addMessage(@PathVariable(value="channel-name") String channelName,
                               @RequestParam(value="authorization") String authorization,
                               @RequestParam(value="account-id") String accountId,
                               @RequestParam(value="data") String data) throws IOException {

        String url = "https://dev.onair-backend.moon42.com/api/business-layer/v1/chat/account/" + accountId + "/channel/" + channelName;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization", authorization);
        HttpEntity entity = new HttpEntity(headers);
        try{
            HttpEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            if(((ResponseEntity<String>) response).getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseJson = new Gson().fromJson(response.getBody(), Map.class);
                if(!(boolean)responseJson.get("canWrite")){
                    return new Error(Response.AuthorizationFailed);
                }
            }
        }
        catch(HttpServerErrorException e){
            /*if(e.getRawStatusCode() == 500) {

            }*/
            return new Error(Response.AuthorizationFailed);
        }
        return channelService.saveAndSendMessage(channelName, accountId, data);
    }

    @PatchMapping("/channel/findbyname/{channel-name}")
    public Response updateChannelStatus(@PathVariable(value="channel-name") String channelName,
                                        @RequestParam(value="status") Channel.Status status){
        channelRepository.findByName(channelName).setStatus(status);
        if(status.equals(Channel.Status.CLOSED)){
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            channelRepository.findByName(channelName).setDateOfClosing(dtf.format(LocalDateTime.now()));
        }
        channelRepository.flush();
        return new Success(Response.ChannelStatusChanged);
    }
}
