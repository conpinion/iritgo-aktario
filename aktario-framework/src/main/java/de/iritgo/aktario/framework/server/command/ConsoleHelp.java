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

package de.iritgo.aktario.framework.server.command;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.core.resource.ResourceService;
import de.iritgo.aktario.framework.console.ConsoleCommand;
import de.iritgo.aktario.framework.console.ConsoleManager;
import java.util.Iterator;


/**
 *
 */
public class ConsoleHelp extends Command
{
	private String command = null;

	public ConsoleHelp ()
	{
	}

	public ConsoleHelp (String command)
	{
		this.command = command;
	}

	/**
	 * Display the help for all console commands.
	 */
	public void perform ()
	{
		Engine engine = Engine.instance ();
		ResourceService resourceService = engine.getResourceService ();
		ConsoleManager consoleManager = (ConsoleManager) engine.getManagerRegistry ().getManager ("console");

		if (command == null)
		{
			for (Iterator i = consoleManager.getConsoleCommandIterator (); i.hasNext ();)
			{
				System.out.println (resourceService.getStringWithoutException (((ConsoleCommand) i.next ())
								.getHelpId ()
								+ ".short"));
			}
		}
		else
		{
			ConsoleCommand consoleCommand = consoleManager.getConsoleCommand (command);

			if (consoleCommand == null)
			{
				System.out.println ("Command unknown.");

				return;
			}

			System.out.println (resourceService.getStringWithoutException (consoleCommand.getHelpId () + ".long"));
		}
	}
}
