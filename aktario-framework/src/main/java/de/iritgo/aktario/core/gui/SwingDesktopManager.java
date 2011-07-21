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


import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import java.rmi.server.UID;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * Swing implementation of the IDesktopManager.
 */
public class SwingDesktopManager extends IDesktopManager
{
	/**
	 * Desktop pane information.
	 */
	private class DesktopPaneInfo
	{
		public JDesktopPane desktopPane;

		public String title;

		public DesktopPaneInfo (JDesktopPane desktopPane, String title)
		{
			this.desktopPane = desktopPane;
			this.title = title;
		}
	}

	/** All desktop panes that are currently managed. */
	private Map desktopPanes;

	/** The currently active desktop pane. */
	private JDesktopPane activeDesktopPane;

	/** The id of the active desktop pane. */
	private String activeDesktopPaneId;

	/**
	 * Create a new SwingDesktopManager.
	 */
	public SwingDesktopManager ()
	{
		desktopPanes = new HashMap ();
		displays = new LinkedList ();
	}

	/**
	 * Initialize the desktop manager.
	 *
	 * @param desktopPane The DesktopPane which will contain the displays.
	 *
	 * @deprecated The desktop manager is initialized through it's constructor. Use the
	 *   methods setDesktopPane() or addDxesktopPane() to set or add a DesktopPane.
	 */
	public void init (JDesktopPane desktopPane)
	{
		addDesktopPane ("default", desktopPane, "Default");
	}

	/**
	 * Set the DesktopPane to use.
	 *
	 * @param desktopPane The DesktopPane which will contain the displays.
	 */
	public void setDesktopPane (JDesktopPane desktopPane)
	{
		addDesktopPane ("default", desktopPane, "Default");
	}

	/**
	 * Add a DesktopPane to the desktop manager.
	 *
	 * @param id The id of the desktop pane to add.
	 * @param desktopPane The desktop pane to add.
	 * @param title The title of the desktop pane.
	 */
	public void addDesktopPaneNoActivation (String id, JDesktopPane desktopPane, String title)
	{
		desktopPanes.put (id, new DesktopPaneInfo (desktopPane, title));
	}

	/**
	 * Add a DesktopPane to the desktop manager.
	 *
	 * @param id The id of the desktop pane to add.
	 * @param desktopPane The desktop pane to add.
	 */
	public void addDesktopPaneNoActivation (String id, JDesktopPane desktopPane)
	{
		addDesktopPaneNoActivation (id, desktopPane, null);
	}

	/**
	 * Add a DesktopPane to the desktop manager.
	 *
	 * @param id The id of the desktop pane to add.
	 * @param desktopPane The desktop pane to add.
	 * @param title The title of the desktop pane.
	 */
	public void addDesktopPane (String id, JDesktopPane desktopPane, String title)
	{
		addDesktopPaneNoActivation (id, desktopPane, title);
		activeDesktopPane = desktopPane;
		activeDesktopPaneId = id;
	}

	/**
	 * Add a DesktopPane to the desktop manager.
	 *
	 * @param id The id of the desktop pane to add.
	 * @param desktopPane The desktop pane to add.
	 */
	public void addDesktopPane (String id, JDesktopPane desktopPane)
	{
		addDesktopPane (id, desktopPane, null);
	}

	/**
	 * Add a DesktopPane to the desktop manager.
	 *
	 * @param desktopPane The desktop pane to add.
	 */
	public void addDesktopPane (JDesktopPane desktopPane)
	{
		addDesktopPane (new UID ().toString (), desktopPane, "");
	}

	/**
	 * Remove a DesktopPane from the desktop manager.
	 *
	 * @param id The id of the desktop pane to remove.
	 */
	public void removeDesktopPane (String id)
	{
		DesktopPaneInfo desktopInfo = (DesktopPaneInfo) desktopPanes.get (id);

		if (desktopInfo != null)
		{
			desktopPanes.remove (id);

			if (id.equals (activeDesktopPaneId))
			{
				activeDesktopPane = null;
				activeDesktopPaneId = null;
			}
		}
	}

	/**
	 * Remove all desktop panes from the desktop manager.
	 */
	public void removeAllDesktopPanes ()
	{
		desktopPanes.clear ();
		activeDesktopPane = null;
		activeDesktopPaneId = null;
	}

	/**
	 * Get the active desktop pane.
	 *
	 * @return The currently active desktop pane.
	 */
	public JDesktopPane getActiveDesktopPane ()
	{
		return activeDesktopPane;
	}

	/**
	 * Get the id of the active desktop pane.
	 *
	 * @return The id of the currently active desktop pane.
	 */
	public String getActiveDesktopPaneId ()
	{
		return activeDesktopPaneId;
	}

