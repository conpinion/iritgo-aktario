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


import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;


/**
 *
 */
public class InfoItem
{
	private JPanel panel;

	private String guiPaneId;

	private long uniqueId;

	private String iObjectTypeId;

	public InfoItem (String category, String icon, String message, String guiPaneId, long uniqueId,
					String iObjectTypeId, int level)
	{
		init (category, icon, message, guiPaneId, uniqueId, iObjectTypeId, level);
	}

	public InfoItem (String message)
	{
		init ("", "", message, "", 0, "", 0);
	}

	public void init (String category, String icon, String message, String guiPaneId, long uniqueId,
					String iObjectTypeId, int level)
	{
		this.guiPaneId = guiPaneId;
		this.uniqueId = uniqueId;
		this.iObjectTypeId = iObjectTypeId;

		panel = new JPanel ();

		GridBagLayout gridBagLayout = new GridBagLayout ();

		panel.setLayout (gridBagLayout);

		int row = 0;

		JLabel label = new JLabel ("IC");

		panel
						.add (label, getConstraints (0, row, 1, 1, GridBagConstraints.HORIZONTAL, 0, 0, new Insets (5,
										15, 5, 15)));

		JEditorPane messagePane = new JEditorPane ("text/html", message);

		panel.add (messagePane, getConstraints (1, row, 1, 1, GridBagConstraints.BOTH, 100, 100, new Insets (5, 15, 5,
						15)));
	}

	public JPanel getPanel ()
	{
		return panel;
	}

	public String getGuiPaneId ()
	{
		return guiPaneId;
	}

	public long getUniqueId ()
	{
		return uniqueId;
	}

	public String getTypeId ()
	{
		return iObjectTypeId;
	}

	protected GridBagConstraints getConstraints (int x, int y, int width, int height, int fill, int wx, int wy,
					Insets insets)
	{
		GridBagConstraints gbc = new GridBagConstraints ();

		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.fill = fill;
		gbc.weightx = wx;
		gbc.weighty = wy;
		gbc.anchor = GridBagConstraints.NORTHWEST;

		if (insets == null)
		{
			gbc.insets = new Insets (0, 0, 0, 0);
		}
		else
		{
			gbc.insets = insets;
		}

		return gbc;
	}
}
