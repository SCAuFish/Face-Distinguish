#Face-Distinguish

This program is able to detect and compare figures in a picture taken with the people stored in a local base, by making use of Amazon Web Service API.

###Preparing the development environment 

Sign up for an AWS account  and create an Administrator User.

Python 2.7+ and Pip are needed. 

Use Pip to install AWS CLI. Configure the AWS CLI. 

Make sure you choose a region where all of the above services are available. Regions us-east-1(N.Virginia), us-west-2(Oregon), and eu-west-1(Ireland) fulfill this criterion.

For more instruction, please refer to this website. 

###Running the tests

The test case has already been coded into the file, so just compile and run to see the result of the test case. The test case is comprised of five images with one common person in every image, with one image to be the source image. The program will then find the faces in the other four images that match with the one in the source image. This test case is used to see whether the program can compare faces correctly—whether it correctly finds all the matched faces in the five images. Besides, we extract the attributes of the faces to test the function of the program to detect faces.

###Deployment

To use this program, we need at least one source image and at least one target image. The program will identify the largest face in the source image and looks for that face in the target image. Then a list of matched faces and a list of unmatched faces will be generated. 




