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

package de.iritgo.aktario.core.application;


import de.iritgo.aktario.core.AktarioPlugin;
import de.iritgo.aktario.core.Engine;
import javax.swing.ImageIcon;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * An instance of this class describes a single collaboration application.
 *
 * @version $Id: Application.java,v 1.8 2006/09/25 10:34:32 grappendorf Exp $
 */
public class Application
{
	/** Applications mapped by id. */
	private static Map applications;

	static
	{
		applications = new HashMap ();
	}

	/** The application id. */
	private String id;

	/** Aktario plugin that contains this application. */
	private AktarioPlugin plugin;

	/** The application name (resource key). */
	private String nameKey;

	/** The application icon (resource key). */
	private String iconKey;

	/** The id of the main gui pane. */
	private String guiPaneId;

	/** Application icon. */
	private ImageIcon icon;

	/**
	 * Create a new Application.
	 *
	 * @param id The application id.
	 * @param plugin Aktario plugin that contains this application.
	 * @param nameKey The application name (resource key).
	 * @param iconKey The application icon (resource key).
	 * @param guiPaneId The id of the main gui pane.
	 */
	public Application (String id, AktarioPlugin plugin, String nameKey, String iconKey, String guiPaneId)
	{
		this.id = id;
		this.nameKey = nameKey;
		this.iconKey = iconKey;
		this.plugin = plugin;
		this.guiPaneId = guiPaneId;
	}

	/**
	 * Get the application id.
	 *
	 * @return The application id.
	 */
	public String getId ()
	{
		return id;
	}

	/**
	 * Get the application name key.
	 *
	 * @return The application name key.
	 */
	public String getNameKey ()
	{
		return nameKey;
	}

	/**
	 * Get the application name.
	 *
	 * @return The application name.
	 */
	public String getName ()
	{
		return Engine.instance ().getResourceService ().getString (nameKey);
	}

	/**
	 * Get the application icon key.
	 *
	 * @return The application icon key.
	 */
	public String getIconKey ()
	{
		return iconKey;
	}

	/**
	 * Get the application icon.
	 *
	 * @return The application icon.
	 */
	public ImageIcon getIcon ()
	{
		if (icon == null)
		{
			URL resource = plugin.getClass ().getResource (iconKey);

			if (resource != null)
			{
				icon = new ImageIcon (resource);
			}
		}

		return icon;
	}

	/**
	 * Get the plugin.
	 *
	 * @return The plugin.
	 */
	public AktarioPlugin getPlugin ()
	{
		return plugin;
	}

	/**
	 * Get the id of the main gui pane..
	 *
	 * @return The gui pane id.
	 */
	public String getGuiPaneId ()
	{
		return guiPaneId;
	}

	/**
	 * Create a string representation of the application.
	 *
	 * @return The string representation.
	 */
	@Override
	public String toString ()
	{
		return getName ();
	}

	/**
	 * Add a an application to the application registry.
	 *
	 * @param application The application to add.
	 */
	public static void add (Application application)
	{
		applications.put (application.getId (), application);
	}

	/**
	 * Get an iterator over all available applications.
	 *
	 * @return An application iterator.
	 */
	public static Iterator iterator ()
	{
		return applications.values ().iterator ();
	}

	/**
	 * Retrieve an application.
	 *
	 * @param id The application id.
	 * @return The application or null if it wasn't found.
	 */
	public static Application get (String id)
	{
		return (Application) applications.get (id);
	}
}
