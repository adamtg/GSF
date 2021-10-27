import com.devitron.gsf.common.message.Message;

import java.util.function.Function;

public class SendBuilder {

    private Message message = null;
    boolean isSync = false;
    Function<Message, Message> callback = null;


    public SendBuilder(Message message) {
        this.message = message;
    }


    public SendBuilder withSync(boolean isSync) {
        this.isSync = isSync;
        return this;
    }

    public SendBuilder withCallback(Function<Message, Message> callback) {
        this.callback = callback;
        return this;
    }



}



