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

package de.iritgo.aktario.agent.container;


import de.iritgo.aktario.agent.Agent;
import de.iritgo.aktario.agent.AgentManager;
import de.iritgo.aktario.agent.transfer.Dispatcher;
import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.thread.ThreadService;
import de.iritgo.aktario.framework.base.DataObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * The techno core class holts all information about the underling system.
 * Free RAM, free CPU performance, Network traffic and so on.
 * The techno core bring the entity to live, it give there the heard beats...
 */
public class AgentContainer
{
	/** The sphere */
	@SuppressWarnings("unused")
	private AgentManager agentManager;

	private Map container;

	private Map agents;

	private Map dispatcherMap;

	/**
	 * The default constructor from the technocore
	 *
	 * @param agentManager The sphere for this technocore
	 */
	public AgentContainer(AgentManager agentManager)
	{
		this.agentManager = agentManager;
		agents = new HashMap();
		dispatcherMap = new HashMap();
		container = new HashMap();
	}

	/**
	 * Helper method for a agent to bring it to life.
	 *
	 * @param agent The agent to thread.
	 */
	public void simpleThreadAwake(DataObject agent)
	{
		ThreadService threadService = Engine.instance().getThreadService();
		SimpleThreadAgentContainer simpleThreadContainer = new SimpleThreadAgentContainer((Agent) agent);

		container.put(new Long(agent.getUniqueId()), simpleThreadContainer);
		threadService.add(simpleThreadContainer);
		((Agent) agent).setHeartBeat(true);
	}

	/**
	 * Helper method for a agent to sleep
	 *
	 * @param agent The agent to thread.
	 */
	public void simpleThreadSleep(DataObject agent)
	{
		SimpleThreadAgentContainer simpleThreadContainer = (SimpleThreadAgentContainer) container.get(new Long(agent
						.getUniqueId()));

		simpleThreadContainer.setState(SimpleThreadAgentContainer.CLOSING);
		container.remove(simpleThreadContainer);
	}

	/**
	 * Add agent to the map
	 *
	 * @param agent The agent.
	 */
	public void addAgent(Agent agent)
	{
		agents.put(new Long(agent.getUniqueId()), agent);
	}

	/**
	 * Remove a agent from the map
	 *
	 * @param agent The agent.
	 */
	public void removeAgent(Agent agent)
	{
		agents.remove(new Long(agent.getUniqueId()));
	}

	/**
	 * Add a dispatcher to the map
	 *
	 * @param dispatcher The dispatcher.
	 */
	public void addDispatcher(Dispatcher dispatcher)
	{
		dispatcherMap.put(dispatcher.getUniqueId(), dispatcher);
	}

	/**
	 * Remove a dispatcher from the map
	 *
	 * @param dispatcher The dispatcher.
	 */
	public void removeDispatcher(Dispatcher dispatcher)
	{
		dispatcherMap.remove(dispatcher.getUniqueId());
	}

	/**
	 * Remove a dispatcher from the map
	 *
	 * @param dispatcherId The dispatcher id.
	 */
	public void removeDispatcher(String dispatcherId)
	{
		dispatcherMap.remove(dispatcherId);
	}

	synchronized public boolean sendToFirstDispatcher(Agent agent)
	{
		if (dispatcherMap.values().iterator().hasNext())
		{
			send(((Dispatcher) dispatcherMap.values().iterator().next()).getUniqueId(), agent);

			return true;
		}

		return false;
	}

	public void send(String dispatcherId, Agent agent)
	{
		Dispatcher dispatcher = (Dispatcher) dispatcherMap.get(dispatcherId);

		agent.sleep(this);
		dispatcher.sendAgent(agent);
	}

	public void close()
	{
		for (Iterator i = agents.values().iterator(); i.hasNext();)
		{
			Agent agent = (Agent) i.next();

			agent.die(this);
		}
	}
}
