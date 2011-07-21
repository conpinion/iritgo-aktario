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

package de.iritgo.aktario.filetransfer.commands;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.filetransfer.FileTransferManager;


/**
 *
 */
public class StartFileTransferCommand extends Command
{
	/**
	 * Create a new ShowSystemMonitor command.
	 */
	public StartFileTransferCommand ()
	{
		super ("StartFileTransferCommand");
	}

	/**
	 * Perform the command.
	 */
	@Override
	public void perform ()
	{
		FileTransferManager fileTransferManager = (FileTransferManager) Engine.instance ().getManager (
						"FileTransferManager");
		String fileId = properties.getProperty ("fileId", "id-"
						+ Engine.instance ().getTransientIDGenerator ().createId ());

		fileTransferManager.startFileTransfer (fileId, properties);
	}
}