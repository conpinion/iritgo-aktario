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


import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.core.gui.SwingGUIPane;
import de.iritgo.aktario.framework.base.DataObject;


/**
 *
 */
public class DataObjectCommand extends Command
{
	protected SwingGUIPane swingGUIPane;

	protected DataObject dataObject;

	protected String value;

	protected DataObjectButton button;

	/**
	 * Standard constructor
	 */
	public DataObjectCommand (String commandId)
	{
		super (commandId);
	}

	/**
	 * Perform the command
	 */
	public void perform ()
	{
	}

	public boolean canPerform ()
	{
		return true;
	}

	public void setDataObject (DataObject dataObject)
	{
		this.dataObject = dataObject;
	}

	public void setSwingGUIPane (SwingGUIPane swingGUIPane)
	{
		this.swingGUIPane = swingGUIPane;
	}

	public void setValue (String value)
	{
		this.value = value;
	}

	public void setDataObjectButton (DataObjectButton button)
	{
		this.button = button;
	}
}
