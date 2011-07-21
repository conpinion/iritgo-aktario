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

// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3)

package bsh;


import java.io.PrintStream;
import java.io.Reader;


// Referenced classes of package bsh:
//            Interpreter, CallStack, SimpleNode, InterpreterError,
//            ReturnControl, ParseException, EvalError, TargetError,
//            TokenMgrError, JJTParserState, Parser, NameSpace,
//            JavaCharStream
public class AgentInterpreter extends Interpreter
{
	/** */
	private static final long serialVersionUID = 1L;

	private AgentInterpreter localInterpreter;

	private CallStack callstack;

	private Object retVal;

	private SimpleNode node;

	private boolean eof;

	public AgentInterpreter ()
	{
		eof = false;
	}

	public AgentInterpreter (Reader reader, PrintStream printstream, PrintStream printstream1, boolean flag,
					NameSpace namespace, Interpreter interpreter, String s)
	{
		super (reader, printstream, printstream1, flag, namespace, interpreter, s);
		eof = false;
	}

	public void init (Reader reader, NameSpace namespace, String s)
	{
		retVal = null;
		localInterpreter = new AgentInterpreter (reader, out, err, false, namespace, this, s);
		callstack = new CallStack (namespace);
		node = null;
	}

	public NameSpace getGlobalNameSpace ()
	{
		return globalNameSpace;
	}

	public Object evalSingleLine (@SuppressWarnings("unused") Reader reader, NameSpace namespace, String s)
		throws EvalError
	{
		try
		{
			if (! localInterpreter.Line ())
			{
				eof = true;
			}

			if (localInterpreter.get_jjtree ().nodeArity () > 0)
			{
				node = (SimpleNode) localInterpreter.get_jjtree ().rootNode ();
				node.setSourceFile (s);

				if (TRACE)
				{
					println ("// " + node.getText ());
				}

				retVal = node.eval (callstack, localInterpreter);

				if (callstack.depth () > 1)
				{
					throw new InterpreterError ("Callstack growing: " + callstack);
				}

				if (retVal instanceof ReturnControl)
				{
					retVal = ((ReturnControl) retVal).value;
				}
			}
		}
		catch (ParseException parseexception)
		{
			if (DEBUG)
			{
				error (parseexception.getMessage (DEBUG));
			}

			parseexception.setErrorSourceFile (s);
			throw parseexception;
		}
		catch (InterpreterError interpretererror)
		{
			interpretererror.printStackTrace ();
			throw new EvalError ("Sourced file: " + s + " internal Error: " + interpretererror.getMessage (), node,
							callstack);
		}
		catch (TargetError targeterror)
		{
			if (targeterror.getNode () == null)
			{
				targeterror.setNode (node);
			}

			targeterror.reThrow ("Sourced file: " + s);
		}
		catch (EvalError evalerror)
		{
			if (DEBUG)
			{
				evalerror.printStackTrace ();
			}

			if (evalerror.getNode () == null)
			{
				evalerror.setNode (node);
			}

			evalerror.reThrow ("Sourced file: " + s);
		}
		catch (Exception exception)
		{
			if (DEBUG)
			{
				exception.printStackTrace ();
			}

			throw new EvalError ("Sourced file: " + s + " unknown error: " + exception.getMessage (), node, callstack);
		}
		catch (TokenMgrError tokenmgrerror)
		{
			throw new EvalError ("Sourced file: " + s + " Token Parsing Error: " + tokenmgrerror.getMessage (), node,
							callstack);
		}
		finally
		{
			localInterpreter.get_jjtree ().reset ();

			if (callstack.depth () > 1)
			{
				callstack.clear ();
				callstack.push (namespace);
			}
		}

		return null;
	}

	public boolean isEOF ()
	{
		return eof;
	}

	private boolean Line () throws ParseException
	{
		return parser.Line ();
	}

	private JJTParserState get_jjtree ()
	{
		return parser.jjtree;
	}
}
