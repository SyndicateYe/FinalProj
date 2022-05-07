package com.ece420.lab7;


import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.TextView;

public class ImageActivity extends AppCompatActivity {

    // UI Variable
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private SurfaceView surfaceView2;
    private SurfaceHolder surfaceHolder2;
    private TextView textHelper;
    // Camera Variable
//    private Camera camera;
//    boolean previewing = false;
//    private int width = 640;
//    private int height = 480;
//    // Kernels
//    private double[][] kernelS = new double[][] {{-1,-1,-1},{-1,9,-1},{-1,-1,-1}};
//    private double[][] kernelX = new double[][] {{1,0,-1},{1,0,-1},{1,0,-1}};
//    private double[][] kernelY = new double[][] {{1,1,1},{0,0,0},{-1,-1,-1}};

//    private File photoDirectory;
//    private List<Bitmap> images;
//    private int currentImage;
//
//    // Image view
//    ImageView sImageView;
//    ImageView tImageView;
//    Button choose_source;
//    Button choose_target;
//
//    private static final int IMAGE_PICK_CODE0 = 999;
//    private static final int IMAGE_PICK_CODE = 1000;
//    private static final int PERMISSION_CODE = 1001;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFormat(PixelFormat.UNKNOWN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_camera);
//        super.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

//        // VIEWS
//        sImageView = findViewById(R.id.ViewOrigin_m);
//        tImageView = findViewById(R.id.ViewHisteq_t);

//        // handle button Click
//        choose_source = (Button) findViewById(R.id.choose_source_image);
//        choose_source.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//                //   check runtime permission
//                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
//                        // permission not granted, request
//                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
//                        // popup for runtime permission
//                        requestPermissions(permissions, PERMISSION_CODE);
//                    }
//                    else{
//                        // permission already granted
//                        pickImageFromGallery();
//                    }
//                }
//                else {
//                    // system os is less then marshmallow
//                    pickImageFromGallery();
//                }
//            }
//        });
//
//        choose_target = (Button) findViewById(R.id.choose_target_image);
//        choose_target.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // check runtime permission
//                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
//                        // permission not granted, request
//                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
//                        // popup for runtime permission
//                        requestPermissions(permissions, PERMISSION_CODE);
//                    }
//                    else{
//                        // permission already granted
//                        pickImageFromGallery2();
//                    }
//                }
//                else {
//                    // system os is less then marshmallow
//                    pickImageFromGallery2();
//                }
//            }
//        });
//
//
//        //super.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
//
////        // Modify UI Text
////        textHelper = (TextView) findViewById(R.id.Helper);
////        if(MainActivity.maskFlag == 1) textHelper.setText("Poisson Editing");
////        // Setup Surface View handler
////        surfaceView = (SurfaceView)findViewById(R.id.ViewOrigin);
////        surfaceHolder = surfaceView.getHolder();
////        surfaceHolder.addCallback(this);
////        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
////        surfaceView2 = (SurfaceView)findViewById(R.id.ViewHisteq);
////        surfaceHolder2 = surfaceView2.getHolder();
//
//        // create a list to store the images for further use
//
////        // photoDirectory
////        photoDirectory = new File(Environment.getExternalStorageDirectory().toString(), "Poisson-photos");
////        photoDirectory.mkdirs();
////
////        // Access all photos and put in into a list
////        images = new ArrayList<Bitmap>();
////
////        for (File file: photoDirectory.listFiles()){
////            if(file.isFile()){
////                try{
////                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), Uri.fromFile(file));
////                    images.add(bitmap);
////                } catch (FileNotFoundException e) {
////                    e.printStackTrace();
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////            }
////        }
////
////        if(images.size()>0){
////            // display the image
////            currentImage = 0;
////            displayCurrentImage();
////        }
////        else{
////            // if no image then do not display
////            currentImage = -1;
////        }
//
//


    }

