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


/*
 This file is part of the Iritgo/RTC Framework.

 Copyright (C) 2003-2006 BueroByte GbR.
 */
import de.iritgo.aktario.core.event.Event;
import de.iritgo.aktario.framework.base.DataObject;


public class DataObjectGUIValidatorEvent implements Event
{
	private DataObject object;

	private Controller controller;

	private boolean error;

	public DataObjectGUIValidatorEvent (DataObject object, Controller controller)
	{
		this.object = object;
		this.controller = controller;
		this.error = false;
	}

	public Controller getController ()
	{
		return controller;
	}

	public void setController (Controller controller)
	{
		this.controller = controller;
	}

	public DataObject getObject ()
	{
		return object;
	}

	public void setObject (DataObject object)
	{
		this.object = object;
	}

	public boolean isError ()
	{
		return error;
	}

	public void setError (boolean error)
	{
		this.error = error;
	}
}
