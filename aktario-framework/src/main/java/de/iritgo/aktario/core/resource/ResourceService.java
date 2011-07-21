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

package de.iritgo.aktario.core.resource;


import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.resource.locale.TextResource;
import de.iritgo.aktario.core.resource.resourcetypes.ResourceImage;
import de.iritgo.aktario.core.resource.resourcetypes.ResourceObject;
import de.iritgo.aktario.core.resource.resourcetypes.ResourceString;
import de.iritgo.aktario.core.resource.resourcexmlparser.XMLParser;
import de.iritgo.aktario.core.tools.NamePartIterator;
import de.iritgo.simplelife.string.StringTools;
import java.awt.Image;
import java.net.URL;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


/**
 *
 */
public class ResourceService extends BaseObject
{
	private ResourceNode baseNode;

	private Locale locale;

	private ResourceBundle resourceBundle;

	public ResourceService (ResourceNode baseNode)
	{
		this.baseNode = baseNode;
		locale = Locale.GERMAN;
		resourceBundle = getResourceBundle ();
	}

	/**
	 * Load the XML desciption from an URL.
	 *
	 * @param url The URL to load
	 */
	public void loadResources (URL url)
	{
		@SuppressWarnings("unused")
		XMLParser parser = new XMLParser (url, this);

		Log.logInfo ("system", "ResourceService", "Resources loaded from URL '" + url + "'");
	}

	/**
	 * Load the XML-Desciption from file.
	 *
	 * @param fileName The filename to load
	 */
	public void loadResources (String fileName)
	{
		@SuppressWarnings("unused")
		XMLParser parser = new XMLParser (fileName, this);

		Log.logInfo ("system", "ResourceService", "Resources loaded from file '" + fileName + "'");
	}

	/**
	 * return the base node of resources
	 *
	 * @return The base node of resources.
	 */
	public ResourceNode getBaseNode ()
	{
		return baseNode;
	}

	/**
	 * Add node to the resources
	 *
	 * @param name The absloute treePos (Offset) of the Node.
	 * @param resourceLoader The ResourceLoader for the resource.
	 * @param newNode The Node.
	 */
	public void addNode (String name, String resourceLoader, ResourcePersistent newNode)
	{
		try
		{
			newNode.setResourceLoader (getResourceLoader (resourceLoader));
		}
		catch (ResourceNotFoundException e)
		{
		}

		addNode (name, newNode);
	}

	/**
	 * Add node to the resources
	 *
	 * @param treePos The absloute treePos (Offset) of the Node.
	 * @param newNode The Node.
	 */
	public void addNode (String treePos, ResourceNode newNode)
	{
		NamePartIterator i = new NamePartIterator (treePos);
		ResourceNode node = baseNode;
		ResourceNode lastNode = baseNode;

		while (i.hasNext ())
		{
			String partName = (String) i.next ();

			lastNode = node;
			node = node.getNodeByName (partName);

			if (node == null)
			{
				if (i.hasNext ())
				{
					node = new ResourceNode ("directory." + partName, partName);
					lastNode.addNode (node);

					continue;
				}
			}
		}

		lastNode.addNode (newNode);
	}

	/**
	 * return the node by name
	 *
	 * @return The node of resources by name.
	 */
	public ResourceNode getNode (String name) throws ResourceNotFoundException
	{
		NamePartIterator i = new NamePartIterator (name);
		ResourceNode node = baseNode;

		while (i.hasNext ())
		{
			node = node.getNodeByName ((String) i.next ());

			if (node == null)
			{
				throw new ResourceNotFoundException (name);
			}
		}

		return node;
	}

	/**
	 * return the resource
	 *
	 * @return The resource by name.
	 */
	public String getResourceDescription (String name) throws ResourceNotFoundException
	{
		return getNode (name).getDescription ();
	}

	/**
	 * Return the resource
	 *
	 * @param name The name/key of the resource.
	 * @param fireException
	 * @return The resource by name.
	 */
	public String getString (String name, boolean fireException) throws ResourceNotFoundException
	{
		name = name.replace (":", ".");

		try
		{
			return resourceBundle.getString (name);
		}
		catch (MissingResourceException x)
		{
		}

		try
		{
			return ((ResourceString) getNode (name)).getValue ();
		}
		catch (ResourceNotFoundException x)
		{
			if (fireException)
			{
				throw new ResourceNotFoundException (name);
			}
		}

		return name;
	}

	/**
	 * Return the resource
	 *
	 * @param name The name/key of the resource.
	 * @return The resource by name.
	 */
	public String getString (String name)
	{
		return getStringWithoutException (name);
	}

	/**
	 * Return the resource
	 *
	 * @param name The name/key of the resource.
	 * @return The resource by name, int.
	 */
	public int getInt (String name) throws ResourceNotFoundException
	{
		return Integer.parseInt (getString (name, true));
	}

	/**
	 * Return the resource
	 *
	 * @param name The name/key of the resource.
	 * @return The resource by name.
	 */
	public String getStringWithoutException (String name)
	{
		if (StringTools.isEmpty (name))
		{
			return "";
		}

		try
		{
			if (name.startsWith ("$"))
			{
				return getString (name.substring (1), false);
			}
			else
			{
				return getString (name, false);
			}
		}
		catch (ResourceNotFoundException x)
		{
		}

		return name;
	}

