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

package de.iritgo.aktario.framework.base;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.logger.Log;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * If a Iobject changed this class notified all observers.
 */
public class IObjectChangedNotifier implements IObjectModifiedListener
{
	/* Type observers */
	private HashMap typeObservers;

	/* custom observers */
	private List customObservers;

	/**
	 * Standard constructor
	 */
	public IObjectChangedNotifier()
	{
		typeObservers = new HashMap();
		customObservers = new LinkedList();
		Engine.instance().getEventRegistry().addListener("objectmodified", this);
	}

	/**
	 * Called when an iobject was modified.
	 *
	 * @param event The modification event.
	 */
	public void iObjectModifiedEvent(IObjectModifiedEvent event)
	{
		IObject iObject = event.getModifiedObject();

		List observers = (List) typeObservers.get(iObject.getTypeId());

		if (observers != null)
		{
			for (Iterator i = observers.iterator(); i.hasNext();)
			{
				callFirstMethodFromObserver((IObjectChangedObserver) i.next(), iObject);
			}
		}

		for (Iterator i = customObservers.iterator(); i.hasNext();)
		{
			IObjectChangedCustomObserver iObjectChangedCustomObserver = (IObjectChangedCustomObserver) i.next();

			checkAndCallFirstMethod(iObjectChangedCustomObserver, iObject);
		}
	}

	private void checkAndCallFirstMethod(IObjectChangedCustomObserver iObjectChangedCustomObserver, IObject iObject)
	{
		Class klass = iObjectChangedCustomObserver.getClass();
		boolean fired = false;

		while (klass != null && ! fired)
		{
			Class[] interfaces = klass.getInterfaces();

			for (int j = 0; j < interfaces.length; ++j)
			{
				if (de.iritgo.aktario.framework.base.IObjectChangedCustomObserver.class.isAssignableFrom(interfaces[j]))
				{
					try
					{
						Object object = interfaces[j].getDeclaredMethods()[1].invoke(iObjectChangedCustomObserver,
										new Object[]
										{
											iObject
										});

						if (((Boolean) object).booleanValue())
						{
							interfaces[j].getDeclaredMethods()[0].invoke(iObjectChangedCustomObserver, new Object[]
							{
								iObject
							});
						}

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
						Log.logError("system", "IObjectChangedNotifier.fire",
										"Called listener method has a InvocationTargetException in Class: " + klass
														+ ": " + interfaces[j]);

						ByteArrayOutputStream trace = new ByteArrayOutputStream();
						PrintWriter traceOut = new PrintWriter(trace);

						x.getCause().printStackTrace(traceOut);
						traceOut.close();

						Log.logError("system", "IObjectChangedNotifier.fire", "Root cause was: " + x.getCause());

						Log.logError("system", "IObjectChangedNotifier.fire", "Root cause stack trace: "
										+ trace.toString());
					}
				}
			}

			klass = klass.getSuperclass();
		}
	}

	private void callFirstMethodFromObserver(IObjectChangedObserver iObjectChangedObserver, IObject iObject)
	{
		Class klass = iObjectChangedObserver.getClass();
		boolean fired = false;

		while (klass != null && ! fired)
		{
			Class[] interfaces = klass.getInterfaces();

			for (int j = 0; j < interfaces.length; ++j)
			{
				if (de.iritgo.aktario.framework.base.IObjectChangedObserver.class.isAssignableFrom(interfaces[j]))
				{
					try
					{
						interfaces[j].getDeclaredMethods()[0].invoke(iObjectChangedObserver, new Object[]
						{
							iObject
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
						Log.logError("system", "IObjectChangedNotifier.fire",
										"Called listener method has a InvocationTargetException in Class: " + klass
														+ ": " + interfaces[j]);

						ByteArrayOutputStream trace = new ByteArrayOutputStream();
						PrintWriter traceOut = new PrintWriter(trace);

						x.getCause().printStackTrace(traceOut);
						traceOut.close();

						Log.logError("system", "IObjectChangedNotifier.fire", "Root cause was: " + x.getCause());

						Log.logError("system", "IObjectChangedNotifier.fire", "Root cause stack trace: "
										+ trace.toString());
					}
				}
			}

			klass = klass.getSuperclass();
		}
	}

	/**
	 * Add a type observer
	 */
	public void registerTypeObserver(String iObjectType, IObjectChangedObserver iObjectChangeObserver)
	{
		if (! typeObservers.containsKey(iObjectType))
		{
			typeObservers.put(iObjectType, new LinkedList());
		}

		List observers = (List) typeObservers.get(iObjectType);

		if (! observers.contains(iObjectChangeObserver)) //Dobble registration make no sense.
		{
			observers.add(iObjectChangeObserver);
		}
	}

	/**
	 * Remove a type observer
	 */
	public void removeTypeObserver(String iObjectType, IObjectChangedObserver iObjectChangeObserver)
	{
		List listeners = (List) typeObservers.get(iObjectType);

		if (listeners != null)
		{
			listeners.remove(iObjectChangeObserver);
		}
	}

	/**
	 * Add a custom observer
	 */
	public void registerCustomObserver(IObjectChangedCustomObserver iObjectChangedCustomObserver)
	{
		customObservers.add(iObjectChangedCustomObserver);
	}

	/**
	 * Remove a custom observer
	 */
	public void removeCustomObserver(IObjectChangedCustomObserver iObjectChangedCustomObserver)
	{
		customObservers.remove(iObjectChangedCustomObserver);
	}
}
