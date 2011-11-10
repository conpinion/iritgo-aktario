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

package de.iritgo.aktario.xmlrpc;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.core.logger.Log;


/**
 * Define a command that is callable through xmlrpc.
 *
 * @version $Id: AddXmlRpcCommand.java,v 1.2 2006/09/23 00:08:45 grappendorf Exp $
 */
public class AddXmlRpcCommand extends Command
{
	/**
	 * Create a new AddXmlRpcCommand.
	 */
	public AddXmlRpcCommand()
	{
		super("aktario-xmlrpc.AddXmlRpcCommand");
	}

	/**
	 * Execute the command.
	 */
	public void perform()
	{
		Object command = properties.get("command");

		if (command == null)
		{
			Log.logError("AddXmlRpcCommand", "perform", "No command specified");

			return;
		}

		String commandName = null;

		if (command instanceof String)
		{
			commandName = (String) command;
		}
		else if (command instanceof Command)
		{
			commandName = ((Command) command).getTypeId();
		}
		else
		{
			Log.logError("AddXmlRpcCommand", "perform", "Type of paramter 'command' must be 'String' or 'Command'");

			return;
		}

		String xmlRpcMethodName = (String) properties.get("name");

		if (xmlRpcMethodName == null)
		{
			xmlRpcMethodName = commandName;
		}

		((AktarioXmlRpcManager) Engine.instance().getManager("AktarioXmlRpcManager")).addXmlRpcCommand(
						xmlRpcMethodName, commandName);
	}
}
