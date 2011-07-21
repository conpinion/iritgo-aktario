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


public class IObjectProxyEvent implements Event
{
	private long uniqueId;

	private boolean waitingForNewObject;

	private String iObjectTypeId;

	private IObject iObject;

	public IObjectProxyEvent (IObject iObject, boolean waitingForNewObject)
	{
		this.uniqueId = iObject.getUniqueId ();
		this.waitingForNewObject = waitingForNewObject;
		this.iObjectTypeId = iObject.getTypeId ();
		this.iObject = iObject;
	}

	public long getUniqueId ()
	{
		return uniqueId;
	}

	public String getIObjectTypeId ()
	{
		return iObjectTypeId;
	}

	public IObject getObject ()
	{
		return iObject;
	}

	public boolean isWaitingForNewObject ()
	{
		return waitingForNewObject;
	}

	@Override
	public String toString ()
	{
		return super.toString () + "[uniqueId=" + uniqueId + ",iObjectTypeId=" + iObjectTypeId + ",iObject=" + iObject
						+ ",waitingForNewObject=" + waitingForNewObject + "]";
	}
}
