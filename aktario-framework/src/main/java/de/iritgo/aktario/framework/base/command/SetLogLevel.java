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

package de.iritgo.aktario.framework.base.command;


import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.core.logger.Log;
import java.util.Properties;


/**
 *
 */
public class SetLogLevel extends Command
{
	private int level;

	public SetLogLevel()
	{
		super("setloglevel");
	}

	public SetLogLevel(String level)
	{
		this.level = new Integer(level).intValue();
	}

	/**
	 * Set the command Properties
	 *
	 * @param properties The properties.
	 */
	@Override
	public void setProperties(Properties properties)
	{
	}

	/**
	 * ConnectToServer
	 */
	@Override
	public void perform()
	{
		Log.setLevel(level);
	}
}
