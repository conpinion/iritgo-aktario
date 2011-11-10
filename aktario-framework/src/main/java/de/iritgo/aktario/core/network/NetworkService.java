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
import de.iritgo.aktario.core.action.NetworkActionProcessorInterface;
import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.iobject.NoSuchIObjectException;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.thread.ThreadService;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 *
 */
public class NetworkService extends BaseObject
{
	private ThreadService threadService;

	private Socket socket;

	private List networkSystemListenerList;

	private Channel channel;

	private HashMap channelList;

	private double numChannels;

	private StreamOrganizer defaultStreamOrganizer;

	@SuppressWarnings("unused")
	private List objects;

	private NetworkActionProcessorInterface reveiveNetworkActionProcessor;

	private NetworkActionProcessorInterface sendNetworkActionProcessor;

	private ChannelFactory channelFactory;

	/**
	 * Networkbase Standard constructor
	 */
	public NetworkService(ThreadService threadBase, NetworkActionProcessorInterface reveiveNetworkActionProcessor,
					NetworkActionProcessorInterface sendNetworkActionProcessor)
	{
		this.threadService = threadBase;
		networkSystemListenerList = new LinkedList();
		channelList = new HashMap();
		numChannels = 0;
		this.reveiveNetworkActionProcessor = reveiveNetworkActionProcessor;
		this.sendNetworkActionProcessor = sendNetworkActionProcessor;
	}

	/**
	 * Is a connection accepted (reseiver or sender) a Channel will created for
	 * sending and receiving objects.
	 * This method will called from ChannelFactory (receiver) or from Networkbase (sender).
	 *
	 * @param channel Channel-Class, managed the transmission.
	 */
	public void addConnectedChannel(Channel channel)
	{
		channel.setChannelNumber(numChannels);

		synchronized (channelList)
		{
			channelList.put(String.valueOf(numChannels), channel);
			reveiveNetworkActionProcessor.newChannelCreated(channel);
		}

		threadService.add(channel);
		numChannels++;
		fireConnectionEstablished(channel);
	}

	/**
	 * Is a object received, all processors will called
	 *
	 * @param action Most often it is a action object.
	 * @param channel The channel that received the object.
	 */
	synchronized public void callReceiveNetworkActionProcessor(Action action, final Channel channel)
	{
		reveiveNetworkActionProcessor.perform(action, new ClientTransceiver(channel.getChannelNumber(), channel));
	}

	/**
	 * Is a connection accepted (reseiver or sender) a Channel will created for
	 * sending and receiving objects.
	 * This method will called from ChannelFactory (receiver) or from Networkbase (sender).
	 */
	public void addNetworkSystemListener(NetworkSystemListener listener)
	{
		synchronized (networkSystemListenerList)
		{
			networkSystemListenerList.add(listener);
		}
	}

	/**
	 * Called when a network connection was established.
	 *
	 * @param channel The network channel.
	 */
	public void fireConnectionEstablished(Channel channel)
	{
		for (Iterator i = networkSystemListenerList.iterator(); i.hasNext();)
		{
			((NetworkSystemListener) i.next()).connectionEstablished(this, channel);
		}
	}

	/**
	 * Called when a network connection was terminated.
	 *
	 * @param channel The network channel.
	 */
	public void fireConnectionTerminated(Channel channel)
	{
		for (Iterator i = networkSystemListenerList.iterator(); i.hasNext();)
		{
			((NetworkSystemListener) i.next()).connectionTerminated(this, channel);
		}

		reveiveNetworkActionProcessor.channelClosed(channel);
		sendNetworkActionProcessor.channelClosed(channel);
	}

	/**
	 * Called when a NoSuchIObjectException has occurred.
	 *
	 * @param channel The network channel.
	 */
	public void fireError(Channel channel, NoSuchIObjectException x)
	{
		for (Iterator i = networkSystemListenerList.iterator(); i.hasNext();)
		{
			((NetworkSystemListener) i.next()).error(this, channel, x);
		}
	}

