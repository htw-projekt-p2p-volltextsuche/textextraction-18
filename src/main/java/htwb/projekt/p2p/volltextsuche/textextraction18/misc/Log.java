package htwb.projekt.p2p.volltextsuche.textextraction18.misc;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Stefan Sadewasser 568158
 * My logger class
 */
public class Log {
	public enum LogLevel{
		ERROR, INFO, WARNING
	}
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_COLOR_END = "]";

    public static void log(LogLevel level, String message) {
        String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
        String logMessage = className + "." + methodName + "() at line[" + lineNumber + "]: " + message;
        switchLogLevel(level, fullClassName, logMessage);
    }    

    private static void switchLogLevel(LogLevel level, String fullClassName, String logMessage) {
        switch (level) {
            case ERROR:
                Logger.getLogger(fullClassName).log(Level.SEVERE, ANSI_RED + logMessage + ANSI_COLOR_END);
                break;
            case INFO:
                Logger.getLogger(fullClassName).log(Level.INFO, ANSI_GREEN + logMessage + ANSI_COLOR_END);
                break;
            case WARNING:
                Logger.getLogger(fullClassName).log(Level.WARNING, ANSI_YELLOW + logMessage + ANSI_COLOR_END);
                break;
            default:
                break;
        }
    }
}
