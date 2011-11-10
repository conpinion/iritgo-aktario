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

package de.iritgo.aktario.framework.server.cadencemanager;


import de.iritgo.aktario.framework.user.User;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 *
 */
public class CadeneContextRegistry
{
	//	Attribute
	private Map turnContexts;

	/**
	 * Constructor
	 *
	 */
	public CadeneContextRegistry()
	{
		turnContexts = new HashMap();
	}

	/**
	 * Add a CadenceContext.
	 */
	public void add(CadenceContext turnContext)
	{
		turnContexts.put(turnContext.getUser(), turnContext);
	}

	/**
	 * Get a CadenceContext.
	 */
	public CadenceContext get(User user)
	{
		return (CadenceContext) turnContexts.get(user);
	}

	/**
	 * Remove a CadenceContext.
	 */
	public void remove(CadenceContext turnContext)
	{
		turnContexts.remove(turnContext.getUser());
	}

	/**
	 * Remove a CadenceContext.
	 */
	public void remove(User user)
	{
		turnContexts.remove(user);
	}

	/**
	 * Remove all turncontexts.
	 */
	public void clear()
	{
		turnContexts.clear();
	}

	public Iterator getTurnContextIterator()
	{
		return turnContexts.values().iterator();
	}
}
