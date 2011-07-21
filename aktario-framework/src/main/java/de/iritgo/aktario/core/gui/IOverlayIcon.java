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


import javax.swing.Icon;
import java.awt.Component;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 *
 */
public class IOverlayIcon implements Icon
{
	/** Items of the overlay map. */
	protected static class Overlay
	{
		/** The x position of the overlay top/left corner. */
		public int x;

		/** The x position of the overlay top/left corner. */
		public int y;

		/** All icons of this overlay. */
		public Map icons;

		/** Currently active icon. */
		public Icon activeIcon;

		/** Create a new Overlay instance. */
		public Overlay (int x, int y)
		{
			this.x = x;
			this.y = y;
			this.icons = new HashMap ();
			activeIcon = null;
		}
	}

	/** The default background icon. */
	protected Icon backgroundIcon;

	/** The id of the background overlay. */
	public final String BACKGROUND = "BACKGROUND";

	/** The id of an empty overlay icon. */
	public final String NONE = "NONE";

	/** The id of the default overlay icon. */
	public final String DEFAULT = "DEFAULT";

	/** A list of all defined overlays. */
	protected List overlays;

	/** A map of all defined overlays. */
	protected Map overlayByName;

	/**
	 * Create a new IOverlayIcon.
	 *
	 * @param backgroundIcon The default background icon.
	 */
	public IOverlayIcon (Icon backgroundIcon)
	{
		this.backgroundIcon = backgroundIcon;
		overlays = new LinkedList ();
		overlayByName = new HashMap ();
		addOverlay (BACKGROUND, 0, 0);
		addIcon (BACKGROUND, DEFAULT, backgroundIcon);
		setIcon (BACKGROUND, DEFAULT);
	}

	/**
	 * Add an overlay to this icon.
	 * This method only describes the overlay region by giving it a name
	 * and a relative position. Add icons to an overlay by calling the
	 * addIcon() method.
	 *
	 * @param name The name of the overlay.
	 * @param x The x position of the overlay top/left corner.
	 * @param y The y position of the overlay top/left corner.
	 */
	public void addOverlay (String name, int x, int y)
	{
		Overlay overlay = new Overlay (x, y);

		overlays.add (overlay);
		overlayByName.put (name, overlay);
	}

	/**
	 * Add an icon to an overlay.
	 *
	 * @param overlayName The name of the overlay.
	 * @param iconName The name of the icon.
	 * @param icon The icon.
	 */
	public void addIcon (String overlayName, String iconName, Icon icon)
	{
		Overlay overlay = (Overlay) overlayByName.get (overlayName);

		if (overlay == null)
		{
			throw new IllegalArgumentException ("Unknown overlay specified: " + overlayName);
		}

		overlay.icons.put (iconName, icon);
	}

	/**
	 * Activate an icon.
	 *
	 * @param overlayName The name of the overlay.
	 * @param iconName The name of the icon.
	 */
	public void setIcon (String overlayName, String iconName)
	{
		Overlay overlay = (Overlay) overlayByName.get (overlayName);

		if (overlay == null)
		{
			throw new IllegalArgumentException ("Unknown overlay specified: " + overlayName);
		}

		Icon icon = (Icon) overlay.icons.get (iconName);

		if (icon == null)
		{
			throw new IllegalArgumentException ("Unknown icon specified: " + iconName);
		}

		overlay.activeIcon = icon;
	}

	/**
	 * Get the icon height. For an overlay icon this is the height of the default
	 * background icon.
	 *
	 * @return The icon height.
	 */
	public int getIconHeight ()
	{
		return backgroundIcon.getIconHeight ();
	}

	/**
	 * Get the icon width. For an overlay icon this is the width of the default
	 * background icon.
	 *
	 * @return The icon width.
	 */
	public int getIconWidth ()
	{
		return backgroundIcon.getIconWidth ();
	}

	/**
	 * Paint the icon.
	 *
	 * @param c Component that can be used for painting.
	 * @param g The graphics context to paint to.
	 * @param x The x coordinate at which to paint.
	 * @param y The y coordinate at which to paint.
	 */
	public void paintIcon (Component c, Graphics g, int x, int y)
	{
		for (Iterator i = overlays.iterator (); i.hasNext ();)
		{
			Overlay overlay = (Overlay) i.next ();

			if (overlay.activeIcon != null)
			{
				overlay.activeIcon.paintIcon (c, g, x + overlay.x, y + overlay.y);
			}
		}
	}
}
