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


import java.text.DateFormat;
import java.util.Date;


/**
 * A logger that prints logging messages to the system console.
 */
public class ConsoleLogger implements Logger
{
	/** The current time. */
	private Date currentTime;

	/** Used to format the current time. */
	private DateFormat timeFormat;

	/**
	 * Create a new console logger.
	 */
	public ConsoleLogger()
	{
		currentTime = new Date();
		timeFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
	}

	/**
	 * Get the id of the Logger
	 *
	 * @return The logger id.
	 */
	public String getId()
	{
		return "Console";
	}

	/**
	 * Initialize the logger.
	 *
	 * @param category The logger category.
	 */
	public void init(String category)
	{
	}

	/**
	 * Free all logger resources.
	 */
	public void dispose()
	{
	}

	/**
	 * Log a message.
	 *
	 * @param category The logger category.
	 * @param source The logging source.
	 * @param message The log message.
	 * @param level The logging level.
	 */
	public void log(String category, String source, String message, int level)
	{
		currentTime.setTime(System.currentTimeMillis());
		System.out.println("" + timeFormat.format(currentTime) + " " + Log.logLevelName(level) + " [" + category
						+ "] [" + source + "] " + message);
	}
}
