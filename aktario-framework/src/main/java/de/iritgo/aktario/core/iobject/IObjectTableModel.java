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
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;


/**
 *
 */
public abstract class IObjectTableModel extends AbstractTableModel implements IObjectProxyListener
{
	/** */
	private static final long serialVersionUID = 1L;

	/** List item map. */
	private Map mapping;

	/** IObject list. */
	private LinkedList list;

	/**
	 * Create a new IObjectTableModel.
	 */
	public IObjectTableModel()
	{
		super();
		mapping = new HashMap();
	}

	/**
	 * Get the total number of rows.
	 *
	 * @return The row count.
	 */
	public int getRowCount()
	{
		if (list != null)
		{
			return list.size();
		}

		return 0;
	}

	/**
	 * The IObjectProxyListener, called if the basicobject is a fresh object or the update process is working.
	 *
	 * @param event The EventOject.
	 */
	public void proxyEvent(IObjectProxyEvent event)
	{
		if (event.isWaitingForNewObject())
		{
			return;
		}

		final IObjectTableModelItem item = (IObjectTableModelItem) mapping.get(event.getObject());
		int row = list.indexOf(event.getObject());

		if (row >= 0)
		{
			list.set(row, event.getObject());
		}
		else
		{
			return;
		}

		try
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					fireTableRowsUpdated(item.row, item.row);
				}
			});
		}
		catch (Exception x)
		{
		}
	}

	/**
	 * Dispose the model.
	 */
	public void dispose()
	{
		for (Iterator i = mapping.keySet().iterator(); i.hasNext();)
		{
			IObject prototypeable = (IObject) i.next();

			Engine.instance().getProxyEventRegistry().removeEventListener(prototypeable, this);
		}

		mapping.clear();
	}

	/**
	 * Update the model with a new iobject list.
	 *
	 * @param linkedList The new list.
	 */
	public void update(IObjectList linkedList)
	{
		dispose();

		this.list = new LinkedList();

		int row = 0;

		for (IObjectIterator i = (IObjectIterator) linkedList.iterator(); i.hasNext();)
		{
			IObject object = (IObject) i.next(this);
			long uniqueId = object.getUniqueId();

			list.add(object);
			mapping.put(object, new IObjectTableModelItem(row, uniqueId));

			fireTableDataChanged();
			++row;
		}
	}

	public LinkedList getModelList()
	{
		return list;
	}

	public IObject getObjectByRow(int row)
	{
		return (IObject) list.get(row);
	}
}
