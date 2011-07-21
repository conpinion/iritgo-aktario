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

package de.iritgo.aktario.core.network;


import de.iritgo.aktario.core.base.Transceiver;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 *
 */
public class ClientTransceiver implements Transceiver
{
	private double sender;

	private List receivers;

	private Channel connectedChannel;

	/**
	 * Standard-Constructor
	 */
	public ClientTransceiver (double sender)
	{
		receivers = new LinkedList ();
		this.sender = sender;
	}

	/**
	 * Standard-Constructor
	 */
	public ClientTransceiver (double sender, Channel connectedChannel)
	{
		this (sender);
		this.connectedChannel = connectedChannel;
	}

	/**
	 * Get the SenderChannelID.
	 *
	 * @return The SenderChannelID
	 */
	public double getSender ()
	{
		return sender;
	}

	/**
	 * Set the SenderChannelID.
	 *
	 * @param sender The SenderChannelID
	 */
	public void setSender (double sender)
	{
		this.sender = sender;
	}

	/**
	 * Return the current connectedChannel for this transceiver
	 *
	 * @return connectedChannel
	 **/
	public Channel getConnectedChannel ()
	{
		return connectedChannel;
	}

	/**
	 * Get a iterator of all receivers channel id's.
	 *
	 * @return Iterator
	 */
	public Iterator getReceiverIterator ()
	{
		return receivers.iterator ();
	}

	/**
	 * Add a receiverchannelid to the list.
	 */
	public void addReceiver (double receiver)
	{
		receivers.add (new Double (receiver));
	}

	/**
	 * Remove all Receivers
	 */
	public void removeAllReceivers ()
	{
		receivers.clear ();
	}

	/**
	 * Get the number of recievers.
	 *
	 * @return The number of recievers.
	 */
	public int getReceiverCount ()
	{
		return receivers.size ();
	}
}
