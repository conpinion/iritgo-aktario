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


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.application.ApplicationPane;
import de.iritgo.aktario.core.filebrowser.FileBrowser;
import de.iritgo.aktario.core.filebrowser.FileNode;
import de.iritgo.aktario.core.gui.GUIPane;
import de.iritgo.aktario.core.gui.IButton;
import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.core.user.AktarioUserReadyServerAction;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.appcontext.AppContext;
import de.iritgo.simplelife.math.NumberTools;
import de.iritgo.simplelife.string.StringTools;
import org.swixml.SwingEngine;
import org.swixml.SwingTagLibrary;
import org.syntax.jedit.DefaultInputHandler;
import org.syntax.jedit.SyntaxStyle;
import org.syntax.jedit.tokenmarker.HTMLTokenMarker;
import org.syntax.jedit.tokenmarker.JavaTokenMarker;
import org.syntax.jedit.tokenmarker.PropsTokenMarker;
import org.syntax.jedit.tokenmarker.TSQLTokenMarker;
import org.syntax.jedit.tokenmarker.Token;
import org.syntax.jedit.tokenmarker.TokenMarker;
import org.syntax.jedit.tokenmarker.XMLTokenMarker;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


/**
 * EditorPane
 *
 * @version $Id: EditorPane.java,v 1.10 2006/10/05 15:00:42 grappendorf Exp $
 */
public class EditorPane extends ApplicationPane
{
	/** Editor view index. */
	protected static final int VIEW_EDITOR = 0;

	/** File browser index. */
	protected static final int VIEW_FILE_BROWSER = 1;

	/** Web browser index. */
	protected static final int VIEW_WEB_BROWSER = 2;

	/** Source code display. */
	public CodeEditor code;

	/** Open text button. */
	public IButton btnBaseDir;

	/** File name display. */
	public JTextField fileNameDisplay;

	/** View panel. */
	public JPanel viewPanel;

	/** Url display. */
	public JTextField urlDisplay;

	/** HTML panel. */
	public JPanel htmlPanel;

	/** HTML display. */

	// 	public JEditorPane html;
	// 	public WebBrowser html;

	/** File extension to sytax highlight map. */
	protected Map<String, TokenMarker> tokenMarkerByExtension;

	/** The base directory. */
	protected File baseDir;

	/** The current file. */
	protected File currentFile;

	/** True if a base dir was selected. */
	protected boolean baseDirSelected;

	/** If true, event listeners will ignore all events. */
	protected boolean ignoreEvents;

	/** The code file browser. */
	public FileBrowser fileBrowser;

	/** Regular expression mathing an import line. */
	public Pattern reJavaImport;

	/** Wait for basedir selecten */
	public boolean basedirSelected;

	/** Wait for basedir selecten */
	public boolean isFileOpen;

