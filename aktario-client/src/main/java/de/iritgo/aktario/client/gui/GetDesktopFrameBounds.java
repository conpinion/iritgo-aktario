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

package de.iritgo.aktario.client.gui;


import de.iritgo.aktario.core.command.Command;
import de.iritgo.aktario.framework.client.Client;


/**
 * GetDesktopFrameBounds
 *
 * @version $Id: GetDesktopFrameBounds.java,v 1.7 2006/09/25 10:34:31 grappendorf Exp $
 */
public class GetDesktopFrameBounds extends Command
{
	public GetDesktopFrameBounds()
	{
		super("AktarioGetDesktopFrameBounds");
	}

	@Override
	public Object performWithResult()
	{
		return ((AktarioGUI) Client.instance().getClientGUI()).getDesktopPane().getBounds();
	}
}
