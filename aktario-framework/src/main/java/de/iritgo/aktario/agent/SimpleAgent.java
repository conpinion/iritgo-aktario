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
import de.iritgo.aktario.framework.base.DataObject;


/**
 * A simple agent that derrives from DataObject.
 */
public class SimpleAgent extends DataObject implements Agent
{
	/** Our agent manager. */
	private AgentManager agentManager;

	/** The heartbeat flag. */
	private boolean heartBeat = false;

	/** Incremented on any heartbeat. */
	private int time = 0;

	/**
	 * Create a new SimpleAgent.
	 */
	public SimpleAgent ()
	{
		super ("SimpleAgent");
	}

	/**
	 * Initialize the agent.
	 *
	 * @param agentManager The agent manager.
	 */
	public void init (AgentManager agentManager)
	{
		this.agentManager = agentManager;
	}

	/**
	 * If an agent comes to life, the awake method is called.
	 *
	 * @param container The agent container.
	 */
	public void awake (AgentContainer container)
	{
		container.simpleThreadAwake (this);
	}

	/**
	 * The heartbeat will be called at regular intervals.
	 */
	public void heartbeat ()
	{
		System.out.print (".");

		if (time > 3)
		{
			agentManager.getContainer ().sendToFirstDispatcher (this);
		}

		++time;
	}

	/**
	 * If a agent should sleep the sleep method will called.
	 *
	 * @param container The agent container.
	 */
	public void sleep (AgentContainer container)
	{
		container.simpleThreadSleep (this);
	}

	/**
	 * This method is called if the agents environment is
	 * about to be shut down.
	 *
	 * @param container The agent container.
	 */
	public void shutdown (AgentContainer container)
	{
	}

	/**
	 * Set the heart beat.
	 *
	 * @param heartBeat True if the agent is alive and has a heart beat.
	 */
	public void setHeartBeat (boolean heartBeat)
	{
		this.heartBeat = heartBeat;
	}

	/**
	 * Get an an overview of all functions of this agent.
	 */
	public boolean hasHeartBeat ()
	{
		return heartBeat;
	}

	/**
	 * Get an an overview of all functions of this agent.
	 */
	public String getFunctions ()
	{
		return "";
	}

	/**
	 * Communication interface to other agents.
	 *
	 * @param communication A message string.
	 * @return A message string.
	 */
	public String communication (String communication)
	{
		return "";
	}

	/**
	 * The agent system will die. This is the last chance for the agent
	 * to clean up.
	 *
	 * @param container The agent container.
	 */
	public void die (AgentContainer container)
	{
		sleep (container);
	}
}
