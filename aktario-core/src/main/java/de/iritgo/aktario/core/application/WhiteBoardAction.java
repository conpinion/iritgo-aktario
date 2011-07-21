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


import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.aktario.framework.base.action.FrameworkAction;
import de.iritgo.aktario.framework.base.action.FrameworkInputStream;
import de.iritgo.aktario.framework.base.action.FrameworkOutputStream;
import java.io.IOException;


/**
 * WhiteBoardAction
 *
 * @version $Id: WhiteBoardAction.java,v 1.9 2006/09/25 10:34:32 grappendorf Exp $
 */
public class WhiteBoardAction extends FrameworkAction
{
	/** Action type for mouse moves. */
	public static final int ACTION_POINTER = 1;

	/** Action type for mouse moves. */
	public static final int ACTION_PAINT = 2;

	/** Paint exclamation icon. */
	public static final int PAINT_ERASE = 0;

	/** Paint exclamation icon. */
	public static final int PAINT_EXCLAMATION = 1;

	/** Paint info icon. */
	public static final int PAINT_INFO = 2;

	/** Paint ok icon. */
	public static final int PAINT_OK = 3;

	/** Paint question icon. */
	public static final int PAINT_QUESTION = 4;

	/** Number of paints. */
	public static final int NUM_PAINTS = 5;

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
	public WhiteBoardAction ()
	{
		setTypeId ("WB");
	}

	/**
	 * Create a new action.
	 *
	 * @param action The server action.
	 */
	public WhiteBoardAction (WhiteBoardServerAction action)
	{
		this ();
		this.userId = action.getUserId ();
		this.pointerId = action.getPointerId ();
		this.type = action.getType ();
		this.x = action.getX ();
		this.y = action.getY ();
		this.paint = action.getPaint ();
	}

	/**
	 * Read the attributes from a stream.
	 */
	@Override
	public void readObject (FrameworkInputStream stream) throws IOException
	{
		userId = stream.readLong ();
		pointerId = stream.readInt ();
		type = stream.readInt ();
		x = stream.readInt ();
		y = stream.readInt ();
		paint = stream.readInt ();
	}

	/**
	 * Write the attributes to a stream.
	 */
	@Override
	public void writeObject (FrameworkOutputStream stream) throws IOException
	{
		stream.writeLong (userId);
		stream.writeInt (pointerId);
		stream.writeInt (type);
		stream.writeInt (x);
		stream.writeInt (y);
		stream.writeInt (paint);
	}

	/**
	 * Perform the action.
	 */
	@Override
	public void perform ()
	{
		ApplicationPane appPane = (ApplicationPane) AppContext.instance ().getObject ("applicationPane");

		if (appPane == null)
		{
			return;
		}

		ApplicationGlassPane glassPane = appPane.getGlassPane ();

		switch (type)
		{
			case ACTION_POINTER:
				glassPane.movePointer (pointerId, x, y);

				break;

			case ACTION_PAINT:
				glassPane.paint (x, y, paint);

				break;
		}
	}
}
