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

package de.iritgo.aktario.infocenter.manager;


import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.manager.Manager;
import de.iritgo.aktario.infocenter.infocenter.InfoCenterRegistry;


public class InfoCenterManager extends BaseObject implements Manager
{
	private InfoCenterRegistry infoCenterRegistry;

	public InfoCenterManager()
	{
		super("infocenter");
	}

	public void init()
	{
		infoCenterRegistry = new InfoCenterRegistry();
	}

	public void unload()
	{
		infoCenterRegistry.close();
	}

	public InfoCenterRegistry getInfoCenterRegistry()
	{
		return infoCenterRegistry;
	}
}
