package dev.mehmet27.rokbot.peerlessscholar;

public class QuestionNotFoundException extends Exception {

	public QuestionNotFoundException(String message) {
		super(message);
	}

	public QuestionNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
