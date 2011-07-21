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

package de.iritgo.aktario.server;


import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.manager.Manager;
import de.iritgo.aktario.framework.command.CommandTools;
import de.iritgo.aktario.framework.server.Server;


/**
 * AktarioServerManager
 *
 * @version $Id: AktarioServerManager.java,v 1.11 2006/09/25 10:34:32 grappendorf Exp $
 */
public class AktarioServerManager extends BaseObject implements Manager
{
	/**
	 * Create a new AktarioServerManager.
	 */
	public AktarioServerManager ()
	{
		super ("server");
	}

	/**
	 * Initialize the manager.
	 */
	public void init ()
	{
		CommandTools.performSimple ("persist.LoadAllUsers", new Object[]
		{});

		Server.instance ().createDefaultNetworkProcessingSystem ();
	}

	/**
	 * Unload the manager from the system.
	 */
	public void unload ()
	{
	}
}
