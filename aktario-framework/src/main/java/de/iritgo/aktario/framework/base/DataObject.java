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

package de.iritgo.aktario.framework.base;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.iobject.IObjectList;
import de.iritgo.aktario.core.iobject.IObjectProxy;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.framework.command.CommandTools;
import de.iritgo.aktario.framework.dataobject.DynIObjectFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 *
 */
public class DataObject extends BaseObject implements IObject, DataObjectInterface
{
	/** The object attributes. */
	private Map attributes;

	/** The number attributes. */
	@SuppressWarnings("unused")
	private int numAttributes;

	/**
	 * Create a new DataObject.
	 *
	 * @param typeId The type id of the data object.
	 */
	public DataObject(String typeId)
	{
		super(typeId);
		attributes = new LinkedHashMap();
	}

	/**
	 * Get the attributes.
	 *
	 * @return The data object attributes.
	 */
	public Map getAttributes()
	{
		return attributes;
	}

	/**
	 * Set the attributes.
	 *
	 * @param attributes The new attributes.
	 */
	public void setAttributes(Map attributes)
	{
		this.attributes = attributes;
	}

	/**
	 * Get the number of attributes.
	 *
	 * @return The data object attributes.
	 */
	public int getNumAttributes()
	{
		return attributes.size();
	}

	/**
	 * Add an int attribute to this data object.
	 *
	 * @param name The attribute name.
	 * @param value The default attribute value.
	 */
	public void addAttribute(String name, int value)
	{
		attributes.put(name, new Integer(value));
	}

	/**
	 * Add a long attribute to this data object.
	 *
	 * @param name The attribute name.
	 * @param value The default attribute value.
	 */
	public void addAttribute(String name, long value)
	{
		attributes.put(name, new Long(value));
	}

	/**
	 * Add a boolean attribute to this data object.
	 *
	 * @param name The attribute name.
	 * @param value The default attribute value.
	 */
	public void addAttribute(String name, boolean value)
	{
		attributes.put(name, new Integer(value ? 1 : 0));
	}

	/**
	 * Add a float attribute to this data object.
	 *
	 * @param name The attribute name.
	 * @param value The default attribute value.
	 */
	public void addAttribute(String name, float value)
	{
		attributes.put(name, new Float(value));
	}

	/**
	 * Add a double attribute to this data object.
	 *
	 * @param name The attribute name.
	 * @param value The default attribute value.
	 */
	public void addAttribute(String name, double value)
	{
		attributes.put(name, new Double(value));
	}

	/**
	 * Add a date attribute to this data object.
	 *
	 * @param name The attribute name.
	 * @param value The default attribute value.
	 */
	public void addAttribute(String name, Date value)
	{
		setAttribute(name, value.getTime());
	}

	/**
	 * Add a data object attribute to this data object.
	 *
	 * @param name The attribute name.
	 * @param value The default attribute value.
	 */
	public void addAttribute(String name, DataObject value)
	{
		attributes.put(name, value);
	}

	/**
	 * Add an object attribute to this data object.
	 *
	 * @param name The attribute name.
	 * @param value The default attribute value.
	 */
	public void addAttribute(String name, Object value)
	{
		attributes.put(name, value);
	}

	/**
	 * Add a data object list attribute to this data object.
	 *
	 * @param name The attribute name.
	 * @param klass The class of the list items.
	 */
	public void addAttribute(String name, Class klass)
	{
		try
		{
			setAttribute(name, new IObjectList(name, new FrameworkProxy((IObject) klass.newInstance()), this));
		}
		catch (Exception x)
		{
			Log.log("system", "DataObject.addAttribute", "Unable to create list item prototype: " + x, Log.ERROR);
		}
	}

	/**
	 * Set an attribute.
	 *
	 * @param name The attribute key.
	 * @param object The attribute value.
	 */
	public void setAttribute(String name, Object object)
	{
		attributes.put(name, object);
	}

	/**
	 * Set an attribute.
	 *
	 * @param name The attribute key.
	 * @param value The attribute value.
	 */
	public void setAttribute(String name, float value)
	{
		attributes.put(name, new Float(value));
	}

	/**
	 * Set an attribute.
	 *
	 * @param name The attribute key.
	 * @param value The attribute value.
	 */
	public void setAttribute(String name, int value)
	{
		attributes.put(name, new Integer(value));
	}

