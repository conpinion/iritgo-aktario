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

package de.iritgo.aktario.core.iobject;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.logger.Log;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


/**
 * The IObjectSerializer is used to read IObjects
 * from DataInputStreams and write IObject to
 * DataOutputStreams.
 */
public class IObjectSerializer extends BaseObject
{
	private String classId;

	/**
	 * Create a new IObjectSerializer.
	 */
	public IObjectSerializer()
	{
	}

	/**
	 * Read an IObject from a DataInputStream.
	 *
	 * @param inputStream The input stream to read the object from.
	 * @return The read IObject.
	 * @throws IOException Is thrown if an error occurred during reading.
	 * @throws NoSuchIObjectException Is thrown if an invalid type id
	 *   was transmitted.
	 * @throws ClassNotFoundException Is thrown if an invalid type id
	 *   was transmitted.
	 */
	public IObject read(DataInputStream inputStream) throws IOException, NoSuchIObjectException, ClassNotFoundException
	{
		IObject object = null;

		classId = inputStream.readUTF();

		if (classId.length() == 0)
		{
			Log.log("system", "Prototype.get", "ClassID is NULL", Log.FATAL);

			return null;
		}

		object = Engine.instance().getIObjectFactory().newInstance(classId);

		if (object == null)
		{
			return null;
		}

		object.readObject(inputStream);

		if (! classId.equals(object.getTypeId()))
		{
			Log.log("system", "Prototype.get", "Wrong objecttype!!!!!", Log.FATAL);

			return null;
		}

		return object;
	}

	/**
	 * Write an IritogObject to a DataOutputStream.
	 *
	 * @param outputStream The output stream to write to.
	 * @param object The IritogObject to write.
	 * @throws IOException Is thrown if an error occurred during writing.
	 */
	public void write(DataOutputStream outputStream, IObject object) throws IOException
	{
		synchronized (outputStream)
		{
			outputStream.writeUTF(object.getTypeId());
			object.writeObject(outputStream);
		}
	}
}
