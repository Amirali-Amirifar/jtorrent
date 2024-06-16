package com.jtorrnet.models.message;

import java.io.Serializable;

public class Message implements Serializable {

    private final MessageTypes type;
    private final String text;

    public Message(MessageTypes type, String text) {
        this.text = text;
        this.type = type;

    }

    public MessageTypes getType() {
        return type;
    }

    public String getText() {
        return text;
    }
}