	/**
	 * Retrieve a resource string and replace any {n}-Parameters with the
	 * '|'-delimited key parameters.
	 *
	 * @param keyWithParams The resource key
	 * @return The translated string
	 */
	public String getStringWithParams (String keyWithParams)
	{
		if (StringTools.isEmpty (keyWithParams))
		{
			return "";
		}

		String[] parts = keyWithParams.split ("\\|");

		String text = getString (parts[0]);

		for (int i = 0; i < parts.length - 1; ++i)
		{
			text = text.replaceAll ("\\{" + i + "\\}", parts[i + 1]);
		}

		return text;
	}

	/**
	 * Retrieve a resource string and replace any {n}-Parameters with the
	 * suppplied parameter values.
	 *
	 * @param key The resource key
	 * @param params The string parameters
	 * @return The translated string
	 */
	public String getStringWithParams (String key, Object... params)
	{
		if (StringTools.isEmpty (key))
		{
			return "";
		}

		String text = getString (key);

		for (int i = 0; i < params.length; ++i)
		{
			text = text.replaceAll ("\\{" + i + "\\}", params[i].toString ());
		}

		return text;
	}

	/**
	 * Return the ResourceLoader
	 *
	 * @return The ResourceLoader by name.
	 */
	public ResourceLoader getResourceLoader (String name) throws ResourceNotFoundException
	{
		return (ResourceLoader) ((ResourceObject) getNode (name)).getClone ();
	}

	/**
	 * Return the a Object
	 *
	 * @return The ResourceLoader by name.
	 */
	public Object getObject (String name) throws ResourceNotFoundException
	{
		return ((ResourceObject) getNode (name)).getClone ();
	}

	/**
	 * Return the imageobject.
	 *
	 * @return The imageobject.
	 */
	public Image getImage (String name) throws ResourceNotFoundException
	{
		return ((ResourceImage) getNode (name)).getImage ();
	}

	/**
	 * Return the imagecomponentobject.
	 *
	 * @return The imagecomponentobject.
	 */
	public ImageComponent getImageComponent (String name) throws ResourceNotFoundException
	{
		return ((ResourceImage) getNode (name)).getImageComponent ();
	}

	/**
	 * Set the Locale.
	 *
	 * @param locale The locale.
	 */
	public void setLocale (Locale locale)
	{
		this.locale = locale;
	}

	/**
	 * Set the Locale.
	 *
	 * @return Return the new ResourceBundle.
	 */
	public ResourceBundle getResourceBundle ()
	{
		if (resourceBundle == null)
		{
			resourceBundle = ResourceBundle.getBundle ("de.iritgo.aktario.core.resource.locale.TextResource", locale);
		}

		return resourceBundle;
	}

	/**
	 * Set the Locale and creates a new ResourceBundle.
	 *
	 * @param locale The new Locale.
	 */
	public void updateResourceBundle (Locale locale)
	{
		setLocale (locale);
		resourceBundle = ResourceBundle.getBundle ("de.iritgo.aktario.core.resource.locale.TextResource", locale);
	}

	/**
	 * Load the translations form a file.
	 *
	 * @param resourceDir The dir that contains the resource file.
	 * @param filePrefix The prefix of the translation file.
	 */
	public void loadTranslationsFromFile (String resourceDir, String filePrefix)
	{
		TextResource textResource = (TextResource) resourceBundle;

		textResource.loadFromFile (resourceDir, filePrefix + "_" + locale.getLanguage () + ".properties");
	}

	/**
	 * Unload the translations form a file.
	 *
	 * @param resourceDir The dir that contains the resource file.
	 * @param filePrefix The prefix of the translation file.
	 */
	public void unloadTranslationsFromFile (String resourceDir, String filePrefix)
	{
		TextResource textResource = (TextResource) resourceBundle;

		textResource.unloadFromFile (resourceDir, filePrefix + "_" + locale.getLanguage () + ".properties");
	}

	/**
	 * Load the translations form a jar file.
	 *
	 * @param resourceDir The directory containing the jar file.
	 * @param filename The name of the jar file.
	 * @param resourceName The name/path of the resource file.
	 */
	public void loadTranslationsFromJarFile (String resourceDir, String filename, String resourceName)
	{
		TextResource textResource = (TextResource) resourceBundle;

		textResource
						.loadFromJarFile (resourceDir, filename, resourceName + "_" + locale.getLanguage ()
										+ ".properties");
	}

	/**
	 * Unload the translations form a jar file.
	 *
	 * @param resourceDir The directory containing the jar file.
	 * @param filename The name of the jar file.
	 * @param resourceName The name/path of the resource file.
	 */
	public void unloadTranslationsFromJarFile (String resourceDir, String filename, String resourceName)
	{
		TextResource textResource = (TextResource) resourceBundle;

		textResource.unloadFromJarFile (resourceDir, filename, resourceName + "_" + locale.getLanguage ()
						+ ".properties");
	}

	/**
	 * Load the translations with a class loader.
	 *
	 * @param klass The class which class loader should be used.
	 * @param resourceName The name of the resource file.
	 */
	public void loadTranslationsWithClassLoader (Class klass, String resourceName)
	{
		TextResource textResource = (TextResource) resourceBundle;

		textResource.loadWithClassLoader (klass, resourceName + "_" + locale.getLanguage () + ".properties");
	}

	/**
	 * Unload the translations with a class loader.
	 *
	 * @param klass The class which class loader should be used.
	 * @param resourceName The name of the resource file.
	 */
	public void unloadTranslationsWithClassLoader (Class klass, String resourceName)
	{
		TextResource textResource = (TextResource) resourceBundle;

		textResource.unloadWithClassLoader (klass, resourceName + "_" + locale.getLanguage () + ".properties");
	}
}
