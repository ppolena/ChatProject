package ChatProject;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service("AuthorizationService")
public class AuthorizationService {

    public ResponseEntity authenticate(String accountId, String channelName, String token){
        String url = "https://dev.onair-backend.moon42.com/api/business-layer/v1/chat/account/" + accountId + "/channel/" + channelName;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        if(!token.isEmpty()) headers.add("authorization", token);
        HttpEntity entity = new HttpEntity(headers);
        try{
            HttpEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
        }
        catch(HttpServerErrorException e){
            return new ResponseEntity<>(new Error(Response.AuthorizationFailed), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
        }
    }
}
