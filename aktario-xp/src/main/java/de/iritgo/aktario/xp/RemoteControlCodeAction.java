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

package de.iritgo.aktario.xp;


import de.iritgo.aktario.core.application.ApplicationPane;
import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.aktario.framework.base.action.FrameworkAction;
import de.iritgo.aktario.framework.base.action.FrameworkInputStream;
import de.iritgo.aktario.framework.base.action.FrameworkOutputStream;
import java.io.IOException;


/**
 * @version $Id: RemoteControlCodeAction.java,v 1.8 2006/09/25 10:34:32 grappendorf Exp $
 */
public class RemoteControlCodeAction extends FrameworkAction
{
	/** Remote control the caret. */
	public static final int CONTROL_CARET = 1;

	/** Remote control the file. */
	public static final int CONTROL_FILE = 2;

	/** Remote control the scroll area. */
	public static final int CONTROL_SCROLL = 3;

	/** Remote control the browser url. */
	public static final int CONTROL_URL = 4;

	/** Remote control the view type. */
	public static final int CONTROL_VIEW = 5;

	/** Remote control a key press. */
	public static final int CONTROL_KEY_PRESS = 6;

	/** Remote control a key release. */
	public static final int CONTROL_KEY_RELEASE = 7;

	/** Remote control a key type. */
	public static final int CONTROL_KEY_TYPE = 8;

	/** Remote control a file tree expansion. */
	public static final int CONTROL_FILE_TREE_EXPAND = 9;

	/** Remote control a file tree collaps. */
	public static final int CONTROL_FILE_TREE_COLLAPSE = 10;

	/** Remote control a file tree selection. */
	public static final int CONTROL_FILE_TREE_SELECTION = 11;

	/** Remote control a file tree selection. */
	public static final int CONTROL_WORKING = 12;

	/** The id of the user who is sending this action. */
	protected long userId;

	/** Control code. */
	protected int control;

	/** Caret position. */
	protected int dot;

	/** Caret mark position. */
	protected int mark;

	/** File name. */
	protected String fileName;

	/** First visible line. */
	protected int firstLine;

	/** First visible column. */
	protected int firstColumn;

	/** View type. */
	protected int view;

	/** Key code. */
	protected int keyCode;

	/** Key char. */
	protected char keyChar;

	/** Key modifiers. */
	protected int keyModifiers;

	/** Key modifiers. */
	protected int working;

	/**
	 * Create a new action.
	 */
	public RemoteControlCodeAction ()
	{
		setTypeId ("RCC");
	}

	/**
	 * Create a new action.
	 *
	 * @param serverAction The server action that triggers this client action.
	 */
	public RemoteControlCodeAction (RemoteControlCodeServerAction serverAction)
	{
		this ();
		this.userId = serverAction.getUserId ();
		this.control = serverAction.getControl ();
		this.dot = serverAction.getDot ();
		this.mark = serverAction.getMark ();
		this.fileName = serverAction.getFileName ();
		this.firstLine = serverAction.getFirstLine ();
		this.firstColumn = serverAction.getFirstColumn ();
		this.view = serverAction.getView ();
		this.keyCode = serverAction.getKeyCode ();
		this.keyChar = serverAction.getKeyChar ();
		this.keyModifiers = serverAction.getKeyModifiers ();
	}

