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

package de.iritgo.aktario.framework.client.command;


import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.core.gui.IDisplay;
import de.iritgo.aktario.framework.client.Client;
import java.util.Properties;


/**
 *
 */
public class CloseDisplay extends Command
{
	private String windowId;

	/**
	 * Standard constructor
	 */
	public CloseDisplay(String windowId)
	{
		this.windowId = windowId;
	}

	/**
	 * Set the command Properties
	 *
	 * @param properties The properties.
	 */
	public void setProperties(Properties properties)
	{
	}

	/**
	 * Display the IWindow-Pane.
	 */
	public void perform()
	{
		IDisplay display = Client.instance().getClientGUI().getDesktopManager().getDisplay(windowId);

		if (display != null)
		{
			display.close();
			Client.instance().getClientGUI().getDesktopManager().removeDisplay(display);
		}
	}
}
