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


import de.iritgo.aktario.core.action.Action;
import de.iritgo.aktario.core.iobject.NoSuchIObjectException;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.thread.Threadable;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;


/**
 *
 */
public class Channel extends Threadable
{
	static public int NETWORK_OK = 0;

	static public int NETWORK_ERROR = 1;

	static public int NETWORK_CLOSE = 2;

	static public int NETWORK_ERROR_CLOSING = 3;

	private Socket socket = null;

	private NetworkService networkService = null;

	private StreamOrganizer streamOrganizer;

	private double channelNumber;

	private int connectionState;

	private int numReceivedObjects;

	private int numSendObjects;

	/**
	 *  Save your custom context object for a relation.
	 */
	private Object customerContextObject;

	/**
	 * In case of a connection timeout we send an action to the other end point.
	 * After sending the action we set this flag to true. After another timeout,
	 * we can identify this situation by checking this flag.
	 */
	private boolean aliveCheckSent;

	/**
	 * Standard constructor
	 *
	 * @param socket The Socket for input/output action
	 * @param networkService Networkbase for storge received objects
	 */
	public Channel(Socket socket, NetworkService networkService) throws IOException
	{
		super("Channel:" + (networkService.getNumChannels()));
		init(socket, networkService, networkService.getNumChannels());
	}

	/**
	 * Standard constructor
	 *
	 * @param socket The Socket for input/output action
	 * @param networkService Networkbase for storge received objects
	 * @param channelNumber The ChannelNumber
	 */
	public Channel(Socket socket, NetworkService networkService, double channelNumber) throws IOException
	{
		super("channel" + channelNumber);
		init(socket, networkService, channelNumber);
	}

	/**
	 * Init the Channel
	 *
	 * @param The Socket for input/output action
	 * @param Networkbase for storge received objects
	 * @param The ChannelNumber
	 */
	private void init(Socket socket, NetworkService networkService, double channelNumber) throws IOException
	{
		this.socket = socket;
		this.networkService = networkService;
		this.channelNumber = channelNumber;

		numReceivedObjects = 0;
		numSendObjects = 0;
		connectionState = NETWORK_OK;
		streamOrganizer = networkService.getDefaultStreamOrganizer(socket);
	}

	/**
	 * Set the customer ContextObject
	 *
	 * @param customerContextObject The custom object
	 */
	public void setCustomerContextObject(Object customerContextObject)
	{
		this.customerContextObject = customerContextObject;
	}

	/**
	 * Set the customer ContextObject
	 *
	 * @return object The custom object
	 */
	public Object getCustomerContextObject()
	{
		return customerContextObject;
	}

	/**
	 * The id to find the Channel, must save from the framework or you use the customercontextobject.
	 *
	 * @param channelNumber The ChannelNumber
	 */
	public void setChannelNumber(double channelNumber)
	{
		this.channelNumber = channelNumber;
	}

	/**
	 * The id to find the Channel, must save from the framework.
	 *
	 * @return The ChannelNumber
	 */
	public double getChannelNumber()
	{
		return channelNumber;
	}