//    private void pickImageFromGallery() {
//        // intent to pick image
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        startActivityForResult(intent, IMAGE_PICK_CODE);
//    }
//
//    private void pickImageFromGallery2() {
//        // intent to pick image
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        startActivityForResult(intent, IMAGE_PICK_CODE0);
//    }

    // handle result from runtime permission


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode){
//            case PERMISSION_CODE:
//                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    // permission was granted
//                    pickImageFromGallery();
//                }
//                else {
//                    // permission was denied
//                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
//                }
//        }
//    }

    // handle result of picked images

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if(resultCode == RESULT_OK){
//            // set image to iamge view
//            if(requestCode == IMAGE_PICK_CODE) sImageView.setImageURI(data.getData());
//            if(requestCode == IMAGE_PICK_CODE0) tImageView.setImageURI(data.getData());
//        }
//    }

//
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//        // Must have to override native method
//        return;
//    }
//
//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//        if(!previewing) {
//            camera = Camera.open();
//            if (camera != null) {
//                try {
//                    // Modify Camera Settings
//                    Camera.Parameters parameters = camera.getParameters();
//                    parameters.setPreviewSize(width, height);
//                    // Following lines could log possible camera resolutions, including
//                    // 2592x1944;1920x1080;1440x1080;1280x720;640x480;352x288;320x240;176x144;
//                    // List<Camera.Size> sizes = parameters.getSupportedPictureSizes();
//                    // for(int i=0; i<sizes.size(); i++) {
//                    //     int height = sizes.get(i).height;
//                    //     int width = sizes.get(i).width;
//                    //     Log.d("size: ", Integer.toString(width) + ";" + Integer.toString(height));
//                    // }
//                    camera.setParameters(parameters);
//                    camera.setDisplayOrientation(90);
//                    camera.setPreviewDisplay(surfaceHolder);
//                    camera.setPreviewCallback(new PreviewCallback() {
//                        public void onPreviewFrame(byte[] data, Camera camera)
//                        {
//                            // Lock canvas
//                            Canvas canvas = surfaceHolder2.lockCanvas(null);
//                            // Where Callback Happens, camera preview frame ready
//                            onCameraFrame(canvas,data);
//                            // Unlock canvas
//                            surfaceHolder2.unlockCanvasAndPost(canvas);
//                        }
//                    });
//                    camera.startPreview();
//                    previewing = true;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//        // Cleaning Up
//        if (camera != null && previewing) {
//            camera.stopPreview();
//            camera.setPreviewCallback(null);
//            camera.release();
//            camera = null;
//            previewing = false;
//        }
//    }
//
//    protected void onCameraFrame(Canvas canvas, byte[] data) {
//
//        Matrix matrix = new Matrix();
//        matrix.postRotate(90);
//        int retData[] = new int[width * height];
//
//        // Apply different processing methods
//        if(MainActivity.appFlag == 1){
//            byte[] histeqData = histEq(data, width, height);
//            retData = yuv2rgb(histeqData);
//        }
//        else if (MainActivity.appFlag == 2){
//
//            int[] sharpData = conv2(data, width, height, kernelS);
//            retData = merge(sharpData, sharpData);
//        }
//        else if (MainActivity.appFlag == 3){
//            int[] xData = conv2(data, width, height, kernelX);
//            int[] yData = conv2(data, width, height, kernelY);
//            retData = merge(xData, yData);
//        }
//
//        // Create ARGB Image, rotate and draw
//        Bitmap bmp = Bitmap.createBitmap(retData, width, height, Bitmap.Config.ARGB_8888);
//        bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
//        canvas.drawBitmap(bmp, new Rect(0,0, height, width), new Rect(0,0, canvas.getWidth(), canvas.getHeight()),null);
//    }
//
//    // Helper function to convert YUV to RGB
//    public int[] yuv2rgb(byte[] data){
//        final int frameSize = width * height;
//        int[] rgb = new int[frameSize];
//
//        for (int j = 0, yp = 0; j < height; j++) {
//            int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
//            for (int i = 0; i < width; i++, yp++) {
//                int y = (0xff & ((int) data[yp])) - 16;
//                y = y<0? 0:y;
//
//                if ((i & 1) == 0) {
//                    v = (0xff & data[uvp++]) - 128;
//                    u = (0xff & data[uvp++]) - 128;
//                }
//
//                int y1192 = 1192 * y;
//                int r = (y1192 + 1634 * v);
//                int g = (y1192 - 833 * v - 400 * u);
//                int b = (y1192 + 2066 * u);
//
//                r = r<0? 0:r;
//                r = r>262143? 262143:r;
//                g = g<0? 0:g;
//                g = g>262143? 262143:g;
//                b = b<0? 0:b;
//                b = b>262143? 262143:b;
//
//                rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
//            }
//        }
//        return rgb;
//    }
//
//    // Helper function to merge the results and convert GrayScale to RGB
//    public int[] merge(int[] xdata,int[] ydata){
//        int size = height * width;
//        int[] mergeData = new int[size];
//        for(int i=0; i<size; i++)
//        {
//            int p = (int)Math.sqrt((xdata[i] * xdata[i] + ydata[i] * ydata[i]) / 2);
//            mergeData[i] = 0xff000000 | p<<16 | p<<8 | p;
//        }
//        return mergeData;
//    }
//
//    // Function for Histogram Equalization
//    public byte[] histEq(byte[] data, int width, int height){
//        byte[] histeqData = new byte[data.length];
//        int size = height * width;
//
//        // Perform Histogram Equalization
//        // Note that you only need to manipulate data[0:size] that corresponds to luminance
//        // The rest data[size:data.length] is for colorness that we handle for you
//        // *********************** START YOUR CODE HERE  **************************** //
//        int [] hist = new int[256];
//        int [] temp = new int[size];
//        int index = -1;
//        for(int i=0; i<size; i++){
//            if((int)data[i]<0) temp[i] = ((int)data[i])+256;
//            else temp[i] = data[i];
//            hist[temp[i]]++;
//        }
//        for(int i=0; i<256; i++){
//            if(i > 0) hist[i] += hist[i-1];
//            if(index<0 && hist[i]>0) index = i;
//        }
//        for(int i=0; i<size; i++){
//            int l = (hist[temp[i]]-hist[index])*255/(size-1);
//            if(l >= 128) l-=256;
//            histeqData[temp[i]] = (byte)(l);
//        }
//
//        // *********************** End YOUR CODE HERE  **************************** //
//        // We copy the colorness part for you, do not modify if you want rgb images
//        for(int i=size; i<data.length; i++){
//            histeqData[i] = data[i];
//        }
//        return histeqData;
//    }
//
//    public int[] conv2(byte[] data, int width, int height, double kernel[][]){
//        // 0 is black and 255 is white.
//        int size = height * width;
//        int[] convData = new int[size];
//
//        // Perform single channel 2D Convolution
//        // Note that you only need to manipulate data[0:size] that corresponds to luminance
//        // The rest data[size:data.length] is ignored since we only want grayscale output
//        // *********************** START YOUR CODE HERE  **************************** //
//        for(int y=0; y<height; y++){
//            for(int x=0; x<width; x++){
//                double sum=0;
//                for(int i=-1; i<2; i++){
//                    for(int j=-1; j<2; j++){
//                        if(y+i>=0 && y+i<height && x+j>=0 && x+j<=width){
//                            sum += kernel[i+1][j+1]*((double)data[(y+i)*width + x+j]);
//                        }
//                    }
//                }
//                convData[y*width+x] = (byte) sum;
//            }
//        }
//
//
//        // *********************** End YOUR CODE HERE  **************************** //
//        return convData;
//    }
}
