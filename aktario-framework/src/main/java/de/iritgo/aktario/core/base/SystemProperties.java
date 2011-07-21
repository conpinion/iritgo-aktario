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

package de.iritgo.aktario.core.base;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.simplelife.math.NumberTools;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;


/**
 * This is an enhanced version of Java's Property class.
 *
 * It provides convenient methods for an easier property value access.
 */
@SuppressWarnings("serial")
public class SystemProperties extends Properties
{
	/**
	 * Create a new SystemProperties
	 */
	public SystemProperties ()
	{
	}

	/**
	 * Load the properties from the property file.
	 *
	 * @param fileName The name of the property file.
	 */
	public void load (String fileName)
	{
		@SuppressWarnings("unused")
		Engine engine = Engine.instance ();

		try
		{
			super.load (new FileInputStream (fileName));

			Log.logInfo ("system", "SystemProperties.load", "System properties loaded from file '" + fileName + "'");
		}
		catch (IOException x)
		{
			Log.logWarn ("system", "SystemProperties.load", "Unable to load system properties file '" + fileName + "'");
		}
	}

	/**
	 * Store the properties to the property file.
	 *
	 * @param fileName The name of the property file.
	 */
	public void store (String fileName)
	{
		@SuppressWarnings("unused")
		Engine engine = Engine.instance ();

		try
		{
			super.store (new FileOutputStream (fileName), fileName);

			Log.logInfo ("system", "SystemProperties.store", "System properties stored to file '" + fileName + "'");
		}
		catch (IOException x)
		{
			Log.logError ("system", "SystemProperties.store", "Unable to store system properties file '" + fileName
							+ "'");
			x.printStackTrace ();
		}
	}

	/**
	 * Store a string value.
	 *
	 * @param key The value key.
	 * @param value The value.
	 */
	public void put (String key, String value)
	{
		setProperty (key, value);
	}

	/**
	 * Retrieve a string value.
	 *
	 * @param key The key of the property to retrieve
	 * @param defaultValue A default value in case the property wasn't found.
	 * @return The value of the property.
	 */
	public String getString (String key, String defaultValue)
	{
		return getProperty (key, defaultValue);
	}

	/**
	 * Store an integer value.
	 *
	 * @param key The value key.
	 * @param value The value.
	 */
	public void put (String key, int value)
	{
		setProperty (key, String.valueOf (value));
	}

	/**
	 * Retrieve a integer value.
	 *
	 * @param key The key of the property to retrieve
	 * @param defaultValue A default value in case the property wasn't found.
	 * @return The value of the property.
	 */
	public int getInt (String key, int defaultValue)
	{
		return NumberTools.toInt (getProperty (key), defaultValue);
	}

	/**
	 * Store a long integer value.
	 *
	 * @param key The value key.
	 * @param value The value.
	 */
	public void put (String key, long value)
	{
		setProperty (key, String.valueOf (value));
	}

	/**
	 * Retrieve a long integer value.
	 *
	 * @param key The key of the property to retrieve
	 * @param defaultValue A default value in case the property wasn't found.
	 * @return The value of the property.
	 */
	public long getLong (String key, long defaultValue)
	{
		return NumberTools.toLong (getProperty (key), defaultValue);
	}

	/**
	 * Store a boolean value.
	 *
	 * @param key The value key.
	 * @param value The value.
	 */
	public void put (String key, boolean value)
	{
		setProperty (key, String.valueOf (value));
	}

	/**
	 * Retrieve a boolean value.
	 *
	 * @param key The key of the property to retrieve
	 * @param defaultValue A default value in case the property wasn't found.
	 * @return The value of the property.
	 */
	public boolean getBool (String key, boolean defaultValue)
	{
		return NumberTools.toBool (getProperty (key), defaultValue);
	}
}
