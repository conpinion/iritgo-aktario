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

package de.iritgo.aktario.core.tools;


import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.core.tools.EasyHashMap;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.aktario.framework.base.action.FrameworkInputStream;
import de.iritgo.aktario.framework.base.action.FrameworkOutputStream;
import de.iritgo.aktario.framework.base.action.FrameworkServerAction;
import de.iritgo.aktario.framework.server.Server;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.UserRegistry;
import java.io.IOException;
import java.util.Iterator;


/**
 * @version $Id: GenericBroadcastAction.java,v 1.11 2006/09/25 10:34:32 grappendorf Exp $
 */
public class GenericBroadcastAction extends FrameworkServerAction
{
	/** Action direction: Execute on server. */
	public static final int SEND_TO_SERVER = 1;

	/** Action direction: Execute on client. */
	public static final int SEND_TO_CLIENT = 2;

	/** Action direction. */
	protected int direction;

	/** Wether the action should also be resend to the originator. */
	protected boolean resendToOriginator;

	/** The id of the user who is sending this action. */
	protected long originatorId;

	/** Action data. */
	protected EasyHashMap data;

	/**
	 * Create a new action.
	 *
	 * @param name The action name.
	 */
	public GenericBroadcastAction(String name)
	{
		this(name, SEND_TO_SERVER, new EasyHashMap());
	}

	/**
	 * Create a new action.
	 *
	 * @param name The action name.
	 * @param direction Action direction.
	 * @param data Action data.
	 */
	public GenericBroadcastAction(String name, int direction, EasyHashMap data)
	{
		setTypeId(name);
		this.direction = direction;
		this.data = data;
		resendToOriginator = false;

		if (AppContext.instance().getUser() != null)
		{
			this.originatorId = AppContext.instance().getUser().getUniqueId();
		}
	}

	/**
	 * Specify wether the action should also be resend to the originator or not.
	 *
	 * @param resendToOriginator If true the action is send to the originator.
	 */
	public void setResendToOriginator(boolean resendToOriginator)
	{
		this.resendToOriginator = resendToOriginator;
	}

	/**
	 * Put an integer attribute.
	 *
	 * @param key The attribute key.
	 * @param value The attribute value.
	 */
	public void put(String key, int value)
	{
		data.put(key, value);
	}

	/**
	 * Put a long attribute.
	 *
	 * @param key The attribute key.
	 * @param value The attribute value.
	 */
	public void put(String key, long value)
	{
		data.put(key, value);
	}

	/**
	 * Put a float attribute.
	 *
	 * @param key The attribute key.
	 * @param value The attribute value.
	 */
	public void put(String key, float value)
	{
		data.put(key, value);
	}

	/**
	 * Put a double attribute.
	 *
	 * @param key The attribute key.
	 * @param value The attribute value.
	 */
	public void put(String key, double value)
	{
		data.put(key, value);
	}

	/**
	 * Put a boolean attribute.
	 *
	 * @param key The attribute key.
	 * @param value The attribute value.
	 */
	public void put(String key, boolean value)
	{
		data.put(key, value);
	}

	/**
	 * Put a string attribute.
	 *
	 * @param key The attribute key.
	 * @param value The attribute value.
	 */
	public void put(String key, String value)
	{
		data.put(key, value);
	}

	/**
	 * Get an integer attribute.
	 *
	 * @param key The attribute key.
	 * @return The attribute value.
	 */
	public int getInt(String key)
	{
		return data.getInt(key);
	}

	/**
	 * Get a long attribute.
	 *
	 * @param key The attribute key.
	 * @return The attribute value.
	 */
	public long getLong(String key)
	{
		return data.getLong(key);
	}

	/**
	 * Get a float attribute.
	 *
	 * @param key The attribute key.
	 * @return The attribute value.
	 */
	public float getFloat(String key)
	{
		return data.getLong(key);
	}

	/**
	 * Get a double attribute.
	 *
	 * @param key The attribute key.
	 * @return The attribute value.
	 */
	public double getDouble(String key)
	{
		return data.getLong(key);
	}

	/**
	 * Get a boolean attribute.
	 *
	 * @param key The attribute key.
	 * @return The attribute value.
	 */
	public boolean getBoolean(String key)
	{
		return data.getBoolean(key);
	}

	/**
	 * Get a string attribute.
	 *
	 * @param key The attribute key.
	 * @return The attribute value.
	 */
	public String getString(String key)
	{
		return data.getString(key);
	}

	/**
	 * Perform the action.
	 */
	@Override
	public void perform()
	{
		if (direction == SEND_TO_SERVER)
		{
			ClientTransceiver ct = (ClientTransceiver) transceiver;
			UserRegistry userRegistry = Server.instance().getUserRegistry();

			for (Iterator i = userRegistry.userIterator(); i.hasNext();)
			{
				User user = (User) i.next();

				if (user.isOnline() && (resendToOriginator || (user.getUniqueId() != originatorId)))
				{
					ct.addReceiver(user.getNetworkChannel());
				}
			}

			GenericBroadcastAction action = new GenericBroadcastAction(getTypeId(), SEND_TO_CLIENT, data);

			action.setTransceiver(transceiver);
			ActionTools.sendToClient(action);
		}
		else
		{
			execute();
		}
	}

	/**
	 * Write the attributes to a stream.
	 */
	@Override
	public void writeObject(FrameworkOutputStream stream) throws IOException
	{
		stream.writeInt(direction);
		stream.writeBoolean(resendToOriginator);
		stream.writeLong(originatorId);
		data.writeObject(stream);
	}

	/**
	 * Read the attributes from a stream.
	 */
	@Override
	public void readObject(FrameworkInputStream stream) throws IOException
	{
		direction = stream.readInt();
		resendToOriginator = stream.readBoolean();
		originatorId = stream.readLong();
		data.readObject(stream);
	}

	/**
	 * Send the action.
	 */
	public void send()
	{
		ClientTransceiver transceiver = new ClientTransceiver(AppContext.instance().getChannelNumber());

		transceiver.addReceiver(AppContext.instance().getChannelNumber());
		setTransceiver(transceiver);
		ActionTools.sendToServer(this);
	}

	/**
	 * Subclasses should override this method to implement
	 * the action code to be executed on the clients.
	 */
	protected void execute()
	{
	}
}
