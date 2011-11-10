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

package de.iritgo.aktario.buddylist;


import de.iritgo.aktario.buddylist.gui.BuddyListPane;
import de.iritgo.aktario.core.AktarioPlugin;
import de.iritgo.aktario.core.plugin.Plugin;


/**
 * BuddyListPlugin.
 *
 * @version $Id: BuddyListPlugin.java,v 1.4 2006/09/25 10:34:32 grappendorf Exp $
 */
public class BuddyListPlugin extends AktarioPlugin
{
	@Override
	protected void registerDataObjects()
	{
		registerDataObject(new BuddyListRegistry());
		registerDataObject(new BuddyList());
		registerDataObject(new BuddyListGroup());
	}

	@Override
	protected void registerActions()
	{
	}

	@Override
	protected void registerGUIPanes()
	{
		registerGUIPane(new BuddyListPane());
	}

	@Override
	protected void registerManagers()
	{
		registerManager(Plugin.SERVER, new BuddyListManager());

		// 		registerManager (new BuddyListGroupManager());
	}

	@Override
	protected void registerConsoleCommands()
	{
	}

	@Override
	protected void registerCommands()
	{
	}
}
