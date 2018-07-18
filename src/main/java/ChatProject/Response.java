package ChatProject;

public interface Response{
    String ChannelCreationSuccess = "CHANNEL_CREATION_SUCCESS";
    String ChannelCreationFailed = "CHANNEL_CREATION_FAILED";
    String ChannelStatusChanged = "CHANNEL_STATUS_CHANGED";
    String NoSession = "NO_SESSION";
    String ChannelStatus = "CHANNEL_";
    String NoSessionChannelStatus = "NO_SESSION_CHANNEL_";
    String AuthorizationSuccess = "AUTHORIZATION_SUCCESS";
    String AuthorizationFailed = "AUTHORIZATION_FAILED";
    String NotAuthorized = "NOT_AUTHORIZED";

    enum Type{
        MESSAGE, ERROR, SUCCESS
    }
}
