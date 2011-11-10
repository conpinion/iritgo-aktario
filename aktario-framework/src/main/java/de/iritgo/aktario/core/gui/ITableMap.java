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

package de.iritgo.aktario.core.gui;


import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;


public class ITableMap extends AbstractTableModel implements TableModelListener
{
	/** */
	private static final long serialVersionUID = 1L;

	protected TableModel model;

	public TableModel getModel()
	{
		return model;
	}

	public void setModel(TableModel model)
	{
		this.model = model;
	}

	public Object getValueAt(int aRow, int aColumn)
	{
		return model.getValueAt(aRow, aColumn);
	}

	@Override
	public void setValueAt(Object aValue, int aRow, int aColumn)
	{
		model.setValueAt(aValue, aRow, aColumn);
	}

	public int getRowCount()
	{
		return (model == null) ? 0 : model.getRowCount();
	}

	public int getColumnCount()
	{
		return (model == null) ? 0 : model.getColumnCount();
	}

	@Override
	public String getColumnName(int aColumn)
	{
		return model.getColumnName(aColumn);
	}

	@Override
	public Class getColumnClass(int aColumn)
	{
		return model.getColumnClass(aColumn);
	}

	@Override
	public boolean isCellEditable(int row, int column)
	{
		return model.isCellEditable(row, column);
	}

	public void tableChanged(TableModelEvent e)
	{
		fireTableChanged(e);
	}
}
