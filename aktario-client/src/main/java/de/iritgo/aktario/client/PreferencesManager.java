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

package de.iritgo.aktario.client;


import de.iritgo.aktario.client.gui.PreferencesPane;
import de.iritgo.aktario.client.gui.PreferencesPaneInterface;
import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.manager.Manager;
import de.iritgo.aktario.core.plugin.PluginEventListener;
import de.iritgo.aktario.core.plugin.PluginStateEvent;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * PreferencesManager.
 *
 * @version $Id: PreferencesManager.java,v 1.5 2006/09/25 10:34:32 grappendorf Exp $
 */
public class PreferencesManager extends BaseObject implements Manager, PluginEventListener
{
	private List preferences;

	/**
	 * Create a new client manager.
	 */
	public PreferencesManager()
	{
		super("PreferencesManager");
	}

	/**
	 * Initialize the client manager.
	 */
	public void init()
	{
		preferences = new LinkedList();
		Engine.instance().getEventRegistry().addListener("Plugin", this);
	}

	public void pluginEvent(PluginStateEvent event)
	{
		if (event.allPluginsInitialized())
		{
			for (Iterator i = preferences.iterator(); i.hasNext();)
			{
				PreferencesPaneInterface ppi = (PreferencesPaneInterface) i.next();

				ppi.init();
			}
		}
	}

	/**
	 * Free all client manager resources.
	 */
	public void unload()
	{
		preferences.clear();
	}

	public void applyAction()
	{
		for (Iterator i = preferences.iterator(); i.hasNext();)
		{
			PreferencesPaneInterface ppi = (PreferencesPaneInterface) i.next();

			ppi.applyAction();
		}
	}

	public void cancleAction()
	{
		for (Iterator i = preferences.iterator(); i.hasNext();)
		{
			PreferencesPaneInterface ppi = (PreferencesPaneInterface) i.next();

			ppi.cancleAction();
		}
	}

	public void saveAction()
	{
		for (Iterator i = preferences.iterator(); i.hasNext();)
		{
			PreferencesPaneInterface ppi = (PreferencesPaneInterface) i.next();

			ppi.saveAction();
		}
	}

	public void addPreferences(PreferencesPane preferencesPane)
	{
		int j = 2;

		for (Iterator i = preferences.iterator(); i.hasNext();)
		{
			PreferencesPaneInterface ppi = (PreferencesPaneInterface) i.next();

			++j;

			preferencesPane.addPreferencesPane(ppi.getName(), ppi.getIcon(), ppi.getPanel(), j);
		}
	}

	public void registerPreferences(PreferencesPaneInterface ppi)
	{
		preferences.add(ppi);
	}
}
