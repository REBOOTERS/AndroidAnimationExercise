/**
 * Copyright (c) 2014-2016 by Wen Yu.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Any modifications to this file must keep this entire header intact.
 * 
 * Change History - most recent changes go on top of previous changes
 *
 * AnimatedGIFWriter.java
 *
 * Who   Date       Description
 * ====  =========  =================================================
 * WY    29Oct2015  Added parameters check for GIFFrame constructor
 * WY    27Oct2015  Initial creation
 */

package com.engineer.gif.revert.lib;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class AnimatedGIFWriter {	
	// Fields
	private int codeLen;
	private int codeIndex;
	private int clearCode;
	private int endOfImage;
	private int bufIndex;	
	private int empty_bits = 0x08;
	
	private int bitsPerPixel = 0x08;

	private byte bytes_buf[] = new byte[256];
	private int[] colorPalette;
	private boolean isApplyDither;
 	
	/**
	 * A child is made up of a parent(or prefix) code plus a suffix color
	 * and siblings are strings with a common parent(or prefix) and different
	 * suffix colors
	 */
	int child[] = new int[4097];
	
	int siblings[] = new int[4097];
	int suffix[] = new int[4097];
	
	private int logicalScreenWidth;
	private int logicalScreenHeight;
	
	private boolean animated;
	private int loopCount;
	private boolean firstFrame = true;
	
	// Define constants
	public static final byte IMAGE_SEPARATOR = 0x2c; // ","
	public static final byte IMAGE_TRAILER = 0x3b; // ";"
	public static final byte EXTENSION_INTRODUCER = 0x21; // "!"
	public static final byte GRAPHIC_CONTROL_LABEL = (byte)0xf9;
	public static final byte APPLICATION_EXTENSION_LABEL = (byte)0xff;
	public static final byte COMMENT_EXTENSION_LABEL = (byte)0xfe;
	public static final byte TEXT_EXTENSION_LABEL = 0x01;	

	private static final int MASK[] = {0x00, 0x01, 0x03, 0x07, 0x0f, 0x1f, 0x3f, 0x7f, 0xff};
		
	private static Rect getLogicalScreenSize(Bitmap[] images) {
		// Determine the logical screen dimension assuming all the frames have the same
		// left and top coordinates (0, 0)
		int logicalScreenWidth = 0;
		int logicalScreenHeight = 0;
		
		for(Bitmap image : images) {
			if(image.getWidth() > logicalScreenWidth)
				logicalScreenWidth = image.getWidth();
			if(image.getHeight() > logicalScreenHeight)
				logicalScreenHeight = image.getHeight();
		}
		
		return new Rect(0, 0, logicalScreenWidth, logicalScreenHeight);
	}
	
	private static Rect getLogicalScreenSize(GIFFrame[] frames) {
		// Determine the logical screen dimension given all the frames with different
		// left and top coordinates.
		int logicalScreenWidth = 0;
		int logicalScreenHeight = 0;
		
		for(GIFFrame frame : frames) {
			int frameRightPosition = frame.getFrameWidth() + frame.getLeftPosition();
			int frameBottomPosition = frame.getFrameHeight() + frame.getTopPosition();
			if(frameRightPosition > logicalScreenWidth)
				logicalScreenWidth = frameRightPosition;
			if(frameBottomPosition > logicalScreenHeight)
				logicalScreenHeight = frameBottomPosition;
		}
		
		return new Rect(0, 0, logicalScreenWidth, logicalScreenHeight);
	}
	
	public AnimatedGIFWriter() { this(false);}
	
	public AnimatedGIFWriter(boolean isApplyDither) {
		this.isApplyDither = isApplyDither;
	}
	
	// Write as a single frame GIF
	public void write(Bitmap img, OutputStream os) throws Exception {
		if(img == null) throw new NullPointerException("Input image is null");
		int imageWidth = img.getWidth();
		int imageHeight = img.getHeight();
		int pixels[] = new int[imageWidth * imageHeight];
		img.getPixels(pixels, 0, imageWidth, 0, 0, imageWidth, imageHeight);
		write(pixels, imageWidth, imageHeight, os);
	}	
	
	private void encode(byte[] pixels, OutputStream os) throws Exception {
		// Define local variables
		int parent = 0;
		int son = 0;
		int brother = 0;
		int color = 0;
		int index = 0;
		int dimension = pixels.length;

		// Write out the length of the root
		os.write(bitsPerPixel = (bitsPerPixel == 1)?2:bitsPerPixel);
		// Initialize the encoder
		init_encoder(bitsPerPixel);
		// Tell the decoder to initialize the string table
		send_code_to_buffer(clearCode, os);
        // Get the first color and assign it to parent
		parent = (pixels[index++]&0xff);

		while (index < dimension)
		{
			color = (pixels[index++]&0xff);
			son = child[parent];

			if ( son > 0){
				if (suffix[son] == color) {
					parent = son;
				} else {
					brother = son;
					while (true)
					{
						if (siblings[brother] > 0)
						{
							brother = siblings[brother];
							if (suffix[brother] == color)
						    {
							   parent = brother;
							   break;
						    }
						} else {
							siblings[brother] = codeIndex;
							suffix[codeIndex] = color;
							send_code_to_buffer(parent,os);
							parent = color;
							codeIndex++;
               				// Check code length
				            if(codeIndex > ((1<<codeLen)))
			                {
								if (codeLen == 12) 
 			                    {
				                    send_code_to_buffer(clearCode,os);
				                    init_encoder(bitsPerPixel);
			                    } else
					                codeLen++;
			                }
							break;
						}
					}
				}
			} else {
				child[parent] = codeIndex;
				suffix[codeIndex] = color;
				send_code_to_buffer(parent,os);
				parent = color;
				codeIndex++;
				// Check code length
				if(codeIndex > ((1<<codeLen)))
			    {
                   if (codeLen == 12) 
			       { 
				       send_code_to_buffer(clearCode,os);
				       init_encoder(bitsPerPixel);
			       } else
					   codeLen++;
			    }
			}
		}
		// Send the last color code to the buffer
		send_code_to_buffer(parent,os);
		// Send the endOfImage code to the buffer
		send_code_to_buffer(endOfImage,os);
		// Flush the last code buffer
		flush_buf(os, bufIndex+1);
    }
	
	/**
	 * This is intended to be called after writing all the frames if we write
	 * an animated GIF frame by frame.
	 * 
	 * @param os OutputStream for the animated GIF
	 * @throws Exception
	 */
	public void finishWrite(OutputStream os) throws Exception {	   	
    	os.write(IMAGE_TRAILER);
		os.close();    	
	}
    
    private void flush_buf(OutputStream os, int len) throws Exception {
		os.write(len);
		os.write(bytes_buf,0,len);
		// Clear the bytes buffer
		bufIndex = 0;
		Arrays.fill(bytes_buf, 0, 0xff, (byte)0x00);
	}
    
    private void init_encoder(int bitsPerPixel ) {
		clearCode = 1 << bitsPerPixel;
	    endOfImage = clearCode + 1;
  	    codeLen = bitsPerPixel + 1;
	    codeIndex = endOfImage + 1;
	    // Reset arrays
	    Arrays.fill(child, 0);
		Arrays.fill(siblings, 0);
		Arrays.fill(suffix, 0);
    }
    
    /**
     * This is intended to be called first when writing an animated GIF
     * frame by frame.
     * 
     * @param os OutputStream for the animated GIF
     * @param logicalScreenWidth width of the logical screen. If it is less than
     *        or equal zero, it will be determined from the first frame
     * @param logicalScreenHeight height of the logical screen. If it is less than
     *        or equal zero, it will be determined from the first frame
     * @throws Exception
     */
    public void prepareForWrite(OutputStream os, int logicalScreenWidth, int logicalScreenHeight) throws Exception {
    	// Header first
    	writeHeader(os, true);
    	this.logicalScreenWidth = logicalScreenWidth;
    	this.logicalScreenHeight = logicalScreenHeight;
    	// We are going to write animated GIF, so enable animated flag
    	animated = true;
    }
    
    // Translate codes into bytes
    private void send_code_to_buffer(int code, OutputStream os)throws Exception	{
		int temp = codeLen;
		// Shift the code to the left of the last byte in bytes_buf
        bytes_buf[bufIndex] |= ((code&MASK[empty_bits])<<(8-empty_bits));
		code >>= empty_bits;
        temp -= empty_bits;
        // If the code is longer than the empty_bits
		while (temp > 0)
		{
			if ( ++bufIndex >= 0xff)
			{
				flush_buf(os,0xff);
			}
			bytes_buf[bufIndex] |= (code&0xff);
			code >>= 8;
			temp -= 8;
		}
		empty_bits = -temp;
	}
    
    public void setLoopCount(int loopCount) {
    	this.loopCount = loopCount;
    }

    private void write(int[] pixels, int imageWidth, int imageHeight, OutputStream os) throws Exception {	
    	// Write GIF header
		writeHeader(os, true);
		// Set logical screen size
		logicalScreenWidth = imageWidth;
		logicalScreenHeight = imageHeight;
		firstFrame = true;
		// We only need to write one frame, so disable animated flag
    	animated = false;
		// Write the image frame
		writeFrame(pixels, imageWidth, imageHeight, 0, 0, 0, os);
		// Make a clean end up of the image
		os.write(IMAGE_TRAILER);
		os.close();
   }

    /**
     * Writes an array of BufferedImage as an animated GIF
     * 
     * @param images an array of BufferedImage
     * @param delays delays in millisecond for each frame
     * @param os OutputStream for the animated GIF
     * @throws Exception
     */
    public void writeAnimatedGIF(Bitmap[] images, int[] delays, OutputStream os) throws Exception {
    	// Header first
    	writeHeader(os, true);
    	
    	Rect logicalScreenSize = getLogicalScreenSize(images);
    	
    	logicalScreenWidth = logicalScreenSize.width();
    	logicalScreenHeight = logicalScreenSize.height();
    	// We are going to write animated GIF, so enable animated flag
    	animated = true;
    	
    	for(int i = 0; i < images.length; i++) {
    		// Retrieve image dimension
			int imageWidth = images[i].getWidth();
			int imageHeight = images[i].getHeight();
			int[] pixels = new int[imageWidth * imageHeight];
			images[i].getPixels(pixels, 0, imageWidth, 0, 0, imageWidth, imageHeight);
			writeFrame(pixels, imageWidth, imageHeight, 0, 0, delays[i], os);
    	}
    	
    	os.write(IMAGE_TRAILER);
		os.close();    	
    }
    
    /**
     * Writes an array of GIFFrame as an animated GIF
     * 
     * @param frames an array of GIFFrame
     * @param os OutputStream for the animated GIF
     * @throws Exception
     */
    public void writeAnimatedGIF(GIFFrame[] frames, OutputStream os) throws Exception {
    	// Header first
    	writeHeader(os, true);
    	
    	Rect logicalScreenSize = getLogicalScreenSize(frames);
    	
    	logicalScreenWidth = logicalScreenSize.width();
    	logicalScreenHeight = logicalScreenSize.height();
    	// We are going to write animated GIF, so enable animated flag
    	animated = true;
    	
    	for(int i = 0; i < frames.length; i++) {
    		// Retrieve image dimension
			int imageWidth = frames[i].getFrameWidth();
			int imageHeight = frames[i].getFrameHeight();
			int[] pixels = new int[imageWidth * imageHeight];
			frames[i].getFrame().getPixels(pixels, 0, imageWidth, 0, 0, imageWidth, imageHeight);
			if(frames[i].getTransparencyFlag() == GIFFrame.TRANSPARENCY_INDEX_SET && frames[i].getTransparentColor() != -1) {
				int transColor = (frames[i].getTransparentColor() & 0x00ffffff);				
				for(int j = pixels.length - 1; j > 0; j--) {
					int pixel = (pixels[j] & 0x00ffffff);
					if(pixel == transColor) pixels[j] = pixel; 
				}
			}
			writeFrame(pixels, imageWidth, imageHeight, frames[i].getLeftPosition(), frames[i].getTopPosition(),
					frames[i].getDelay(), frames[i].getDisposalMethod(), frames[i].getUserInputFlag(), os);
    	}
    	
    	os.write(IMAGE_TRAILER);
		os.close();    	
    }
    
    /**
     * Writes a list of GIFFrame as an animated GIF
     * 
     * @param frames a list of GIFFrame
     * @param os OutputStream for the animated GIF
     * @throws Exception
     */
    public void writeAnimatedGIF(List<GIFFrame> frames, OutputStream os) throws Exception {
    	writeAnimatedGIF(frames.toArray(new GIFFrame[0]), os);
    }
    
    private void writeComment(OutputStream os, String comment) throws Exception {
    	os.write(EXTENSION_INTRODUCER);
		os.write(COMMENT_EXTENSION_LABEL);
		byte[] commentBytes = comment.getBytes();
		int numBlocks = commentBytes.length/0xff;
		int leftOver = commentBytes.length % 0xff;
		int offset = 0;
		if(numBlocks > 0) {
			for(int i = 0; i < numBlocks; i++) {
				os.write(0xff);
				os.write(commentBytes, offset, 0xff);
				offset += 0xff;
			}
		}
		if(leftOver > 0) {
			os.write(leftOver);
			os.write(commentBytes, offset, leftOver);
		}
		os.write(0);
    }
    
    public void writeFrame(OutputStream os, GIFFrame frame) throws Exception {
    	// Retrieve image dimension
    	Bitmap image = frame.getFrame();
    	int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		int frameLeft = frame.getLeftPosition();
		int frameTop = frame.getTopPosition();
		// Determine the logical screen dimension
		if(firstFrame) {
			if(logicalScreenWidth <= 0)
				logicalScreenWidth = imageWidth;
			if(logicalScreenHeight <= 0)
				logicalScreenHeight = imageHeight;
		}
		if(frameLeft >= logicalScreenWidth || frameTop >= logicalScreenHeight) return;
		if((frameLeft + imageWidth) > logicalScreenWidth) imageWidth = logicalScreenWidth - frameLeft;
		if((frameTop + imageHeight) > logicalScreenHeight) imageHeight = logicalScreenHeight - frameTop;
		int[] pixels = new int[imageWidth * imageHeight];
		image.getPixels(pixels, 0, imageWidth, 0, 0, imageWidth, imageHeight);
		// Handle transparency color if explicitly set
    	if(frame.getTransparencyFlag() == GIFFrame.TRANSPARENCY_INDEX_SET && frame.getTransparentColor() != -1) {
			int transColor = (frame.getTransparentColor() & 0x00ffffff);				
			for(int j = pixels.length - 1; j > 0; j--) {
				int pixel = (pixels[j] & 0x00ffffff);
				if(pixel == transColor) pixels[j] = pixel; 
			}
		}    	
		writeFrame(pixels, imageWidth, imageHeight, frame.getLeftPosition(), frame.getTopPosition(),
				frame.getDelay(), frame.getDisposalMethod(), frame.getUserInputFlag(), os);
    }
    
    public void writeFrame(OutputStream os, Bitmap frame) throws Exception {
    	writeFrame(os, frame, 100); // default delay is 100 milliseconds
    }
    
    public void writeFrame(OutputStream os, Bitmap frame, int delay) throws Exception {
    	// Retrieve image dimension
		int imageWidth = frame.getWidth();
		int imageHeight = frame.getHeight();
		// Determine the logical screen dimension
		if(firstFrame) {
			if(logicalScreenWidth <= 0)
				logicalScreenWidth = imageWidth;
			if(logicalScreenHeight <= 0)
				logicalScreenHeight = imageHeight;
		}
		if(delay <= 0) delay = 100;
		if(imageWidth > logicalScreenWidth) imageWidth = logicalScreenWidth;
		if(imageHeight > logicalScreenHeight) imageHeight = logicalScreenHeight;
		int[] pixels = new int[imageWidth * imageHeight];
		frame.getPixels(pixels, 0, imageWidth, 0, 0, imageWidth, imageHeight);
		writeFrame(pixels, imageWidth, imageHeight, 0, 0, delay, os);
    }

	private void writeFrame(int[] pixels, int imageWidth, int imageHeight, int imageLeftPosition, int imageTopPosition, int delay, int disposalMethod, int userInputFlag, OutputStream os) throws Exception {	
		// Reset empty_bits
    	empty_bits = 0x08;
    	
    	int transparent_color = -1;
		int[] colorInfo; 
		
		// Reduce colors, if the color depth is less than 8 bits, reduce colors
		// to the actual bits needed, otherwise reduce to 8 bits.
		byte[] newPixels = new byte[imageWidth*imageHeight];
	    colorPalette = new int[256];
	    
	    colorInfo = checkColorDepth(pixels, newPixels, colorPalette);
		
	    if(colorInfo[0] > 0x08) {
	    	bitsPerPixel = 8;
	    	if(isApplyDither)
	    		colorInfo = reduceColorsDiffusionDither(pixels, imageWidth, imageHeight, bitsPerPixel, newPixels, colorPalette);
	    	else
	    		colorInfo = reduceColors(pixels, bitsPerPixel, newPixels, colorPalette);
	 	}
	    
	    bitsPerPixel = colorInfo[0];
	    
	    transparent_color = colorInfo[1];
	    
	    int num_of_color = 1<<bitsPerPixel;
	    
	    if(firstFrame) {
	    	// Logical screen descriptor
			byte  flags = (byte)0x88;//0b10001000 (having sorted global color map) - To be updated
			byte  bgcolor = 0x00;// To be set
			byte  aspectRatio = 0x00;			
			int colorResolution = 0x07;
			// Set GIF logical screen descriptor parameters
			flags |= ((colorResolution<<4)|(bitsPerPixel - 1)); 			
			if(transparent_color >= 0)
				bgcolor = (byte)transparent_color;
			// Write logical screen descriptor
			writeLSD(os, (short)logicalScreenWidth, (short)logicalScreenHeight, flags, bgcolor, aspectRatio);
			// Write the global colorPalette
			writePalette(os, num_of_color);
			writeComment(os, "Created by ICAFE - https://github.com/dragon66/icafe");
			if(animated)// Write Netscape extension block
				writeNetscapeApplicationBlock(os, loopCount);
	    }		
		
      	// Output the graphic control block
	    writeGraphicControlBlock(os, delay, transparent_color, disposalMethod, userInputFlag);
        // Output image descriptor
        if(firstFrame) {
        	writeImageDescriptor(os, imageWidth, imageHeight, imageLeftPosition, imageTopPosition, -1);
			firstFrame = false;
        } else {
        	writeImageDescriptor(os, imageWidth, imageHeight, imageLeftPosition, imageTopPosition, bitsPerPixel - 1);
        	// Write local colorPalette
        	writePalette(os, num_of_color);
        }
        // LZW encode the image
        encode(newPixels, os);
		/** Write out a zero length data sub-block */
		os.write(0x00);
	}
	
	private void writeFrame(int[] pixels, int imageWidth, int imageHeight, int imageLeftPosition, int imageTopPosition, int delay, OutputStream os) throws Exception	{	
    	writeFrame(pixels, imageWidth, imageHeight, imageLeftPosition, imageTopPosition, delay, GIFFrame.DISPOSAL_RESTORE_TO_BACKGROUND, GIFFrame.USER_INPUT_NONE, os);
    }
	
	// Unit of delay is supposed to be in millisecond
    private void writeGraphicControlBlock(OutputStream os, int delay, int transparent_color, int disposalMethod, int userInputFlag) throws Exception {
    	// Scale delay
    	delay = Math.round(delay/10.0f);
    	
        byte[] buf = new byte[8];
		buf[0] = EXTENSION_INTRODUCER; // Extension introducer
		buf[1] = GRAPHIC_CONTROL_LABEL; // Graphic control label
		buf[2] = 0x04; // Block size
		// Add disposalMethod and userInputFlag
		buf[3] |= (((disposalMethod&0x07) << 2)|((userInputFlag&0x01) << 1));
		buf[4] = (byte)(delay&0xff);// Delay time
		buf[5] = (byte)((delay>>8)&0xff);
		buf[6] = (byte)transparent_color;
		buf[7] = 0x00;
		
		if(transparent_color >= 0) // Add transparency indicator
			buf[3] |= 0x01;
		
		os.write(buf, 0, 8);
	}
	
	private void writeHeader(OutputStream os, boolean newFormat) throws IOException {
		// 6 bytes: GIF signature (always "GIF") plus GIF version ("87a" or "89a")
		if(newFormat)
			os.write("GIF89a".getBytes());
		else
			os.write("GIF87a".getBytes());			
	}
	
    private void writeImageDescriptor(OutputStream os, int imageWidth, int imageHeight, int imageLeftPosition, int imageTopPosition, int colorTableSize) throws Exception {
		byte imageDescriptor[] = new byte[10];
		imageDescriptor[0] = IMAGE_SEPARATOR;// Image separator ","
		imageDescriptor[1] = (byte)(imageLeftPosition&0xff);// Image left position
		imageDescriptor[2] = (byte)((imageLeftPosition>>8)&0xff);
		imageDescriptor[3] = (byte)(imageTopPosition&0xff);// Image top position
		imageDescriptor[4] = (byte)((imageTopPosition>>8)&0xff);
        imageDescriptor[5] = (byte)(imageWidth&0xff);
		imageDescriptor[6] = (byte)((imageWidth>>8)&0xff);
		imageDescriptor[7] = (byte)(imageHeight&0xff);
		imageDescriptor[8] = (byte)((imageHeight>>8)&0xff);
		imageDescriptor[9] = (byte)0x20;//0b00100000 - Packed fields
		
		if(colorTableSize >= 0) // Local color table will follow
			imageDescriptor[9] |= (1<<7|colorTableSize);
		
		os.write(imageDescriptor, 0, 10);
	}
    
    // Write logical screen descriptor
	private void writeLSD(OutputStream os, short screen_width, short screen_height, short flags, byte bgcolor, byte aspectRatio) throws IOException	{
		byte[] descriptor = new byte[7]; 
		// Screen_width
	    descriptor[0] = (byte)(screen_width&0xff);
	    descriptor[1] = (byte)((screen_width>>8)&0xff);
	    // Screen_height
	    descriptor[2] = (byte)(screen_height&0xff);
	    descriptor[3] = (byte)((screen_height>>8)&0xff);
		// Global flags
        descriptor[4] = (byte)(flags&0xff);
		// Background color
        descriptor[5] = bgcolor;
		// AspectRatio
	    descriptor[6] = aspectRatio;
	    
	    os.write(descriptor);
	}
	
    private void writeNetscapeApplicationBlock(OutputStream os, int loopCounts) throws Exception {
    	byte[] buf = new byte[19];
 		buf[0] = EXTENSION_INTRODUCER; // Extension introducer
 		buf[1] = APPLICATION_EXTENSION_LABEL; // Application extension label
 		buf[2] = 0x0b; // Block size
 		buf[3] = 'N'; // Application Identifier (8 bytes)
 		buf[4] = 'E';
 		buf[5] = 'T';
 		buf[6] = 'S';
 		buf[7] = 'C';
 		buf[8] = 'A';
 		buf[9] = 'P';
 		buf[10]= 'E';
 		buf[11]= '2';// Application Authentication Code (3 bytes)
 		buf[12]= '.';
 		buf[13]= '0';
 		buf[14]= 0x03;
 		buf[15]= 0x01;
 		buf[16]= (byte)(loopCounts&0xff); // Loop counts
 		buf[17]= (byte)((loopCounts>>8)&0xff);
 		buf[18]= 0x00; // Block terminator 
 		
 		os.write(buf);
    }
    
    private void writePalette(OutputStream os, int num_of_color) throws Exception {
        int index = 0;
        byte colors[] = new byte[num_of_color*3];
        
        for (int i=0; i<num_of_color; i++)
	    {
		  colors[index++] = (byte)(((colorPalette[i]>>16)&0xff));
		  colors[index++] = (byte)(((colorPalette[i]>>8)&0xff));
		  colors[index++] = (byte)(colorPalette[i]&0xff);
	    }
        
	    os.write(colors, 0, num_of_color*3);
	}
    
    private static int[] checkColorDepth(int[] rgbTriplets, byte[] newPixels, final int[] colorPalette) {
		int index = 0;
		int temp = 0;
		int bitsPerPixel = 1;
		int transparent_index = -1;// Transparent color index
		int transparent_color = -1;// Transparent color
		int[] colorInfo = new int[2];// Return value
		
		IntHashtable<Integer> rgbHash = new IntHashtable<Integer>(1023);
				
		for (int i = 0; i < rgbTriplets.length; i++) {
			temp = (rgbTriplets[i]&0x00ffffff);

            if((rgbTriplets[i] >>> 24) < 0x80 ) { // Transparent
				if (transparent_index < 0) {
					transparent_index = index;
				    transparent_color = temp;// Remember transparent color
				}
				temp = Integer.MAX_VALUE;
			}	

            Integer entry = rgbHash.get(temp);
			
			if (entry!=null) {
				newPixels[i] = entry.byteValue();
			} else {
				if(index > 0xff) {// More than 256 colors, have to reduce
				 // Colors before saving as an indexed color image
					colorInfo[0] = 24;
					return colorInfo;
				}
				rgbHash.put(temp, index);
				newPixels[i] = (byte)index;
				colorPalette[index++] = ((0xff<<24)|temp);
			}
		}
		if(transparent_index >= 0)// This line could be used to set a different background color
			colorPalette[transparent_index] = transparent_color;
		// Return the actual bits per pixel and the transparent color index if any
		while ((1<<bitsPerPixel)<index)  bitsPerPixel++;
		
		colorInfo[0] = bitsPerPixel;
		colorInfo[1] = transparent_index;

        return colorInfo;
	}
    
    private static int[] reduceColorsDiffusionDither(int[] rgbTriplets, int width, int height, int colorDepth, byte[] newPixels, final int[] colorPalette)	{
		if(colorDepth > 8 || colorDepth < 1) 
			throw new IllegalArgumentException("Invalid color depth " + colorDepth);
		int[] colorInfo = new int[2];
		int colors = 0;
		colors = new WuQuant(rgbTriplets, 1<<colorDepth).quantize(colorPalette, colorInfo);
		// Call Floyd-Steinberg dither
		dither_FloydSteinberg(rgbTriplets, width, height, newPixels, colors, colorPalette, colorInfo[1]);
		// Return the actual bits per pixel and the transparent color index if any

		return colorInfo;
	}
    
 // Color quantization
 	private static int[] reduceColors(int[] rgbTriplets, int colorDepth, byte[] newPixels, final int[] colorPalette)	{
 		int[] colorInfo = new int[2];
 		new WuQuant(rgbTriplets, 1<<colorDepth).quantize(newPixels, colorPalette, colorInfo);
 		
 		return colorInfo;
 	}
    
    private static void dither_FloydSteinberg(int[] rgbTriplet, int width, int height, byte[] newPixels, int no_of_color, 
            int[] colorPalette, int transparent_index)
	{
    	int index = 0, index1 = 0, err1, err2, err3, red, green, blue;
	    // Define error arrays
		// Errors for the current line
		int[] tempErr;
		int[] thisErrR = new int[width + 2];
		int[] thisErrG = new int[width + 2];
		int[] thisErrB = new int[width + 2];
        // Errors for the following line
		int[] nextErrR = new int[width + 2];
		int[] nextErrG = new int[width + 2];
		int[] nextErrB = new int[width + 2];

		InverseColorMap invMap;

		invMap = new InverseColorMap();
		invMap.createInverseMap(no_of_color, colorPalette);

		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; index1++, col++)	{
				 // Transparent, no dither
				if((rgbTriplet[index1] >>> 24) < 0x80 ) {
					newPixels[index1] = (byte)transparent_index;
					continue;
		        }
		
				red = ((rgbTriplet[index1]&0xff0000)>>>16) + thisErrR[col + 1];
				if (red > 255) red = 255;
			    else if (red < 0) red = 0;
	  		    
				green = ((rgbTriplet[index1]&0x00ff00)>>>8) + thisErrG[col + 1];
				if (green > 255) green = 255;
			    else if (green < 0) green = 0;
				
				blue = (rgbTriplet[index1]&0x0000ff) + thisErrB[col + 1];
				if (blue > 255) blue = 255;
			    else if (blue < 0) blue = 0;

                // Find the nearest color index
			    index = invMap.getNearestColorIndex(red, green, blue);				
				newPixels[index1] = (byte)index;// The colorPalette index for this pixel

				// Find errors for different channels
		        err1 = red   - ((colorPalette[index]>>16)&0xff);// Red channel
				err2 = green - ((colorPalette[index]>>8)&0xff);// Green channel
			    err3 = blue  -  (colorPalette[index]&0xff);// Blue channel
			    // Diffuse error
				// Red
                thisErrR[col + 2] += ((err1*7)/16);
				nextErrR[col    ] += ((err1*3)/16);
				nextErrR[col + 1] += ((err1*5)/16);
				nextErrR[col + 2] += ((err1)/16);
                // Green
                thisErrG[col + 2] += ((err2*7)/16);
				nextErrG[col    ] += ((err2*3)/16);
				nextErrG[col + 1] += ((err2*5)/16);
				nextErrG[col + 2] += ((err2)/16);
				// Blue
				thisErrB[col + 2] += ((err3*7)/16);
				nextErrB[col    ] += ((err3*3)/16);
				nextErrB[col + 1] += ((err3*5)/16);
				nextErrB[col + 2] += ((err3)/16);
		    }
			// We have finished one row, switch the error arrays
			tempErr = thisErrR;
			thisErrR = nextErrR;
            nextErrR = tempErr;

			tempErr = thisErrG;
			thisErrG = nextErrG;
			nextErrG = tempErr;
	
	        tempErr = thisErrB;
			thisErrB = nextErrB;
			nextErrB = tempErr;
			
            // Clear the error arrays
			Arrays.fill(nextErrR, 0);
			Arrays.fill(nextErrG, 0);
			Arrays.fill(nextErrB, 0);
		}
	}
    
    public static class GIFFrame {
    	// Frame parameters
    	private Bitmap frame;
    	private int leftPosition;
    	private int topPosition;
    	private int frameWidth;
    	private int frameHeight;
    	private int delay;
    	private int disposalMethod = DISPOSAL_UNSPECIFIED;
    	private int userInputFlag = USER_INPUT_NONE;
    	private int transparencyFlag = TRANSPARENCY_INDEX_NONE;
    	
    	// The transparent color value in RRGGBB format.
    	// The highest order byte has no effect.
    	private int transparentColor = TRANSPARENCY_COLOR_NONE; // Default no transparent color
    	
    	public static final int DISPOSAL_UNSPECIFIED = 0;
    	public static final int DISPOSAL_LEAVE_AS_IS = 1;
    	public static final int DISPOSAL_RESTORE_TO_BACKGROUND = 2;
    	public static final int DISPOSAL_RESTORE_TO_PREVIOUS = 3;
    	// Values between 4-7 inclusive
    	public static final int DISPOSAL_TO_BE_DEFINED = 7;
    	
    	public static final int USER_INPUT_NONE = 0;
    	public static final int USER_INPUT_EXPECTED = 1;
    	
    	public static final int TRANSPARENCY_INDEX_NONE = 0;
    	public static final int TRANSPARENCY_INDEX_SET = 1;
    	
    	public static final int TRANSPARENCY_COLOR_NONE = -1;
    	
    	public GIFFrame(Bitmap frame) {
    		this(frame, 0, 0, 0, GIFFrame.DISPOSAL_UNSPECIFIED);
    	}
    	
    	public GIFFrame(Bitmap frame, int delay) {
    		this(frame, 0, 0, delay, GIFFrame.DISPOSAL_UNSPECIFIED);
    	}
    	
    	public GIFFrame(Bitmap frame, int delay, int disposalMethod) {
    		this(frame, 0, 0, delay, disposalMethod);
    	}
    	
    	public GIFFrame(Bitmap frame, int leftPosition, int topPosition, int delay, int disposalMethod) {
    		this(frame, leftPosition, topPosition, delay, disposalMethod, USER_INPUT_NONE, TRANSPARENCY_INDEX_NONE, TRANSPARENCY_COLOR_NONE);
    	}
    	
    	public GIFFrame(Bitmap frame, int leftPosition, int topPosition, int delay, int disposalMethod, int userInputFlag, int transparencyFlag, int transparentColor) {
    		if(frame == null) throw new IllegalArgumentException("Null input image");
    		if(disposalMethod < DISPOSAL_UNSPECIFIED || disposalMethod > DISPOSAL_TO_BE_DEFINED)
    			throw new IllegalArgumentException("Invalid disposal method: " + disposalMethod);
    		if(userInputFlag < USER_INPUT_NONE || userInputFlag > USER_INPUT_EXPECTED)
    			throw new IllegalArgumentException("Invalid user input flag: " + userInputFlag);
    		if(transparencyFlag < TRANSPARENCY_INDEX_NONE || transparencyFlag > TRANSPARENCY_INDEX_SET)
    			throw new IllegalArgumentException("Invalid transparency flag: " + transparencyFlag);
    		if(leftPosition < 0 || topPosition < 0)
    			throw new IllegalArgumentException("Negative coordinates for frame top-left position");
    		if(delay < 0) delay = 0;
    		this.frame = frame;
    		this.leftPosition = leftPosition;
    		this.topPosition = topPosition;	
    		this.delay = delay;
    		this.disposalMethod = disposalMethod;
    		this.userInputFlag = userInputFlag;
    		this.transparencyFlag = transparencyFlag;
    		this.frameWidth = frame.getWidth();
    		this.frameHeight = frame.getHeight();
    		this.transparentColor = transparentColor;
    	}
    	
    	public int getDelay() {
    		return delay;
    	}
    	
    	public int getDisposalMethod() {
    		return disposalMethod;
    	}
    	
    	public Bitmap getFrame() {
    		return frame;
    	}
    	
    	public int getFrameHeight() {
    		return frameHeight;
    	}
    	
    	public int getFrameWidth() {
    		return frameWidth;
    	}
    	
    	public int getLeftPosition() {
    		return leftPosition;
    	}
    	
    	public int getTopPosition() {
    		return topPosition;
    	}
    	
    	public int getTransparentColor() {
    		return transparentColor;
    	}
    	
    	public int getTransparencyFlag() {
    		return transparencyFlag;
    	}
    	
    	public int getUserInputFlag() {
    		return userInputFlag;
    	}
     }
    
    /**
     * Java port of
     * C Implementation of Wu's Color Quantizer (v. 2)
     * (see Graphics Gems vol. II, pp. 126-133)
     * Author:	Xiaolin Wu
     * Dept. of Computer Science
     * Univ. of Western Ontario
     * London, Ontario N6A 5B7
     * wu@csd.uwo.ca
     * 
     * Algorithm: Greedy orthogonal bipartition of RGB space for variance
     * minimization aided by inclusion-exclusion tricks.
     * For speed no nearest neighbor search is done. Slightly
     * better performance can be expected by more sophisticated
     * but more expensive versions.
     * 
     * The author thanks Tom Lane at Tom_Lane@G.GP.CS.CMU.EDU for much of
     * additional documentation and a cure to a previous bug.
     * 
     * Free to distribute, comments and suggestions are appreciated.
     */
    private static class WuQuant {
    	private static final int MAXCOLOR =	256;
    	private static final int RED = 2;
    	private static final int GREEN = 1;
    	private static final int BLUE =	0;
    	
    	private static int QUANT_SIZE = 33;// quant size

    	private static final class Box {
    		int r0;	 /* min value, exclusive */
    		int r1;	 /* max value, inclusive */
    		int g0;  
    		int g1;  
    		int b0;  
    		int b1;
    		int vol;
    	};
    	
    	private int	size; /*image size*/
    	private int	lut_size; /*color look-up table size*/
    	private int qadd[];
    	private int pixels[];
    	private int transparent_color = -1;// Transparent color 
    	
        private float m2[][][] = new float[QUANT_SIZE][QUANT_SIZE][QUANT_SIZE];
        private long wt[][][] = new long[QUANT_SIZE][QUANT_SIZE][QUANT_SIZE];
        private long mr[][][] = new long[QUANT_SIZE][QUANT_SIZE][QUANT_SIZE];
        private long mg[][][] = new long[QUANT_SIZE][QUANT_SIZE][QUANT_SIZE];
        private long mb[][][] = new long[QUANT_SIZE][QUANT_SIZE][QUANT_SIZE];
           
        public WuQuant(int[] pixels, int lut_size) {
        	this.pixels = pixels;
        	this.size = pixels.length;
        	this.lut_size = lut_size;
        }
        
        public int quantize(final byte[] newPixels, final int[] lut, int[] colorInfo) {
            Box cube[] = new Box[MAXCOLOR];
            int lut_r, lut_g, lut_b;
            int tag[] = new int[QUANT_SIZE*QUANT_SIZE*QUANT_SIZE];

            int next, i, k;
            long weight;
            float vv[] = new float[MAXCOLOR], temp;
            
            Hist3d(wt, mr, mg, mb, m2);
            M3d(wt, mr, mg, mb, m2);
            
            for(i = 0; i < MAXCOLOR; i++)
         	   cube[i] = new Box();
            
            cube[0].r0 = cube[0].g0 = cube[0].b0 = 0;
            cube[0].r1 = cube[0].g1 = cube[0].b1 = QUANT_SIZE - 1;
            next = 0;
            
            if(transparent_color >= 0) lut_size--;
            
            for(i = 1; i < lut_size; ++i){
         	   if (Cut(cube[next], cube[i])) {
         		   /* volume test ensures we won't try to cut one-cell box */
         		   vv[next] = (cube[next].vol > 1) ? Var(cube[next]) : 0.0f;
         		   vv[i] = (cube[i].vol > 1) ? Var(cube[i]) : 0.0f;
         	   } else {
         		   vv[next] = 0.0f;   /* don't try to split this box again */
         		   i--;              /* didn't create box i */
         	   }
         	   next = 0; temp = vv[0];
         	   for(k = 1; k <= i; ++k)
         		   if (vv[k] > temp) {
         			   temp = vv[k]; next = k;
         		   }
         	   if (temp <= 0.0f) {
         		   k = i + 1;
         		   break;
         	   }
            }
        
            for(k = 0; k < lut_size; ++k){
         	   Mark(cube[k], k, tag);
         	   weight = Vol(cube[k], wt);
         	   if (weight > 0) {
         		   lut_r = (int)(Vol(cube[k], mr) / weight);
         		   lut_g = (int)(Vol(cube[k], mg) / weight);
         		   lut_b = (int)(Vol(cube[k], mb) / weight);
         		   lut[k] = (255 << 24) | (lut_r  << 16) | (lut_g << 8) | lut_b;
         	   }
         	   else	{
           		   lut[k] = 0;
         	   }
            }

            for(i = 0; i < size; ++i) {
         	   if((pixels[i] >>> 24) < 0x80)
         		   newPixels[i] = (byte)lut_size;
         	   else
         		   newPixels[i] = (byte)tag[qadd[i]];
            }
            
            int bitsPerPixel = 0;
            while ((1<<bitsPerPixel) < lut_size)  bitsPerPixel++;
            colorInfo[0] = bitsPerPixel;
            colorInfo[1] = -1;       
            
            if(transparent_color >= 0) {
          	  lut[lut_size] = transparent_color; // Set the transparent color
          	  colorInfo[1] = lut_size;
            }
            
            return lut_size;
        }
        
        public int quantize(final int[] lut, int[] colorInfo) {
           Box cube[] = new Box[MAXCOLOR];
           int lut_r, lut_g, lut_b;
         
           int next, i, k;
           long weight;
           float vv[] = new float[MAXCOLOR], temp;
           
           Hist3d(wt, mr, mg, mb, m2);
           M3d(wt, mr, mg, mb, m2);
           
           for(i = 0; i < MAXCOLOR; i++)
        	   cube[i] = new Box();
           
           cube[0].r0 = cube[0].g0 = cube[0].b0 = 0;
           cube[0].r1 = cube[0].g1 = cube[0].b1 = QUANT_SIZE - 1;
           next = 0;
           
           if(transparent_color >= 0) lut_size--;
           
           for(i = 1; i < lut_size; ++i){
        	   if (Cut(cube[next], cube[i])) {
        		   /* volume test ensures we won't try to cut one-cell box */
        		   vv[next] = (cube[next].vol > 1) ? Var(cube[next]) : 0.0f;
        		   vv[i] = (cube[i].vol > 1) ? Var(cube[i]) : 0.0f;
        	   } else {
        		   vv[next] = 0.0f;   /* don't try to split this box again */
        		   i--;              /* didn't create box i */
        	   }
        	   next = 0; temp = vv[0];
        	   for(k = 1; k <= i; ++k)
        		   if (vv[k] > temp) {
        			   temp = vv[k]; next = k;
        		   }
        	   if (temp <= 0.0f) {
        		   k = i + 1;
        		   break;
        	   }
           }
       
           for(k = 0; k < lut_size; ++k){
        	   weight = Vol(cube[k], wt);
        	   if (weight > 0) {
        		   lut_r = (int)(Vol(cube[k], mr) / weight);
        		   lut_g = (int)(Vol(cube[k], mg) / weight);
        		   lut_b = (int)(Vol(cube[k], mb) / weight);
        		   lut[k] = (255 << 24) | (lut_r  << 16) | (lut_g << 8) | lut_b;
        	   }
        	   else	{
          		   lut[k] = 0;		
        	   }
           }
           
           int bitsPerPixel = 0;
           while ((1<<bitsPerPixel) < lut_size)  bitsPerPixel++;
           colorInfo[0] = bitsPerPixel;
           colorInfo[1] = -1;
           
           if(transparent_color >= 0) {
          	  lut[lut_size] = transparent_color; // Set the transparent color
          	  colorInfo[1] = lut_size;
           }
           
           return lut_size;
        }

    	/* Histogram is in elements 1..HISTSIZE along each axis,
    	 * element 0 is for base or marginal value
    	 * NB: these must start out 0!
    	 */
    	private void Hist3d(long vwt[][][], long vmr[][][], long vmg[][][], long vmb[][][], float m2[][][]) {
    		/* build 3-D color histogram of counts, r/g/b, c^2 */
    		int r, g, b;
    		int	i, inr, ing, inb, table[] = new int[256];
    	
    		for(i = 0; i < 256; ++i) table[i]= i*i;
    		
    		qadd = new int[size];
    	
    		for(i = 0; i < size; ++i) {
    			int rgb = pixels[i];
    			if((rgb >>> 24) < 0x80) { // Transparent
    				if (transparent_color < 0)	// Find the transparent color	
    					transparent_color = rgb;
    			}
    			r = ((rgb >> 16)& 0xff);
    			g = ((rgb >> 8 )& 0xff);
    			b = ( rgb       & 0xff);
    			inr = (r >> 3) + 1; 
    			ing = (g >> 3) + 1; 
    			inb = (b >> 3) + 1; 
    			qadd[i] = (inr << 10) + (inr << 6) + inr + (ing << 5) + ing + inb;
    			/*[inr][ing][inb]*/
    			++vwt[inr][ing][inb];
    			vmr[inr][ing][inb] += r;
    			vmg[inr][ing][inb] += g;
    			vmb[inr][ing][inb] += b;
    		    m2[inr][ing][inb] += table[r] + table[g] + table[b];
    		}
    	}
    	
    	/* At conclusion of the histogram step, we can interpret
    	 *   wt[r][g][b] = sum over voxel of P(c)
    	 *   mr[r][g][b] = sum over voxel of r*P(c)  ,  similarly for mg, mb
    	 *   m2[r][g][b] = sum over voxel of c^2*P(c)
    	 * Actually each of these should be divided by 'size' to give the usual
    	 * interpretation of P() as ranging from 0 to 1, but we needn't do that here.
    	*/

    	/* We now convert histogram into moments so that we can rapidly calculate
    	 * the sums of the above quantities over any desired box.
    	 */
    	private void M3d(long vwt[][][], long vmr[][][], long vmg[][][], long vmb[][][], float m2[][][]) {
    		/* compute cumulative moments. */
    		int i, r, g, b;
    		int line, line_r, line_g, line_b;
    		int area[] = new int[QUANT_SIZE];
    		int area_r[] = new int[QUANT_SIZE];
    		int area_g[] = new int[QUANT_SIZE];
    		int area_b[] = new int[QUANT_SIZE];
    		float line2, area2[] = new float[QUANT_SIZE];
    	
    		for(r = 1; r < QUANT_SIZE; ++r) {
    			for(i = 0; i < QUANT_SIZE; ++i) 
    				area2[i] = area[i] = area_r[i] = area_g[i] = area_b[i] = 0;
    			for(g = 1; g < QUANT_SIZE; ++g) {
    				line2 = line = line_r = line_g = line_b = 0;
    				for(b = 1; b < QUANT_SIZE; ++b){
    					line   += vwt[r][g][b];
    					line_r += vmr[r][g][b]; 
    					line_g += vmg[r][g][b]; 
    					line_b += vmb[r][g][b];
    					line2  += m2[r][g][b];
    					
    					area[b] += line;
    					area_r[b] += line_r;
    					area_g[b] += line_g;
    					area_b[b] += line_b;
    					area2[b] += line2;
    					
    					vwt[r][g][b] = vwt[r-1][g][b] + area[b];
    					vmr[r][g][b] = vmr[r-1][g][b] + area_r[b];
    					vmg[r][g][b] = vmg[r-1][g][b] + area_g[b];
    					vmb[r][g][b] = vmb[r-1][g][b] + area_b[b];
    					m2[r][g][b]  = m2[r-1][g][b]  + area2[b];
    				}
    			}			
    		}
    	}
    	
    	private long Vol(Box cube, long mmt[][][]) {
    		/* Compute sum over a box of any given statistic */
    		return ( mmt[cube.r1][cube.g1][cube.b1] 
    				-mmt[cube.r1][cube.g1][cube.b0]
    				-mmt[cube.r1][cube.g0][cube.b1]
    				+mmt[cube.r1][cube.g0][cube.b0]
    				-mmt[cube.r0][cube.g1][cube.b1]
    				+mmt[cube.r0][cube.g1][cube.b0]
    				+mmt[cube.r0][cube.g0][cube.b1]
    				-mmt[cube.r0][cube.g0][cube.b0] );
    	}
    	
    	/* The next two routines allow a slightly more efficient calculation
    	* of Vol() for a proposed subbox of a given box.  The sum of Top()
    	* and Bottom() is the Vol() of a subbox split in the given direction
    	* and with the specified new upper bound.
    	*/
    	
    	private long Bottom(Box cube, int dir, long mmt[][][]) {
    		/* Compute part of Vol(cube, mmt) that doesn't depend on r1, g1, or b1 */
    		/* (depending on dir) */
    		switch(dir) {
    			case RED:
    				return( -mmt[cube.r0][cube.g1][cube.b1]
    						+mmt[cube.r0][cube.g1][cube.b0]
    						+mmt[cube.r0][cube.g0][cube.b1]
    						-mmt[cube.r0][cube.g0][cube.b0] );
    			case GREEN:
    				return( -mmt[cube.r1][cube.g0][cube.b1]
    						+mmt[cube.r1][cube.g0][cube.b0]
    						+mmt[cube.r0][cube.g0][cube.b1]
    						-mmt[cube.r0][cube.g0][cube.b0] );
    			case BLUE:
    				return( -mmt[cube.r1][cube.g1][cube.b0]
    						+mmt[cube.r1][cube.g0][cube.b0]
    						+mmt[cube.r0][cube.g1][cube.b0]
    						-mmt[cube.r0][cube.g0][cube.b0] );
    			default:
    				return 0;
    		}
    	}

    	private long Top(Box cube, int dir, int pos, long mmt[][][]) {
    		/* Compute remainder of Vol(cube, mmt), substituting pos for */
    		/* r1, g1, or b1 (depending on dir) */
    		switch(dir) {
    			case RED:
    				return( mmt[pos][cube.g1][cube.b1] 
    				   -mmt[pos][cube.g1][cube.b0]
    				   -mmt[pos][cube.g0][cube.b1]
    				   +mmt[pos][cube.g0][cube.b0] );
    			case GREEN:
    				return( mmt[cube.r1][pos][cube.b1] 
    				   -mmt[cube.r1][pos][cube.b0]
    				   -mmt[cube.r0][pos][cube.b1]
    				   +mmt[cube.r0][pos][cube.b0] );
    			case BLUE:
    				return( mmt[cube.r1][cube.g1][pos]
    				   -mmt[cube.r1][cube.g0][pos]
    				   -mmt[cube.r0][cube.g1][pos]
    				   +mmt[cube.r0][cube.g0][pos] );
    			default:
    				return 0;
    		}
    	}
    	
    	private float Var(Box cube) {
    		/* Compute the weighted variance of a box */
    		/* NB: as with the raw statistics, this is really the variance * size */
    		float dr, dg, db, xx;
    		dr = Vol(cube, mr); 
    		dg = Vol(cube, mg); 
    		db = Vol(cube, mb);
    		xx =  m2[cube.r1][cube.g1][cube.b1] 
    			  -m2[cube.r1][cube.g1][cube.b0]
    			  -m2[cube.r1][cube.g0][cube.b1]
    			  +m2[cube.r1][cube.g0][cube.b0]
    			  -m2[cube.r0][cube.g1][cube.b1]
    			  +m2[cube.r0][cube.g1][cube.b0]
    			  +m2[cube.r0][cube.g0][cube.b1]
    			  -m2[cube.r0][cube.g0][cube.b0];
    		return  xx - (dr*dr + dg*dg + db*db)/Vol(cube,wt);    
    	}

    	/* We want to minimize the sum of the variances of two subboxes.
    	* The sum(c^2) terms can be ignored since their sum over both subboxes
    	* is the same (the sum for the whole box) no matter where we split.
    	* The remaining terms have a minus sign in the variance formula,
    	* so we drop the minus sign and MAXIMIZE the sum of the two terms.
    	*/
    	private float Maximize(Box cube, int dir, int first, int last, int cut[],
    			long whole_r, long whole_g, long whole_b, long whole_w) {
    		long half_r, half_g, half_b, half_w;
    		long base_r, base_g, base_b, base_w;
    		int i;
    		float temp, max;

    		base_r = Bottom(cube, dir, mr);
    		base_g = Bottom(cube, dir, mg);
    		base_b = Bottom(cube, dir, mb);
    		base_w = Bottom(cube, dir, wt);
    		
    		max = 0.0f;
    		cut[0] = -1;
    	
    		for(i = first; i < last; ++i){
    			half_r = base_r + Top(cube, dir, i, mr);
    			half_g = base_g + Top(cube, dir, i, mg);
    			half_b = base_b + Top(cube, dir, i, mb);
    			half_w = base_w + Top(cube, dir, i, wt);
    			/* now half_x is sum over lower half of box, if split at i */
    			if (half_w == 0) /* subbox could be empty of pixels! */
    				continue;    /* never split into an empty box */
    			temp = (half_r*half_r + half_g*half_g +	half_b*half_b)/(float)half_w;
    			half_r = whole_r - half_r;
    			half_g = whole_g - half_g;
    			half_b = whole_b - half_b;
    			half_w = whole_w - half_w;
    			if (half_w == 0) /* subbox could be empty of pixels! */
    				continue; /* never split into an empty box */
    			temp += (half_r*half_r + half_g*half_g + half_b*half_b)/(float)half_w;
    			
    			if (temp > max) { max = temp; cut[0] = i;}
    		}
    		
    		return max;
    	}
    	
    	private boolean Cut(Box set1, Box set2) {
    		int dir;
    		int cutr[] = new int[1];
            int cutg[] = new int[1];
            int cutb[] = new int[1];
    		float maxr, maxg, maxb;
    		long whole_r, whole_g, whole_b, whole_w;

    		whole_r = Vol(set1, mr);
    		whole_g = Vol(set1, mg);
    		whole_b = Vol(set1, mb);
    		whole_w = Vol(set1, wt);

    		maxr = Maximize(set1, RED, set1.r0 + 1, set1.r1, cutr,
    				whole_r, whole_g, whole_b, whole_w);
    		maxg = Maximize(set1, GREEN, set1.g0 + 1, set1.g1, cutg,
    				whole_r, whole_g, whole_b, whole_w);
    		maxb = Maximize(set1, BLUE, set1.b0 + 1, set1.b1, cutb,
    				whole_r, whole_g, whole_b, whole_w);

    		if(maxr >= maxg && maxr >= maxb) {
    			dir = RED;
    			if (cutr[0] < 0) return false; /* can't split the box */
    		} else if(maxg >= maxr && maxg >= maxb) 
    			dir = GREEN;
    		else
    			dir = BLUE; 

    		set2.r1 = set1.r1;
    		set2.g1 = set1.g1;
    		set2.b1 = set1.b1;

    		switch (dir){
    			case RED:
    				set2.r0 = set1.r1 = cutr[0];
    				set2.g0 = set1.g0;
    				set2.b0 = set1.b0;
    				break;
    			case GREEN:
    				set2.g0 = set1.g1 = cutg[0];
    				set2.r0 = set1.r0;
    				set2.b0 = set1.b0;
    				break;
    			case BLUE:
    				set2.b0 = set1.b1 = cutb[0];
    				set2.r0 = set1.r0;
    				set2.g0 = set1.g0;
    				break;
    		}
    		set1.vol = (set1.r1 - set1.r0)*(set1.g1 - set1.g0)*(set1.b1 - set1.b0);
    		set2.vol = (set2.r1 - set2.r0)*(set2.g1 - set2.g0)*(set2.b1 - set2.b0);
    	
    		return true;  
    	}
    	
    	private void Mark(Box cube, int label, int tag[]) {
    		int r, g, b;

    		for(r = cube.r0 + 1; r <= cube.r1; ++r)
    			for(g = cube.g0 + 1; g <= cube.g1; ++g)
    				for(b = cube.b0 + 1; b <= cube.b1; ++b)
    					tag[(r<<10) + (r<<6) + r + (g<<5) + g + b] = label;
    	}
    }
    
    /**
     * A hash table using primitive integer keys. 
     *
     * Based on
     * QuadraticProbingHashTable.java
     * <p>
     * Probing table implementation of hash tables.
     * Note that all "matching" is based on the equals method.
     * 
     * @author Mark Allen Weiss
     */
    private static class IntHashtable<E> {

        /** The array of HashEntry. */
        private HashEntry<E> [ ] array;  // The array of HashEntry
        private int currentSize;  // The number of occupied cells
      
    	/**
         * Construct the hash table.
         * @param size the approximate initial size.
         */
        @SuppressWarnings("unchecked")
    	public IntHashtable(int size) {
            array = new HashEntry [size];
            makeEmpty( );
        }

        /**
         * Insert into the hash table. If the item is
         * already present, do nothing.
         * @param key the item to insert.
         */
        public void put(int key, E value) {
            // Insert key as active
            int currentPos = locate( key );
            if( isActive( currentPos ) )
                return;

            array[ currentPos ] = new HashEntry<E> ( key, value, true );

            // Rehash
            if( ++currentSize > array.length / 2 )
                rehash( );
        }

        /**
         * Expand the hash table.
         */
        @SuppressWarnings("unchecked")
    	private void rehash( ) {
            HashEntry<E> [ ] oldArray = array;

            // Create a new double-sized, empty table
            array = new HashEntry [nextPrime( 2 * oldArray.length )];
            currentSize = 0;

            // Copy table over
            for( int i = 0; i < oldArray.length; i++ )
                if( oldArray[i] != null && oldArray[i].isActive )
                    put( oldArray[i].key, oldArray[i].value );

            return;
        }

        /**
         * Method that performs quadratic probing resolution.
         * @param key the item to search for.
         * @return the index of the item.
         */
        private int locate(int key) {
            int collisionNum = 0;

    		// And with the largest positive integer
    		int currentPos = (key & 0x7FFFFFFF) % array.length;

            while( array[ currentPos ] != null &&
                    array[ currentPos ].key != key )
            {
                currentPos += 2 * ++collisionNum - 1;  // Compute ith probe
                if( currentPos >= array.length )       // Implement the mod
                  currentPos -= array.length;
            }
            return currentPos;
        }

       	/**
         * Find an item in the hash table.
         * @param key the item to search for.
         * @return the value of the matching item.
         */
        public E get(int key) {
            int currentPos = locate( key );
            return isActive( currentPos ) ? array[ currentPos ].value : null;
        }

        /**
         * Return true if currentPos exists and is active.
         * @param currentPos the result of a call to findPos.
         * @return true if currentPos is active.
         */
        private boolean isActive( int currentPos ) {
            return array[ currentPos ] != null && array[ currentPos ].isActive;
        }

        /**
         * Make the hash table logically empty.
         */
        public void makeEmpty( ) {
            currentSize = 0;
            for( int i = 0; i < array.length; i++ )
                array[ i ] = null;
        }
        
        /**
         * Internal method to find a prime number at least as large as n.
         * @param n the starting number (must be positive).
         * @return a prime number larger than or equal to n.
         */
        private static int nextPrime(int n) {
            if( n % 2 == 0 )
                n++;

            for( ; !isPrime( n ); n += 2 )
                ;

            return n;
        }

        /**
         * Internal method to test if a number is prime.
         * Not an efficient algorithm.
         * @param n the number to test.
         * @return the result of the test.
         */
        private static boolean isPrime(int n) {
            if( n == 2 || n == 3 )
                return true;

            if( n == 1 || n % 2 == 0 )
                return false;

            for( int i = 3; i * i <= n; i += 2 )
                if( n % i == 0 )
                    return false;

            return true;
        }
        
        // The basic entry stored in ProbingHashTable
        private static class HashEntry<V> {
           int key;         // the key
           V value;       // the value
           boolean  isActive;  // false if deleted
      
      	       @SuppressWarnings("unused")
           HashEntry(int k, V val) {
               this( k, val, true );
           }

           HashEntry(int k, V val, boolean i) {
               key = k;
    	       value = val;
               isActive  = i;
           }
        }        
    }
    
    private static class InverseColorMap {
    	private int bitsReserved;// Number of bits used in color quantization.
    	private int bitsDiscarded;// Number of discarded bits
    	private int maxColorVal;// Maximum value for each quantized color
    	private int invMapLen;// Length of the inverse color map
    	// The inverse color map itself
    	private byte[] invColorMap;
    	
    	// Default constructor using 5 for quantization bits
    	public InverseColorMap() {
    		this(5);
    	}
    	
        // Constructor using bitsReserved bits for quantization
    	public InverseColorMap(int rbits) {
    		bitsReserved = rbits;
    		bitsDiscarded  = 8 - bitsReserved;
    		maxColorVal = 1 << bitsReserved;
    		invMapLen = maxColorVal * maxColorVal * maxColorVal;
            invColorMap = new byte[invMapLen];
    	}
    	
       	// Fetch the forward color map index for this RGB 
    	public int getNearestColorIndex(int red, int green, int blue) {
    		return invColorMap[(((red >> bitsDiscarded) << (bitsReserved<<1))) | 
    					 ((green >> bitsDiscarded) << bitsReserved) |
    					 (blue >> bitsDiscarded)]&0xff;
    	}
    	
       	/**
    	 * Create an inverse color map using the input forward RGB map.
    	 */
    	public void createInverseMap(int no_of_colors, int[] colorPalette) {   
    		int red, green, blue, r, g, b;
            int rdist, gdist, bdist, dist;
            int rinc, ginc, binc;

    		int x = (1 << bitsDiscarded);// Step size for each color
    		int xsqr = (1 << (bitsDiscarded + bitsDiscarded));
            int txsqr = xsqr + xsqr;
    		int buf_index;

            int[] dist_buf = new int[invMapLen];
    		
    		// Initialize the distance buffer array with the largest integer value
    		for (int i = invMapLen; --i >= 0;)
    			dist_buf[i] = 0x7FFFFFFF;
            // Now loop through all the colors in the color map
    		for (int i = 0; i < no_of_colors; i++)
    		{
    			red   = ((colorPalette[i]>>16)&0xff);
    			green = ((colorPalette[i]>>8)&0xff);
    			blue  = (colorPalette[i]&0xff);
    			/**
    			 * We start from the origin (0,0,0) of the quantized colors, calculate
    			 * the distance between the cell center of the quantized colors and
    			 * the current color map entry as follows:
    			 * (rcenter * x + x/2) - red, where rcenter is the center of the 
    			 * Quantized red color map entry which is 0 since we start from 0.
    			 */
    	        rdist = (x>>1) - red;// Red distance
    	        gdist = (x>>1) - green;// Green distance
    	        bdist = (x>>1) - blue;// Blue distance
    	        dist = rdist*rdist + gdist*gdist + bdist*bdist;//The modular
                // The distance increment with each step value x
    	        rinc = txsqr - (red   << (bitsDiscarded + 1));
    	        ginc = txsqr - (green << (bitsDiscarded + 1));
                binc = txsqr - (blue  << (bitsDiscarded + 1));

    			buf_index = 0;
    			// Loop through quantized RGB space
    			for (r = 0, rdist = dist; r < maxColorVal; rdist += rinc, rinc += txsqr, r++ )
    			{
    				for (g = 0, gdist = rdist; g < maxColorVal; gdist += ginc, ginc += txsqr, g++)
    				{
    					for (b = 0, bdist = gdist; b < maxColorVal; bdist += binc, binc += txsqr, buf_index++, b++)
    					{
    						if (bdist < dist_buf[buf_index])
    						{
    							dist_buf[buf_index] = bdist;
    							invColorMap[buf_index] = (byte)i;
    						}
    					}
    				}
    			}
    		}
    	}    	
    }
}