This program implements K Means Clustering Technique to compress the images.

In a colored image, each pixel is of size 3 bytes (RGB), where each color can have intensity values from 0 to 255. 
Following combinatorics, the total number of colors which can be represented are 256*256*256. 
Practically, we are able to visualize only a few colors in an image. 

How K Means clustering technique compresses the image file: 

1)	First it selects K random number of pixel points from available pixels of Image. 
2)	Then it will find the nearest pixels points to each of those K points. 
3)	Then it will assign the of pixel point to its nearest cluster centroid and creating K clusters. 
4)	Now it will find new centers of those clusters by taking average of each points pixel values assigned to clusters.
5)	Repeat the process from step 2 until no cluster centers are changing. 

This will reduce the number of unique colors in the image resulting in the reduction of the size of image. 

To run this program KMeans.java
Compile the program 

command : javac KMeans.java

To run program

command : java KMeans "<Path of image>/<imageName>.jpg" "<value of K>" "<path of destination image>/<imagename>.jpg"
example : java KMeans "Koala.jpg" "20" "Koala_Compressed.jpg"