	/**
	 * Set an attribute.
	 *
	 * @param name The attribute key.
	 * @param value The attribute value.
	 */
	public void setAttribute(String name, boolean value)
	{
		attributes.put(name, new Integer(value ? 1 : 0));
	}

	/**
	 * Set an attribute.
	 *
	 * @param name The attribute key.
	 * @param value The attribute value.
	 */
	public void setAttribute(String name, double value)
	{
		attributes.put(name, new Double(value));
	}

	/**
	 * Set an attribute.
	 *
	 * @param name The attribute key.
	 * @param value The attribute value.
	 */
	public void setAttribute(String name, long value)
	{
		attributes.put(name, new Long(value));
	}

	/**
	 * Set an attribute.
	 *
	 * @param name The attribute key.
	 * @param object The attribute value.
	 */
	public void setAttribute(String name, DataObject object)
	{
		attributes.put(name, object);
	}

	/**
	 * Set an attribute.
	 *
	 * @param name The attribute key.
	 * @param object The attribute value.
	 */
	public void setAttribute(String name, Date object)
	{
		setAttribute(name, object.getTime());
	}

	/**
	 * Get an attribute.
	 *
	 * @param name The attribute key.
	 * @return The attribute value.
	 */
	public Object getAttribute(String name)
	{
		return attributes.get(name);
	}

	/**
	 * Get an attribute.
	 *
	 * @param name The attribute key.
	 * @return The attribute value.
	 */
	public Date getDateAttribute(String name)
	{
		return new Date(((Long) attributes.get(name)).longValue());
	}

	/**
	 * Get an attribute.
	 *
	 * @param name The attribute key.
	 * @return The attribute value.
	 */
	public int getIntAttribute(String name)
	{
		return ((Integer) attributes.get(name)).intValue();
	}

	/**
	 * Get an attribute.
	 *
	 * @param name The attribute key.
	 * @return The attribute value.
	 */
	public boolean getBooleanAttribute(String name)
	{
		return getIntAttribute(name) == 1;
	}

	/**
	 * Get an attribute.
	 *
	 * @param name The attribute key.
	 * @return The attribute value.
	 */
	public String getStringAttribute(String name)
	{
		return (String) attributes.get(name);
	}

	/**
	 * Get an attribute.
	 *
	 * @param name The attribute key.
	 * @return The attribute value.
	 */
	public double getDoubleAttribute(String name)
	{
		return ((Double) attributes.get(name)).doubleValue();
	}

	/**
	 * Get an attribute.
	 *
	 * @param name The attribute key.
	 * @return The attribute value.
	 */
	public long getLongAttribute(String name)
	{
		return ((Long) attributes.get(name)).longValue();
	}

	/**
	 * Get an attribute.
	 *
	 * @param name The attribute key.
	 * @return The attribute value.
	 */
	public IObjectList getIObjectListAttribute(String name)
	{
		return ((IObjectList) attributes.get(name));
	}

	/**
	 * Get an attribute.
	 *
	 * @param name The attribute key.
	 * @return The attribute value.
	 */
	public IObjectList getListAttribute(String name)
	{
		return getIObjectListAttribute(name);
	}

	/**
	 * Get an attribute.
	 *
	 * @param name The attribute key.
	 * @return The attribute value.
	 */
	public DataObject getDataObjectAttribute(String name)
	{
		return (DataObject) attributes.get(name);
	}

