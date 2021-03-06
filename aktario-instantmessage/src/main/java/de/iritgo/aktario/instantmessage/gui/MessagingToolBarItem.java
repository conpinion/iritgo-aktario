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

package de.iritgo.aktario.instantmessage.gui;


import de.iritgo.aktario.core.gui.IAction;
import de.iritgo.aktario.framework.client.Client;
import de.iritgo.aktario.framework.client.command.ShowWindow;
import de.iritgo.aktario.framework.command.CommandTools;
import javax.swing.ImageIcon;
import java.awt.event.ActionEvent;
import java.util.Properties;


/**
 *
 */
@SuppressWarnings("serial")
public class MessagingToolBarItem extends IAction
{
	private boolean firstActivation = true;

	public MessagingToolBarItem()
	{
		setSmallIcon(new ImageIcon(getClass().getResource("/resources/tool-messanger.png")));
		setToolTipText("messangerArchivButton");
	}

	public void actionPerformed(ActionEvent e)
	{
		if (firstActivation)
		{
			Properties props = new Properties();

			props.put("closable", false);
			props.put("iconifiable", false);
			props.put("maximizable", false);
			props.put("maximized", false);
			props.put("titlebar", false);
			props.put("visible", true);
			CommandTools.performAsync(new ShowWindow("ArchivInstantMessagePane"), props);
			firstActivation = true;
		}
		else
		{
			Client.instance().getClientGUI().getDesktopManager().getDisplay("ArchivInstantMessagePane").bringToFront();
			Client.instance().getClientGUI().getDesktopManager().getDisplay("ArchivInstantMessagePane").show();
		}
	}
}
