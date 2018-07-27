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
    String EmptyName = "EMPTY_NAME";
    String EmptyStatus = "EMPTY_STATUS";
    String NoSuchStatus = "NO_SUCH_STATUS";
    String InvalidChannelIdEdit = "INVALID_CHANNEL_ID_EDIT";
    String InvalidNameEdit = "INVALID_NAME_EDIT";
    String InvalidDateOfCreationEdit = "INVALID_DATE_OF_CREATION_EDIT";
    String InvalidDateOfClosingEdit = "INVALID_DATE_OF_CLOSING_EDIT";

    enum Type{
        MESSAGE, ERROR, SUCCESS
    }
}
