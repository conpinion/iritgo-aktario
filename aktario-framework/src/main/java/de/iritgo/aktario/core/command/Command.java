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
import java.util.Properties;


/**
 * This class implements the command pattern.
 */
public class Command extends BaseObject
{
	/** Command execution properties. */
	protected Properties properties;

	/**
	 * Create a new anonymous command.
	 */
	public Command ()
	{
	}

	/**
	 * Create a new command with a specific type id.
	 *
	 * @param typeId The command id.
	 */
	public Command (String typeId)
	{
		super (typeId);
	}

	/**
	 * Set the command properties.
	 *
	 * @param properties The new command properties.
	 */
	public void setProperties (Properties properties)
	{
		this.properties = properties;
	}

	/**
	 * Get the command properties.
	 *
	 * @return The command properties.
	 */
	public Properties getProperties ()
	{
		return properties;
	}

	/**
	 * Do whatever the command needs to do.
	 * Subclasses should override this method to provide custom command code.
	 * Override this method if you do not want to return a command result (The
	 * return value defaults to null).
	 */
	public void perform ()
	{
	}

	/**
	 * Do whatever the command needs to do.
	 * Subclasses should override this method to provide custom command code.
	 * Override this method if you want to return a command result.
	 *
	 * @return The command results.
	 */
	public Object performWithResult ()
	{
		perform ();

		return null;
	}

	/**
	 * Check wether the command can currently be executed or not.
	 * By default commands are executable. Subclasses should provide a
	 * reasonable implementation of this method.
	 *
	 * @return True if the command can be executed.
	 */
	public boolean canPerform ()
	{
		return true;
	}
}
