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


import de.iritgo.aktario.client.gui.AktarioGUI;
import de.iritgo.aktario.core.gui.IAction;
import de.iritgo.aktario.framework.client.Client;
import javax.swing.ImageIcon;
import java.awt.event.ActionEvent;


/**
 *
 */
@SuppressWarnings("serial")
public class BuddiesToolBarItem extends IAction
{
	public BuddiesToolBarItem()
	{
		setSmallIcon(new ImageIcon(getClass().getResource("/resources/tool-buddies.png")));
		setToolTipText("buddyListButton");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		AktarioGUI gui = ((AktarioGUI) Client.instance().getClientGUI());

		gui.getDesktopManager().getDisplay("BuddyListPane").bringToFront();
		gui.getDesktopManager().getDisplay("BuddyListPane").show();
	}
}
