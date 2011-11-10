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


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.manager.Manager;
import de.iritgo.aktario.core.thread.Threadable;
import java.util.LinkedList;


public class FileTransferSessionManager extends Threadable implements Manager
{
	private LinkedList fileTransferSessions;

	private boolean stopFileTransfer;

	public FileTransferSessionManager()
	{
		super("FileTransferSessionManager");
	}

	public void init()
	{
		fileTransferSessions = new LinkedList();

		Engine.instance().getThreadService().add(this);
		stopFileTransfer = false;
	}

	@Override
	public void run()
	{
		int readed = 0;

		if (stopFileTransfer)
		{
			setState(Threadable.CLOSING);

			return;
		}

		FileTransferManager fileTransferManager = (FileTransferManager) Engine.instance().getManager(
						"FileTransferManager");

		if ((fileTransferSessions != null) && (fileTransferSessions.size() > 0))
		{
			synchronized (fileTransferSessions)
			{
				readed = fileTransferManager.fileTransfer((String) fileTransferSessions.getFirst(), null);

				if (readed == - 1)
				{
					fileTransferManager.endFileTransfer((String) fileTransferSessions.getFirst());
					fileTransferSessions.remove(fileTransferSessions.getFirst());
				}
			}
		}

		try
		{
			if (readed <= 0)
			{
				Thread.sleep(1000);
			}
			else if (fileTransferManager.getWaitForNextSend() != 0)
			{
				Thread.sleep(fileTransferManager.getWaitForNextSend());
			}
		}
		catch (Exception x)
		{
		}
	}

	public void addFileTransfer(String fileId)
	{
		synchronized (fileTransferSessions)
		{
			fileTransferSessions.add(fileId);
		}
	}

	public void endFileTransfer(@SuppressWarnings("unused") String fileId)
	{
	}

	public void unload()
	{
		stopFileTransfer = true;
		fileTransferSessions = null;
	}
}
