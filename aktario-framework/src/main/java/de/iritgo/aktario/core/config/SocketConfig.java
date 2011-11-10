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

package de.iritgo.aktario.core.config;


public class SocketConfig
{
	private int port = 3000;

	private int buffer = 64 * 1024;

	private int readTimeout = 60 * 1000;

	private int acceptTimeout = 60 * 1000;

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public int getBuffer()
	{
		return buffer;
	}

	public void setBuffer(int buffer)
	{
		this.buffer = buffer;
	}

	public int getReadTimeout()
	{
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout)
	{
		this.readTimeout = readTimeout;
	}

	public int getAcceptTimeout()
	{
		return acceptTimeout;
	}

	public void setAcceptTimeout(int acceptTimeout)
	{
		this.acceptTimeout = acceptTimeout;
	}
}
