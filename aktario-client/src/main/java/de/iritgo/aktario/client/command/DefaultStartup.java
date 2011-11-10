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

package de.iritgo.aktario.client.command;


import de.iritgo.aktario.client.gui.AktarioGUI;
import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.user.AktarioUser;
import de.iritgo.aktario.framework.client.Client;
import de.iritgo.aktario.framework.client.command.ShowWindow;
import de.iritgo.aktario.framework.command.CommandTools;
import javax.swing.JDesktopPane;
import java.awt.Rectangle;
import java.util.Properties;


/**
 * Startup.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Type</th><th>Description</th></tr>
 *   <tr><td>user</td><td>AktarioUser</td><td>The current user.</td></td>
 * </table>
 *
 * @version $Id: DefaultStartup.java,v 1.9 2006/09/25 10:34:30 grappendorf Exp $
 */
public class DefaultStartup extends Command
{
	/**
	 * Create a new startup command.
	 */
	public DefaultStartup()
	{
		super("DefaultStartup");
	}

	/**
	 * This command is called after a successful server login.
	 * It opens all initial client windows.
	 */
	@Override
	public void perform()
	{
		if (properties.get("user") == null)
		{
			Log.logError("client", "DefaultStartup", "Missing user");

			return;
		}

		AktarioUser user = (AktarioUser) properties.get("user");

		try
		{
			JDesktopPane frame = ((AktarioGUI) Client.instance().getClientGUI()).getDesktopPane();
			Rectangle bounds = frame.getBounds();

			Properties props = new Properties();

			props.put("closable", new Boolean(false));
			props.put("iconifiable", new Boolean(false));
			props.put("bounds", new Rectangle(bounds.width - bounds.width / 6, 0, bounds.width / 6,
							(int) (bounds.height * 0.4)));

			// 			CommandTools.performAsync (
			// 				new ShowWindow(
			// 					"ParticipantControlPane",
			// 					AppContext.instance ().getUser ().getNamedIritgoObject ("AktarioUserRegistry")),
			// 				props);
			props = new Properties();
			props.put("closable", new Boolean(false));
			props.put("iconifiable", new Boolean(false));
			props.put("bounds", new Rectangle(bounds.width - bounds.width / 6, (int) (bounds.height * 0.4),
							bounds.width / 6, (int) (bounds.height * 0.4)));
			CommandTools.performAsync(new ShowWindow("RoomControlPane", user), props);

			props = new Properties();
			props.put("closable", new Boolean(false));
			props.put("iconifiable", new Boolean(false));
			props.put("bounds", new Rectangle(bounds.width - bounds.width / 6, (int) (bounds.height * 0.8),
							bounds.width / 6, (int) (bounds.height * 0.2)));

			// 			CommandTools.performAsync (
			// 				new ShowWindow(
			// 					"ToolControlPane",
			// 					AppContext.instance ().getUser ().getNamedIritgoObject ("AktarioUserRegistry")),
			// 				props);
		}
		catch (Exception x)
		{
			Log.logError("client", "DefaultStartup", x.toString());
		}
	}
}
