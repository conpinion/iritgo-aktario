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
import java.net.*;

import org.apache.mina.core.future.*;
import org.apache.mina.core.service.*;
import org.apache.mina.core.session.*;
import org.apache.mina.filter.codec.*;
import org.apache.mina.filter.codec.textline.*;
import org.apache.mina.transport.socket.nio.*;


/**
 * A ChannelFactory is a threadable object that waits for network connections
 * on a tcp port. If a connection is established, it creates a new connected channel
 * and adds it to the NetworkService.
 */
public class ChannelFactory implements ProtocolCodecFactory
{
	/** The hostname of the system on which the server runs. */
	@SuppressWarnings("unused")
	private String hostName = "localhost";

	/** The port number to listen on. */
	@SuppressWarnings("unused")
	private int port;

    private ProtocolEncoder encoder;
    private ProtocolDecoder decoder;

    /**
	 * Create a new ChannelFactory
	 *
	 * @param networkService The network service to use.
	 * @param port The port on which to listen.
	 * @param timeout The accept timeout.
	 */
	public ChannelFactory(NetworkService networkService, int port, int timeout) throws IOException
	{
		this(networkService, "localhost", port, timeout);
	}

	/**
	 * Create a new ChannelFactory
	 *
	 * @param networkService The network service to use.
	 * @param hostName The name of the server host.
	 * @param port The port on which to listen.
	 * @param timeout The accept timeout.
	 */
	public ChannelFactory(NetworkService networkService, String hostName, int port, int timeout) throws IOException
	{
		this.port = port;
		this.hostName = hostName;

		IoAcceptor acceptor = new NioSocketAcceptor(20);

        acceptor.getFilterChain().addLast( "protocol",
        		new ProtocolCodecFilter(
        				this));

        acceptor.setHandler (new ChannelHandler (networkService));
		acceptor.getSessionConfig ().setReadBufferSize (1024*512);
        acceptor.getSessionConfig ().setIdleTime (IdleStatus.BOTH_IDLE, 120);

        try
        {
			decoder = new ObjectStreamDecoder ();
			encoder = new ObjectStreamEncoder ();
		}
        catch (Exception x)
        {
			x.printStackTrace();
		}

        acceptor.bind( new InetSocketAddress (port));
	}

	/**
	 * Create a new ChannelFactory
	 *
	 * @param networkService The network service to use.
	 * @param hostName The name of the server host.
	 * @param port The port on which to listen.
	 * @param timeout The accept timeout.
	 */
	public ChannelFactory(NetworkService networkService, Channel channel, String hostName, int port) throws IOException
	{
		this.port = port;
		this.hostName = hostName;

		IoConnector connector = new NioSocketConnector();

        connector.getFilterChain().addLast( "protocol",
        		new ProtocolCodecFilter(
        				this));

        connector.setHandler (new ChannelHandler (networkService, channel));
		connector.getSessionConfig ().setReadBufferSize (2048*1024);
        connector.getSessionConfig ().setIdleTime (IdleStatus.BOTH_IDLE, 120);

        try
        {
			decoder = new ObjectStreamDecoder ();
			encoder = new ObjectStreamEncoder ();
		}
        catch (Exception x)
        {
			x.printStackTrace();
		}

        ConnectFuture future = connector.connect(new InetSocketAddress (hostName, port));
        future.join ();
        channel.setSession (future.getSession());

	}

	public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception
	{
		return encoder;
	}

	public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception
	{
		return decoder;
	}
}
