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
import de.iritgo.aktario.infocenter.command.CommonInfoCenterCommand;
import de.iritgo.aktario.infocenter.guinetworkdisplay.DiskWriterDisplay;
import de.iritgo.aktario.infocenter.manager.InfoCenterManager;


/**
 *
 */
public class CreateDiskWriterDisplay extends CommonInfoCenterCommand
{
	/**
	 * Standard constructor
	 *
	 */
	public CreateDiskWriterDisplay ()
	{
		super ("creatediskwriterdisplay");
	}

	/**
	 * Perform the command.
	 */
	public void perform ()
	{
		InfoCenterManager infoCenterManager = (InfoCenterManager) Engine.instance ().getManagerRegistry ().getManager (
						"infocenter");

		DiskWriterDisplay diskWriterDisplay = new DiskWriterDisplay ();

		diskWriterDisplay.setInfoStoreFile ("FILE");

		infoCenterManager.getInfoCenterRegistry ().addDisplay (diskWriterDisplay, context);
		infoCenterManager.getInfoCenterRegistry ().addDisplay (category, diskWriterDisplay.getId (), context, null);
	}
}