	/**
	 * Called when a SocketTimeoutException has occurred.
	 *
	 * @param channel The network channel.
	 */
	public void fireError(Channel channel, SocketTimeoutException x)
	{
		for (Iterator i = networkSystemListenerList.iterator(); i.hasNext();)
		{
			((NetworkSystemListener) i.next()).error(this, channel, x);
		}
	}

	/**
	 * Called when a ClassNotFoundException has occurred.
	 *
	 * @param channel The network channel.
	 */
	public void fireError(Channel channel, ClassNotFoundException x)
	{
		for (Iterator i = networkSystemListenerList.iterator(); i.hasNext();)
		{
			((NetworkSystemListener) i.next()).error(this, channel, x);
		}
	}

	/**
	 * Called when an EOFException has occurred.
	 *
	 * @param channel The network channel.
	 */
	public void fireError(Channel channel, EOFException x)
	{
		for (Iterator i = networkSystemListenerList.iterator(); i.hasNext();)
		{
			((NetworkSystemListener) i.next()).error(this, channel, x);
		}
	}

	/**
	 * Called when a SocketException has occurred.
	 *
	 * @param channel The network channel.
	 */
	public void fireError(Channel channel, SocketException x)
	{
		for (Iterator i = networkSystemListenerList.iterator(); i.hasNext();)
		{
			((NetworkSystemListener) i.next()).error(this, channel, x);
		}
	}

	/**
	 * Called when an IOException has occurred.
	 *
	 * @param channel The network channel.
	 */
	public void fireError(Channel channel, IOException x)
	{
		for (Iterator i = networkSystemListenerList.iterator(); i.hasNext();)
		{
			((NetworkSystemListener) i.next()).error(this, channel, x);
		}
	}

	/**
	 * Connect with a Server.
	 */
	public double connect(String name, int port, ConnectObserver connectObserver)
	{
		Log.logDebug("network", "NetworkService.connect", "Connecting to server on port " + port);

		Channel channel = null;

		try
		{
			socket = null;

			int i = 0;

			while ((socket == null) && (i < 10))
			{
				for (int j = 0; j < 10; ++j)
				{
					try
					{
						Thread.sleep(100);

						if (connectObserver != null)
						{
							connectObserver.notice();
						}
					}
					catch (InterruptedException e)
					{
					}
				}

				socket = new Socket(InetAddress.getByName(name), port);
				++i;
			}

			if (socket == null)
			{
				return - 1;
			}

			channel = new Channel(socket, this);
			addConnectedChannel(channel);
		}

		catch (Exception e)
		{
			Log.logError("network", "NetworkService.connect", "Error while connecting to " + name + ":" + port);

			return - 1;
		}

		return channel.getChannelNumber();
	}

