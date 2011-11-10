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


import java.awt.Dimension;


/**
 *
 */
public interface IDesktopFrame
{
	/**
	 * Initialize the desktop frame.
	 */
	public void init();

	/**
	 * Show the desktop frame.
	 */
	public void show();

	/**
	 * Close the desktop frame.
	 */
	public void close();

	/**
	 * Show or hide the desktop frame.
	 *
	 * @param visible If true, the desktop is visible.
	 */
	public void setVisible(boolean visible);

	/**
	 * Check wether the desktop frame is visible or not.
	 *
	 * @return true If the desktop frame is visible.
	 */
	public boolean isVisible();

	/**
	 * Set the fullscreen state.
	 *
	 * @param fullScreen If true, the desktop frame is switched to fullscreen mode.
	 */
	public void setFullScreen(boolean fullScreen);

	/**
	 * Check wether the fullscreen mode is enabled or not.
	 *
	 * @return true If the desktop frame is in fullscreen mode.
	 */
	public boolean isFullScreen();

	/**
	 * Check wether the dektop frame supports the fullscreen mode or not.
	 *
	 * @return true If the dektop frame supports the fullscreen mode.
	 */
	public boolean canFullScreen();

	/**
	 * Return the screen size of the desktop frame.
	 *
	 * @return The screen size.
	 */
	public Dimension getScreenSize();

	/**
	 * Set the desktop frame's title.
	 *
	 * @param title The new title.
	 */
	public void setTitle(String title);

	/**
	 * Enable disable the desktop frame.
	 *
	 * @param enabled If true the desktop is enabled.
	 */
	public void setEnabled(boolean enabled);
}
