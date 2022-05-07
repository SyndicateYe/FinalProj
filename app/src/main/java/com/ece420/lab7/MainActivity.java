package com.ece420.lab7;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.content.Intent;
import android.provider.MediaStore;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;
import org.opencv.android.Utils;

import java.io.IOException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static int appFlag;

    private ImageView SourceImage;
    private ImageView TargetImage;
    private ImageView ResultImage;

    private Mat Source;
    private Mat Target;
    private Mat Mask;
    private Mat Result;
    private Bitmap ResultBitmap;

    public static final int PICK_SOURCE = 1;
    public static final int PICK_Target = 2;
    public static final int Apply_Result = 3;
    public static final int MixedApply = 4;
    private Point center;
    Point[] points = {
            new Point(100, 100),
            new Point(100, 140),
            new Point(150, 140),
            new Point(150, 100),
    };

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        super.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // OpenCV Loader and Avoid using OpenCV Manager
        if (!OpenCVLoader.initDebug()) {
            Log.e(this.getClass().getSimpleName(), "  OpenCVLoader.initDebug(), not working.");
        } else {
            Log.d(this.getClass().getSimpleName(), "  OpenCVLoader.initDebug(), working.");
        }

        Button sourceButton = (Button) findViewById(R.id.SourceButton);
        SourceImage = (ImageView) findViewById(R.id.SourceImage);
        sourceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_SOURCE);
            }
        });

        Button targetButton = (Button) findViewById(R.id.TargetButton);
        TargetImage = (ImageView) findViewById(R.id.TargetImage);
        targetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_Target);
            }
        });

        Button applyButton = (Button) findViewById(R.id.ApplyButton);
        ResultImage = (ImageView) findViewById(R.id.ResultImage);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), Apply_Result);
            }
        });

        Button MixedapplyButton = (Button) findViewById(R.id.MixedapplyButton);
        MixedapplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), MixedApply);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_SOURCE) {
            Uri imageUri = data.getData();
            Bitmap Sourcebitmap = null;
            try {
                Sourcebitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            SourceImage.setImageBitmap(Sourcebitmap);
            //SourceImage.setImageURI(data.getData());
            //    SourceImage.buildDrawingCache();
            //  Bitmap SourceBit = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            Source = new Mat(Sourcebitmap.getWidth(), Sourcebitmap.getHeight(), CvType.CV_8UC4);
            Utils.bitmapToMat(Sourcebitmap, Source);
            Imgproc.cvtColor(Source, Source, Imgproc.COLOR_BGR2RGB);
            //   ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            //   Source = Imgcodecs.imread(getResources().getDrawable(R.drawable.source1).toString(),Imgcodecs.CV_LOAD_IMAGE_COLOR);
            //   Source = Imgcodecs.imread(Objects.requireNonNull(data.getData()).toString());
            Log.i(TAG, Source.size().toString());
        }
        else if (requestCode == PICK_Target) {
            Uri imageUri = data.getData();
            Bitmap Targetbitmap = null;
            try {
                Targetbitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            TargetImage.setImageBitmap(Targetbitmap);
            //SourceImage.setImageURI(data.getData());
            //    SourceImage.buildDrawingCache();
            //  Bitmap SourceBit = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            Target = new Mat(Targetbitmap.getWidth(), Targetbitmap.getHeight(), CvType.CV_8UC4);
            Utils.bitmapToMat(Targetbitmap, Target);
            Imgproc.cvtColor(Target, Target, Imgproc.COLOR_BGR2RGB);
            //    Target = Imgcodecs.imread(getResources().getDrawable(R.drawable.reference).toString(),Imgcodecs.CV_LOAD_IMAGE_COLOR);
            //   Source = Imgcodecs.imread(Objects.requireNonNull(data.getData()).toString());
            Log.i(TAG, Target.size().toString());
        }
        else if (requestCode == Apply_Result) {
            //         Mask = Mat.zeros(Source.size(), Source.type());
            //   Mask = Imgcodecs.imread(Objects.requireNonNull(data.getData().toString()));
            Uri imageUri = data.getData();
            Bitmap Maskbitmap = null;
            try {
                Maskbitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //SourceImage.setImageURI(data.getData());
            //    SourceImage.buildDrawingCache();
            //  Bitmap SourceBit = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            Mask = new Mat(Maskbitmap.getWidth(), Maskbitmap.getHeight(), CvType.CV_8UC4);
            Utils.bitmapToMat(Maskbitmap, Mask);
            Imgproc.cvtColor(Mask, Mask, Imgproc.COLOR_BGR2RGB);
            //   Mask = new Mat(Mask.size(), Mask.depth(), new Scalar(255));
            //   Mask = Imgcodecs.imread(getResources().getDrawable(R.drawable.mask).toString(), Imgcodecs.CV_LOAD_IMAGE_COLOR);
            //   Source = Imgcodecs.imread(Objects.requireNonNull(data.getData()).toString());
            Log.i(TAG, "Mask size");
            Log.i(TAG, Mask.size().toString());
            //Imgproc.fillConvexPoly(Mask, new MatOfPoint(points), new Scalar(255, 255, 255));
            //Result = new Mat();
            Result = Mat.zeros(Target.size(), Target.type());
            Point p = new Point();
            Log.i(TAG, "result size");
            Log.i(TAG, Result.size().toString());
            Log.i(TAG, "source size");
            Log.i(TAG, Source.size().toString());
            Log.i(TAG, "target size");
            Log.i(TAG, Target.size().toString());
            p.x = Result.size().width/2;
            p.y = Result.size().height/2;
            //Photo.seamlessClone(Source, Target, Mask, p, Result, Photo.NORMAL_CLONE);
            Result = Poisson_Edit(Source, Target, Mask, p, Result, Photo.NORMAL_CLONE);
//            Mat Result2 = new Mat(Result.rows()/3, Result.cols()/3, CvType.CV_8UC1);
//            Imgproc.resize(Result,Result,Result2.size(), 0,0, Imgproc.INTER_AREA);
            //Result = upSample_Image_l(2, Result);
//            Toast.makeText(MainActivity.this,
//                    Mask.rows() + "and" + Mask.cols() + "and" + Mask.get(1,1).length,
//                    Toast.LENGTH_LONG).show();
            ResultBitmap = Bitmap.createBitmap(Result.cols(),  Result.rows(),Bitmap.Config.ARGB_8888);
            Imgproc.cvtColor(Result, Result, Imgproc.COLOR_BGR2RGB);
            Utils.matToBitmap(Result, ResultBitmap);
            ResultImage.setImageBitmap(ResultBitmap);
        }
        else if (requestCode == MixedApply) {
            Uri imageUri = data.getData();
            Bitmap Maskbitmap = null;
            try {
                Maskbitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Mask = new Mat(Maskbitmap.getWidth(), Maskbitmap.getHeight(), CvType.CV_8UC4);
            Utils.bitmapToMat(Maskbitmap, Mask);
            Imgproc.cvtColor(Mask, Mask, Imgproc.COLOR_BGR2RGB);
            Result = Mat.zeros(Target.size(), Target.type());
            Point p = new Point();
            p.x = Result.size().width/2;
            p.y = Result.size().height/2;
            //Poisson_Edit(Source, Target, Mask, p, Result, Photo.MIXED_CLONE);
            Photo.seamlessClone(Source, Target, Mask, p, Result, Photo.MIXED_CLONE);
            //Result = upSample_Image_l(2, Result);
            ResultBitmap = Bitmap.createBitmap(Result.cols(),  Result.rows(),Bitmap.Config.ARGB_8888);
            Imgproc.cvtColor(Result, Result, Imgproc.COLOR_BGR2RGB);
            Utils.matToBitmap(Result, ResultBitmap);
            ResultImage.setImageBitmap(ResultBitmap);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    private final BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            if (status == LoaderCallbackInterface.SUCCESS) {
                Log.i(TAG, "OpenCV loaded successfully");
            } else {
                super.onManagerConnected(status);
            }
        }
    };

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public static int[][] laplacian_matrix(int n, int m){
        int[][] mat_D = new int[m][m];
        for(int i = 0; i < m - 1; ++i){
            mat_D[i][i+1] = -1;
        }
        for(int i = 0; i < m-1; ++i){
            mat_D[i+1][i] = -1;
        }
        for(int i = 0; i < m; ++i){
            mat_D[i][i] = 4;
        }
        int dim = n*m;
        int[][] mat_A = new int[dim][dim];
        for(int i = 0; i < n; ++i){
            for(int j = 0; j < m; j++){
                for(int k = 0; k < m; k++){
                    mat_A[n*4+j][n*4+k] = mat_D[j][k];
                }
            }
        }
        for(int i = 0; i < dim - m; i++){
            mat_A[i][i+m] = -1;
        }
        for(int i = 0; i < dim - m; i++) {
            mat_A[i + m][i] = -1;
        }
        return mat_A;
    }

    public static Mat Poisson_Edit(Mat src, Mat dst, Mat mask, Point p, Mat blend, int flag) {
        double[][] R = new double[mask.rows()][mask.cols()];
        double[][] G = new double[mask.rows()][mask.cols()];
        double[][] B = new double[mask.rows()][mask.cols()];
        int[] length_row = new int[src.rows()];
        int[] length_col = new int[src.cols()];
        Arrays.fill(length_row, 0);
        Arrays.fill(length_col, 0);
        if(flag == 1){
            flag+=1;
            for(int r = 0; r < mask.rows(); ++r){
                for(int c = 0; c < mask.cols(); ++c){
                    double[] data = mask.get(r,c);
                    double[] data1 = src.get(r,c);
                    // double sum = DoubleStream.of(data).sum();
                    double sum = 0;
                    for(int channel = 0; channel < mask.channels(); channel++){
                        sum+=data[channel];
                        if(channel == 0)
                            R[r][c] = data1[channel];
                        else if(channel == 1)
                            G[r][c] = data1[channel];
                        else
                            B[r][c] = data1[channel];
                    }
                    if(sum < 1){
                        src.put(r,c,data);
                    }
                    else{
                        length_row[r] += 1;
                        length_col[c] += 1;
                    }
                }
            }
        }
        else{
            for(int r = 0; r < mask.rows(); ++r){
                for(int c = 0; c < mask.cols(); ++c){
                    double[] data = mask.get(r,c);
                    double[] data1 = src.get(r,c);
                    // double sum = DoubleStream.of(data).sum();
                    double sum = 0;
                    for(int channel = 0; channel < mask.channels(); channel++){
                        sum+=data[channel];
                        if(channel == 0)
                            R[r][c] = data1[channel];
                        else if(channel == 1)
                            G[r][c] = data1[channel];
                        else
                            B[r][c] = data1[channel];
                    }
                    if(sum < 1){
                        src.put(r,c,data);
                    }
                    else{
                        length_row[r] += 1;
                        length_col[c] += 1;
                    }
                }
            }
        }
        int[][] laplacian_matrix_ = new int[mask.cols()][mask.cols()];
        for(int i = 0; i < mask.cols() - 1; ++i){
            laplacian_matrix_[i][i+1] = -1;
        }
        for(int i = 0; i < mask.cols()-1; ++i){
            laplacian_matrix_[i+1][i] = -1;
        }
        for(int i = 0; i < mask.cols(); ++i){
            laplacian_matrix_[i][i] = 4;
        }
//        for(int i = 0; i < mask.cols() - mask.rows(); i++){
//            laplacian_matrix_[i][i+ mask.rows()] = -1;
//        }
//        for(int i = 0; i < mask.cols() - mask.rows(); i++) {
//            laplacian_matrix_[i + mask.rows()][i] = -1;
//        }
        double[][] matr = new double[mask.rows()][mask.cols()];
        double[][] matg = new double[mask.rows()][mask.cols()];
        double[][] matb = new double[mask.rows()][mask.cols()];
        double[][] mat_gray = new double[mask.rows()][mask.cols()];
        for(int row = 0; row < mask.rows(); row++){
            for(int col = 0; col < mask.cols(); col++){
                for(int s = 0; s < mask.cols(); s++) {
                    matr[row][col] += (R[row][s] * laplacian_matrix_[s][col]);
                    matg[row][col] += (G[row][s] * laplacian_matrix_[s][col]);
                    matb[row][col] += (B[row][s] * laplacian_matrix_[s][col]);
                }
            }
        }

        for(int row = 0; row < mask.rows(); row++){
            for(int col = 0; col < mask.cols(); col++){
                mat_gray[row][col] = 0.299 + R[row][col] + 0.587 * G[row][col] + 0.114 * B[row][col];
            }
        }

        double prob[] = new double[30];
        prob[0] = 0.001;
        prob[1] = 0.011;
        prob[2] = 0.021;
        prob[3] = 0.031;
        prob[4] = 0.153;
        prob[5] = 0.173;
        prob[6] = 0.291;
        prob[7] = 0.306;
        prob[8] = 0.621;
        prob[9] = 0.731;
        prob[10] = 0.856;
        prob[11] = 0.936;
        prob[12] = 0.936;
        prob[13] = 0.941;
        prob[14] = 0.953;
        prob[15] = 0.953;
        prob[16] = 0.941;
        prob[17] = 0.936;
        prob[18] = 0.936;
        prob[19] = 0.856;
        prob[20] = 0.731;
        prob[21] = 0.621;
        prob[22] = 0.306;
        prob[23] = 0.291;
        prob[24] = 0.173;
        prob[25] = 0.153;
        prob[26] = 0.031;
        prob[27] = 0.021;
        prob[28] = 0.011;
        prob[29] = 0.001;

        for(int row = 0; row < mask.rows(); row++) {
            double row_index = 0;
            int div;
            for (int col = 0; col < mask.cols(); col++) {
                if(length_row[row] == 0) continue;
                div = (int) ((row_index / length_row[row]) * 30);
                double sum = matr[row][col] + matg[row][col] + matb[row][col];
                matr[row][col] = matr[row][col] / sum * 255;
                matg[row][col] = matg[row][col] / sum * 255;
                matb[row][col] = matb[row][col] / sum * 255;
                double[] data = new double[3];
                data[0] = Math.sqrt(matr[row][col]);
                data[1] = Math.sqrt(matg[row][col]);
                data[2] = Math.sqrt(matb[row][col]);
//                data[0] = matr[row][col];
//                data[1] = matg[row][col];
//                data[2] = matb[row][col];
                if (src.get(row, col)[0] != 0)
                    data[0] = matr[row][col] * (1-prob[div]) + src.get(row, col)[0] * prob[div];
                else
                    data[0] = 0;
                if (src.get(row, col)[1] != 0)
                    data[1] = matg[row][col] * (1-prob[div]) + src.get(row, col)[1] * prob[div];
                else
                    data[1] = 0;
                if (src.get(row, col)[2] != 0)
                    data[2] = matb[row][col] * (1-prob[div]) + src.get(row, col)[2] * prob[div];
                else
                    data[2] = 0;
                if (data[0] > 0 && data[1] > 0 && data[2] > 0) {
                    row_index+=1;
                    src.put((int) (row), (int) (col), data);
                }
            }
        }

        dst.copyTo(blend);

        for(int row = 0; row < mask.rows(); row++){
            double row_index = 0;
            int div;
            for(int col = 0; col < mask.cols(); col++){
                if(length_row[row] == 0) continue;
                double[] data = src.get(row, col);
                double[] daa = dst.get((int) (row + p.y - src.rows()/2),(int) (col + p.x - src.cols()/2));
                div = (int) ((row_index / length_row[row]) * 30);
                if(src.get(row,col)[0] != 0){
                    data[0] = daa[0] * (1-prob[div]) + data[0] * prob[div];
                }
                else
                    data[0] = 0;
                if(src.get(row,col)[1] != 0){
                    data[1] = daa[1] * (1-prob[div]) + data[1] * prob[div];
                }
                else
                    data[1] = 0;
                if(src.get(row,col)[2] != 0){
                    data[2] = daa[2] * (1-prob[div]) + data[2] * prob[div];
                }
                else
                    data[2] = 0;

                if(data[0] > 0 && data[1] > 0 && data[2] > 0){
                    row_index+=1;
                    dst.put((int) (row + p.y - src.rows()/2), (int) (col + p.x - src.cols()/2), data);
                }
            }
        }


        for(int col = 0; col < mask.cols(); col++){
            double col_index = 0;
            int div;
            for(int row = 0; row < mask.rows(); row++){
                if(length_col[col] == 0) continue;
                double[] data = src.get(row, col);
                double[] daa = blend.get((int) (row + p.y - src.rows()/2),(int) (col + p.x - src.cols()/2));
                div = (int) ((col_index / length_col[col]) * 30);
                if(src.get(row,col)[0] != 0){
                    data[0] = daa[0] * (1-prob[div]) + data[0] * prob[div];
                }
                else
                    data[0] = 0;
                if(src.get(row,col)[1] != 0){
                    data[1] = daa[1] * (1-prob[div]) + data[1] * prob[div];
                }
                else
                    data[1] = 0;
                if(src.get(row,col)[2] != 0){
                    data[2] = daa[2] * (1-prob[div]) + data[2] * prob[div];
                }
                else
                    data[2] = 0;

                if(data[0] > 0 && data[1] > 0 && data[2] > 0){
                    col_index+=1;
                    blend.put((int) (row + p.y - src.rows()/2), (int) (col + p.x - src.cols()/2), data);
                }
            }
        }

        for(int row = (int) (blend.rows()/2 - src.rows()/2 - 1); row < ((blend.rows() / 2) + src.rows()/2 + 1); row++){
            for(int col = (int) (blend.cols()/2 - src.cols()/2 - 1); col < ((blend.cols() / 2) + src.cols()/2 + 1); col++){
                double[] data0 = dst.get(row, col);
                double[] data1 = blend.get(row, col);
                if(data0[0] == data1[0] && data1[1] == data1[1] && data0[2] == data1[2]) {
                    continue;
                }
                else{
                    for(int i = 0; i < 3; ++i){
                        blend.put(row, col, (data0[0] + data1[0]) / 2, (data0[1] + data1[1]) / 2, (data0[2] + data1[2]) / 2);
                    }
                }
            }
        }

        return blend;
    }


    public int[] conv2(byte[] data, int width, int height, double kernel[][]){
        // 0 is black and 255 is white.
        int size = height * width;
        int[] convData = new int[size];

        // Perform single channel 2D Convolution
        // Note that you only need to manipulate data[0:size] that corresponds to luminance
        // The rest data[size:data.length] is ignored since we only want grayscale output
        // *********************** START YOUR CODE HERE  **************************** //
        for(int y=0; y<height; y++){
            for(int x=0; x<width; x++){
                double sum=0;
                for(int i=-1; i<2; i++){
                    for(int j=-1; j<2; j++){
                        if(y+i>=0 && y+i<height && x+j>=0 && x+j<=width){
                            sum += kernel[i+1][j+1]*((double)data[(y+i)*width + x+j]);
                        }
                    }
                }
                convData[y*width+x] = (byte) sum;
            }
        }


        // *********************** End YOUR CODE HERE  **************************** //
        return convData;
    }

    public Mat upSample_Image_l(int U, Mat src){
        Mat result;
        result = Mat.zeros(src.rows() * U, src.cols() * U, src.type());
        for(int row = 0; row < src.rows(); row++){
            for(int col = 0; col < src.cols(); col++){
                double[] data = src.get(row, col);
                result.put(row*U, col*U, data);
            }
        }
        double[] a_i;
        double[] a_j;
        double[] a_j_minus_a_i = new double[3];
        int rows = result.rows();
        int cols = result.cols();
        double[] add_new = new double[3];
        int index, index2;
        for(int i = 0; i < rows; ++i){
            for(int j = 0; j < cols; ++j){
                int delta = j - U * (int)(j/U);
                if((U*(int)(j/U)+U) >= cols){
                    a_j = new double[]{0, 0, 0};
                }
                else{
                    index = (U)*(int)(j/U) + U;
                    a_j = result.get(i, index);
                }
                index2 = (U)*(int)(j/U);
                a_i = result.get(i, index2);
                for(int p = 0; p < 3; p++){
                    a_j_minus_a_i[p] = a_j[p] - a_i[p];
                }
                for(int p = 0; p < 3; p++){
                    add_new[p] = delta * a_j_minus_a_i[p] / U + a_i[p];
                }
                result.put(i, j, add_new);
            }
        }
        for(int i = 0; i < cols; i++){
            for(int j = 0; j < rows; j++){
                int delta = j - U * (int)(j/U);
                if((U*(int)(j/U)+U) >= rows){
                    a_j = new double[]{0, 0, 0};
                }
                else{
                    index = (U)*(int)(j/U) + U;
                    a_j = result.get(index, i);
                }
                index2 = (U)*(int)(j/U);
                a_i = result.get(index2, i);
                for(int p = 0; p < 3; p++){
                    a_j_minus_a_i[p] = a_j[p] - a_i[p];
                }
                for(int p = 0; p < 3; p++){
                    add_new[p] = delta * a_j_minus_a_i[p] / U + a_i[p];
                }
                result.put(j, i, add_new);
            }
        }
        return result;
    }


}