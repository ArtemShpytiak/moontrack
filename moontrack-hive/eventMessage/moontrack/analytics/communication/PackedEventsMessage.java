package moontrack.analytics.communication;

import java.io.Serializable;

import core.cluster.core.CommunicationMessage;
import core.cluster.core.MessageProcessor;
import core.cluster.core.MessageProcessorProvider;

//DO NOT CHANGE, DO NOT MOVE, DO NOT RENAME
public class PackedEventsMessage extends CommunicationMessage implements Serializable {
	private static final long serialVersionUID = 8012367706644547540L;
	//DO NOT CHANGE, DO NOT MOVE, DO NOT RENAME
	public final byte[] data;
	//DO NOT CHANGE, DO NOT MOVE, DO NOT RENAME
	public PackedEventsMessage(byte[] data) {
		this.data = data;
	}

	@Override
	public MessageProcessor<?> getMessageProcessor() {
		return MessageProcessorProvider.instance.get(this);
	}

}
