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

package de.iritgo.aktario.chat.command;


import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.framework.client.command.CloseDisplay;
import de.iritgo.aktario.framework.client.command.ShowWindow;
import de.iritgo.aktario.framework.command.CommandTools;


public class ShowChatter extends Command
{
	private static boolean isChatterVisible = false;

	public ShowChatter()
	{
		super("show.chatter");
	}

	public static void setChatterIsVisible()
	{
		isChatterVisible = true;
	}

	public static void setChatterIsNotVisible()
	{
		isChatterVisible = false;
	}

	@Override
	public void perform()
	{
		if (isChatterVisible)
		{
			isChatterVisible = false;
			CommandTools.performAsync(new CloseDisplay("common.chatview"));
		}
		else
		{
			CommandTools.performAsync(new ShowWindow("common.chatview"));
			isChatterVisible = true;
		}
	}

	@Override
	public boolean canPerform()
	{
		return true;
	}
}
