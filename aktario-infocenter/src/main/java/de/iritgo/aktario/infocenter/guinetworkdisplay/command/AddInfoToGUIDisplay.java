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

package de.iritgo.aktario.infocenter.guinetworkdisplay.command;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.infocenter.guinetworkdisplay.gui.GUIDisplay;
import de.iritgo.aktario.infocenter.manager.InfoCenterClientManager;


/**
 *
 */
public class AddInfoToGUIDisplay extends Command
{
	private int context;

	private String category;

	private String icon;

	private String message;

	private String guiPaneId;

	private long uniqueId;

	private int level;

	private String iObjectTypeId;

	/**
	 * Standard constructor
	 *
	 */
	public AddInfoToGUIDisplay()
	{
		super("addinfotoguidisplay");
	}

	public AddInfoToGUIDisplay(int context, String category, String icon, String message, String guiPaneId,
					long uniqueId, String iObjectTypeId, int level)
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

	public void perform()
	{
		InfoCenterClientManager infoCenterClientManager = (InfoCenterClientManager) Engine.instance()
						.getManagerRegistry().getManager("infocenterclient");

		GUIDisplay guiDisplay = infoCenterClientManager.getGUIDisplay();

		if (guiDisplay != null)
		{
			guiDisplay.addInfo(context, category, icon, message, guiPaneId, uniqueId, iObjectTypeId, level);
		}
	}
}
