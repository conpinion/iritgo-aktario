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


import de.iritgo.aktario.chat.chatter.ChatClientManager;
import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.framework.base.action.FrameworkAction;
import de.iritgo.aktario.framework.base.action.FrameworkInputStream;
import de.iritgo.aktario.framework.base.action.FrameworkOutputStream;
import java.io.IOException;


public class UserJoinAction extends FrameworkAction
{
	private String userName;

	private String channel;

	public UserJoinAction ()
	{
	}

	public UserJoinAction (String userName, long userUniqueId, String channel)
	{
		super (userUniqueId);
		this.userName = userName;
		this.channel = channel;
	}

	@Override
	public String getTypeId ()
	{
		return "action.userjoin";
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
	public void perform ()
	{
		ChatClientManager chatManager = (ChatClientManager) Engine.instance ().getManagerRegistry ().getManager (
						"chat.client");

		chatManager.joinChannel (channel, userUniqueId, userName);
	}
}
