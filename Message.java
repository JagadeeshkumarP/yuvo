package com.example.yuvo;

public class Message {
    private String text;
    private com.example.yuvo.MemberData memberData;
    private boolean belongsToCurrentUser;

    public Message(String text, com.example.yuvo.MemberData data, boolean belongsToCurrentUser) {
        this.text = text;
        this.memberData = data;
        this.belongsToCurrentUser = belongsToCurrentUser;
    }

    public String getText() {
        return text;
    }

    public com.example.yuvo.MemberData getMemberData() {
        return memberData;
    }

    public boolean isBelongsToCurrentUser() {
        return belongsToCurrentUser;
    }
}
