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

package de.iritgo.aktario.core.resource.locale;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.logger.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;


/**
 *
 */
public class TextResource extends ResourceBundle
{
	/** The text resources. */
	private Properties localisations;

	/**
	 * Create a new TextResource.
	 */
	public TextResource ()
	{
		super ();
		localisations = new Properties ();
	}

	/**
	 * Get an enumeration containing all text resource keys.
	 *
	 * @return The key enumeration.
	 */
	@Override
	public Enumeration getKeys ()
	{
		return localisations.keys ();
	}

	/**
	 * Retrieve a text resource by key.
	 *
	 * @param key The resource key.
	 * @return The resource value.
	 */
	@Override
	public Object handleGetObject (String key)
	{
		return localisations.get (key);
	}

	/**
	 * Get the parent resources.
	 *
	 * @return The parent resources.
	 */
	public ResourceBundle getParent ()
	{
		return parent;
	}

	/**
	 * Add a text resource.
	 *
	 * @param key The resource key.
	 * @param value The resource value.
	 */
	public void addResource (String key, String value)
	{
		localisations.put (key, value);
	}

	/**
	 * Load resources from a jar file
	 *
	 * @param directory The directory.
	 * @param fileName The Filename of the JAR-File.
	 * @param resourceName The ResourceName.
	 */
	public void loadFromJarFile (String directory, String fileName, String resourceName)
	{
		try
		{
			loadFromInputStream (createInputStreamFromJarFile (directory + Engine.instance ().getFileSeparator ()
							+ fileName, resourceName));
		}
		catch (IOException x)
		{
			Log.logFatal ("system", "TextResource.loadFromJarFile", "Unable to unload property file " + fileName + "!"
							+ resourceName + ": " + x.getMessage ());
		}
	}

	/**
	 * Load resources from file
	 *
	 * @param directory The directory.
	 * @param fileName The Filename of the JAR-File
	 */
	public void loadFromFile (String directory, String fileName)
	{
		try
		{
			File file = new File (directory + Engine.instance ().getFileSeparator () + fileName);

			loadFromInputStream (new FileInputStream (file));
		}
		catch (Exception x)
		{
			Log.logFatal ("system", "TextResource.loadFromFile", "Unable to load property file " + fileName + ": " + x);
		}
	}

	/**
	 * Load resources from file
	 *
	 * @param directory The directory.
	 * @param fileName The Filename of the JAR-File
	 */
	public void unloadFromFile (String directory, String fileName)
	{
		try
		{
			File file = new File (directory + Engine.instance ().getFileSeparator () + fileName);

			unloadFromInputStream (new FileInputStream (file));
		}
		catch (Exception x)
		{
			Log.logFatal ("system", "TextResource.unloadFromFile", "Unable to unload property file " + fileName + ": "
							+ x);
		}
	}

	/**
	 * Load resources from jar file
	 *
	 * @param directory The directory.
	 * @param fileName The Filename of the JAR-File.
	 * @param resourceName The ResourceName.
	 */
	public void unloadFromJarFile (String directory, String fileName, String resourceName)
	{
		try
		{
			unloadFromInputStream (createInputStreamFromJarFile (directory + Engine.instance ().getFileSeparator ()
							+ fileName, resourceName));
		}
		catch (IOException x)
		{
			Log.logFatal ("system", "TextResource.unloadFromJarFile", "Unable to unload property file " + fileName
							+ "!" + resourceName + ": " + x.getMessage ());
		}
	}

	/**
	 * Create an input stream which reads a resource file from a jar file.
	 *
	 * @param file The name of the jar file.
	 * @param resourceName The ResourceName.
	 */
	public InputStream createInputStreamFromJarFile (String file, String resourceName)
	{
		try
		{
			JarFile jarFile = new JarFile (file);
			ZipEntry entry = jarFile.getEntry (resourceName);

			if (entry != null)
			{
				return jarFile.getInputStream (entry);
			}
			else
			{
				throw new Exception ("entry not found.");
			}
		}
		catch (Exception x)
		{
			Log.logFatal ("system", "TextResource.createInputStreamFromJarFile", "Unable to load property file " + file
							+ "!" + resourceName + ": " + x.getMessage ());
		}

		return null;
	}

	/**
	 * Load resources from an input stream.
	 *
	 * @param in The input stream.
	 */
	public void loadFromInputStream (InputStream in) throws IOException
	{
		localisations.load (in);
	}

	/**
	 * Unload resources from an input stream.
	 *
	 * @param is The input stream.
	 */
	public void unloadFromInputStream (InputStream is) throws IOException
	{
		Properties properties = new Properties ();

		properties.load (is);

		for (Iterator i = properties.values ().iterator (); i.hasNext ();)
		{
			localisations.remove ((String) i.next ());
		}
	}

	/**
	 * Load the translations with a class loader.
	 *
	 * @param klass The class which class loader should be used.
	 * @param resourceName The name of the resource file.
	 */
	public void loadWithClassLoader (Class klass, String resourceName)
	{
		try
		{
			loadFromInputStream (klass.getResourceAsStream (resourceName));
		}
		catch (Exception x)
		{
			Log.logFatal ("system", "TextResource.loadWithClassLoader", "Unable to load resources " + klass.getName ()
							+ "!" + resourceName + ": " + x.getMessage ());
		}
	}

	/**
	 * Unload the translations with a class loader.
	 *
	 * @param klass The class which class loader should be used.
	 * @param resourceName The name of the resource file.
	 */
	public void unloadWithClassLoader (Class klass, String resourceName)
	{
		try
		{
			unloadFromInputStream (klass.getResourceAsStream (resourceName));
		}
		catch (Exception x)
		{
			Log.logFatal ("system", "TextResource.unloadWithClassLoader", "Unable to unload resources "
							+ klass.getName () + "!" + resourceName + ": " + x.getMessage ());
		}
	}
}
