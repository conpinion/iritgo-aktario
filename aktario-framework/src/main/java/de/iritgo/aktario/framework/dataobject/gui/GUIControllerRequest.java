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
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.framework.base.action.FrameworkAction;
import de.iritgo.aktario.framework.base.action.FrameworkInputStream;
import de.iritgo.aktario.framework.base.action.FrameworkOutputStream;
import de.iritgo.aktario.framework.base.action.NetworkFrameworkServerAction;
import java.io.IOException;


/**
 *
 */
public class GUIControllerRequest extends NetworkFrameworkServerAction
{
	public static int DATAOBJECT = 0;

	public static int QUERY = 1;

	private String controllerTypeId;

	private int displayType;

	/**
	 * Create a new GUIControllerRequerst.
	 */
	public GUIControllerRequest()
	{
		setTypeId("GUIControllerRequest");
	}

	/**
	 * Create a new GUIControllerRequerst.
	 */
	public GUIControllerRequest(String controllerTypeId, int displayType)
	{
		this();
		this.controllerTypeId = controllerTypeId;
		this.displayType = displayType;
	}

	/**
	 * Read the attributes from the a stream.
	 *
	 * @param stream The stream to read from.
	 */
	public void readObject(FrameworkInputStream stream) throws IOException
	{
		controllerTypeId = stream.readUTF();
		displayType = stream.readInt();
	}

	/**
	 * Write the attributes to a stream.
	 *
	 * @param stream The stream to write to.
	 */
	public void writeObject(FrameworkOutputStream stream) throws IOException
	{
		stream.writeUTF(controllerTypeId);
		stream.writeInt(displayType);
	}

	/**
	 * Get the action to execute in the client.
	 *
	 * @param clientTransceiver The client transceiver.
	 * @return The client action.
	 */
	public FrameworkAction getAction(ClientTransceiver clientTransceiver)
	{
		clientTransceiver.addReceiver(clientTransceiver.getSender());

		GUIManager guiManager = (GUIManager) Engine.instance().getManagerRegistry().getManager("GUIManager");

		Controller controller = guiManager.getController(controllerTypeId);

		if (controller == null)
		{
			Engine.instance().getEventRegistry().fire("guimanager",
							new GUIControllerMissingEvent(controllerTypeId, guiManager, displayType, userUniqueId));

			controller = guiManager.getController(controllerTypeId);

			if (controller == null)
			{
				controller = guiManager.createDefaultController(controllerTypeId);
				guiManager.addController(controllerTypeId, controller);
			}

			Engine.instance().getBaseRegistry().add(controller);

			IObjectProxy proxy = new IObjectProxy(controller);

			Engine.instance().getProxyRegistry().addProxy(proxy, controller.getTypeId());
		}

		return new GUIControllerResponse(controllerTypeId, controller);
	}
}
