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

package de.iritgo.aktario.framework.server.cadencemanager;


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
public class CadenceManager extends Threadable implements UserListener, ShutdownObserver
{
	static public int TURN_INTERVAL = 60 * 100 * 5; //Milliseconds

	static public int DELAY_INTERVAL = 5; // ThreadSleepTime

	private CadeneContextRegistry turnContextRegistry;

	private int currentMilli = 60 * 100 * 5;

	private long tmpTime;

	private long tmpTime2;

	private int addTurnDelay;

	/**
	 * Standard constructor
	 */
	public CadenceManager ()
	{
		super ("turn");
		turnContextRegistry = new CadeneContextRegistry ();

		((ShutdownManager) Engine.instance ().getManagerRegistry ().getManager ("shutdown")).addObserver (this);

		Engine.instance ().getEventRegistry ().addListener ("User", this);
		currentMilli = 0;
		addTurnDelay = 0;
	}

	public void run ()
	{
		setState (Threadable.FREE);

		long c = System.currentTimeMillis ();
		long diff = c - tmpTime;

		tmpTime = c;

		int add = Math.round (diff);

		if (add == 0)
		{
			add = 1;
		}

		synchronized (turnContextRegistry)
		{
			for (Iterator i = turnContextRegistry.getTurnContextIterator (); i.hasNext ();)
			{
				CadenceContext turnContext = (CadenceContext) i.next ();

				if ((! turnContext.isTurnFired ()) && (currentMilli >= (turnContext.getPingTime () + addTurnDelay)))
				{
					turnContext.turn ();
				}
			}
		}

		if (currentMilli >= (TURN_INTERVAL + addTurnDelay))
		{
			long c2 = System.currentTimeMillis ();
			long diff2 = c2 - tmpTime2;

			tmpTime2 = c;

			if (diff2 == 0)
			{
				addTurnDelay = addTurnDelay * 100;
			}
			else
			{
				addTurnDelay = addTurnDelay + (TURN_INTERVAL - Math.round (diff2));
			}

			currentMilli = 0;

			for (Iterator i = turnContextRegistry.getTurnContextIterator (); i.hasNext ();)
			{
				CadenceContext turnContext = (CadenceContext) i.next ();

				if (turnContext.isTurnFired ())
				{
					turnContext.reset ();
				}
				else
				{
				}
			}
		}

		try
		{
			Thread.sleep (DELAY_INTERVAL);
		}
		catch (InterruptedException x)
		{
		}

		currentMilli = currentMilli + add;
	}

	/**
	 * Its called if a new user login successful.
	 *
	 * @param event The EventOject.
	 */
	public void userEvent (UserEvent event)
	{
		synchronized (turnContextRegistry)
		{
			turnContextRegistry.add (new CadenceContext (event.getUser ()));
		}
	}

	public void onShutdown ()
	{
		synchronized (turnContextRegistry)
		{
			turnContextRegistry.clear ();
		}
	}

	public void onUserLogoff (User user)
	{
		synchronized (turnContextRegistry)
		{
			turnContextRegistry.remove (user);
		}
	}
}
