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
import de.iritgo.aktario.core.action.Action;
import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.iobject.IObjectProxy;
import de.iritgo.aktario.core.iobject.IObjectProxyListener;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.aktario.framework.base.DataObject;
import de.iritgo.aktario.framework.base.FrameworkProxy;
import de.iritgo.aktario.framework.user.User;


/**
 * Helper class for DataObjects
 */
public class DataObjectTools
{
	/**
	 * Register a new dynamic data object on the server. The dynamic will announced on the server.
	 * The client must logged into the server to use this method.
	 */
	public static void registerDynDataObject (DynDataObject dynDataObject, ClientTransceiver clientTransceiver)
	{
		Action action = new AnnounceDynDataObjectRequest (dynDataObject);

		if (clientTransceiver == null)
		{
			clientTransceiver = new ClientTransceiver (AppContext.instance ().getChannelNumber ());
		}

		clientTransceiver.addReceiver (AppContext.instance ().getChannelNumber ());
		action.setTransceiver (clientTransceiver);

		Engine.instance ().getActionProcessorRegistry ().get ("Client.SendEntryNetworkActionProcessor")
						.perform (action);
	}

	/**
	 * Register a new dynamic data object on the current instance. This method is used on startup from
	 * the server and client. The dynamic data object must the same on server and client. The object
	 * will not transferd between the client and server.
	 *
	 * @param dynDataObject The dynamic object.
	 */
	public static void registerOnStartupDynDataObject (DynDataObject dynDataObject)
	{
		Engine.instance ().getIObjectFactory ().register (dynDataObject);
	}

	public static void registerDynDataObject (DynDataObject dynDataObject)
	{
		registerDynDataObject (dynDataObject, null);
	}

	private static void generateUniqueIdAndRegisterDataObject (DataObject dataObject)
	{
		IObjectProxy proxy = (IObjectProxy) new FrameworkProxy ();

		long tmpUniqueId = Engine.instance ().getPersistentIDGenerator ().createId () * - 1;

		dataObject.setUniqueId (tmpUniqueId);

		proxy.setSampleRealObject ((IObject) dataObject);
		Engine.instance ().getBaseRegistry ().add ((BaseObject) dataObject);
		Engine.instance ().getProxyRegistry ().addProxy (proxy, dataObject.getTypeId ());

		((User) AppContext.instance ().getUser ())
						.putNewObjectsMapping (new Long (tmpUniqueId), new Long (tmpUniqueId));
	}

	/**
	 * Register a dataobject that allready exists.
	 *
	 * @param dataobject Dataobject
	 */
	public static void registerDataObject (DataObject dataObject)
	{
		IObjectProxy proxy = (IObjectProxy) new FrameworkProxy ();

		proxy.setSampleRealObject ((IObject) dataObject);
		Engine.instance ().getBaseRegistry ().add ((BaseObject) dataObject);
		Engine.instance ().getProxyRegistry ().addProxy (proxy, dataObject.getTypeId ());
		proxy.reset ();
	}

	/**
	 * Register a dataobject that allready exists and add a proxy event listener.
	 *
	 * @param dataobject Dataobject
	 * @param iObjectProxyListener IObjectProxyListener
	 */
	public static void registerDataObject (DataObject dataObject, IObjectProxyListener iObjectProxyListener)
	{
		registerDataObject (dataObject);
		Engine.instance ().getProxyEventRegistry ().addEventListener (dataObject, iObjectProxyListener);
	}

	/**
	 * Register a dataobject that allready exists and add a proxy event listener.
	 *
	 * @param String Dataobject type
	 * @param long uniqueId of the dataobject
	 */
	public static DataObject registerDataObject (String typeId, long uniqueId)
	{
		try
		{
			DataObject dataObject = (DataObject) Engine.instance ().getIObjectFactory ().newInstance (typeId);

			dataObject.setUniqueId (uniqueId);
			registerDataObject (dataObject);

			return dataObject;
		}
		catch (Exception x)
		{
		}

		return null;
	}

	/**
	 * Register a dataobject that allready exists and add a proxy event listener.
	 *
	 * @param String Dataobject type
	 * @param long uniqueId of the dataobject
	 */
	public static DataObject registerDataObject (String typeId, long uniqueId, IObjectProxyListener iObjectProxyListener)
	{
		DataObject dataObject = registerDataObject (typeId, uniqueId);

		Engine.instance ().getProxyEventRegistry ().addEventListener (dataObject, iObjectProxyListener);

		return dataObject;
	}

	/**
	 * Add a new data object to the system without a relation to anything.
	 *
	 * @param dataObject The data object to add.
	 * @param clientTransceiver The client transceiver.
	 */
	public static void addDataObject (DataObject dataObject, ClientTransceiver clientTransceiver)
	{
		generateUniqueIdAndRegisterDataObject (dataObject);

		Action action = new AddDataObjectRequest (dataObject);

		if (clientTransceiver == null)
		{
			clientTransceiver = new ClientTransceiver (AppContext.instance ().getChannelNumber ());
		}

		clientTransceiver.addReceiver (AppContext.instance ().getChannelNumber ());
		action.setTransceiver (clientTransceiver);

		Engine.instance ().getActionProcessorRegistry ().get ("Client.SendEntryNetworkActionProcessor")
						.perform (action);
	}

	public static void addDynDataObject (DataObject dataObject)
	{
		addDataObject (dataObject, null);
	}

	public static void executeQuery (AbstractQuery abstractQuery)
	{
		generateUniqueIdAndRegisterDataObject (abstractQuery);

		Action action = new QueryRequest (abstractQuery);

		ClientTransceiver clientTransceiver = new ClientTransceiver (AppContext.instance ().getChannelNumber ());

		clientTransceiver.addReceiver (AppContext.instance ().getChannelNumber ());
		action.setTransceiver (clientTransceiver);

		Engine.instance ().getActionProcessorRegistry ().get ("Client.SendEntryNetworkActionProcessor")
						.perform (action);
	}

	public static DataObject createDataObject (String typeId, long uniqueId)
	{
		return createDynDataObject (typeId, uniqueId);
	}

	public static DataObject createDynDataObject (String typeId, long uniqueId)
	{
		DataObject dataObject = null;

		dataObject = (DataObject) Engine.instance ().getBaseRegistry ().get (uniqueId, typeId);

		if (dataObject != null)
		{
			return dataObject;
		}

		try
		{
			dataObject = (DataObject) Engine.instance ().getIObjectFactory ().newInstance (typeId);
		}
		catch (Exception x)
		{
			Log.log ("system", "DataObjecTools.createDynDataObject", "DynDataObject can not created: " + typeId
							+ " Error: " + x, Log.FATAL);

			return null;
		}

		dataObject.setUniqueId (uniqueId);

		IObjectProxy proxy = (IObjectProxy) new FrameworkProxy ();

		proxy.setSampleRealObject ((IObject) dataObject);

		Engine.instance ().getBaseRegistry ().add ((BaseObject) dataObject);
		Engine.instance ().getProxyRegistry ().addProxy (proxy, dataObject.getTypeId ());

		return dataObject;
	}
}
