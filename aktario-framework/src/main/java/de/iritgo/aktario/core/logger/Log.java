/**
 * This file is part of the Iritgo/Aktario Framework.
 *
 * Copyright (C) 2005-2011 Iritgo Technologies.
 * Copyright (C) 2003-2005 BueroByte GbR.
 *
 * Iritgo licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.iritgo.aktario.core.logger;


import java.util.List;
import java.util.Map;


/**
 * The Log class provides easy access to the logging system.
 *
 * A Log singleton is initialized a program start time and lets you log
 * messages by simply calling
 * <pre>
 *     Log.log ("server", "AClass.someMethod", "Something happened", Log.ERROR);
 * </pre>
 *
 * For the predefined log levels you can use the shortcut methods, e.g.
 * <pre>
 *     Log.logError ("server", "AClass.someMethod", "Something happened");
 * </pre>
 */
public class Log
{
	/** Log level for dsplaying all messages. */
	public static final int ALL = 0;

	/** Log level for verbose debug messages. */
	public static final int VERBOSE = 5;

	/** Log level for debug messages. */
	public static final int DEBUG = 10;

	/** Log level for informational messages. */
	public static final int INFO = 20;

	/** Log level for warning messages. */
	public static final int WARN = 30;

	/** Log level for error messages. */
	public static final int ERROR = 40;

	/** Log level for fatal messages. */
	public static final int FATAL = 100;

	/** Log level for displaying no messages. */
	public static final int NONE = Integer.MAX_VALUE;

	/** Logger categories accessable by name. */
	static Map categories = null;

	/** Logger accessable by name. */
	static Map loggers = null;

	/**
	 * The log level. Display only messages with a log level higher
	 * or equal to this level.
	 */
	static int logLevel = INFO;

	/** Log level names. */
	static Map logLevelNames;

	/**
	 * Create a new Log object.
	 */
	public Log ()
	{
	}

	/**
	 * Set the loggers an the log categories.
	 *
	 * @param categories The logger categories.
	 * @param loggers The loggers.
	 */
	public static void setloggerRegistries (Map categories, Map loggers)
	{
		Log.categories = categories;
		Log.loggers = loggers;
	}

	/**
	 * Set log level.
	 * Only log messages with a log level higher or equal to this one will be
	 * displayed.
	 *
	 * @param level The new log level.
	 */
	public static void setLevel (int level)
	{
		logLevel = level;
	}

	/**
	 * Send a log message to the logger.
	 *
	 * @param category The category of the log message.
	 * @param source A description of the source of this log message.
	 * @param message The log message itself.
	 * @param level The level of the log message.
	 */
	public static void log (String category, String source, String message, int level)
	{
		if (categories == null)
		{
			return;
		}

		if (! categories.containsKey (category) || level < logLevel)
		{
			return;
		}

		List loggerlist = (List) categories.get (category);

		for (int i = 0; i < loggerlist.size (); ++i)
		{
			((Logger) loggers.get (loggerlist.get (i))).log (category, source, message, level);
		}
	}

	/**
	 * Send a log message to the logger.
	 * This method implicitly uses the VERBOSE log level.
	 *
	 * @param category The category of the log message.
	 * @param source A description of the source of this log message.
	 * @param message The log message itself.
	 */
	public static void logVerbose (String category, String source, String message)
	{
		log (category, source, message, VERBOSE);
	}

	/**
	 * Send a log message to the logger.
	 * This method implicitly uses the VERBOSE log level.
	 *
	 * @param category The category of the log message.
	 * @param message The log message itself.
	 */
	public static void logVerbose (String category, String message)
	{
		log (category, null, message, VERBOSE);
	}

	/**
	 * Send a log message to the logger.
	 * This method implicitly uses the DEBUG log level.
	 *
	 * @param category The category of the log message.
	 * @param source A description of the source of this log message.
	 * @param message The log message itself.
	 */
	public static void logDebug (String category, String source, String message)
	{
		log (category, source, message, DEBUG);
	}

	/**
	 * Send a log message to the logger.
	 * This method implicitly uses the DEBUG log level.
	 *
	 * @param category The category of the log message.
	 * @param message The log message itself.
	 */
	public static void logDebug (String category, String message)
	{
		log (category, null, message, DEBUG);
	}

	/**
	 * Send a log message to the logger.
	 * This method implicitly uses the INFO log level.
	 *
	 * @param category The category of the log message.
	 * @param source A description of the source of this log message.
	 * @param message The log message itself.
	 */
	public static void logInfo (String category, String source, String message)
	{
		log (category, source, message, INFO);
	}

	/**
	 * Send a log message to the logger.
	 * This method implicitly uses the INFO log level.
	 *
	 * @param category The category of the log message.
	 * @param message The log message itself.
	 */
	public static void logInfo (String category, String message)
	{
		log (category, null, message, INFO);
	}

	/**
	 * Send a log message to the logger.
	 * This method implicitly uses the WARN log level.
	 *
	 * @param category The category of the log message.
	 * @param source A description of the source of this log message.
	 * @param message The log message itself.
	 */
	public static void logWarn (String category, String source, String message)
	{
		log (category, source, message, WARN);
	}

	/**
	 * Send a log message to the logger.
	 * This method implicitly uses the WARN log level.
	 *
	 * @param category The category of the log message.
	 * @param message The log message itself.
	 */
	public static void logWarn (String category, String message)
	{
		log (category, null, message, WARN);
	}

	/**
	 * Send a log message to the logger.
	 * This method implicitly uses the ERROR log level.
	 *
	 * @param category The category of the log message.
	 * @param source A description of the source of this log message.
	 * @param message The log message itself.
	 */
	public static void logError (String category, String source, String message)
	{
		log (category, source, message, ERROR);
	}

	/**
	 * Send a log message to the logger.
	 * This method implicitly uses the ERROR log level.
	 *
	 * @param category The category of the log message.
	 * @param message The log message itself.
	 */
	public static void logError (String category, String message)
	{
		log (category, null, message, ERROR);
	}

	/**
	 * Send a log message to the logger.
	 * This method implicitly uses the FATAL log level.
	 *
	 * @param category The category of the log message.
	 * @param source A description of the source of this log message.
	 * @param message The log message itself.
	 */
	public static void logFatal (String category, String source, String message)
	{
		log (category, source, message, FATAL);
	}

	/**
	 * Send a log message to the logger.
	 * This method implicitly uses the FATAL log level.
	 *
	 * @param category The category of the log message.
	 * @param message The log message itself.
	 */
	public static void logFatal (String category, String message)
	{
		log (category, null, message, FATAL);
	}

	/**
	 * Get the name of a log level.
	 *
	 * @param level The log level.
	 * @return The name of the log level.
	 */
	public static String logLevelName (int level)
	{
		switch (level)
		{
			case VERBOSE:
				return "VERB";

			case DEBUG:
				return "DEBG";

			case INFO:
				return "INFO";

			case WARN:
				return "WARN";

			case ERROR:
				return "ERRR";

			case FATAL:
				return "FAIL";

			default:
				return "????";
		}
	}
}
