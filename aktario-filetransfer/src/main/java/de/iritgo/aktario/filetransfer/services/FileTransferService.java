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


import de.iritgo.aktario.filetransfer.FileTransferContext;
import de.iritgo.aktario.framework.IritgoEngine;


public abstract class FileTransferService
{
	protected boolean send;

	protected byte[] data;

	public abstract void startClientTransfer(FileTransferContext fileTransferContext);

	public abstract void startServerTransfer(FileTransferContext fileTransferContext);

	public void startTransfer(FileTransferContext fileTransferContext)
	{
		if (isServer())
		{
			startServerTransfer(fileTransferContext);
		}
		else
		{
			startClientTransfer(fileTransferContext);
		}
	}

	public abstract int clientTransfer(FileTransferContext fileTransferContext);

	public abstract int serverTransfer(FileTransferContext fileTransferContext);

	public int transfer(FileTransferContext fileTransferContext, byte[] data)
	{
		this.data = data;

		if (isServer())
		{
			return serverTransfer(fileTransferContext);
		}
		else
		{
			return clientTransfer(fileTransferContext);
		}
	}

	public abstract void endClientTransfer(FileTransferContext fileTransferContext);

	public abstract void endServerTransfer(FileTransferContext fileTransferContext);

	public void endTransfer(FileTransferContext fileTransferContext)
	{
		if (isServer())
		{
			endServerTransfer(fileTransferContext);
		}
		else
		{
			endClientTransfer(fileTransferContext);
		}
	}

	@Override
	public abstract FileTransferService clone();

	public boolean isServer()
	{
		return IritgoEngine.instance().isServer();
	}
}
