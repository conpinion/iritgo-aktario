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

package de.iritgo.aktario.betwixt.baseobjectsave;


import java.util.LinkedList;
import java.util.List;


public class Context
{
	private String id;

	private String string;

	private String proxyLinkedListKey;

	private List childs;

	public Context()
	{
		childs = new LinkedList();
	}

	public Context(String id, String string)
	{
		this();
		this.id = id;
		this.string = string;
	}

	public Context(String proxyLinkedListKey, String id, String string)
	{
		this();
		this.proxyLinkedListKey = proxyLinkedListKey;
		this.id = id;
		this.string = string;
	}

	public String getId()
	{
		return id;
	}

	public void setProxyLinkedListKey(String proxyLinkedListKey)
	{
		this.proxyLinkedListKey = proxyLinkedListKey;
	}

	public String getProxyLinkedListKey()
	{
		return proxyLinkedListKey;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getString()
	{
		if (string == null)
		{
			return "";
		}

		return string;
	}

	public void setString(String string)
	{
		this.string = string;
	}

	public List getChilds()
	{
		return childs;
	}

	public void addChild(Context context)
	{
		childs.add(context);
	}
}
