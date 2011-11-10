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

package de.iritgo.aktario.monitor.manager;


import de.iritgo.aktario.core.*;
import de.iritgo.aktario.core.base.*;
import de.iritgo.aktario.core.iobject.*;
import de.iritgo.aktario.core.manager.Manager;
import de.iritgo.aktario.core.thread.*;
import de.iritgo.aktario.framework.base.*;
import de.iritgo.aktario.framework.server.*;
import de.iritgo.aktario.monitor.*;
import java.lang.*;


public class SystemMonitorManager extends BaseObject implements Manager
{
	class SystemMonitorWorker extends Threadable
	{
		public SystemMonitorWorker()
		{
			super("SystemMonitorWorker");
		}

		public void run()
		{
			systemMonitor.setRegisteredUsers(Server.instance().getUserRegistry().getUserSize());
			systemMonitor.setWorkingThreads(Engine.instance().getThreadService().getWorkingSlots());
			systemMonitor.setFreeThreads(Engine.instance().getThreadService().getFreeSlots());
			systemMonitor.setFreeRam(Runtime.getRuntime().freeMemory());

			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException x)
			{
			}
		}
	}

	private final SystemMonitor systemMonitor = new SystemMonitor();

	public SystemMonitorManager()
	{
		super("system-monitor");
	}

	public void init()
	{
		systemMonitor.setUniqueId(SystemMonitor.SYSTEM_MONITOR_ID);
		Engine.instance().getBaseRegistry().add(systemMonitor);
		Engine.instance().getProxyRegistry().addProxy(new FrameworkProxy(systemMonitor), systemMonitor.getTypeId());

		Engine.instance().getThreadService().add(new SystemMonitorWorker());
	}

	public void unload()
	{
	}
}
