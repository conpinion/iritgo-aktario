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

package de.iritgo.aktario.filetransfer.actions;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.filetransfer.FileTransferManager;
import de.iritgo.aktario.framework.base.action.FrameworkInputStream;
import de.iritgo.aktario.framework.base.action.FrameworkOutputStream;
import de.iritgo.aktario.framework.base.action.FrameworkServerAction;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;


/**
 *
 */
public class StartFileTransferRequest extends FrameworkServerAction
{
	private Properties properties;

	private String fileId;

	/**
	 * Standard constructor
	 */
	public StartFileTransferRequest()
	{
	}

	/**
	 * Standard constructor
	 */
	public StartFileTransferRequest(String fileId, Properties properties)
	{
		this.properties = properties;
		this.fileId = fileId;
	}

	/**
	 * Read the attributes from the given stream.
	 */
	@Override
	public void readObject(FrameworkInputStream stream) throws IOException, ClassNotFoundException
	{
		fileId = stream.readUTF();

		ObjectInputStream s = new ObjectInputStream(stream);

		properties = (Properties) s.readObject();
	}

	/**
	 * Write the attributes to the given stream.
	 */
	@Override
	public void writeObject(FrameworkOutputStream stream) throws IOException
	{
		stream.writeUTF(fileId);

		ObjectOutputStream s = new ObjectOutputStream(stream);

		s.writeObject(properties);
	}

	/**
	 * Perform the action.
	 */
	@Override
	public void perform()
	{
		properties.put("userUniqueId", new Long(getUserUniqueId()));

		FileTransferManager fileTransferManager = (FileTransferManager) Engine.instance().getManager(
						"FileTransferManager");

		fileTransferManager.startFileTransfer(fileId, properties);
	}
}
