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

package de.iritgo.aktario.core.application;


import de.iritgo.aktario.framework.base.DataObject;


/**
 * Data object that represents a specific incarnation of an application.
 *
 * @version $Id: ApplicationInstance.java,v 1.8 2006/09/25 10:34:32 grappendorf Exp $
 */
public class ApplicationInstance extends DataObject
{
	/**
	 * Create a new ApplicationInstance.
	 */
	public ApplicationInstance ()
	{
		super ("ApplicationInstance");

		addAttribute ("applicationId", "");
		addAttribute ("name", "");
	}

	/**
	 * Get application id.
	 *
	 * @return The application id.
	 */
	public String getApplicationId ()
	{
		return getStringAttribute ("applicationId");
	}

	/**
	 * Set the application id.
	 *
	 * @param name The new apllication id.
	 */
	public void setApplicationId (String applicationId)
	{
		setAttribute ("applicationId", applicationId);
	}

	/**
	 * Get application name.
	 *
	 * @return The application name.
	 */
	public String getName ()
	{
		return getStringAttribute ("name");
	}

	/**
	 * Set the application name.
	 *
	 * @param name The new name.
	 */
	public void setName (String name)
	{
		setAttribute ("name", name);
	}

	/**
	 * Create a string representation of this application.
	 *
	 * @return The string representation.
	 */
	@Override
	public String toString ()
	{
		Application app = Application.get (getApplicationId ());

		if (app != null)
		{
			return "[" + app.getName () + "] " + getName ();
		}
		else
		{
			return "";
		}
	}
}
