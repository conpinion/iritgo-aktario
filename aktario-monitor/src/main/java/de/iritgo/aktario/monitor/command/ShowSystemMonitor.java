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

package de.iritgo.aktario.monitor.command;


import de.iritgo.aktario.core.*;
import de.iritgo.aktario.core.base.*;
import de.iritgo.aktario.core.command.*;
import de.iritgo.aktario.core.iobject.*;
import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.aktario.framework.base.*;
import de.iritgo.aktario.framework.client.command.*;
import de.iritgo.aktario.framework.command.CommandTools;
import de.iritgo.aktario.monitor.*;


/**
 * This command creates a new system monitor
 *
 * <table>
 *   <tr><th>Parameter</th><th>Type</th><th>Description</th></tr>
 * </table>
 */
public class ShowSystemMonitor extends Command
{
	/**
	 * Create a new ShowSystemMonitor command.
	 */
	public ShowSystemMonitor()
	{
		super("ShowSystemMonitor");
	}

	/**
	 * Perform the command.
	 */
	public void perform()
	{
		BaseRegistry baseRegistry = Engine.instance().getBaseRegistry();

		SystemMonitor systemMonitor = (SystemMonitor) baseRegistry
						.get(SystemMonitor.SYSTEM_MONITOR_ID, "SystemMonitor");

		if (systemMonitor == null)
		{
			systemMonitor = new SystemMonitor();
			systemMonitor.setUniqueId(SystemMonitor.SYSTEM_MONITOR_ID);
			baseRegistry.add(systemMonitor);
			Engine.instance().getProxyRegistry().addProxy(new FrameworkProxy(systemMonitor), systemMonitor.getTypeId());
		}

		Engine.instance().getProxyRegistry().getProxy(systemMonitor.getUniqueId(), systemMonitor.getTypeId()).reset();

		CommandTools.performAsync(new ShowWindow("SystemMonitor", systemMonitor));
	}
}
