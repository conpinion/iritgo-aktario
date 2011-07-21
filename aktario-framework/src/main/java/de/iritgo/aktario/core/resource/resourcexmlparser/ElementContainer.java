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

package de.iritgo.aktario.core.resource.resourcexmlparser;


import org.jdom.Element;


/**
 *
 */
public class ElementContainer
{
	private int pos = 0;

	private Element element;

	private String path;

	/**
	 * Constructor.
	 */
	public ElementContainer (Element element, int pos, String path)
	{
		this.element = element;
		this.pos = pos;
		this.path = path;
	}

	public int getPos ()
	{
		return pos;
	}

	public Element getElement ()
	{
		return element;
	}

	public String getPath ()
	{
		return path;
	}
}
