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

package de.iritgo.aktario.core.user;


import de.iritgo.aktario.framework.base.DataObject;


/**
 * User preferences.
 *
 * @version $Id: AktarioUserPreferences.java,v 1.10 2006/09/25 10:34:30 grappendorf Exp $
 */
public class AktarioUserPreferences extends DataObject
{
	/**
	 * Create a new UserPreferences.
	 */
	public AktarioUserPreferences()
	{
		super("AktarioUserPreferences");

		addAttribute("colorScheme", "com.jgoodies.looks.plastic.theme.KDE");
		addAttribute("language", "de");
		addAttribute("alwaysDrawWindowContents", true);
	}

	/**
	 * Create a new UserPreferences.
	 *
	 * @param uniqueId The unique id.
	 */
	public AktarioUserPreferences(long uniqueId)
	{
		this();
		setUniqueId(uniqueId);
	}

	/**
	 * Set the language.
	 *
	 * @param language The language specified by a locale id.
	 */
	public void setLanguage(String language)
	{
		setAttribute("language", language);
	}

	/**
	 * Get the language.
	 *
	 * @return The language specified by a locale id.
	 */
	public String getLanguage()
	{
		return getStringAttribute("language");
	}

	/**
	 * Set the name of the color scheme to use.
	 *
	 * @param colorScheme The color scheme name.
	 */
	public void setColorScheme(String colorScheme)
	{
		setAttribute("colorScheme", colorScheme);
	}

	/**
	 * Get the name of the color scheme to use.
	 *
	 * @return The color scheme name.
	 */
	public String getColorScheme()
	{
		return getStringAttribute("colorScheme");
	}

	/**
	 * Determine wether to always draw window contents.
	 *
	 * @param alwaysDrawWindowContents True if the windows contents should always be drawn.
	 */
	public void setAlwaysDrawWindowContents(boolean alwaysDrawWindowContents)
	{
		setAttribute("alwaysDrawWindowContents", alwaysDrawWindowContents);
	}

	/**
	 * Check wether to always draw window contents.
	 *
	 * @return True if the windows contents should always be drawn.
	 */
	public boolean getAlwaysDrawWindowContents()
	{
		return getBooleanAttribute("alwaysDrawWindowContents");
	}
}
