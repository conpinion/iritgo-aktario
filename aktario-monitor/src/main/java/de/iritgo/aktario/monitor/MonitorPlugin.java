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

package de.iritgo.aktario.monitor;


import de.iritgo.aktario.core.plugin.Plugin;
import de.iritgo.aktario.framework.base.FrameworkPlugin;
import de.iritgo.aktario.monitor.command.*;
import de.iritgo.aktario.monitor.gui.*;
import de.iritgo.aktario.monitor.manager.*;


/**
 * The MonitorPlugin registers all functionality of the system monitor
 * plugin.
 */
public class MonitorPlugin extends FrameworkPlugin
{
	/**
	 * Register all commands in this method.
	 */
	protected void registerCommands ()
	{
		registerCommand (new ShowSystemMonitor ());
	}

	/**
	 * Register all data objects in this method.
	 */
	protected void registerDataObjects ()
	{
		registerDataObject (new SystemMonitor ());
	}

	/**
	 * Register all gui panes in this method.
	 */
	protected void registerGUIPanes ()
	{
		registerGUIPane (Plugin.CLIENT, new SystemMonitorGUIPane ());
	}

	/**
	 * Register all managers in this method.
	 */
	protected void registerManagers ()
	{
		registerManager (Plugin.SERVER, new SystemMonitorManager ());

		//		registerManager (Plugin.CLIENT, new InfoCenterClientManager());
	}
}
