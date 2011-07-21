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


import de.iritgo.aktario.core.iobject.IObject;
import javax.swing.Icon;
import java.util.Properties;


/**
 * This is the interface that all high level display elements (like dialogs or
 * desktop windows ust implement).
 */
public interface IDisplay
{
	/**
	 * Get the id of this display.
	 *
	 * @return The display's id.
	 */
	public String getTypeId ();

	/**
	 * Get the on screen unique id of this display.
	 *
	 * @return The display's id.
	 */
	public String getOnScreenUniqueId ();

	/**
	 * Get the gui pane of this display.
	 *
	 * @return The gui pane.
	 */
	public GUIPane getGUIPane ();

	/**
	 * Set the gui pane of this display.
	 *
	 * @param The gui pane.
	 */
	public void setGUIPane (GUIPane guiPane);

	/**
	 * Get the id of the desktop on which this display is displayed.
	 *
	 * @return The desktop id (or null if this display is a dialog).
	 */
	public String getDesktopId ();

	/**
	 * Set the id of the desktop on which this display is displayed.
	 *
	 * @param desktopId The desktop id.
	 */
	public void setDesktopId (String desktopId);

	/**
	 * Set the desktop manager.
	 *
	 * @param desktopManager The desktop manager.
	 */
	public void setDesktopManager (IDesktopManager desktopManager);

	/**
	 * Get the desktop manager.
	 *
	 * @return The desktop manager.
	 */
	public IDesktopManager getDesktopManager ();

	/**
	 * Close this display element.
	 */
	public void close ();

	/**
	 * This method is called by classes implementing window or dialog frames.
	 */
	public void systemClose ();

	/**
	 * Get the data object shown in this display.
	 *
	 * @return The data object.
	 */
	public IObject getDataObject ();

	/**
	 * Set the display title. This title will be displayed on the
	 * display frame's title bar.
	 *
	 * @param title The new title.
	 */
	public void setTitle (String title);

	/**
	 * Get the display title.
	 *
	 * @return The display title.
	 */
	public String getTitle ();

	/**
	 * Set the display icon.
	 *
	 * @param icon The icon.
	 */
	public void setIcon (Icon icon);

	/**
	 * Get the display's icon.
	 *
	 * @return The display's icon.
	 */
	public Icon getIcon ();

	/**
	 * Set a display property.
	 *
	 * @param key The key under which to store the property.
	 * @param value The property value.
	 */
	public void putProperty (String key, Object value);

	/**
	 * Get a display property.
	 *
	 * @param key The key of the property to retrieve.
	 * @return The property value.
	 */
	public Object getProperty (String key);

	/**
	 * Set the display properties.
	 *
	 * @param properties The new properties.
	 */
	public void setProperties (Properties properties);

	/**
	 * Get the display properties.
	 *
	 * @return The display properties.
	 */
	public Properties getProperties ();

	/**
	 * Remove a display property.
	 *
	 * @param key The key of the property to remove.
	 */
	public void removeProperty (String key);

	/**
	 * Displays this display on the screen
	 */
	public void show ();

	/**
	 * Enable/disable the display.
	 *
	 * @param enabled If true the display is enabled.
	 */
	public void setEnabled (boolean enabled);

	/**
	 * Check wether the display is enabled or not.
	 *
	 * @return True if the display is enabled.
	 */
	public boolean isEnabled ();

	/**
	 * Make this display the topmost display.
	 */
	public void bringToFront ();
}
