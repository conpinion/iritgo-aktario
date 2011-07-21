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
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.framework.user.User;
import java.io.IOException;


/**
 *
 */
public class ProxyServerAction extends NetworkFrameworkServerAction
{
	private long prototypeUniqueId;

	private String iObjectTypeId;

	/**
	 * Standard constructor
	 */
	public ProxyServerAction ()
	{
	}

	/**
	 * Standard constructor
	 *
	 * @param prototypeUniqueId
	 * @param iObjectTypeId
	 */
	public ProxyServerAction (long prototypeUniqueId, String iObjectTypeId)
	{
		this.prototypeUniqueId = prototypeUniqueId;
		this.iObjectTypeId = iObjectTypeId;
	}

	public long getPrototypeUniqueId ()
	{
		return prototypeUniqueId;
	}

	/**
	 * Get the id of the iritgo object.
	 */
	@Override
	public String getTypeId ()
	{
		return "server.action.proxy";
	}

	/**
	 * Read the attributes from the given stream.
	 */
	@Override
	public void readObject (FrameworkInputStream stream) throws IOException, ClassNotFoundException
	{
		prototypeUniqueId = stream.readLong ();
		iObjectTypeId = stream.readUTF ();
	}

	/**
	 * Write the attributes to the given stream.
	 */
	@Override
	public void writeObject (FrameworkOutputStream stream) throws IOException
	{
		stream.writeLong (prototypeUniqueId);
		stream.writeUTF (iObjectTypeId);
	}

	@Override
	public FrameworkAction getAction (ClientTransceiver clientTransceiver)
	{
		IObject prototype = null;

		if (prototypeUniqueId <= 0)
		{
			// Its a new object and the client has always the wrong id;
			// Look in the Mapping
			User user = (User) clientTransceiver.getConnectedChannel ().getCustomerContextObject ();

			prototype = (IObject) Engine.instance ().getBaseRegistry ().get (
							user.getNewObjectsMapping (new Long (prototypeUniqueId)).longValue (), iObjectTypeId);
		}
		else
		{
			prototype = (IObject) Engine.instance ().getBaseRegistry ().get (prototypeUniqueId, iObjectTypeId);
		}

		if (prototype == null)
		{
			return null;
		}

		clientTransceiver.addReceiver (clientTransceiver.getSender ());

		return (FrameworkAction) new ProxyAction (prototypeUniqueId, prototype.getTypeId (), prototype);
	}
}
