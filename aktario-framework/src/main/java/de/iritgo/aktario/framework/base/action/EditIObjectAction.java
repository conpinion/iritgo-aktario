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

package de.iritgo.aktario.framework.base.action;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.iobject.IObjectProxy;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 *
 */
public class EditIObjectAction extends FrameworkAction
{
	// HELPER CLASS
	private class Helper
	{
		public long uniqueId;

		public String typeId;
	}

	public static int OK = 0;

	public static int ERROR = - 1;

	private List iObjects;

	private int state;

	@SuppressWarnings("unused")
	private long iObjectId;

	/**
	 * Standard constructor
	 */
	public EditIObjectAction ()
	{
		iObjects = new LinkedList ();
	}

	/**
	 * Standard constructor
	 *
	 * @param state The state of this edit
	 * @param iObjectId The iObjectId
	 */
	public EditIObjectAction (int state, IObject iObject)
	{
		this ();

		this.state = state;
		iObjects.add (iObject);
	}

	/**
	 * Standard constructor
	 *
	 * @param state The state of this edit
	 */
	public EditIObjectAction (int state)
	{
		this ();

		this.state = state;
	}

	/**
	 * Add a changed prototype...
	 */
	public void addIObject (IObject iObject)
	{
		iObjects.add (iObject);
	}

	/**
	 * Read the attributes from the given stream.
	 */
	@Override
	public void readObject (FrameworkInputStream stream) throws IOException, ClassNotFoundException
	{
		state = stream.readInt ();

		int size = stream.readInt ();

		for (int i = 0; i < size; ++i)
		{
			Helper helper = new Helper ();

			helper.uniqueId = stream.readLong ();
			helper.typeId = stream.readUTF ();
			iObjects.add (helper);
		}
	}

	/**
	 * Write the attributes to the given stream.
	 */
	@Override
	public void writeObject (FrameworkOutputStream stream) throws IOException
	{
		stream.writeInt (state);
		stream.writeInt (iObjects.size ());

		for (Iterator i = iObjects.iterator (); i.hasNext ();)
		{
			IObject object = (IObject) i.next ();

			stream.writeLong (object.getUniqueId ());
			stream.writeUTF (object.getTypeId ());
		}
	}

	/**
	 * Perform the action.
	 */
	@Override
	public void perform ()
	{
		for (Iterator i = iObjects.iterator (); i.hasNext ();)
		{
			Helper helper = (Helper) i.next ();

			IObjectProxy proxy = (IObjectProxy) Engine.instance ().getProxyRegistry ().getProxy (helper.uniqueId,
							helper.typeId);

			if (proxy != null)
			{
				proxy.reset ();
			}
			else
			{
			}
		}
	}
}
