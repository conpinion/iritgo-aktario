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

package de.iritgo.aktario.core.io;


import java.io.BufferedInputStream;
import java.io.InputStream;


/**
 *
 */
public class IBufferedInputStream extends BufferedInputStream
{
	// 	private int max = 1024*1024;
	// 	private byte[] ringBuffer = new byte[max];
	// 	private int wpos;
	// 	private int rpos;
	// 	private boolean ring;
	// 	private InputStream in;

	/**
	 * Creates a IBufferedInputStream and saves its argument, the input stream in, for later use.
	 */
	public IBufferedInputStream (InputStream in)
	{
		super (in);
	}

	/**
	 * Creates a BufferedInputStream with the specified buffer size, and saves
	 * its argument, the input stream in, for later use.
	 */
	public IBufferedInputStream (InputStream in, int size)
	{
		super (in, size);
	}

	// 	/**
	// 	 * Creates a IBufferedInputStream and saves its argument, the input stream in, for later use.
	// 	 */
	// 	public IBufferedInputStream (InputStream in)
	// 	{
	// 		this.in = in;
	// 		wpos = 0;
	// 		rpos = 0;
	// 		ring = false;
	// 	}
	// 	/**
	// 	 * Creates a BufferedInputStream with the specified buffer size, and saves
	// 	 * its argument, the input stream in, for later use.
	// 	 */
	// 	public IBufferedInputStream (InputStream in, int size)
	// 	{
	// 		this (in);
	// 	}
	// 	public int read () throws IOException
	// 	{
	// 		int length = in.available();
	// 		int writeBufLengthFirst = 0;
	// 		if (wpos >= max)
	// 		{
	// 			wpos = 0;
	// 			ring = true;
	// 		}
	// 		if (rpos >= max)
	// 		{
	// 			rpos = 0;
	// 			ring = false;
	// 		}
	// 		if (! ring && (rpos > wpos))
	// 		{
	// 			System.out.println ("No Data in Stream!");
	// 			return -1;
	// 		}
	// 		if (ring && (wpos + length) >= rpos)
	// 		{
	// 			writeBufLengthFirst = rpos-1-wpos;
	// 			if (writeBufLengthFirst > 0)
	// 			{
	// // 				System.out.println ("Read ring: " + writeBufLengthFirst + ":" + rpos);
	// 				in.read (ringBuffer, wpos, writeBufLengthFirst);
	// 				wpos = rpos-1;
	// 			}
	// 			return checkAndRead ();
	// 		}
	// 		if ((wpos +  length) > max)
	// 		{
	// 			writeBufLengthFirst = max-wpos;
	// 			in.read (ringBuffer, wpos, writeBufLengthFirst);
	// 			wpos = max;
	// 			writeBufLengthFirst = rpos-1;
	// 			if (writeBufLengthFirst > 0)
	// 			{
	// 				in.read (ringBuffer, wpos, writeBufLengthFirst);
	// 				ring = true;
	// 				wpos = rpos-1;
	// 			}
	// 			return checkAndRead ();
	// 		}
	// // 		System.out.println ("Read: " + length + ":" + rpos +":" + wpos);
	// 		writeBufLengthFirst = length;
	// 		in.read (ringBuffer, wpos, writeBufLengthFirst);
	// 		wpos += length;
	// 		return checkAndRead ();
	// 	}
	// 	private int checkAndRead ()
	// 	{
	// 		return ringBuffer [rpos++] & 0xff;
	// 	}
	// 	public int available () throws IOException
	// 	{
	// 		if (ring)
	// 		{
	// 			return max-rpos+wpos;
	// 		}
	// 		else
	// 		{
	// 			return wpos - rpos + in.available ();
	// 		}
	// 	}
	// 	public boolean markSupported ()
	// 	{
	// 		return false;
	// 	}
	// 	public int read (byte[] buffer) throws IOException
	// 	{
	// 		return read (buffer, 0, 1);
	// 	}
	// 	public int read (byte[] buffer, int off, int l) throws IOException
	// 	{
	// 		int result = read ();
	// 		if (result == -1)
	// 			return -1;
	// 		if (l == 1)
	// 		{
	// 			buffer[off] = ringBuffer[rpos-1];
	// 			return 1;
	// 		}
	// 		if (rpos+1 > wpos)
	// 		{
	// 			buffer[off] = ringBuffer[rpos-1];
	// 			return 1;
	// 		}
	// 		buffer[off++] = ringBuffer[rpos-1];
	// 		if (! ring && (rpos+l-1 <= wpos))
	// 		{
	// 			System.arraycopy(ringBuffer, rpos, buffer, off, (l-1));
	// 			rpos += (l-1);
	// 			return l;
	// 		}
	// 		if (! ring && (rpos+l-1 > wpos))
	// 		{
	// 			int writeBufLengthFirst = wpos-rpos+1;
	// 			System.arraycopy(ringBuffer, rpos, buffer, off, writeBufLengthFirst);
	// 			rpos = wpos+1;
	// 			return writeBufLengthFirst;
	// 		}
	// 		if (ring)
	// 		{
	// 			System.arraycopy(ringBuffer, rpos, buffer, off, max-rpos);
	// 			ring = false;
	// 			System.arraycopy(ringBuffer, rpos, buffer, off+max-rpos, wpos);
	// 			int newl = max-rpos+wpos;
	// 			rpos = wpos;
	// 			return newl;
	// 		}
	// 		return -1;
	// 	}
}
