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


/**
 * An Agent is a thing thats lives. Most often this will be a
 * DataObject with additional executable functionality.
 *
 * An agent can be executed anywhere in the system (on any server or
 * any client). An agent also can wander around the system.
 */
public interface Agent
{
	/**
	 * Get the unique id of this agent
	 *
	 * @return int Unique id of this agent.
	 */
	public long getUniqueId();

	/**
	 * Initialize the agent.
	 *
	 * @param agentManager The agent manager.
	 */
	public void init(AgentManager agentManager);

	/**
	 * If an agent comes to life, the awake method is called.
	 *
	 * @param container The agent container.
	 */
	public void awake(AgentContainer container);

	/**
	 * The heartbeat will be called at regular intervals.
	 */
	public void heartbeat();

	/**
	 * If a agent should sleep the sleep method will called.
	 *
	 * @param container The agent container.
	 */
	public void sleep(AgentContainer container);

	/**
	 * This method is called if the agents environment is
	 * about to be shut down.
	 *
	 * @param container The agent container.
	 */
	public void shutdown(AgentContainer container);

	/**
	 * Check wether this agent has a heart beat.
	 *
	 * @return True if the agent is alive and has a heart beat.
	 */
	public boolean hasHeartBeat();

	/**
	 * Set the heart beat.
	 *
	 * @param heartBeat True if the agent is alive and has a heart beat.
	 */
	public void setHeartBeat(boolean heartBeat);

	/**
	 * Get an an overview of all functions of this agent.
	 */
	public String getFunctions();

	/**
	 * Communication interface to other agents.
	 *
	 * @param communication A message string.
	 * @return A message string.
	 */
	public String communication(String communication);

	/**
	 * The agent system will die. This is the last chance for the agent
	 * to clean up.
	 *
	 * @param container The agent container.
	 */
	public void die(AgentContainer container);
}
