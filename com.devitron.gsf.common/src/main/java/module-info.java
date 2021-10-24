module com.devitron.gsf.common {
    requires com.devitron.gsf.utilities;
    requires com.fasterxml.jackson.databind;

    exports com.devitron.gsf.common.message;
    exports com.devitron.gsf.common.configuration;
    exports com.devitron.gsf.common.configuration.exceptions;
}