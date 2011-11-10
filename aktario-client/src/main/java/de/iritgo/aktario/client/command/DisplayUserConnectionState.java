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


import de.iritgo.aktario.core.command.Command;


/**
 * Visually display a users connection state.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Type</th><th>Description</th></tr>
 *   <tr><td>userIds</td><td>List</td><td>List of (Long) user ids </td></td>
 *   <tr><td>connected</td><td>Boolean</td><td>True if the user is currently connected.</td></td>
 * </table>
 *
 * @version $Id: DisplayUserConnectionState.java,v 1.11 2006/09/25 10:34:30 grappendorf Exp $
 */
public class DisplayUserConnectionState extends Command
{
	/**
	 * Create a new command.
	 */
	public DisplayUserConnectionState()
	{
		super("DisplayUserConnectionState");
	}

	/**
	 * Perform the command.
	 */
	@Override
	public void perform()
	{
		// 		if (properties.get ("userIds") == null)
		// 		{
		// 			Log.logError ("client", "DisplayUserConnectionState", "Missing user id");
		// 			return;
		// 		}
		// 		List userIds = (List) properties.get ("userIds");
		// 		if (properties.get ("connected") == null)
		// 		{
		// 			Log.logError ("client", "DisplayUserConnectionState", "Missing connection state");
		// 			return;
		// 		}
		// 		boolean connected = ((Boolean) properties.get ("connected")).booleanValue ();
		// 		((ParticipantControlPane) AppContext.instance ().getObject ("participantControlPane")).setUserConnectionState (
		// 			userIds, connected);
	}
}
