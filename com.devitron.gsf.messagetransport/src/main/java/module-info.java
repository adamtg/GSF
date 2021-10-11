module com.devitron.gsf.messagetransport {
    requires com.devitron.gsf.common;

    exports com.devitron.gsf.messagetransport;
    exports com.devitron.gsf.messagetransport.sysinit to com.devitron.gsf.messagerouter;

}