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


import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.thread.Threadable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * A ChannelFactory is a threadable object that waits for network connections
 * on a tcp port. If a connection is established, it creates a new connected channel
 * and adds it to the NetworkService.
 */
public class ChannelFactory extends Threadable
{
	/** The hostname of the system on which the server runs. */
	@SuppressWarnings("unused")
	private String hostName = "localhost";

	/** The port number to listen on. */
	@SuppressWarnings("unused")
	private int port;

	/** The server socket. */
	private ServerSocket serverSocket;

	/** The network service. */
	private NetworkService networkService;

	/**
	 * Create a new ChannelFactory
	 *
	 * @param networkService The network service to use.
	 * @param port The port on which to listen.
	 * @param timeout The accept timeout.
	 */
	public ChannelFactory (NetworkService networkService, int port, int timeout) throws IOException
	{
		this (networkService, "localhost", port, timeout);
	}

	/**
	 * Create a new ChannelFactory
	 *
	 * @param networkService The network service to use.
	 * @param hostName The name of the server host.
	 * @param port The port on which to listen.
	 * @param timeout The accept timeout.
	 */
	public ChannelFactory (NetworkService networkService, String hostName, int port, int timeout) throws IOException
	{
		super ("ServerSocket [" + (hostName == null ? "localhost" : hostName) + ":" + port + "]");

		this.networkService = networkService;
		this.port = port;
		this.hostName = hostName;

		serverSocket = new ServerSocket (port);

		serverSocket.setSoTimeout (timeout);
	}

	/**
	 * Execute the Threadable.
	 *
	 * This method waits for connections on the server socket and creates new
	 * ConnectedChannels if a connection was established.
	 */
	@Override
	public void run ()
	{
		try
		{
			Log.logVerbose ("network", "ChannelFactory.run", "Waiting for Connections");

			Socket s = serverSocket.accept ();

			networkService.addConnectedChannel (new Channel (s, networkService));
			Log.logDebug ("network", "ChannelFactory.run", "Connection accepted");
		}
		catch (IOException e)
		{
		}

		setState (Threadable.FREE);
	}

	/**
	 * Dispose this channel factory.
	 */
	@Override
	public void dispose ()
	{
		try
		{
			serverSocket.close ();
		}
		catch (IOException x)
		{
		}
	}
}
