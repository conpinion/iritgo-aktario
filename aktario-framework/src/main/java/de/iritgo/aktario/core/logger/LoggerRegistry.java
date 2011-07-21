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


import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;


/**
 * This registry contains all system loggers.
 */
public class LoggerRegistry
{
	/** Logger catgeories. */
	static Hashtable categories = new Hashtable ();

	/** Loggers of one category. */
	static Hashtable loggers = new Hashtable ();

	/**
	 * Create a new LoggerRegistry.
	 */
	public LoggerRegistry ()
	{
		Log.setloggerRegistries (categories, loggers);
	}

	/**
	 * Initialize the base loggers.
	 */
	public void initBaseLogger ()
	{
		addLogger (new ConsoleLogger ());
	}

	/**
	 * Add a logger.
	 *
	 * @param category The logger category.
	 * @param loggerId The id
	 */
	public void addLogger (String category, String loggerId)
	{
		if (! categories.containsKey (category))
		{
			categories.put (category, new ArrayList ());
		}

		ArrayList loggerList = (ArrayList) categories.get (category);

		if (loggerList.contains (loggerId))
		{
			return;
		}

		loggerList.add (loggerId);

		((Logger) loggers.get (loggerId)).init (category);
	}

	/**
	 * Add a logger.
	 *
	 * @param logger The logger to add.
	 */
	public void addLogger (Logger logger)
	{
		if (! loggers.contains (logger.getId ()))
		{
			loggers.put (logger.getId (), logger);
		}
	}

	/**
	 * Remove a logger from a category.
	 *
	 * @param category The category from which to remove the logger.
	 * @param loggerId The id of the logger to remove.
	 */
	public void removeLogger (String category, String loggerId)
	{
		if (! categories.containsKey (category))
		{
			return;
		}

		ArrayList loggerList = (ArrayList) categories.get (category);

		for (int i = 0; i < loggerList.size (); ++i)
		{
			if (loggerList.get (i).equals (loggerId))
			{
				loggerList.remove (i);
			}
		}
	}

	/**
	 * Remove a logger from all categories.
	 *
	 * @param loggerId The id of the logger to remove.
	 */
	public void removeLogger (String loggerId)
	{
		Enumeration e = categories.elements ();

		while (e.hasMoreElements ())
		{
			ArrayList loggerList = (ArrayList) e.nextElement ();

			for (int i = 0; i < loggerList.size (); ++i)
			{
				if (loggerList.get (i).equals (loggerId))
				{
					loggerList.remove (i);
				}
			}
		}

		loggers.remove (loggerId);
	}

	/**
	 * Remove all loggers.
	 */
	public void dispose ()
	{
		for (Iterator i = loggers.values ().iterator (); i.hasNext ();)
		{
			Logger logger = (Logger) i.next ();

			logger.dispose ();
		}

		loggers.clear ();
		categories.clear ();
	}
}
