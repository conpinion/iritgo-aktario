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

package de.iritgo.aktario.framework.client;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.action.ActionProcessorRegistry;
import de.iritgo.aktario.core.action.ReceiveEntryNetworkActionProcessor;
import de.iritgo.aktario.core.action.SendEntryNetworkActionProcessor;
import de.iritgo.aktario.core.action.SimpleActionProcessor;
import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.config.Configuration;
import de.iritgo.aktario.core.config.ThreadPoolConfig;
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.core.network.NetworkService;
import de.iritgo.aktario.framework.IritgoEngine;
import de.iritgo.aktario.framework.action.ReceiveNetworkActionProcessor;
import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.aktario.framework.base.InitIritgoException;
import de.iritgo.aktario.framework.base.NetworkProxyLinkedListManager;
import de.iritgo.aktario.framework.base.NetworkProxyManager;
import de.iritgo.aktario.framework.base.command.PingPong;
import de.iritgo.aktario.framework.base.command.SetLogLevel;
import de.iritgo.aktario.framework.client.command.ClientReloadPlugins;
import de.iritgo.aktario.framework.client.gui.ClientGUI;
import de.iritgo.aktario.framework.client.network.NetworkSystemListenerImpl;
import de.iritgo.aktario.framework.client.network.SendNetworkActionProcessor;
import de.iritgo.aktario.framework.client.network.SimpleSyncNetworkActionProcessor;
import de.iritgo.aktario.framework.console.ConsoleCommand;
import de.iritgo.aktario.framework.console.ConsoleCommandRegistry;
import de.iritgo.aktario.framework.console.ConsoleManager;
import de.iritgo.aktario.framework.dataobject.gui.GUIExtensionManager;
import de.iritgo.aktario.framework.manager.ClientManager;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.UserRegistry;


/**
 *
 */
public class Client extends BaseObject
{
	static private Client client;

	private Engine engine;

	private NetworkService networkService;

	private ClientGUI clientGUI;

	@SuppressWarnings("unused")
	private ClientTransceiver clientTransceiver;

	private ActionProcessorRegistry actionProcessorRegistry;

	@SuppressWarnings("unused")
	private SimpleActionProcessor simpleActionProcessor;

	private AppContext appContext;

	private UserRegistry userRegistry;

	@SuppressWarnings("unused")
	private NetworkProxyManager networkProxyManager;

	@SuppressWarnings("unused")
	private NetworkProxyLinkedListManager networkProxyLinkedListManager;

	private GUIExtensionManager guiExtensionManager;

	/**
	 * Standard constructor
	 */
	public Client ()
	{
	}

	/**
	 * Init all client functions.
	 */
	public void init () throws InitIritgoException
	{
		engine = Engine.instance ();

		appContext = AppContext.instance ();

		loadUser ();
		registerActionProcessors ();
		initBasics ();
		initResources ();
		registerConsoleCommands ();
	}

	/**
	 * Return the Client, it must init before.
	 *
	 * @return The client.
	 */
	static public Client instance ()
	{
		if (client == null)
		{
			client = new Client ();
		}

		return client;
	}

	/**
	 * Return the ClientEngine, it must init before.
	 *
	 * @return The clientEngine.
	 */
	public Engine getEngine ()
	{
		return engine;
	}

	public UserRegistry getUserRegistry ()
	{
		return userRegistry;
	}

	public ClientGUI getClientGUI ()
	{
		return clientGUI;
	}

	public GUIExtensionManager getGUIExtensionManager ()
	{
		return guiExtensionManager;
	}

	public NetworkService getNetworkService ()
	{
		return networkService;
	}

	/**
	 * Load the Userdata or set the user of default (-1).
	 */
	private void loadUser ()
	{
		userRegistry = new UserRegistry ();

		User user = new User ();

		user.setUniqueId (- 1);
		appContext.setUser (user);
	}

