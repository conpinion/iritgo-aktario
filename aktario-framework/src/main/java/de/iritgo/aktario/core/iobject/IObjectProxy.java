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


/**
 * Proxy for <code>IObjects</code>.
 */
public class IObjectProxy extends BaseObject implements IObject
{
	public static boolean initState = false;

	/** The real object. */
	public long iObjectUniqueId;

	public String iObjectTypeId;

	private boolean upToDate = initState;

	private boolean updateRunning = false;

	/**
	 * Create a new <code>IObjectProxy</code>.
	 */
	public IObjectProxy ()
	{
		super ("IObjectProxy");
	}

	/**
	 * Create a new <code>IObjectProxy</code>.
	 *
	 * @param object The real object.
	 */
	public IObjectProxy (IObject object)
	{
		this ();
		this.uniqueId = object.getUniqueId ();
		this.iObjectUniqueId = object.getUniqueId ();
		this.iObjectTypeId = object.getTypeId ();
	}

	public String getIObjectTypeId ()
	{
		return iObjectTypeId;
	}

	/**
	 * Create a new instance of this object.
	 *
	 * @return A new <code>IObject</code> (In this case a proxy version).
	 */
	public IObject create ()
	{
		return new IObjectProxy ();
	}

	/**
	 * Create a new instance of the proxy.
	 *
	 * @return A new proxy instance.
	 */
	public IObjectProxy createProxy ()
	{
		return new IObjectProxy ();
	}

	/**
	 * Read the object attributes from an input stream.
	 *
	 * @param stream The input stream to read from.
	 * @throws IOException In case of a read error.
	 * @throws ClassNotFoundException If the object class could not be
	 *   instantiated.
	 */
	public void readObject (InputStream stream) throws IOException, ClassNotFoundException
	{
		readObject (new DataInputStream (stream));
	}

	/**
	 * Write the object attributes to an output stream.
	 *
	 * @param stream The output stream to write to.
	 * @throws IOException In case of a write error.
	 */
	public void writeObject (OutputStream stream) throws IOException
	{
		writeObject (new DataOutputStream (stream));
	}

	/**
	 * Read the object attributes from a data input stream.
	 *
	 * @param stream The data input stream to read from.
	 * @throws IOException In case of a read error.
	 * @throws ClassNotFoundException If the object class could not be
	 *   instantiated.
	 */
	public void readObject (DataInputStream stream) throws IOException, ClassNotFoundException
	{
	}

	/**
	 * Write the object attributes to a data output stream.
	 *
	 * @param stream The odata utput stream to write to.
	 * @throws IOException In case of a write error.
	 */
	public void writeObject (DataOutputStream stream) throws IOException
	{
	}

	/**
	 * Update the object attributes from a data input stream.
	 *
	 * @param stream The data input stream to read from.
	 * @throws IOException In case of a read error.
	 * @throws ClassNotFoundException If the object class could not be
	 *             instantiated.
	 */
	public void update (DataInputStream stream) throws IOException, ClassNotFoundException
	{
		try
		{
			setUpToDate (true);
			updateRunning = false;

			IObject object = (IObject) Engine.instance ().getBaseRegistry ().get (iObjectUniqueId, iObjectTypeId);

			object.readObject (stream);

			Engine.instance ().getProxyEventRegistry ().fire (object, new IObjectProxyEvent (object, false));

			Engine.instance ().getEventRegistry ().fire ("proxyisuptodate", new IObjectProxyEvent (object, false));
		}
		catch (IOException x)
		{
			throw new IOException ();
		}
		catch (ClassNotFoundException x)
		{
			throw new ClassNotFoundException ();
		}
	}

	/**
	 * Set the up-to-date flag.
	 *
	 * @param upToDate If true, the proxy object is up-to-date.
	 */
	public void setUpToDate (boolean upToDate)
	{
		this.upToDate = upToDate;
	}

	/**
	 * Check wether the proxy object is up to date or not.
	 *
	 * @return True if the proxy object is up-to-date.
	 */
	public boolean isUpToDate ()
	{
		return upToDate;
	}

	/**
	 * Set the real object.
	 *
	 * @param object The real object.
	 */
	public void setRealObject (IObject object)
	{
		setUniqueId (object.getUniqueId ());
		setUpToDate (true);
		this.iObjectUniqueId = object.getUniqueId ();
		this.iObjectTypeId = object.getTypeId ();
	}

	/**
	 * Set an example real object.
	 *
	 * @param object The object example.
	 */
	public void setSampleRealObject (IObject object)
	{
		if (object.getUniqueId () == 0)
		{
			Log.logFatal ("system", "IObjectProxy", "Tried to set a sample object with uniqueId = 0");

			// TODO: Enter a safe halt mode.
			return;
		}

		setUniqueId (object.getUniqueId ());
		setUpToDate (false);
		this.iObjectUniqueId = object.getUniqueId ();
		this.iObjectTypeId = object.getTypeId ();
	}

	/**
	 * Get the real object.
	 *
	 * @return The real object.
	 */
	public IObject getRealObject ()
	{
		if ((! isUpToDate ()) && (getUniqueId () > 0))
		{
			reset ();
		}

		IObject object = (IObject) Engine.instance ().getBaseRegistry ().get (iObjectUniqueId, iObjectTypeId);

		return object;
	}

	/**
	 * Get the sample real object.
	 *
	 * @return The sample real object.
	 */
	public IObject getSampleRealObject ()
	{
		// TODO: Check the transaction
		IObject object = (IObject) Engine.instance ().getBaseRegistry ().get (iObjectUniqueId, iObjectTypeId);

		return object;
	}

	/**
	 * Reset the proxy and try to get a new real object.
	 */
	public void reset ()
	{
		if (updateRunning)
		{
			return;
		}

		IObject object = (IObject) Engine.instance ().getBaseRegistry ().get (iObjectUniqueId, iObjectTypeId);

		setUpToDate (false);
		updateRunning = true;

		Engine.instance ().getEventRegistry ().fire ("proxyupdate", new IObjectProxyEvent (object, true));
		Engine.instance ().getProxyEventRegistry ().fire (object, new IObjectProxyEvent (object, true));
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
