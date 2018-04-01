package com.amazonaws.samples;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class executor {
	static List<Data> dataList = new ArrayList<Data>();
	
	public static void main(String[] args) {
		String[] arr;
		arr = new String[] {"J1.jpg", "J2.jpg", "J3.jpg", "J4.jpg", "J5.jpg"};
		for (int i = 0; i < arr.length; i++){
			CompareFacesExample cfe = new CompareFacesExample("portrait.jpg", arr[i]);
			String name = cfe.sourcePath.substring(0, cfe.sourcePath.indexOf('.'));
			Data data = null;
			boolean contains = false;
			for (Data d : dataList){
				if (d.name.equals(name)){
					data = d;
					contains = true;
				}
			}
			if (!contains)
				data = new Data(name);
			try {
				cfe.main(null);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if (cfe.matched) {
				dataList.add(data);
				data.addTarget(cfe.targetPath.substring(0, cfe.targetPath.indexOf('.')));
				BufferedImage img = null;
				try {
					img = ImageIO.read(new File(
							"C:\\Users\\hp\\git\\Amazon\\Amazon\\src\\com\\amazonaws\\samples\\" + cfe.targetPath));
				} catch (IOException e) {
					e.printStackTrace();
				}
				boolean flag = false;
				int Left = (int) (img.getWidth() * (cfe.left - 0.1));
				int Top = (int) (img.getHeight() * (cfe.top - 0.1));
				if (Left < img.getMinX() || Top < img.getMinY()){
					Left = (int) (img.getWidth() * (cfe.left));
					Top = (int) (img.getHeight() * (cfe.top));
					img = img.getSubimage((int) (Left), (int) (Top), (int) (img.getWidth() * (cfe.width)),
							(int) (img.getHeight() * (cfe.height)));
				}
				else{
					img = img.getSubimage((int) (Left), (int) (Top), (int) (img.getWidth() * (cfe.width+0.15)),
							(int) (img.getHeight() * (cfe.height+0.15)));
				}
				try {
					File outputfile = new File("out.png");
					ImageIO.write(img, "png", outputfile);
				} catch (IOException e) {
					e.printStackTrace();
				}
				DetectFacesExample dfe = new DetectFacesExample(data);
				try {
					dfe.main(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
			for (Data d : dataList){
				d.toFile();
			}
	}
}
