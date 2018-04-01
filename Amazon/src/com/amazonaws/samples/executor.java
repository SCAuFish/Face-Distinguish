package com.amazonaws.samples;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class executor {
	public static void main(String[] args){
		CompareFacesExample cfe = new CompareFacesExample("paul.png", "paul2.png");
		try {
			cfe.main(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if (cfe.matched){
			BufferedImage img = null;
			try {
				img = ImageIO.read(new File("C:\\Users\\hp\\git\\Amazon\\Amazon\\src\\com\\amazonaws\\samples\\" + 
											 cfe.targetPath));
			} catch (IOException e) {
		        e.printStackTrace();
			}
			img = img.getSubimage((int) (img.getWidth() * cfe.left - 20),
								  (int) (img.getHeight() * cfe.top - 20),
								  (int) (img.getWidth() * cfe.width + 25),
								  (int) (img.getHeight() * cfe.height + 25));
			try{
				File outputfile = new File("out.png");
				ImageIO.write(img, "png", outputfile);
			}catch (IOException e) {
		        e.printStackTrace();
			}
			DetectFacesExample dfe = new DetectFacesExample();
			try {
				//dfe.main(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
