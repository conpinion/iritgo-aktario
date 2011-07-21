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

package de.iritgo.aktario.agent;


import de.iritgo.aktario.agent.container.AgentContainer;
import de.iritgo.aktario.agent.transfer.AgentTransferAction;
import de.iritgo.aktario.agent.transfer.Dispatcher;
import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.iobject.AbstractIObjectFactory;
import de.iritgo.aktario.framework.IritgoEngine;
import de.iritgo.aktario.framework.base.DataObject;
import de.iritgo.aktario.framework.base.shutdown.ShutdownManager;
import de.iritgo.aktario.framework.base.shutdown.ShutdownObserver;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.UserEvent;
import de.iritgo.aktario.framework.user.UserListener;


/**
 * This is the main startup class of the Iritgo agent system.
 * The IritgoClient or IritgoServer classes create a single instance
 * of the Iritgo agent system class which initializes the whole system.
 * The agent manager adds dispatchers and holds the agent container.
 */
public class AgentManager implements UserListener, ShutdownObserver
{
	/** Singleton manager instance. */
	private static AgentManager agentManager;

	/** Server operation mode. */
	public static int AGENT_SERVER = 1;

	/** Client operation mode. */
	public static int AGENTE_CLIENT = 2;

	/** The manager mode (AGENT_SERVER or AGENT_CLIENT). */
	@SuppressWarnings("unused")
	private int mode;

	/** The agent container. */
	private AgentContainer container;

	/**
	 * Since the manager is a singleton, this constructor is private.
	 *
	 * @param mode The manager startup mode (SPHERE_SERVER or SPHERE_CLIENT).
	 */
	private AgentManager (int mode)
	{
		this.mode = mode;
		container = new AgentContainer (this);
		init (mode);
	}

	/**
	 * Initialize the agent manager.
	 *
	 * @param mode The agent manager mode.
	 */
	private void init (int mode)
	{
		Engine.instance ().getEventRegistry ().addListener ("User", this);
		((ShutdownManager) Engine.instance ().getManagerRegistry ().getManager ("shutdown")).addObserver (this);

		AbstractIObjectFactory iObjectFactory = (AbstractIObjectFactory) Engine.instance ().getIObjectFactory ();

		iObjectFactory.register (new AgentTransferAction ());
	}

	/**
	 * This method is only used for testing purposes. It add some simple
	 * agents to the system.
	 *
	 * @param mode The agent manager mode.
	 */
	@SuppressWarnings("unused")
	private void initTestAgents (int mode)
	{
		AbstractIObjectFactory iObjectFactory = (AbstractIObjectFactory) Engine.instance ().getIObjectFactory ();
		SimpleAgent simpleAgent = new SimpleAgent ();

		iObjectFactory.register (simpleAgent);

		ScriptAgent scriptAgent = new ScriptAgent ();

		iObjectFactory.register (scriptAgent);

		simpleAgent.init (this);
		simpleAgent.setUniqueId (1);

		scriptAgent.init (this);
		scriptAgent.setUniqueId (2);

		System.out.println ("Awake simple entity");

		if (mode == AGENT_SERVER)
		{
			simpleAgent.awake (container);
			scriptAgent.awake (container);
		}
	}

	/**
	 * Start a agent
	 *
	 * @param agent The agent to start.
	 */
	public void bringAgentToLife (Agent agent)
	{
		((DataObject) agent).setUniqueId (Engine.instance ().getPersistentIDGenerator ().createId ());
		agent.init (this);
		container.simpleThreadAwake ((DataObject) agent);
		agent.awake (container);
	}

	/**
	 * Get the agent container.
	 *
	 * @return The agent container.
	 */
	public AgentContainer getContainer ()
	{
		return container;
	}

	public boolean onServer ()
	{
		return IritgoEngine.instance ().isServer ();
	}

	/**
	 * This method is called when user has logged in.
	 *
	 * @param event The user event.
	 */
	public void userEvent (UserEvent event)
	{
		if (event.isLoggedIn ())
		{
			Dispatcher dispatcher = new Dispatcher (event.getUser ());

			dispatcher.setUniqueId (event.getUser ().getName ());
			container.addDispatcher (dispatcher);
		}
		else
		{
			container.removeDispatcher (event.getUser ().getName ());
		}
	}

	/**
	 * Called when the system shuts down.
	 */
	public void onShutdown ()
	{
		container.close ();
	}

	/**
	 * This method is called when a user has logged of.
	 *
	 * @param user The leaving user.
	 */
	public void onUserLogoff (User user)
	{
	}

	/**
	 * Return the singleton agent manager instance.
	 *
	 * @return AgentManager The agent manager.
	 */
	public static AgentManager instance ()
	{
		return agentManager;
	}

	/**
	 * Create a singeleton agent manager.
	 *
	 * @param mode The manager mode, client or server (SPHERE_SERVER, SPHERE_CLIENT)
	 */
	public static void createAgentManager (int mode)
	{
		if (agentManager != null)
		{
			return;
		}

		agentManager = new AgentManager (mode);
	}

	/**
	 * Get the object type id.
	 *
	 * @return The type id.
	 */
	public String getTypeId ()
	{
		return "AgentManager";
	}
}
