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


import bsh.AgentInterpreter;
import bsh.NameSpace;
import de.iritgo.aktario.agent.container.AgentContainer;
import de.iritgo.aktario.framework.base.DataObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;


/**
 * This is a scriptable agent. This agents functionality comes from a BeanShell-Script.
 */
public class ScriptAgent extends DataObject implements Agent
{
	/** The agent container. */
	@SuppressWarnings("unused")
	private AgentContainer agentContainer;

	/** The agent manager. */
	private AgentManager agentManager;

	/** True if the agent was sent to another place. */
	@SuppressWarnings("unused")
	private boolean isSend = false;

	/** The heart beat flag .*/
	private boolean heartBeat = false;

	/** The script interpreter. */
	private AgentInterpreter interpreter;

	/** Agent init script code. */
	private String initCode = "int i = 0;";

	/** Agent script code. */
	private String code = "System.out.println(\"\" + (++i));\n\r System.out.println (\"hallo\");";

	/** Used to transfer the BeanShell environment. */
	private Reader sourceIn;

	/** Incremented on any heartbeart. */
	private int time;

	/**
	 * Create a new ScriptAgent.
	 */
	public ScriptAgent()
	{
		super("ScriptAgent");
	}

	/**
	 * Initialize the agent.
	 *
	 * @param agentManager The agent manager.
	 */
	public void init(AgentManager agentManager)
	{
		this.agentManager = agentManager;
	}

	/**
	 * If an agent comes to life, the awake method is called.
	 *
	 * @param container The agent container.
	 */
	public void awake(AgentContainer container)
	{
		boolean isNew = false;

		if (interpreter == null)
		{
			isNew = true;
		}

		initInterpreter();

		container.simpleThreadAwake(this);

		try
		{
			if (isNew)
			{
				interpreter.eval(initCode);
				System.out.println("avake init!");
			}
		}
		catch (Exception x)
		{
		}
	}

	/**
	 * Initialize the BeanShell interpreter.
	 *
	 */
	private void initInterpreter()
	{
		try
		{
			NameSpace nameSpace = null;

			if (interpreter != null)
			{
				nameSpace = interpreter.getGlobalNameSpace();
			}

			if (nameSpace == null)
			{
				System.out.println("init!");

				interpreter = new bsh.AgentInterpreter();
				nameSpace = interpreter.getGlobalNameSpace();
			}

			if (sourceIn != null)
			{
				sourceIn.close();
			}

			sourceIn = new BufferedReader(new StringReader(code));

			interpreter.init(sourceIn, (NameSpace) nameSpace, "tmp");
		}
		catch (Exception x)
		{
			System.out.println("Create Interpreter:" + x);
		}
	}

	/**
	 * The heartbeat will called at regular intervals
	 */
	public void heartbeat()
	{
		try
		{
			if (interpreter.isEOF())
			{
				initInterpreter();
			}

			interpreter.evalSingleLine(sourceIn, interpreter.getGlobalNameSpace(), "tmp");

			++time;

			if (time > 5)
			{
				agentManager.getContainer().sendToFirstDispatcher(this);
			}
		}
		catch (Exception x)
		{
			System.out.println("Out:" + x);
		}
	}

	/**
	 * If a agent should sleep the sleep method will called.
	 *
	 * @param container The agent container.
	 */
	public void sleep(AgentContainer container)
	{
		container.simpleThreadSleep(this);
	}

	/**
	 * This method is called if the agents environment is
	 * about to be shut down.
	 *
	 * @param container The agent container.
	 */
	public void shutdown(AgentContainer container)
	{
	}

	/**
	 * Set the heart beat.
	 *
	 * @param heartBeat True if the agent is alive and has a heart beat.
	 */
	public void setHeartBeat(boolean heartBeat)
	{
		this.heartBeat = heartBeat;
	}

	/**
	 * Check wether this agent has a heart beat.
	 *
	 * @return True if the agent is alive and has a heart beat.
	 */
	public boolean hasHeartBeat()
	{
		return heartBeat;
	}

	/**
	 * Get an an overview of all functions of this agent.
	 */
	public String getFunctions()
	{
		return "";
	}

	/**
	 * Communication interface to other agents.
	 *
	 * @param communication A message string.
	 * @return A message string.
	 */
	public String communication(String communication)
	{
		return "";
	}

	/**
	 * Write the object attributes to an output stream.
	 *
	 * @param stream The output stream.
	 */
	@Override
	public void writeObject(OutputStream stream) throws IOException
	{
		super.writeObject(stream);
		new ObjectOutputStream(stream).writeObject(interpreter);
	}

	/**
	 * Read the object attributes from an input stream.
	 *
	 * @param stream The input stream.
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	@Override
	public void readObject(InputStream stream) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		super.readObject(stream);
		interpreter = (AgentInterpreter) new ObjectInputStream(stream).readObject();
	}

	/**
	 * The agent system will die. This is the last chance for the agent
	 * to clean up.
	 *
	 * @param container The agent container.
	 */
	public void die(AgentContainer container)
	{
		sleep(container);
	}
}