	/**
	 * Init all actionProcessors, it used for the Network and local actions.
	 */
	private void registerActionProcessors ()
	{
		actionProcessorRegistry = engine.getActionProcessorRegistry ();

		ReceiveEntryNetworkActionProcessor receiveEntryNetworkActionProcessor = new ReceiveEntryNetworkActionProcessor (
						"Client.ReceiveEntryNetworkActionProcessor", null, null);

		actionProcessorRegistry.put (receiveEntryNetworkActionProcessor);

		SendEntryNetworkActionProcessor sendEntryNetworkActionProcessor = new SendEntryNetworkActionProcessor (
						"Client.SendEntryNetworkActionProcessor", null, null);

		actionProcessorRegistry.put (sendEntryNetworkActionProcessor);

		networkService = new NetworkService (engine.getThreadService (), receiveEntryNetworkActionProcessor,
						sendEntryNetworkActionProcessor);

		networkService.addNetworkSystemListener (new NetworkSystemListenerImpl ());
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
						.get ("Client.ReceiveEntryNetworkActionProcessor");

		ReceiveNetworkActionProcessor receiveNetworkActionProcessor = new ReceiveNetworkActionProcessor (null,
						receiveEntryNetworkActionProcessor);

		receiveEntryNetworkActionProcessor.addOutput (receiveNetworkActionProcessor);

		SimpleSyncNetworkActionProcessor simpleSyncNetworkActionProcessor = new SimpleSyncNetworkActionProcessor (null,
						receiveNetworkActionProcessor);

		receiveNetworkActionProcessor.addOutput (simpleSyncNetworkActionProcessor);

		SimpleActionProcessor simpleActionProcessor = new SimpleActionProcessor ();

		simpleSyncNetworkActionProcessor.addOutput (simpleActionProcessor);
	}

	private void createSend ()
	{
		SendEntryNetworkActionProcessor sendEntryNetworkActionProcessor = (SendEntryNetworkActionProcessor) actionProcessorRegistry
						.get ("Client.SendEntryNetworkActionProcessor");

		sendEntryNetworkActionProcessor.addOutput (new SendNetworkActionProcessor (networkService, null,
						sendEntryNetworkActionProcessor));
	}

	private void registerConsoleCommands () throws InitIritgoException
	{
		ConsoleCommandRegistry consoleCommandRegistry = ((ConsoleManager) engine.getManagerRegistry ().getManager (
						"console")).getConsoleCommandRegistry ();

		consoleCommandRegistry.add (new ConsoleCommand ("reloadplugins", new ClientReloadPlugins (),
						"system.help.reloadplugin", 0));
		consoleCommandRegistry.add (new ConsoleCommand ("loglevel", new SetLogLevel (), "system.help.loglevel", 1));
		consoleCommandRegistry.add (new ConsoleCommand ("pingpong", new PingPong (), "system.help.pingpong", 0));
	}

	public void initGUI () throws InitIritgoException
	{
		ClientManager clientManager = (ClientManager) engine.getManagerRegistry ().getManager ("client");

		if (clientManager == null)
		{
			return;
		}

		clientGUI = clientManager.getClientGUI ();
		clientGUI.init ();
	}

	public void stopGUI () throws InitIritgoException
	{
		clientGUI.stopGUI ();
		clientGUI = null;
	}

	private void initBasics () throws InitIritgoException
	{
		Configuration config = IritgoEngine.instance ().getConfiguration ();
		ThreadPoolConfig threadPoolConfig = config.getThreadPool ();

		int minThreads = threadPoolConfig.getMinThreads ();

		for (int i = 0; i < minThreads; ++i)
		{
			engine.getThreadService ().addThreadSlot ();
		}

		networkProxyManager = new NetworkProxyManager ();
		networkProxyLinkedListManager = new NetworkProxyLinkedListManager ();
		guiExtensionManager = new GUIExtensionManager ();
		Engine.instance ().getManagerRegistry ().addManager (guiExtensionManager);
	}

	private void initResources () throws InitIritgoException
	{
		engine.getResourceService ().loadTranslationsWithClassLoader (IritgoEngine.class, "/resources/system");
	}

	public void startGUI () throws InitIritgoException
	{
		if (clientGUI != null)
		{
			clientGUI.startGUI ();
		}
	}

	public void startApplication () throws InitIritgoException
	{
		if (clientGUI != null)
		{
			clientGUI.startApplication ();
		}
	}

	public void lostNetworkConnection ()
	{
		if (clientGUI != null)
		{
			clientGUI.lostNetworkConnection ();
		}

		// Client clean all cached IObjects, proxy and proxyEvents.
		Engine.instance ().getProxyEventRegistry ().clear ();
		Engine.instance ().getProxyRegistry ().clear ();
		Engine.instance ().getBaseRegistry ().clear ();
		Client.instance ().getUserRegistry ().clear ();
	}

	public void stop ()
	{
		if (clientGUI != null)
		{
			clientGUI.stopGUI ();
			clientGUI.stopApplication ();
		}

		// Important for the lostNetworkConnection() method!
		clientGUI = null;

		networkService.closeChannel (appContext.getUser ().getNetworkChannel ());
	}
}
