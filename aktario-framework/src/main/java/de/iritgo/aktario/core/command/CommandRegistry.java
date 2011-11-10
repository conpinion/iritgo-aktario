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


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.logger.Log;
import java.util.HashMap;
import java.util.Properties;


/**
 * This registry contains all known commands.
 */
public class CommandRegistry extends BaseObject
{
	/** All commands stored in the registry. */
	private HashMap commands;

	/**
	 * Create a new empty command registry.
	 */
	public CommandRegistry()
	{
		super("commandregistry");
		commands = new HashMap();
	}

	/**
	 * Add a command to the registry.
	 *
	 * @param command The command to add.
	 */
	public void add(Command command)
	{
		commands.put(command.getTypeId(), command);
	}

	/**
	 * Retrieve a command from the registry.
	 *
	 * @param commandId The id of the command to retrieve.
	 */
	public Command get(String commandId)
	{
		Command command = (Command) commands.get(commandId);

		if (command == null)
		{
			Log.logError("system", "CommandRegistry", "Attempting to get not existing command '" + commandId + "'");
		}

		return command;
	}

	/**
	 * Remove a command from the registry.
	 *
	 * @param command The command to remove.
	 */
	public void remove(Command command)
	{
		commands.remove(command.getTypeId());
	}

	/**
	 * Checks wether a command exists or not.
	 *
	 * @param commandId The id of the command to search.
	 * @return True if the command was found.
	 */
	public boolean commandExists(String commandId)
	{
		return commands.containsKey(commandId);
	}

	/**
	 * Perform a command.
	 *
	 * @param commandProcessor The command processor used to execute the command.
	 * @param commandId The id of the command to execute.
	 * @param properties The execution properties.
	 */
	public Object perform(String commandProcessor, String commandId, Properties properties)
	{
		Command cmd = (Command) get(commandId);

		if (cmd == null)
		{
			return null;
		}

		cmd.setProperties(properties);

		CommandProcessor cmdProc = Engine.instance().getCommandProcessorRegistry().get(commandProcessor);

		return cmdProc.perform(cmd);
	}

	/**
	 * Perform a command.
	 *
	 * @param commandProcessor The command processor used to execute the command.
	 * @param commandId The id of the command to execute.
	 * @param properties The execution properties.
	 * @return The command results.
	 */
	public Object perform(String commandProcessor, String commandId, Object[] properties)
	{
		Command cmd = (Command) get(commandId);

		if (cmd == null)
		{
			return null;
		}

		return perform(commandProcessor, cmd, properties);
	}

	/**
	 * Perform a command.
	 *
	 * @param commandProcessor The command processor used to execute the command.
	 * @param command The command to execute.
	 * @param properties The execution properties.
	 * @return The command results.
	 */
	public Object perform(String commandProcessor, Command command, Properties properties)
	{
		command.setProperties(properties);

		CommandProcessor cmdProc = Engine.instance().getCommandProcessorRegistry().get(commandProcessor);

		return cmdProc.perform(command);
	}

	/**
	 * Perform a command.
	 *
	 * @param commandProcessor The command processor used to execute the command.
	 * @param command The id of the command to execute.
	 * @param properties The execution properties.
	 * @return The command results.
	 */
	public Object perform(String commandProcessor, Command command, Object[] properties)
	{
		int numPropElements = properties.length;

		if (numPropElements % 2 == 1)
		{
			--numPropElements;
		}

		Properties props = new Properties();

		for (int i = 0; i < numPropElements; ++i)
		{
			props.put(properties[i], properties[i + 1]);
			++i;
		}

		command.setProperties(props);

		CommandProcessor cmdProc = Engine.instance().getCommandProcessorRegistry().get(commandProcessor);

		return cmdProc.perform(command);
	}

	/**
	 * Perform a command.
	 *
	 * @param commandProcessor The command processor used to execute the command.
	 * @param commandId The id of the command to execute.
	 * @return The command results.
	 */
	public Object perform(String commandProcessor, String commandId)
	{
		Command cmd = (Command) get(commandId);

		if (cmd == null)
		{
			return null;
		}

		CommandProcessor cmdProc = Engine.instance().getCommandProcessorRegistry().get(commandProcessor);

		return cmdProc.perform(cmd);
	}

	/**
	 * Perform a command.
	 *
	 * @param commandProcessor The command processor used to execute the command.
	 * @param command The command to execute.
	 * @return The command results.
	 */
	public Object perform(String commandProcessor, Command command)
	{
		CommandProcessor cmdProc = Engine.instance().getCommandProcessorRegistry().get(commandProcessor);

		return cmdProc.perform(command);
	}

	/**
	 * Remove all commands from the registry.
	 */
	public void clear()
	{
		commands.clear();
	}
}
