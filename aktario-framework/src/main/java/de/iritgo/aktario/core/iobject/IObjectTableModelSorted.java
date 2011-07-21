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
import de.iritgo.aktario.core.gui.ITableSorter;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;


/**
 *
 */
public abstract class IObjectTableModelSorted extends AbstractTableModel implements IObjectProxyListener
{
	/** */
	private static final long serialVersionUID = 1L;

	/** List item map. */
	private Map mapping;

	/** IObject list. */
	protected LinkedList list;

	private ITableSorter tableSorter;

	/**
	 * Create a new IObjectTableModel.
	 */
	public IObjectTableModelSorted ()
	{
		super ();
		mapping = new HashMap ();
		this.tableSorter = new ITableSorter (this);
	}

	public ITableSorter getTableSorter ()
	{
		return tableSorter;
	}

	/**
	 * Get the total number of rows.
	 *
	 * @return The row count.
	 */
	public int getRowCount ()
	{
		if (list != null)
		{
			return list.size ();
		}

		return 0;
	}

	/**
	 * The IObjectProxyListener, called if the basicobject is a fresh object or the update process is working.
	 *
	 * @param event The EventOject.
	 */
	public void proxyEvent (IObjectProxyEvent event)
	{
		if (event.isWaitingForNewObject ())
		{
			return;
		}

		final IObjectTableModelItem item = (IObjectTableModelItem) mapping.get (event.getObject ());

		if (item.row >= 0)
		{
			list.set (item.row, event.getObject ());
		}
		else
		{
			return;
		}

		try
		{
			SwingUtilities.invokeLater (new Runnable ()
			{
				public void run ()
				{
					fireTableRowsUpdated (tableSorter.getRealRow (item.row), tableSorter.getRealRow (item.row));
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
	public void dispose ()
	{
		for (Iterator i = mapping.keySet ().iterator (); i.hasNext ();)
		{
			IObject prototypeable = (IObject) i.next ();

			Engine.instance ().getProxyEventRegistry ().removeEventListener (prototypeable, this);
		}

		mapping.clear ();
	}

	/**
	 * Update the model with a new iobject list.
	 *
	 * @param linkedList The new list.
	 * @param transaction The current transaction.
	 */
	public void update (IObjectList linkedList)
	{
		dispose ();

		this.list = new LinkedList ();

		int row = 0;

		for (IObjectIterator i = (IObjectIterator) linkedList.iterator (); i.hasNext ();)
		{
			IObject object = (IObject) i.next (this);
			long uniqueId = object.getUniqueId ();

			list.add (object);
			mapping.put (object, new IObjectTableModelItem (row, uniqueId));
			// 			Engine.instance ().getProxyEventRegistry ().addEventListener (object, this);
			tableSorter.reallocateIndexesUpdate ();
			fireTableDataChanged ();
			++row;
		}
	}

	public IObject getObjectByRow (int row)
	{
		return (IObject) list.get (row);
	}

	public IObject getRealObjectByRow (int row)
	{
		return (IObject) tableSorter.getObjectByRow (row);
	}

	@SuppressWarnings("unused")
	private void dump (IObject object)
	{
		System.out.println ("TableModelDump:");
		System.out.println ("----------------");
		System.out.println (object.dump ());
	}
}
