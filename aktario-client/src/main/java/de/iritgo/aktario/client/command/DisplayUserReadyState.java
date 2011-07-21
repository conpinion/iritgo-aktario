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
 *   <tr><td>userId</td><td>Long</td><td>The id of the user </td></td>
 *   <tr><td>ready</td><td>Boolean</td><td>True if the user is currently ready.</td></td>
 * </table>
 *
 * @version $Id: DisplayUserReadyState.java,v 1.11 2006/09/25 10:34:30 grappendorf Exp $
 */
public class DisplayUserReadyState extends Command
{
	/**
	 * Create a new command.
	 */
	public DisplayUserReadyState ()
	{
		super ("DisplayUserReadyState");
	}

	/**
	 * Perform the command.
	 */
	@Override
	public void perform ()
	{
		// 		if (properties.get ("userId") == null)
		// 		{
		// 			Log.logError ("client", "DisplayUserReadyState", "Missing user id");
		// 			return;
		// 		}
		// 		long userId = ((Long) properties.get ("userId")).longValue ();
		// 		if (properties.get ("ready") == null)
		// 		{
		// 			Log.logError ("client", "DisplayUserReadyState", "Missing ready state");
		// 			return;
		// 		}
		// 		boolean ready = ((Boolean) properties.get ("ready")).booleanValue ();
		// 		((ParticipantControlPane) AppContext.instance ().getObject ("participantControlPane")).setUserReady (
		// 			userId, ready);
	}
}
