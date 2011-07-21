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

package de.iritgo.aktario.core.plugin;


import de.iritgo.aktario.core.event.Event;


/**
 * UserEvent, contains the user object.
 */
public class PluginStateEvent implements Event
{
	public static int NONE_STATE = 0;

	public static int ALL_PLUGINS_INITIALIZED = 1;

	@SuppressWarnings("unused")
	private Plugin plugin;

	private int state;

	/**
	 * Standard constructor
	 */
	public PluginStateEvent (Plugin plugin, int state)
	{
		this.plugin = plugin;
		this.state = state;
	}

	public int getState ()
	{
		return state;
	}

	public boolean allPluginsInitialized ()
	{
		return state == ALL_PLUGINS_INITIALIZED;
	}
}
