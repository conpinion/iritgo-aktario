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

package de.iritgo.aktario.framework;


import de.iritgo.aktario.agent.AgentManager;
import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.command.SimpleCommandProcessor;
import de.iritgo.aktario.core.config.Configuration;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.plugin.PluginManager;
import de.iritgo.aktario.core.resource.ResourceService;
import de.iritgo.aktario.core.thread.ThreadService;
import de.iritgo.aktario.core.uid.PropertiesIDGenerator;
import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.aktario.framework.appcontext.ServerAppContext;
import de.iritgo.aktario.framework.base.AsyncCommandProcessor;
import de.iritgo.aktario.framework.base.IObjectChangedNotifier;
import de.iritgo.aktario.framework.base.InitIritgoException;
import de.iritgo.aktario.framework.base.action.AliveCheckAction;
import de.iritgo.aktario.framework.base.action.AliveCheckServerAction;
import de.iritgo.aktario.framework.base.action.CommandAction;
import de.iritgo.aktario.framework.base.action.CommandServerAction;
import de.iritgo.aktario.framework.base.action.EditIObjectAction;
import de.iritgo.aktario.framework.base.action.EditIObjectServerAction;
import de.iritgo.aktario.framework.base.action.PingAction;
import de.iritgo.aktario.framework.base.action.PingServerAction;
import de.iritgo.aktario.framework.base.action.PropertiesRequest;
import de.iritgo.aktario.framework.base.action.PropertiesResponse;
import de.iritgo.aktario.framework.base.action.ProxyAction;
import de.iritgo.aktario.framework.base.action.ProxyLinkedListAddAction;
import de.iritgo.aktario.framework.base.action.ProxyLinkedListAddServerAction;
import de.iritgo.aktario.framework.base.action.ProxyLinkedListRemoveAction;
import de.iritgo.aktario.framework.base.action.ProxyLinkedListRemoveServerAction;
import de.iritgo.aktario.framework.base.action.ProxyServerAction;
import de.iritgo.aktario.framework.base.action.TurnAction;
import de.iritgo.aktario.framework.base.action.WrongVersionAction;
import de.iritgo.aktario.framework.base.command.SetLogLevel;
import de.iritgo.aktario.framework.base.shutdown.ShutdownManager;
import de.iritgo.aktario.framework.client.Client;
import de.iritgo.aktario.framework.console.ConsoleCommand;
import de.iritgo.aktario.framework.console.ConsoleCommandRegistry;
import de.iritgo.aktario.framework.console.ConsoleManager;
import de.iritgo.aktario.framework.dataobject.AddDataObjectRequest;
import de.iritgo.aktario.framework.dataobject.AddDataObjectResponse;
import de.iritgo.aktario.framework.dataobject.AnnounceDynDataObjectRequest;
import de.iritgo.aktario.framework.dataobject.AnnounceDynDataObjectResponse;
import de.iritgo.aktario.framework.dataobject.DynIObjectFactory;
import de.iritgo.aktario.framework.dataobject.QueryRequest;
import de.iritgo.aktario.framework.dataobject.QueryResponse;
import de.iritgo.aktario.framework.dataobject.SimpleQuery;
import de.iritgo.aktario.framework.dataobject.gui.CommandDescription;
import de.iritgo.aktario.framework.dataobject.gui.Controller;
import de.iritgo.aktario.framework.dataobject.gui.GUIControllerRequest;
import de.iritgo.aktario.framework.dataobject.gui.GUIControllerResponse;
import de.iritgo.aktario.framework.dataobject.gui.GUIManager;
import de.iritgo.aktario.framework.dataobject.gui.WidgetDescription;
import de.iritgo.aktario.framework.server.Server;
import de.iritgo.aktario.framework.splash.Splash;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.action.RegisterNewUserAction;
import de.iritgo.aktario.framework.user.action.RegisterNewUserFailureAction;
import de.iritgo.aktario.framework.user.action.RegisterNewUserServerAction;
import de.iritgo.aktario.framework.user.action.UserKickAction;
import de.iritgo.aktario.framework.user.action.UserLoginAction;
import de.iritgo.aktario.framework.user.action.UserLoginFailureAction;
import de.iritgo.aktario.framework.user.action.UserLoginServerAction;
import de.iritgo.aktario.framework.user.action.UserLogoffAction;
import de.iritgo.aktario.framework.user.action.UserLogoffServerAction;
import de.iritgo.simplelife.math.NumberTools;
import de.iritgo.simplelife.string.StringTools;
import net.schst.XJConf.DefinitionParser;
import net.schst.XJConf.NamespaceDefinitions;
import net.schst.XJConf.XmlReader;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * This is the main startup class of the Iritgo system.
 * The IritgoClient or IritgoServer classes create
 * a single instance of the IritgoEngine class which initializes the
 * whole system.
 */
