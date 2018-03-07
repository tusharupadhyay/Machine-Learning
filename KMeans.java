/* Author : Tushar Upadhyay
 * Project : Machine Learning Assignment Spring 2015
*/


import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel; 

public class KMeans {
    public static void main(String [] args){
	if (args.length < 3){
	    System.out.println("Usage: Kmeans <input-image> <k> <output-image>");
	    return;
	}
	double compressedFileSize [] = new double [10];
	double compressionRatio [] = new double [10];
	double variance [] = new double [10];
	for(int i=0; i<10; i++)
	{
		compressedFileSize[i]=(long) 0.0;
		compressionRatio[i] = 0.0;
	}
	double sizeOfOriginalImage;
	try{
	    BufferedImage originalImage = ImageIO.read(new File(args[0]));
	    sizeOfOriginalImage = new File(args[0]).length();
	    int k=Integer.parseInt(args[1]);
	    
	    for(int i=0; i<10; i++)
	    {
	    	BufferedImage kmeansJpg = kmeans_helper(originalImage,k);
	    	ImageIO.write(kmeansJpg, "jpg", new File(args[2]));
	    	compressedFileSize[i] = new File(args[2]).length();
	    	
	    	//Calculating Compression ratio
	    	compressionRatio[i] = sizeOfOriginalImage/compressedFileSize[i];
	    }
	    
	    double mean;
	    mean = 0.0;
	    for(int i=0; i<10; i++)
	    {
	    	mean += compressionRatio[i];
	    }
	    mean = mean/10.0;
	    for(int i=0; i<10; i++)
	    {
	    	double sum = compressionRatio[i] - mean;
	    	variance[i] = sum * sum;
	    }
	    
	    System.out.println("Compression Ratio Mean : " + mean + " byte");
	    for(int i=0; i<10; i++)
	    {
	    		System.out.println("Variance for iteration " + i + " : " + variance[i]);
	    }
	    
	    System.out.println(" \n\n\n");
	    for(int i=0; i<10; i++)
	    {
	    		System.out.println("Compression ratio for iteration " + i + " : " + compressionRatio[i]);
	    }
	    
	    	    
	    
	}catch(IOException e){
	    System.out.println(e.getMessage());
	}	
    }
    
    /* This function reads original Image, extracts RGB value from it 
     * and calls K-Means algorithm to find the compressed Image.
     */
    private static BufferedImage kmeans_helper(BufferedImage originalImage, int k){
	int w=originalImage.getWidth();
	int h=originalImage.getHeight();
	BufferedImage kmeansImage = new BufferedImage(w,h,originalImage.getType());
	
	// Drawing the original Image
	Graphics2D g = kmeansImage.createGraphics();
	g.drawImage(originalImage, 0, 0, w,h , null);
	
	// Read rgb values from the image
	int[] rgb=new int[w*h];
	int count=0;
	for(int i=0;i<w;i++){
	    for(int j=0;j<h;j++){
		rgb[count++]=kmeansImage.getRGB(i,j);
	    }
	}
	// Call kmeans algorithm: update the rgb values
	
		kmeans(rgb,k);
	
	// Write the new rgb values to the image
	count=0;
	for(int i=0;i<w;i++){
	    for(int j=0;j<h;j++){
		kmeansImage.setRGB(i,j,rgb[count++]);
	    }
	}
	return kmeansImage;
    }
    
    /* Checks if the new cluster center points are same as previous.
     * If they are same as previous, it means no points in clusters are going to change their assignments.
     * Hence the Cluster is converged. 
     */
  
    public static boolean converge(int[] oldCenter, int[] newCenter)
    {
    	for(int i = 0; i < oldCenter.length; i++)
    	{
    		if(oldCenter[i] != newCenter[i])
    			return false;
    	}
    	return true;
    }

