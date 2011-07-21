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
public class PingAction extends FrameworkAction
{
	private long createTimeFromThis;

	private long pingTime;

	/**
	 * Standard constructor
	 */
	public PingAction ()
	{
		super (- 1);
	}

	/**
	 * Standard constructor
	 */
	public PingAction (long pingTime)
	{
		createTimeFromThis = System.currentTimeMillis ();
		this.pingTime = pingTime;
	}

	/**
	 * Read the attributes from the given stream.
	 */
	@Override
	public void readObject (FrameworkInputStream stream) throws IOException, ClassNotFoundException
	{
		pingTime = stream.readLong ();
	}

	/**
	 * Write the attributes to the given stream.
	 */
	@Override
	public void writeObject (FrameworkOutputStream stream) throws IOException
	{
		stream.writeLong (pingTime);
	}

	/**
	 * Perform the action.
	 */
	@Override
	public void perform ()
	{
		AppContext.instance ().getUser ().addPingTime (createTimeFromThis - pingTime);

		double channelNumber = AppContext.instance ().getChannelNumber ();
		ClientTransceiver clientTransceiver = new ClientTransceiver (channelNumber);

		clientTransceiver.addReceiver (channelNumber);

		PingServerAction pingServerAction = new PingServerAction (System.currentTimeMillis ());

		pingServerAction.setTransceiver (clientTransceiver);

		ActionTools.sendToServer (pingServerAction);
	}
}
