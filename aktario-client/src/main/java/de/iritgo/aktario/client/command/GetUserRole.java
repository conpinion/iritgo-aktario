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


import de.iritgo.aktario.client.AktarioClientManager;
import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.core.user.AktarioUser;


/**
 * Get the role of a user.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Type</th><th>Description</th></tr>
 *   <tr><td>userId</td><td>Integer</td><td>Id of the user which role should be retrieved.
 *      If not specified, the current user is queried..</td></tr>
 * </table>
 *
 * @version $Id: GetUserRole.java,v 1.13 2006/11/08 11:05:21 grappendorf Exp $
 */
public class GetUserRole extends Command
{
	/**
	 * Create a new command.
	 */
	public GetUserRole()
	{
		super("GetUserRole");
	}

	/**
	 * Perform the command.
	 */
	@Override
	public Object performWithResult()
	{
		AktarioUser user = ((AktarioClientManager) Engine.instance().getManager("aktarioclient")).getUser();

		return new Integer(user.getRole());
	}
}
