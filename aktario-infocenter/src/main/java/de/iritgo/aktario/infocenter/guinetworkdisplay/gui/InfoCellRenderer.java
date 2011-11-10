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

package de.iritgo.aktario.infocenter.guinetworkdisplay.gui;


import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;


/**
 *
 */
public class InfoCellRenderer implements TableCellRenderer
{
	private JPanel panel;

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
					int row, int column)
	{
		InfoItem infoItem = (InfoItem) value;

		panel = infoItem.getPanel();

		if (isSelected)
		{
			panel.setBackground(table.getSelectionBackground());
			panel.setForeground(table.getSelectionForeground());
		}
		else
		{
			panel.setBackground(table.getBackground());
			panel.setForeground(table.getForeground());
		}

		panel.setEnabled(table.isEnabled());
		panel.setFont(table.getFont());
		panel.setOpaque(true);

		return panel;
	}
}