public class IritgoEngine
{
	/** Singleton framework instance. */
	private static IritgoEngine framework;

	/** Start the engine as a server instance. */
	public static final int START_SERVER = 1;

	/** Start the engine as a client instance. */
	public static final int START_CLIENT = 2;

	/** The system engine. */
	private Engine engine;

	/** The system resources. */
	private ResourceService resources;

	/** The standard asynchronous command processor. */
	private AsyncCommandProcessor asyncCommandProcessor;

	/** The standard simple command processor. */
	private SimpleCommandProcessor simpleCommandProcessor;

	/** If the framework is started in server mode, this is the server. */
	private Server server;

	/** If the framework is started in client mode, this is the client. */
	private Client client;

	/** The engine mode (START_SERVER or START_CLIENT). */
	private int mode;

	/** The name of this engine. */
	private String engineName;

	/** The command line options. */
	private CommandLine commandLine;

	/** The iobject change notifier. */
	private IObjectChangedNotifier iObjectChangeNotifier;

	/** The iritgo splash screen. */
	private Splash splash;

	/**
	 * Since IritgoEngine is a singleton, this constructor is private.
	 *
	 * @param mode The framework startup mode (START_SERVER or START_CLIENT).
	 */
	private IritgoEngine (int mode)
	{
		this.mode = mode;
		engineName = "iritgo";

		if (mode == IritgoEngine.START_SERVER)
		{
			engineName = "server";
		}
		else if (mode == IritgoEngine.START_CLIENT)
		{
			engineName = "client";
		}
	}

	/**
	 * Singleton constructor.
	 *
	 * @param mode The framework startup mode (START_SERVER or START_CLIENT).
	 */
	public static void create (int mode, Options options, String[] args)
	{
		framework = new IritgoEngine (mode);

		System.getProperties ().put ("iritgo.engine", framework);

		CommandLine commandLine = framework.processOptions (options, args);

		if (commandLine != null)
		{
			framework.init (commandLine);
		}
	}

	/**
	 * Singleton accessor.
	 *
	 * @return The framework instance.
	 */
	static public IritgoEngine instance ()
	{
		return framework;
	}

	/**
	 * Process the command line options.
	 *
	 * @param options The option description.
	 * @param args The actual program arguments.
	 * @return The parsed command line or null if we should better not start
	 *   the engine.
	 */
	private CommandLine processOptions (Options options, String[] args)
	{
		options.addOption ("h", "help", false, "Print this message");
		options.addOption ("d", "debug", true, "Set the initial debugging level");
		options.addOption ("x", "xmlrpcport", true, "Set the xml-rpc port");
		options.addOption ("c", "server-cli", false, "Start the interactive server shell");
		options.addOption ("l", "log-file", true, "Print logging messages to the specified file");
		options.addOption ("q", "quiet", false, "Ommit informational startup messages");
		options.addOption ("w", "webstartlogin", true, "The WebStart login name eg. user@server.net");
		options
						.addOption ("n", "no-version-check", false,
										"Don't check the application and framework version at logon");
		options.addOption ("s", "system-dir", true, "Set the directory containing the system files");
		options
						.addOption ("e", "embedded", false,
										"Activate the embedded mode (one single server and client instance)");
		options.addOption ("i", "login-info", true, "Login information (user@host)");

		CommandLine line = null;

		try
		{
			line = new PosixParser ().parse (options, args);

			if (line.hasOption ("h"))
			{
				printHelp (options);

				return null;
			}
		}
		catch (ParseException exp)
		{
			printHelp (options);

			return null;
		}

		return line;
	}

	/**
	 * Print the command line options help.
	 *
	 * @param options The command line options.
	 */
	private void printHelp (Options options)
	{
		HelpFormatter formatter = new HelpFormatter ();

		formatter.printHelp (80, engineName, "Iritgo Client/Server-Framework Copyright (C) 2003-2007 BueroByte GbR",
						options, "");
	}

