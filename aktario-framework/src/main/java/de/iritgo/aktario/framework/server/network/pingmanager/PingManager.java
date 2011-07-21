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

package de.iritgo.aktario.framework.server.network.pingmanager;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.thread.Threadable;
import de.iritgo.aktario.framework.base.shutdown.ShutdownManager;
import de.iritgo.aktario.framework.base.shutdown.ShutdownObserver;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.UserEvent;
import de.iritgo.aktario.framework.user.UserListener;
import java.util.Iterator;


/**
 *
 */
public class PingManager extends Threadable implements UserListener, ShutdownObserver
{
	static public int PING_INTERVAL = 10000; //Milliseconds

	private PingContextRegistry pingContextRegistry;

	/**
	 * Standard constructor
	 */
	public PingManager ()
	{
		super ("ping");
		pingContextRegistry = new PingContextRegistry ();

		((ShutdownManager) Engine.instance ().getManagerRegistry ().getManager ("shutdown")).addObserver (this);

		Engine.instance ().getEventRegistry ().addListener ("User", this);
	}

	public void run ()
	{
		setState (Threadable.FREE);

		synchronized (pingContextRegistry)
		{
			for (Iterator i = pingContextRegistry.getPingContextIterator (); i.hasNext ();)
			{
				PingContext pingContext = (PingContext) i.next ();

				pingContext.ping ();
			}
		}

		try
		{
			Thread.sleep (PING_INTERVAL);
		}
		catch (InterruptedException x)
		{
		}
	}

	public void receivedPing (long uniqueId, long time)
	{
		synchronized (pingContextRegistry)
		{
			((PingContext) pingContextRegistry.get ("" + uniqueId)).receivedPing (time);
		}
	}

	/**
	 * Its called if a new user login successful.
	 *
	 * @param event The EventOject.
	 */
	public void userEvent (UserEvent event)
	{
		synchronized (pingContextRegistry)
		{
			pingContextRegistry.add (new PingContext (event.getUser ()));
		}
	}

	public void doShutdownNotify ()
	{
		synchronized (pingContextRegistry)
		{
			pingContextRegistry.clear ();
		}
	}

	public void onShutdown ()
	{
	}

	public void onUserLogoff (User user)
	{
		synchronized (pingContextRegistry)
		{
			pingContextRegistry.remove ("" + user.getUniqueId ());
		}
	}
}
