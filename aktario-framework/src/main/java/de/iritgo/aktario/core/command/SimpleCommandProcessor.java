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

package de.iritgo.aktario.core.command;


/**
 * A simple command processor directly executes commands in the current
 * thread.
 */
public class SimpleCommandProcessor implements CommandProcessor
{
	/**
	 * Get the type id.
	 *
	 * @return The type id.
	 */
	public String getTypeId ()
	{
		return "SimpleCommandProcessor";
	}

	/**
	 * Perform a command.
	 *
	 * @param command The command to execute.
	 * @return The command results.
	 */
	public Object perform (Command command)
	{
		if (command.canPerform ())
		{
			return command.performWithResult ();
		}

		return null;
	}
}
