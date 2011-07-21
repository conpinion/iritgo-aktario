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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * CommandDescription
 */
public class WidgetDescription extends DataObject
{
	private Map controlMapping;

	public WidgetDescription ()
	{
		super ("WidgetDescription");
		addAttribute ("controllerUniqueId", new Long (0));
		addAttribute ("iconId", "");
		addAttribute ("labelId", "");
		addAttribute ("rendererId", "");
		addAttribute ("widgetId", "");
		addAttribute ("description", "");
		addAttribute ("mandatoryField", false);
		addAttribute ("visible", true);
		addAttribute ("enabled", true);
		addAttribute ("widgets", new IObjectList ("widgets", new IObjectProxy (new DataObject ("dummy")), this));
		controlMapping = new HashMap ();
	}

	public void addControl (String key, Object component)
	{
		controlMapping.put (key, component);
	}

	public Object getControl (String key)
	{
		return (Object) controlMapping.get (key);
	}

	public void removeControl (String key)
	{
		controlMapping.remove (key);
	}

	public void setControllerUniqueId (long controllerUniqueId)
	{
		setAttribute ("controllerUniqueId", controllerUniqueId);
	}

	public long getControllerUniqueId ()
	{
		return getLongAttribute ("controllerUniqueId");
	}

	public IObjectList getWidgetDescriptions ()
	{
		return getIObjectListAttribute ("widgets");
	}

	public void addWidgetDescription (WidgetDescription widgetDescription)
	{
		getIObjectListAttribute ("widgets").add (widgetDescription);
	}

	public void removeWidgetDescription (WidgetDescription widgetDescription)
	{
		getIObjectListAttribute ("widgets").remove (widgetDescription);
	}

	public void setRendererId (String rendererId)
	{
		setAttribute ("rendererId", rendererId);
	}

	public String getRendererId ()
	{
		return getStringAttribute ("rendererId");
	}

	public void setIconId (String iconId)
	{
		setAttribute ("iconId", iconId);
	}

	public String getIconId ()
	{
		return getStringAttribute ("iconId");
	}

	public void setLabelId (String labelId)
	{
		setAttribute ("labelId", labelId);
	}

	public String getLabelId ()
	{
		return getStringAttribute ("labelId");
	}

	public void setWidgetId (String widgetId)
	{
		setAttribute ("widgetId", widgetId);
	}

	public String getWidgetId ()
	{
		return getStringAttribute ("widgetId");
	}

	public void setDescription (String description)
	{
		setAttribute ("description", description);
	}

	public String getDescription ()
	{
		return getStringAttribute ("description");
	}

	public void setMandatoryField (boolean mandatoryField)
	{
		setAttribute ("mandatoryField", mandatoryField);
	}

	public boolean getMandatoryField ()
	{
		return getBooleanAttribute ("mandatoryField");
	}

	public boolean isMandatoryField ()
	{
		return getBooleanAttribute ("mandatoryField");
	}

	public void setVisible (boolean visible)
	{
		setAttribute ("visible", visible);
	}

	public boolean getVisible ()
	{
		return getBooleanAttribute ("visible");
	}

	public boolean isVisible ()
	{
		return getBooleanAttribute ("visible");
	}

	public void setEnabled (boolean Enabled)
	{
		setAttribute ("enabled", Enabled);
	}

	public boolean getEnabled ()
	{
		return getBooleanAttribute ("enabled");
	}

	/**
	 * Check if this dataobject valid
	 *
	 * @return boolean The valid state of this object
	 */
	public boolean isValid ()
	{
		boolean valid = true;

		if (! super.isValid ())
		{
			markAsInvalid ();
			valid = false;
		}

		for (Iterator i = getWidgetDescriptions ().iterator (); i.hasNext ();)
		{
			if (! ((DataObject) i.next ()).isValid ())
			{
				valid = false;
			}
		}

		return valid;
	}
}
