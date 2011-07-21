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


import de.iritgo.aktario.filetransfer.services.FileTransferService;
import java.util.Properties;


public class FileTransferContext
{
	public static int TRANSFER_BUFFER_SIZE = 4096;

	public static int START = 1;

	public static int TRANSFER = 2;

	public static int END = 3;

	public static int ERROR = 4;

	private FileTransferService fileTransferService;

	private String filename;

	private String path;

	private long fileSize;

	private long sended;

	private String fileDescription;

	private String fileId;

	private String service;

	private Properties properties;

	private long userUniqueId;

	public FileTransferContext (FileTransferService fileTransferService, Properties properties)
	{
		this.fileTransferService = fileTransferService;
		filename = properties.getProperty ("filename", "");
		fileDescription = properties.getProperty ("fileDescription", "");
		fileId = properties.getProperty ("fileId", "");
		path = properties.getProperty ("path", "");
		service = properties.getProperty ("service", "default");

		if (properties.get ("userUniqueId") != null)
		{
			userUniqueId = ((Long) properties.get ("userUniqueId")).longValue ();
		}

		this.properties = properties;
		sended = 0;
	}

	public void startFileTransfer ()
	{
		fileTransferService.startTransfer (this);
	}

	public int fileTransfer (byte[] data)
	{
		return fileTransferService.transfer (this, data);
	}

	public void endFileTransfer ()
	{
		fileTransferService.endTransfer (this);
	}

	public void setFilename (String filename)
	{
		this.filename = filename;
	}

	public String getFilename ()
	{
		return filename;
	}

	public void setPath (String path)
	{
		this.path = path;
	}

	public String getPath ()
	{
		return path;
	}

	public void setFileSize (long fileSize)
	{
		this.fileSize = fileSize;
	}

	public long getFileSize ()
	{
		return fileSize;
	}

	public long getSended ()
	{
		return sended;
	}

	public String getFileDescription ()
	{
		return fileDescription;
	}

	public String getFileId ()
	{
		return fileId;
	}

	public String getService ()
	{
		return service;
	}

	public long getUserUniqueId ()
	{
		return userUniqueId;
	}

	public Properties getProperties ()
	{
		return properties;
	}

	public void calculateSendedBytes (int sended)
	{
		this.sended += sended;
	}
}