	/**
	 * Activate a desktop pane.
	 *
	 * @param id The id of the desktop pane to activate.
	 */
	public void activateDesktopPane (String id)
	{
		DesktopPaneInfo desktopPaneInfo = (DesktopPaneInfo) desktopPanes.get (id);

		if (desktopPaneInfo != null)
		{
			activeDesktopPane = desktopPaneInfo.desktopPane;
			activeDesktopPaneId = id;
		}
	}

	/**
	 * Get a desktop pane.
	 *
	 * @param id The name of the desktop pane to retrieve.
	 * @return The desktop pane or null if no pane with the given exists.
	 */
	public JDesktopPane getDesktopPane (String id)
	{
		DesktopPaneInfo desktopPaneInfo = (DesktopPaneInfo) desktopPanes.get (id);

		if (desktopPaneInfo == null)
		{
			return activeDesktopPane;
		}

		return desktopPaneInfo.desktopPane;
	}

	/**
	 * Check the existence of a specific desktop pane.
	 *
	 * @param id The id of the desktop pane to check.
	 * @return True if the desktop pane exists.
	 */
	public boolean existsDesktopPane (String id)
	{
		return desktopPanes.containsKey (id);
	}

	/**
	 * Retrieve the title of a desktop pane.
	 *
	 * @param id The id of the desktop pane.
	 * @return The title of the specified pane.
	 */
	public String getTitle (String id)
	{
		DesktopPaneInfo desktopPaneInfo = (DesktopPaneInfo) desktopPanes.get (id);

		if (desktopPaneInfo == null)
		{
			return "";
		}

		return desktopPaneInfo.title;
	}

	/**
	 * Set the title of a desktop pane.
	 *
	 * @param id The id of the desktop pane to rename.
	 * @param title The new title.
	 */
	public void setTitle (String id, String title)
	{
		DesktopPaneInfo desktopPaneInfo = (DesktopPaneInfo) desktopPanes.get (id);

		if (desktopPaneInfo != null)
		{
			desktopPaneInfo.title = title;
		}
	}

	/**
	 * Add an IDisplay to the desktop manager.
	 *
	 * @param display The IDisplay to add.
	 * @param desktopId The id of the desktop on which to open the display.
	 */
	@Override
	public void addImpl (IDisplay display, String desktopId)
	{
		display.setDesktopManager (this);

		if (display.getDesktopId () == null)
		{
			display.setDesktopId (desktopId != null ? desktopId : activeDesktopPaneId);
		}
	}

	/**
	 * Get the desktop's JFrame.
	 *
	 * @return The JFrame.
	 */
	public JFrame getJFrame ()
	{
		return ((SwingDesktopFrame) desktopFrame).getJFrame ();
	}

	/**
	 * Set the drawing mode for the internal frames.
	 *
	 * @param drawAlways True if during a window resize the content
	 *   should always be redrawn.
	 */
	public void setDrawAlways (boolean drawAlways)
	{
		int mode = JDesktopPane.OUTLINE_DRAG_MODE;

		if (drawAlways)
		{
			mode = JDesktopPane.LIVE_DRAG_MODE;
		}

		for (Iterator iter = desktopPanes.values ().iterator (); iter.hasNext ();)
		{
			DesktopPaneInfo desktopPaneInfo = (DesktopPaneInfo) iter.next ();

			desktopPaneInfo.desktopPane.setDragMode (mode);
		}
	}

	/**
	 * Get an iterator over all desktop pane ids.
	 *
	 * @return A desktop pane id iterator.
	 */
	public Iterator desktopPaneIdIterator ()
	{
		return desktopPanes.keySet ().iterator ();
	}

	/**
	 * Retrieve a list of all displays on the specified desktop pane id.
	 *
	 * @param id The id of the desktop pane.
	 * @return A list of displays.
	 */
	public List getDisplaysOnDesktopPane (String id)
	{
		List displaysOnDesktop = new LinkedList ();

		for (Iterator i = displays.iterator (); i.hasNext ();)
		{
			IDisplay display = (IDisplay) i.next ();

			if (display.getDesktopId ().equals (id))
			{
				displaysOnDesktop.add (display);
			}
		}

		return displaysOnDesktop;
	}

	/**
	 * Get the current number of desktop panes.
	 *
	 * @return The number of desktop panes.
	 */
	public int getDesktopPaneCount ()
	{
		return desktopPanes.size ();
	}

	/**
	 * Close all displays on a specified pane.
	 *
	 * @param desktopId The id of the desktop.
	 */
	public void closeAllDisplays (String desktopId)
	{
		super.closeAllDisplays (desktopId);
	}

	/**
	 * Activate a display.
	 *
	 * @param activeDisplay The display to activate.
	 */
	public void activateDisplay (IDisplay activeDisplay)
	{
	}
}
