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

package de.iritgo.aktario.core.plugin;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.splash.Splash;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;


/**
 * The plugin manager handles loading and unloading of Iritgo plugins.
 */
public class PluginManager extends BaseObject
{
	/** List of all plugins. */
	private List<PluginContext> plugins;

	/** The Iritgo engine. */
	private Engine engine;

	/**
	 * Crate a new PluginManager.
	 *
	 * @param engine The Iritgo engine.
	 */
	public PluginManager (Engine engine)
	{
		this.engine = engine;
		plugins = new LinkedList<PluginContext> ();
	}

	/**
	 * Load all plugins.
	 */
	public void loadPlugins ()
	{
		String pluginList = System.getProperty ("iritgo.plugins");

		if (pluginList != null)
		{
			loadPluginsFromClassPath (pluginList);

			return;
		}

		InputStream in = Engine.class.getResourceAsStream ("/plugin.properties");

		if (in != null)
		{
			Properties pluginProperties = new Properties ();

			try
			{
				pluginProperties.load (in);

				if (pluginProperties.get ("plugin.names") != null)
				{
					loadPluginsFromClassPath (pluginProperties.getProperty ("plugin.names"));

					return;
				}
			}
			catch (IOException x)
			{
				Log.log ("plugin", "PluginManager", "Unable to load plugin descriptor /plugin.properties: " + x,
								Log.ERROR);
			}
		}

		String directory = Engine.instance ().getSystemDir () + Engine.instance ().getFileSeparator () + "plugins"
						+ Engine.instance ().getFileSeparator ();

		File dir = new File (directory);

		if (dir.exists ())
		{
			loadPluginsFromFile (directory);

			return;
		}
	}

	/**
	 * Load all plugins from the file system.
	 *
	 * @param directory The directory containing the plugins.
	 */
	public void loadPluginsFromFile (String directory)
	{
		try
		{
			final File pluginDir = new File (Engine.instance ().getSystemDir (), "plugins");

			File[] pluginFiles = pluginDir.listFiles (new FilenameFilter ()
			{
				public boolean accept (File dir, String name)
				{
					return name.endsWith (".jar");
				}
			});

			if (pluginFiles == null)
			{
				Log.log ("plugin", "PluginManager", "No plugins found in directory " + directory, Log.WARN);

				return;
			}

			for (int i = 0; i < pluginFiles.length; ++i)
			{
				String name = "";
				String dependency = "";
				String pluginClass = "";
				String displayName = "";

				String fileName = pluginFiles[i].getName ();
				String pluginName = fileName.substring (0, fileName.lastIndexOf ('-'));
				Properties properties = getPluginPropertiesFromFile (directory, fileName, pluginName);

				if (properties == null)
				{
					continue;
				}

				for (Enumeration<String> e = (Enumeration<String>) properties.propertyNames (); e.hasMoreElements ();)
				{
					dependency = "";
					pluginClass = "";
					name = "";

					String key = (String) e.nextElement ();

					if (key.startsWith ("name"))
					{
						name = properties.getProperty (key);
					}

					dependency = properties.getProperty (name + ".dependency");
					pluginClass = properties.getProperty (name + ".pluginclass");
					displayName = properties.getProperty ("displayName");

					if (name.length () != 0)
					{
						loadPluginFromFile (directory, fileName, pluginClass, name, displayName, dependency);
					}
				}
			}
		}
		catch (IOException x)
		{
			Log.log ("plugin", "PluginManager", "Error while loading plugins from directory " + directory + ": " + x,
							Log.ERROR);
		}
	}

	/**
	 * Load a plugin from the file system.
	 *
	 * @param directory The directory containing the plugin.
	 * @param fileName The file name of the plugin.
	 * @param pluginName The name of the plugin.
	 * @param name The display name of the plugin.
	 * @param dependency Dependencies to other plugins.
	 */
	public void loadPluginFromFile (String directory, String fileName, String pluginName, String name,
					String displayName, String dependency) throws IOException
	{
		Log.log ("plugin", "PluginManager", "Loading plugin: " + fileName, Log.INFO);

		File dir = new File (directory + fileName);

		if (pluginName.length () == 0)
		{
			Log.log ("plugin", "PluginManager", "No plugin name specified in descriptor " + fileName, Log.ERROR);

			return;
		}

		try
		{
			URLClassLoader loader = new URLClassLoader (new URL[]
			{
				dir.toURI ().toURL ()
			}, Thread.currentThread ().getContextClassLoader ());

			Plugin plugin = (Plugin) (loader.loadClass (pluginName).newInstance ());

			plugin.setClassName (pluginName);
			plugin.setName (name);
			plugin.setClassLoader (loader);
			plugin.setDisplayName (displayName);
			plugin.setDependency (dependency);

			plugins.add (new PluginContext (plugin, loader));
		}
		catch (Exception x)
		{
			Log.log ("plugin", "PluginManager", "Unable to load plugin " + pluginName + ": " + x, Log.ERROR);
		}
	}

	/**
	 * Read the plugin descriptor from the jar file.
	 *
	 * @param directory The directory containing the plugin.
	 * @param fileName The name of the jar file.
	 * @param pluginName TODO
	 */
	public Properties getPluginPropertiesFromFile (String directory, String fileName, String pluginName)
	{
		try
		{
			JarFile jarFile = new JarFile (directory + fileName);
			ZipEntry entry = jarFile.getEntry ("resources/" + pluginName + "-plugin.properties");

			if (entry != null)
			{
				Properties properties = new Properties ();
				InputStream is = jarFile.getInputStream (entry);

				properties.load (is);

				return properties;
			}
		}
		catch (Exception x)
		{
			Log.log ("plugin", "PluginManager", "Unable to read plugin descriptor " + fileName + ": " + x, Log.ERROR);
		}

		return null;
	}

