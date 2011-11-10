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


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.iobject.IObjectProxy;
import de.iritgo.aktario.framework.base.DataObject;
import de.iritgo.aktario.framework.base.action.FrameworkInputStream;
import de.iritgo.aktario.framework.base.action.FrameworkOutputStream;
import de.iritgo.aktario.framework.base.action.FrameworkServerAction;
import java.io.IOException;


/**
 *
 */
public class AddDataObjectResponse extends FrameworkServerAction
{
	private long oldUniqueId;

	private long newUniqueId;

	private String iObjectTypeId;

	/**
	 * Standard constructor
	 */
	public AddDataObjectResponse()
	{
	}

	/**
	 * Standard constructor
	 */
	public AddDataObjectResponse(long oldUniqueId, long newUniqueId, String iObjectTypeId)
	{
		this.oldUniqueId = oldUniqueId;
		this.newUniqueId = newUniqueId;
		this.iObjectTypeId = iObjectTypeId;
	}

	/**
	 * Read the attributes from the given stream.
	 */
	public void readObject(FrameworkInputStream stream) throws IOException, ClassNotFoundException
	{
		oldUniqueId = stream.readLong();
		newUniqueId = stream.readLong();
		iObjectTypeId = stream.readUTF();
	}

	/**
	 * Write the attributes to the given stream.
	 */
	public void writeObject(FrameworkOutputStream stream) throws IOException
	{
		stream.writeLong(oldUniqueId);
		stream.writeLong(newUniqueId);
		stream.writeUTF(iObjectTypeId);
	}

	/**
	 * Perform the action.
	 */
	public void perform()
	{
		DataObject dataObject = (DataObject) Engine.instance().getBaseRegistry().get(oldUniqueId, iObjectTypeId);

		DataObject exists = (DataObject) Engine.instance().getBaseRegistry().get(newUniqueId, iObjectTypeId);

		if (exists != null)
		{
			// TODO: Bitte loeschen falls dieser Abschnitt nicht mehr aufgerufen werden sollte.
			System.out.println("ProxyLinkedListAddAction.perform. Object exists, mail me!");

			Engine.instance().getBaseRegistry().remove(dataObject);

			IObjectProxy oldProxy = (IObjectProxy) Engine.instance().getProxyRegistry().getProxy(oldUniqueId,
							exists.getTypeId());

			Engine.instance().getProxyRegistry().removeProxy(oldProxy, exists.getTypeId());

			return;
		}

		Engine.instance().getBaseRegistry().remove(dataObject);

		dataObject.setUniqueId(newUniqueId);
		Engine.instance().getBaseRegistry().add(dataObject);

		IObjectProxy oldProxy = (IObjectProxy) Engine.instance().getProxyRegistry().getProxy(oldUniqueId,
						dataObject.getTypeId());

		Engine.instance().getProxyRegistry().removeProxy(oldProxy, dataObject.getTypeId());

		oldProxy.setRealObject(dataObject);
		Engine.instance().getProxyRegistry().addProxy(oldProxy, dataObject.getTypeId());

		return;
	}
}
