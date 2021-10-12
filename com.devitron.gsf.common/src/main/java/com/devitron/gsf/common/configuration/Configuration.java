package com.devitron.gsf.common.configuration;

public class Configuration {


    private Global global;

    private String service;
    private String versionNumber;
    private String configVersion;

    public Global getGlobal() {
        return global;
    }

    public void setGlobal(Global global) {
        this.global = global;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getConfigVersion() {
        return configVersion;
    }

    public void setConfigVersion(String configVersion) {
        this.configVersion = configVersion;
    }

    /**
     * Returns a configuration object.  It first loads the global conf, then
     * it loads the configuration, using the field service as the hint to the
     * file name.  It first searches "${GSF_HOME}"/config for the filename.
     * If that does not exist, it searches /etc/gsf/config
     *
     * @return a configuration object
     */
    public static Configuration loadConfiguration() {
        return null;
    }

    /**
     * Returns a configuration object.  It first loads the global conf, then
     * it loads the configuration, using the field service and configleName as the hint to the
     * file name.  It finds the files by:
     * 1. if configFilename starts with a "/", it treats it as an absolute filename
     * 2. if configFilename ends with ".conf", it uses that filename
     * 3. It first loads the global configuration.  It searches "${GSF_HOME}"/config first, then /etc/gsf/config
     * 4. It uses configFilename and the field serviceName as hints to the location of the service config filename.
     * if configFilename is not absolute, it looks in the same directories as the global config
     *
     * @param configFilename hint to what the configuration filename is
     * @return
     */
    public static Configuration loadConfiguration(String configFilename) {
        return null;
    }


    public class Global {
        private String messageBrokerName;
        private int messageBrokerPort;

        private String registerationQueue;



        public String getMessageBrokerName() {
            return messageBrokerName;
        }

        public void setMessageBrokerName(String messageBrokerName) {
            this.messageBrokerName = messageBrokerName;
        }

        public int getMessageBrokerPort() {
            return messageBrokerPort;
        }

        public void setMessageBrokerPort(int messageBrokerPort) {
            this.messageBrokerPort = messageBrokerPort;
        }


        public String getRegisterationQueue() {
            return registerationQueue;
        }

        public void setRegisterationQueue(String registerationQueue) {
            this.registerationQueue = registerationQueue;
        }
    }

}
