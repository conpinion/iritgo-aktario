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

package de.iritgo.aktario.infocenter.infocenter;


import de.iritgo.aktario.framework.user.User;
import java.util.ArrayList;
import java.util.Hashtable;


/**
 *
 */
public class InfoCenter
{
	//	Attribute
	static Hashtable categoryRegistry = null;

	static Hashtable displayRegistry = null;

	// Category's  
	static public int INFORMATION = 1;

	static public int WARNING = 2;

	static public int FATAL = 10;

	/**
	 *        Constructor
	 *
	 */
	public InfoCenter ()
	{
	}

	/**
	 * Set the displayRegistry
	 */
	public static void setDisplayRegistrys (Hashtable h1, Hashtable h2)
	{
		categoryRegistry = h1;
		displayRegistry = h2;
	}

	/**
	 */
	public static void info (User user, int context, String category, String icon, String message, String guiPaneId,
					long uniqueId, String iObjectTypeId, int level)
	{
		if (categoryRegistry == null)
		{
			return;
		}

		if (! categoryRegistry.containsKey (category + context))
		{
			return;
		}

		ArrayList loggerlist = (ArrayList) categoryRegistry.get (category + context);

		for (int i = 0; i < loggerlist.size (); ++i)
		{
			((InfoCenterDisplay) displayRegistry.get (loggerlist.get (i))).info (user, context, category, icon,
							message, guiPaneId, uniqueId, iObjectTypeId, level);
		}
	}

	/**
	 *        info
	 *
	 *        @param category
	 *        @param icon
	 *        @param message
	 */
	public static void info (User user, int context, String category, String icon, String message)
	{
		info (user, context, category, icon, message, "", 0, "", 0);
	}

	/**
	 *        info
	 *
	 *        @param category
	 *        @param message
	 */
	public static void info (User user, int context, String category, String message)
	{
		info (user, context, category, "", message, "", 0, "", 0);
	}
}