    //########################################### K Means Algoritham #############################################
    // Update the array rgb by assigning each entry in the rgb array to its cluster center
    private static void kmeans(int[] rgb, int k)
    {
    	int[] newCenter = new int[k];
    	int[] oldCenter = new int[k];
    	
    	// Choosing new center points for clusters
    	for(int i = 0; i < newCenter.length; i++)
    	{
    		Random rnd = new Random();
    		newCenter[i] = rgb[rnd.nextInt(rgb.length)];
    		//System.out.println(newCenter[i]);
    	}
    	
    	// arrays to store Alpha,Red,Green, Blue and total pixel value of Image for new center points
    	int[] totalAlpha = new int[k];
    	int[] totalRed = new int[k];
    	int[] totalGreen = new int[k];
    	int[] totalBlue = new int[k];
    	int[] totalPixel = new int[k];
    	
    	// Array to store new compressed rgb value of original image 
    	int[] assignment = new int[rgb.length];
    	
    	int nearest = 0;
    	double d = 0;
    	double maxD = 0;
    	
    	// Execute the process until new center points has been seen before
    	while(!converge(oldCenter,newCenter))
    	{
    		// initiating values and storing the current center points to old center points array 
    		for(int i = 0; i < newCenter.length; i++)
    		{
    			oldCenter[i] = newCenter[i];
    			totalAlpha[i] = 0;
    			totalRed[i] = 0;
    			totalGreen[i] = 0;
    			totalBlue[i] = 0;
    			totalPixel[i] = 0;
    		}
    		
    		
    		for ( int i = 0; i < rgb.length; i++ ) 
	    	{
	    		// Finding nearest pixels from centroids and save their corresponding Alpha, red, green and blue values
    			maxD = Double.MAX_VALUE;
	    		
	    		for ( int j = 0; j < newCenter.length; j++ ) 
	    		{
	    			int alphaDiff = ((rgb[i] & 0xFF000000) >>> 24) - ((newCenter[j] & 0xFF000000) >>> 24);
	    		    int redDiff = ((rgb[i] & 0x00FF0000) >>> 16) - ((newCenter[j] & 0x00FF0000) >>> 16);
	    		    int greenDiff = ((rgb[i] & 0x0000FF00) >>> 8)  - ((newCenter[j] & 0x0000FF00) >>> 8);
	    		    int blueDiff = ((rgb[i] & 0x000000FF) >>> 0)  - ((newCenter[j] & 0x000000FF) >>> 0);
	    		    
	    		    // Calculating Euclidean distance
	    		    d = Math.sqrt( alphaDiff*alphaDiff + redDiff*redDiff + greenDiff*greenDiff + blueDiff*blueDiff );
	    		    
	    			if ( d < maxD ) 
	    			{
	    				maxD = d;
	    				// Saving cluster point as nearest
	    				nearest = j;
	    			}
	    		}
	    		/* Here assignment is same size as rgb. saving nearest value(pixel value) in the ith location in assignment
	    		 * is task of assigning same pixel value to the nearest neighbors of centroid j.
	    		 */
		        assignment[i] = nearest;
		        totalAlpha[nearest] = totalAlpha[nearest] + ((rgb[i] & 0xFF000000) >>> 24);
		        totalRed[nearest] = totalRed[nearest] + ((rgb[i] & 0x00FF0000) >>> 16);
		        totalGreen[nearest] = totalGreen[nearest] + ((rgb[i] & 0x0000FF00) >>> 8);
		        totalBlue[nearest] = totalBlue[nearest] + ((rgb[i] & 0x000000FF) >>> 0);
		        totalPixel[nearest]++;
	    	}
    		
    		
    		for ( int i = 0; i < newCenter.length; i++ ) 
	    	{
    			// Calculating the average of Alpha,Red,Green and blue value of K Clusters
		        int avgAlpha = (int)((double)totalAlpha[i] / (double)totalPixel[i]);
		        int avgRed =   (int)((double)totalRed[i] /   (double)totalPixel[i]);
		        int avgGreen = (int)((double)totalGreen[i] / (double)totalPixel[i]);
		        int avgBlue =  (int)((double)totalBlue[i] /  (double)totalPixel[i]);
		        
		        // Calculating new centroids of clusters by taking average of data points available in clusters
		        newCenter[i] = ((avgAlpha & 0x000000FF) << 24) |
		                            ((avgRed & 0x000000FF) << 16) |
		                            ((avgGreen & 0x000000FF) << 8) |
		                            ((avgBlue & 0x000000FF) << 0);
	    	}
    		
    		// Update the rgb values of the input array by new values
    		for ( int i = 0; i < rgb.length; i++ ) 
    	    {
    	    	rgb[i] = newCenter[assignment[i]];
    	    }
    	}
    }

}