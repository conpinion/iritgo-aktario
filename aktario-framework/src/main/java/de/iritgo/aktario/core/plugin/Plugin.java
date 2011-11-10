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
import de.iritgo.aktario.core.action.Action;
import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.core.command.CommandRegistry;
import de.iritgo.aktario.core.gui.GUIPane;
import de.iritgo.aktario.core.gui.GUIPaneRegistry;
import de.iritgo.aktario.core.iobject.AbstractIObjectFactory;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.manager.Manager;
import de.iritgo.aktario.core.manager.ManagerRegistry;
import de.iritgo.aktario.core.resource.ResourceService;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * This is the primary class that a custom plugin must implement.
 * It contains overridable methods for initializing and unloading the plugin
 * and methods where one can register actions, commands, etc.
 */
public abstract class Plugin extends BaseObject
{
	/** Specifies a SERVER resource. */
	static protected int SERVER = 0;

	/** Specified a CLIENT resource. */
	static protected int CLIENT = 1;

	/** The system engine. */
	protected Engine engine;

	/** Determines server or client mode. */
	private int mode = 0;

	/** The resource service. */
	protected ResourceService resourceService;

	/** Factory that creates iobjects. */
	protected AbstractIObjectFactory iObjectFactory;

	/** Manager registry. */
	private ManagerRegistry managerRegistry;

	/** Gui pane registry. */
	private GUIPaneRegistry guiPaneRegistry;

	/** Command registry. */
	private CommandRegistry commandRegistry;

	/** List of all actions contained in this plugin. */
	private List actions;

	/** List of all managers contained in this plugin. */
	private List managerList;

	/** List of all gui panes contained in this plugin. */
	private List guiPanes;

	/** List of all console commands in this plugin. */
	@SuppressWarnings("unused")
	private List consoleCommands;

	/** List of all commands contained in this plugin. */
	private List commands;

	/** Name of the plugin class. */
	private String pluginClassName;

	/** Dependencies to other plugins. */
	private String dependency;

	/** Name of the plugin. */
	private String name;

	/** Display name of the plugin. */
	private String displayName;

	/** Class loader of the plugin. */
	private ClassLoader classLoader;

	/**
	 * Create a new Plugin.
	 */
	public Plugin()
	{
		engine = Engine.instance();
		resourceService = engine.getResourceService();
		iObjectFactory = (AbstractIObjectFactory) engine.getIObjectFactory();
		managerRegistry = engine.getManagerRegistry();
		guiPaneRegistry = GUIPaneRegistry.instance();
		commandRegistry = engine.getCommandRegistry();

		actions = new LinkedList();
		managerList = new LinkedList();
		guiPanes = new LinkedList();
		commands = new LinkedList();
	}

	/**
	 * Set the plugin mode (client or server).
	 *
	 * @param mode CLIENT or SERVER.
	 */
	public void setMode(int mode)
	{
		this.mode = mode;
	}

	/**
	 * Get the plugin mode (client or server).
	 *
	 * @return CLIENT or SERVER.
	 */
	public int getMode()
	{
		return mode;
	}

	/**
	 * Set the plugin's class name.
	 *
	 * @param pluginClassName The class name of the plugin.
	 */
	public void setClassName(String pluginClassName)
	{
		this.pluginClassName = pluginClassName;
	}

	/**
	 * Get the plugin's class name.
	 *
	 * @return The class name of the plugin.
	 */
	public String getClassName()
	{
		return pluginClassName;
	}

	/**
	 * Specify the plugin dependencies.
	 *
	 * @param dependency A comma sperated list of other plugins.
	 */
	public void setDependency(String dependency)
	{
		this.dependency = dependency;
	}

	/**
	 * Get the plugin dependencies.
	 *
	 * @return A comma seperated list of other plugins.
	 */
	public String getDependency()
	{
		return dependency;
	}

