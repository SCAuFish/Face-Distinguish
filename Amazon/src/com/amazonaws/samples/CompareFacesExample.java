/**
 * @author UCSD hacker team
 * @time 2018-3-31
 * This is a class adapted from a amazon sample face comparison program.
 * It can detect human faces from local files and compare the sourceImage and the targetImage
 * for the similarity between human faces in different images. 
 */

package com.amazonaws.samples;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Landmark;
import com.amazonaws.util.IOUtils;
import com.amazonaws.services.rekognition.model.BoundingBox;
import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.rekognition.model.ComparedFace;
import com.amazonaws.services.rekognition.model.ComparedSourceImageFace;


public class CompareFacesExample {
	static String source, target;
	static Student student;
	public CompareFacesExample(Student s, String photo){
		this.source = s.portrait;
		this.target = photo;
		this.student = s;
	}

   public static void main(String[] args) throws Exception{
       Float similarityThreshold = 70F;
       String location = "C:\\Users\\hp\\Amazon\\Amazon\\src\\com\\amazonaws\\samples\\";
       String sourceImage = location + source;
       String targetImage = location + target;
       ByteBuffer sourceImageBytes=null;
       ByteBuffer targetImageBytes=null;


       AWSCredentials credentials;
       try {
           credentials = new ProfileCredentialsProvider("user2").getCredentials();
       } catch (Exception e) {
           throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
                   + "Please make sure that your credentials file is at the correct "
                   + "location (/Users/userid/.aws/credentials), and is in valid format.", e);
       }

       EndpointConfiguration endpoint=new EndpointConfiguration("rekognition.us-east-1.amazonaws.com",
              "us-east-1");

        AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder
               .standard()
               .withEndpointConfiguration(endpoint)
               .withCredentials(new AWSStaticCredentialsProvider(credentials))
               .build();


       //Load source and target images and create input parameters
       try (InputStream inputStream = new FileInputStream(new File(sourceImage))) {
          sourceImageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
       }
       catch(Exception e)
       {
           System.out.println("Failed to load source image " + sourceImage);
           System.exit(1);
       }
       try (InputStream inputStream = new FileInputStream(new File(targetImage))) {
           targetImageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
       }
       catch(Exception e)
       {
           System.out.println("Failed to load target images: " + targetImage);
           System.exit(1);
       }

       Image source=new Image()
       		.withBytes(sourceImageBytes);
       Image target=new Image()
       		.withBytes(targetImageBytes);

       CompareFacesRequest request = new CompareFacesRequest()
               .withSourceImage(source)
               .withTargetImage(target)
               .withSimilarityThreshold(similarityThreshold);

       // Call operation
       CompareFacesResult compareFacesResult=rekognitionClient.compareFaces(request);


       // Display results
       List <CompareFacesMatch> faceDetails = compareFacesResult.getFaceMatches();
       ComparedSourceImageFace sourceFace = compareFacesResult.getSourceImageFace();
       List <ComparedFace> allFaces = new ArrayList<ComparedFace>(compareFacesResult.getUnmatchedFaces());
       for (CompareFacesMatch match : compareFacesResult.getFaceMatches()){
    	   allFaces.add(match.getFace());
       }
       List <ComparedFace> sorted = sortByPos(allFaces);
       
       // Print the info of sourceImage
       System.out.println("We're " + sourceFace.getConfidence() 
       							   + "% confident that the sourceImage contains a face\n"
       							   + "Bouding Box: \n"
       							   + "	Height: " + sourceFace.getBoundingBox().getHeight() + "\n"
       							   + "	Width : " + sourceFace.getBoundingBox().getWidth() + "\n"
       							   + "	Left  : " + sourceFace.getBoundingBox().getLeft() + "\n"
       							   + "	Top   : " + sourceFace.getBoundingBox().getTop() + "\n");
       // Print the info of match faces
       for (CompareFacesMatch match: faceDetails){
       	ComparedFace face= match.getFace();
       	BoundingBox position = face.getBoundingBox();
       	Float nosePosX = null;
       	Float nosePosY = null;
       	for (Landmark l : face.getLandmarks()){
       		if (l.toString().contains("nose")){
       			nosePosX = l.getX();
       			nosePosY = l.getY();
       		}
       	}
       	System.out.println("Face" + (sorted.indexOf(face) + 1) + " at " + position.getLeft().toString()
       			+ " " + position.getTop()
       			+ " matches with " + face.getConfidence().toString()
       			+ "% confidence.\n"
       			+ "\"Similarity\" :" + match.getSimilarity() + "\n"
       			+ "Position of nose: \n"
       			+ "	X: " + nosePosX + "\n"
       			+ "	Y: " + nosePosY + "\n");
       }
       // Print the number of faces that don't match
       List<ComparedFace> uncompared = compareFacesResult.getUnmatchedFaces();

       System.out.println("There were " + uncompared.size()
       		+ " faces that did not match\n");
       for (ComparedFace f : uncompared){
    	   
    	   BoundingBox position = f.getBoundingBox();
    	   Float nosePosX = null;
    	   Float nosePosY = null;
    	   for (Landmark l : f.getLandmarks()){
    		   if (l.toString().contains("nose")){
    			   nosePosX = l.getX();
    			   nosePosY = l.getY();
    		   }
    	   }
    	   System.out.println("Face" + (sorted.indexOf(f) + 1) + " at " + position.getLeft().toString()
    			   + " " + position.getTop()
    			   + " doesn't match with sourceImage \nbecause "
    			   + "similarity is less than " + similarityThreshold + "\n"
    			   + "Position of nose: \n"
    			   + "	X: " + nosePosX + "\n"
    			   + "	Y: " + nosePosY + "\n");
       }
       System.out.println("Source image rotation: " + compareFacesResult.getSourceImageOrientationCorrection());
       System.out.println("target image rotation: " + compareFacesResult.getTargetImageOrientationCorrection());
   }
   
   private static List<ComparedFace> sortByPos(List<ComparedFace> lst){
	   List<ComparedFace> sorted = new ArrayList<ComparedFace>();
	   float minLeft; 
	   int minIndex;
	   while (lst.size() > 0){
		   minLeft = lst.get(0).getBoundingBox().getLeft();
		   minIndex = 0;
		   for (int i = 0; i < lst.size(); i++){
			   if (lst.get(i).getBoundingBox().getLeft() < minLeft){
				   minLeft = lst.get(i).getBoundingBox().getLeft();
				   minIndex = i;
			   }
		   }
		   sorted.add(lst.remove(minIndex));
	   }
	   return sorted;
   }
}