	/**
	 * Read the attributes from the given stream.
	 */
	public void readObject(InputStream stream) throws IOException, ClassNotFoundException
	{
		DataInputStream dataStream = new DataInputStream(stream);

		int attributeSize = dataStream.readInt();

		addNewAttributesIfNecessary(dataStream, attributeSize);

		uniqueId = dataStream.readLong();

		int q = 0;

		for (Iterator i = attributes.keySet().iterator(); i.hasNext() && (q < attributeSize);)
		{
			++q;

			String key = (String) i.next();

			Object object = attributes.get(key);

			if (object instanceof Integer)
			{
				setAttribute(key, new Integer(dataStream.readInt()));

				continue;
			}

			if (object instanceof String)
			{
				setAttribute(key, new String(dataStream.readUTF()));

				continue;
			}

			if (object instanceof Long)
			{
				setAttribute(key, new Long(dataStream.readLong()));

				continue;
			}

			if (object instanceof Float)
			{
				setAttribute(key, new Float(dataStream.readFloat()));

				continue;
			}

			if (object instanceof Double)
			{
				setAttribute(key, new Double(dataStream.readDouble()));

				continue;
			}

			if (object instanceof IObjectList)
			{
				((IObjectList) object).readObject(stream);

				continue;
			}

			if (object instanceof DataObject)
			{
				((DataObject) object).readObject(stream);

				// 				IObjectProxy existsProxy =
				// 					(IObjectProxy) Engine.getEngine ().getProxyRegistry ().getProxy (getUniqueId ());
				// 				if (existsProxy == null)
				// 				{
				// 					IObjectProxy proxy = new IObjectProxy (this);
				// 					Engine.getEngine ().getProxyRegistry ().addProxy (proxy);
				// 				}
				continue;
			}

			Log.log("network", "DataObject.readObject", "Unknown attribute in data object:"
							+ object.getClass().getName(), Log.FATAL);
		}
	}

	/**
	 * @param dataStream
	 * @param attributeSize
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void addNewAttributesIfNecessary(DataInputStream dataStream, int attributeSize)
	{
		if (attributeSize != ((DynIObjectFactory) Engine.instance().getIObjectFactory())
						.getDataObjectAttributeSize(getTypeId()))
		{
			System.out.println("addNewAttributesIfNecessary");
			attributes.clear();
			readTypeInformations(dataStream, this);
		}
	}

	/**
	 * Write the attributes to the given stream.
	 */
	public void writeObject(OutputStream stream) throws IOException
	{
		DataOutputStream dataStream = new DataOutputStream(stream);

		dataStream.writeInt(attributes.size());

		sendNewAttributesIfNecessary(dataStream);

		dataStream.writeLong(uniqueId);

		for (Iterator i = attributes.keySet().iterator(); i.hasNext();)
		{
			String key = (String) i.next();

			Object object = attributes.get(key);

			if (object instanceof Integer)
			{
				writeAttribute(dataStream, (Integer) object);
			}

			if (object instanceof String)
			{
				writeAttribute(dataStream, (String) object);
			}

			if (object instanceof Long)
			{
				writeAttribute(dataStream, (Long) object);
			}

			if (object instanceof Float)
			{
				writeAttribute(dataStream, (Float) object);
			}

			if (object instanceof Double)
			{
				writeAttribute(dataStream, (Double) object);
			}

			if (object instanceof IObjectList)
			{
				writeAttribute(dataStream, (IObjectList) object);
			}

			if (object instanceof DataObject)
			{
				writeAttribute(dataStream, (DataObject) object);
			}
		}
	}

	/**
	 * @param dataStream
	 * @throws IOException
	 */
	private void sendNewAttributesIfNecessary(DataOutputStream dataStream)
	{
		if (getNumAttributes() != ((DynIObjectFactory) Engine.instance().getIObjectFactory())
						.getDataObjectAttributeSize(getTypeId()))
		{
			System.out.println("sendNewAttributesIfNecessary: "
							+ getNumAttributes()
							+ ":"
							+ ((DynIObjectFactory) Engine.instance().getIObjectFactory())
											.getDataObjectAttributeSize(getTypeId()));
			writeTypeInformations(dataStream, null);
		}
	}

	private void writeAttribute(DataOutputStream stream, Integer integer) throws IOException
	{
		stream.writeInt(integer.intValue());
	}

	private void writeAttribute(DataOutputStream stream, String string) throws IOException
	{
		stream.writeUTF(string);
	}

	private void writeAttribute(DataOutputStream stream, Long longAttribute) throws IOException
	{
		stream.writeLong(longAttribute.longValue());
	}

	private void writeAttribute(DataOutputStream stream, Float floatAttribute) throws IOException
	{
		stream.writeFloat(floatAttribute.floatValue());
	}

	private void writeAttribute(DataOutputStream stream, Double doubleAttribute) throws IOException
	{
		stream.writeDouble(doubleAttribute.doubleValue());
	}

	private void writeAttribute(OutputStream stream, IObjectList proxyLinkedList) throws IOException
	{
		proxyLinkedList.writeObject(stream);
	}

