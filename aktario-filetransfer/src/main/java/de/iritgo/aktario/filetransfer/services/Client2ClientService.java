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


public class Client2ClientService extends FileTransferService
{
	@Override
	public void startClientTransfer(FileTransferContext fileTransferContext)
	{
	}

	@Override
	public void startServerTransfer(FileTransferContext fileTransferContext)
	{
	}

	@Override
	public int clientTransfer(FileTransferContext fileTransferContext)
	{
		return 0;
	}

	@Override
	public int serverTransfer(FileTransferContext fileTransferContext)
	{
		return 0;
	}

	@Override
	public void endClientTransfer(FileTransferContext fileTransferContext)
	{
	}

	@Override
	public void endServerTransfer(FileTransferContext fileTransferContext)
	{
	}

	@Override
	public FileTransferService clone()
	{
		return (FileTransferService) new Client2ClientService();
	}
}
