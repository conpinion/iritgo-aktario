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

package de.iritgo.aktario.client;


import de.iritgo.aktario.client.command.ApplyPreferences;
import de.iritgo.aktario.client.command.DefaultStartup;
import de.iritgo.aktario.client.command.DisplayUserConnectionState;
import de.iritgo.aktario.client.command.DisplayUserReadyState;
import de.iritgo.aktario.client.command.EnableAdminFunctions;
import de.iritgo.aktario.client.command.GetUserRole;
import de.iritgo.aktario.client.gui.AboutPane;
import de.iritgo.aktario.client.gui.EditRoomPane;
import de.iritgo.aktario.client.gui.EditUserPane;
import de.iritgo.aktario.client.gui.GetDesktopFrameBounds;
import de.iritgo.aktario.client.gui.MessangerPane;
import de.iritgo.aktario.client.gui.ParticipantControlPane;
import de.iritgo.aktario.client.gui.PreferencesPane;
import de.iritgo.aktario.client.gui.RoomControlPane;
import de.iritgo.aktario.client.gui.RoomPane;
import de.iritgo.aktario.client.gui.TestLoginCommand;
import de.iritgo.aktario.client.gui.TestLogoffCommand;
import de.iritgo.aktario.client.gui.ToolControlPane;
import de.iritgo.aktario.client.gui.UserListPane;
import de.iritgo.aktario.client.gui.UserLoginFailurePane;
import de.iritgo.aktario.client.gui.UserLoginPane;
import de.iritgo.aktario.core.AktarioPlugin;
import de.iritgo.aktario.framework.base.FrameworkPlugin;


/**
 * AktarioClientPlugin.
 *
 * @version $Id: AktarioClientPlugin.java,v 1.14 2006/09/25 10:34:32 grappendorf Exp $
 */
public class AktarioClientPlugin extends AktarioPlugin
{
	@Override
	protected void registerDataObjects()
	{
	}

	@Override
	protected void registerActions()
	{
	}

	@Override
	protected void registerGUIPanes()
	{
		registerGUIPane(new ParticipantControlPane());
		registerGUIPane(new RoomControlPane());
		registerGUIPane(new ToolControlPane());
		registerGUIPane(new UserLoginPane());
		registerGUIPane(new UserLoginFailurePane());
		registerGUIPane(new PreferencesPane());
		registerGUIPane(new UserListPane());
		registerGUIPane(new EditUserPane());
		registerGUIPane(new AboutPane());
		registerGUIPane(new RoomPane());
		registerGUIPane(new EditRoomPane());
		registerGUIPane(new MessangerPane());
	}

	@Override
	protected void registerManagers()
	{
		registerManager(FrameworkPlugin.CLIENT, new AktarioClientManager());
		registerManager(FrameworkPlugin.CLIENT, new PreferencesManager());
	}

	@Override
	protected void registerConsoleCommands()
	{
	}

	@Override
	protected void registerCommands()
	{
		registerCommand(FrameworkPlugin.CLIENT, new GetDesktopFrameBounds());
		registerCommand(FrameworkPlugin.CLIENT, new DefaultStartup());
		registerCommand(FrameworkPlugin.CLIENT, new GetUserRole());
		registerCommand(FrameworkPlugin.CLIENT, new DisplayUserConnectionState());
		registerCommand(FrameworkPlugin.CLIENT, new DisplayUserReadyState());
		registerCommand(FrameworkPlugin.CLIENT, new ApplyPreferences());
		registerCommand(FrameworkPlugin.CLIENT, new EnableAdminFunctions());
		registerCommand(FrameworkPlugin.CLIENT, new TestLoginCommand());
		registerCommand(FrameworkPlugin.CLIENT, new TestLogoffCommand());
	}
}
