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

package de.iritgo.aktario.core.tools;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 *
 */
public class EasyHashMap extends HashMap
{
	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * Create a new empty map.
	 */
	public EasyHashMap()
	{
	}

	/**
	 * Put an integer attribute.
	 *
	 * @param key The attribute key.
	 * @param value The attribute value.
	 */
	public void put(String key, int value)
	{
		super.put(key, new Integer(value));
	}

	/**
	 * Put a long attribute.
	 *
	 * @param key The attribute key.
	 * @param value The attribute value.
	 */
	public void put(String key, long value)
	{
		super.put(key, new Long(value));
	}

	/**
	 * Put a float attribute.
	 *
	 * @param key The attribute key.
	 * @param value The attribute value.
	 */
	public void put(String key, float value)
	{
		super.put(key, new Float(value));
	}

	/**
	 * Put a double attribute.
	 *
	 * @param key The attribute key.
	 * @param value The attribute value.
	 */
	public void put(String key, double value)
	{
		super.put(key, new Double(value));
	}

	/**
	 * Put a boolean attribute.
	 *
	 * @param key The attribute key.
	 * @param value The attribute value.
	 */
	public void put(String key, boolean value)
	{
		super.put(key, new Boolean(value));
	}

	/**
	 * Put a string attribute.
	 *
	 * @param key The attribute key.
	 * @param value The attribute value.
	 */
	public void put(String key, String value)
	{
		super.put(key, value);
	}

	/**
	 * Put an object attribute.
	 *
	 * @param key The attribute key.
	 * @param value The attribute value.
	 */
	public void put(String key, Object value)
	{
		super.put(key, value);
	}

	/**
	 * Get an integer attribute.
	 *
	 * @param key The attribute key.
	 * @return The attribute value.
	 */
	public int getInt(String key)
	{
		return ((Integer) super.get(key)).intValue();
	}

	/**
	 * Get a long attribute.
	 *
	 * @param key The attribute key.
	 * @return The attribute value.
	 */
	public long getLong(String key)
	{
		return ((Long) super.get(key)).longValue();
	}

	/**
	 * Get a float attribute.
	 *
	 * @param key The attribute key.
	 * @return The attribute value.
	 */
	public float getFloat(String key)
	{
		return ((Float) super.get(key)).floatValue();
	}

	/**
	 * Get a double attribute.
	 *
	 * @param key The attribute key.
	 * @return The attribute value.
	 */
	public double getDouble(String key)
	{
		return ((Double) super.get(key)).doubleValue();
	}

	/**
	 * Get a boolean attribute.
	 *
	 * @param key The attribute key.
	 * @return The attribute value.
	 */
	public boolean getBoolean(String key)
	{
		return ((Boolean) super.get(key)).booleanValue();
	}

	/**
	 * Get a string attribute.
	 *
	 * @param key The attribute key.
	 * @return The attribute value.
	 */
	public String getString(String key)
	{
		return (String) super.get(key);
	}

	/**
	 * Get an object attribute.
	 *
	 * @param key The attribute key.
	 * @return The attribute value.
	 */
	public Object getObject(String key)
	{
		return super.get(key);
	}

	/**
	 * Write the attributes to a stream.
	 */
	public void writeObject(DataOutputStream stream) throws IOException
	{
		stream.writeInt(size());

		for (Iterator i = entrySet().iterator(); i.hasNext();)
		{
			Map.Entry entry = (Map.Entry) i.next();

			stream.writeUTF((String) entry.getKey());

			Object value = entry.getValue();
			Class klass = value.getClass();

			if (klass == Integer.class)
			{
				stream.writeUTF("I");
				stream.writeInt(((Integer) value).intValue());
			}
			else if (klass == Long.class)
			{
				stream.writeUTF("L");
				stream.writeLong(((Long) value).longValue());
			}
			else if (klass == String.class)
			{
				stream.writeUTF("S");
				stream.writeUTF((String) value);
			}
			else if (klass == Boolean.class)
			{
				stream.writeUTF("B");
				stream.writeBoolean(((Boolean) value).booleanValue());
			}
			else if (klass == Float.class)
			{
				stream.writeUTF("F");
				stream.writeFloat(((Float) value).floatValue());
			}
			else if (klass == Double.class)
			{
				stream.writeUTF("D");
				stream.writeDouble(((Double) value).doubleValue());
			}
		}
	}

	/**
	 * Read the attributes from a stream.
	 */
	public void readObject(DataInputStream stream) throws IOException
	{
		int numAttributes = stream.readInt();

		for (int i = 0; i < numAttributes; ++i)
		{
			String key = stream.readUTF();
			String type = stream.readUTF();

			if ("I".equals(type))
			{
				put(key, stream.readInt());
			}

			if ("L".equals(type))
			{
				put(key, stream.readLong());
			}

			if ("S".equals(type))
			{
				put(key, stream.readUTF());
			}

			if ("B".equals(type))
			{
				put(key, stream.readBoolean());
			}

			if ("F".equals(type))
			{
				put(key, stream.readFloat());
			}

			if ("D".equals(type))
			{
				put(key, stream.readDouble());
			}
		}
	}
}
