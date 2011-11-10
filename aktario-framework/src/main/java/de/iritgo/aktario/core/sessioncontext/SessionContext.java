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

package de.iritgo.aktario.core.sessioncontext;


import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.logger.Log;
import java.util.HashMap;
import java.util.Iterator;


/**
 *
 */
public class SessionContext extends BaseObject
{
	//	Attribute
	private HashMap sessionContexts;

	/**
	 * Constructor
	 *
	 */
	public SessionContext(String id)
	{
		super(id);
		sessionContexts = new HashMap();
	}

	/**
	 * Add a baseObject.
	 *
	 * @param baseObject
	 */
	public void add(BaseObject baseObject)
	{
		sessionContexts.put(baseObject.getTypeId(), baseObject);
	}

	/**
	 * Add a baseObject.
	 *
	 * @param baseObject
	 */
	public void add(String id, BaseObject baseObject)
	{
		sessionContexts.put(id, baseObject);
	}

	/**
	 * Get a baseObject.
	 *
	 * @param id The id of the baseObject.
	 */
	public BaseObject get(String id)
	{
		BaseObject baseObject = (BaseObject) sessionContexts.get(id);

		if (baseObject == null)
		{
			Log.log("system", "SessionContext.get", "Key not found: " + id, Log.WARN);
		}

		return baseObject;
	}

	/**
	 * Remove a baseObject.
	 */
	public void remove(BaseObject baseObject)
	{
		sessionContexts.remove(baseObject.getTypeId());
	}

	/**
	 * Remove a baseObject.
	 */
	public void remove(String id)
	{
		sessionContexts.remove(id);
	}

	/**
	 * get the values iterator
	 */
	public Iterator getValueIterator()
	{
		return sessionContexts.values().iterator();
	}

	/**
	 * Checks the object exists
	 *
	 * @param id The id of the rule.
	 */
	public boolean baseObjectExists(String id)
	{
		return sessionContexts.containsKey(id);
	}

	/**
	 * Checks the object exists
	 *
	 * @param id The id of the rule.
	 */
	public boolean contains(String id)
	{
		return sessionContexts.containsKey(id);
	}

	/**
	 * clean sessioncontext
	 */
	public void cleanSessionContext()
	{
		sessionContexts.clear();
	}
}
