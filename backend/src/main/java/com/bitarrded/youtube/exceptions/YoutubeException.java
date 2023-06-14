package com.bitarrded.youtube.exceptions;

public class YoutubeException extends RuntimeException {
    public YoutubeException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public YoutubeException(String exMessage) {
        super(exMessage);
    }
}
