package ChatProject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.io.IOException;
import java.util.Map;

@Component
@Configurable
@RequiredArgsConstructor
public class SocketHandler extends TextWebSocketHandler {

    private final ChannelController channelController;
    private final ChannelService channelService;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException{

        Map<String, String> messageJson = new Gson().fromJson(message.getPayload(), Map.class);

        String channelName = (String) session.getAttributes().get("channelName");

        if(messageJson.get("type").equals("authorization") && !(boolean)session.getAttributes().get("authenticated")){
            String token = messageJson.get("authorization") == null ? "" : messageJson.get("authorization");
            String accountId = messageJson.get("accountId") == null ? "guest" : messageJson.get("accountId");
            String url = "https://dev.onair-backend.moon42.com/api/business-layer/v1/chat/account/" + accountId + "/channel/" + channelName;
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            if(!token.isEmpty()){
                headers.add("authorization", token);
            }

            HttpEntity entity = new HttpEntity(headers);
            try {
                HttpEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                if(((ResponseEntity<String>) response).getStatusCode() == HttpStatus.OK){
                    Map<String, Object> responseJson = new Gson().fromJson(response.getBody(), Map.class);
                    if(!(boolean)responseJson.get("canRead")){
                        channelService.getSessions().get(channelName).remove(session);
                        session.sendMessage(
                                new TextMessage(new Gson().toJson(new Error(Response.AuthorizationFailed))));
                    }
                    else {
                        ObjectMapper objectMapper = new ObjectMapper();
                        for (Message m : channelController
                                .listOfMessages((String) session
                                        .getAttributes()
                                        .get("channelName"), 60L)) {
                            session.sendMessage(new TextMessage(
                                    objectMapper.writeValueAsString(m)));
                        }
                        session.getAttributes().put("authenticated", true);
                        session.getAttributes().put("canRead", (boolean)responseJson.get("canRead"));
                        session.getAttributes().put("canWrite", (boolean)responseJson.get("canWrite"));
                        if(responseJson.get("profilePictureId") != null) {
                            session.getAttributes().put("profilePictureId", responseJson.get("profilePictureId"));
                        }
                        session.getAttributes().put("accountId", accountId);
                        session.sendMessage(
                                new TextMessage(new Gson().toJson(new Success(Response.AuthorizationSuccess))));
                    }
                }
                else{
                    session.sendMessage(
                            new TextMessage(new Gson().toJson(new Error(Response.AuthorizationFailed))));
                }
            }
            catch(HttpServerErrorException e){
                /*if(e.getRawStatusCode() == 500) {

                }*/
                session.sendMessage(
                        new TextMessage(new Gson().toJson(new Error(Response.AuthorizationFailed))));
            }
        }
        else {
            if((boolean)session.getAttributes().get("authenticated") &&
                    (boolean)session.getAttributes().get("canWrite")) {
                channelService.saveAndSendMessage(session,
                        channelName,
                        (String) session.getAttributes().get("accountId"),
                        messageJson.get("data"));
            }
            else{
                session.sendMessage(
                        new TextMessage(new Gson()
                                .toJson(new Error(Response.NotAuthorized))));
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception, IOException {
        channelService.addSession(session);
    }
}
//{"type":"authorization", "accountId":"58c57183-109d-4ffd-9634-ebd4bf0b31e1", "authorization":"Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJmaXJzdE5hbWUiOiJQZXRlciIsImxhc3ROYW1lIjoiUG9sZW5hIiwiYXVkIjpbIm9hdXRoMl9pZCJdLCJ1c2VyX25hbWUiOiJwZXRlci5wb2xlbmFAbW9vbjQyLmNvbSIsImRpc3BsYXlOYW1lIjoiUGV0aSIsInByb2ZpbGVQaWN0dXJlSWQiOm51bGwsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJhY2NvdW50VHlwZSI6IlBSSVZBVEUiLCJleHAiOjE1MzIwMzEyNTIsInVzZXJJZCI6IjU4YzU3MTgzLTEwOWQtNGZmZC05NjM0LWViZDRiZjBiMzFlMSIsImp0aSI6IjVhZDFjOTQ0LTIyMDgtNDg2YS1hN2YxLTAwN2U3YzIyY2EwYiIsImNsaWVudF9pZCI6Im9uYWlyLXVpIn0.TXRA77U25CsqC581IK2k7nThAtXXd3dIRegTG2k8R2E"}