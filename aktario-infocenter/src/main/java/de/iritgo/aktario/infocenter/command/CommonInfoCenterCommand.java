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

package de.iritgo.aktario.infocenter.command;


import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.framework.user.User;
import java.util.Properties;


/**
 *
 */
public class CommonInfoCenterCommand extends Command
{
	protected int context;

	protected String category;

	protected String icon;

	protected String message;

	protected String guiPaneId;

	protected long uniqueId;

	protected String iObjectTypeId;

	protected int level;

	protected User user;

	/**
	 * Standard constructor
	 *
	 */
	public CommonInfoCenterCommand (String id)
	{
		super (id);
	}

	/**
	 * Set the command Properties
	 *
	 * @param properties The properties.
	 */
	public void setProperties (Properties properties)
	{
		super.setProperties (properties);
		context = Integer.parseInt (properties.getProperty ("context", "0"));
		category = properties.getProperty ("category", "system");
		icon = properties.getProperty ("icon", "none");
		message = properties.getProperty ("message", "no message");
		guiPaneId = properties.getProperty ("guiPaneId", "none");
		uniqueId = Long.parseLong (properties.getProperty ("uniqueId", "0"));
		level = Integer.parseInt (properties.getProperty ("level", "0"));
		user = (User) properties.get ("user");
		iObjectTypeId = (String) properties.get ("typeId");
	}
}
