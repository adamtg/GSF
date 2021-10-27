import com.devitron.gsf.common.message.Message;

import java.util.function.Function;

public class SendBuilder {

    private Message message = null;
    private String queueName = null;
    boolean isSync = false;
    Function<Message, Message> callback = null;


    public SendBuilder(Message message) {
        this.message = message;
    }

    public SendBuilder withQueue(String queueName) {
            this.queueName = queueName;
            return this;
    }

    public SendBuilder withSync(boolean isSync) {

    }



}



