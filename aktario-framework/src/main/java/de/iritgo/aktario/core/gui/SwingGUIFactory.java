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


import org.swixml.SwingTagLibrary;
import java.util.Properties;


/**
 * This is a GUI factory that creates swing components.
 */
public class SwingGUIFactory implements IGUIFactory
{
	/**
	 * Create a new SwingGUIFactory.
	 */
	public SwingGUIFactory ()
	{
		SwingTagLibrary.getInstance ().registerTag ("ilabel", ILabel.class);
		SwingTagLibrary.getInstance ().registerTag ("ifieldlabel", IFieldLabel.class);
		SwingTagLibrary.getInstance ().registerTag ("ibutton", IButton.class);
		SwingTagLibrary.getInstance ().registerTag ("icheckbox", ICheckBox.class);
		SwingTagLibrary.getInstance ().registerTag ("icombobox", IComboBox.class);
		SwingTagLibrary.getInstance ().registerTag ("imenubar", IMenuBar.class);
		SwingTagLibrary.getInstance ().registerTag ("imenu", IMenu.class);
		SwingTagLibrary.getInstance ().registerTag ("imenuitem", IMenuItem.class);
		SwingTagLibrary.getInstance ().registerTag ("itoolbar", IToolBar.class);
		SwingTagLibrary.getInstance ().registerTag ("iradiobutton", IRadioButton.class);
		SwingTagLibrary.getInstance ().registerTag ("ititledpanel", ITitledPanel.class);
		SwingTagLibrary.getInstance ().registerTag ("iclocktextfield", IClockTextField.class);
		SwingTagLibrary.getInstance ().registerTag ("itextfield", ITextField.class);
		SwingTagLibrary.getInstance ().registerTag ("iformattedtextfield", IFormattedTextField.class);
		SwingTagLibrary.getInstance ().registerTag ("ibusybutton", IBusyButton.class);
	}

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
	 * @param properties Additional properties.
	 */
	public IWindowFrame createWindowFrame (IWindow window, String titleKey, boolean resizable, boolean closable,
					boolean maximizable, boolean iconifiable, boolean titlebar, boolean initVisible,
					Properties properties)
	{
		return new SwingWindowFrame (window, titleKey, resizable, closable, maximizable, iconifiable, titlebar,
						initVisible);
	}

	/**
	 * Create a dialog frame.
	 *
	 * @param dialog The IDialog to which this frame belongs.
	 * @param titleKey The dialog title specified as a resource key.
	 * @param properties Creation properties.
	 * @return The new dialog frame.
	 */
	public IDialogFrame createDialogFrame (IDialog dialog, String titleKey, Properties properties)
	{
		if (properties.get ("parent") != null)
		{
			return new SwingDialogFrame (dialog, titleKey, (IDialogFrame) ((IDialog) properties.get ("parent"))
							.getDialogFrame ());
		}

		return new SwingDialogFrame (dialog, titleKey);
	}
}
