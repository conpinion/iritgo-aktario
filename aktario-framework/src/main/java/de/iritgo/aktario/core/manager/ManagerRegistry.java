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

package de.iritgo.aktario.core.manager;


import java.util.HashMap;


/**
 * This registry contains all managers knwon to the system.
 */
public class ManagerRegistry
{
	/** The managers stored by its id. */
	private HashMap managers;

	/**
	 * Create a new empty manager registry.
	 */
	public ManagerRegistry ()
	{
		managers = new HashMap ();
	}

	/**
	 * Add a manager to the registry.
	 *
	 * @param manager The manager to add.
	 */
	public void addManager (Manager manager)
	{
		managers.put (manager.getTypeId (), manager);
	}

	/**
	 * Retrieve a manager from the registry.
	 *
	 * @param id The id of the manager to retrieve.
	 * @return The manager or null if none was found.
	 */
	public Manager getManager (String id)
	{
		return (Manager) managers.get (id);
	}

	/**
	 * Remove a manager from the registry.
	 *
	 * @param manager The manager to remove.
	 */
	public void remove (Manager manager)
	{
		managers.remove (manager.getTypeId ());
	}
}
