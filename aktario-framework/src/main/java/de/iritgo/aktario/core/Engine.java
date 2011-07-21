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

package de.iritgo.aktario.core;


import de.iritgo.aktario.core.action.ActionProcessorRegistry;
import de.iritgo.aktario.core.base.BaseRegistry;
import de.iritgo.aktario.core.base.SystemProperties;
import de.iritgo.aktario.core.command.CommandProcessorRegistry;
import de.iritgo.aktario.core.command.CommandRegistry;
import de.iritgo.aktario.core.config.Configuration;
import de.iritgo.aktario.core.event.EventRegistry;
import de.iritgo.aktario.core.flowcontrol.FlowControl;
import de.iritgo.aktario.core.gui.IGUIFactory;
import de.iritgo.aktario.core.iobject.IObjectFactory;
import de.iritgo.aktario.core.iobject.IObjectFactoryInterface;
import de.iritgo.aktario.core.iobject.IObjectProxyEventRegistry;
import de.iritgo.aktario.core.iobject.IObjectProxyRegistry;
import de.iritgo.aktario.core.logger.ConsoleLogger;
import de.iritgo.aktario.core.logger.DialogLogger;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.logger.LoggerRegistry;
import de.iritgo.aktario.core.logger.OutputStreamLogger;
import de.iritgo.aktario.core.manager.Manager;
import de.iritgo.aktario.core.manager.ManagerRegistry;
import de.iritgo.aktario.core.plugin.PluginManager;
import de.iritgo.aktario.core.resource.ResourceNode;
import de.iritgo.aktario.core.resource.ResourceService;
import de.iritgo.aktario.core.sessioncontext.SessionContext;
import de.iritgo.aktario.core.thread.ThreadService;
import de.iritgo.aktario.core.uid.DefaultIDGenerator;
import de.iritgo.aktario.core.uid.IDGenerator;
import org.apache.commons.cli.CommandLine;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * This is the engine of the Iritgo client/server-framework.
 *
 * All system initialization is performed in this class and most of the system
 * components can be accessed through the sinlgeton Engine instance.
 */
public class Engine
{
	/** Singleton Engine instance. */
	private static Engine engine;

	/** Engine name. */
	private String name;

	/** System directory. */
	private String systemDir;

	/** File path part separator. */
	private String fileSeparator;

	/** System properties. */
	private SystemProperties systemProperties;

	/** The system configuration. */
	private Configuration configuration;

	/** Unique id generator for persistent objects. */
	private IDGenerator persistentIdGenerator;

	/** Unique id generator for transient objects. */
	private IDGenerator transientIdGenerator;

	/** Resource subsystem. */
	private ResourceService resourceService;

	/** Threading subsystem. */
	private ThreadService threadService;

	/** Iritgo object factory. */
	private IObjectFactoryInterface iObjectFactory;

	/** Base object registry. */
	private BaseRegistry baseRegistry;

	/** Registry of action processors. */
	private ActionProcessorRegistry actionProcessorRegistry;

	/** Plugin manager. */
	private PluginManager pluginManager;

	/** Event registry. */
	private EventRegistry eventRegistry;

	/** Proxy event registry. */
	private IObjectProxyEventRegistry proxyEventRegistry;

	/** Flow controller. */
	private FlowControl flowControl;

	/** Proxy registry. */
	private IObjectProxyRegistry proxyRegistry;

	/** Manager registry. */
	private ManagerRegistry managerRegistry;

	/** Command registry. */
	private CommandRegistry commandRegistry;

	/** Command processor registry. */
	private CommandProcessorRegistry commandProcessorRegistry;

	/** Session context. */
	private SessionContext sessionContext;

	/** The command line options. */
	private CommandLine commandLine;

	/** The factory that creates gui elements. */
	private IGUIFactory guiFactory;

	/** All registered loggers. */
	private LoggerRegistry loggerRegistry;

	/** The user's home directory. */
	private String userHomeDir;

	/**
	 * Since Engine is a singleton, this constructor is private.
	 *
	 * @param commandLine Command line options.
	 */
	private Engine (String name, CommandLine commandLine)
	{
		this.name = name;
		this.commandLine = commandLine;
		userHomeDir = System.getProperty ("user.home");
		systemDir = System.getProperty ("user.dir");
		fileSeparator = System.getProperty ("file.separator");

		if (commandLine.hasOption ("s"))
		{
			File dir = new File (commandLine.getOptionValue ("s").trim ());

			if (dir.isAbsolute ())
			{
				systemDir = dir.getPath ();
			}
			else
			{
				systemDir += fileSeparator + dir.getPath ();
			}
		}
		else if (System.getProperty ("iritgo.system.dir") != null)
		{
			File dir = new File (System.getProperty ("iritgo.system.dir").trim ());

			if (dir.isAbsolute ())
			{
				systemDir = dir.getPath ();
			}
			else
			{
				systemDir += fileSeparator + dir.getPath ();
			}
		}
	}

	/**
	 * Singleton constructor.
	 *
	 * @param commandLine Command line options.
	 */
	public static void create (String name, CommandLine commandLine)
	{
		engine = new Engine (name, commandLine);
		engine.init ();
	}

