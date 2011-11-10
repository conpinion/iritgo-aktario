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


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.framework.IritgoEngine;
import de.iritgo.aktario.framework.base.action.FrameworkServerAction;
import de.iritgo.aktario.framework.client.Client;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


/**
 * QueryResponse
 *
 * @version $Id: AktarioUserDeletedResponse.java,v 1.8 2006/09/25 10:34:30 grappendorf Exp $
 */
public class AktarioUserDeletedResponse extends FrameworkServerAction
{
	/**
	 * Standard constructor
	 */
	public AktarioUserDeletedResponse()
	{
	}

	/**
	 * Perform the action.
	 */
	@Override
	public void perform()
	{
		JOptionPane.showMessageDialog((JFrame) Client.instance().getClientGUI().getDesktopManager().getDesktopFrame(),
						Engine.instance().getResourceService().getString("aktario.userDeleted"), Engine.instance()
										.getResourceService().getString("app.title"), JOptionPane.OK_OPTION);

		IritgoEngine.instance().shutdown();
	}
}
