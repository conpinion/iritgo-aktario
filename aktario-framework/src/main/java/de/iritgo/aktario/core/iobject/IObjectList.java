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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;


/**
 * A list of iObjects.
 */
public class IObjectList extends LinkedList implements IObject
{
	/** */
	private static final long serialVersionUID = 1L;

	private IObjectProxy proxy;

	private Map proxyPrototypeMapping;

	/** The object that owns this list. */
	private IObject owner;

	/** The name of the list attribute in the enclosing object. */
	private String attributeName;

	/** The uniqueId of the object. */
	protected long uniqueId;

	@SuppressWarnings("unused")
	private Object uniqueIdObject = new Object ();

	/**
	 * Create a new object list.
	 *
	 */
	public IObjectList ()
	{
		proxyPrototypeMapping = new HashMap ();
	}

	/**
	 * Create a new object list.
	 *
	 * @param attributeName The name of the list attribute in the enclosing
	 *   object.
	 * @param proxy
	 * @param owner The owner of this list.
	 */
	public IObjectList (String attributeName, IObjectProxy proxy, IObject owner)
	{
		this.attributeName = attributeName;
		this.proxy = proxy;
		proxyPrototypeMapping = new HashMap ();
		this.owner = owner;
		setUniqueId (owner.getUniqueId ());
	}

	/**
	 * Get the type id of the iritgo object.
	 *
	 * @return The type id.
	 */
	public String getTypeId ()
	{
		return "IObjectList";
	}

	/**
	 * Set the id of the iritgo object.
	 *
	 * @param id The new type
	 */
	public void setTypeId (String id)
	{
	}

	/**
	 * Get the id of the iritgo object.
	 *
	 * @return The unique id.
	 */
	public long getUniqueId ()
	{
		return uniqueId;
	}

	/**
	 * Set the id of the iritgo object.
	 *
	 * @param uniqueId The new type
	 */
	public void setUniqueId (long uniqueId)
	{
		this.uniqueId = uniqueId;
	}

	/**
	 * Get the the owner of this list.
	 *
	 * @return The list owner.
	 */
	public IObject getOwner ()
	{
		return owner;
	}

	/**
	 * Get the attribute name of this list (as specified in the
	 * owning object).
	 *
	 * @return attributeName The name of the list attribute in the enclosing
	 *   object.
	 */
	public String getAttributeName ()
	{
		return attributeName;
	}

	private IObjectProxy createListProxy (Object object)
	{
		IObjectProxy tmpProxy = null;

		IObject prototype = (IObject) object;

		IObjectProxy existsProxy = (IObjectProxy) Engine.instance ().getProxyRegistry ().getProxy (
						((IObject) object).getUniqueId (), prototype.getTypeId ());

		if (existsProxy == null)
		{
			prototype = (IObject) object;

			if (prototype.getUniqueId () == 0) // Ist ein neues Object, vom Client erzeugt, bekommt eine temp uniqueId
			{
				prototype.setUniqueId ((Engine.instance ().getPersistentIDGenerator ().createId () * - 1));
			}

			IObjectProxy clonedProxy = (IObjectProxy) proxy.createProxy ();

			clonedProxy.setSampleRealObject ((IObject) prototype);

			Engine.instance ().getBaseRegistry ().add ((BaseObject) prototype);
			Engine.instance ().getProxyRegistry ().addProxy (clonedProxy, prototype.getTypeId ());

			tmpProxy = clonedProxy;

			Engine.instance ().getEventRegistry ().fire ("proxylinkedlistupdate",
							new IObjectListEvent (prototype, owner, attributeName, IObjectListEvent.ADD));
		}
		else
		{
			tmpProxy = existsProxy;
		}

		proxyPrototypeMapping.put (tmpProxy.getSampleRealObject (), tmpProxy);

		return tmpProxy;
	}

	/**
	 * Add an element to this list.
	 *
	 * @param object The object to add.
	 */
	@Override
	public boolean add (Object object)
	{
		IObject prototype = (IObject) object;

		boolean createdByClient = prototype.getUniqueId () <= 0;

		if (createdByClient)
		{
			createListProxy (object);

			return true;
		}

		return super.add (createListProxy (object));
	}

	/**
	 * Remove an element from this list.
	 *
	 * @param object The object to remove.
	 */
	@Override
	public boolean remove (Object object)
	{
		if (object == null)
		{
			return false;
		}

		Engine.instance ().getEventRegistry ().fire ("proxylinkedlistupdate",
						new IObjectListEvent ((IObject) object, owner, attributeName, IObjectListEvent.REMOVE));

		// 		//TODO: Check is read only, becourse it is open by an other user.
		// 		proxyPrototypeMapping.remove (proxy);
		// 		return super.remove (proxy);
		return true;
	}

	/**
	 * Remove an iobject from this list. It will only use form a action, don't use it from the client.
	 *
	 * @param iObject The iObject to remove.
	 */
	public boolean removeIObject (IObject iObject)
	{
		IObjectProxy proxy = (IObjectProxy) proxyPrototypeMapping.get (iObject);

		if (proxy == null)
		{
			Log.logFatal ("system", "ProxyLinkedList.removeIObject", "That object '" + iObject + ":"
							+ iObject.getUniqueId () + " is to remove, but no mapping exists in this iObjectList: "
							+ getAttributeName () + " Owner:" + getOwner () + ":" + getOwner ().getUniqueId ());

			return false;
		}

		//TODO: Check is read only, becourse it is open by an other user.
		proxyPrototypeMapping.remove (proxy);

		return super.remove (proxy);
	}

