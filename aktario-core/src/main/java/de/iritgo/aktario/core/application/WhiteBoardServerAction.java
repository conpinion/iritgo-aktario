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

package de.iritgo.aktario.core.application;


import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.base.action.FrameworkInputStream;
import de.iritgo.aktario.framework.base.action.FrameworkOutputStream;
import de.iritgo.aktario.framework.base.action.FrameworkServerAction;
import de.iritgo.aktario.framework.server.Server;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.UserRegistry;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * WhiteBoardServerAction.
 *
 * @version $Id: WhiteBoardServerAction.java,v 1.10 2006/10/05 15:00:42 grappendorf Exp $
 */
public class WhiteBoardServerAction extends FrameworkServerAction
{
	/** Pointers */
	protected class Pointer
	{
		public long userId;

		public int pointerId;

		public Pointer(long userId)
		{
			this.userId = userId;
			pointerId = pointers.size();
		}
	}

	/** List of all pointers. */
	static List pointers = new LinkedList();

	/** The id of the user who is sending this action. */
	protected long userId;

	/** The pointer id. */
	protected int pointerId;

	/** The action type. */
	protected int type;

	/** The mouse x coordinate. */
	protected int x;

	/** The mouse y coordinate. */
	protected int y;

	/** The paint type. */
	protected int paint;

	/**
	 * Create a new action.
	 */
	public WhiteBoardServerAction()
	{
		setTypeId("WBS");
	}

	/**
	 * Create a new action.
	 *
	 * @param user The id of the user who is sending this action.
	 */
	public WhiteBoardServerAction(User user)
	{
		this();
		this.userId = user.getUniqueId();
	}

	/**
	 * Get the user id.
	 *
	 * @return The user id.
	 */
	public long getUserId()
	{
		return userId;
	}

	/**
	 * Get the pointer id.
	 *
	 * @return The pointer id.
	 */
	public int getPointerId()
	{
		return pointerId;
	}

	/**
	 * Get the action type.
	 *
	 * @return The action type.
	 */
	public int getType()
	{
		return type;
	}

	/**
	 * Get mouse x coordinate.
	 *
	 * @return The mouse x coordinate.
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * Get mouse y coordinate.
	 *
	 * @return The mouse y coordinate.
	 */
	public int getY()
	{
		return y;
	}

	/**
	 * Get the paint type.
	 *
	 * @return The paint type.
	 */
	public int getPaint()
	{
		return paint;
	}

	/**
	 * Set this action to be a mouse movement action.
	 *
	 * @param x The mouse x coordinate.
	 * @param y The mouse y coordinate.
	 */
	public void sendMouseMove(int x, int y)
	{
		type = WhiteBoardAction.ACTION_POINTER;
		this.x = x;
		this.y = y;
	}

	/**
	 * Set this action to be a paint action.
	 *
	 * @param x The mouse x coordinate.
	 * @param y The mouse y coordinate.
	 * @param paint The paint type.
	 */
	public void sendPaint(int x, int y, int paint)
	{
		type = WhiteBoardAction.ACTION_PAINT;
		this.x = x;
		this.y = y;
		this.paint = paint;
	}

	/**
	 * Read the attributes from a stream.
	 */
	@Override
	public void readObject(FrameworkInputStream stream) throws IOException
	{
		userId = stream.readLong();
		pointerId = stream.readInt();
		type = stream.readInt();
		x = stream.readInt();
		y = stream.readInt();
		paint = stream.readInt();
	}

	/**
	 * Write the attributes to a stream.
	 */
	@Override
	public void writeObject(FrameworkOutputStream stream) throws IOException
	{
		stream.writeLong(userId);
		stream.writeInt(pointerId);
		stream.writeInt(type);
		stream.writeInt(x);
		stream.writeInt(y);
		stream.writeInt(paint);
	}

	/**
	 * Perform the action.
	 */
	@Override
	public void perform()
	{
		if (type == WhiteBoardAction.ACTION_POINTER)
		{
			Pointer pointer = null;

			synchronized (pointers)
			{
				for (Iterator i = pointers.iterator(); i.hasNext();)
				{
					Pointer p = (Pointer) i.next();

					if (p.userId == userId)
					{
						pointer = p;

						break;
					}
				}

				if (pointer == null)
				{
					pointer = new Pointer(userId);
					pointers.add(pointer);
				}
			}

			pointerId = pointer.pointerId;
		}

		ClientTransceiver ct = (ClientTransceiver) transceiver;
		UserRegistry userRegistry = Server.instance().getUserRegistry();

		for (Iterator i = userRegistry.userIterator(); i.hasNext();)
		{
			User user = (User) i.next();

			if (user.isOnline() && ((type == WhiteBoardAction.ACTION_PAINT) || (user.getUniqueId() != userId)))
			{
				ct.addReceiver(user.getNetworkChannel());
			}
		}

		WhiteBoardAction action = new WhiteBoardAction(this);

		action.setTransceiver(transceiver);
		ActionTools.sendToClient(action);
	}
}
