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
import java.awt.Rectangle;
import java.util.Properties;


/**
 * Interface to be implmented by window frames.
 */
public interface IWindowFrame
{
	/**
	 * Set the window properties.
	 *
	 * @param properties Set the propterties.
	 */
	public void setProperties(Properties properties);

	/**
	 * Set the window bounds.
	 *
	 * @param bounds The new bounds.
	 */
	public void setBounds(Rectangle bounds);

	/**
	 * Get the window bounds.
	 *
	 * @return The bounds.
	 */
	public Rectangle getBounds();

	/**
	 * Set the window title.
	 *
	 * @param title The new title.
	 */
	public void setTitle(String title);

	/**
	 * Get the window title.
	 *
	 * @return The title.
	 */
	public String getTitle();

	/**
	 * Set the dialog icon.
	 *
	 * @param icon The new icon.
	 */
	public void setIcon(Icon icon);

	/**
	 * Get the dialog's icon.
	 *
	 * @return The icon.
	 */
	public Icon getIcon();

	/**
	 * Close the window frame.
	 */
	public void close();

	/**
	 * Close the window frame.
	 */
	public void systemClose();

	/**
	 * Show the window frame.
	 */
	public void showWindow();

	/**
	 * Set the maxmized state of the window frame.
	 */
	public void setMaximized(boolean maximized);

	/**
	 * Enable/disable the window frame.
	 *
	 * @param enabled If true the window frame is enabled.
	 */
	public void setEnabled(boolean enabled);

	/**
	 * Check wether the display is enabled or not.
	 *
	 * @return True if the display is enabled.
	 */
	public boolean isEnabled();

	/**
	 * Set the window name.
	 *
	 * @param name The new name.
	 */
	public void setName(String name);

	/**
	 * Get the window name.
	 *
	 * @return The name.
	 */
	public String getName();

	/**
	 * Make this window the topmost window.
	 */
	public void bringToFront();
}
