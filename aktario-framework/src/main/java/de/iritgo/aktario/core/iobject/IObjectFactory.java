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


import de.iritgo.aktario.core.logger.Log;
import java.util.Map;
import java.util.TreeMap;


/**
 * <code>IObjectFactory</code> is a prototype factory that can
 * create new <code>IObject</code>s by specifying their type id,
 * e.g.
 */
public class IObjectFactory extends AbstractIObjectFactory
{
	/** A mapping from type names to prototype objects. */
	private Map iObjectPrototypes = null;

	/**
	 * Create a new <code>IObjectFactory</code>.
	 */
	public IObjectFactory ()
	{
		iObjectPrototypes = new TreeMap ();
	}

	/**
	 * Register a <code>IObject</code>.
	 *
	 * @param object The prototype object to add.
	 */
	@Override
	public void register (IObject object)
	{
		iObjectPrototypes.put (object.getTypeId (), object);
	}

	/**
	 * Remove a <code>IObject</code> from the factory.
	 *
	 * @param object The prototype object to remove.
	 */
	@Override
	public void remove (IObject object)
	{
		iObjectPrototypes.remove (object.getTypeId ());
	}

	/**
	 * Check wether the factory can create objects of a specific type.
	 *
	 * @param typeId The type to check.
	 * @return True if the factory can generate the specified type.
	 */
	@Override
	public boolean contains (String typeId)
	{
		return iObjectPrototypes.containsKey (typeId);
	}

	/**
	 * Create a new object instance by specyfing the type id.
	 *
	 * @param typeId The type id of the object to create.
	 */
	@Override
	public IObject newInstance (String typeId) throws NoSuchIObjectException
	{
		IObject prototype = (IObject) iObjectPrototypes.get (typeId);

		if (prototype == null)
		{
			Log.log ("system", "PrototypeFactory.newInstance", "Type is not registred: " + typeId, Log.FATAL);
			throw new NoSuchIObjectException (typeId);
		}

		try
		{
			return (IObject) prototype.getClass ().newInstance ();
		}
		catch (Exception x)
		{
			Log.log ("system", "PrototypeFactory.newInstance", "Cannot determine classname.", Log.FATAL);
		}

		return null;
	}
}
