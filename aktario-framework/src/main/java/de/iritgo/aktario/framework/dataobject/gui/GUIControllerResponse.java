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


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.iobject.IObjectProxy;
import de.iritgo.aktario.framework.base.DataObject;
import de.iritgo.aktario.framework.base.action.FrameworkAction;
import de.iritgo.aktario.framework.base.action.FrameworkInputStream;
import de.iritgo.aktario.framework.base.action.FrameworkOutputStream;
import java.io.IOException;


/**
 *
 */
public class GUIControllerResponse extends FrameworkAction
{
	private String controllerTypeId;

	private DataObject controller;

	/**
	 *
	 */
	public GUIControllerResponse ()
	{
		setTypeId ("GUIControllerResponse");
		controller = new Controller ();
	}

	/**
	 *
	 */
	public GUIControllerResponse (String controllerTypeId, DataObject controller)
	{
		this ();
		this.controllerTypeId = controllerTypeId;
		this.controller = controller;
	}

	/**
	 * Read the attributes from the a stream.
	 *
	 * @param stream The stream to read from.
	 */
	public void readObject (FrameworkInputStream stream) throws IOException
	{
		controllerTypeId = stream.readUTF ();

		try
		{
			controller.readObject (stream);
		}
		catch (ClassNotFoundException x)
		{
		}
	}

	/**
	 * Write the attributes to a stream.
	 *
	 * @param stream The stream to write to.
	 */
	public void writeObject (FrameworkOutputStream stream) throws IOException
	{
		stream.writeUTF (controllerTypeId);
		controller.writeObject (stream);
	}

	/**
	 * Perform the action.
	 */
	public void perform ()
	{
		Engine.instance ().getBaseRegistry ().add (controller);

		IObjectProxy proxy = new IObjectProxy (controller);

		Engine.instance ().getProxyRegistry ().addProxy (proxy, controller.getTypeId ());

		GUIManager guiManager = (GUIManager) Engine.instance ().getManagerRegistry ().getManager ("GUIManager");

		guiManager.addController (controllerTypeId, (Controller) controller);
		guiManager.controllerChanged (controller);
	}
}
