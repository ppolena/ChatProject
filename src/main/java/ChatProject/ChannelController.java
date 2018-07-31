package ChatProject;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@RepositoryRestController
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelRepository channelRepository;
    private final ChannelService channelService;

    /*@PostMapping("/channel/{channelName}/message")
    public ResponseEntity addMessage(@PathVariable(value="channelName") String channelName,
                               @RequestParam(value="authorization") String authorization,
                               @RequestParam(value="accountId") String accountId,
                               @RequestParam(value="data") String data) throws IOException {
        Resource<Response> resource;
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
                    return new ResponseEntity<>(new Error(Response.NotAuthorizedToWrite),
                                                new HttpHeaders(),
                                                HttpStatus.UNAUTHORIZED);
                }
            }
        }
        catch(HttpServerErrorException e){
            if(e.getRawStatusCode() == 500) {

            }
            return new ResponseEntity<>(new Error(Response.AuthorizationFailed), new HttpHeaders(),
                    HttpStatus.UNAUTHORIZED);
        }
        return channelService.saveAndSendMessage(channelName, accountId, data);
    }*/
}
