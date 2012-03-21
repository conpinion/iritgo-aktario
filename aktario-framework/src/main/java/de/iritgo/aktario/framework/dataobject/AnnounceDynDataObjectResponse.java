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

package de.iritgo.aktario.framework.dataobject;


import java.io.*;

import de.iritgo.aktario.core.*;
import de.iritgo.aktario.framework.base.action.*;


/**
 *
 */
public class AnnounceDynDataObjectResponse extends FrameworkAction
{
	private DynDataObject dynDataObject;

	/**
	 *
	 */
	public AnnounceDynDataObjectResponse()
	{
		setTypeId("ADDORES");
	}

	/**
	 *
	 */
	public AnnounceDynDataObjectResponse(DynDataObject dynDataObject)
	{
		this();
		this.dynDataObject = dynDataObject;
	}

	/**
	 * Read the attributes from the a stream.
	 *
	 * @param stream The stream to read from.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	@Override
	public void readObject(FrameworkInputStream stream) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		String dynDataObjectTypeId = stream.readUTF();

		dynDataObject = new DynDataObject(dynDataObjectTypeId);

		dynDataObject.readTypeInformations(stream, dynDataObject);
	}

	/**
	 * Write the attributes to a stream.
	 *
	 * @param stream The stream to write to.
	 */
	public void writeObject(FrameworkOutputStream stream) throws IOException
	{
		stream.writeUTF(dynDataObject.getTypeId());

		dynDataObject.writeTypeInformations(stream, dynDataObject);
	}

	/**
	 * Perform the action.
	 */
	public void perform()
	{
		Engine.instance().getIObjectFactory().register(dynDataObject);
	}
}
