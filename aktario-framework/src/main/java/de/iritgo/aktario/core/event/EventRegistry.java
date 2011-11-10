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

package de.iritgo.aktario.core.event;


import de.iritgo.aktario.core.logger.Log;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * The EventRegistry is the central point used to register event
 * listeners and fire events.
 *
 * Take for example an event class PhoneRingEvent with an
 * assciated PhoneRingListener:
 *
 * <pre>
 *     public class PhoneRingEvent extends Event
 *     {
 *         public String phoneNumber;
 *
 *         ...
 *     }
 *
 *     public interface PhoneRingListener implements EventListener
 *     {
 *         public void phoneIsRinging (PhoneRingEvent event);
 *     }
 * </pre>
 *
 * Lets assume that you want to listen for PhoneRingEvents in a class
 * called CallCenter:
 *
 * <pre>
 *     public class CallCenter implements PhoneRingListener
 *     {
 *         public void phoneIsRinging (PhoneRingEvent event)
 *         {
 *             System.out.println ("Call from " + event.phoneNumber);
 *         }
 *
 *         ...
 *     }
 * </pre>
 *
 * At last you need to register your CallCenter instance with
 * the EventRegistry:
 *
 * <pre>
 *     Engine.getEngine ().getEventRegistry ().addListener ("phone", myCallCenter);
 * </pre>
 */
public class EventRegistry
{
	/**
	 * Events are organized by categories. For each category exists a list of
	 * event listeners.
	 */
	private HashMap categoryList;

	/**
	 * Create a new EventRegistry
	 */
	public EventRegistry()
	{
		categoryList = new HashMap();
	}

	/**
	 * Add an event lister to an event category.
	 *
	 * @param category The event category.
	 * @param listener The event listener to add.
	 */
	public void addListener(String category, EventListener listener)
	{
		if (! categoryList.containsKey(category))
		{
			categoryList.put(category, new LinkedList());
		}

		List listeners = (List) categoryList.get(category);

		if (! listeners.contains(listener)) //Dobble registration make no sense.
		{
			listeners.add(listener);
		}
	}

	/**
	 * Remove an event listener from an event category.
	 *
	 * @param category The event category.
	 * @param listener The event listener to remove.
	 */
	public void removeListener(String category, EventListener listener)
	{
		List listeners = (List) categoryList.get(category);

		if (listeners != null)
		{
			listeners.remove(listener);
		}
	}

	/**
	 * Remove all from the registry
	 */
	public void clear()
	{
		categoryList.clear();
	}

	/**
	 * Fire an event.
	 *
	 * @param category The event category which listener should be notified.
	 * @param event The event object that is send to the listener.
	 */
	public void fire(String category, Event event)
	{
		List listeners = (List) categoryList.get(category);

		if (listeners == null)
		{
			return;
		}

		listeners = new LinkedList(listeners);

		for (Iterator i = listeners.iterator(); i.hasNext();)
		{
			EventListener listener = (EventListener) i.next();

			Class klass = listener.getClass();
			boolean fired = false;

			while (klass != null && ! fired)
			{
				Class[] interfaces = klass.getInterfaces();

				for (int j = 0; j < interfaces.length; ++j)
				{
					if (de.iritgo.aktario.core.event.EventListener.class.isAssignableFrom(interfaces[j]))
					{
						try
						{
							interfaces[j].getDeclaredMethods()[0].invoke(listener, new Object[]
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
							Log.logError("system", "EventRegistry.fire",
											"Called listener method has a InvocationTargetException in Class: " + klass
															+ ": " + interfaces[j]);

							ByteArrayOutputStream trace = new ByteArrayOutputStream();
							PrintWriter traceOut = new PrintWriter(trace);

							x.getCause().printStackTrace(traceOut);
							traceOut.close();

							Log.logError("system", "EventRegistry.fire", "Root cause was: " + x.getCause());

							Log.logError("system", "EventRegistry.fire", "Root cause stack trace: " + trace.toString());
						}
					}
				}

				klass = klass.getSuperclass();
			}
		}
	}
}
