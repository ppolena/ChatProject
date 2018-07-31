package ChatProject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.hateoas.Resources;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Component
@Configurable
@RequiredArgsConstructor
public class SocketHandler extends TextWebSocketHandler {

    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ChannelService channelService;
    private final AuthorizationService authorizationService;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException{

        Map<String, String> messageJson = new Gson().fromJson(message.getPayload(), Map.class);

        String channelName = (String) session.getAttributes().get("channelName");

        if(messageJson.get("type").equals("authorization") && !(boolean)session.getAttributes().get("authenticated")){
            String token = messageJson.get("authorization") == null ? "" : messageJson.get("authorization");
            String accountId = messageJson.get("accountId") == null ? "guest" : messageJson.get("accountId");

            ResponseEntity response = authorizationService.authenticate(accountId,channelName,token);
            if(response.getStatusCode() == HttpStatus.OK){
                String body = ((HttpEntity<String>)(response.getBody())).getBody();
                Map<String, Object> responseJson = new Gson().fromJson(body, Map.class);
                if(!(boolean)responseJson.get("canRead")){
                    channelService.getSessions().get(channelName).remove(session);
                    session.sendMessage(
                            new TextMessage(new Gson().toJson(new Error(Response.NotAuthorizedToRead))));
                }
                else {
                    ObjectMapper objectMapper = new ObjectMapper();
                    List<Message> validMessages = messageRepository.findByParentAndDateOfCreationGreaterThan(
                            channelRepository.findByName(channelName), (Instant.now().minusSeconds(30*60)).toString());
                    for (Message m : validMessages) {
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
                            new TextMessage(new Gson().toJson(new Success(Response.AuthorizationSuccessful))));
                }
            }
            else{
                session.sendMessage(
                        new TextMessage(new Gson().toJson(response.getBody())));
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
                                .toJson(new Error(Response.NotAuthorizedToWrite))));
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception, IOException {
        channelService.addSession(session);
    }
}