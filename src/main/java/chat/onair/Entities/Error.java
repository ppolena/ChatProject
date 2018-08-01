package chat.onair.Entities;

import chat.onair.Interfaces.Response;
import lombok.Data;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Data
public class Error implements Response {
    private String errorId;
    private Type type;
    private String dateOfCreation;
    private String data;

    public Error(String data){
        this.errorId = UUID.randomUUID().toString();
        this.type = Response.Type.ERROR;
        this.dateOfCreation = DateTimeFormatter.ISO_INSTANT.format(Instant.now());
        this.data = data;
    }
}