	/**
	 * Retrieve an element of this list at a specific position.
	 *
	 * @param index The index of the element to retrieve.
	 * @return The element at the specified index.
	 */
	@Override
	public Object get (int index)
	{
		IObjectProxy proxy = (IObjectProxy) super.get (index);

		return proxy.getRealObject ();
	}

	/**
	 * Check if a object in the list
	 *
	 * @param object The object.
	 * @return True or fase
	 */
	@Override
	public boolean contains (Object object)
	{
		IObjectProxy existsProxy = (IObjectProxy) Engine.instance ().getProxyRegistry ().getProxy (
						((IObject) object).getUniqueId (), ((IObject) object).getTypeId ());

		if (existsProxy == null)
		{
			return false;
		}

		return super.contains (existsProxy);
	}

	/**
	 * Get an iterator over all real elements in this list. Do not use this function.
	 * In most cases you need a transaction object.
	 *
	 * @return An element iterator.
	 */
	@Override
	synchronized public Iterator iterator ()
	{
		return new IObjectIterator (this);
	}

	/**
	 * Get an iterator over all elements in this list.
	 *
	 * @return An element iterator.
	 */
	public Iterator getListIterator ()
	{
		return super.iterator ();
	}

	/**
	 * Read the object attributes from an input stream.
	 *
	 * @param stream The input stream.
	 */
	public void readObject (InputStream stream) throws IOException, ClassNotFoundException
	{
		DataInputStream dataStream = new DataInputStream (stream);

		int size = dataStream.readInt ();

		int curSize = super.size ();

		if (size >= curSize)
		{
			for (int i = 0; i < curSize; ++i)
			{
				@SuppressWarnings("unused")
				String receivedTypeId = dataStream.readUTF ();
				@SuppressWarnings("unused")
				long uniqueId = dataStream.readLong ();
			}

			int newRecords = size - curSize;

			for (int i = 0; i < newRecords; ++i)
			{
				String receivedTypeId = dataStream.readUTF ();
				long uniqueId = dataStream.readLong ();
				IObject prototype = null;

				try
				{
					prototype = Engine.instance ().getIObjectFactory ().newInstance (receivedTypeId);

					// 					System.out.println (uniqueIdObject + ":" + newRecords + " - Owner: " + getOwner ().getUniqueId () + ":" + receivedTypeId +":"+ prototype.getTypeId ());
				}
				catch (NoSuchIObjectException x)
				{
					Log.log ("system", "ProxyLinkedList.readObject",
									"ProxyLinkedList - Prototype not found! DataObject not in plugin registered? : "
													+ receivedTypeId, Log.WARN);

					// Oh, you don't have register the Prototype.
					// Thats not very bad, if you use only one Prototype-type in the list!
				}

				if (prototype == null)
				{
					prototype = proxy.getRealObject ().create ();
				}

				prototype.setUniqueId (uniqueId);

				super.add (createListProxy (prototype));
			}
		}
	}

	/**
	 * Write the object attributes to an output stream.
	 *
	 * @param stream The output stream.
	 */
	synchronized public void writeObject (OutputStream stream) throws IOException
	{
		DataOutputStream dataStream = new DataOutputStream (stream);

		int size = super.size ();

		dataStream.writeInt (size);

		for (Iterator i = super.iterator (); i.hasNext ();)
		{
			IObjectProxy proxy = (IObjectProxy) i.next ();

			IObject prot = proxy.getSampleRealObject ();

			dataStream.writeUTF (prot.getTypeId ());
			dataStream.writeLong (proxy.getUniqueId ());

			// 			System.out.println (uniqueIdObject + ":" + size + " - W-Owner: " + getOwner ().getUniqueId () + ":" + prot.getTypeId () +":"+ prot);
		}
	}

	/**
	 * Remove all elements from this list.
	 */
	@Override
	public void clear ()
	{
		// TODO: hack! We must wrote a bulk thing here...
		LinkedList tmpList = new LinkedList (this);

		for (Iterator i = tmpList.iterator (); i.hasNext ();)
		{
			remove (((IObjectProxy) i.next ()).getRealObject ());
		}
	}

	/**
	 * Remove all elements from this list.
	 */
	public void clearIObjectList ()
	{
		proxyPrototypeMapping.clear ();
		super.clear ();
	}

	/**
	 * Create a instance of the iritgo object.
	 */
	public IObject create ()
	{
		return new IObjectList (attributeName, proxy, owner);
	}

	/**
	 * Check wether this is a valid object or not.
	 *
	 * @return True for a valid object.
	 */
	public boolean isValid ()
	{
		return true;
	}

	/**
	 * Serialize the object type information on this object
	 */
	public IObject writeTypeInformations (OutputStream stream, IObject iObject)
	{
		try
		{
			DataOutputStream dataStream = new DataOutputStream (stream);

			dataStream.writeUTF (attributeName);
			dataStream.writeUTF (proxy.getClass ().getName ());
		}
		catch (Exception x)
		{
		}

		return this;
	}

	/**
	 * Read Serialize type information a given stream
	 * and do some things...
	 */
	public IObject readTypeInformations (InputStream stream, IObject iObject)
	{
		DataInputStream dataStream = new DataInputStream (stream);

		try
		{
			attributeName = dataStream.readUTF ();
			proxy = (IObjectProxy) Class.forName (dataStream.readUTF ()).newInstance ();
			owner = iObject;
		}
		catch (Exception x)
		{
			x.printStackTrace ();
		}

		return null;
	}

	/**
	 * Return a dump form the current object.
	 *
	 * @return String The current dump
	 */
	public String dump ()
	{
		return toString ();
	}
}
