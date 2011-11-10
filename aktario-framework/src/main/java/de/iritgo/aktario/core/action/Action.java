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

package de.iritgo.aktario.core.action;


import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.base.Transceiver;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.logger.Log;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Actions are primarily used to trigger some remote functionality.
 * A client can for example execute an action on the server, or the
 * server can execute actions on the clients.
 */
public class Action extends BaseObject implements IObject
{
	/** The action transceiver. */
	protected Transceiver transceiver;

	/**
	 * Create a new action.
	 */
	public Action()
	{
	}

	/**
	 * Create a new action.
	 *
	 * @param uniqueId The unique id of the action.
	 */
	public Action(long uniqueId)
	{
		this.uniqueId = uniqueId;
	}

	/**
	 * Create a new action instance.
	 */
	public IObject create()
	{
		try
		{
			IObject newAction = (IObject) getClass().newInstance();

			newAction.setTypeId(getTypeId());

			return newAction;
		}
		catch (Exception x)
		{
			Log
							.logError("system", "Action", "Cannot create instance for action '" + getTypeId() + "': "
											+ x.toString());

			return null;
		}
	}

	/**
	 * Read the object attributes from an input stream.
	 *
	 * @param stream The input stream.
	 */
	public void readObject(InputStream stream) throws IOException, ClassNotFoundException
	{
		readObject(new DataInputStream(stream));
	}

	/**
	 * Write the object attributes to an output stream.
	 *
	 * @param stream The output stream.
	 */
	public void writeObject(OutputStream stream) throws IOException
	{
		writeObject(new DataOutputStream(stream));
	}

	/**
	 * Read the object attributes from a data input stream.
	 *
	 * @param stream The input stream.
	 */
	public void readObject(DataInputStream stream) throws IOException, ClassNotFoundException
	{
		uniqueId = stream.readLong();
	}

	/**
	 * Write the object attributes to a data output stream.
	 *
	 * @param stream The output stream.
	 */
	public void writeObject(DataOutputStream stream) throws IOException
	{
		stream.writeLong(uniqueId);
	}

	/**
	 * Perform the action.
	 * Subclasses should override this method to provide their custom
	 * action code.
	 */
	public void perform()
	{
	}

	/**
	 * Check wether the action can be performed or not.
	 * By default actions are executable. Subclasses should provide a
	 * reasonable implementation of this method.
	 *
	 * @return True if the action can be performed.
	 */
	public boolean canPerform()
	{
		return true;
	}

	/**
	 * Set the transceiver for this action.
	 *
	 * @param transceiver The new transceiver.
	 */
	public void setTransceiver(Transceiver transceiver)
	{
		this.transceiver = transceiver;
	}

	/**
	 * Get the transceiver of this action.
	 *
	 * @return The transceiver.
	 */
	public Transceiver getTransceiver()
	{
		return transceiver;
	}

	/**
	 * Serialize the object type information on this object
	 */
	public IObject writeTypeInformations(OutputStream stream, IObject iObject)
	{
		return null;
	}

	/**
	 * Read Serialize type information a given stream
	 * and do some things...
	 */
	public IObject readTypeInformations(InputStream stream, IObject iObject)
	{
		return null;
	}
}
