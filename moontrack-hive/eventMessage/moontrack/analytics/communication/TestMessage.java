package moontrack.analytics.communication;

import java.io.Serializable;

import org.hibernate.Session;

import core.action.exceptions.InvalidActionException;
import core.cluster.core.CommunicationMessage;
import core.cluster.core.MessageProcessor;

import com.moonmana.log.Log;

public class TestMessage extends CommunicationMessage implements Serializable{
	private static final long serialVersionUID = -1209597025941631975L;
	private final String messageText;

	public TestMessage(String message) {
		this.messageText = message;
	}

	@Override
	public MessageProcessor<?> getMessageProcessor() {
		return new MessageProcessor<TestMessage>(this) {
			@Override
			protected void perform() throws InvalidActionException {
				Log.out("Test Message Received: " + messageText);
			}
			
			@Override
			protected Session createSession() {
				return null;
			}
			
			@Override
			public void clean() {
				//do nothing
			}
		};
	}
	
	


}
