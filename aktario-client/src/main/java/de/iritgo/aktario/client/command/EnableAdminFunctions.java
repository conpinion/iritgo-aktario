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
import de.iritgo.aktario.framework.client.Client;


/**
 * This command enables or disables the administration functions.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Type</th><th>Description</th></tr>
 *   <tr><td>enabled</td><td>Boolean</td><td>If true the admin functions are enabled.</td></tr>
 * </table>
 *
 * @version $Id: EnableAdminFunctions.java,v 1.8 2006/09/25 10:34:30 grappendorf Exp $
 */
public class EnableAdminFunctions extends Command
{
	/**
	 * Create a new command object.
	 */
	public EnableAdminFunctions ()
	{
		super ("EnableAdminFunctions");
	}

	/**
	 * Perform the command.
	 */
	@Override
	public void perform ()
	{
		AktarioGUI gui = (AktarioGUI) Client.instance ().getClientGUI ();

		boolean enable = false;

		if (properties.get ("enabled") != null)
		{
			enable = ((Boolean) properties.get ("enabled")).booleanValue ();
		}

		gui.setAdminMenuVisible (enable);
	}
}