	/**
	 * Read the attributes from a stream.
	 */
	public void readObject (FrameworkInputStream stream) throws IOException
	{
		userId = stream.readLong ();
		control = stream.readInt ();

		switch (control)
		{
			case CONTROL_CARET:
				dot = stream.readInt ();
				mark = stream.readInt ();

				break;

			case CONTROL_FILE:
			case CONTROL_URL:
				fileName = stream.readUTF ();

				break;

			case CONTROL_SCROLL:
				firstLine = stream.readInt ();
				firstColumn = stream.readInt ();

				break;

			case CONTROL_VIEW:
				view = stream.readInt ();

				break;

			case CONTROL_KEY_PRESS:
			case CONTROL_KEY_RELEASE:
			case CONTROL_KEY_TYPE:
				keyCode = stream.readInt ();
				keyChar = stream.readChar ();
				keyModifiers = stream.readInt ();

				break;

			case CONTROL_FILE_TREE_EXPAND:
			case CONTROL_FILE_TREE_COLLAPSE:
			case CONTROL_FILE_TREE_SELECTION:
				fileName = stream.readUTF ();

				break;

			case CONTROL_WORKING:
				working = stream.readInt ();

				break;
		}
	}

	/**
	 * Write the attributes to a stream.
	 */
	public void writeObject (FrameworkOutputStream stream) throws IOException
	{
		stream.writeLong (userId);
		stream.writeInt (control);

		switch (control)
		{
			case CONTROL_CARET:
				stream.writeInt (dot);
				stream.writeInt (mark);

				break;

			case CONTROL_FILE:
			case CONTROL_URL:
				stream.writeUTF (fileName);

				break;

			case CONTROL_SCROLL:
				stream.writeInt (firstLine);
				stream.writeInt (firstColumn);

				break;

			case CONTROL_VIEW:
				stream.writeInt (view);

				break;

			case CONTROL_KEY_PRESS:
			case CONTROL_KEY_RELEASE:
			case CONTROL_KEY_TYPE:
				stream.writeInt (keyCode);
				stream.writeChar (keyChar);
				stream.writeInt (keyModifiers);

				break;

			case CONTROL_FILE_TREE_EXPAND:
			case CONTROL_FILE_TREE_COLLAPSE:
			case CONTROL_FILE_TREE_SELECTION:
				stream.writeUTF (fileName);

				break;

			case CONTROL_WORKING:
				stream.writeInt (working);

				break;
		}
	}

	/**
	 * Perform the action.
	 */
	public void perform ()
	{
		ApplicationPane appPane = (ApplicationPane) AppContext.instance ().getObject ("applicationPane");

		if (appPane == null || ! (appPane instanceof SoftwareReviewPane))
		{
			return;
		}

		SoftwareReviewPane pane = (SoftwareReviewPane) appPane;

		if (! pane.isBasedirSelected ())
		{
			return;
		}

		if ((! pane.isFileOpen ()) && (control != CONTROL_FILE))
		{
			return;
		}

		// 		if ((pane.getWorking () > 1) && (control != CONTROL_CARET))
		// 			return;
		try
		{
			switch (control)
			{
				case CONTROL_CARET:
					pane.controlCaret (dot, mark);

					break;

				case CONTROL_FILE:
					pane.controlFile (fileName);

					break;

				case CONTROL_SCROLL:
					pane.controlScroll (firstLine, firstColumn);

					break;

				case CONTROL_URL:
					pane.controlUrl (fileName);

					break;

				case CONTROL_VIEW:
					pane.controlView (view);

					break;

				case CONTROL_KEY_PRESS:
					pane.controlKeyPress (keyCode, keyChar, keyModifiers);

					break;

				case CONTROL_KEY_RELEASE:
					pane.controlKeyRelease (keyCode, keyChar, keyModifiers);

					break;

				case CONTROL_KEY_TYPE:
					pane.controlKeyType (keyCode, keyChar, keyModifiers);

					break;

				case CONTROL_FILE_TREE_EXPAND:
					pane.controlFileTreeExpansion (fileName, true);

					break;

				case CONTROL_FILE_TREE_COLLAPSE:
					pane.controlFileTreeExpansion (fileName, false);

					break;

				case CONTROL_FILE_TREE_SELECTION:
					pane.controlFileTreeSelection (fileName);

					break;

				case CONTROL_WORKING:
					pane.setWorking (working);

					break;
			}
		}
		catch (Exception x)
		{
		}
	}
}
