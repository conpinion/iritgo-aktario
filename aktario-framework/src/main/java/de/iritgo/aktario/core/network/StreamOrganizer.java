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


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.config.Configuration;
import de.iritgo.aktario.core.config.SocketConfig;
import de.iritgo.aktario.core.io.IBufferedInputStream;
import de.iritgo.aktario.core.io.IBufferedOutputStream;
import de.iritgo.aktario.core.iobject.NoSuchIObjectException;
import java.io.IOException;
import java.net.Socket;


/**
 *
 */
public abstract class StreamOrganizer
{
	/** Communication socket. */
	protected Socket socket;

	/** Input stream. */
	protected IBufferedInputStream streamIn;

	/** Output stream. */
	protected IBufferedOutputStream streamOut;

	/**
	 * Create a new StreamOrganizer.
	 *
	 * @param socket The communication socket.
	 */
	public StreamOrganizer(Socket socket) throws IOException
	{
		Configuration config = Engine.instance().getConfiguration();
		SocketConfig socketConfig = config.getNetwork().getSocket();

		int bufSize = socketConfig.getPort();
		int timeout = socketConfig.getReadTimeout();

		this.socket = socket;
		this.socket.setSoTimeout(timeout);
		this.socket.setKeepAlive(true);

		if (checkOrder(socket))
		{
			streamIn = new IBufferedInputStream(socket.getInputStream(), bufSize);
			streamOut = new IBufferedOutputStream(socket.getOutputStream(), bufSize);
		}
		else
		{
			streamOut = new IBufferedOutputStream(socket.getOutputStream(), bufSize);
			streamIn = new IBufferedInputStream(socket.getInputStream(), bufSize);
		}
	}

	/**
	 * @param socket The Com. socket.
	 */
	protected boolean checkOrder(Socket socket) throws IOException
	{
		int port1 = socket.getLocalPort();
		int port2 = socket.getPort();

		if (port1 < port2)
		{
			return true;
		}
		else if (port1 > port2)
		{
			return false;
		}

		int address1 = socket.getLocalAddress().hashCode();
		int address2 = socket.getInetAddress().hashCode();

		if (address1 < address2)
		{
			return true;
		}
		else if (address1 > address2)
		{
			return false;
		}

		throw new IOException();
	}

	/**
	 * Send an object over the socket.
	 *
	 * @param object The object to send.
	 */
	public abstract void send(Object object) throws IOException;

	/**
	 * Receive an object from the socket.
	 *
	 * @return the received object.
	 */
	public abstract Object receive() throws IOException, ClassNotFoundException, NoSuchIObjectException;

	/**
	 * Close the connection.
	 */
	public abstract void close() throws IOException;

	/**
	 * Flush all data.
	 */
	public abstract void flush() throws IOException;

	/**
	 * Create a new StreamOrganizer.
	 *
	 * @param socket The communication socket.
	 * @return The new StreamOrganizer.
	 */
	public abstract StreamOrganizer create(Socket socket) throws IOException;
}
