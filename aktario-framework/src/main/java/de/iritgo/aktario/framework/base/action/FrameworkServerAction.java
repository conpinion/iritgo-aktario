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
import de.iritgo.aktario.core.action.AbstractAction;
import de.iritgo.aktario.core.action.ActionProcessorRegistry;
import de.iritgo.aktario.core.iobject.*;
import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.aktario.framework.user.User;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


/**
 *
 */
public class FrameworkServerAction extends AbstractAction
{
	protected long userUniqueId;

	protected ActionProcessorRegistry actionProcessorRegistry;

	/**
	 * Standard constructor
	 */
	public FrameworkServerAction()
	{
		super(Engine.instance().getTransientIDGenerator().createId());

		actionProcessorRegistry = Engine.instance().getActionProcessorRegistry();

		AppContext appContext = AppContext.instance();

		if (appContext != null)
		{
			User user = appContext.getUser();

			if (user != null)
			{
				this.userUniqueId = user.getUniqueId();
			}
		}
	}

	/**
	 * Standard constructor
	 */
	public FrameworkServerAction(long userUniqueId)
	{
		super(Engine.instance().getTransientIDGenerator().createId());

		this.userUniqueId = userUniqueId;
		actionProcessorRegistry = Engine.instance().getActionProcessorRegistry();
	}

	/**
	 * Get the UserUniqueID.
	 */
	public long getUserUniqueId()
	{
		return userUniqueId;
	}

	/**
	 * Read the attributes from the given stream.
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws NoSuchIObjectException
	 */
	@Override
	public void readObject(DataInputStream stream) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchIObjectException
	{
		super.readObject(stream);
		userUniqueId = stream.readLong();
		readObject(new FrameworkInputStream(stream));
	}

	/**
	 * Write the attributes to the given stream.
	 */
	@Override
	public void writeObject(DataOutputStream stream) throws IOException
	{
		super.writeObject(stream);
		stream.writeLong(userUniqueId);
		writeObject(new FrameworkOutputStream(stream));
	}

	/**
	 * Read the attributes from the given stream.
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws NoSuchIObjectException
	 */
	public void readObject(@SuppressWarnings("unused") FrameworkInputStream stream)
		throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchIObjectException
	{
	}

	/**
	 * Write the attributes to the given stream.
	 */
	public void writeObject(@SuppressWarnings("unused") FrameworkOutputStream stream) throws IOException
	{
	}
}
