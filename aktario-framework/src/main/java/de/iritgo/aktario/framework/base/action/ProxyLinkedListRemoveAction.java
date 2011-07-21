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
import de.iritgo.aktario.core.iobject.IObjectList;
import de.iritgo.aktario.core.iobject.IObjectProxyEvent;
import de.iritgo.aktario.framework.base.DataObject;
import java.io.IOException;


/**
 *
 */
public class ProxyLinkedListRemoveAction extends FrameworkServerAction
{
	private long uniqueId;

	private String iObjectTypeId;

	private long ownerUniqueId;

	private String ownerTypeId;

	private String iObjectListName;

	/**
	 * Standard constructor
	 */
	public ProxyLinkedListRemoveAction ()
	{
	}

	/**
	 * Standard constructor
	 */
	public ProxyLinkedListRemoveAction (long uniqueId, String iObjectTypeId, long ownerUniqueId, String ownerTypeId,
					String iObjectListName)
	{
		this.uniqueId = uniqueId;
		this.iObjectTypeId = iObjectTypeId;
		this.ownerUniqueId = ownerUniqueId;
		this.ownerTypeId = ownerTypeId;
		this.iObjectListName = iObjectListName;
	}

	/**
	 * Read the attributes from the given stream.
	 */
	@Override
	public void readObject (FrameworkInputStream stream) throws IOException, ClassNotFoundException
	{
		uniqueId = stream.readLong ();
		iObjectTypeId = stream.readUTF ();
		ownerUniqueId = stream.readLong ();
		ownerTypeId = stream.readUTF ();
		iObjectListName = stream.readUTF ();
	}

	/**
	 * Write the attributes to the given stream.
	 */
	@Override
	public void writeObject (FrameworkOutputStream stream) throws IOException
	{
		stream.writeLong (uniqueId);
		stream.writeUTF (iObjectTypeId);
		stream.writeLong (ownerUniqueId);
		stream.writeUTF (ownerTypeId);
		stream.writeUTF (iObjectListName);
	}

	/**
	 * Perform the action.
	 */
	@Override
	public void perform ()
	{
		DataObject ownerObject = (DataObject) Engine.instance ().getBaseRegistry ().get (ownerUniqueId, ownerTypeId);

		if (ownerObject == null)
		{
			// We don't have this object, we do nothing...
			return;
		}

		DataObject dataObject = (DataObject) Engine.instance ().getBaseRegistry ().get (uniqueId, iObjectTypeId);

		IObjectList iObjectList = ownerObject.getIObjectListAttribute (iObjectListName);

		iObjectList.removeIObject (dataObject);

		Engine.instance ().getProxyEventRegistry ().fire (ownerObject, new IObjectProxyEvent (ownerObject, false));
		Engine.instance ().getEventRegistry ().fire ("proxyisuptodate", new IObjectProxyEvent (ownerObject, false));
	}
}
