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


import de.iritgo.aktario.core.base.BaseObject;
import java.util.HashMap;


/**
 * This registry contains all known command processors.
 */
public class CommandProcessorRegistry extends BaseObject
{
	/** All known command processors. */
	private HashMap commandProcessors;

	/**
	 * Create a new CommandProcessorRegistry.
	 */
	public CommandProcessorRegistry()
	{
		super("commandprocessorregistry");
		commandProcessors = new HashMap();
	}

	/**
	 * Add a command processor.
	 *
	 * @param commandProcessor The command processor to add.
	 */
	public void add(CommandProcessor commandProcessor)
	{
		commandProcessors.put(commandProcessor.getTypeId(), commandProcessor);
	}

	/**
	 * Get a command processor by it's id.
	 *
	 * @param id The id of the command processor.
	 */
	public CommandProcessor get(String id)
	{
		return (CommandProcessor) commandProcessors.get(id);
	}

	/**
	 * Remove a command processor.
	 *
	 * @param commandProcessor The command processor to add.
	 */
	public void remove(CommandProcessor commandProcessor)
	{
		commandProcessors.remove(commandProcessor.getTypeId());
	}

	/**
	 * Checks the exitence of a command processor.
	 *
	 * @param id The id of the command processor to check.
	 * @return True if the command processor exists.
	 */
	public boolean exists(String id)
	{
		return commandProcessors.containsKey(id);
	}

	/**
	 * Clear the registry.
	 */
	public void clear()
	{
		commandProcessors.clear();
	}
}
