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

package de.iritgo.aktario.chat;


import de.iritgo.aktario.chat.action.ChatCloseAction;
import de.iritgo.aktario.chat.action.ChatMessageAction;
import de.iritgo.aktario.chat.action.ChatMessageServerAction;
import de.iritgo.aktario.chat.action.UserJoinAction;
import de.iritgo.aktario.chat.action.UserJoinServerAction;
import de.iritgo.aktario.chat.action.UserLeaveAction;
import de.iritgo.aktario.chat.action.UserLeaveServerAction;
import de.iritgo.aktario.chat.chatter.ChatClientManager;
import de.iritgo.aktario.chat.chatter.ChatServerManager;
import de.iritgo.aktario.chat.command.ShowChatter;
import de.iritgo.aktario.chat.command.UserJoinCommand;
import de.iritgo.aktario.chat.command.UserLeaveCommand;
import de.iritgo.aktario.chat.gui.ChatGUIPane;
import de.iritgo.aktario.core.plugin.Plugin;
import de.iritgo.aktario.framework.base.FrameworkPlugin;
import de.iritgo.aktario.framework.console.ConsoleCommand;


public class ChatPlugin extends FrameworkPlugin
{
	@Override
	protected void registerDataObjects()
	{
	}

	@Override
	protected void registerActions()
	{
		registerAction(new UserJoinAction());
		registerAction(new UserJoinServerAction());
		registerAction(new UserLeaveAction());
		registerAction(new UserLeaveServerAction());
		registerAction(new ChatMessageAction());
		registerAction(new ChatMessageServerAction());
		registerAction(new ChatCloseAction());
	}

	@Override
	protected void registerGUIPanes()
	{
		registerGUIPane(Plugin.CLIENT, new ChatGUIPane());
	}

	@Override
	protected void registerManagers()
	{
		registerManager(Plugin.CLIENT, new ChatClientManager());
		registerManager(Plugin.SERVER, new ChatServerManager());
	}

	@Override
	protected void registerCommands()
	{
		registerCommand(new ShowChatter());
	}

	@Override
	protected void registerConsoleCommands()
	{
		registerConsoleCommand(Plugin.CLIENT, new ConsoleCommand("join", new UserJoinCommand(),
						"chat.help.joinchannel", 1));
		registerConsoleCommand(Plugin.CLIENT, new ConsoleCommand("leave", new UserLeaveCommand(),
						"chat.help.leavechannel", 0));
	}
}
