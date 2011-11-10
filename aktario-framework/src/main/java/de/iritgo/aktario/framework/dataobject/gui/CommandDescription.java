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

package de.iritgo.aktario.framework.dataobject.gui;


import de.iritgo.aktario.framework.base.DataObject;


/**
 * CommandDescription
 */
public class CommandDescription extends DataObject
{
	public CommandDescription()
	{
		super("CommandDescription");
		addAttribute("controllerUniqueId", new Long(0));
		addAttribute("iconId", "");
		addAttribute("textId", "");
		addAttribute("commandId", "");
		addAttribute("description", "");
		addAttribute("value", "");
		addAttribute("instantExecute", false);
		addAttribute("visible", true);
		addAttribute("enabled", true);
	}

	public void setControllerUniqueId(long controllerUniqueId)
	{
		setAttribute("controllerUniqueId", controllerUniqueId);
	}

	public long getControllerUniqueId()
	{
		return getLongAttribute("controllerUniqueId");
	}

	public void setIconId(String iconId)
	{
		setAttribute("iconId", iconId);
	}

	public String getIconId()
	{
		return getStringAttribute("iconId");
	}

	public void setTextId(String textId)
	{
		setAttribute("textId", textId);
	}

	public String getTextId()
	{
		return getStringAttribute("textId");
	}

	public void setCommandId(String commandId)
	{
		setAttribute("commandId", commandId);
	}

	public String getCommandId()
	{
		return getStringAttribute("commandId");
	}

	public void setValue(String value)
	{
		setAttribute("value", value);
	}

	public String getValue()
	{
		return getStringAttribute("value");
	}

	public void setDescription(String description)
	{
		setAttribute("description", description);
	}

	public String getDescription()
	{
		return getStringAttribute("description");
	}

	public void setInstantExecute(boolean instantExecute)
	{
		setAttribute("instantExecute", instantExecute);
	}

	public boolean getInstantExecute()
	{
		return getBooleanAttribute("instantExecute");
	}

	public void setVisible(boolean visible)
	{
		setAttribute("visible", visible);
	}

	public boolean getVisible()
	{
		return getBooleanAttribute("visible");
	}

	public void setEnabled(boolean enabled)
	{
		setAttribute("enabled", enabled);
	}

	public boolean getEnabled()
	{
		return getBooleanAttribute("enabled");
	}
}
