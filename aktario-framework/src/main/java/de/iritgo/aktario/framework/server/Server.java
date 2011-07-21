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

package de.iritgo.aktario.framework.server;


import bsh.Interpreter;
import bsh.util.JConsole;
import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.action.ActionProcessorRegistry;
import de.iritgo.aktario.core.action.FilterActionProcessor;
import de.iritgo.aktario.core.action.ReceiveEntryNetworkActionProcessor;
import de.iritgo.aktario.core.action.SendEntryNetworkActionProcessor;
import de.iritgo.aktario.core.action.SimpleActionProcessor;
import de.iritgo.aktario.core.action.ThreadNetworkActionProcessor;
import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.config.Configuration;
import de.iritgo.aktario.core.config.SocketConfig;
import de.iritgo.aktario.core.config.ThreadPoolConfig;
import de.iritgo.aktario.core.iobject.IObjectProxy;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.core.network.NetworkService;
import de.iritgo.aktario.core.resource.ResourceService;
import de.iritgo.aktario.core.uid.IDGenerator;
import de.iritgo.aktario.framework.IritgoEngine;
import de.iritgo.aktario.framework.action.ConcurrencyNetworkActionProcessor;
import de.iritgo.aktario.framework.action.ReceiveNetworkActionProcessor;
import de.iritgo.aktario.framework.appcontext.ServerAppContext;
import de.iritgo.aktario.framework.base.InitIritgoException;
import de.iritgo.aktario.framework.base.shutdown.ShutdownManager;
import de.iritgo.aktario.framework.base.shutdown.ShutdownObserver;
import de.iritgo.aktario.framework.console.CommandNotFoundException;
import de.iritgo.aktario.framework.console.ConsoleCommand;
import de.iritgo.aktario.framework.console.ConsoleCommandRegistry;
import de.iritgo.aktario.framework.console.ConsoleManager;
import de.iritgo.aktario.framework.console.UnknownClassException;
import de.iritgo.aktario.framework.console.UnknownConstructorException;
import de.iritgo.aktario.framework.console.UnknownErrorException;
import de.iritgo.aktario.framework.console.WrongParameterException;
import de.iritgo.aktario.framework.dataobject.DataObjectManager;
import de.iritgo.aktario.framework.server.command.ConsoleHelp;
import de.iritgo.aktario.framework.server.command.ReloadPlugins;
import de.iritgo.aktario.framework.server.command.ShowThreads;
import de.iritgo.aktario.framework.server.command.ShowUsers;
import de.iritgo.aktario.framework.server.network.NetworkSystemListenerImpl;
import de.iritgo.aktario.framework.server.network.SendNetworkActionProcessor;
import de.iritgo.aktario.framework.server.network.pingmanager.PingManager;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.UserRegistry;
import de.iritgo.simplelife.math.NumberTools;
import javax.swing.JFrame;
import java.io.BufferedReader;
import java.io.InputStreamReader;


/**
 *
 */
public class Server extends BaseObject implements ShutdownObserver
{
	static private Server server;

	private Engine engine;

	private NetworkService networkService;

	private ClientTransceiver clientTransceiver;

	private ActionProcessorRegistry actionProcessorRegistry;

	private SimpleActionProcessor simpleActionProcessor;

	private ServerAppContext serverAppContext;

	private UserRegistry userRegistry;

	private IDGenerator appIdGenerator;

	private PingManager pingManager;

	private boolean shutdownFlag;

	private boolean isUpAndRunning;

	/**
	 * Standard constructor
	 */
	public Server ()
	{
		shutdownFlag = false;
	}

	/**
	 * Return the Server.
	 *
	 * @return The Server
	 */
	static public Server instance ()
	{
		if (server == null)
		{
			server = new Server ();
		}

		return server;
	}

	/**
	 * Check if the server is up and running.
	 *
	 * @return True if the server is up and running.
	 */
	public boolean isUpAndRunning ()
	{
		return isUpAndRunning;
	}

	/**
	 * Return the ServerEngine.
	 *
	 * @return The ServerEngine
	 */
	public Engine getEngine ()
	{
		return engine;
	}

	/**
	 * The global User Registry.
	 *
	 * @return userregistry
	 */
	public UserRegistry getUserRegistry ()
	{
		return userRegistry;
	}

	/**
	 * The global ID-Generator for application objects
	 *
	 * @return userregistry
	 */
	public IDGenerator getApplicationIdGenerator ()
	{
		return appIdGenerator;
	}

