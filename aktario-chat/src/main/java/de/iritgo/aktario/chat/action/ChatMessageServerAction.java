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
import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.framework.base.action.FrameworkAction;
import de.iritgo.aktario.framework.base.action.FrameworkInputStream;
import de.iritgo.aktario.framework.base.action.FrameworkOutputStream;
import de.iritgo.aktario.framework.base.action.NetworkFrameworkServerAction;
import de.iritgo.aktario.framework.command.CommandTools;
import java.io.IOException;
import java.util.Properties;


public class ChatMessageServerAction extends NetworkFrameworkServerAction
{
	private String chatMessage;

	private int channelId;

	private String userName;

	public ChatMessageServerAction()
	{
	}

	public ChatMessageServerAction(String chatMessage, int channelId, String userName)
	{
		super();
		this.chatMessage = chatMessage;
		this.channelId = channelId;
		this.userName = userName;
	}

	@Override
	public String getTypeId()
	{
		return "server.action.chatmessage";
	}

	@Override
	public void readObject(FrameworkInputStream stream) throws IOException, ClassNotFoundException
	{
		userName = stream.readUTF();
		chatMessage = stream.readUTF();
		channelId = stream.readInt();
	}

	@Override
	public void writeObject(FrameworkOutputStream stream) throws IOException
	{
		stream.writeUTF(userName);
		stream.writeUTF(chatMessage);
		stream.writeInt(channelId);
	}

	@Override
	public FrameworkAction getAction(ClientTransceiver clientTransceiver)
	{
		Boolean chatEnabled = (Boolean) CommandTools.performSimple("ChatEnabled", new Properties());

		if ((chatEnabled != null) && ! chatEnabled)
		{
			return null;
		}

		ChatManager chatManager = (ChatManager) Engine.instance().getManagerRegistry().getManager("chat.server");

		chatManager.messageChannel(chatMessage, channelId, userUniqueId);

		return null;
	}
}
