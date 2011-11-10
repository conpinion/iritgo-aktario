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

package de.iritgo.aktario.infocenter.guinetworkdisplay.action;


import de.iritgo.aktario.framework.base.action.FrameworkAction;
import de.iritgo.aktario.framework.base.action.FrameworkInputStream;
import de.iritgo.aktario.framework.base.action.FrameworkOutputStream;
import de.iritgo.aktario.framework.command.CommandTools;
import de.iritgo.aktario.infocenter.guinetworkdisplay.command.AddInfoToGUIDisplay;
import java.io.IOException;


/**
 *
 */
public class InfoCenterAction extends FrameworkAction
{
	private int context;

	private String category;

	private String icon;

	private String message;

	private String guiPaneId;

	private long uniqueId;

	private String iObjectTypeId;

	private int level;

	/**
	 * Standard constructor
	 */
	public InfoCenterAction()
	{
	}

	/**
	 * Standard constructor
	 *
	 */
	public InfoCenterAction(int context, String category, String icon, String message, String guiPaneId, long uniqueId,
					String iObjectTypeId, int level)
	{
		this.context = context;
		this.category = category;
		this.icon = icon;
		this.message = message;
		this.guiPaneId = guiPaneId;
		this.uniqueId = uniqueId;
		this.iObjectTypeId = iObjectTypeId;
		this.level = level;
	}

	/**
	 * Read the attributes from the given stream.
	 */
	public void readObject(FrameworkInputStream stream) throws IOException
	{
		context = stream.readInt();
		category = stream.readUTF();
		icon = stream.readUTF();
		message = stream.readUTF();
		guiPaneId = stream.readUTF();
		uniqueId = stream.readLong();
		iObjectTypeId = stream.readUTF();
		level = stream.readInt();
	}

	/**
	 * Write the attributes to the given stream.
	 */
	public void writeObject(FrameworkOutputStream stream) throws IOException
	{
		stream.writeInt(context);
		stream.writeUTF(category);
		stream.writeUTF(icon);
		stream.writeUTF(message);
		stream.writeUTF(guiPaneId);
		stream.writeLong(uniqueId);
		stream.writeUTF(iObjectTypeId);
		stream.writeInt(level);
	}

	/**
	 * Perform the action.
	 */
	public void perform()
	{
		AddInfoToGUIDisplay displayCmd = new AddInfoToGUIDisplay(context, category, icon, message, guiPaneId, uniqueId,
						iObjectTypeId, level);

		CommandTools.performSimple(displayCmd);
	}
}
