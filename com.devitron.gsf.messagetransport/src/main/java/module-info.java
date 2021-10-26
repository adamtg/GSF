module com.devitron.gsf.messagetransport {
    requires com.devitron.gsf.common;
    requires com.rabbitmq.client;
    requires com.devitron.gsf.messagerouter;

    exports com.devitron.gsf.messagetransport;
    exports com.devitron.gsf.messagetransport.exceptions;
    exports com.devitron.gsf.messagetransport.sysinit to com.devitron.gsf.messagerouter;

}