package ChatProject;

public interface Response{
    String ChannelCreationSuccess = "CHANNEL_CREATION_SUCCESS";
    String ChannelCreationFailed = "CHANNEL_CREATION_FAILED";
    String ChannelAlreadyExists = "CHANNEL_ALREADY_EXISTS";
    String ChannelStatusChanged = "CHANNEL_STATUS_CHANGED";
    String ChannelNotFound = "CHANNEL_NOT_FOUND";
    String SessionNotFound = "SESSION_NOT_FOUND";
    String ChannelStatus = "CHANNEL_";
    String NoSessionChannelStatus = "NO_SESSION_CHANNEL_";
    String AuthorizationSuccess = "AUTHORIZATION_SUCCESS";
    String AuthorizationFailed = "AUTHORIZATION_FAILED";
    String NotAuthorized = "NOT_AUTHORIZED";
    String BadNameField = "BAD_NAME_FIELD";
    String BadStatusField = "BAD_STATUS_FIELD";

    enum Type{
        MESSAGE, ERROR, SUCCESS
    }
}
