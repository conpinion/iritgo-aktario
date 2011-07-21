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

package de.iritgo.aktario.chat.action;


import de.iritgo.aktario.chat.chatter.ChatManager;
import de.iritgo.aktario.chat.chatter.UserAllreadyJoindException;
import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.framework.base.action.FrameworkAction;
import de.iritgo.aktario.framework.base.action.FrameworkInputStream;
import de.iritgo.aktario.framework.base.action.FrameworkOutputStream;
import de.iritgo.aktario.framework.base.action.NetworkFrameworkServerAction;
import java.io.IOException;


public class UserJoinServerAction extends NetworkFrameworkServerAction
{
	private String userName;

	private String channel;

	public UserJoinServerAction ()
	{
	}

	public UserJoinServerAction (String userName, String channel)
	{
		this.userName = userName;
		this.channel = channel;
	}

	@Override
	public String getTypeId ()
	{
		return "server.action.userjoin";
	}

	public String getUserName ()
	{
		return userName;
	}

	@Override
	public void readObject (FrameworkInputStream stream) throws IOException, ClassNotFoundException
	{
		userName = stream.readUTF ();
		channel = stream.readUTF ();
	}

	@Override
	public void writeObject (FrameworkOutputStream stream) throws IOException
	{
		stream.writeUTF (userName);
		stream.writeUTF (channel);
	}

	@Override
	public FrameworkAction getAction (ClientTransceiver clientTransceiver)
	{
		ChatManager chatManager = (ChatManager) Engine.instance ().getManagerRegistry ().getManager ("chat.server");

		try
		{
			chatManager.joinChannel (channel, userUniqueId);
		}
		catch (UserAllreadyJoindException x)
		{
		}

		return null;
	}
}
