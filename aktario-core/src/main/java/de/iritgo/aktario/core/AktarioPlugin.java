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

package de.iritgo.aktario.core;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.application.Application;
import de.iritgo.aktario.framework.base.FrameworkPlugin;


/**
 * Base class for Aktario plugins.
 *
 * @version $Id: AktarioPlugin.java,v 1.8 2006/09/25 10:34:32 grappendorf Exp $
 */
public class AktarioPlugin extends FrameworkPlugin
{
	/**
	 * Initiaize the plugin.
	 *
	 * @param engine The system engine.
	 */
	@Override
	public void init (Engine engine)
	{
		registerApplications ();
		super.init (engine);
	}

	/**
	 * Register all collaboration applications in this method.
	 */
	public void registerApplications ()
	{
	}

	/**
	 * Register a single application.
	 *
	 * @param id The id of the application.
	 * @param The application name (resource key).
	 * @param The application icon (resource key).
	 * @param guiPaneId The id of the main gui pane.
	 */
	protected void registerApplication (String id, String nameKey, String iconKey, String guiPaneId)
	{
		Application.add (new Application (id, this, nameKey, iconKey, guiPaneId));
	}
}
