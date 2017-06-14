package com.example.manan.hygiea;

/**
 * Created by manan on 4/15/17.
 */

public class ChatBubble {
    private boolean myMessage;
    private String content;
    public ChatBubble(String content, boolean myMessage) {
        this.content = content;
        this.myMessage = myMessage;
    }

    public String getContent() {
        return content;
    }

    public boolean myMessage() {
        return myMessage;
    }
}
