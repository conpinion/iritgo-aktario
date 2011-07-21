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

package de.iritgo.aktario.framework.client.command;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.core.plugin.PluginManager;
import de.iritgo.aktario.core.resource.ResourceService;
import de.iritgo.aktario.framework.IritgoEngine;
import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.aktario.framework.client.Client;
import java.util.Locale;
import java.util.Properties;


/**
 *
 */
public class ChangeLanguage extends Command
{
	private Locale locale = null;

	/**
	 * @deprecated Use the ChangeLanguage (Locale locale) constructor.
	 */
	public ChangeLanguage (String nativeLanguage)
	{
		if (nativeLanguage.equals ("Deutsch"))
		{
			locale = new Locale ("de");
		}
		else if (nativeLanguage.equals ("English"))
		{
			locale = new Locale ("en");
		}
	}

	public ChangeLanguage (Locale locale)
	{
		this.locale = locale;
	}

	/**
	 * Set the command Properties
	 *
	 * @param properties The properties.
	 */
	public void setProperties (Properties properties)
	{
	}

	/**
	 * ConnectToServer
	 */
	public void perform ()
	{
		Client.instance ().getClientGUI ().getDesktopManager ().saveVisibleDisplays ();
		Client.instance ().getClientGUI ().getDesktopManager ().closeAllDisplays ();

		Engine engine = Engine.instance ();

		String resourceDir = engine.getSystemDir () + engine.getFileSeparator () + "resources"
						+ engine.getFileSeparator ();

		PluginManager pluginManager = engine.getPluginManager ();
		ResourceService resourceService = engine.getResourceService ();

		resourceService.loadTranslationsWithClassLoader (IritgoEngine.class, "/resources/system");

		pluginManager.unloadTranslationResources ();

		resourceService.updateResourceBundle (locale);

		resourceService.unloadTranslationsWithClassLoader (IritgoEngine.class, "/resources/system");

		pluginManager.loadTranslationResources ();

		Client.instance ().getClientGUI ().getDesktopManager ().showSavedDisplays ();

		AppContext.instance ().setLocale (locale);
	}
}
