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

package de.iritgo.aktario.participant;


import de.iritgo.aktario.core.plugin.PluginStateEvent;
import de.iritgo.aktario.framework.dataobject.DataObjectTools;


/**
 * ParticipantClientManager.
 *
 * @version $Id: ParticipantClientManager.java,v 1.6 2006/09/25 10:34:31 grappendorf Exp $
 */
public class ParticipantClientManager extends ParticipantManager
{
	/**
	 * Create a new client manager.
	 */
	public ParticipantClientManager()
	{
		super("ParticipantClientManager");
	}

	public void pluginEvent(PluginStateEvent event)
	{
		if (event.allPluginsInitialized())
		{
			DataObjectTools.registerOnStartupDynDataObject(participant);
		}
	}

	/**
	 * Free all client manager resources.
	 */
	public void unload()
	{
	}
}
