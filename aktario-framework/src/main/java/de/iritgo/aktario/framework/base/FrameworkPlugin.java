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

package de.iritgo.aktario.framework.base;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.plugin.Plugin;
import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.aktario.framework.appcontext.ServerAppContext;
import de.iritgo.aktario.framework.console.ConsoleCommand;
import de.iritgo.aktario.framework.console.ConsoleCommandRegistry;
import de.iritgo.aktario.framework.console.ConsoleManager;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 *
 */
public abstract class FrameworkPlugin extends Plugin
{
	private ConsoleCommandRegistry consoleCommandRegistry;

	private List consoleCommands;

	private List dataObjects;

	/**
	 * Standard constructor
	 */
	public FrameworkPlugin()
	{
		super();

		consoleCommands = new LinkedList();
		dataObjects = new LinkedList();

		consoleCommandRegistry = ((ConsoleManager) engine.getManagerRegistry().getManager("console"))
						.getConsoleCommandRegistry();
	}

	/**
	 * Init the Plugin
	 *
	 * @param engine The base Engine.
	 */
	@Override
	public void init(Engine engine)
	{
		if (AppContext.instance().getClient())
		{
			setMode(Plugin.CLIENT);
		}

		if (ServerAppContext.serverInstance().getServer())
		{
			setMode(Plugin.SERVER);
		}

		registerDataObjects();

		super.init(engine);

		registerConsoleCommands();
	}

	/**
	 * Unload Plugin
	 *
	 * @param engine The Engine
	 */
	@Override
	public void unloadPlugin(Engine engine)
	{
		super.unloadPlugin(engine);

		for (Iterator i = consoleCommands.iterator(); i.hasNext();)
		{
			consoleCommandRegistry.remove((ConsoleCommand) i.next());
		}

		for (Iterator i = dataObjects.iterator(); i.hasNext();)
		{
			iObjectFactory.remove((IObject) i.next());
		}
	}

	/**
	 * Register a manager.
	 */
	protected void registerConsoleCommand(ConsoleCommand consoleCommand)
	{
		consoleCommandRegistry.add(consoleCommand);
		consoleCommands.add(consoleCommand);
	}

	/**
	 * Register a manager.
	 */
	protected void registerConsoleCommand(int mode, ConsoleCommand consoleCommand)
	{
		if (getMode() == mode)
		{
			registerConsoleCommand(consoleCommand);
		}
	}

	/**
	 * Register a data object.
	 *
	 * @param dataObject The data object to register.
	 */
	protected void registerDataObject(DataObject dataObject)
	{
		iObjectFactory.register(dataObject);
		dataObjects.add(dataObject);
	}

	/**
	 * Register a data object.
	 *
	 * @param mode Specifies wether this is a client or server manager.
	 * @param dataObject The data object to register.
	 */
	protected void registerDataObject(int mode, DataObject dataObject)
	{
		if (getMode() == mode)
		{
			registerDataObject(dataObject);
		}
	}

	/**
	 * Register all data objects in this method.
	 */
	protected void registerDataObjects()
	{
	}

	/**
	 * Register all console commands in this method.
	 */
	protected void registerConsoleCommands()
	{
	}
}
