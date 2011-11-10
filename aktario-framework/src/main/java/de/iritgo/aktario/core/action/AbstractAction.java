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

package de.iritgo.aktario.core.action;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


/**
 * Absract base class for actions that provides some basic functionality.
 */
public abstract class AbstractAction extends Action
{
	/** Action time stamp. */
	private double timeStamp;

	/** Number of objects that are transferred by this action. */
	private int numObjects;

	/**
	 * Create a new AbstractAction.
	 */
	public AbstractAction()
	{
	}

	/**
	 * Create a new AbstractAction.
	 *
	 * @param uniqueId The unique id of the action.
	 */
	public AbstractAction(long uniqueId)
	{
		super(uniqueId);
	}

	/**
	 * Set the action time stamp.
	 */
	public void setTimeStamp(double timeStamp)
	{
		this.timeStamp = timeStamp;
	}

	/**
	 * Get the action time stamp.
	 */
	public double getTimeStamp()
	{
		return timeStamp;
	}

	/**
	 * Set the number of transferred objects.
	 *
	 * @param numObjects The new number of transferred objects.
	 */
	public void setNumObjects(int numObjects)
	{
		this.numObjects = numObjects;
	}

	/**
	 * Get the number of transferred objects.
	 *
	 * @return The number of transferred objects.
	 */
	public int getNumObjects()
	{
		return numObjects;
	}

	/**
	 * Read the object attributes from a data input stream.
	 *
	 * @param stream The data input stream.
	 */
	@Override
	public void readObject(DataInputStream stream) throws IOException, ClassNotFoundException
	{
		super.readObject(stream);
		timeStamp = stream.readDouble();
		numObjects = stream.readInt();
	}

	/**
	 * Write the object attributes to a data output stream.
	 *
	 * @param stream The data output stream.
	 */
	@Override
	public void writeObject(DataOutputStream stream) throws IOException
	{
		super.writeObject(stream);
		stream.writeDouble(timeStamp);
		stream.writeInt(numObjects);
	}
}