	/**
	 * Singleton accessor.
	 *
	 * @return The engine instance.
	 */
	static public Engine instance ()
	{
		return engine;
	}

	/**
	 * Initialize the engine.
	 */
	private void init ()
	{
		initLoggingEngine ();

		systemProperties = new SystemProperties ();
		systemProperties.load (userHomeDir + fileSeparator + name + ".properties");

		iObjectFactory = new IObjectFactory ();
		actionProcessorRegistry = new ActionProcessorRegistry ();

		eventRegistry = new EventRegistry ();
		proxyEventRegistry = new IObjectProxyEventRegistry ();
		persistentIdGenerator = new DefaultIDGenerator ();

		flowControl = new FlowControl ();

		proxyRegistry = new IObjectProxyRegistry ();
		baseRegistry = new BaseRegistry ();

		managerRegistry = new ManagerRegistry ();

		sessionContext = new SessionContext ("root");

		commandRegistry = new CommandRegistry ();

		commandProcessorRegistry = new CommandProcessorRegistry ();

		initThreadPooling ();
		initResourceEngine ();
		initPluginManager ();
	}

	/**
	 * Initialize the logging subsystem.
	 */
	private void initLoggingEngine ()
	{
		loggerRegistry = new LoggerRegistry ();

		String loggerId = "Console";

		loggerRegistry.addLogger (new ConsoleLogger ());

		if (commandLine.hasOption ("l"))
		{
			if (commandLine.getOptionValue ("l").equals ("_DIALOG_"))
			{
				loggerRegistry.addLogger (new DialogLogger ());
				loggerId = "Dialog";
			}
			else
			{
				try
				{
					File logFile = new File (commandLine.getOptionValue ("l"));

					logFile.createNewFile ();
					loggerRegistry.addLogger (new OutputStreamLogger (new FileOutputStream (logFile)));
					loggerId = "OutputStream";
				}
				catch (IOException x)
				{
					System.err.println ("Unable to create log file '" + commandLine.getOptionValue ("l") + "': "
									+ x.toString ());
				}
			}
		}
		else if (System.getProperty ("iritgo.log.file") != null)
		{
			try
			{
				File logFile = new File (System.getProperty ("iritgo.log.file"));

				logFile.createNewFile ();
				loggerRegistry.addLogger (new OutputStreamLogger (new FileOutputStream (logFile)));
				loggerId = "OutputStream";
			}
			catch (IOException x)
			{
				System.err.println ("Unable to create log file '" + System.getProperty ("iritgo.log.file") + "': "
								+ x.toString ());
			}
		}
		else if (System.getProperty ("iritgo.log.window") != null)
		{
			loggerRegistry.addLogger (new DialogLogger ());
			loggerId = "Dialog";
		}

		loggerRegistry.addLogger ("system", loggerId);
		loggerRegistry.addLogger ("resource", loggerId);
		loggerRegistry.addLogger ("thread", loggerId);
		loggerRegistry.addLogger ("network", loggerId);
		loggerRegistry.addLogger ("plugin", loggerId);
		loggerRegistry.addLogger ("client", loggerId);
		loggerRegistry.addLogger ("server", loggerId);
		loggerRegistry.addLogger ("persist", loggerId);

		Log.logInfo ("system", "Engine", "Logging subsystem initialized");
	}

	/**
	 * Initialize the resource subsystem.
	 */
	private void initResourceEngine ()
	{
		resourceService = new ResourceService (new ResourceNode ("resource.engine.root", "root"));
		Log.logInfo ("system", "Engine", "Resource subsystem initialized");
	}

	/**
	 * Initialize the threading subsystem.
	 */
	private void initThreadPooling ()
	{
		threadService = new ThreadService (4);
		Log.logInfo ("system", "Engine", "Threading subsystem initialized");
	}

	/**
	 * Initialize the plugin subsystem.
	 */
	private void initPluginManager ()
	{
		pluginManager = new PluginManager (this);
	}

	/**
	 * Store the system properties.
	 */
	public void storeSystemProperties ()
	{
		systemProperties.store (userHomeDir + fileSeparator + name + ".properties");
	}

	/**
	 * Start the engine.
	 */
	public void start ()
	{
	}

	/**
	 * Stop the engine.
	 *
	 * @return True if all resources are successfully released.
	 */
	public boolean stop ()
	{
		storeSystemProperties ();

		boolean res = getThreadService ().stopThreadEngine ();

		loggerRegistry.dispose ();

		return res;
	}

	/**
	 * Retrieve the registry containing the action processors.
	 *
	 * @return The action processor registry.
	 */
	public ActionProcessorRegistry getActionProcessorRegistry ()
	{
		return actionProcessorRegistry;
	}

	/**
	 * Retrieve the iritgo object factory.
	 *
	 * @return The iritgo object factory.
	 */
	public IObjectFactoryInterface getIObjectFactory ()
	{
		return iObjectFactory;
	}

	/**
	 * Set the iritgo object factory.
	 *
	 * @param iObjectFactoryInterface The iritgo object factory.
	 */
	public void setIObjectFactory (IObjectFactoryInterface iObjectFactoryInterface)
	{
		iObjectFactory = iObjectFactoryInterface;
	}

