package com.amazonaws.samples;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

import com.amazonaws.services.rekognition.model.AgeRange;
import com.amazonaws.services.rekognition.model.Emotion;
import com.amazonaws.services.rekognition.model.EyeOpen;
import com.amazonaws.services.rekognition.model.MouthOpen;
import com.amazonaws.services.rekognition.model.Smile;

public class Data { 
	String name;
	List<Float> emotions = new ArrayList<Float>();
	List<Integer> age = new ArrayList<Integer>();
	List<Boolean> mouthOpen = new ArrayList<Boolean>();
	List<Boolean> eyesOpen = new ArrayList<Boolean>();
	List<Boolean> smile = new ArrayList<Boolean>();
	List<String> targets = new ArrayList<String>();

	public Data(String name) {
		this.name = name;
	}

	/*public void addEmotion(List<Emotion> list) {
		float max = 0;
		Emotion em = null;
		for (Emotion e : list) {
			if (e.getConfidence() > max) {
				max = e.getConfidence();
				em = e;
			}
		}
		if (em.getType().equals("HAPPY") || em.getType().equals("SUPRISED"))
			emotions.add(em.getConfidence());
		else if (em.getType().equals("CALM"))
			emotions.add(0f);
		else
			emotions.add(-em.getConfidence());
	}*/
	
	public void addZhuIndex(List<Emotion> list){
		double zhuIndex;
        	double scaledZhuIndex;
		for(Emotion e: list){
			if(e.getType().equals("HAPPY")){
				zhuIndex+=e.getConfidence();
			}
			if(e.getType().equals("SURPRISE"){
				double surpriseRatio=0.8;
				zhuIndex+=e.getConfidence()*surpriseRatio;
			}
		        if(e.getType().equals("CALM")||e.getType().equals("UNKNOWN")){
				double calmRatio = 0;
				zhuIndex+=e.getConfidence()*calmRatio;
			}
			if(e.getType().equals("CONFUSED")){
                		double confusedRatio = -.1;
                		zhuIndex+=e.getConfidence()*confusedRatio;
            		}
            		if(e.getType().equals("SAD")||e.getType().equals("ANGRY")){
                		double sadRatio = -1;
                		zhuIndex+=e.getConfidence()*sadRatio;
            		}
            		if(e.getType().equals("DISGUSTED")){
                		double disgustedRatio = -1.2;
                		zhuIndex+=e.getConfidence()*disgusteRatio;
            		}
		}

        	scaledZhuIndex = Math.atan(zhuIndex)/(Math.PI/2);
		this.emotions.add((float)scaledZhuIndex);
	}


	public void addAge(AgeRange ageRange) {
		if (ageRange != null) {
			int high = ageRange.getHigh();
			int low = ageRange.getLow();
			age.add((high + low) / 2);
		}
	}

	public void addMouthOpen(MouthOpen mthOpen) {
		if (mthOpen != null) {
			mouthOpen.add(mthOpen.getValue());
		}
	}

	public void addEyesOpen(EyeOpen eyeOpen) {
		if (eyeOpen != null)
			eyesOpen.add(eyeOpen.getValue());
	}

	public void addSmile(Smile smi) {
		if (smi != null)
			smile.add(smi.getValue());
	}
	
	public void addTarget(String target){
		if (target != null){
			targets.add(target);
		}
	}

	public void toFile() {
		String out = "Name: " + this.name + "\n";
		out += "The targetImages are: \n";
		for (int i = 0; i < targets.size(); i++){
			out += targets.get(i) + "\n";
		}
		out += "The emotions levels are: \n";
		for (int i = 0; i < emotions.size(); i++) {
			out += emotions.get(i) + "\n";
		}
		out += "The estimated ages by computer are: \n";
		for (int i = 0; i < age.size(); i++) {
			out += age.get(i) + "\n";
		}
		out += "The mouthOpen conditions are: \n";
		for (int i = 0; i < mouthOpen.size(); i++) {
			out += mouthOpen.get(i) + "\n";
		}
		out += "The eyesOpen conditions are: \n";
		for (int i = 0; i < eyesOpen.size(); i++) {
			out += eyesOpen.get(i) + "\n";
		}
		out += "The smile conditions are: \n";
		for (int i = 0; i < smile.size(); i++) {
			out += smile.get(i) + "\n";
		}
		try {
			Files.write(Paths.get("C:\\Users\\hp\\Desktop\\out.txt"), out.getBytes());
			System.out.println("Data written to Desktop\\out.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