	/**
	 * Wait for a Object.
	 *
	 */
	@Override
	public void run()
	{
		Object object = null;

		setState(Threadable.RUNNING);

		while (getState() == Threadable.RUNNING)
		{
			try
			{
				if (connectionState != NETWORK_OK)
				{
					Log.logWarn("network", "Channel", "A network error occurred. Closing connection");
					setState(Threadable.CLOSING);

					return;
				}

				object = streamOrganizer.receive();

				if (object == null)
				{
					Log.logError("network", "Channel", "Receiving NULL-Object" + channelNumber + ").");
					setConnectionState(NETWORK_ERROR);
					networkService.fireError(this, new NoSuchIObjectException("null"));
					networkService.fireConnectionTerminated(this);
					return;
				}

				Log.logDebug("network", "Channel", "Receiving Object (Channel:" + channelNumber + ").");

				++numReceivedObjects;
				networkService.callReceiveNetworkActionProcessor((Action) object, this);
			}
			catch (SocketTimeoutException x)
			{
				Log.logDebug("network", "Channel", "SocketTimeoutException");
				networkService.fireError(this, x);
			}
			catch (NoSuchIObjectException x)
			{
				Log.logError("network", "Channel", "NoSuchPrototypeRegisteredException: " + x);
				object = null;
				setConnectionState(NETWORK_ERROR);
				networkService.fireError(this, x);
				networkService.fireConnectionTerminated(this);

				return;
			}
			catch (ClassNotFoundException x)
			{
				Log.logError("network", "Channel", "ClassNotFoundException");
				setConnectionState(NETWORK_ERROR);
				networkService.fireError(this, x);
				networkService.fireConnectionTerminated(this);

				return;
			}
			catch (EOFException x)
			{
				Log.logDebug("network", "Channel", "EOFException (ConnectionClosed?!)");
				setConnectionState(NETWORK_CLOSE);
				setState(Threadable.CLOSING);
				networkService.fireError(this, x);
				networkService.fireConnectionTerminated(this);

				return;
			}
			catch (SocketException x)
			{
				Log.logDebug("network", "Channel", "SocketClosed.");
				setConnectionState(NETWORK_CLOSE);
				setState(Threadable.CLOSING);
				networkService.fireError(this, x);
				networkService.fireConnectionTerminated(this);

				return;
			}
			catch (IOException x)
			{
				setState(Threadable.CLOSING);
				Log.logError("network", "Channel", "IOException: " + x);
				x.printStackTrace();

				setConnectionState(NETWORK_ERROR);
				networkService.fireError(this, x);
				networkService.fireConnectionTerminated(this);

				return;
			}
		}

		return;
	}

	/**
	 * Send a Object over the ObjectStream.
	 *
	 * @param object The Object to send.
	 */
	public void send(Object object)
	{
		try
		{
			Log.logDebug("network", "Channel", "Sending Object (Channel:" + channelNumber + "):" + object);

			++numSendObjects;
			streamOrganizer.send(object);
		}
		catch (IOException e)
		{
			Log.logError("network", "Channel", "Serializeable?" + e);
		}
	}

	/**
	 * Get information about the channelstate (OK, DISCONNECT and so on).
	 *
	 * @return the Connection State.
	 */
	public int getConnectionState()
	{
		return connectionState;
	}

	/**
	 * Set ConnectionState
	 *
	 * @param connectionState The connection state
	 */
	public void setConnectionState(int connectionState)
	{
		this.connectionState = connectionState;
	}

	/**
	 * Close this Channel
	 */
	@Override
	public void dispose()
	{
		setState(Threadable.CLOSING);
		connectionState = NETWORK_CLOSE;

		try
		{
			streamOrganizer.flush();
			streamOrganizer.close();
		}
		catch (IOException x)
		{
			Log.logError("network", "Channel", x.toString());
			setConnectionState(NETWORK_ERROR_CLOSING);
		}
	}

	/**
	 * Get the Networkbase from this Channel
	 *
	 * @return The NetworkService
	 */
	public NetworkService getNetworkBase()
	{
		return networkService;
	}

	/**
	 * Get the number of received objects.
	 *
	 * @return Number of received objects.
	 */
	public int getNumReceivedObjects()
	{
		return numReceivedObjects;
	}

	/**
	 * Get the number of send objects.
	 *
	 * @return Number of send objects.
	 */
	public int getNumSendObjects()
	{
		return numSendObjects;
	}

	/**
	 * Get the number of all received/send objects
	 *
	 * @return Number of send objects.
	 */
	public int getNumAllObjects()
	{
		return numReceivedObjects + numSendObjects;
	}

	/**
	 * Flush the streams.
	 */
	public void flush()
	{
		try
		{
			streamOrganizer.flush();
		}
		catch (IOException x)
		{
			Log.logError("network", "Channel", x.toString());
		}
	}

	/**
	 * Set a new stream organizer for this connected channel
	 *
	 * @param streamOrganizer The stream organizer.
	 */
	public void setStreamOrganizer(StreamOrganizer streamOrganizer) throws IOException
	{
		if (streamOrganizer != null)
		{
			streamOrganizer.flush();
		}

		this.streamOrganizer = streamOrganizer.create(socket);
	}

	/**
	 * Get the current alive check flag.
	 *
	 * @return The current flag.
	 */
	public boolean isAliveCheckSent()
	{
		return aliveCheckSent;
	}

	/**
	 * Set the alive check flag.
	 *
	 * @param aliveCheckSent The new flag.
	 */
	public void setAliveCheckSent(boolean aliveCheckSent)
	{
		this.aliveCheckSent = aliveCheckSent;
	}
}
