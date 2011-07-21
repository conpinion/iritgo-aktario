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

package de.iritgo.aktario.filetransfer;


import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.manager.Manager;
import de.iritgo.aktario.filetransfer.services.Client2ClientService;
import de.iritgo.aktario.filetransfer.services.Client2ServerService;
import de.iritgo.aktario.filetransfer.services.FileTransferService;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class FileTransferManager extends BaseObject implements Manager
{
	private Map fileTransferContexts;

	private Map fileTransferServices;

	private int waitForNextSend;

	public FileTransferManager ()
	{
		super ("FileTransferManager");
	}

	public void init ()
	{
		fileTransferContexts = new HashMap ();
		fileTransferServices = new HashMap ();

		registerDefaultServices ();
	}

	public void startFileTransfer (@SuppressWarnings("unused") String fileId, Properties fileTransferProperties)
	{
		FileTransferService fileTransferService = (FileTransferService) fileTransferServices
						.get (fileTransferProperties.getProperty ("service", "default"));

		if (fileTransferService == null)
		{
			return;
		}

		FileTransferContext fileTransferContext = new FileTransferContext (fileTransferService.clone (),
						fileTransferProperties);

		fileTransferContexts.put (fileTransferContext.getFileId (), fileTransferContext);
		fileTransferContext.startFileTransfer ();
	}

	public int fileTransfer (String fileId, byte[] data)
	{
		FileTransferContext fileTransferContext = (FileTransferContext) fileTransferContexts.get (fileId);

		return fileTransferContext.fileTransfer (data);
	}

	public void endFileTransfer (String fileId)
	{
		FileTransferContext fileTransferContext = (FileTransferContext) fileTransferContexts.get (fileId);

		fileTransferContext.endFileTransfer ();
	}

	private void registerDefaultServices ()
	{
		fileTransferServices.put ("client2client", new Client2ClientService ());
		fileTransferServices.put ("client2server", new Client2ServerService ());
	}

	public int getWaitForNextSend ()
	{
		return waitForNextSend;
	}

	public void setWaitForNextSend (int waitForNextSend)
	{
		this.waitForNextSend = waitForNextSend;
	}

	public void unload ()
	{
		fileTransferContexts = null;
		fileTransferServices = null;
	}
}
