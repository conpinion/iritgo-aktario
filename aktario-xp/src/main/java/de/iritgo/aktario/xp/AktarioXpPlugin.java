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

package de.iritgo.aktario.xp;


import de.iritgo.aktario.core.AktarioPlugin;
import de.iritgo.aktario.core.plugin.Plugin;


/**
 * AktarioXpPlugin
 *
 * @version $Id: AktarioXpPlugin.java,v 1.6 2006/09/23 00:08:46 grappendorf Exp $
 */
public class AktarioXpPlugin extends AktarioPlugin
{
	/**
	 * Register all data objects in this method.
	 */
	protected void registerDataObjects()
	{
		registerDataObject(new SoftwareReviewData());
	}

	/**
	 * Register all actions in this method.
	 */
	protected void registerActions()
	{
		registerAction(new RemoteControlCodeAction());
		registerAction(new RemoteControlCodeServerAction());
	}

	protected void registerManagers()
	{
		registerManager(Plugin.SERVER, new XPManager());
	}

	/**
	 * Register all gui panes in this method.
	 */
	protected void registerGUIPanes()
	{
		registerGUIPane(new SoftwareReviewPane());
	}

	/**
	 * Register all collaboration applications in this method.
	 */
	public void registerApplications()
	{
		registerApplication("aktario.xp.SoftwareReview", "aktario.softwareReview", "/resources/xp.png",
						"SoftwareReviewPane");
	}
}
