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


import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.core.command.CommandProcessor;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.thread.Threadable;
import de.iritgo.aktario.framework.appcontext.AppContext;
import java.util.LinkedList;
import java.util.List;


/**
 *
 */
public class AsyncCommandProcessor extends Threadable implements CommandProcessor
{
	private List commands;

	private Object listLock;

	public AsyncCommandProcessor ()
	{
		super ("AsyncCommandProcessor");
		commands = new LinkedList ();
		listLock = new Object ();
	}

	/**
	 * Checks the list and process the commands.
	 */
	@Override
	public void run ()
	{
		Command command = null;

		synchronized (listLock)
		{
			if (commands.size () > 0)
			{
				command = (Command) commands.get (0);
				Log.log ("system", "AsyncCommandProcessor", "Command: " + command, Log.DEBUG);
				commands.remove (command);
			}
		}

		if (command != null)
		{
			performCommand (command);
		}

		synchronized (listLock)
		{
			if (commands.size () == 0)
			{
				try
				{
					listLock.wait ();
				}
				catch (InterruptedException x)
				{
				}
			}
		}
	}

	/**
	 * Add a command to the list.
	 *
	 * @param command The Command.
	 *
	 * @deprecated Use the perform() method.
	 */
	public void addCommand (Command command)
	{
		perform (command);
	}

	/**
	 * Perform a command.
	 */
	public Object perform (Command command)
	{
		synchronized (listLock)
		{
			commands.add (command);
			listLock.notify ();

			return null;
		}
	}

	/**
	 * Perform an command.
	 */
	private void performCommand (Command command)
	{
		Object lockObject = AppContext.instance ().getLockObject ();

		synchronized (lockObject)
		{
			if (command.canPerform ())
			{
				command.performWithResult ();
			}
		}
	}

	public boolean commandsInProcess ()
	{
		return commands.size () > 0;
	}

	/**
	 * Called from the ThreadController to close this Thread.
	 */
	@Override
	public void dispose ()
	{
		synchronized (listLock)
		{
			setState (Threadable.CLOSING);
			listLock.notify ();
		}
	}
}
