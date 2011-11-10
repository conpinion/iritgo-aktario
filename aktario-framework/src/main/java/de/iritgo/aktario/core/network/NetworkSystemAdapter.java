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


import de.iritgo.aktario.core.iobject.NoSuchIObjectException;
import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;


/**
 *
 */
public class NetworkSystemAdapter implements NetworkSystemListener
{
	public void connectionEstablished(NetworkService networkBase, Channel connectedChannel)
	{
	}

	public void connectionTerminated(NetworkService networkBase, Channel connectedChannel)
	{
	}

	public void error(NetworkService networkBase, Channel connectedChannel, NoSuchIObjectException x)
	{
	}

	public void error(NetworkService networkBase, Channel connectedChannel, SocketTimeoutException x)
	{
	}

	public void error(NetworkService networkBase, Channel connectedChannel, ClassNotFoundException x)
	{
	}

	public void error(NetworkService networkBase, Channel connectedChannel, EOFException x)
	{
	}

	public void error(NetworkService networkBase, Channel connectedChannel, SocketException x)
	{
	}

	public void error(NetworkService networkBase, Channel connectedChannel, IOException x)
	{
	}
}