	/**
	 * The PingManager server calc. for every user the pingtime
	 *
	 * @return pingManager
	 */
	public PingManager getPingManager ()
	{
		return pingManager;
	}

	/**
	 * Init all client functions.
	 */
	public void init () throws InitIritgoException
	{
		engine = Engine.instance ();

		serverAppContext = (ServerAppContext) ServerAppContext.serverInstance ();

		initBasics ();
		registerActionProcessors ();
		initApplication ();
		registerConsoleCommands ();

		((ShutdownManager) engine.getManagerRegistry ().getManager ("shutdown")).addObserver (this);

		Log.logInfo ("system", "Server.init", "Server successfully initialized");
	}

	private void initApplication ()
	{
		userRegistry = new UserRegistry ();
		appIdGenerator = Engine.instance ().getPersistentIDGenerator ();
	}

	private void initBasics () throws InitIritgoException
	{
		IObjectProxy.initState = true;
		engine.getManagerRegistry ().addManager (new DataObjectManager ());
	}

	/**
	 * Init the ActionProcessors for the server.
	 */
	private void registerActionProcessors ()
	{
		actionProcessorRegistry = engine.getActionProcessorRegistry ();

		ReceiveEntryNetworkActionProcessor receiveEntryNetworkActionProcessor = new ReceiveEntryNetworkActionProcessor (
						"Server.ReceiveEntryNetworkActionProcessor", null, null);

		actionProcessorRegistry.put (receiveEntryNetworkActionProcessor);

		SendEntryNetworkActionProcessor sendEntryNetworkActionProcessor = new SendEntryNetworkActionProcessor (
						"Server.SendEntryNetworkActionProcessor", null, null);

		actionProcessorRegistry.put (sendEntryNetworkActionProcessor);

		networkService = new NetworkService (engine.getThreadService (), receiveEntryNetworkActionProcessor,
						sendEntryNetworkActionProcessor);
	}

	/**
	 * This method create a default network action processor path.
	 */
	public void createDefaultNetworkProcessingSystem ()
	{
		createReceive ();
		createSend ();
	}

	private void createReceive ()
	{
		ReceiveEntryNetworkActionProcessor receiveEntryNetworkActionProcessor = (ReceiveEntryNetworkActionProcessor) actionProcessorRegistry
						.get ("Server.ReceiveEntryNetworkActionProcessor");

		ReceiveNetworkActionProcessor receiveNetworkActionProcessor = new ReceiveNetworkActionProcessor (null,
						receiveEntryNetworkActionProcessor);

		receiveEntryNetworkActionProcessor.addOutput (receiveNetworkActionProcessor);

		ConcurrencyNetworkActionProcessor concurrencyNetworkActionProcessor = new ConcurrencyNetworkActionProcessor (
						null, receiveNetworkActionProcessor);

		ThreadNetworkActionProcessor threadActionProcessor = new ThreadNetworkActionProcessor (
						"ThreadNetworkActionProcessor", null, concurrencyNetworkActionProcessor);

		SimpleActionProcessor simpleActionProcessor = new SimpleActionProcessor ();

		threadActionProcessor.addOutput (simpleActionProcessor);

		concurrencyNetworkActionProcessor.setThreadNetworkActionProcessor (threadActionProcessor);

		//Reduce the number of threads
		receiveNetworkActionProcessor.addOutput (simpleActionProcessor);

		//		receiveNetworkActionProcessor.addOutput (concurrencyNetworkActionProcessor);
	}

	private void createSend ()
	{
		SendEntryNetworkActionProcessor sendEntryNetworkActionProcessor = (SendEntryNetworkActionProcessor) actionProcessorRegistry
						.get ("Server.SendEntryNetworkActionProcessor");

		FilterActionProcessor filterActionProcessor = new FilterActionProcessor ("Server.FilterActionProcessor", null,
						sendEntryNetworkActionProcessor);

		sendEntryNetworkActionProcessor.addOutput (filterActionProcessor);

		actionProcessorRegistry.put (filterActionProcessor);

		filterActionProcessor.addOutput (new SendNetworkActionProcessor (networkService, null, filterActionProcessor));
	}

	public NetworkService getNetworkService ()
	{
		return networkService;
	}

