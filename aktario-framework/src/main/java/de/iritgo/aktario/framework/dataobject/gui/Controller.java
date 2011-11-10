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


import de.iritgo.aktario.core.iobject.IObjectList;
import de.iritgo.aktario.core.iobject.IObjectProxy;
import de.iritgo.aktario.framework.base.DataObject;
import java.util.Iterator;


/**
 * Controller
 */
public class Controller extends DataObject
{
	public Controller()
	{
		super("Controller");
		addAttribute("controllerTypeId", "default");

		addAttribute("widgets", new IObjectList("widgets", new IObjectProxy(new WidgetDescription()), this));

		addAttribute("commands", new IObjectList("commands", new IObjectProxy(new CommandDescription()), this));
	}

	public String getControllerTypeId()
	{
		return getStringAttribute("controllerTypeId");
	}

	public void setControllerTypeId(String controllerTypeId)
	{
		setAttribute("controllerTypeId", controllerTypeId);
	}

	public IObjectList getCommandDescriptions()
	{
		return getIObjectListAttribute("commands");
	}

	public CommandDescription getCommandDescription(String id)
	{
		CommandDescription commandDescription = null;

		for (Iterator i = getCommandDescriptions().iterator(); i.hasNext();)
		{
			commandDescription = (CommandDescription) i.next();

			if (commandDescription.getCommandId().equals(id))
			{
				return commandDescription;
			}
		}

		return null;
	}

	public WidgetDescription getWidgetDescription(String id)
	{
		WidgetDescription widgetDescription = null;

		for (Iterator i = getWidgetDescriptions().iterator(); i.hasNext();)
		{
			widgetDescription = (WidgetDescription) i.next();

			if (widgetDescription.getWidgetId().equals(id))
			{
				return widgetDescription;
			}
		}

		return null;
	}

	public void addCommandDescription(CommandDescription commandDescription)
	{
		getIObjectListAttribute("commands").add(commandDescription);
	}

	public void removeCommandDescription(CommandDescription commandDescription)
	{
		getIObjectListAttribute("commands").remove(commandDescription);
	}

	public IObjectList getWidgetDescriptions()
	{
		return getIObjectListAttribute("widgets");
	}

	public void addWidgetDescription(WidgetDescription widgetDescription)
	{
		getIObjectListAttribute("widgets").add(widgetDescription);
	}

	public void removeWidgetDescription(WidgetDescription widgetDescription)
	{
		getIObjectListAttribute("widgets").remove(widgetDescription);
	}

	/**
	 * Check if this dataobject valid
	 *
	 * @return boolean The valid state of this object
	 */
	public boolean isValid()
	{
		boolean valid = true;

		if (! super.isValid())
		{
			markAsInvalid();
			valid = false;
		}

		for (Iterator i = getCommandDescriptions().iterator(); i.hasNext();)
		{
			DataObject dataObject = (DataObject) i.next();

			if (! dataObject.isValid())
			{
				dataObject.markAsInvalid();
				valid = false;
			}
		}

		for (Iterator i = getWidgetDescriptions().iterator(); i.hasNext();)
		{
			DataObject dataObject = (DataObject) i.next();

			if (! dataObject.isValid())
			{
				dataObject.markAsInvalid();
				valid = false;
			}
		}

		return valid;
	}
}
