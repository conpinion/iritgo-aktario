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

package de.iritgo.aktario.editor;


import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.aktario.framework.base.action.FrameworkInputStream;
import de.iritgo.aktario.framework.base.action.FrameworkOutputStream;
import de.iritgo.aktario.framework.base.action.FrameworkServerAction;
import de.iritgo.aktario.framework.server.Server;
import de.iritgo.aktario.framework.user.User;
import de.iritgo.aktario.framework.user.UserRegistry;
import java.io.IOException;
import java.util.Iterator;


/**
 * @version $Id: RemoteControlServerAction.java,v 1.5 2006/09/25 10:34:32 grappendorf Exp $
 */
public class RemoteControlServerAction extends FrameworkServerAction
{
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

	/** The selected view. */
	protected int view;

	/** Key code. */
	protected int keyCode;

	/** Key char. */
	protected char keyChar;

	/** Key modifiers. */
	protected int keyModifiers;

	/** Text. */
	protected String text;

	/**
	 * Create a new action.
	 */
	public RemoteControlServerAction()
	{
		setTypeId("EditorRCS");

		if (AppContext.instance().getUser() != null)
		{
			this.userId = AppContext.instance().getUser().getUniqueId();
		}

		fileName = "";
	}

	/**
	 * Get the user id.
	 *
	 * @return The id of the user who is sending this action.
	 */
	public long getUserId()
	{
		return userId;
	}

	/**
	 * Get the control code.
	 *
	 * @return The control code.
	 */
	public int getControl()
	{
		return control;
	}

	/**
	 * Get the caret position.
	 *
	 * @return The caret position.
	 */
	public int getDot()
	{
		return dot;
	}

	/**
	 * Get the mark position.
	 *
	 * @return The mark position.
	 */
	public int getMark()
	{
		return mark;
	}

	/**
	 * Get the file name.
	 *
	 * @return The file name.
	 */
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * Get the first visible line.
	 *
	 * @return The first visible line.
	 */
	public int getFirstLine()
	{
		return firstLine;
	}

	/**
	 * Get the first visible column.
	 *
	 * @return The first visible column.
	 */
	public int getFirstColumn()
	{
		return firstColumn;
	}

	/**
	 * Get the view type.
	 *
	 * @return The view type.
	 */
	public int getView()
	{
		return view;
	}

	/**
	 * Get the key code.
	 *
	 * @return The key code.
	 */
	public int getKeyCode()
	{
		return keyCode;
	}

	/**
	 * Get the key char.
	 *
	 * @return The key char.
	 */
	public char getKeyChar()
	{
		return keyChar;
	}

	/**
	 * Get the key modifiers.
	 *
	 * @return The key modifiers.
	 */
	public int getKeyModifiers()
	{
		return keyModifiers;
	}

	/**
	 * Get the text.
	 *
	 * @return The text.
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * Send a clear all text to other clients.
	 */
	public void controlTextClear()
	{
		control = RemoteControlAction.CONTROL_TEXT_CLEAR;
	}

	/**
	 * Send a text to insert.
	 *
	 * @param text The text to insert.
	 */
	public void controlTextInsert(String text)
	{
		control = RemoteControlAction.CONTROL_TEXT_INSERT;
		this.text = text;
	}

	/**
	 * Control the caret.
	 *
	 * @param dot Caret position.
	 * @param mark Caret mark position.
	 */
	public void controlCaret(int dot, int mark)
	{
		control = RemoteControlAction.CONTROL_CARET;
		this.dot = dot;
		this.mark = mark;
	}

	/**
	 * Control the file.
	 *
	 * @param fileName The name of the file to display.
	 */
	public void controlFile(String fileName)
	{
		control = RemoteControlAction.CONTROL_FILE;
		this.fileName = fileName;
	}

	/**
	 * Control the file name.
	 *
	 * @param fileName The file name.
	 */
	public void controlFileName(String fileName)
	{
		control = RemoteControlAction.CONTROL_FILE_NAME;
		this.fileName = fileName;
	}

	/**
	 * Control the scroll area.
	 *
	 * @param firstLine The first visible line.
	 * @param firstColumn The first visible line.
	 */
	public void controlScroll(int firstLine, int firstColumn)
	{
		control = RemoteControlAction.CONTROL_SCROLL;
		this.firstLine = firstLine;
		this.firstColumn = firstColumn;
	}

	/**
	 * Control the browser url.
	 *
	 * @param url The new url.
	 */
	public void controlUrl(String url)
	{
		control = RemoteControlAction.CONTROL_URL;
		this.fileName = url;
	}

	/**
	 * Control the view type.
	 *
	 * @param view The view type.
	 */
	public void controlView(int view)
	{
		control = RemoteControlAction.CONTROL_VIEW;
		this.view = view;
	}

	/**
	 * Control a key press.
	 *
	 * @param keyCode The key code.
	 * @param keyChar The key char.
	 * @param keyModifiers The modifiers.
	 */
	public void controlKeyPress(int keyCode, char keyChar, int keyModifiers)
	{
		control = RemoteControlAction.CONTROL_KEY_PRESS;
		this.keyCode = keyCode;
		this.keyChar = keyChar;
		this.keyModifiers = keyModifiers;
	}

