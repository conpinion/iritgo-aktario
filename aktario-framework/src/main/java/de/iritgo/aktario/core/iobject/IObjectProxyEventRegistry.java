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

package de.iritgo.aktario.core.iobject;


import de.iritgo.aktario.core.event.Event;
import de.iritgo.aktario.core.event.EventListener;
import de.iritgo.aktario.core.logger.Log;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * The IObjectProxyEventRegistry, registriert und feuert events.
 */
public class IObjectProxyEventRegistry
{
	/** All proxy event listeners. */
	private HashMap eventListeners;

	@SuppressWarnings("unused")
	private LinkedList unfiredListeners;

	/**
	 * Constructor
	 *
	 */
	public IObjectProxyEventRegistry ()
	{
		eventListeners = new HashMap ();
		unfiredListeners = new LinkedList ();
	}

	/**
	 * Add the EventListener for Category
	 */
	public void addEventListener (IObject prototypeable, EventListener eventListener)
	{
		List listeners = null;

		synchronized (eventListeners)
		{
			listeners = (List) eventListeners.get (prototypeable);
		}

		if (listeners == null)
		{
			listeners = new LinkedList ();
			eventListeners.put (prototypeable, listeners);
		}

		if (! listeners.contains (eventListener))
		{
			listeners.add (eventListener);
		}
	}

	/**
	 * Remove the EventListener for Category
	 */
	public void removeEventListener (IObject prototypeable, EventListener eventListener)
	{
		List listeners = null;

		synchronized (eventListeners)
		{
			listeners = (List) eventListeners.get (prototypeable);
		}

		if (listeners != null)
		{
			listeners.remove (eventListener);
		}
	}

	public void removeEventListener (EventListener eventListener)
	{
		for (Iterator i = eventListeners.values ().iterator (); i.hasNext ();)
		{
			((List) i.next ()).remove (eventListener);
		}
	}

	public void clear ()
	{
		eventListeners.clear ();
	}

	/**
	 * Fire a event.
	 */
	public void fire (IObject prototypeable, Event event)
	{
		List listeners = null;
		List tmpList = null;

		synchronized (eventListeners)
		{
			listeners = (List) eventListeners.get (prototypeable);

			if ((listeners == null) || (listeners.size () == 0))
			{
				//TODO: Please delete this code after the 14.7.2004! Than i believe we don't need this code :)!
				// 				if (! unfiredListeners.contains (prototypeable))
				// 				{
				// 					unfiredListeners.add (prototypeable);
				// 				}
				return;
			}

			tmpList = new LinkedList (listeners);
		}

		for (Iterator i = tmpList.iterator (); i.hasNext ();)
		{
			EventListener listener = (EventListener) i.next ();

			Class klass = listener.getClass ();
			boolean fired = false;

			while (klass != null && ! fired)
			{
				Class[] interfaces = klass.getInterfaces ();

				for (int j = 0; j < interfaces.length; ++j)
				{
					if (EventListener.class.isAssignableFrom (interfaces[j]))
					{
						try
						{
							interfaces[j].getDeclaredMethods ()[0].invoke (listener, new Object[]
							{
								event
							});
							fired = true;

							break;
						}
						catch (IllegalArgumentException x)
						{
						}
						catch (SecurityException x)
						{
						}
						catch (IllegalAccessException x)
						{
						}
						catch (InvocationTargetException x)
						{
							Log.logError ("system", "IObjectProxyEventRegistry.fire",
											"Called listener method has a InvocationTargetException in Class: " + klass
															+ ": " + interfaces[j]);

							ByteArrayOutputStream trace = new ByteArrayOutputStream ();
							PrintWriter traceOut = new PrintWriter (trace);

							x.getCause ().printStackTrace (traceOut);
							traceOut.close ();

							Log.logError ("system", "IObjectProxyEventRegistry.fire", "Root cause was: "
											+ x.getCause ());

							Log.logError ("system", "IObjectProxyEventRegistry.fire", "Root cause stack trace: "
											+ trace.toString ());
						}
					}
				}

				klass = klass.getSuperclass ();
			}
		}
	}
}
