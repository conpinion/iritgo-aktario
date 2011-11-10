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

package de.iritgo.aktario.core.command;


/**
 * Commands are executed indirectly by telling <code>CommandProcessors</code>
 * to do so. This separates 'how' they are executed from the point at which
 * they are called. For example an asynchronous command processor performs its
 * commands by putting them into the thread pool for later execution, whereas
 * a simple command processor directly executes the commands in the current
 * thread.
 */
public interface CommandProcessor
{
	/** Id of the default simple command processor. */
	public static final String SIMPLE = "SimpleCommandProcessor";

	/** Id of the default asynchronous command processor. */
	public static final String ASYNC = "AsyncCommandProcessor";

	/**
	 * Get the id of the command processor.
	 *
	 * @return The command processor id.
	 */
	public String getTypeId();

	/**
	 * Perform a command.
	 *
	 * @param command The command to execute.
	 * @return The command results.
	 */
	public Object perform(Command command);
}
