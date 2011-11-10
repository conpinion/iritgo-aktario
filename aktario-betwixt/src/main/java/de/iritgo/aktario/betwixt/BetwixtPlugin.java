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

package de.iritgo.aktario.betwixt;


import de.iritgo.aktario.betwixt.command.LoadBaseObjects;
import de.iritgo.aktario.betwixt.command.LoadBetwixtBean;
import de.iritgo.aktario.betwixt.command.SaveBaseObjects;
import de.iritgo.aktario.betwixt.command.SaveBetwixtBean;
import de.iritgo.aktario.framework.base.FrameworkPlugin;


public class BetwixtPlugin extends FrameworkPlugin
{
	@Override
	protected void registerDataObjects()
	{
	}

	@Override
	protected void registerActions()
	{
	}

	@Override
	protected void registerGUIPanes()
	{
	}

	@Override
	protected void registerManagers()
	{
	}

	@Override
	protected void registerCommands()
	{
		registerCommand(new LoadBetwixtBean());
		registerCommand(new SaveBetwixtBean());
		registerCommand(new LoadBaseObjects());
		registerCommand(new SaveBaseObjects());
	}

	@Override
	protected void registerConsoleCommands()
	{
	}
}
