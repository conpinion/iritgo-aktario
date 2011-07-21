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

package de.iritgo.aktario.framework.console;


import java.util.HashMap;
import java.util.Iterator;


/**
 *
 */
public class ConsoleCommandRegistry
{
	private HashMap consoleCommands;

	/**
	 * Constructor
	 *
	 */
	public ConsoleCommandRegistry ()
	{
		consoleCommands = new HashMap ();
	}

	/**
	 * Add a ConsoleCommand.
	 */
	public void add (ConsoleCommand consoleCommand)
	{
		consoleCommands.put (consoleCommand.getCommandId (), consoleCommand);
	}

	/**
	 * Get a ConsoleCommand.
	 */
	public ConsoleCommand get (String id)
	{
		return (ConsoleCommand) consoleCommands.get (id);
	}

	/**
	 * Remove a ConsoleCommand.
	 */
	public void remove (ConsoleCommand consoleCommand)
	{
		consoleCommands.remove (consoleCommand.getCommandId ());
	}

	public Iterator getCommandIterator ()
	{
		return consoleCommands.values ().iterator ();
	}
}
