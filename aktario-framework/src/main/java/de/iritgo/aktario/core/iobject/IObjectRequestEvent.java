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

package de.iritgo.aktario.core.iobject;


import de.iritgo.aktario.core.event.Event;


/**
 *
 */
public class IObjectRequestEvent implements Event
{
	/** The unique id of the requested object. */
	private long uniqueId;

	/**
	 * Create a new  iobject request event.
	 *
	 * @param uniqueId Unique id of the requested object.
	 */
	public IObjectRequestEvent(long uniqueId)
	{
		this.uniqueId = uniqueId;
	}

	/**
	 * Get the unique id of the requested object.
	 *
	 * @return The unique id of the requested object.
	 */
	public long getUniqueId()
	{
		return uniqueId;
	}
}