	/**
	 * Listen on a given host and Port.
	 *
	 * @param name The host name or ip.
	 * @param port The port number.
	 * @param timeout Socket timeout.
	 */
	public void listen(String name, int port, int timeout)
	{
		Log.logInfo("network", "NetworkService.listen", "Listening on port:" + port);

		try
		{
			channelFactory = new ChannelFactory(this, name, port, timeout);
			threadService.add(channelFactory);
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * Send a object about a channel.
	 */
	public void send(Object object, double channel)
	{
		Channel connectedChannel = null;

		synchronized (channelList)
		{
			connectedChannel = (Channel) channelList.get(String.valueOf(channel));

			if (connectedChannel == null)
			{
				Log.logFatal("network", "NetworkService.send", "Channel not found: " + channel);

				return;
			}
		}

		connectedChannel.send(object);
	}

	/**
	 * Send a object about a channel.
	 */
	public void send(Object object, Channel channel)
	{
		channel.send(object);
	}

	/**
	 * Send a object to all channels.
	 */
	public void sendBroadcast(Object object)
	{
		synchronized (channelList)
		{
			Iterator i = channelList.keySet().iterator();

			while (i.hasNext())
			{
				((Channel) channelList.get(i.next())).send(object);
			}
		}
	}

	/**
	 * Close a Channel
	 *
	 * @param channelNumber
	 */
	public void closeChannel(double channelNumber)
	{
		try
		{
			synchronized (channelList)
			{
				Channel connectedChannel = (Channel) channelList.get(String.valueOf(channelNumber));

				if (connectedChannel != null)
				{
					connectedChannel.dispose();
					channelList.remove(String.valueOf(channelNumber));
					Log.logInfo("network", "NetworkService.closeChannel", "Closing channel " + channelNumber);
				}
			}
		}
		catch (Exception x)
		{
			Log.logError("network", "NetworkService.closeChannel", "Error while closing channel " + channelNumber
							+ ": " + x.toString());
		}
	}

	/**
	 * Close all Channel
	 */
	public void closeAllChannels()
	{
		try
		{
			synchronized (channelList)
			{
				for (Iterator i = channelList.values().iterator(); i.hasNext();)
				{
					Channel connectedChannel = (Channel) i.next();

					connectedChannel.dispose();
				}

				channelList.clear();
			}
		}
		catch (Exception x)
		{
			Log.logError("network", "NetworkService.closeChannel", "Error while closing channel :" + x.toString());
		}
	}

	/**
	 * Get the number of channels.
	 *
	 * @return Numbers of channels.
	 */
	public double getNumChannels()
	{
		return numChannels;
	}

	/**
	 * Return the Channel of the ChannelNumber.
	 */
	public Channel getConnectedChannel(double channelNumber)
	{
		Channel connectedChannel = null;

		synchronized (channelList)
		{
			connectedChannel = (Channel) channelList.get(String.valueOf(channelNumber));
		}

		return connectedChannel;
	}

	/**
	 * Flush the Buffer, send all...
	 */
	public void flush(double channel)
	{
		Channel connectedChannel = null;

		synchronized (channelList)
		{
			connectedChannel = (Channel) channelList.get(String.valueOf(channel));

			if (connectedChannel == null)
			{
				Log.logFatal("network", "NetworkService.flush", "Channel not found: " + channel);

				return;
			}
		}

		connectedChannel.flush();
	}

	/**
	 * Flush the Buffer, send all...
	 */
	public void flushAll()
	{
		Channel connectedChannel = null;

		synchronized (channelList)
		{
			for (Iterator i = channelList.values().iterator(); i.hasNext();)
			{
				connectedChannel = (Channel) i.next();

				if (connectedChannel == null)
				{
					Log.logFatal("network", "NetworkService.flushAll", "Channel not found: " + channel);

					continue;
				}

				connectedChannel.flush();
			}
		}
	}

	/**
	 * The streamorganizer do the low level protocol for the communication
	 *
	 * @param s The socket with work the stream organizer.
	 */
	public StreamOrganizer getDefaultStreamOrganizer(Socket s) throws IOException
	{
		if (defaultStreamOrganizer != null)
		{
			return defaultStreamOrganizer.create(s);
		}

		return new ObjectStream(s);
	}

	/**
	 * Set the default stream organizer.
	 *
	 * @param streamOrganizer StreamOrganizer The stream organizer
	 */
	public void setDefaultStreamOrganizer(StreamOrganizer streamOrganizer)
	{
		defaultStreamOrganizer = streamOrganizer;
	}

	/**
	 * Set the a new stream organizer to a channel.
	 *
	 * @param streamOrganizer StreamOrganizer The stream organizer
	 * @param channel ConnectChannelNumber The channel number.
	 */
	public void setStreamOrganizer(StreamOrganizer streamOrganizer, double channel)
	{
		Channel connectedChannel = null;

		try
		{
			synchronized (channelList)
			{
				connectedChannel = (Channel) channelList.get(String.valueOf(channel));

				if (connectedChannel == null)
				{
					Log.logFatal("network", "NetworkService.setStreamOrganizer", "Channel not found: " + channel);

					return;
				}

				connectedChannel.setStreamOrganizer(streamOrganizer);
			}
		}
		catch (IOException x)
		{
			Log.logFatal("network", "NetworkService.setStreamOrganizer", "Unable to set stream organizer for channel "
							+ channel);
		}
	}

	/**
	 * Dispose this network service.
	 */
	public void dispose()
	{
		channelFactory.dispose();
	}
}
