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

package de.iritgo.aktario.buddylist.gui;


import com.l2fprod.common.swing.JTaskPaneGroup;
import de.iritgo.aktario.buddylist.BuddyListGroup;
import de.iritgo.aktario.buddylist.BuddyListPlugin;
import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.gui.GUIPane;
import de.iritgo.aktario.core.gui.GUIPaneRegistry;
import de.iritgo.aktario.core.iobject.IObjectProxyEvent;
import de.iritgo.aktario.core.iobject.IObjectProxyListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;


/**
 * This gui pane displays a list of all users and lets the administrator
 * add, edit, and delete users.
 *
 * @version $Id$
 */
public class TaskGroup extends JTaskPaneGroup implements IObjectProxyListener
{
	/** */
	private static final long serialVersionUID = 1L;

	private BuddyListGroup buddyListGroup;

	private JPanel panel;

	public TaskGroup(BuddyListGroup buddyListGroup)
	{
		this.buddyListGroup = buddyListGroup;
		Engine.instance().getProxyEventRegistry().addEventListener(buddyListGroup, this);
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		// 		getContentPane ().setLayout (new GridBagLayout ());
		setExpanded(true);
		setScrollOnExpand(true);
		setIcon(new ImageIcon(BuddyListPlugin.class.getResource("/resources/admin-group-16.png")));
	}

	/**
	 * Event for new buddy list group update.
	 *
	 * @param event The EventOject.
	 */
	public void proxyEvent(IObjectProxyEvent event)
	{
		if (event.isWaitingForNewObject())
		{
			return;
		}

		final BuddyListGroup buddyListGroup = (BuddyListGroup) event.getObject();

		try
		{
			setTitle(buddyListGroup.getName());

			SwingUtilities.invokeAndWait(new Runnable()
			{
				public void run()
				{
					GUIPane guiPane = (GUIPane) GUIPaneRegistry.instance().create("ParticipantStatePane");

					guiPane.setOnScreenUniqueId(buddyListGroup.getName());
					guiPane.setContentPane(panel);
					guiPane.setObject(buddyListGroup);
					guiPane.initGUI();
					guiPane.loadFromObject(buddyListGroup);

					add(panel);

					revalidate();
					repaint();
				}
			});
		}
		catch (Exception x)
		{
			x.printStackTrace();
		}
	}

	public void dispose()
	{
		Engine.instance().getProxyEventRegistry().removeEventListener(buddyListGroup, this);
	}

	/**
	 * Helper method for creating gridbag constraints.
	 *
	 * @param x The grid column.
	 * @param y The grid row.
	 * @param width The number of occupied columns.
	 * @param height The number of occupied rows.
	 * @param fill The fill method.
	 * @param wx The horizontal stretch factor.
	 * @param wy The vertical stretch factor.
	 * @param insets The cell insets.
	 * @return The gridbag constraints.
	 */
	protected GridBagConstraints createConstraints(int x, int y, int width, int height, int fill, int wx, int wy,
					Insets insets)
	{
		GridBagConstraints gbc = new GridBagConstraints();

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
			gbc.insets = new Insets(0, 0, 0, 0);
		}
		else
		{
			gbc.insets = insets;
		}

		return gbc;
	}

	/**
	 * Helper method for creating gridbag constraints.
	 *
	 * @param x The grid column.
	 * @param y The grid row.
	 * @param width The number of occupied columns.
	 * @param height The number of occupied rows.
	 * @param fill The fill method.
	 * @param anchor The anchor.
	 * @param wx The horizontal stretch factor.
	 * @param wy The vertical stretch factor.
	 * @param insets The cell insets.
	 * @return The gridbag constraints.
	 */
	protected GridBagConstraints createConstraints(int x, int y, int width, int height, double wx, double wy, int fill,
					int anchor, Insets insets)
	{
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.fill = fill;
		gbc.weightx = wx;
		gbc.weighty = wy;
		gbc.anchor = anchor;

		if (insets == null)
		{
			gbc.insets = new Insets(0, 0, 0, 0);
		}
		else
		{
			gbc.insets = insets;
		}

		return gbc;
	}
}
