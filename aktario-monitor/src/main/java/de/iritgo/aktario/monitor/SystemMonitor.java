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


import de.iritgo.aktario.framework.base.*;


/**
 * Specifies the Monitor object with all system data.
 */
public class SystemMonitor extends DataObject
{
	public static final long SYSTEM_MONITOR_ID = 11111; //TODO: Think about this id's...

	/**
	 * Create a new system monitor object.
	 */
	public SystemMonitor ()
	{
		super ("SystemMonitor");
		addAttribute ("registeredUsers", 0);
		addAttribute ("onlineUsers", 0);
		addAttribute ("workingThreads", 0);
		addAttribute ("freeThreads", 0);
		addAttribute ("freeRam", new Long (0));
	}

	public void setRegisteredUsers (int registeredUsers)
	{
		setAttribute ("registeredUsers", registeredUsers);
	}

	public int getRegisteredUsers ()
	{
		return getIntAttribute ("registeredUsers");
	}

	public void setOnlineUsers (int onlineUsers)
	{
		setAttribute ("onlineUsers", onlineUsers);
	}

	public int getOnlineUsers ()
	{
		return getIntAttribute ("onlineUsers");
	}

	public void setWorkingThreads (int workingThreads)
	{
		setAttribute ("workingThreads", workingThreads);
	}

	public int getWorkingThreads ()
	{
		return getIntAttribute ("workingThreads");
	}

	public void setFreeThreads (int freeThreads)
	{
		setAttribute ("freeThreads", freeThreads);
	}

	public int getFreeThreads ()
	{
		return getIntAttribute ("freeThreads");
	}

	public void setFreeRam (long freeRam)
	{
		setAttribute ("freeRam", freeRam);
	}

	public long getFreeRam ()
	{
		return getLongAttribute ("freeRam");
	}
}