	/**
	 * Retrieve the resource service.
	 *
	 * @return The resource service.
	 */
	public ResourceService getResourceService ()
	{
		return resourceService;
	}

	/**
	 * Retrieve the threading service.
	 *
	 * @return The threading service.
	 */
	public ThreadService getThreadService ()
	{
		return threadService;
	}

	/**
	 * Retrieve the plugin manager.
	 *
	 * @return The plugin manager.
	 */
	public PluginManager getPluginManager ()
	{
		return pluginManager;
	}

	/**
	 * Retrieve the directory from which the Iritgo system was started.
	 *
	 * @return The system directory.
	 */
	public String getSystemDir ()
	{
		return systemDir;
	}

	/**
	 * Retrieve the (os-dependent) string used to separate file path parts.
	 *
	 * @return The file separator.
	 */
	public String getFileSeparator ()
	{
		return fileSeparator;
	}

	/**
	 * Retrieve the system properties.
	 *
	 * @return The system properties.
	 */
	public SystemProperties getSystemProperties ()
	{
		return systemProperties;
	}

	/**
	 * Retrieve the event registry.
	 *
	 * @return The event registry.
	 */
	public EventRegistry getEventRegistry ()
	{
		return eventRegistry;
	}

	/**
	 * Retrieve the proxy event registry.
	 *
	 * @return The proxy event registry.
	 */
	public IObjectProxyEventRegistry getProxyEventRegistry ()
	{
		return proxyEventRegistry;
	}

	/**
	 * Retrieve the unique id generator for persistent objects.
	 *
	 * @return The unique id generator.
	 */
	public IDGenerator getPersistentIDGenerator ()
	{
		return persistentIdGenerator;
	}

	/**
	 * Install a new unique id generator for persistent objects.
	 *
	 * @param idGenerator The id generator.
	 */
	public void installPersistentIDGenerator (IDGenerator idGenerator)
	{
		this.persistentIdGenerator = idGenerator;
	}

	/**
	 * Retrieve the unique id generator for transient objects.
	 *
	 * @return The unique id generator.
	 */
	public IDGenerator getTransientIDGenerator ()
	{
		return transientIdGenerator;
	}

	/**
	 * Install a new unique id generator for transient objects.
	 *
	 * @param idGenerator The unique id generator.
	 */
	public void installTransientIDGenerator (IDGenerator idGenerator)
	{
		this.transientIdGenerator = idGenerator;
	}

	/**
	 * Retrieve the flow controller.
	 *
	 * @return The flow controller.
	 */
	public FlowControl getFlowControl ()
	{
		return flowControl;
	}

	/**
	 * Retrieve the base registry.
	 *
	 * @return The base registry.
	 */
	public BaseRegistry getBaseRegistry ()
	{
		return baseRegistry;
	}

	/**
	 * Retrieve the proxy registry.
	 *
	 * @return The proxy registry.
	 */
	public IObjectProxyRegistry getProxyRegistry ()
	{
		return proxyRegistry;
	}

	/**
	 * Retrieve the manager registry.
	 *
	 * @return The manager registry.
	 */
	public ManagerRegistry getManagerRegistry ()
	{
		return managerRegistry;
	}

	/**
	 * Static Helper method for easier manager access.
	 *
	 * @param id The id of the manager to retrieve.
	 * @return The specified manager or null if none was found.
	 */
	public Manager getManager (String id)
	{
		return instance ().getManagerRegistry ().getManager (id);
	}

	/**
	 * Retrieve the session context.
	 *
	 * @return The session context.
	 */
	public SessionContext getSessionContext ()
	{
		return sessionContext;
	}

	/**
	 * Retrieve the command registry.
	 *
	 * @return The session context.
	 */
	public CommandRegistry getCommandRegistry ()
	{
		return commandRegistry;
	}

	/**
	 * Retrieve the command processor registry.
	 *
	 * @return The command processor registry.
	 */
	public CommandProcessorRegistry getCommandProcessorRegistry ()
	{
		return commandProcessorRegistry;
	}

	/**
	 * Retrieve the gui factory.
	 *
	 * @return The gui factory.
	 */
	public IGUIFactory getGUIFactory ()
	{
		if (guiFactory == null)
		{
			Log.logError ("system", "Engine", "No gui factory set");
		}

		return guiFactory;
	}

	/**
	 * Set the gui factory.
	 *
	 * @param guiFactory The new gui factory.
	 */
	public void setGUIFactory (IGUIFactory guiFactory)
	{
		this.guiFactory = guiFactory;
	}

	/**
	 * Get the system configuration.
	 *
	 * @return The configuration.
	 */
	public Configuration getConfiguration ()
	{
		return configuration;
	}

	/**
	 * Set the system configuration.
	 *
	 * @param configuration The new configuration.
	 */
	public void setConfiguration (Configuration configuration)
	{
		this.configuration = configuration;
	}

	/**
	 * Get the engine name.
	 *
	 * @return The engine name.
	 */
	public String getName ()
	{
		return name;
	}
}
