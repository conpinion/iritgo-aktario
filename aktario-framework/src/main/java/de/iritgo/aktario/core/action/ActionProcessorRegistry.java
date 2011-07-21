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

package de.iritgo.aktario.core.action;


import java.util.HashMap;
import java.util.Map;


/**
 * A registry of action processors.
 */
public class ActionProcessorRegistry
{
	/** Action processors */
	private Map actionProcessors;

	/**
	 * Create a new ActionProcessorRegistry.
	 */
	public ActionProcessorRegistry ()
	{
		actionProcessors = new HashMap ();
	}

	/**
	 * Add an action processor.
	 *
	 * @param processor The action processor.
	 */
	public void put (ActionProcessor processor)
	{
		actionProcessors.put (processor.getTypeId (), processor);
	}

	/**
	 * Get an action processor.
	 *
	 * @param id The id of the action processor.
	 * @return The action processor
	 */
	public ActionProcessor get (String id)
	{
		return (ActionProcessor) actionProcessors.get (id);
	}

	/**
	 * Remove an action processor.
	 *
	 * @param processor Type action processor to remove.
	 */
	public void remove (ActionProcessor processor)
	{
		actionProcessors.remove (processor.getTypeId ());
		processor.close ();
	}
}
