module com.devitron.gsf.messagerouter {
    requires com.devitron.gsf.common;
    requires com.devitron.gsf.messagetransport;
    requires com.devitron.gsf.utilities;

    exports com.devitron.gsf.messagerouter.exception;
    exports com.devitron.gsf.messagerouter.register;
    exports com.devitron.gsf.messagerouter.messages;
}