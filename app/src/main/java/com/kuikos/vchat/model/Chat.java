package com.kuikos.vchat.model;

/**
 * @author greg
 * @since 6/21/13
 */
public class Chat {

    private String message;
    private String author_username;
    private String gender;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    private Chat() {
    }

    public Chat(String message, String author_username, String gender) {
        this.message = message;
        this.author_username = author_username;
        this.gender=gender;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor_username() {
        return author_username;
    }

    public String getGender() {
        return gender;
    }
}