	private void writeAttribute(OutputStream stream, DataObject dataObject) throws IOException
	{
		if (getNumAttributes() != ((DynIObjectFactory) Engine.instance().getIObjectFactory())
						.getDataObjectAttributeSize(getTypeId()))
		{
			dataObject.writeObject(stream);
		}
	}

	/**
	 * Create a instance of the iritgo object.
	 */
	public IObject create()
	{
		try
		{
			IObject newProt = (IObject) this.getClass().newInstance();

			newProt.setTypeId(getTypeId());

			return newProt;
		}
		catch (Exception x)
		{
			Log.log("system", "DataObject:create", "Cannot create class: " + toString(), Log.FATAL);
		}

		return null;
	}

	public void update()
	{
		CommandTools.performSimple(new de.iritgo.aktario.framework.client.command.EditIObject(this));
	}

	public void markAsInvalid()
	{
		((IObjectProxy) Engine.instance().getProxyRegistry().getProxy(getUniqueId(), getTypeId())).reset();
	}

	/**
	 * Check if this dataobject valid
	 *
	 * @return boolean The valid state of this object
	 */
	@Override
	public boolean isValid()
	{
		if (uniqueId == 0)
		{
			return false;
		}

		IObjectProxy iObjectProxy = Engine.instance().getProxyRegistry().getProxy(getUniqueId(), getTypeId());

		return iObjectProxy == null ? false : iObjectProxy.isUpToDate();
	}

	public void waitForTransfer()
	{
		while (true)
		{
			if (((IObjectProxy) Engine.instance().getProxyRegistry().getProxy(getUniqueId(), getTypeId())).isUpToDate())
			{
				break;
			}

			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException x)
			{
			}
		}
	}

	/**
	 * Serialize the object type information on this object
	 */
	public IObject writeTypeInformations(OutputStream stream, IObject iObject)
	{
		DataOutputStream dataStream = new DataOutputStream(stream);

		try
		{
			dataStream.writeInt(attributes.size());

			for (Iterator i = attributes.keySet().iterator(); i.hasNext();)
			{
				String key = (String) i.next();

				Object object = attributes.get(key);

				dataStream.writeUTF(object.getClass().getName());
				dataStream.writeUTF(key);

				if (object.getClass().getPackage().getName().indexOf("java.lang") < 0)
				{
					((IObject) object).writeTypeInformations(stream, this);
				}
			}
		}
		catch (IOException x)
		{
			Log.log("system", "DataObject:sendNewAttributesIfNecessary", "Error (wti): " + x, Log.FATAL);
		}

		return this;
	}

	/**
	 * Read serialize type information a given stream
	 * and do some things...
	 */
	public IObject readTypeInformations(InputStream stream, IObject iObject)
	{
		DataInputStream dataStream = new DataInputStream(stream);

		String objectType = "";
		String attributeName = "";

		try
		{
			int attributeSize = dataStream.readInt();

			Object object = null;

			for (int i = 0; i < attributeSize; ++i)
			{
				objectType = dataStream.readUTF();
				attributeName = dataStream.readUTF();

				if (objectType.indexOf("java.lang") >= 0)
				{
					object = Class.forName(objectType).newInstance();
				}
				else
				{
					object = Class.forName(objectType).newInstance();
					((IObject) object).readTypeInformations(stream, this);
				}

				addAttribute(attributeName, object);
			}
		}
		catch (Exception x)
		{
			x.printStackTrace();
			Log.log("network", "DataObject.readObject", "Unknown attribute in data object:" + typeId + "." + objectType
							+ "." + attributeName, Log.FATAL);
		}

		return this;
	}

	/**
	 * Clone the attributes map and return a new map with the attributes
	 *
	 * @return The new attribute map
	 */
	public Map cloneAttributesDescription()
	{
		return new LinkedHashMap(getAttributes());
	}

	/**
	 * Return a dump form the current object.
	 *
	 * @return String The current dump
	 */
	@Override
	public String dump()
	{
		String dump = "";

		for (Iterator i = attributes.keySet().iterator(); i.hasNext();)
		{
			String key = (String) i.next();

			Object object = attributes.get(key);

			dump += key + ":" + object + "\n";
		}

		return dump;
	}

	@Override
	public String toString()
	{
		return super.toString() + "[id=" + uniqueId + ",type=" + typeId + "]";
	}
}