	/**
	 * Set the plugin name.
	 *
	 * @param name The name of the plugin.
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Get the plugin name.
	 *
	 * @return The name of the plugin.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Set the display name.
	 *
	 * @param displayName The display name of the plugin.
	 */
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}

	/**
	 * Get the display name.
	 *
	 * @return The display name of the plugin.
	 */
	public String getDisplayName()
	{
		return displayName;
	}

	/**
	 * Get the name of the plugin's jar file.
	 *
	 * @return The jar file from which the plugin was loaded.
	 */
	protected String getJarName()
	{
		return getName();
	}

	/**
	 * Initiaize the plugin.
	 *
	 * @param engine The system engine.
	 */
	public void init(Engine engine)
	{
		loadTranslationResources();

		registerActions();

		if ("client".equals(Engine.instance().getName()))
		{
			registerGUIPanes();
		}

		registerCommands();

		if (mode == CLIENT)
		{
			registerClientCommands();
		}
		else if (mode == SERVER)
		{
			registerServerCommands();
		}

		registerManagers();

		if (mode == CLIENT)
		{
			registerClientManagers();
		}
		else if (mode == SERVER)
		{
			registerServerManagers();
		}
	}

	/**
	 *
	 */
	protected void registerServerManagers()
	{
	}

	/**
	 *
	 */
	protected void registerClientManagers()
	{
	}

	/**
	 *
	 */
	protected void registerServerCommands()
	{
	}

	/**
	 *
	 */
	protected void registerClientCommands()
	{
	}

	/**
	 * Unload the plugin from the system.
	 *
	 * @param engine The system engine.
	 */
	public void unloadPlugin(Engine engine)
	{
		unloadTranslationResources();

		for (Iterator i = guiPanes.iterator(); i.hasNext();)
		{
			guiPaneRegistry.remove((GUIPane) i.next());
		}

		for (Iterator i = managerList.iterator(); i.hasNext();)
		{
			Manager manager = (Manager) i.next();

			manager.unload();
			managerRegistry.remove(manager);
		}

		for (Iterator i = actions.iterator(); i.hasNext();)
		{
			iObjectFactory.remove((Action) i.next());
		}

		for (Iterator i = commands.iterator(); i.hasNext();)
		{
			commandRegistry.remove((Command) i.next());
		}
	}

	/**
	 * Register an action.
	 *
	 * @param action The action to register.
	 */
	protected void registerAction(Action action)
	{
		iObjectFactory.register(action);
		actions.add(action);
	}

	/**
	 * Register an action.
	 *
	 * @param mode Specifies wether this is a client or server action.
	 * @param action The action to register.
	 */
	protected void registerAction(int mode, Action action)
	{
		if (this.mode == mode)
		{
			registerAction(action);
		}
	}

	/**
	 * Register a gui pane.
	 *
	 * @param guiPane The gui pane to register.
	 */
	protected void registerGUIPane(GUIPane guiPane)
	{
		guiPaneRegistry.add(guiPane);
		guiPanes.add(guiPane);
	}

	/**
	 * Register a gui pane.
	 *
	 * @param mode Specifies wether this is a client or server gui pane.
	 * @param guiPane The gui pane to register.
	 */
	protected void registerGUIPane(int mode, GUIPane guiPane)
	{
		if (this.mode == mode)
		{
			registerGUIPane(guiPane);
		}
	}

	/**
	 * Register a manager.
	 *
	 * @param manager The manager to register.
	 */
	protected void registerManager(Manager manager)
	{
		Log.logDebug("system", "Plugin.registerManager", "Register manager: " + manager.getTypeId());
		managerRegistry.addManager(manager);
		managerList.add(manager);
		manager.init();
	}

	/**
	 * Register a manager.
	 *
	 * @param mode Specifies wether this is a client or server manager.
	 * @param manager The manager to register.
	 */
	protected void registerManager(int mode, Manager manager)
	{
		if (this.mode == mode)
		{
			registerManager(manager);
		}
	}

	/**
	 * Register a manager.
	 *
	 * @param mode Specifies wether this is a client or server manager.
	 * @param className The manager Classname
	 */
	protected void registerManager(int mode, String className)
	{
		if (this.mode == mode)
		{
			try
			{
				Manager manager = (Manager) classLoader.loadClass(className).newInstance();

				registerManager(manager);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Register a command.
	 *
	 * @param command The command to register.
	 */
	protected void registerCommand(Command command)
	{
		commands.add(command);
		commandRegistry.add(command);
	}

	/**
	 * Register a command.
	 *
	 * @param mode Specifies wether this is a client or server manager.
	 * @param command The command to register.
	 */
	protected void registerCommand(int mode, Command command)
	{
		if (this.mode == mode)
		{
			registerCommand(command);
		}
	}

	/**
	 * Load the localization resources from the plugin's jar file.
	 */
	protected void loadTranslationResources()
	{
		engine.getResourceService().loadTranslationsWithClassLoader(getClass(), "/resources/" + getName());
	}

	/**
	 * Free all localization resources.
	 */
	public void unloadTranslationResources()
	{
		engine.getResourceService().unloadTranslationsWithClassLoader(getClass(), "/resources/" + getName());
	}

	/**
	 * Set the class loader for this plugin
	 *
	 * @param classloader The class loader
	 */
	public void setClassLoader(ClassLoader classLoader)
	{
		this.classLoader = classLoader;
	}

	/**
	 * Register all actions in this method.
	 */
	protected void registerActions()
	{
	}

	/**
	 * Register all gui panes in this method.
	 */
	protected void registerGUIPanes()
	{
	}

	/**
	 * Register all managers in this method.
	 */
	protected void registerManagers()
	{
	}

	/**
	 * Register all commands in this method.
	 */
	protected void registerCommands()
	{
	}
}
