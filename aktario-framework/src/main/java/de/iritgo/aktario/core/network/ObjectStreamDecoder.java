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

import de.iritgo.aktario.core.*;
import de.iritgo.aktario.core.iobject.*;
import de.iritgo.aktario.core.logger.*;


/**
 *
 */
public class ObjectStreamDecoder extends CumulativeProtocolDecoder
{
	/** The object protocol. */
	private IObjectSerializer objectSerializer;

	/**
	 * Create a new ObjectStream.
	 *
	 * @param socket The communication socket.
	 */
	public ObjectStreamDecoder () throws Exception
	{
		objectSerializer = new IObjectSerializer ();
	}

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out)
	{
		IObject object = null;
		int startPos = in.position ();
		try
		{
			DataInputStream inputStream = new DataInputStream (in.asInputStream());
			String classId = inputStream.readUTF ();

			if (classId.length() == 0)
			{
				Log.log("system", "Prototype.get", "ClassID is NULL", Log.FATAL);
				in.position(startPos);
				return false;
			}

			object = Engine.instance().getIObjectFactory().newInstance(classId);

			if (object == null)
			{
				in.position(startPos);
				return false;
			}

			object.readObject(inputStream);


			if (! classId.equals(object.getTypeId()))
			{
				Log.log("system", "Prototype.get", "Wrong objecttype!!!!!", Log.FATAL);

				in.position(startPos);
				return false;
			}
		}
		catch (IOException x)
		{
//			System.out.println(x.toString() + " ->Current-Pos: " + in.position());
			in.position(startPos);
//			System.out.println("Rewind Pos: " + in.position());
			return false;
		}
		catch (NoSuchIObjectException x)
		{
			x.printStackTrace();
			in.position(startPos);
			return false;
		}
		catch (ClassNotFoundException x)
		{
			x.printStackTrace();
			in.position(startPos);
			return false;
		} catch (InstantiationException x) {
			x.printStackTrace();
			in.position(startPos);
			return false;
		} catch (IllegalAccessException x) {
			x.printStackTrace();
			in.position(startPos);
			return false;
		}
		Log.logDebug("system", "ObjectStream", "Object created and received: " + object);

		out.write (object);
		return true;
	}
}