	/**
	 * Initialize the framework.
	 *
	 * @param options The command line options.
	 * @throws InitIritgoException If an error occurred during initialization.
	 */
	private void init (CommandLine options)
	{
		try
		{
			commandLine = options;

			File sysDir = new File (".");

			if (commandLine.hasOption ("s"))
			{
				sysDir = new File (commandLine.getOptionValue ("s").trim ());
			}

			try
			{
				Properties sysProperties = new Properties ();

				sysProperties.load (new FileInputStream (new File (sysDir, "sys.properties")));

				for (Object key : sysProperties.keySet ())
				{
					System.setProperty (key.toString (), sysProperties.get (key).toString ());
				}
			}
			catch (FileNotFoundException x1)
			{
			}
			catch (IOException x1)
			{
			}

			AppContext appContext = AppContext.instance ();
			ServerAppContext serverAppContext = ServerAppContext.serverInstance ();

			if (options.hasOption ("d"))
			{
				Log.setLevel (NumberTools.toInt (options.getOptionValue ("d"), Log.ERROR));
			}
			else if (System.getProperty ("iritgo.debug.level") != null)
			{
				Log.setLevel (NumberTools.toInt (System.getProperty ("iritgo.debug.level"), Log.ERROR));
			}
			else
			{
				Log.setLevel (Log.ERROR);
			}

			initEngine ();

			if (mode == IritgoEngine.START_CLIENT)
			{
				try
				{
					//TODO: xml rpc port Hack!
					String xmlRPCPort = commandLine.getOptionValue ("x");

					if (! StringTools.isEmpty (xmlRPCPort))
					{
						System.out.println ("****************** xmlRPCPort:" + xmlRPCPort);
						appContext.put ("xmlrpcport", xmlRPCPort);
					}

					splash = (Splash) Class.forName ("de.iritgo.aktario.core.splash.CustomSplash").newInstance ();
				}
				catch (Exception x)
				{
					splash = new Splash ();
				}
			}

			if (splash != null)
			{
				splash.setText ("Initializing: Engine");
			}

			initIdGenerator ();
			registerDefaultActions ();
			registerDefaultManager ();
			registerConsoleCommands ();
			initCommandProcessor ();
			initConfiguration ();

			if (mode == IritgoEngine.START_SERVER)
			{
				server = Server.instance ();
				server.init ();
				serverAppContext.setServer (true);
				serverAppContext.setClient (false);
				appContext.setServer (true);
				appContext.setClient (false);
			}

			if (mode == IritgoEngine.START_CLIENT)
			{
				checkLoginOptions (commandLine);
				client = Client.instance ();
				client.init ();
				serverAppContext.setServer (false);
				serverAppContext.setClient (true);
				appContext.setServer (false);
				appContext.setClient (true);
			}

			initPlugins ();
			AgentManager.createAgentManager (mode);

			registerDefaultDataObjects ();

			if (mode == IritgoEngine.START_SERVER)
			{
				server = Server.instance ();
				server.start ();
			}

			if (mode == IritgoEngine.START_CLIENT)
			{
				splash.setText ("Initializing: Client");
				client = Client.instance ();
				client.initGUI ();
				client.startGUI ();
				client.startApplication ();
				splash.startCoolDown ();
			}
		}
		catch (InitIritgoException x)
		{
			if (engine != null)
			{
				engine.stop ();
			}
		}
	}

	private void checkLoginOptions (CommandLine options)
	{
		if (options.hasOption ("w"))
		{
			String login = options.getOptionValue ("w").trim ();

			Engine.instance ().getSystemProperties ().setProperty ("lastlogin", login);
		}
	}

	/**
	 * Initialize the system engine.
	 *
	 * @throws InitIritgoException If an error occurred during initialization.
	 */
	private void initEngine () throws InitIritgoException
	{
		Engine.create (engineName, commandLine);
		engine = Engine.instance ();

		resources = engine.getResourceService ();

		resources.loadResources (IritgoEngine.class.getResource ("/resources/framework.xml"));

		resources.loadTranslationsWithClassLoader (IritgoEngine.class, "/resources/system");

		Engine.instance ().setIObjectFactory (new DynIObjectFactory ());

		engine.start ();

		iObjectChangeNotifier = new IObjectChangedNotifier ();
	}

