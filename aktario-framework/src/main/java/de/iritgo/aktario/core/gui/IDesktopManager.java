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
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.framework.IritgoEngine;
import de.iritgo.aktario.framework.client.Client;
import de.iritgo.aktario.framework.client.command.ShowWindow;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * A desktop manager handles virtual desktops.
 */
public abstract class IDesktopManager extends BaseObject
{
	/**
	 * Used to temporarily store display information.
	 */
	public class DisplayItem
	{
		/** Gui pane id. */
		public String guiPaneId;

		/** Id of the desktop of this display. */
		public String desktopId;

		/** Id of the IObject displayed in the gui pane. */
		public long iObjectUniqueId;

		/** Id of the IObject displayed in the gui pane. */
		public String iObjectTypeId;

		/** The properties of the gui pane. */
		public Properties properties;

		/** The current onScreen uniqueId of the guipane. */
		protected String onScreenUniqueId;

		/**
		 * Create a new DisplayItem.
		 *
		 * @param guiPaneId Gui pane id.
		 * @param desktopId Id of the desktop of this display.
		 * @param iObject IObject displayed in the gui pane.
		 */
		public DisplayItem(String guiPaneId, String onScreenUniqueId, String desktopId, IObject iObject,
						Properties properties)
		{
			this.guiPaneId = guiPaneId;
			this.desktopId = desktopId;
			this.properties = properties;
			this.onScreenUniqueId = onScreenUniqueId;

			if (iObject != null)
			{
				this.iObjectUniqueId = iObject.getUniqueId();
				this.iObjectTypeId = iObject.getTypeId();
			}
		}
	}

	/** The desktop frame. */
	protected IDesktopFrame desktopFrame;

	/** All displays on all desktops. */
	protected List displays;

	/** Mapping from display ids to displays. */
	protected Map displayById;

	/** Used to temporarily store displays. */
	private List savedDisplays;

	/** All active displays. */
	private IDisplay activeDisplay;

	/**
	 * Create a new IDesktopManager.
	 */
	public IDesktopManager()
	{
		displays = new LinkedList();
		displayById = new HashMap();
	}

	/**
	 * Add an IDisplay to the IDesktopManager.
	 *
	 * @param display The IDisplay to add.
	 */
	public void addDisplay(IDisplay display)
	{
		addDisplay(display, null);
	}

	/**
	 * Add an IDisplay to the IDesktopManager.
	 *
	 * @param display The IDisplay to add.
	 * @param desktopId The id of the desktop on which to open the display.
	 */
	public void addDisplay(IDisplay display, String desktopId)
	{
		displays.add(display);
		displayById.put(display.getOnScreenUniqueId(), display);
		addImpl(display, desktopId);
	}

	/**
	 * Get a Iterator over all Displays
	 *
	 * @return Iterator Iterator for all Displays
	 */
	public Iterator getDisplayIterator()
	{
		return displays.iterator();
	}

	/**
	 * Remove a display.
	 *
	 * @param display The display to remove.
	 */
	public void removeDisplay(IDisplay display)
	{
		if (display != null)
		{
			displays.remove(display);
			displayById.remove(display.getOnScreenUniqueId());
		}
	}

	/**
	 * Get a display by id.
	 *
	 * @param onScreenUniqueId The id of the display to retrieve.
	 * @return The display or null if no display was found.
	 */
	public IDisplay getDisplay(String onScreenUniqueId)
	{
		return (IDisplay) displayById.get(onScreenUniqueId);
	}

	/**
	 * Get a display by id.
	 *
	 * @param onScreenUniqueId The id of the display to retrieve.
	 * @return The display or null if no display was found.
	 */
	public IDisplay waitForDisplay(String onScreenUniqueId)
	{
		IDisplay display = null;

		while (display == null)
		{
			display = (IDisplay) Client.instance().getClientGUI().getDesktopManager().getDisplay(onScreenUniqueId);

			if (display == null)
			{
				try
				{
					Thread.sleep(100);
				}
				catch (InterruptedException x)
				{
				}
			}
		}

		return display;
	}

	/**
	 * Close all displays.
	 */
	public void closeAllDisplays()
	{
		List tmpDisplays = new LinkedList(displays);

		for (Iterator i = tmpDisplays.iterator(); i.hasNext();)
		{
			((IDisplay) i.next()).close();
		}
	}

	/**
	 * Close all displays on a specified pane.
	 *
	 * @param desktopId The id of the desktop.
	 */
	public void closeAllDisplays(String desktopId)
	{
		List tmpDisplays = new LinkedList(displays);

		for (Iterator i = tmpDisplays.iterator(); i.hasNext();)
		{
			IDisplay display = (IDisplay) i.next();

			if (display.getDesktopId().equals(desktopId))
			{
				display.close();
			}
		}
	}

	/**
	 * Temporarily save all currently visible displays.
	 */
	public void saveVisibleDisplays()
	{
		savedDisplays = new LinkedList();

		for (Iterator i = displays.iterator(); i.hasNext();)
		{
			IDisplay display = (IDisplay) i.next();

			if (display.getDesktopId() == null)
			{
				continue;
			}

			IObject businessObject = display.getGUIPane().getObject();

			savedDisplays.add(new DisplayItem(display.getGUIPane().getTypeId(), display.getGUIPane()
							.getOnScreenUniqueId(), display.getDesktopId(), businessObject, display.getGUIPane()
							.getProperties()));
		}
	}

	/**
	 * Redisplay all saved displays.
	 */
	public void showSavedDisplays()
	{
		ShowWindow show = null;

		for (Iterator i = savedDisplays.iterator(); i.hasNext();)
		{
			DisplayItem entry = (DisplayItem) i.next();

			GUIPane guiPane = GUIPaneRegistry.instance().create(entry.guiPaneId);

			if ((guiPane != null) && (entry.iObjectUniqueId != 0))
			{
				show = new ShowWindow(entry.guiPaneId, entry.onScreenUniqueId, entry.iObjectUniqueId,
								entry.iObjectTypeId, entry.desktopId);
			}
			else
			{
				show = new ShowWindow(entry.guiPaneId, entry.onScreenUniqueId, null, null, entry.desktopId);
			}

			show.setProperties(entry.properties);

			IritgoEngine.instance().getAsyncCommandProcessor().perform(show);
		}
	}

	/**
	 * Set the desktop frame.
	 *
	 * @param desktopFrame The desktop frame.
	 */
	public void setDesktopFrame(IDesktopFrame desktopFrame)
	{
		this.desktopFrame = desktopFrame;
	}

	/**
	 * Get the desktop frame.
	 *
	 * @return The desktop frame.
	 */
	public IDesktopFrame getDesktopFrame()
	{
		return desktopFrame;
	}

	/**
	 * Get the currently active display.
	 *
	 * @return The active display or null if no display is currently shown.
	 */
	public IDisplay getActiveDisplay()
	{
		return activeDisplay;
	}

	/**
	 * Set the currently active display.
	 *
	 * @param activeDisplay The active display.
	 */
	public void setActiveDisplay(IDisplay activeDisplay)
	{
		this.activeDisplay = activeDisplay;
	}

	/**
	 * Activate a display.
	 *
	 * @param activeDisplay The display to activate.
	 */
	public void activateDisplay(IDisplay activeDisplay)
	{
	}

	/**
	 * Add an IDisplay.
	 *
	 * @param display The IDisplay to add.
	 * @param desktopId The id of the desktop on which to open the display.
	 */
	public abstract void addImpl(IDisplay display, String desktopId);
}