	/**
	 * Select the base directory.
	 */
	public Action baseDirAction = new AbstractAction()
	{
		/** */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e)
		{
			showBaseDirChooser();
		}
	};

	/**
	 * Browse to a new url.
	 */
	public Action urlAction = new AbstractAction()
	{
		/** */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e)
		{
			controlUrl(urlDisplay.getText());
			remoteControlUrl(urlDisplay.getText());
		}
	};

	/**
	 * Browse to a new url.
	 */
	public Action viewAction = new AbstractAction()
	{
		/** */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e)
		{
			int viewType = NumberTools.toInt(((Component) e.getSource()).getName(), 0);

			controlView(viewType);
			remoteControlView(viewType);
		}
	};

	/**
	 * Create a new EditorPane.
	 */
	public EditorPane()
	{
		super("EditorPane");

		tokenMarkerByExtension = new HashMap<String, TokenMarker>();
		tokenMarkerByExtension.put("java", new JavaTokenMarker());
		tokenMarkerByExtension.put("xml", new XMLTokenMarker());
		tokenMarkerByExtension.put("jsp", new XMLTokenMarker());
		tokenMarkerByExtension.put("html", new HTMLTokenMarker());
		tokenMarkerByExtension.put("properties", new PropsTokenMarker());
		tokenMarkerByExtension.put("sql", new TSQLTokenMarker());

		try
		{
			reJavaImport = Pattern.compile("import\\s+(\\w+(\\.\\w+)*)\\s*;");
		}
		catch (PatternSyntaxException x)
		{
			Log.logError("client", "EditorPane", x.toString());
		}
	}

	/**
	 * Initialize the gui.
	 */
	@Override
	public void initGUI()
	{
		super.initGUI();

		AktarioUserReadyServerAction aktarioUserReadyServerAction = new AktarioUserReadyServerAction(AppContext
						.instance().getUser(), false);

		ActionTools.sendToServer(aktarioUserReadyServerAction);

		try
		{
			SwingTagLibrary.getInstance().registerTag("jedit", CodeEditor.class);

			SwingEngine swingEngine = new SwingEngine(this);

			swingEngine.setClassLoader(EditorPane.class.getClassLoader());

			JPanel panel = (JPanel) swingEngine.render(getClass().getResource("/swixml/EditorPane.xml"));

			code.addCaretListener(new CaretListener()
			{
				public void caretUpdate(CaretEvent e)
				{
					if (! ignoreEvents)
					{
						remoteControlCaret(e.getDot(), e.getMark());
					}
				}
			});

			code.addHorizontalAdjustmentListener(new AdjustmentListener()
			{
				public void adjustmentValueChanged(AdjustmentEvent e)
				{
					if (! ignoreEvents && ! e.getValueIsAdjusting())
					{
						remoteControlScroll(code.getFirstVisibleLine(), code.getFirstVisibleColumn());
					}
				}
			});

			code.addVerticalAdjustmentListener(new AdjustmentListener()
			{
				public void adjustmentValueChanged(AdjustmentEvent e)
				{
					if (! ignoreEvents && ! e.getValueIsAdjusting())
					{
						remoteControlScroll(code.getFirstVisibleLine(), code.getFirstVisibleColumn());
					}
				}
			});

			DefaultInputHandler codeInputHandler = new DefaultInputHandler()
			{
				@Override
				public void keyTyped(KeyEvent e)
				{
					if (! ignoreEvents)
					{
						remoteControlKeyType(e.getKeyCode(), e.getKeyChar(), e.getModifiers());
					}

					super.keyTyped(e);
				}

				@Override
				public void keyPressed(KeyEvent e)
				{
					if (! ignoreEvents)
					{
						remoteControlKeyPress(e.getKeyCode(), e.getKeyChar(), e.getModifiers());
					}

					super.keyPressed(e);
				}

				@Override
				public void keyReleased(KeyEvent e)
				{
					if (! ignoreEvents)
					{
						remoteControlKeyRelease(e.getKeyCode(), e.getKeyChar(), e.getModifiers());
					}

					super.keyReleased(e);
				}
			};

			codeInputHandler.addDefaultKeyBindings();
			code.setInputHandler(codeInputHandler);

			SyntaxStyle[] styles = new SyntaxStyle[Token.ID_COUNT];

			styles[Token.COMMENT1] = new SyntaxStyle(new Color(0x3F7F5F), true, false);
			styles[Token.COMMENT2] = new SyntaxStyle(new Color(0x3F7F5F), true, false);
			styles[Token.KEYWORD1] = new SyntaxStyle(new Color(0x7F0055), false, true);
			styles[Token.KEYWORD2] = new SyntaxStyle(new Color(0x7F0055), false, true);
			styles[Token.KEYWORD3] = new SyntaxStyle(new Color(0x7F0055), false, true);
			styles[Token.LITERAL1] = new SyntaxStyle(new Color(0x2A00FF), false, false);
			styles[Token.LITERAL2] = new SyntaxStyle(new Color(0x2A00FF), false, true);
			styles[Token.LABEL] = new SyntaxStyle(new Color(0x990033), false, true);
			styles[Token.OPERATOR] = new SyntaxStyle(Color.black, false, true);
			styles[Token.INVALID] = new SyntaxStyle(Color.red, false, true);

			code.getPainter().setStyles(styles);

			// 			html = new WebBrowser();
			// 			htmlPanel.add (html);

			// 			html.addHyperlinkListener (
			// 				new HyperlinkListener()
			// 				{
			// 					public void hyperlinkUpdate (HyperlinkEvent e)
			// 					{
			// 						if (e.getEventType () == HyperlinkEvent.EventType.ACTIVATED)
			// 						{
			// 							controlUrl (e.getURL ().toString ());
			// 							remoteControlUrl (e.getURL ().toString ());
			// 						}
			// 					}
			// 				});
			fileBrowser.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseClicked(MouseEvent e)
				{
					if (e.getClickCount() == 2)
					{
						if (fileBrowser.getSelectionPath() == null
										|| (FileNode) fileBrowser.getSelectionPath().getLastPathComponent() == null)
						{
							return;
						}

						File file = ((FileNode) fileBrowser.getSelectionPath().getLastPathComponent()).getFile();

						if (! file.isFile())
						{
							return;
						}

						String fileName = createRelativeFilePath(file);

						controlView(VIEW_EDITOR);
						remoteControlView(VIEW_EDITOR);

						String text = controlFile(fileName);

						remoteControlFile(fileName, text);
						controlCaret(0, 0);
						remoteControlCaret(0, 0);
					}
				}
			});

			fileBrowser.addTreeExpansionListener(new TreeExpansionListener()
			{
				public void treeExpanded(TreeExpansionEvent e)
				{
					if (! ignoreEvents)
					{
						remoteControlFileTreeExpansion(((FileNode) e.getPath().getLastPathComponent()).getFile(), true);
					}
				}

				public void treeCollapsed(TreeExpansionEvent e)
				{
					if (! ignoreEvents)
					{
						remoteControlFileTreeExpansion(((FileNode) e.getPath().getLastPathComponent()).getFile(), false);
					}
				}
			});

			fileBrowser.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener()
			{
				public void valueChanged(TreeSelectionEvent e)
				{
					if (! ignoreEvents)
					{
						File file = null;

						if (fileBrowser.getSelectionPath() != null)
						{
							file = ((FileNode) fileBrowser.getSelectionPath().getLastPathComponent()).getFile();
						}

						remoteControlFileTreeSelection(file);
					}
				}
			});

			content.add(panel, createConstraints(0, 0, 1, 1, GridBagConstraints.BOTH, 100, 100, null));

			// 			CommandTools.performAsync (new BaseDirCommand(this));
		}
		catch (Exception x)
		{
			Log.logError("client", "EditorPane", x.toString());
		}
	}

	/**
	 * Load the gui values from the data object attributes.
	 */
	@Override
	public void loadFromObject(IObject iobject)
	{
		// 		EditorData data = (EditorData) iobject;
		// 		setTitle (lesson.getTitle ());
		// 		if (! baseDirSelected)
		// 		{
		// 			baseDirSelected = true;
		// 			SwingUtilities.invokeLater (
		// 				new Runnable()
		// 				{
		// 					public void run ()
		// 					{
		// 						baseDirAction.actionPerformed (null);
		// 						sendUserReadyAction ();
		// 					}
		// 				});
		// 		}
	}

	/**
	 * Store the current gui values into the data object attributes.
	 */
	@Override
	public void storeToObject(IObject iobject)
	{
	}

	/**
	 * Return a sample of the data object that is displayed in this gui pane.
	 *
	 * @return The sample oject.
	 */
	@Override
	public IObject getSampleObject()
	{
		return new EditorData();
	}

	/**
	 * Return a clone of this gui pane.
	 *
	 * @return The gui pane clone.
	 */
	@Override
	public GUIPane cloneGUIPane()
	{
		return new EditorPane();
	}

	/**
	 * Remote control the caret position.
	 *
	 * @param dot Caret position.
	 * @param mark Caret mark position.
	 */
	protected void remoteControlCaret(int dot, int mark)
	{
		RemoteControlServerAction action = new RemoteControlServerAction();

		action.controlCaret(dot, mark);
		sendRemoteControlAction(action);
	}

	/**
	 * Remote control the scroll area.
	 *
	 * @param firstLine The first visible line.
	 * @param firstColumn The first visible line.
	 */
	protected void remoteControlScroll(int firstLine, int firstColumn)
	{
		RemoteControlServerAction action = new RemoteControlServerAction();

		action.controlScroll(firstLine, firstColumn);
		sendRemoteControlAction(action);
	}

	/**
	 * Remote control the view type.
	 *
	 * @param view The view type.
	 */
	protected void remoteControlView(int view)
	{
		RemoteControlServerAction action = new RemoteControlServerAction();

		action.controlView(view);
		sendRemoteControlAction(action);
	}

	/**
	 * Remote control the url.
	 *
	 * @param url The new url.
	 */
	protected void remoteControlUrl(String url)
	{
		RemoteControlServerAction action = new RemoteControlServerAction();

		action.controlUrl(url);
		sendRemoteControlAction(action);
	}

	/**
	 * Remote control a key press.
	 *
	 * @param keyCode The key code.
	 * @param keyChar The key char.
	 * @param keyModifiers The key modifiers.
	 */
	public void remoteControlKeyPress(int keyCode, char keyChar, int keyModifiers)
	{
		RemoteControlServerAction action = new RemoteControlServerAction();

		action.controlKeyPress(keyCode, keyChar, keyModifiers);
		sendRemoteControlAction(action);
	}

	/**
	 * Remote control a key release.
	 *
	 * @param keyCode The key code.
	 * @param keyChar The key char.
	 * @param keyModifiers The key modifiers.
	 */
	public void remoteControlKeyRelease(int keyCode, char keyChar, int keyModifiers)
	{
		RemoteControlServerAction action = new RemoteControlServerAction();

		action.controlKeyRelease(keyCode, keyChar, keyModifiers);
		sendRemoteControlAction(action);
	}

	/**
	 * Remote control a key type.
	 *
	 * @param keyCode The key code.
	 * @param keyChar The key char.
	 * @param keyModifiers The key modifiers.
	 */
	public void remoteControlKeyType(int keyCode, char keyChar, int keyModifiers)
	{
		RemoteControlServerAction action = new RemoteControlServerAction();

		action.controlKeyType(keyCode, keyChar, keyModifiers);
		sendRemoteControlAction(action);
	}

	/**
	 * Remote control the file tree expansion.
	 *
	 * @param file The expanded or collapsed file node.
	 * @param expanded True for an expanded, false for a collapsed path.
	 */
	public void remoteControlFileTreeExpansion(File file, boolean expanded)
	{
		RemoteControlServerAction action = new RemoteControlServerAction();

		action.controlFileTreeExpansion(createRelativeFilePath(file), expanded);
		sendRemoteControlAction(action);
	}

	/**
	 * Remote control the file tree selection.
	 *
	 * @param file The expanded file node.
	 */
	public void remoteControlFileTreeSelection(File file)
	{
		RemoteControlServerAction action = new RemoteControlServerAction();

		action.controlFileTreeSelection(createRelativeFilePath(file));
		sendRemoteControlAction(action);
	}

	/**
	 * Remote control the file.
	 *
	 * @param fileName The name of the new file.
	 * @param text The file contents.
	 */
	protected void remoteControlFile(String fileName, String text)
	{
		RemoteControlServerAction action = new RemoteControlServerAction();

		action.controlTextClear();
		sendRemoteControlAction(action);

		action = new RemoteControlServerAction();
		action.controlFileName(fileName);
		sendRemoteControlAction(action);

		action = new RemoteControlServerAction();

		StringTokenizer st = new StringTokenizer(text, "\n", true);
		String nextToken = null;

		if (st.hasMoreTokens())
		{
			nextToken = st.nextToken();
		}

		while (nextToken != null)
		{
			String nextToken2 = null;

			if (st.hasMoreTokens())
			{
				nextToken2 = st.nextToken();
			}

			String val = nextToken;

			if (nextToken2 != null && nextToken2.equals("\n"))
			{
				val += "\n";

				if (st.hasMoreTokens())
				{
					nextToken2 = st.nextToken();
				}
			}

			action.controlTextInsert(val);
			sendRemoteControlAction(action);
			nextToken = nextToken2;
		}
	}

	/**
	 * Send a remote control action.
	 *
	 * @param action The action to send.
	 */
	protected void sendRemoteControlAction(RemoteControlServerAction action)
	{
		ClientTransceiver transceiver = new ClientTransceiver(AppContext.instance().getChannelNumber());

		transceiver.addReceiver(AppContext.instance().getChannelNumber());
		action.setTransceiver(transceiver);
		ActionTools.sendToServer(action);
	}

	/**
	 * Clear the whole text.
	 */
	public void controlTextClear()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				ignoreEvents = true;
				code.setText("");
				ignoreEvents = false;
			}
		});
	}

	/**
	 * Insert text.
	 *
	 * @param text The text to insert.
	 */
	public void controlTextInsert(final String text)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				ignoreEvents = true;
				code.appendText(text);
				ignoreEvents = false;
			}
		});
	}

	/**
	 * Set the caret and mark position.
	 *
	 * @param dot The caret position.
	 * @param mark The mark position.
	 */
	public void controlCaret(final int dot, final int mark)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				ignoreEvents = true;
				code.select(dot, mark);
				ignoreEvents = false;
			}
		});
	}

	/**
	 * Set the first visible line and column.
	 *
	 * @param firstLine The first visible line.
	 * @param firstColumn The first visible line.
	 */
	public void controlScroll(int firstLine, int firstColumn)
	{
		//		final int fl = firstLine;
		final int fc = firstColumn;

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				ignoreEvents = true;
				//					code.setVerticalOffset (fl);
				code.setHorizontalOffset(- fc);
				ignoreEvents = false;
			}
		});
	}

	/**
	 * Open a file.
	 *
	 * @param fileName The name of the file to open (realitve to the base directory).
	 */
	public String controlFile(String fileName)
	{
		try
		{
			currentFile = new File(baseDir, fileName);

			BufferedReader in = new BufferedReader(new FileReader(currentFile));
			final StringBuffer content = new StringBuffer();
			String line = null;

			while ((line = in.readLine()) != null)
			{
				content.append(line + "\n");
			}

			String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
			final TokenMarker tokenMarker = (TokenMarker) tokenMarkerByExtension.get(extension);

			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					ignoreEvents = true;
					code.setTokenMarker(tokenMarker);
					code.setText(content.toString());
					fileNameDisplay.setText(currentFile.getAbsolutePath());
					ignoreEvents = false;
					isFileOpen = true;
				}
			});

			return content.toString();
		}
		catch (IOException x)
		{
			code.setTokenMarker(null);
			code.setText("Unable to open file: " + fileName);
		}

		return "";
	}

	/**
	 * Set the file name.
	 *
	 * @param fileName The new file name.
	 */
	public void controlFileName(String fileName)
	{
		String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
		final TokenMarker tokenMarker = (TokenMarker) tokenMarkerByExtension.get(extension);

		if (tokenMarker != null)
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					ignoreEvents = true;
					code.setTokenMarker(tokenMarker);
					ignoreEvents = false;
				}
			});
		}
	}

	/**
	 * Open an url.
	 *
	 * @param url The name of the url to browse.
	 */
	public void controlUrl(String url)
	{
		if (! url.startsWith("http://") && ! url.startsWith("https://"))
		{
			url = "http://" + url;
		}

		try
		{
			// 			if (html != null)
			// 			{
			// 				html.setURL (new URL(url));
			// 			}
		}
		catch (Exception x)
		{
			// 			html.setURL ();
		}

		// 		SwingUtilities.invokeLater (
		// 			new Runnable()
		// 			{
		// 				public void run ()
		// 				{
		// 					ignoreEvents = true;
		// 					try
		// 					{
		// 						String urlSpec = url;
		// 						html.getDocument ().putProperty (Document.StreamDescriptionProperty, null);
		// 						if (! urlSpec.startsWith ("http://") && ! urlSpec.startsWith ("https://"))
		// 						{
		// 							urlSpec = "http://" + urlSpec;
		// 						}
		// 						URL aUrl = new URL(urlSpec);
		// 						URLConnection urlConnection = aUrl.openConnection ();
		// 						if ("text/html".equals (urlConnection.getContentType ()))
		// 						{
		// 							html.setPage (aUrl);
		// 						}
		// 						else
		// 						{
		// 							html.setContentType ("text/html");
		// 							html.setText (
		// 								"<html><body><img src=\"" + urlSpec + "\"></body></html>");
		// 						}
		// 					}
		// 					catch (Exception x)
		// 					{
		// 						try
		// 						{
		// 							html.setText (x.toString ());
		// 						}
		// 						catch (Exception xx)
		// 						{
		// 						}
		// 					}
		// 					urlDisplay.setText (url);
		// 					ignoreEvents = false;
		// 				}
		// 			});
	}

	/**
	 * Select a view.
	 *
	 * @param view The view to select.
	 */
	public void controlView(final int view)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				ignoreEvents = true;
				((CardLayout) (viewPanel.getLayout())).show(viewPanel, String.valueOf(view));
				ignoreEvents = false;
			}
		});
	}

	/**
	 * Press a key.
	 *
	 * @param keyCode The key code.
	 * @param keyChar The key char.
	 * @param keyModifiers The key modifiers.
	 */
	public void controlKeyPress(final int keyCode, final char keyChar, final int keyModifiers)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				ignoreEvents = true;
				code.getInputHandler().keyPressed(
								new KeyEvent(code, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), keyModifiers,
												keyCode, keyChar));
				ignoreEvents = false;
			}
		});
	}

	/**
	 * Release a key.
	 *
	 * @param keyCode The key code.
	 * @param keyChar The key char.
	 * @param keyModifiers The key modifiers.
	 */
	public void controlKeyRelease(final int keyCode, final char keyChar, final int keyModifiers)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				ignoreEvents = true;
				code.getInputHandler().keyReleased(
								new KeyEvent(code, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), keyModifiers,
												keyCode, keyChar));
				ignoreEvents = false;
			}
		});
	}

	/**
	 * Type a key.
	 *
	 * @param keyCode The key code.
	 * @param keyChar The key char.
	 * @param keyModifiers The key modifiers.
	 */
	public void controlKeyType(final int keyCode, final char keyChar, final int keyModifiers)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				ignoreEvents = true;
				code.getInputHandler().keyTyped(
								new KeyEvent(code, KeyEvent.KEY_TYPED, System.currentTimeMillis(), keyModifiers,
												keyCode, keyChar));
				ignoreEvents = false;
			}
		});
	}

	/**
	 * Expand collapse a file tree node.
	 *
	 * @param fileName The path name of the expanded or collapsed file node.
	 * @param expanded True if the node is expanded.
	 */
	public void controlFileTreeExpansion(final String fileName, final boolean expanded)
	{
		if (StringTools.isEmpty(fileName))
		{
			return;
		}

		File file = new File(baseDir + Engine.instance().getFileSeparator() + fileName);
		final TreePath path = ((FileNode) fileBrowser.getModel().getRoot()).findTreePath(file);

		if (path == null)
		{
			return;
		}

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				ignoreEvents = true;

				if (expanded)
				{
					fileBrowser.expandPath(path);
				}
				else
				{
					fileBrowser.collapsePath(path);
				}

				ignoreEvents = false;
			}
		});
	}

	/**
	 * Select a file tree node.
	 *
	 * @param fileName The path name of the selected node.
	 */
	public void controlFileTreeSelection(final String fileName)
	{
		File file = new File(baseDir, fileName);
		final TreePath path = ((FileNode) fileBrowser.getModel().getRoot()).findTreePath(file);

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				ignoreEvents = true;

				if (path != null)
				{
					fileBrowser.setSelectionPath(path);
					fileBrowser.scrollPathToVisible(path);
				}
				else
				{
					fileBrowser.clearSelection();
				}

				ignoreEvents = false;
			}
		});
	}

	private void showBaseDirChooser()
	{
		AktarioUserReadyServerAction aktarioUserReadyServerAction = new AktarioUserReadyServerAction(AppContext
						.instance().getUser(), false);

		ActionTools.sendToServer(aktarioUserReadyServerAction);

		JFileChooser chooser = new JFileChooser();

		chooser.setDialogTitle(Engine.instance().getResourceService().getString("chooseBaseDir"));

		if (baseDir != null)
		{
			chooser.setCurrentDirectory(baseDir);
		}

		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int result = chooser.showOpenDialog(content.getTopLevelAncestor());

		if (result == JFileChooser.APPROVE_OPTION)
		{
			baseDir = chooser.getSelectedFile();
			fileNameDisplay.setText(baseDir.getAbsolutePath());
			fileBrowser.setRoot(baseDir);

			basedirSelected = true;

			aktarioUserReadyServerAction = new AktarioUserReadyServerAction(AppContext.instance().getUser(), true);

			ActionTools.sendToServer(aktarioUserReadyServerAction);
		}
	}

	public boolean isBasedirSelected()
	{
		return basedirSelected;
	}

	public void setBasedirSelected(boolean value)
	{
		basedirSelected = value;
	}

	public boolean isFileOpen()
	{
		return isFileOpen;
	}

	/**
	 * Context help lookup.
	 */
	@Override
	public void contextHelp()
	{
		String line = code.getLineText(code.getMarkLine());
		Matcher matcher = reJavaImport.matcher(line);

		if (matcher.matches())
		{
			String url = "http://java.sun.com/j2se/1.4.2/docs/api/" + matcher.group(1).replace('.', '/') + ".html";

			controlView(VIEW_WEB_BROWSER);
			remoteControlView(VIEW_WEB_BROWSER);
			controlUrl(url);
			remoteControlUrl(url);
		}
	}

	/**
	 * Calculate a relative file name from the given file and the
	 * base directory. Null is returned if file is not a child of the base directory.
	 *
	 * @param file The file to convert.
	 * @return The relative file path.
	 */
	protected String createRelativeFilePath(File file)
	{
		if (file == null)
		{
			return null;
		}

		String pathName = file.getAbsolutePath();
		String basePathName = baseDir.getAbsolutePath();

		if (! pathName.startsWith(basePathName))
		{
			return null;
		}

		if (pathName.equals(basePathName))
		{
			return null;
		}

		return pathName.substring(basePathName.length() + 1).replace('\\', '/');
	}

	public JPanel getContent()
	{
		return content;
	}

	public File getBaseDir()
	{
		return baseDir;
	}

	public void setBaseDir(File baseDir)
	{
		this.baseDir = baseDir;
	}

	public FileBrowser getFileBrowser()
	{
		return fileBrowser;
	}

	public void setFileBrowser(FileBrowser fileBrowser)
	{
		this.fileBrowser = fileBrowser;
	}

	public JTextField getFileNameDisplay()
	{
		return fileNameDisplay;
	}

	public void setFileNameDisplay(JTextField fileNameDisplay)
	{
		this.fileNameDisplay = fileNameDisplay;
	}
}
