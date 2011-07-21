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
import java.util.Properties;


/**
 * This action informs a client about the busy state (busy/ready)
 * of another users.
 *
 * @version $Id: AktarioUserReadyAction.java,v 1.8 2006/09/25 10:34:30 grappendorf Exp $
 */
public class AktarioUserReadyAction extends FrameworkAction
{
	/** The id of the user. */
	protected long userId;

	/** The ready state. */
	protected boolean ready;

	/**
	 * Create a new action.
	 */
	public AktarioUserReadyAction ()
	{
		setTypeId ("AktarioUserReadyAction");
	}

	/**
	 * Create a new action.
	 *
	 * @param user The user.
	 * @param ready The ready state.
	 */
	public AktarioUserReadyAction (AktarioUser user, boolean ready)
	{
		this ();
		this.userId = user.getUniqueId ();
		this.ready = ready;
	}

	/**
	 * Write the attributes to a stream.
	 */
	@Override
	public void writeObject (FrameworkOutputStream stream) throws IOException
	{
		stream.writeLong (userId);
		stream.writeBoolean (ready);
	}

	/**
	 * Read the attributes from a stream.
	 */
	@Override
	public void readObject (FrameworkInputStream stream) throws IOException
	{
		userId = stream.readLong ();
		ready = stream.readBoolean ();
	}

	/**
	 * Perform the action.
	 */
	@Override
	public void perform ()
	{
		Properties props = new Properties ();

		props.put ("userId", new Long (userId));
		props.put ("ready", new Boolean (ready));
		CommandTools.performSimple ("DisplayUserReadyState", props);
	}
}
