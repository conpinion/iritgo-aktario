package de.iritgo.aktario.core.network;

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

import de.iritgo.aktario.core.action.Action;
import de.iritgo.aktario.core.iobject.*;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.thread.Threadable;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.*;

import org.apache.mina.core.service.*;
import org.apache.mina.core.session.*;


/**
 *
 */
public class ChannelHandler extends IoHandlerAdapter
{
	public ConcurrentHashMap<IoSession, Channel> channels = new ConcurrentHashMap<IoSession, Channel> ();
	private NetworkService networkService;
	private Channel clientChannel;


	/**
	 * Standard constructor
	 *
	 * @param socket The Socket for input/output action
	 * @param networkService Networkbase for storge received objects
	 */
	public ChannelHandler (NetworkService networkService)
	{
		this.networkService = networkService;
	}

	public ChannelHandler (NetworkService networkService, Channel clientChannel)
	{
		this.networkService = networkService;
		this.clientChannel = clientChannel;
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception
	{
		Channel channel = clientChannel == null ? new Channel (networkService, session) : clientChannel;

		channels.put (session,  channel);
		networkService.addConnectedChannel(channel);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception
	{
		networkService.closeChannel (channels.get(session).getChannelNumber());
		networkService.fireConnectionTerminated(channels.get(session));
		channels.remove(session);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception
	{
//		System.out.println ("Idle: " + status.toString());
		networkService.fireError(channels.get(session), new SocketTimeoutException());
	}

	/**
	 * Wait for a Object.
	 *
	 */
	@Override
	public void messageReceived (IoSession session, Object message) throws Exception
	{
//		System.out.println("Received-Message: " + ((IObject) message).getTypeId() + ":" + ((IObject) message).getUniqueId());
		channels.get(session).received(message);
	}

	@Override
	public void exceptionCaught (IoSession session, Throwable cause )
	{
		cause.printStackTrace();
		if (cause != null)
		{
			Log.logDebug("network", "Channel", "SocketTimeoutException");
//			networkService.fireConnectionTerminated(this);
		}

//			catch (NoSuchIObjectException x)
//			{
//				Log.logError("network", "Channel", "NoSuchPrototypeRegisteredException: " + x);
//				object = null;
//				setConnectionState(NETWORK_ERROR);
//				networkService.fireError(this, x);
//				networkService.fireConnectionTerminated(this);
//
//				return;
//			}
//			catch (ClassNotFoundException x)
//			{
//				Log.logError("network", "Channel", "ClassNotFoundException");
//				setConnectionState(NETWORK_ERROR);
//				networkService.fireError(this, x);
//				networkService.fireConnectionTerminated(this);
//
//				return;
//			}
//			catch (EOFException x)
//			{
//				Log.logDebug("network", "Channel", "EOFException (ConnectionClosed?!)");
//				setConnectionState(NETWORK_CLOSE);
//				setState(Threadable.CLOSING);
//				networkService.fireError(this, x);
//				networkService.fireConnectionTerminated(this);
//
//				return;
//			}
//			catch (SocketException x)
//			{
//				Log.logDebug("network", "Channel", "SocketClosed.");
//				setConnectionState(NETWORK_CLOSE);
//				setState(Threadable.CLOSING);
//				networkService.fireError(this, x);
//				networkService.fireConnectionTerminated(this);
//
//				return;
//			}
//			catch (IOException x)
//			{
//				setState(Threadable.CLOSING);
//				Log.logError("network", "Channel", "IOException: " + x);
//				x.printStackTrace();
//
//				setConnectionState(NETWORK_ERROR);
//				networkService.fireError(this, x);
//				networkService.fireConnectionTerminated(this);
//
//				return;
//			}
//		}

		return;
	}




	/**
	 * Send a Object over the ObjectStream.
	 *
	 * @param object The Object to send.
	 */
	public void send(Object object)
	{

//		try
//		{
//			Log.logDebug("network", "Channel", "Sending Object (Channel:" + channelNumber + "):" + object);
//
//			++numSendObjects;
//			streamOrganizer.send(object);
//		}
//		catch (IOException e)
//		{
//			Log.logError("network", "Channel", "Serializeable?" + e);
//		}
	}
}
