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

package de.iritgo.aktario.client.command;


import de.iritgo.aktario.client.gui.AktarioGUI;
import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.core.gui.SwingDesktopManager;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.user.AktarioUserPreferences;
import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.aktario.framework.client.Client;
import de.iritgo.aktario.framework.client.command.ChangeLanguage;
import de.iritgo.aktario.framework.command.CommandTools;
import javax.swing.SwingUtilities;
import java.util.Locale;


/**
 * This command updates the gui depending on the preferences found in
 * the users preferences object.
 *
 * <table>
 *   <tr><th>Parameter</th><th>Type</th><th>Description</th></tr>
 *   <tr><td>preferences</td><td>UserPreferences</td><td>The preferences to apply</td></tr>
 * </table>
 *
 * @version $Id: ApplyPreferences.java,v 1.11 2006/09/25 10:34:30 grappendorf Exp $
 */
public class ApplyPreferences extends Command
{
	/**
	 * Create a new command object.
	 */
	public ApplyPreferences()
	{
		super("ApplyPreferences");
	}

	/**
	 * Perform the command.
	 */
	@Override
	public void perform()
	{
		if (properties.get("preferences") == null)
		{
			Log.logError("client", "ApplyPreferences", "Missing preferences instance");

			return;
		}

		final AktarioUserPreferences preferences = (AktarioUserPreferences) properties.get("preferences");

		final AktarioGUI gui = (AktarioGUI) Client.instance().getClientGUI();

		Locale locale = new Locale(preferences.getLanguage());

		if (! AppContext.instance().getLocale().equals(locale))
		{
			CommandTools.performSimple(new ChangeLanguage(locale));

			try
			{
				SwingUtilities.invokeAndWait(new Runnable()
				{
					public void run()
					{
						gui.reloadMenuBar();
						gui.reloadToolBar();
					}
				});
			}
			catch (Exception x)
			{
			}
		}

		if (! gui.getColorScheme().equals(preferences.getColorScheme()))
		{
			try
			{
				SwingUtilities.invokeAndWait(new Runnable()
				{
					public void run()
					{
						gui.setColorScheme(preferences.getColorScheme());
					}
				});
			}
			catch (Exception x)
			{
			}
		}

		((SwingDesktopManager) gui.getDesktopManager()).setDrawAlways(preferences.getAlwaysDrawWindowContents());
	}
}
