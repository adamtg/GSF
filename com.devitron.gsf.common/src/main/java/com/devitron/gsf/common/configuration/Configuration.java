package com.devitron.gsf.common.configuration;

import com.devitron.gsf.common.configuration.exceptions.ConfigFileNotFoundException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.devitron.gsf.common.configuration.exceptions.ConfigFileParseException;
import com.devitron.gsf.utilities.Json;
import com.devitron.gsf.utilities.exceptions.UtilitiesJsonParseException;

public class Configuration {

    static private String GSF_HOME_VAR_NAME = "GSF_HOME";
    static private String DEFAULT_GSF_HOMR = "/etc/gsf";
    static private String GLOBAL_CONFIG_NAME = "global.conf";


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
     * Figures out the fullpath of the filename
     *
     * @param configname configuration file name
     * @return the fullpath to the configuration file
     * @throws ConfigFileNotFoundException
     */
    private static String getFilename(String configname) throws ConfigFileNotFoundException {

        String fullFilepath = null;

        String gsfHome = System.getenv(GSF_HOME_VAR_NAME);

        if (gsfHome != null) {
            fullFilepath = gsfHome + "/config/" + configname;

            Path path = Paths.get(fullFilepath);

            System.out.println("GSF_HOME: " + fullFilepath);

            if (Files.notExists(path)) {

                fullFilepath = DEFAULT_GSF_HOMR + "/config/" + configname;
                path = Paths.get(fullFilepath);
                if (Files.notExists(path)) {
                    throw new ConfigFileNotFoundException(configname);
                }
            }
        }


        return fullFilepath;
    }

    /**
     * Loads the global configuration file
     *
     * @return Global object
     * @throws ConfigFileNotFoundException
     * @throws ConfigFileParseException
     */
    private static Global loadGlobal() throws ConfigFileNotFoundException, ConfigFileParseException {

        String filename = getFilename(GLOBAL_CONFIG_NAME);
        String rawJson = null;

        try {
            Path path = Path.of(filename);
            rawJson = Files.readString(path);
        } catch (IOException e) {
            throw new ConfigFileNotFoundException(e);
        }


        Global global = null;
        try {
            global = (Global)Json.jsonToObject(rawJson, Global.class);
        } catch (UtilitiesJsonParseException e) {
            throw new ConfigFileParseException(e);
        }

        return global;
    }


    /**
     * Merge the values from the global config file into the
     * service's global config.  The service's values will
     * always override the values from the global config.
     * @param globalGlobal global from the global config
     * @param serviceGlobal global from the service's config
     */
    private static void mergeGlobal(Global globalGlobal, Global serviceGlobal) {

        if (serviceGlobal.getMessageBrokerAddress() == null) {
            serviceGlobal.setMessageBrokerAddress(globalGlobal.getMessageBrokerAddress());
        }

        if (serviceGlobal.getMessageBrokerPort() == null) {
            serviceGlobal.setMessageBrokerPort(globalGlobal.getMessageBrokerPort());
        }

        if (serviceGlobal.getMessageBrokerUsername() == null) {
            serviceGlobal.setMessageBrokerUsername(globalGlobal.getMessageBrokerUsername());
        }

        if (serviceGlobal.getMessageBrokerPassword() == null) {
            serviceGlobal.setMessageBrokerPassword(globalGlobal.getMessageBrokerPassword());
        }


        if (serviceGlobal.getSendMessageQueue() == null) {
            serviceGlobal.setSendMessageQueue(globalGlobal.getSendMessageQueue());
        }


    }

    /**
     * Returns a configuration object.  It first loads the global conf, then
     * it loads the configuration, using the field service and configleName as the hint to the
     * file name.  If configFilename starts with a "/", it treats it as an absolute filename.  It
     * first loads the global configuration.  It searches "${GSF_HOME}"/config first, then /etc/gsf/config
     * if configFilename is not absolute, it looks in the same directories as the global config
     *
     * @param configFilename the configuration filename
     * @param configClass    the configuration class
     * @return
     */
    public static Configuration loadConfiguration(String configFilename, Class configClass) throws ConfigFileNotFoundException, ConfigFileParseException, ConfigFileParseException {

        Configuration config = null;
        String rawJson = null;

        Global global = loadGlobal();
        String filename = null;

        System.out.println("configFilename: " + configFilename);

        if (configFilename.startsWith("/")) {
            filename = configFilename;
        } else {
            filename = getFilename(configFilename);
        }
        try {
            Path path = Path.of(filename);
            rawJson = Files.readString(path);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ConfigFileNotFoundException(e);
        }

        try {
            config = (Configuration)Json.jsonToObject(rawJson, configClass);
        } catch (UtilitiesJsonParseException e) {
            throw new ConfigFileParseException(e);
        }

        if (config.getGlobal() == null) {
            config.setGlobal(new Global());
        }




        mergeGlobal(global, config.getGlobal());

        return config;
    }


    static public class Global {
        private String messageBrokerAddress;
        private Integer messageBrokerPort;

        private String messageBrokerUsername;
        private String messageBrokerPassword;

        private String sendMessageQueue;

        private int shutdownOnDup;
        private int shutdownOnUnique;


        public String getMessageBrokerAddress() {
            return messageBrokerAddress;
        }

        public void setMessageBrokerAddress(String messageBrokerAddress) {
            this.messageBrokerAddress = messageBrokerAddress;
        }

        public Integer getMessageBrokerPort() {
            return messageBrokerPort;
        }

        public void setMessageBrokerPort(Integer messageBrokerPort) {
            this.messageBrokerPort = messageBrokerPort;
        }

        public String getMessageBrokerUsername() {
            return messageBrokerUsername;
        }

        public void setMessageBrokerUsername(String messageBrokerUsername) {
            this.messageBrokerUsername = messageBrokerUsername;
        }

        public String getMessageBrokerPassword() {
            return messageBrokerPassword;
        }

        public void setMessageBrokerPassword(String messageBrokerPassword) {
            this.messageBrokerPassword = messageBrokerPassword;
        }

        public String getSendMessageQueue() {
            return sendMessageQueue;
        }

        public void setSendMessageQueue(String sendMessageQueue) {
            this.sendMessageQueue = sendMessageQueue;
        }

        public int getShutdownOnDup() {
            return shutdownOnDup;
        }

        public void setShutdownOnDup(int shutdownOnDup) {
            this.shutdownOnDup = shutdownOnDup;
        }

        public int getShutdownOnUnique() {
            return shutdownOnUnique;
        }

        public void setShutdownOnUnique(int shutdownOnUnique) {
            this.shutdownOnUnique = shutdownOnUnique;
        }

    }

}
