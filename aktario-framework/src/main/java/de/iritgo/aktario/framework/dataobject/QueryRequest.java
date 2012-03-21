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
import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.iobject.*;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.base.FrameworkProxy;
import de.iritgo.aktario.framework.base.action.FrameworkInputStream;
import de.iritgo.aktario.framework.base.action.FrameworkOutputStream;
import de.iritgo.aktario.framework.base.action.NetworkFrameworkServerAction;
import de.iritgo.aktario.framework.user.User;
import java.io.IOException;


/**
 *
 */
public class QueryRequest extends NetworkFrameworkServerAction
{
	private AbstractQuery abstractQuery;

	private String queryTypeId;

	private long queryUniqueId;

	/**
	 * Send a query request
	 */
	public QueryRequest()
	{
		setTypeId("QREQ");
	}

	/**
	 * Create a new HistoricalDataServerAction.
	 */
	public QueryRequest(AbstractQuery query)
	{
		this();
		this.abstractQuery = query;
	}

	/**
	 * Read the attributes from the a stream.
	 *
	 * @param stream The stream to read from.
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 * @throws NoSuchIObjectException
	 */
	@Override
	public void readObject(FrameworkInputStream stream) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchIObjectException
	{
		queryTypeId = stream.readUTF();
		queryUniqueId = stream.readLong();

		abstractQuery = (AbstractQuery) Engine.instance().getIObjectFactory().newInstance(queryTypeId);
		abstractQuery.readObject(stream);
	}

	/**
	 * Write the attributes to a stream.
	 *
	 * @param stream The stream to write to.
	 */
	public void writeObject(FrameworkOutputStream stream) throws IOException
	{
		stream.writeUTF(abstractQuery.getTypeId());
		stream.writeLong(abstractQuery.getUniqueId());

		abstractQuery.writeObject(stream);
	}

	/**
	 */
	public void perform()
	{
		ClientTransceiver clientTransceiver = (ClientTransceiver) transceiver;

		long newUniqueId = Engine.instance().getPersistentIDGenerator().createId();
		long oldUniqueId = abstractQuery.getUniqueId();

		abstractQuery.setUniqueId(newUniqueId);

		((User) clientTransceiver.getConnectedChannel().getCustomerContextObject()).putNewObjectsMapping(new Long(
						oldUniqueId), new Long(newUniqueId));

		IObjectProxy proxy = (IObjectProxy) new FrameworkProxy();

		proxy.setSampleRealObject((IObject) abstractQuery);
		Engine.instance().getBaseRegistry().add((BaseObject) abstractQuery);
		Engine.instance().getProxyRegistry().addProxy(proxy, abstractQuery.getTypeId());

		DataObjectManager dataObjectManager = (DataObjectManager) Engine.instance().getManagerRegistry().getManager(
						"DataObjectManager");
		QueryRegistry queryRegistry = dataObjectManager.getQueryRegistry();

		queryRegistry.addQuery(abstractQuery);
		abstractQuery.doQuery();

		QueryResponse queryResponse = new QueryResponse(oldUniqueId, newUniqueId, abstractQuery.getTypeId());

		clientTransceiver.addReceiver(clientTransceiver.getSender());
		queryResponse.setTransceiver(clientTransceiver);
		queryResponse.setUniqueId(getUniqueId());
		ActionTools.sendToClient(queryResponse);
	}
}
