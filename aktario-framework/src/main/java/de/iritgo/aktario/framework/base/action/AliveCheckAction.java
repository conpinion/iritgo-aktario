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

package de.iritgo.aktario.framework.base.action;


import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.appcontext.AppContext;
import java.io.IOException;


/**
 *
 */
public class AliveCheckAction extends FrameworkAction
{
	public static final int SERVER = 1;

	public static final int CLIENT = 2;

	private int source;

	/**
	 * Standard constructor
	 */
	public AliveCheckAction()
	{
	}

	/**
	 * Standard constructor
	 *
	 * @param source The source form this action.
	 */
	public AliveCheckAction(int source)
	{
		this.source = source;
	}

	/**
	 * Read the attributes from the given stream.
	 */
	@Override
	public void readObject(FrameworkInputStream stream) throws IOException, ClassNotFoundException
	{
		source = stream.readInt();
	}

	/**
	 * Write the attributes to the given stream.
	 */
	@Override
	public void writeObject(FrameworkOutputStream stream) throws IOException
	{
		stream.writeInt(source);
	}

	/**
	 * Perform the action.
	 */
	@Override
	public void perform()
	{
		if (source == CLIENT)
		{
			((ClientTransceiver) getTransceiver()).getConnectedChannel().setAliveCheckSent(false);

			return;
		}

		double channelNumber = AppContext.instance().getChannelNumber();
		ClientTransceiver clientTransceiver = new ClientTransceiver(channelNumber);

		clientTransceiver.addReceiver(channelNumber);

		AliveCheckServerAction aliveCheckServerAction = new AliveCheckServerAction(source);

		aliveCheckServerAction.setTransceiver(clientTransceiver);

		ActionTools.sendToServer(aliveCheckServerAction);
	}
}
