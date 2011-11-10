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

package de.iritgo.aktario.framework.command;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.core.command.CommandProcessor;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.base.action.CommandServerAction;
import java.util.Properties;
import java.util.Vector;


/**
 * Utility methods for easier command handling.
 */
public class CommandTools
{
	/**
	 * Check for the existence of a specific command.
	 *
	 * @param commandId The id of the command to check.
	 * @return True if the command exists.
	 */
	public static boolean commandExists(String commandId)
	{
		return Engine.instance().getCommandRegistry().commandExists(commandId);
	}

	/**
	 * Perform a command using the simple command processor of the currently
	 * running engine.
	 *
	 * @param commandId The id of the command to execute.
	 * @param properties The execution properties.
	 */
	public static Object performSimple(String commandId, Properties properties)
	{
		return Engine.instance().getCommandRegistry().perform(CommandProcessor.SIMPLE, commandId, properties);
	}

	/**
	 * Perform a command using the simple command processor of the currently
	 * running engine.
	 *
	 * @param commandId The id of the command to execute.
	 * @param properties The execution properties.
	 * @return The command results.
	 */
	public static Object performSimple(String commandId, Object[] properties)
	{
		return Engine.instance().getCommandRegistry().perform(CommandProcessor.SIMPLE, commandId, properties);
	}

	/**
	 * Perform a command using the simple command processor of the currently
	 * running engine.
	 *
	 * @param commandId The id of the command to execute.
	 * @param properties The execution properties.
	 * @return The command results.
	 */
	public static Object performSimple(String commandId, Vector properties)
	{
		return performSimple(commandId, properties.toArray());
	}

	/**
	 * Perform a command using the simple command processor of the currently
	 * running engine.
	 *
	 * @param command The id of the command to execute.
	 * @param properties The execution properties.
	 * @return The command results.
	 */
	public static Object performSimple(Command command, Properties properties)
	{
		return Engine.instance().getCommandRegistry().perform(CommandProcessor.SIMPLE, command, properties);
	}

	/**
	 * Perform a command using the simple command processor of the currently
	 * running engine.
	 *
	 * @param command The id of the command to execute.
	 * @param properties The execution properties.
	 * @return The command results.
	 */
	public static Object performSimple(Command command, Object[] properties)
	{
		return Engine.instance().getCommandRegistry().perform(CommandProcessor.SIMPLE, command, properties);
	}

	/**
	 * Perform a command using the simple command processor of the currently
	 * running engine.
	 *
	 * @param command The id of the command to execute.
	 * @param properties The execution properties.
	 * @return The command results.
	 */
	public static Object performSimple(Command command, Vector properties)
	{
		return performSimple(command, properties.toArray());
	}

	/**
	 * Perform a command using the simple command processor of the currently
	 * running engine.
	 *
	 * @param commandId The id of the command to execute.
	 * @return The command results.
	 */
	public static Object performSimple(String commandId)
	{
		return Engine.instance().getCommandRegistry().perform(CommandProcessor.SIMPLE, commandId);
	}

	/**
	 * Perform a command using the simple command processor of the currently
	 * running engine.
	 *
	 * @param command The command to execute.
	 * @return The command results.
	 */
	public static Object performSimple(Command command)
	{
		return Engine.instance().getCommandRegistry().perform(CommandProcessor.SIMPLE, command);
	}

	/**
	 * Perform a command using the async command processor of the currently
	 * running engine.
	 *
	 * @param commandId The id of the command to execute.
	 * @param properties The execution properties.
	 */
	public static Object performAsync(String commandId, Properties properties)
	{
		return Engine.instance().getCommandRegistry().perform(CommandProcessor.ASYNC, commandId, properties);
	}

	/**
	 * Perform a command using the async command processor of the currently
	 * running engine.
	 *
	 * @param commandId The id of the command to execute.
	 * @param properties The execution properties.
	 * @return The command results.
	 */
	public static Object performAsync(String commandId, Object[] properties)
	{
		return Engine.instance().getCommandRegistry().perform(CommandProcessor.ASYNC, commandId, properties);
	}

	/**
	 * Perform a command using the async command processor of the currently
	 * running engine.
	 *
	 * @param commandId The id of the command to execute.
	 * @param properties The execution properties.
	 * @return The command results.
	 */
	public static Object performAsync(String commandId, Vector properties)
	{
		return performAsync(commandId, properties.toArray());
	}

	/**
	 * Perform a command using the async command processor of the currently
	 * running engine.
	 *
	 * @param command The id of the command to execute.
	 * @param properties The execution properties.
	 * @return The command results.
	 */
	public static Object performAsync(Command command, Properties properties)
	{
		return Engine.instance().getCommandRegistry().perform(CommandProcessor.ASYNC, command, properties);
	}

	/**
	 * Perform a command using the async command processor of the currently
	 * running engine.
	 *
	 * @param command The id of the command to execute.
	 * @param properties The execution properties.
	 * @return The command results.
	 */
	public static Object performAsync(Command command, Object[] properties)
	{
		return Engine.instance().getCommandRegistry().perform(CommandProcessor.ASYNC, command, properties);
	}

	/**
	 * Perform a command using the async command processor of the currently
	 * running engine.
	 *
	 * @param command The id of the command to execute.
	 * @param properties The execution properties.
	 * @return The command results.
	 */
	public static Object performAsync(Command command, Vector properties)
	{
		return performAsync(command, properties.toArray());
	}

	/**
	 * Perform a command using the async command processor of the currently
	 * running engine.
	 *
	 * @param commandId The id of the command to execute.
	 * @return The command results.
	 */
	public static Object performAsync(String commandId)
	{
		return Engine.instance().getCommandRegistry().perform(CommandProcessor.ASYNC, commandId);
	}

	/**
	 * Perform a command using the async command processor of the currently
	 * running engine.
	 *
	 * @param command The command to execute.
	 * @return The command results.
	 */
	public static Object performAsync(Command command)
	{
		return Engine.instance().getCommandRegistry().perform(CommandProcessor.ASYNC, command);
	}

	/**
	 * Perform a command on a other computer.
	 *
	 * @param commandId The command id to execute.
	 * @param properties The properties for this command.
	 */
	public static void performRemote(String commandId, Properties properties)
	{
		CommandServerAction csa = new CommandServerAction(commandId, properties);

		ActionTools.sendToServer(csa);
	}
}
