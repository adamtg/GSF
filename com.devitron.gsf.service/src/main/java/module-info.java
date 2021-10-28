module com.devitron.gsf.service {

    requires com.devitron.gsf.common;
    requires com.devitron.gsf.messagetransport;
    requires com.devitron.gsf.messagerouter;
    requires com.devitron.gsf.utilities;

    exports com.devitron.gsf.service;
    exports com.devitron.gsf.service.exceptions;
    exports com.devitron.gsf.service.annotations;

}