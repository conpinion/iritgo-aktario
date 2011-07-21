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

package de.iritgo.aktario.core.gui;


import de.iritgo.aktario.core.logger.Log;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * CallbackActionListener is a utility class that makes it easier to
 * register action listeners. Instead of subclassing ActionListener, you
 * only need to specify a (public) method, that will be calles by this
 * action listener through reflection.
 */
public class CallbackActionListener implements ActionListener
{
	/** The object to call. */
	protected Object object;

	/** The method to call. */
	protected Method method;

	/**
	 * Create a new CallbackActionListener.
	 *
	 * @param callbackObject The object to invoke.
	 * @param methodName The name of the method to invoke.
	 */
	public CallbackActionListener (Object callbackObject, String methodName)
	{
		this.object = callbackObject;

		try
		{
			method = object.getClass ().getMethod (methodName, new Class[]
			{
				ActionEvent.class
			});
		}
		catch (NoSuchMethodException e)
		{
			Log.logError ("resource", "CallbackActionListener", "NoSuchMethodException");
		}
	}

	/**
	 * Called from the swing framework when an action event
	 * occurred
	 *
	 * @param event The action event.
	 */
	public void actionPerformed (ActionEvent event)
	{
		try
		{
			method.invoke (object, new Object[]
			{
				event
			});
		}
		catch (IllegalAccessException x)
		{
			Log.logError ("system", "CallbackActionListener", x.toString ());
		}
		catch (InvocationTargetException x)
		{
			Log.logError ("resource", "CallbackActionListener", x.toString ());
		}
	}
}
