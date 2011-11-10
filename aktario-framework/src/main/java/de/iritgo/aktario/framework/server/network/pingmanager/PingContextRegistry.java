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

package de.iritgo.aktario.framework.server.network.pingmanager;


import java.util.HashMap;
import java.util.Iterator;


/**
 *
 */
public class PingContextRegistry
{
	//	Attribute
	private HashMap pingContexts;

	/**
	 * Constructor
	 *
	 */
	public PingContextRegistry()
	{
		pingContexts = new HashMap();
	}

	/**
	 * Add a PingContext.
	 */
	public void add(PingContext pingContext)
	{
		pingContexts.put(pingContext.getTypeId(), pingContext);
	}

	/**
	 * Get a PingContext.
	 */
	public PingContext get(String id)
	{
		return (PingContext) pingContexts.get(id);
	}

	/**
	 * Remove a PingContext.
	 */
	public void remove(PingContext pingContext)
	{
		pingContexts.remove(pingContext.getTypeId());
	}

	/**
	 * Remove a PingContext.
	 */
	public void remove(String id)
	{
		pingContexts.remove(id);
	}

	/**
	 * Remove all pingcontexts.
	 */
	public void clear()
	{
		pingContexts.clear();
	}

	public Iterator getPingContextIterator()
	{
		return pingContexts.values().iterator();
	}
}