	/**
	 * Control a key release.
	 *
	 * @param keyCode The key code.
	 * @param keyChar The key char.
	 * @param keyModifiers The modifiers.
	 */
	public void controlKeyRelease(int keyCode, char keyChar, int keyModifiers)
	{
		control = RemoteControlAction.CONTROL_KEY_RELEASE;
		this.keyCode = keyCode;
		this.keyChar = keyChar;
		this.keyModifiers = keyModifiers;
	}

	/**
	 * Control a key type.
	 *
	 * @param keyCode The key code.
	 * @param keyChar The key char.
	 * @param keyModifiers The modifiers.
	 */
	public void controlKeyType(int keyCode, char keyChar, int keyModifiers)
	{
		control = RemoteControlAction.CONTROL_KEY_TYPE;
		this.keyCode = keyCode;
		this.keyChar = keyChar;
		this.keyModifiers = keyModifiers;
	}

	/**
	 * Control a file tree expansion.
	 *
	 * @param fileName The relative path name.
	 * @param expanded True for an expanded, false for a collapsed path.
	 */
	public void controlFileTreeExpansion(String fileName, boolean expanded)
	{
		control = expanded ? RemoteControlAction.CONTROL_FILE_TREE_EXPAND
						: RemoteControlAction.CONTROL_FILE_TREE_COLLAPSE;
		this.fileName = (fileName != null ? fileName : "");
	}

	/**
	 * Control a file tree selection.
	 *
	 * @param fileName The relative path name.
	 */
	public void controlFileTreeSelection(String fileName)
	{
		control = RemoteControlAction.CONTROL_FILE_TREE_SELECTION;
		this.fileName = (fileName != null ? fileName : "");
	}

	/**
	 * Read the attributes from a stream.
	 */
	@Override
	public void readObject(FrameworkInputStream stream) throws IOException
	{
		userId = stream.readLong();
		control = stream.readInt();

		switch (control)
		{
			case RemoteControlAction.CONTROL_TEXT_CLEAR:
				break;

			case RemoteControlAction.CONTROL_TEXT_INSERT:
				text = stream.readUTF();

				break;

			case RemoteControlAction.CONTROL_CARET:
				dot = stream.readInt();
				mark = stream.readInt();

				break;

			case RemoteControlAction.CONTROL_FILE:
			case RemoteControlAction.CONTROL_FILE_NAME:
			case RemoteControlAction.CONTROL_URL:
				fileName = stream.readUTF();

				break;

			case RemoteControlAction.CONTROL_SCROLL:
				firstLine = stream.readInt();
				firstColumn = stream.readInt();

				break;

			case RemoteControlAction.CONTROL_VIEW:
				view = stream.readInt();

				break;

			case RemoteControlAction.CONTROL_KEY_PRESS:
			case RemoteControlAction.CONTROL_KEY_RELEASE:
			case RemoteControlAction.CONTROL_KEY_TYPE:
				keyCode = stream.readInt();
				keyChar = stream.readChar();
				keyModifiers = stream.readInt();

				break;

			case RemoteControlAction.CONTROL_FILE_TREE_EXPAND:
			case RemoteControlAction.CONTROL_FILE_TREE_COLLAPSE:
			case RemoteControlAction.CONTROL_FILE_TREE_SELECTION:
				fileName = stream.readUTF();

				break;
		}
	}

	/**
	 * Write the attributes to a stream.
	 */
	@Override
	public void writeObject(FrameworkOutputStream stream) throws IOException
	{
		stream.writeLong(userId);
		stream.writeInt(control);

		switch (control)
		{
			case RemoteControlAction.CONTROL_TEXT_CLEAR:
				break;

			case RemoteControlAction.CONTROL_TEXT_INSERT:
				stream.writeUTF(text);

				break;

			case RemoteControlAction.CONTROL_CARET:
				stream.writeInt(dot);
				stream.writeInt(mark);

				break;

			case RemoteControlAction.CONTROL_FILE:
			case RemoteControlAction.CONTROL_FILE_NAME:
			case RemoteControlAction.CONTROL_URL:
				stream.writeUTF(fileName);

				break;

			case RemoteControlAction.CONTROL_SCROLL:
				stream.writeInt(firstLine);
				stream.writeInt(firstColumn);

				break;

			case RemoteControlAction.CONTROL_VIEW:
				stream.writeInt(view);

				break;

			case RemoteControlAction.CONTROL_KEY_PRESS:
			case RemoteControlAction.CONTROL_KEY_RELEASE:
			case RemoteControlAction.CONTROL_KEY_TYPE:
				stream.writeInt(keyCode);
				stream.writeChar(keyChar);
				stream.writeInt(keyModifiers);

				break;

			case RemoteControlAction.CONTROL_FILE_TREE_EXPAND:
			case RemoteControlAction.CONTROL_FILE_TREE_COLLAPSE:
			case RemoteControlAction.CONTROL_FILE_TREE_SELECTION:
				stream.writeUTF(fileName);

				break;
		}
	}

	/**
	 * Perform the action.
	 */
	@Override
	public void perform()
	{
		ClientTransceiver ct = (ClientTransceiver) transceiver;
		UserRegistry userRegistry = Server.instance().getUserRegistry();

		for (Iterator i = userRegistry.userIterator(); i.hasNext();)
		{
			User user = (User) i.next();

			if (user.isOnline() && user.getUniqueId() != userId)
			{
				ct.addReceiver(user.getNetworkChannel());
			}
		}

		RemoteControlAction action = new RemoteControlAction(this);

		action.setTransceiver(transceiver);
		ActionTools.sendToClient(action);
	}
}
