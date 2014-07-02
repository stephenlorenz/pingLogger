package com.patientping.jboss.module;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.logging.ErrorManager;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class DynamoDBLoggerHandler extends Handler {

    private String logFile;
    private String jndiLookup;
    private Boolean debugMode;

    public BufferedWriter out = null;

    public DynamoDBLoggerHandler() {
        super();
        debugMode = false;
        logFile = "";
    }

    @Override
    public void publish(LogRecord record) {
        if (!initialize()) {
            return;
        }
        if (isLoggable(record)) {
            process(record);
        }
    }

    private synchronized boolean initialize() {
        if (out == null && logFile != null && !logFile.equals("")) {
            FileWriter fstream = null;
            try {
                fstream = new FileWriter(logFile, true);
                out = new BufferedWriter(fstream);
            } catch (IOException e) {
                reportError(e.getMessage(), e, ErrorManager.OPEN_FAILURE);
            }
            logToFile("Log file initialized. Logging to: " + logFile);
        }
        return true;
    }

    private void process(LogRecord logRecord) {

        if (debugMode) logToFile("New mail should be sent");
    }

    private void logToFile(String text) {
        try {
            if (out != null) {
                out.write((new Date()).toString() + "\t" + text + "\n");
                out.flush();
            }
        } catch (IOException e) {
            reportError(e.getMessage(), e, ErrorManager.WRITE_FAILURE);
        }

    }


    @Override
    public void flush() {
        try {
            if (out != null) {
                out.flush();
            }
        } catch (IOException e) {
            reportError(e.getMessage(), e, ErrorManager.FLUSH_FAILURE);
        }
    }


    @Override
    public void close() {
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                reportError(e.getMessage(), e, ErrorManager.CLOSE_FAILURE);
            }

        }
    }

    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }

    public void setJndiLookup(String jndiLookup) {
        this.jndiLookup = jndiLookup;
    }

    public void setDebugMode(String debugMode) {
        this.debugMode = false;
        try {
            this.debugMode = Boolean.parseBoolean(debugMode);
        } catch (Exception e) {
        }
    }
}