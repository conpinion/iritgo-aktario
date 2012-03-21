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


import java.io.*;

import org.apache.mina.core.buffer.*;
import org.apache.mina.core.session.*;
import org.apache.mina.filter.codec.*;

import de.iritgo.aktario.core.iobject.*;
import de.iritgo.aktario.core.logger.*;


/**
 *
 */
public class ObjectStreamEncoder extends ProtocolEncoderAdapter
{
	/** The object protocol. */
	private IObjectSerializer objectSerializer;

	/**
	 * Create a new ObjectStream.
	 *
	 * @param socket The communication socket.
	 */
	public ObjectStreamEncoder () throws Exception
	{
		objectSerializer = new IObjectSerializer ();
	}

	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception
			{

		IoBuffer buffer = IoBuffer.allocate (1024, false);
        buffer.setAutoExpand(true);

		objectSerializer.write (new DataOutputStream (buffer.asOutputStream()), (IObject) message);
		buffer.flip();
		out.write(buffer);
	}
}
