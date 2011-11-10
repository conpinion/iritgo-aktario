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


import de.iritgo.aktario.core.io.i18n.I18NString;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import java.awt.event.MouseEvent;


/**
 * An ITableColumn describes the visualization of a table column (it's header
 * and it's cells) and defines the code to execute on the events that can happen
 * to it.
 */
public class ITableColumn
{
	/** Icon to display in a single table cell */
	private ImageIcon cellIcon;

	/** The header title */
	private I18NString title = new I18NString();

	public ImageIcon getCellIcon()
	{
		return cellIcon;
	}

	public void setCellIcon(ImageIcon cellIcon)
	{
		this.cellIcon = cellIcon;
	}

	public ITableColumn withCellIcon(ImageIcon cellIcon)
	{
		setCellIcon(cellIcon);

		return this;
	}

	public I18NString getTitle()
	{
		return title;
	}

	public void setTitle(I18NString title)
	{
		this.title = title;
	}

	public ITableColumn withTitle(I18NString title)
	{
		setTitle(title);

		return this;
	}

	public ITableColumn withTitle(String title)
	{
		setTitle(new I18NString(title));

		return this;
	}

	/**
	 * Override this method to perform an action if a cell in this column was
	 * clicked. The handler is passed the row item.
	 *
	 * @param item The item of the row that was clicked on
	 * @param addressTable The table in which the click event occurred
	 * @param e The mouse event that triggered this handler
	 */
	public void onCellClicked(Object item, JTable addressTable, MouseEvent e)
	{
	}
}
