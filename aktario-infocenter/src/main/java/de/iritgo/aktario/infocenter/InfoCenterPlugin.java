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

package de.iritgo.aktario.infocenter;


import de.iritgo.aktario.core.plugin.Plugin;
import de.iritgo.aktario.framework.base.FrameworkPlugin;
import de.iritgo.aktario.infocenter.command.InfoCenter;
import de.iritgo.aktario.infocenter.guinetworkdisplay.action.InfoCenterAction;
import de.iritgo.aktario.infocenter.guinetworkdisplay.command.AddInfoToGUIDisplay;
import de.iritgo.aktario.infocenter.guinetworkdisplay.command.CreateDiskWriterDisplay;
import de.iritgo.aktario.infocenter.guinetworkdisplay.command.CreateGUINetworkDisplay;
import de.iritgo.aktario.infocenter.guinetworkdisplay.command.RemoveGUINetworkDisplay;
import de.iritgo.aktario.infocenter.guinetworkdisplay.gui.NetworkDisplayGUIPane;
import de.iritgo.aktario.infocenter.manager.InfoCenterClientManager;
import de.iritgo.aktario.infocenter.manager.InfoCenterManager;


public class InfoCenterPlugin extends FrameworkPlugin
{
	protected void registerDataObjects()
	{
	}

	protected void registerActions()
	{
		registerAction(new InfoCenterAction());
	}

	protected void registerGUIPanes()
	{
		registerGUIPane(Plugin.CLIENT, new NetworkDisplayGUIPane());
	}

	protected void registerManagers()
	{
		registerManager(Plugin.SERVER, new InfoCenterManager());
		registerManager(Plugin.CLIENT, new InfoCenterClientManager());
	}

	protected void registerCommands()
	{
		registerCommand(new InfoCenter());
		registerCommand(new CreateGUINetworkDisplay());
		registerCommand(new CreateDiskWriterDisplay());
		registerCommand(new AddInfoToGUIDisplay());
		registerCommand(new RemoveGUINetworkDisplay());
	}

	protected void registerConsoleCommands()
	{
	}
}
