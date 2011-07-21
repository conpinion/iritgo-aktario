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

package de.iritgo.aktario.framework.dataobject.gui;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.gui.IButton;
import de.iritgo.aktario.core.gui.SwingGUIPane;
import de.iritgo.aktario.framework.base.DataObject;
import de.iritgo.aktario.framework.command.CommandTools;
import javax.swing.Icon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * DataObjectButton
 */
public class DataObjectButton extends IButton
{
	private SwingGUIPane swingGUIPane;

	private DataObject dataObject;

	private CommandDescription commandDescription;

	private String value;

	private DataObjectButton button;

	public ActionListener action = new ActionListener ()
	{
		public void actionPerformed (ActionEvent e)
		{
			String commandId = commandDescription.getCommandId ();

			DataObjectCommand dataObjectCommand = (DataObjectCommand) Engine.instance ().getCommandRegistry ().get (
							commandId);

			dataObjectCommand.setDataObject (dataObject);
			dataObjectCommand.setDataObjectButton (button);
			dataObjectCommand.setSwingGUIPane (swingGUIPane);
			dataObjectCommand.setValue (commandDescription.getValue ());
			CommandTools.performAsync (dataObjectCommand, swingGUIPane.getProperties ());
		}
	};

	/**
	 * Create a button with text.
	 *
	 * @param text The text of the button.
	 */
	public DataObjectButton (String text)
	{
		super (text);
		registerActionListener ();
	}

	/**
	 * Creates a button with initial text and an icon.
	 *
	 * @param text The text of the button.
	 * @param icon The Icon image to display on the button.
	 */
	public DataObjectButton (String text, Icon icon)
	{
		super (text, icon);
		registerActionListener ();
	}

	public void release ()
	{
		removeActionListener (action);
	}

	private void registerActionListener ()
	{
		button = this;

		addActionListener (action);
	}

	public void setDataObject (DataObject dataObject)
	{
		this.dataObject = dataObject;
	}

	public void setSwingGUIPane (SwingGUIPane swingGUIPane)
	{
		this.swingGUIPane = swingGUIPane;
	}

	public void setCommandDescription (CommandDescription commandDescription)
	{
		this.commandDescription = commandDescription;
	}

	public CommandDescription getCommandDescription ()
	{
		return commandDescription;
	}
}
