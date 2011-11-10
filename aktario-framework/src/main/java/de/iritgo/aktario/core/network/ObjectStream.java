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


import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.iobject.IObjectSerializer;
import de.iritgo.aktario.core.iobject.NoSuchIObjectException;
import de.iritgo.aktario.core.logger.Log;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


/**
 *
 */
public class ObjectStream extends StreamOrganizer
{
	/** The object protocol. */
	private IObjectSerializer objectSerializer;

	/** Data input stream wrapped around the socket stream. */
	private DataInputStream in;

	/** Data output stream wrapped around the socket stream. */
	private DataOutputStream out;

	/**
	 * Create a new ObjectStream.
	 *
	 * @param socket The communication socket.
	 */
	public ObjectStream(Socket socket) throws IOException
	{
		super(socket);

		objectSerializer = new IObjectSerializer();

		in = new DataInputStream(streamIn);
		out = new DataOutputStream(streamOut);
	}

	/**
	 * Send an object over the socket.
	 *
	 * @param object The object to send.
	 */
	@Override
	public void send(Object object) throws IOException
	{
		IObject sendObject = (IObject) object;

		synchronized (out)
		{
			objectSerializer.write(out, sendObject);
		}
	}

	/**
	 * Receive an object from the socket.
	 *
	 * @return the received object.
	 */
	@Override
	public Object receive() throws IOException, ClassNotFoundException, NoSuchIObjectException
	{
		IObject object;

		object = objectSerializer.read(in);
		Log.logDebug("system", "ObjectStream", "Object created and received: " + object);

		return object;
	}

	/**
	 * Close the connection.
	 */
	@Override
	public void close() throws IOException
	{
		if (! socket.isClosed())
		{
			socket.shutdownInput();
			socket.shutdownOutput();
			socket.close();
		}

		out.close();
		in.close();
	}

	/**
	 * Flush all data.
	 */
	@Override
	public void flush() throws IOException
	{
		out.flush();
	}

	/**
	 * Create a new StreamOrganizer.
	 *
	 * @param socket The communication socket.
	 * @return The new StreamOrganizer.
	 */
	@Override
	public StreamOrganizer create(Socket socket) throws IOException
	{
		return new ObjectStream(socket);
	}
}
