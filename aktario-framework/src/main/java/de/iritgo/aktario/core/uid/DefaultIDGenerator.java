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

package de.iritgo.aktario.core.uid;


import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.iobject.IObject;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * A simple in memory id generator that looses it's state when the server shuts down.
 */
public class DefaultIDGenerator extends BaseObject implements IDGenerator
{
	/** The next unique id. */
	protected long id;

	/** The step increment. */
	protected long step;

	/**
	 * Create a new id generator.
	 *
	 * @param typeId The type id of this generator.
	 * @param start The initial id value.
	 * @param step The step increment.
	 */
	public DefaultIDGenerator (String typeId, long start, long step)
	{
		super (typeId);
		this.id = start;
		this.step = step;
	}

	/**
	 * Create a new id generator.
	 *
	 * @param start The initial id value.
	 * @param step The step increment.
	 */
	public DefaultIDGenerator (long start, long step)
	{
		super ("DefaultIDGenerator");
		this.id = start;
		this.step = step;
	}

	/**
	 * Create a new id generator.
	 */
	public DefaultIDGenerator ()
	{
		this ("DefaultIDGenerator", 1, 1);
	}

	/**
	 * Create a new unique id.
	 *
	 * @return The new unique id.
	 */
	public synchronized long createId ()
	{
		long nextId = id;

		id += step;

		return nextId;
	}

	/**
	 * Get the value of the next id that createId() will return.
	 *
	 * @return The next id value.
	 */
	public long peekNextId ()
	{
		return id;
	}

	/**
	 * Create a new instance of the id generator.
	 *
	 * @return The fresh instance.
	 */
	public IObject create ()
	{
		return new DefaultIDGenerator ();
	}

	/**
	 * Load the last generator state.
	 */
	public void load ()
	{
	}

	/**
	 * Store the generator state.
	 */
	public void save ()
	{
	}

	/**
	 * Read the object attributes from an input stream.
	 *
	 * @param stream The input stream.
	 */
	public void readObject (InputStream stream) throws IOException, ClassNotFoundException
	{
		DataInputStream dataStream = new DataInputStream (stream);

		id = dataStream.readLong ();
	}

	/**
	 * Write the object attributes to an output stream.
	 *
	 * @param stream The output stream.
	 */
	public void writeObject (OutputStream stream) throws IOException
	{
		DataOutputStream dataStream = new DataOutputStream (stream);

		dataStream.writeLong (id);
	}

	/**
	 * Serialize the object type information on this object
	 */
	public IObject writeTypeInformations (OutputStream stream, IObject iObject)
	{
		return null;
	}

	/**
	 * Read Serialize type information a given stream
	 * and do some things...
	 */
	public IObject readTypeInformations (InputStream stream, IObject iObject)
	{
		return null;
	}
}
