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

package de.iritgo.aktario.filetransfer.services;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.filetransfer.FileTransferContext;
import de.iritgo.aktario.filetransfer.FileTransferEvent;
import de.iritgo.aktario.filetransfer.actions.EndFileTransferRequest;
import de.iritgo.aktario.filetransfer.actions.StartFileTransferRequest;
import de.iritgo.aktario.filetransfer.actions.StartFileTransferResponse;
import de.iritgo.aktario.filetransfer.actions.TransferRequest;
import de.iritgo.aktario.framework.action.ActionTools;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class Client2ServerService extends FileTransferService
{
	public static int BUFFERSIZE = FileTransferContext.TRANSFER_BUFFER_SIZE;

	File file;

	boolean server;

	BufferedInputStream bufferedInputStream;

	BufferedOutputStream bufferedOutputStream;

	byte[] buffer = new byte[BUFFERSIZE];

	public Client2ServerService()
	{
	}

	@Override
	public void startClientTransfer(FileTransferContext fileTransferContext)
	{
		try
		{
			file = new File(fileTransferContext.getPath() + fileTransferContext.getFilename());
			bufferedInputStream = new BufferedInputStream(new FileInputStream(file), BUFFERSIZE);
			fileTransferContext.setFileSize(file.length());

			ActionTools.sendToServer(new StartFileTransferRequest(fileTransferContext.getFileId(), fileTransferContext
							.getProperties()));

			Engine.instance().getEventRegistry().fire("filetransfer",
							new FileTransferEvent(FileTransferContext.START, fileTransferContext));
		}
		catch (Exception x)
		{
		}
	}

	@Override
	public void startServerTransfer(FileTransferContext fileTransferContext)
	{
		try
		{
			file = File.createTempFile("-" + fileTransferContext.getFileId() + "-", ".tmp");
			fileTransferContext.setFilename(file.getName());
			fileTransferContext.setPath(file.getPath());

			bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
			ActionTools.sendToClient(fileTransferContext.getUserUniqueId(), new StartFileTransferResponse(
							fileTransferContext.getFileId(), fileTransferContext.getProperties()));

			Engine.instance().getEventRegistry().fire("filetransfer",
							new FileTransferEvent(FileTransferContext.START, fileTransferContext));
		}
		catch (Exception x)
		{
			System.out.println(x);
			x.printStackTrace();
		}
	}

	@Override
	public int clientTransfer(FileTransferContext fileTransferContext)
	{
		try
		{
			int readed = bufferedInputStream.read(buffer, 0, BUFFERSIZE);

			if (readed == - 1)
			{
				return - 1;
			}

			fileTransferContext.calculateSendedBytes(readed);

			ActionTools.sendToServer(new TransferRequest(fileTransferContext.getFileId(), buffer, readed));

			Engine.instance().getEventRegistry().fire("filetransfer",
							new FileTransferEvent(FileTransferContext.TRANSFER, fileTransferContext));

			return readed;
		}
		catch (Exception x)
		{
		}

		return - 1;
	}

	@Override
	public int serverTransfer(FileTransferContext fileTransferContext)
	{
		try
		{
			bufferedOutputStream.write(data, 0, data.length);

			Engine.instance().getEventRegistry().fire("filetransfer",
							new FileTransferEvent(FileTransferContext.TRANSFER, fileTransferContext));
		}
		catch (Exception x)
		{
		}

		return data.length;
	}

	@Override
	public void endClientTransfer(FileTransferContext fileTransferContext)
	{
		try
		{
			bufferedInputStream.close();
			ActionTools.sendToServer(new EndFileTransferRequest(fileTransferContext.getFileId()));

			Engine.instance().getEventRegistry().fire("filetransfer",
							new FileTransferEvent(FileTransferContext.END, fileTransferContext));
		}
		catch (Exception x)
		{
			x.printStackTrace();
		}
	}

	@Override
	public void endServerTransfer(FileTransferContext fileTransferContext)
	{
		try
		{
			bufferedOutputStream.flush();
			bufferedOutputStream.close();

			Engine.instance().getEventRegistry().fire("filetransfer",
							new FileTransferEvent(FileTransferContext.END, fileTransferContext));
		}
		catch (Exception x)
		{
			x.printStackTrace();
		}
	}

	@Override
	public FileTransferService clone()
	{
		return new Client2ServerService();
	}
}
