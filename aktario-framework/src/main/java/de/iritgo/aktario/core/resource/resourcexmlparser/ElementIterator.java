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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 *
 */
public class ElementIterator implements Iterator
{
	private int pos = 0;

	private Element element;

	private List elementList = null;

	private LinkedList stack = new LinkedList ();

	private String path = "";

	private ElementContainer elementContainer = null;

	public ElementIterator (Element element)
	{
		//		this.element = (Element) element.getChildren ().get (0);
		this.element = (Element) element;
		this.elementList = this.element.getChildren ();
		elementContainer = new ElementContainer (this.element, 0, this.element.getAttribute ("treename").getValue ());
		path = this.element.getAttribute ("treename").getValue ();
	}

	public boolean hasNext ()
	{
		return (pos < elementList.size () || (stack.size () > 0));
	}

	public Object next ()
	{
		if (pos < elementList.size ())
		{
			stack.addFirst (new ElementContainer (element, pos, path));
			element = (Element) elementList.get (pos);

			if (element.getAttribute ("treename") != null)
			{
				path = path + "." + element.getAttribute ("treename").getValue ();
			}
			else
			{
				path = path + ".classdev";
			}

			elementList = element.getChildren ();
			pos = 0;
			elementContainer = new ElementContainer (element, pos, path);

			return elementContainer;
		}

		if ((stack.size () > 0) && (pos >= elementList.size ()))
		{
			elementContainer = (ElementContainer) stack.removeFirst ();
			pos = elementContainer.getPos ();
			path = elementContainer.getPath ();
			pos++;
			element = elementContainer.getElement ();
			elementList = element.getChildren ();
		}

		return elementContainer;
	}

	public Object current ()
	{
		return elementContainer;
	}

	public void remove ()
	{
	}
}
