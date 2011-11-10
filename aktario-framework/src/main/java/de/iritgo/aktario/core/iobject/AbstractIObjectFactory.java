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


import de.iritgo.aktario.core.base.BaseObject;


/**
 * <code>AbstractIObjectFactory</code> is a prototype factory that can
 * create new <code>IObject</code>s by specifying their type id,
 * e.g.
 */
public class AbstractIObjectFactory extends BaseObject implements IObjectFactoryInterface
{
	private IObjectFactoryInterface iObjectFactory;

	/**
	 * Create a new <code>AbstractIObjectFactory</code>.
	 */
	public AbstractIObjectFactory()
	{
	}

	/**
	 * Set an alternative <code>IObjectFactory</code>.
	 *
	 * @param iObjectFactory The new factory to use
	 */
	public void setIObjectFactory(IObjectFactoryInterface iObjectFactory)
	{
		this.iObjectFactory = iObjectFactory;
	}

	/**
	 * Register a <code>IObject</code>.
	 *
	 * @param object The prototype object to add.
	 */
	public void register(IObject object)
	{
		iObjectFactory.register(object);
	}

	/**
	 * Remove a <code>IObject</code> from the factory.
	 *
	 * @param object The prototype object to remove.
	 */
	public void remove(IObject object)
	{
		iObjectFactory.remove(object);
	}

	/**
	 * Check wether the factory can create objects of a specific type.
	 *
	 * @param typeId The type to check.
	 * @return True if the factory can generate the specified type.
	 */
	public boolean contains(String typeId)
	{
		return iObjectFactory.contains(typeId);
	}

	/**
	 * Create a new object instance by specyfing the type id.
	 *
	 * @param typeId The type id of the object to create.
	 */
	public IObject newInstance(String typeId) throws NoSuchIObjectException
	{
		return iObjectFactory.newInstance(typeId);
	}
}
