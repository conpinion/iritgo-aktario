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

package de.iritgo.aktario.framework.server.command;


import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.framework.server.Server;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.UserRegistry;
import java.util.Iterator;


/**
 *
 */
public class ShowUsers extends Command
{
	public ShowUsers()
	{
	}

	/**
	 * Execute the command.
	 */
	public void perform()
	{
		UserRegistry users = Server.instance().getUserRegistry();

		for (Iterator i = users.userIterator(); i.hasNext();)
		{
			User user = (User) i.next();

			char stateChar = '-';

			if (user.isOnline())
			{
				stateChar = '*';
			}

			System.out.println("(" + stateChar + ") " + user.getName());
		}
	}
}
