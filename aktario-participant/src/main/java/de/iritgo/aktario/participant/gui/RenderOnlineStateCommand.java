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
import de.iritgo.aktario.participant.ParticipantPlugin;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;


/**
 * Render the online state in the participant pane
 *
 * @version $Id: RenderOnlineStateCommand.java,v 1.8 2006/09/25 10:34:30 grappendorf Exp $
 */
public class RenderOnlineStateCommand extends Command
{
	private ImageIcon userIsFreeForCall;

	private ImageIcon userHasACall;

	/**
	 * Create a new startup command.
	 */
	public RenderOnlineStateCommand ()
	{
		super ("RenderOnlineStateCommand");
		userIsFreeForCall = new ImageIcon (ParticipantPlugin.class.getResource ("/resources/user-offline.png"));
		userHasACall = new ImageIcon (ParticipantPlugin.class.getResource ("/resources/user-online.png"));
	}

	/**
	 *
	 */
	public Object performWithResult ()
	{
		DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer ()
		{
			public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected,
							boolean hasFocus, int row, int column)
			{
				setText ("");

				int state = ((Integer) value).intValue ();

				if (state == 0)
				{
					setIcon (userIsFreeForCall);
				}
				else
				{
					setIcon (userHasACall);
				}

				table.getColumnModel ().getColumn (column).setMinWidth (userIsFreeForCall.getIconWidth ());
				table.getColumnModel ().getColumn (column).setMaxWidth (userIsFreeForCall.getIconWidth () + 4);

				return this;
			}
		};

		return defaultTableCellRenderer;
	}
}
