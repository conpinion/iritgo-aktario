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

package de.iritgo.aktario.participant.gui;


import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.framework.command.CommandTools;
import de.iritgo.aktario.framework.dataobject.DynDataObject;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Properties;


/**
 * Render the online state in the participant pane
 *
 * @version $Id: RenderNameStateCommand.java,v 1.8 2006/09/25 10:34:30 grappendorf Exp $
 */
public class RenderNameStateCommand extends Command
{
	private ImageIcon userIsFreeForCall;

	private ImageIcon userHasACall;

	/**
	 * Create a new startup command.
	 */
	public RenderNameStateCommand()
	{
		super("RenderNameStateCommand");
	}

	/**
	 *
	 */
	public Object performWithResult()
	{
		final String participantStateName = properties.getProperty("participantStateName");

		DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer()
		{
			Color disabledFg;

			Font enabledFont;

			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
							boolean hasFocus, int row, int column)
			{
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

				Properties props = new Properties();

				props.put("row", new Integer(row));
				props.put("participantStateName", participantStateName);

				DynDataObject participantState = (DynDataObject) CommandTools.performSimple(
								"aktario-participant.GetParticipantState", props);

				if (participantState.getIntAttribute("onlineUser") == 1)
				{
					enabledFont = new Font(getFont().getFamily(), Font.BOLD, getFont().getSize());
					setFont(enabledFont);
				}

				if (disabledFg == null)
				{
					disabledFg = new Color((getForeground().getRed() + getBackground().getRed()) / 2, (getForeground()
									.getGreen() + getBackground().getGreen()) / 2,
									(getForeground().getBlue() + getBackground().getBlue()) / 2);
				}

				if (! isSelected)
				{
					setForeground(disabledFg);
				}

				return this;
			}
		};

		return defaultTableCellRenderer;
	}
}
