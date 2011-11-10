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
import java.util.Enumeration;
import java.util.Hashtable;


/**
 *
 */
public class InfoCenterRegistry
{
	//	Attribute
	static Hashtable CategoryRegistry = new Hashtable();

	static Hashtable displayRegistry = new Hashtable();

	/**
	 * Constructor
	 *
	 */
	public InfoCenterRegistry()
	{
		InfoCenter.setDisplayRegistrys(CategoryRegistry, displayRegistry);
	}

	/**
	 * Init all Base Display
	 *
	 */
	public void initBaseDisplay()
	{
	}

	/**
	 * Add the InfoCenterDisplay for a Category
	 */
	public void addDisplay(String category, String displayId, int context, User user)
	{
		displayId = displayId + context;

		if (! CategoryRegistry.containsKey(category + context))
		{
			CategoryRegistry.put(category + context, new ArrayList());
		}

		ArrayList displayList = (ArrayList) CategoryRegistry.get(category + context);

		if (displayList.contains(displayId))
		{
			return;
		}

		displayList.add(displayId);

		((InfoCenterDisplay) displayRegistry.get(displayId)).init(category, context, user);
	}

	/**
	 * Add a new Display
	 */
	public void addDisplay(InfoCenterDisplay display, int context)
	{
		String displayId = display.getId() + context;

		if (! displayRegistry.contains(displayId))
		{
			displayRegistry.put(displayId, display);
		}
	}

	/**
	 * Remove the Display for Category
	 */
	public void removeDisplay(String category, String displayId, int context)
	{
		displayId = displayId + context;
		category = category + context;

		if (! CategoryRegistry.containsKey(category))
		{
			return;
		}

		ArrayList displayList = (ArrayList) CategoryRegistry.get(category);

		for (int i = 0; i < displayList.size(); ++i)
		{
			if (displayList.get(i).equals(displayId))
			{
				displayList.remove(i);
			}
		}
	}

	/**
	 * Remove the Display in all Categorys and the Instance
	 */
	public void removeDisplay(String displayId, int context)
	{
		displayId = displayId + context;

		Enumeration e = CategoryRegistry.elements();

		while (e.hasMoreElements())
		{
			ArrayList displayList = (ArrayList) e.nextElement();

			for (int i = 0; i < displayList.size(); ++i)
			{
				if (displayList.get(i).equals(displayId))
				{
					displayList.remove(i);
				}
			}
		}

		displayRegistry.remove(displayId);
	}

	/**
	 * close
	 */
	public void close()
	{
		Enumeration e = CategoryRegistry.elements();

		while (e.hasMoreElements())
		{
			ArrayList displayList = (ArrayList) e.nextElement();

			for (int i = 0; i < displayList.size(); ++i)
			{
				InfoCenterDisplay display = (InfoCenterDisplay) displayRegistry.get((String) displayList.get(i));

				display.release();
				displayList.remove(i);
				displayRegistry.remove(display);
			}
		}
	}
}
