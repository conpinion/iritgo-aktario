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

package de.iritgo.aktario.framework.base.action;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.iobject.IObjectProxy;
import de.iritgo.aktario.framework.base.DataObject;
import java.io.IOException;


/**
 *
 */
public class ProxyLinkedListAddAction extends FrameworkServerAction
{
	private long oldUniqueId;

	private long newUniqueId;

	private long ownerUniqueId;

	private String ownerTypeId;

	private String iObjectTypeId;

	/**
	 * Standard constructor
	 */
	public ProxyLinkedListAddAction()
	{
	}

	/**
	 * Standard constructor
	 */
	public ProxyLinkedListAddAction(long oldUniqueId, long newUniqueId, String iObjectTypeId, long parentUniqueId,
					String ownerTypeId)
	{
		this.oldUniqueId = oldUniqueId;
		this.newUniqueId = newUniqueId;
		this.ownerUniqueId = parentUniqueId;
		this.iObjectTypeId = iObjectTypeId;
		this.ownerTypeId = ownerTypeId;
	}

	/**
	 * Read the attributes from the given stream.
	 */
	@Override
	public void readObject(FrameworkInputStream stream) throws IOException, ClassNotFoundException
	{
		oldUniqueId = stream.readLong();
		newUniqueId = stream.readLong();
		ownerUniqueId = stream.readLong();
		iObjectTypeId = stream.readUTF();
		ownerTypeId = stream.readUTF();
	}

	/**
	 * Write the attributes to the given stream.
	 */
	@Override
	public void writeObject(FrameworkOutputStream stream) throws IOException
	{
		stream.writeLong(oldUniqueId);
		stream.writeLong(newUniqueId);
		stream.writeLong(ownerUniqueId);
		stream.writeUTF(iObjectTypeId);
		stream.writeUTF(ownerTypeId);
	}

	/**
	 * Perform the action.
	 */
	@Override
	public void perform()
	{
		@SuppressWarnings("unused")
		IObject parentObject = (DataObject) Engine.instance().getBaseRegistry().get(ownerUniqueId, ownerTypeId);

		DataObject dataObject = (DataObject) Engine.instance().getBaseRegistry().get(oldUniqueId, iObjectTypeId);

		DataObject exists = (DataObject) Engine.instance().getBaseRegistry().get(newUniqueId, iObjectTypeId);

		if (exists != null)
		{
			// TODO: Delete me if this part is not called any more.
			System.out.println("ProxyLinkedListAddAction.perform. Object exists, mail me!");

			Engine.instance().getBaseRegistry().remove(dataObject);

			IObjectProxy oldProxy = (IObjectProxy) Engine.instance().getProxyRegistry().getProxy(oldUniqueId,
							dataObject.getTypeId());

			Engine.instance().getProxyRegistry().removeProxy(oldProxy, dataObject.getTypeId());

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
