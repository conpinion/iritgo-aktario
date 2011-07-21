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

package de.iritgo.aktario.instantmessage;


import de.iritgo.aktario.agent.Agent;
import de.iritgo.aktario.agent.AgentManager;
import de.iritgo.aktario.agent.container.AgentContainer;
import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.aktario.framework.base.DataObject;
import de.iritgo.aktario.framework.client.command.ShowDialog;
import de.iritgo.aktario.framework.command.CommandTools;
import de.iritgo.aktario.framework.server.Server;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.UserEvent;
import de.iritgo.aktario.framework.user.UserListener;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Properties;


/**
 *
 */
public class InstantMessageAgent extends DataObject implements Agent
{
	/** The agent container. */
	private AgentContainer agentContainer;

	/** The agent manager. */
	private AgentManager agentManager;

	/** True if the agent was sent to another place. */
	private boolean isSend = false;

	/** The heart beat flag .*/
	private boolean heartBeat = false;

	/** Incremented on any heartbeart. */
	private int time;

	private boolean activateHeatbeat;

	private UserListener userListener;

	private boolean activateShowMessage;

	/**
	 * Create a new ScriptAgent.
	 */
	public InstantMessageAgent ()
	{
		super ("InstantMessageAgent");
		addAttribute ("targetUser", "");
		addAttribute ("sourceUser", "");
		addAttribute ("message", "");
		addAttribute ("timestamp", new Long (0));
	}

	public InstantMessageAgent (String sourceUser, String targetUser, String message)
	{
		this ();
		setSourceUser (sourceUser);
		setTargetUser (targetUser);
		setMessage (message);
		setTimestamp (System.currentTimeMillis ());
	}

	public void setTargetUser (String targetUser)
	{
		setAttribute ("targetUser", targetUser);
	}

	public String getTargetUser ()
	{
		return getStringAttribute ("targetUser");
	}

	public void setSourceUser (String sourceUser)
	{
		setAttribute ("sourceUser", sourceUser);
	}

	public String getSourceUser ()
	{
		return getStringAttribute ("sourceUser");
	}

	public void setMessage (String message)
	{
		setAttribute ("message", message);
	}

	public String getMessage ()
	{
		return getStringAttribute ("message");
	}

	public void setTimestamp (long timestamp)
	{
		setAttribute ("timestamp", timestamp);
	}

	public long getTimestamp ()
	{
		return getLongAttribute ("timestamp");
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
	public void awake (final AgentContainer container)
	{
		boolean server = agentManager.onServer ();

		agentContainer = container;

		if (server)
		{
			activateHeatbeat = true;

			boolean isOnline = false;

			Boolean instantMessagesEnabled = (Boolean) CommandTools.performSimple ("InstantMessagesEnabled",
							new Properties ());

			if ((instantMessagesEnabled != null) && ! instantMessagesEnabled)
			{
				this.sleep (container);

				return;
			}

			for (Iterator i = Server.instance ().getUserRegistry ().onlineUserIterator (); i.hasNext ();)
			{
				User user = (User) i.next ();

				if (user.getName ().equals (getTargetUser ()))
				{
					isOnline = true;
					container.send (user.getName (), getInstantMessageAgent ());

					break;
				}
			}

			userListener = new UserListener ()
			{
				public void userEvent (UserEvent e)
				{
					if (e.isLoggedIn ())
					{
						if (e.getUser ().getName ().equals (getTargetUser ()))
						{
							container.send (e.getUser ().getName (), getInstantMessageAgent ());
						}
					}
				}
			};

			if (! isOnline)
			{
				Engine.instance ().getEventRegistry ().addListener ("User", userListener);
			}
		}
	}

	/**
	 * The heartbeat will called at regular intervals
	 */
	public void heartbeat ()
	{
		boolean server = agentManager.onServer ();

		if (! server && (AppContext.instance ().isUserLoggedIn ()))
		{
			User user = AppContext.instance ().getUser ();

			if (user.getName ().equals (getTargetUser ()))
			{
				Properties props = new Properties ();

				props.put ("message", getMessage ());
				props.put ("sourceUser", getSourceUser ());
				props.put ("timestamp", new Long (getTimestamp ()));
				props.put ("bounds", new Rectangle (0, 0, 300, 100));
				props.put ("weightx", new Double (2.5));
				props.put ("weighty", new Double (1.5));
				CommandTools.performAsync (new ShowDialog ("InstantMessageReceivePane"), props);

				Engine.instance ().getEventRegistry ().fire (
								"InstantMessage",
								new InstantMessageEvent (getTimestamp (), getMessage (), getSourceUser (),
												getTargetUser (), true));

				sleep (agentContainer);

				return;
			}

			if (user.getName ().equals (getSourceUser ()))
			{
				agentContainer.sendToFirstDispatcher (this);
			}
		}
	}

	public InstantMessageAgent getInstantMessageAgent ()
	{
		return this;
	}

	/**
	 * If a agent should sleep the sleep method will called.
	 *
	 * @param container The agent container.
	 */
	public void sleep (AgentContainer container)
	{
		if (userListener != null)
		{
			Engine.instance ().getEventRegistry ().removeListener ("User", userListener);
		}

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
		if (userListener != null)
		{
			Engine.instance ().getEventRegistry ().removeListener ("User", userListener);
		}
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
	 * Check wether this agent has a heart beat.
	 *
	 * @return True if the agent is alive and has a heart beat.
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
	 * Write the object attributes to an output stream.
	 *
	 * @param stream The output stream.
	 */
	public void writeObject (OutputStream stream) throws IOException
	{
		super.writeObject (stream);
	}

	/**
	 * Read the object attributes from an input stream.
	 *
	 * @param stream The input stream.
	 */
	public void readObject (InputStream stream) throws IOException, ClassNotFoundException
	{
		super.readObject (stream);
	}

	/**
	 * The agent system will die. This is the last chance for the agent
	 * to clean up.
	 *
	 * @param container The agent container.
	 */
	public void die (AgentContainer container)
	{
		if (userListener != null)
		{
			Engine.instance ().getEventRegistry ().removeListener ("User", userListener);
		}

		sleep (container);
	}
}
