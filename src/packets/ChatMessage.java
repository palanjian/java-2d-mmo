package packets;

import java.io.Serializable;

public class ChatMessage implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String message;
	private String sender;
	private String messageType;
	private String recipient; //"all", "world", "guild", "whisper"
	
	public ChatMessage(String message, String sender, String messageType){
		this.message = message;
		this.sender = sender;
		this.messageType = messageType;
	}
	
	public String getMessage() { return message; }
	public void setMessage(String message) { this.message = message; }
	public String getRecipient() { return recipient; }
	public void setRecipient(String recipient) { this.recipient = recipient; }
	public String getSender() { return sender; }
	public void setSender(String sender) { this.sender = sender; }
	public String getMessageType() { return messageType; }
	public void setMessageType(String messageType) { this.messageType = messageType; }
}
