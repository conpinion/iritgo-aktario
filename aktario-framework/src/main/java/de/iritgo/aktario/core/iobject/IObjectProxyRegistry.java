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

package de.iritgo.aktario.core.iobject;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * The IObjectProxyRegistry, registriert und feuert events.
 */
public class IObjectProxyRegistry
{
	//	Attribute
	private HashMap typeProxyMapping;

	/**
	 * Constructor
	 *
	 */
	public IObjectProxyRegistry()
	{
		typeProxyMapping = new HashMap();
	}

	/**
	 * Add a Proxy.
	 */
	public void addProxy(IObjectProxy proxy, String typeId)
	{
		Map proxys = (Map) typeProxyMapping.get(typeId);

		if (proxys == null)
		{
			proxys = new HashMap();
			typeProxyMapping.put(typeId, proxys);
		}

		proxys.put(new Long(proxy.getUniqueId()), proxy);
	}

	/**
	 * Get a Proxy.
	 *
	 * @param uniqueId The uniqueid of the proxy.
	 */
	public IObjectProxy getProxy(long uniqueId, String typeId)
	{
		Map proxys = (Map) typeProxyMapping.get(typeId);

		if (proxys == null)
		{
			return null;
		}

		IObjectProxy proxy = (IObjectProxy) proxys.get(new Long(uniqueId));

		return proxy;
	}

	/**
	 * Remove a Proxy.
	 */
	public void removeProxy(IObjectProxy proxy, String typeId)
	{
		if (proxy == null)
			return;

		Map proxys = (Map) typeProxyMapping.get(typeId);

		if (proxys == null)
		{
			return;
		}

		proxys.remove(new Long(proxy.getUniqueId()));
	}

	public void setInvalidState()
	{
		for (Iterator i = typeProxyMapping.values().iterator(); i.hasNext();)
		{
			Map proxys = (Map) i.next();

			for (Iterator j = proxys.values().iterator(); j.hasNext();)
			{
				IObjectProxy proxy = (IObjectProxy) j.next();

				proxy.setUpToDate(false);
			}
		}
	}

	public void clear()
	{
		typeProxyMapping.clear();
	}
}
