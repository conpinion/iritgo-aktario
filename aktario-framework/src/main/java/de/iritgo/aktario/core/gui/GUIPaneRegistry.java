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

package de.iritgo.aktario.core.gui;


import de.iritgo.aktario.core.base.BaseObject;
import java.util.HashMap;
import java.util.Map;


/**
 * This registry contains all known gui panes.
 */
public class GUIPaneRegistry extends BaseObject
{
	/** The singleton instance. */
	private static GUIPaneRegistry guiPaneRegistry;

	/** All gui panes. */
	private Map guiPanes;

	/**
	 * Create a new gui pane registry.
	 */
	public GUIPaneRegistry()
	{
		guiPanes = new HashMap();
	}

	/**
	 * Add a gui pane.
	 *
	 * @param guiPane The gui pane to add.
	 */
	public void add(GUIPane guiPane)
	{
		guiPanes.put(guiPane.getTypeId(), guiPane);
	}

	/**
	 * Remove a gui pane.
	 *
	 * @param guiPaneId The id of the gui pane to remove.
	 */
	public void remove(String guiPaneId)
	{
		guiPanes.remove(guiPaneId);
	}

	/**
	 * Remove a gui pane.
	 *
	 * @param guiPane The gui pane to remove.
	 */
	public void remove(GUIPane guiPane)
	{
		guiPanes.remove(guiPane);
	}

	/**
	 * Retrieve a cloned gui pane.
	 * This method searches for the specified gui pane and creates and
	 * returns a clone.
	 *
	 * @param guiPaneId The id of the gui pane to retrieve.
	 */
	public GUIPane create(String guiPaneId)
	{
		return ((GUIPane) guiPanes.get(guiPaneId)).cloneGUIPane();
	}

	/**
	 * Retrieve the singleton instance.
	 *
	 * @return The gui pane registry.
	 */
	static public GUIPaneRegistry instance()
	{
		if (guiPaneRegistry == null)
		{
			guiPaneRegistry = new GUIPaneRegistry();
		}

		return guiPaneRegistry;
	}
}
