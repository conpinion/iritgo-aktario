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


import java.util.Properties;


/**
 * GUI factories are used to create specific implementations of
 * abstract gui elements like windows and dialogs.
 */
public interface IGUIFactory
{
	/**
	 * Create a new window frame.
	 *
	 * @param window The IWindow to which this frame belongs.
	 * @param titleKey The window title specified as a resource key.
	 * @param resizable True if the window should be resizable.
	 * @param closable True if the window should be closable.
	 * @param maximizable True if the window should be maximizable.
	 * @param iconifiable True if the window should be iconifiable.
	 * @param titlebar True if the title bar should be displayed.
	 * @param initVisible True if the window should be displayed.
	 * @param properties Creation properties.
	 */
	public IWindowFrame createWindowFrame(IWindow window, String titleKey, boolean resizable, boolean closable,
					boolean maximizable, boolean iconifiable, boolean titlebar, boolean initVisible,
					Properties properties);

	/**
	 * Create a dialog frame.
	 *
	 * @param dialog The IDialog to which this frame belongs.
	 * @param titleKey The dialog title specified as a resource key.
	 * @param properties Creation properties.
	 * @return The new dialog frame.
	 */
	public IDialogFrame createDialogFrame(IDialog dialog, String titleKey, Properties properties);
}
