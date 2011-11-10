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
import de.iritgo.aktario.core.iobject.NoSuchIObjectException;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.aktario.framework.base.IObjectModifiedEvent;
import de.iritgo.aktario.framework.user.User;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


/**
 *
 */
public class ProxyAction extends FrameworkAction
{
	private long iObjectUniqueId;

	private IObject iObject;

	private String iObjectTypeId;

	/**
	 * Standard constructor
	 */
	public ProxyAction()
	{
	}

	/**
	 * Standard constructor
	 */
	public ProxyAction(long prototypeUniqueId, String prototypeTypeId, IObject prototype)
	{
		this.iObjectUniqueId = prototypeUniqueId;
		this.iObject = prototype;
		this.iObjectTypeId = prototypeTypeId;
	}

	/**
	 * Get the id of the iritgo object.
	 */
	@Override
	public String getTypeId()
	{
		return "action.proxy";
	}

	/**
	 * Get the PrototypeUnqiueId.
	 */
	public long getPrototypeUniqueId()
	{
		return iObjectUniqueId;
	}

	/**
	 * Read the attributes from the given stream.
	 */
	@Override
	public void readObject(FrameworkInputStream stream) throws IOException, ClassNotFoundException
	{
		iObjectUniqueId = stream.readLong();
		iObjectTypeId = stream.readUTF();

		if (iObjectUniqueId <= 0)
		{
			// A new object, and we have not received the real uniqueId. Now we show in the registry and try to get the real object...
			User user = AppContext.instance().getUser();
			Long newUniqueId = user.getNewObjectsMapping(new Long(iObjectUniqueId));

			if (newUniqueId != null)
			{
				iObjectUniqueId = newUniqueId.longValue();
			}
		}

		IObject proto = (IObject) Engine.instance().getBaseRegistry().get(iObjectUniqueId, iObjectTypeId);

		if (proto == null)
		{
			Log.logError("system", "ProxyAction.readObject", "Unable to find the IObject");
		}
		else
		{
			try
			{
				iObject = (IObject) Engine.instance().getIObjectFactory().newInstance(iObjectTypeId);
			}
			catch (NoSuchIObjectException x)
			{
			}

			if (iObject == null)
			{
				Log.logError("system", "ProxyAction.readObject", "Unable to create a new IObject instance");

				return;
			}

			iObject.readObject(stream);
		}
	}

	/**
	 * Write the attributes to the given stream.
	 */
	@Override
	public void writeObject(FrameworkOutputStream stream) throws IOException
	{
		stream.writeLong(iObjectUniqueId);
		stream.writeUTF(iObjectTypeId);

		Engine.instance().getBaseRegistry().get(iObject.getUniqueId(), iObject.getTypeId());
		iObject.writeObject(stream);
	}

	/**
	 * Perform the action.
	 */
	@Override
	public void perform()
	{
		assignCachedObjectToProxyPrototype(); //TODO: Performance problem?
	}

	public void assignCachedObjectToProxyPrototype()
	{
		try
		{
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();

			iObject.writeObject(new DataOutputStream(buffer));

			IObjectProxy proxy = (IObjectProxy) Engine.instance().getProxyRegistry().getProxy(iObjectUniqueId,
							iObject.getTypeId());

			proxy.update(new DataInputStream(new ByteArrayInputStream(buffer.toByteArray())));

			Engine.instance().getEventRegistry().fire("objectmodified",
							new IObjectModifiedEvent(proxy.getRealObject(), null));
		}
		catch (IOException x)
		{
			x.printStackTrace();
		}
		catch (ClassNotFoundException x)
		{
			x.printStackTrace();
		}
	}
}
