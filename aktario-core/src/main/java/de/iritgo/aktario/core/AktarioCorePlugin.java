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


import de.iritgo.aktario.core.application.ApplicationInstance;
import de.iritgo.aktario.core.application.WhiteBoardAction;
import de.iritgo.aktario.core.application.WhiteBoardServerAction;
import de.iritgo.aktario.core.plugin.Plugin;
import de.iritgo.aktario.core.room.Participant;
import de.iritgo.aktario.core.room.Room;
import de.iritgo.aktario.core.user.AktarioUser;
import de.iritgo.aktario.core.user.AktarioUserDeletedResponse;
import de.iritgo.aktario.core.user.AktarioUserManager;
import de.iritgo.aktario.core.user.AktarioUserPreferences;
import de.iritgo.aktario.core.user.AktarioUserProfile;
import de.iritgo.aktario.core.user.AktarioUserReadyAction;
import de.iritgo.aktario.core.user.AktarioUserReadyServerAction;
import de.iritgo.aktario.core.user.AktarioUserRegistry;
import de.iritgo.aktario.core.user.AktarioUserStateAction;
import de.iritgo.aktario.core.user.AktarioUserStateServerAction;
import de.iritgo.aktario.framework.base.FrameworkPlugin;


/**
 * AktarioCorePlugin
 *
 * @version $Id: AktarioCorePlugin.java,v 1.11 2006/09/25 10:34:32 grappendorf Exp $
 */
public class AktarioCorePlugin extends FrameworkPlugin
{
	@Override
	protected void registerDataObjects()
	{
		registerDataObject(new AktarioUser());
		registerDataObject(new AktarioUserProfile());
		registerDataObject(new AktarioUserPreferences());
		registerDataObject(new AktarioUserRegistry());
		registerDataObject(new Room());
		registerDataObject(new ApplicationInstance());
		registerDataObject(new Participant());
	}

	@Override
	protected void registerActions()
	{
		registerAction(new AktarioUserStateAction());
		registerAction(new AktarioUserStateServerAction());
		registerAction(new WhiteBoardServerAction());
		registerAction(new WhiteBoardAction());
		registerAction(new AktarioUserReadyAction());
		registerAction(new AktarioUserReadyServerAction());
		registerAction(new AktarioUserDeletedResponse());
	}

	@Override
	protected void registerGUIPanes()
	{
	}

	@Override
	protected void registerManagers()
	{
		registerManager(Plugin.SERVER, new AktarioUserManager());
	}

	@Override
	protected void registerConsoleCommands()
	{
	}

	@Override
	protected void registerCommands()
	{
	}
}
