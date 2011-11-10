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


import de.iritgo.aktario.core.Engine;
import de.iritgo.simplelife.collection.JointIterator;
import de.iritgo.simplelife.math.IntRange;
import de.iritgo.simplelife.math.Pair;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 *
 */
public class IObjectComboBoxModel extends AbstractListModel implements ComboBoxModel, IObjectProxyListener
{
	/** */
	private static final long serialVersionUID = 1L;

	/** List item map. */
	private Map<IObject, Integer> mapping = new HashMap();

	/** IObject list. */
	private IObjectList list;

	/** The currently selected item. */
	private IObject selectedItem;

	public Object getElementAt(int index)
	{
		if (index >= 0 && index < list.size())
		{
			return list.get(index);
		}
		else
		{
			return null;
		}
	}

	public int getSize()
	{
		return list != null ? list.size() : 0;
	}

	public Object getSelectedItem()
	{
		return selectedItem;
	}

	public void setSelectedItem(Object anItem)
	{
		if ((selectedItem != null && ! selectedItem.equals(anItem)) || selectedItem == null && anItem != null)
		{
			selectedItem = (IObject) anItem;
			fireContentsChanged(this, - 1, - 1);
		}
	}

	public void proxyEvent(IObjectProxyEvent event)
	{
		if (event.isWaitingForNewObject())
		{
			return;
		}

		int index = ((Integer) mapping.get(event.getObject())).intValue();

		fireContentsChanged(this, index, index);
	}

	public void update(IObjectList list)
	{
		if (this.list != null && this.list.size() > 0)
		{
			fireIntervalRemoved(this, 0, this.list.size() - 1);
		}

		this.list = list;
		selectedItem = null;

		for (Iterator i = mapping.keySet().iterator(); i.hasNext();)
		{
			IObject iobject = (IObject) i.next();

			Engine.instance().getProxyEventRegistry().removeEventListener(iobject, this);
		}

		mapping.clear();

		if (list == null || list.size() == 0)
		{
			return;
		}

		for (Pair<IObject, Integer> p : new JointIterator<IObject, Integer>(list, IntRange.N0()))
		{
			mapping.put(p.get1(), p.get2());
			Engine.instance().getProxyEventRegistry().addEventListener(p.get1(), this);
		}

		if (list.size() > 0)
		{
			fireIntervalAdded(this, 0, list.size() - 1);
		}
	}
}