	/**
	 * Initialize the unique id generator.
	 *
	 * @throws InitIritgoException If an error occurred during initialization.
	 */
	private void initIdGenerator ()
	{
		PropertiesIDGenerator idGenerator = new PropertiesIDGenerator (Engine.instance ().getSystemProperties (),
						"system.lastUniqueId", 1, 1);

		idGenerator.load ();

		Engine.instance ().installPersistentIDGenerator (idGenerator);
		Engine.instance ().installTransientIDGenerator (idGenerator);
	}

	/**
	 * Initialize the command processors.
	 */
	private void initCommandProcessor ()
	{
		ThreadService threadService = engine.getThreadService ();

		asyncCommandProcessor = new AsyncCommandProcessor ();
		threadService.addThreadSlot ();
		threadService.add (asyncCommandProcessor);
		engine.getCommandProcessorRegistry ().add (asyncCommandProcessor);

		simpleCommandProcessor = new SimpleCommandProcessor ();
		engine.getCommandProcessorRegistry ().add (simpleCommandProcessor);
	}

	/**
	 * Initialize the framework configuration.
	 *
	 * @throws InitIritgoException If an error occurred during initialization.
	 */
	private void initConfiguration () throws InitIritgoException
	{
		String configName = "iritgo";

		if (mode == IritgoEngine.START_SERVER)
		{
			configName = "server-config";
		}
		else if (mode == IritgoEngine.START_CLIENT)
		{
			configName = "client-config";
		}

		Configuration config = new Configuration ();

		Log.logInfo ("system", "Framework.initConfiguration", "Try to load config file '" + engine.getSystemDir ()
						+ engine.getFileSeparator () + configName + ".xml'");

		try
		{
			InputStream configInput = null;
			File configFile = new File (engine.getSystemDir () + engine.getFileSeparator () + configName + ".xml");

			if (configFile.exists ())
			{
				configInput = new FileInputStream (configFile);
			}
			else
			{
				configInput = IritgoEngine.class.getResourceAsStream ("/" + configName + ".xml");
			}

			DefinitionParser defParser = new DefinitionParser ();
			NamespaceDefinitions defs = defParser.parse (IritgoEngine.class
							.getResourceAsStream ("/configuration-tags.xml"));
			XmlReader confReader = new XmlReader ();

			confReader.setTagDefinitions (defs);
			confReader.parse (configInput);
			config = (Configuration) confReader.getConfigValue ("iritgo");
		}
		catch (Exception x)
		{
			Log.logFatal ("system", "Framework.initConfiguration",
							"Configuration error while loading the configuration file '" + configName + ".xml':" + x);
			throw new InitIritgoException (x);
		}

		engine.setConfiguration (config);
	}

	/**
	 * Create an register all system actions.
	 *
	 * @throws InitIritgoException If an error occurred during initialization.
	 */
	private void registerDefaultActions () throws InitIritgoException
	{
		engine.getIObjectFactory ().register (new EditIObjectAction ());
		engine.getIObjectFactory ().register (new EditIObjectServerAction ());

		engine.getIObjectFactory ().register (new WrongVersionAction ());

		engine.getIObjectFactory ().register (new UserLoginAction ());
		engine.getIObjectFactory ().register (new UserLoginFailureAction ());
		engine.getIObjectFactory ().register (new UserLogoffAction ());
		engine.getIObjectFactory ().register (new UserLoginServerAction ());
		engine.getIObjectFactory ().register (new UserLogoffServerAction ());
		engine.getIObjectFactory ().register (new UserKickAction ());

		engine.getIObjectFactory ().register (new RegisterNewUserAction ());
		engine.getIObjectFactory ().register (new RegisterNewUserServerAction ());
		engine.getIObjectFactory ().register (new RegisterNewUserFailureAction ());
		engine.getIObjectFactory ().register (new RegisterNewUserServerAction ());

		engine.getIObjectFactory ().register (new ProxyAction ());
		engine.getIObjectFactory ().register (new ProxyServerAction ());

		engine.getIObjectFactory ().register (new CommandAction ());
		engine.getIObjectFactory ().register (new CommandServerAction ());

		engine.getIObjectFactory ().register (new AliveCheckAction ());
		engine.getIObjectFactory ().register (new AliveCheckServerAction ());

		engine.getIObjectFactory ().register (new ProxyLinkedListAddAction ());
		engine.getIObjectFactory ().register (new ProxyLinkedListAddServerAction ());
		engine.getIObjectFactory ().register (new ProxyLinkedListRemoveAction ());
		engine.getIObjectFactory ().register (new ProxyLinkedListRemoveServerAction ());

		engine.getIObjectFactory ().register (new PingAction ());
		engine.getIObjectFactory ().register (new PingServerAction ());
		engine.getIObjectFactory ().register (new TurnAction ());

		engine.getIObjectFactory ().register (new AnnounceDynDataObjectResponse ());
		engine.getIObjectFactory ().register (new AnnounceDynDataObjectRequest ());
		engine.getIObjectFactory ().register (new AddDataObjectResponse ());
		engine.getIObjectFactory ().register (new AddDataObjectRequest ());

		engine.getIObjectFactory ().register (new QueryRequest ());
		engine.getIObjectFactory ().register (new QueryResponse ());

		engine.getIObjectFactory ().register (new GUIControllerRequest ());
		engine.getIObjectFactory ().register (new GUIControllerResponse ());

		engine.getIObjectFactory ().register (new PropertiesRequest ());
		engine.getIObjectFactory ().register (new PropertiesResponse ());
	}

