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

package de.iritgo.aktario.core.user;


import de.iritgo.aktario.framework.base.action.FrameworkAction;
import de.iritgo.aktario.framework.base.action.FrameworkInputStream;
import de.iritgo.aktario.framework.base.action.FrameworkOutputStream;
import de.iritgo.aktario.framework.command.CommandTools;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;


/**
 * This action informs a client about the connection state (online/offline)
 * of other users.
 *
 * @version $Id: AktarioUserStateAction.java,v 1.9 2006/09/25 10:34:30 grappendorf Exp $
 */
public class AktarioUserStateAction extends FrameworkAction
{
	/** The ids of the users that have the specified state. */
	protected List userIds;

	/** The connection state. */
	protected boolean connected;

	/**
	 * Create a new action.
	 */
	public AktarioUserStateAction ()
	{
		setTypeId ("AktarioUserStateAction");
		userIds = new LinkedList ();
	}

	/**
	 * Create a new action.
	 *
	 * @param connected The connection state.
	 */
	public AktarioUserStateAction (boolean connected)
	{
		this ();
		this.connected = connected;
	}

	/**
	 * Create a new action.
	 *
	 * @param user The user that has the specified state.
	 * @param connected The connection state.
	 */
	public AktarioUserStateAction (AktarioUser user, boolean connected)
	{
		this (connected);
		addUser (user);
	}

	/**
	 * Add a user.
	 *
	 * @param user The user to add.
	 */
	public void addUser (AktarioUser user)
	{
		if (user != null)
		{
			userIds.add (new Long (user.getUniqueId ()));
		}
	}

	/**
	 * Write the attributes to a stream.
	 */
	@Override
	public void writeObject (FrameworkOutputStream stream) throws IOException
	{
		stream.writeInt (userIds.size ());

		for (Iterator i = userIds.iterator (); i.hasNext ();)
		{
			stream.writeLong (((Long) i.next ()).longValue ());
		}

		stream.writeBoolean (connected);
	}

	/**
	 * Read the attributes from a stream.
	 */
	@Override
	public void readObject (FrameworkInputStream stream) throws IOException
	{
		int numUsers = stream.readInt ();

		for (int i = 0; i < numUsers; ++i)
		{
			userIds.add (new Long (stream.readLong ()));
		}

		connected = stream.readBoolean ();
	}

	/**
	 * Perform the action.
	 */
	@Override
	public void perform ()
	{
		if (userIds.size () > 0)
		{
			Properties props = new Properties ();

			props.put ("userIds", userIds);
			props.put ("connected", new Boolean (connected));
			CommandTools.performSimple ("DisplayUserConnectionState", props);
		}
	}
}
