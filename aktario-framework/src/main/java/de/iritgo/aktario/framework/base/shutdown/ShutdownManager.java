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

package de.iritgo.aktario.framework.base.shutdown;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.manager.Manager;
import de.iritgo.aktario.framework.IritgoEngine;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.aktario.framework.base.AsyncCommandProcessor;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.action.UserLogoffServerAction;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 *
 */
public class ShutdownManager extends BaseObject implements Manager
{
	private static int numOfChecks = 10;

	private List observers;

	private boolean userLogoffActionPerformed;

	public ShutdownManager ()
	{
		super ("shutdown");
		observers = new LinkedList ();
		userLogoffActionPerformed = false;
	}

	public void addObserver (ShutdownObserver shutdownObserver)
	{
		observers.add (shutdownObserver);
	}

	public void removeObserver (ShutdownObserver shutdownObserver)
	{
		observers.remove (shutdownObserver);
	}

	/**
	 * This methode is basically used by the client. Its calls all classes they registered here.
	 */
	public void shutdown ()
	{
		LinkedList tmpObservers = new LinkedList (observers);

		for (Iterator i = tmpObservers.iterator (); i.hasNext ();)
		{
			((ShutdownObserver) i.next ()).onShutdown ();
		}

		if (AppContext.instance ().isUserLoggedIn ())
		{
			ActionTools.sendToServer (new UserLogoffServerAction ());
			waitForUserLogoffAction ();
		}

		for (int i = numOfChecks; i >= 0; --i)
		{
			waitForCommands ();
			waitForActions ();
		}
	}

	/**
	 * This methode is basically used by the server. Its calls all classes they registered here and distribute the user object.
	 */
	public void shutdown (User user)
	{
		LinkedList tmpObservers = new LinkedList (observers);

		for (Iterator i = tmpObservers.iterator (); i.hasNext ();)
		{
			((ShutdownObserver) i.next ()).onUserLogoff (user);
		}

		for (int i = numOfChecks; i >= 0; --i)
		{
			waitForCommands ();
			waitForActions ();
		}
	}

	public void userLogoffActionPerformend ()
	{
		userLogoffActionPerformed = true;
	}

	private void waitForUserLogoffAction ()
	{
		int i = 0;

		while (! userLogoffActionPerformed && i <= 5)
		{
			try
			{
				++i;
				Thread.sleep (1000);
			}
			catch (Exception x)
			{
			}
		}
	}

	private void waitForCommands ()
	{
		AsyncCommandProcessor async = IritgoEngine.instance ().getAsyncCommandProcessor ();

		while (async.commandsInProcess ())
		{
			try
			{
				Thread.sleep (1000);
			}
			catch (Exception x)
			{
			}
		}
	}

	private void waitForActions ()
	{
		for (Iterator i = observers.iterator (); i.hasNext ();)
		{
			ShutdownObserver shutdownObserver = (ShutdownObserver) i.next ();

			while (Engine.instance ().getFlowControl ().ruleExists (
							"shutdown.in.progress." + shutdownObserver.getTypeId ()))
			{
				try
				{
					Thread.sleep (500);
				}
				catch (Exception x)
				{
				}
			}
		}
	}

	public void init ()
	{
	}

	public void unload ()
	{
	}
}