	/**
	 * Create an register all default data objects.
	 *
	 * @throws InitIritgoException If an error occurred during initialization.
	 */
	private void registerDefaultDataObjects () throws InitIritgoException
	{
		engine.getIObjectFactory ().register (new SimpleQuery ());
		engine.getIObjectFactory ().register (new User ());
		engine.getIObjectFactory ().register (new Controller ());
		engine.getIObjectFactory ().register (new WidgetDescription ());
		engine.getIObjectFactory ().register (new CommandDescription ());
	}

	/**
	 * Create and register the system managers.
	 *
	 * @throws InitIritgoException If an error occurred during initialization.
	 */
	private void registerDefaultManager () throws InitIritgoException
	{
		engine.getManagerRegistry ().addManager (new ConsoleManager ());
		engine.getManagerRegistry ().addManager (new ShutdownManager ());
		engine.getManagerRegistry ().addManager (new GUIManager ());
	}

	/**
	 * Create and register the system console commands.
	 *
	 * @throws InitIritgoException If an error occurred during initialization.
	 */
	private void registerConsoleCommands () throws InitIritgoException
	{
		ConsoleCommandRegistry consoleCommandRegistry = ((ConsoleManager) engine.getManagerRegistry ().getManager (
						"console")).getConsoleCommandRegistry ();

		consoleCommandRegistry.add (new ConsoleCommand ("loglevel", new SetLogLevel (), "system.help.logLevel", 1));
	}

	/**
	 * Initialize the plugins.
	 */
	public void initPlugins ()
	{
		PluginManager pluginManager = engine.getPluginManager ();

		pluginManager.loadPlugins ();

		pluginManager.initPlugins (splash);
	}

	/**
	 * Retrieve the standard async command processor.
	 *
	 * @return The async command processor.
	 */
	public AsyncCommandProcessor getAsyncCommandProcessor ()
	{
		return asyncCommandProcessor;
	}

	/**
	 * Retrieve the standard simple command processor.
	 *
	 * @return The simple command processor.
	 */
	public SimpleCommandProcessor getSimpleCommandProcessor ()
	{
		return simpleCommandProcessor;
	}

	/**
	 * Return the iobject change notifier.
	 *
	 * @return IObjectChangedNotifier
	 */
	public IObjectChangedNotifier getIObjectChangeNotifier ()
	{
		return iObjectChangeNotifier;
	}

	/**
	 * Shutdown the framework.
	 *
	 * @return True if all resources are successfully released.
	 */
	public boolean shutdown ()
	{
		((ShutdownManager) engine.getManagerRegistry ().getManager ("shutdown")).shutdown ();

		if (server != null)
		{
			server.stop ();
		}

		if (client != null)
		{
			client.stop ();
		}

		Engine.instance ().getPersistentIDGenerator ().save ();
		Engine.instance ().getTransientIDGenerator ().save ();

		PluginManager pluginManager = engine.getPluginManager ();

		pluginManager.unloadPlugins ();

		engine.stop ();

		System.out.println ("[Iritgo] Shutdown complete");

		if (client != null)
		{
			System.exit (0);
		}

		return true;
	}

	/**
	 * Get the command line parameters.
	 *
	 * @return The command line paramters.
	 */
	public CommandLine getCommandLine ()
	{
		return commandLine;
	}

	/**
	 * Get the system configuration.
	 *
	 * @return The configuration.
	 */
	public Configuration getConfiguration ()
	{
		return engine.getConfiguration ();
	}

	public boolean isServer ()
	{
		return server != null ? true : false;
	}
}
