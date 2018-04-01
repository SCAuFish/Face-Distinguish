import android.hardware.camera2;

public class ShashinToru{
    private static final String savePath;

    public ShashinToru(String savePath){
        try{
            this.savePath= getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        }
        finally{}
    }

    /*
     * This method takes pictures and save it to a private directory: 
     * authorization required
     * see:https://developer.android.com/training/camera/photobasics.html
     */
    private void takeAndSave(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Ensure there's a camera
        if(takePictureIntent.resolveActivity(getPackageManager())!=null){
            File photoFile = null;
            try{
                photoFile = createImageFile();
            }catch (IOException e){
                //Error message, maybe print out
                exit();
            }
          if(photoFile != null){
            Uri photoURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider",photoFile);
            takePictureIntent.putExtra(MediaStore.Extra_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
          }
        }
    }

    /*This method creates unique directory for each image
     */
    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+timeStamp;
        File image = File.createTempFile(imageFileName, ".jpg", this.savePath);
        
        return image;
    }
}
