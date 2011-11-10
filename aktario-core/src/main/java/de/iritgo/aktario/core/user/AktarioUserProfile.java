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

package de.iritgo.aktario.core.user;


import de.iritgo.aktario.core.iobject.IObjectList;
import de.iritgo.aktario.framework.base.DataObject;
import de.iritgo.aktario.framework.base.FrameworkProxy;


/**
 * AktarioUserProfile
 *
 * @version $Id: AktarioUserProfile.java,v 1.8 2006/09/25 10:34:30 grappendorf Exp $
 */
public class AktarioUserProfile extends DataObject
{
	public AktarioUserProfile()
	{
		super("AktarioUserProfile");

		addAttribute("preferences", new IObjectList("preferences", new FrameworkProxy(new AktarioUserPreferences()),
						this));
	}

	public AktarioUserProfile(long uniqueId)
	{
		this();
		setUniqueId(uniqueId);
	}

	public void addPreferences(AktarioUserPreferences preferences)
	{
		getIObjectListAttribute("preferences").add(preferences);
	}

	public AktarioUserPreferences getPreferences()
	{
		return (AktarioUserPreferences) getIObjectListAttribute("preferences").get(0);
	}
}