	private void registerConsoleCommands () throws InitIritgoException
	{
		ConsoleCommandRegistry consoleCommandRegistry = ((ConsoleManager) engine.getManagerRegistry ().getManager (
						"console")).getConsoleCommandRegistry ();

		consoleCommandRegistry.add (new ConsoleCommand ("help", new ConsoleHelp (), "system.help.help"));

		consoleCommandRegistry.add (new ConsoleCommand ("reloadplugins", new ReloadPlugins (),
						"system.help.reloadPlugins", 0));

		consoleCommandRegistry
						.add (new ConsoleCommand ("showthreads", new ShowThreads (), "system.help.showThreads", 0));

		consoleCommandRegistry.add (new ConsoleCommand ("showusers", new ShowUsers (), "system.help.showUsers", 0));
	}

	/**
	 * Init the NetworkSystem from the Server.
	 */
	private void initNetwork ()
	{
		Configuration config = IritgoEngine.instance ().getConfiguration ();
		SocketConfig socketConfig = config.getNetwork ().getSocket ();
		ThreadPoolConfig threadPoolConfig = config.getThreadPool ();

		int port = socketConfig.getPort ();
		int acceptTimeout = socketConfig.getAcceptTimeout ();

		int minThreads = threadPoolConfig.getMinThreads ();

		for (int i = 0; i < minThreads; ++i)
		{
			engine.getThreadService ().addThreadSlot ();
		}

		networkService.listen (serverAppContext.getServerIP (), port, acceptTimeout);

		actionProcessorRegistry = engine.getActionProcessorRegistry ();

		networkService.addNetworkSystemListener (new NetworkSystemListenerImpl ());
	}

	public void start ()
	{
		initNetwork ();

		if (! IritgoEngine.instance ().getCommandLine ().hasOption ("q"))
		{
			System.out.println ("Iritgo Client/Server-Framework. Copyright (C) 2003-2007 BueroByte GbR");
		}

		Runtime.getRuntime ().addShutdownHook (new Thread (new Runnable ()
		{
			public void run ()
			{
				shutdown ();
			}
		}));

		isUpAndRunning = true;

		Thread.currentThread ().setName ("IritgoServer");

		if (IritgoEngine.instance ().getCommandLine ().hasOption ("c"))
		{
			commandLoop ();
		}
		else
		{
			try
			{
				synchronized (this)
				{
					wait ();
				}
			}
			catch (InterruptedException x)
			{
			}
		}

		networkService.closeAllChannels ();
		networkService.dispose ();
	}

	public void stop ()
	{
		synchronized (this)
		{
			notifyAll ();
		}
	}

	/**
	 * Runs the ServerConsole you can send some commands
	 */
	private void commandLoop ()
	{
		ResourceService resourceService = engine.getResourceService ();
		BufferedReader in = new BufferedReader (new InputStreamReader (System.in));

		try
		{
			while (true)
			{
				System.out.print ("> ");
				System.out.flush ();

				String line = in.readLine ();

				if (line.equals ("quit"))
				{
					break;
				}

				if (line.equals (""))
				{
					continue;
				}

				if (line.equals ("beanshell"))
				{
					JFrame frame = new JFrame ("Console");

					JConsole console = new JConsole ();

					frame.setContentPane (console);
					frame.setVisible (true);

					Interpreter i = new Interpreter (console);

					new Thread (i).start ();

					continue;
				}

				ConsoleManager consoleManager = (ConsoleManager) Engine.instance ().getManagerRegistry ().getManager (
								"console");

				try
				{
					consoleManager.doConsoleCommand (line);
				}
				catch (CommandNotFoundException x)
				{
					System.out.println (resourceService.getStringWithoutException ("system.unknownCommand"));
				}
				catch (WrongParameterException x)
				{
					System.out.println ("Wrong parameter(s) for the command.\n");
				}
				catch (UnknownClassException x)
				{
					System.out.println ("Unknown class for this command. (Plugin failure?)\n");
				}
				catch (UnknownConstructorException x)
				{
					System.out.println ("Unknown constructor for this command.\n");
				}
				catch (UnknownErrorException x)
				{
					System.out.println ("Unknown error for this command.\n");
				}
			}
		}
		catch (Exception x)
		{
		}
	}

	public void shutdown ()
	{
		shutdownFlag = true;
		Log.logInfo ("system", "Server", "Shutting down the server");

		if (IritgoEngine.instance () != null)
		{
			if (! IritgoEngine.instance ().shutdown ())
			{
				Log.logError ("system", "Server", "Unable to shutdown gracefully. Committing suicide...");
				System.exit (0);
			}
		}
	}

	public void onUserLogoff (User user)
	{
		if (IritgoEngine.instance ().getCommandLine ().hasOption ("e"))
		{
			shutdown ();
		}
	}

	public void onShutdown ()
	{
	}
}
