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


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * All objects that should be transferable between the client and the
 * server, or serializeable to a backend store must implement the
 * <code>IObject</code> interface.
 */
public interface IObject
{
	/**
	 * Get the id of the iritgo object.
	 *
	 * @return The unique id.
	 */
	public long getUniqueId();

	/**
	 * Set the id of the iritgo object.
	 *
	 * @param uniqueId The new unique id.
	 */
	public void setUniqueId(long uniqueId);

	/**
	 * Get the type id of the iritgo object.
	 *
	 * @return The type id.
	 */
	public String getTypeId();

	/**
	 * Set the id of the iritgo object.
	 *
	 * @param typeId The new type
	 */
	public void setTypeId(String typeId);

	/**
	 * Create a new instance of the iritgo object.
	 *
	 * @return The fresh instance.
	 */
	public IObject create();

	/**
	 * Read the object attributes from an input stream.
	 *
	 * @param stream The input stream.
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws NoSuchIObjectException
	 */
	public void readObject(InputStream stream) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchIObjectException;

	/**
	 * Write the object attributes to an output stream.
	 *
	 * @param stream The output stream.
	 */
	public void writeObject(OutputStream stream) throws IOException;

	/**
	 * Check wether this is a valid object or not.
	 *
	 * @return True for a valid object.
	 */
	public boolean isValid();

	/**
	 * Serialize the object type information on this object
	 * @param iObject TODO
	 */
	public IObject writeTypeInformations(OutputStream stream, IObject iObject);

	/**
	 * Read Serialize type information a given stream
	 * and do some things...
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public IObject readTypeInformations(InputStream stream, IObject iObject) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException;

	/**
	 * Return a dump form the current object.
	 *
	 * @return String The current dump
	 */
	public String dump();
}
