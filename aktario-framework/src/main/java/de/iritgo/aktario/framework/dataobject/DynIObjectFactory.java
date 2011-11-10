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

package de.iritgo.aktario.framework.dataobject;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.iobject.AbstractIObjectFactory;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.iobject.NoSuchIObjectException;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.framework.base.DataObject;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


/**
 * <code>DynIObjectFactory</code> is a prototype factory that can
 * create new <code>IObject</code>s by specifying their type id,
 * e.g.
 */
public class DynIObjectFactory extends AbstractIObjectFactory
{
	/** A mapping from type names to prototype objects. */
	private Map iObjectPrototypes = null;

	private Map dynIObjectPrototypes = null;

	private Map dataObjectAttributeSizes;

	/**
	 * Create a new <code>DynIObjectFactory</code>.
	 */
	public DynIObjectFactory()
	{
		iObjectPrototypes = new TreeMap();
		dynIObjectPrototypes = new TreeMap();
		dataObjectAttributeSizes = new HashMap();
	}

	public int getDataObjectAttributeSize(String typeId)
	{
		try
		{
			return ((Integer) dataObjectAttributeSizes.get(typeId)).intValue();
		}
		catch (Exception x)
		{
			Log.log("system", "getDataObjectAttributeSize", "Can not retrieve attribute size in: " + typeId, Log.FATAL);
		}

		return 0;
	}

	/**
	 * Register a <code>IObject</code>.
	 *
	 * @param object The prototype object to add.
	 */
	public void register(IObject object)
	{
		if (object instanceof DynDataObject)
		{
			dynIObjectPrototypes.put(object.getTypeId(), object);
		}
		else
		{
			iObjectPrototypes.put(object.getTypeId(), object);
		}

		if (object instanceof DataObject)
		{
			dataObjectAttributeSizes.put(object.getTypeId(), new Integer(((DataObject) object).getNumAttributes()));
		}

		Engine.instance().getEventRegistry().fire("iobjectregisterd", new IObjectRegisteredEvent(object));
	}

	/**
	 * Remove a <code>IObject</code> from the factory.
	 *
	 * @param object The prototype object to remove.
	 */
	public void remove(IObject object)
	{
		iObjectPrototypes.remove(object.getTypeId());
		dynIObjectPrototypes.remove(object.getTypeId());
	}

	/**
	 * Check wether the factory can create objects of a specific type.
	 *
	 * @param typeId The type to check.
	 * @return True if the factory can generate the specified type.
	 */
	public boolean contains(String typeId)
	{
		boolean result = iObjectPrototypes.containsKey(typeId);

		if (result == false)
		{
			return dynIObjectPrototypes.containsKey(typeId) == true ? true : false;
		}

		return true;
	}

	/**
	 * Create a new object instance by specyfing the type id.
	 *
	 * @param typeId The type id of the object to create.
	 */
	public IObject newInstance(String typeId) throws NoSuchIObjectException
	{
		IObject prototype = (IObject) iObjectPrototypes.get(typeId);

		try
		{
			Object dynPrototype = dynIObjectPrototypes.get(typeId);

			if (dynPrototype != null)
			{
				DynDataObject dynDataObject = (DynDataObject) dynPrototype.getClass().newInstance();

				dynDataObject.setTypeId(typeId);
				dynDataObject.setAttributes(((DynDataObject) dynPrototype).cloneAttributesDescription());

				return dynDataObject;
			}

			if (prototype == null)
			{
				Log.log("system", "PrototypeFactory.newInstance", "Type is not registred: " + typeId, Log.FATAL);

				throw new NoSuchIObjectException(typeId);
			}

			return (IObject) prototype.getClass().newInstance();
		}
		catch (Exception x)
		{
			Log.log("system", "PrototypeFactory.newInstance", "Cannot determine classname.", Log.FATAL);
		}

		return null;
	}
}
