package com.jtorrnet.lib.messaging;

import com.jtorrnet.lib.messaging.typing.MessageType;
import com.jtorrnet.lib.messaging.typing.RequestType;

import java.util.Arrays;

public class Message {
    // Message contruction
    /*
        |REQ/RES| 3 characters
        |TYPE| 5 characters
        |BODY| variable size
        |'EOF'| indicates that message is done.
        3 characters
     */
    protected MessageType type; // is it a request or a response to a request.
    protected RequestType requestType;
    protected String body;

    public Message(MessageType type, RequestType requestType, String body) {
        this.type = type;
        this.requestType = requestType;
        this.body = body;
    }

    public Message(String message) {
        String[] splitMessage = message.split("\\$");
        this.type = MessageType.valueOf(splitMessage[0]);
        this.requestType = RequestType.valueOf(splitMessage[1]);
        this.body = splitMessage[2];
    }

    public String getMessage() {

        return type.toString() +
                "$" +
                requestType.toString() +
                "$" +
                body +
                "$" +
                "EOF";

    }


    public MessageType getType() {
        return type;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public String getBody() {
        return body;
    }
}