	/**
	 * Load all plugins from the class path.
	 *
	 * @param pluginList A comma separated list of plugins to load.
	 */
	public void loadPluginsFromClassPath (String pluginList)
	{
		StringTokenizer st = new StringTokenizer (pluginList, ",");

		while (st.hasMoreTokens ())
		{
			String pluginName = st.nextToken ();

			Log.log ("plugin", "PluginManager", "Loading plugin " + pluginName, Log.INFO);

			try
			{
				Properties properties = getPluginPropertiesFromClassPath (pluginName);

				if (properties == null)
				{
					continue;
				}

				String name = properties.getProperty ("name");
				String displayName = properties.getProperty ("displayName");
				String className = properties.getProperty (name + ".pluginclass");
				String dependency = properties.getProperty (name + ".dependency");

				Plugin plugin = (Plugin) (getClass ().getClassLoader ().loadClass (className).newInstance ());

				plugin.setClassName (pluginName);
				plugin.setName (name);
				plugin.setClassLoader (getClass ().getClassLoader ());
				plugin.setDisplayName (displayName);
				plugin.setDependency (dependency);

				plugins.add (new PluginContext (plugin, getClass ().getClassLoader ()));
			}
			catch (Exception x)
			{
				Log.log ("plugin", "PluginManager", "Unable to load plugin " + pluginName + ": " + x, Log.ERROR);
			}
		}
	}

	/**
	 * Read the plugin descriptor via the plugins class loader.
	 *
	 * @param pluginName The plugin name.
	 */
	public Properties getPluginPropertiesFromClassPath (String pluginName)
	{
		try
		{
			Properties properties = new Properties ();

			properties.load (getClass ().getResourceAsStream ("/resources/" + pluginName + "-plugin.properties"));

			return properties;
		}
		catch (Exception x)
		{
			Log.log ("plugin", "PluginManager", "Unable to read descriptor of plugin " + pluginName + ": " + x,
							Log.ERROR);
		}

		return null;
	}

	/**
	 * Initialize all plugins.
	 */
	public void initPlugins (final Splash splash)
	{
		PluginProcessor processor = new PluginProcessor (plugins);

		processor.doPlugins (new PluginProcess ()
		{
			public void doPlugin (Plugin plugin)
			{
				Log.log ("plugin", "PluginManager", "Initializing plugin: " + plugin.getClassName (), Log.INFO);
				plugin.init (engine);

				if (splash != null)
				{
					splash.setText ("Initializing: " + plugin.getDisplayName ());
				}
			}
		}, PluginProcessor.FORWARD);

		Engine.instance ().getEventRegistry ().fire ("Plugin",
						new PluginStateEvent (null, PluginStateEvent.ALL_PLUGINS_INITIALIZED));
	}

	/**
	 * Unload all plugins
	 */
	public void unloadPlugins ()
	{
		PluginProcessor processor = new PluginProcessor (plugins);

		processor.doPlugins (new PluginProcess ()
		{
			public void doPlugin (Plugin plugin)
			{
				Log.log ("plugin", "PluginManager", "Unloading plugin: " + plugin.getClassName (), Log.INFO);
				plugin.unloadPlugin (engine);
			}
		}, PluginProcessor.BACKWARD);

		plugins.clear ();
	}

	/**
	 * Load the text resources of all plugins.
	 */
	public void loadTranslationResources ()
	{
		PluginProcessor processor = new PluginProcessor (plugins);

		processor.doPlugins (new PluginProcess ()
		{
			public void doPlugin (Plugin plugin)
			{
				Log.log ("plugin", "PluginManager", "Loading plugin text resources: " + plugin.getClassName (),
								Log.INFO);
				plugin.loadTranslationResources ();
			}
		}, PluginProcessor.FORWARD);
	}

	/**
	 * Unload the text resources of all plugins.
	 */
	public void unloadTranslationResources ()
	{
		PluginProcessor processor = new PluginProcessor (plugins);

		processor.doPlugins (new PluginProcess ()
		{
			public void doPlugin (Plugin plugin)
			{
				Log
								.log ("plugin", "PluginManager", "Unload plugin text resources: "
												+ plugin.getClassName (), Log.INFO);
				plugin.unloadTranslationResources ();
			}
		}, PluginProcessor.BACKWARD);
	}

	/**
	 * Retrieve a plugin.
	 *
	 * @param name The plugin name.
	 * @return The plugin or null if it wasn't found.
	 */
	public Plugin getPlugin (String name)
	{
		for (Iterator<PluginContext> i = plugins.iterator (); i.hasNext ();)
		{
			PluginContext context = (PluginContext) i.next ();

			if (context.getPlugin ().getName ().equals (name))
			{
				return context.getPlugin ();
			}
		}

		return null;
	}

	/**
	 * Retrieve a class loader.
	 *
	 * @param name The plugin name.
	 * @return The class loader from the plugin.
	 */
	public ClassLoader getClassLoaderByPlugin (String name)
	{
		for (Iterator<PluginContext> i = plugins.iterator (); i.hasNext ();)
		{
			PluginContext context = (PluginContext) i.next ();

			if (context.getPlugin ().getName ().equals (name))
			{
				return context.getClassLoader ();
			}
		}

		return null;
	}
}
